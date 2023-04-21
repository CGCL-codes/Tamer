package org.internna.ossmoney.mvc;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Currency;
import java.util.ArrayList;
import java.math.BigDecimal;
import org.internna.ossmoney.model.Bill;
import org.internna.ossmoney.model.Account;
import org.internna.ossmoney.util.DateUtils;
import org.internna.ossmoney.cache.CacheStore;
import org.internna.ossmoney.model.Subcategory;
import org.internna.ossmoney.model.support.Interval;
import org.internna.ossmoney.model.AccountTransaction;
import org.internna.ossmoney.model.security.UserDetails;
import org.internna.ossmoney.model.support.IncomeExpense;
import org.internna.ossmoney.model.support.NameValuePair;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.util.StringUtils.hasText;

@Controller
@RequestMapping("/financial/widgets")
public final class WidgetController extends AbstractTransactionController {

    @Autowired
    private CacheStore cache;

    protected void setCache(CacheStore cache) {
        this.cache = cache;
    }

    @RequestMapping("/income_vs_expenses/{intervals}")
    public String incomeVsExpenses(@PathVariable String intervals, ModelMap modelMap) {
        UserDetails user = getCurrentUser();
        String interval = hasText(intervals) ? intervals.toUpperCase() : Interval.Intervals.PREVIOUS_MONTH.toString();
        Map<Currency, NameValuePair<BigDecimal, BigDecimal>> data = cache.getIncomeAndExpenses(user, intervals);
        if (data == null) {
            List<AccountTransaction> transactions = getTransactions(user, intervals, null, modelMap);
            data = calculateIncomeAndExpenses(transactions);
            cache.storeIncomeAndExpenses(user, intervals, data);
        }
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("interval", interval);
        modelMap.addAttribute("max", getMaxValue(data));
        modelMap.addAttribute("dataSet", data.entrySet());
        return "widgets/incomevsexpenses";
    }

    protected Map<Currency, NameValuePair<BigDecimal, BigDecimal>> calculateIncomeAndExpenses(List<AccountTransaction> transactions) {
        Map<Currency, NameValuePair<BigDecimal, BigDecimal>> data = new HashMap<Currency, NameValuePair<BigDecimal, BigDecimal>>();
        if (!CollectionUtils.isEmpty(transactions)) {
            for (AccountTransaction transaction : transactions) {
                BigDecimal amount = transaction.getAmount();
                Currency currency = Currency.getInstance(transaction.getAccount().getLocale());
                if (!data.containsKey(currency)) {
                    data.put(currency, new NameValuePair<BigDecimal, BigDecimal>(BigDecimal.ZERO, BigDecimal.ZERO));
                }
                NameValuePair<BigDecimal, BigDecimal> incomeExpense = data.get(currency);
                if (amount.doubleValue() >= 0) {
                    incomeExpense.setKey(amount.add(incomeExpense.getKey()));
                } else {
                    incomeExpense.setValue(amount.abs().add(incomeExpense.getValue()));
                }
                data.put(currency, incomeExpense);
            }
        }
        return data;
    }

    protected Long getMaxValue(Map<Currency, NameValuePair<BigDecimal, BigDecimal>> data) {
        Long max = 0L;
        for (Map.Entry<Currency, NameValuePair<BigDecimal, BigDecimal>> dataRow : data.entrySet()) {
            NameValuePair<BigDecimal, BigDecimal> incomeExpense = dataRow.getValue();
            max = Math.max(max, Math.max(incomeExpense.getKey().intValue(), incomeExpense.getValue().intValue()));
        }
        return Math.round(max * 1.1);
    }

    @RequestMapping("/expenses_by_category/{intervals}")
    public String expensesByCategory(@PathVariable String intervals, ModelMap modelMap) {
        UserDetails user = getCurrentUser();
        Interval interval = getInterval(intervals);
        Map<Currency, Map<Subcategory, BigDecimal>> data = cache.getExpensesByCategory(user, intervals);
        if (data == null) {
            List<AccountTransaction> transactions = getTransactions(interval, null, modelMap);
            data = organizeByCategory(transactions);
            cache.storeExpensesByCategory(user, intervals, data);
        }
        modelMap.addAttribute("data", data);
        Map<Currency, Set<Map.Entry<Subcategory, BigDecimal>>> dataAsSet = new HashMap<Currency, Set<Map.Entry<Subcategory, BigDecimal>>>();
        for (Currency currency : data.keySet()) {
            dataAsSet.put(currency, data.get(currency).entrySet());
        }
        modelMap.addAttribute("interval", interval);
        modelMap.addAttribute("dataSet", dataAsSet.entrySet());
        return "widgets/expenses_by_category";
    }

    protected Map<Currency, Map<Subcategory, BigDecimal>> organizeByCategory(List<AccountTransaction> transactions) {
        Map<Currency, Map<Subcategory, BigDecimal>> data = new HashMap<Currency, Map<Subcategory, BigDecimal>>();
        if (!CollectionUtils.isEmpty(transactions)) {
            for (AccountTransaction transaction : transactions) {
                Subcategory category = transaction.getSubcategory();
                if (!category.getParentCategory().isIncome()) {
                    BigDecimal amount = transaction.getAmount();
                    Currency currency = Currency.getInstance(transaction.getAccount().getLocale());
                    if (!data.containsKey(currency)) {
                        data.put(currency, new HashMap<Subcategory, BigDecimal>());
                    }
                    if (!data.get(currency).containsKey(category)) {
                        data.get(currency).put(category, BigDecimal.ZERO);
                    }
                    BigDecimal expenses = data.get(currency).get(category);
                    data.get(currency).put(category, expenses.add(amount.abs()));
                }
            }
        }
        return data;
    }

    @RequestMapping("/expenses_over_time/{months}")
    public String expensesOverTime(@PathVariable int months, ModelMap modelMap) {
        UserDetails user = getCurrentUser();
        Date[] dates = DateUtils.dates(months);
        Map<Currency, Map<Date, IncomeExpense>> data = cache.getIncomeVsExpensesOverTime(user, months);
        if (data == null) {
            data = calculateIncomeVsExpensesOverTime(dates, getAccounts());
            cache.storeIncomeVsExpensesOverTime(user, months, data);
        }
        fillModel(dates, data, modelMap);
        return "widgets/expensesovertime";
    }

    @RequestMapping("/networth/{months}")
    public String netWorthOverTime(@PathVariable int months, ModelMap modelMap) {
        UserDetails user = getCurrentUser();
        Date[] dates = DateUtils.dates(months);
        Map<Currency, Map<Date, IncomeExpense>> data = cache.getNetWorthOverTime(user, months);
        if (data == null) {
            data = calculateWealth(dates, getAccounts());
            cache.storeNetWorthOverTime(user, months, data);
        }
        fillModel(dates, data, modelMap);
        return "widgets/networth";
    }

    @RequestMapping("/income_vs_expenses_over_time/{months}")
    public String incomeVsExpensesOverTime(@PathVariable int months, ModelMap modelMap) {
        UserDetails user = getCurrentUser();
        Date[] dates = DateUtils.dates(months);
        Map<Currency, Map<Date, IncomeExpense>> data = cache.getIncomeVsExpensesOverTime(user, months);
        if (data == null) {
            data = calculateIncomeVsExpensesOverTime(dates, getAccounts());
            cache.storeIncomeVsExpensesOverTime(user, months, data);
        }
        fillModel(dates, data, modelMap);
        return "widgets/incomevsexpensesovertime";
    }

    private void fillModel(Date[] dates, Map<Currency, Map<Date, IncomeExpense>> data, ModelMap modelMap) {
        Map<Currency, BigDecimal> totalMax = new HashMap<Currency, BigDecimal>();
        Map<Currency, IncomeExpense> maxValues = new HashMap<Currency, IncomeExpense>();
        Map<Currency, List<NameValuePair<Date, IncomeExpense>>> dataAsSet = new HashMap<Currency, List<NameValuePair<Date, IncomeExpense>>>();
        for (Currency currency : data.keySet()) {
            IncomeExpense max = maxValues.containsKey(currency) ? maxValues.get(currency) : new IncomeExpense();
            List<NameValuePair<Date, IncomeExpense>> orderedData = new ArrayList<NameValuePair<Date, IncomeExpense>>();
            for (Date date : dates) {
                IncomeExpense value = data.get(currency).get(date);
                max.setKey(max.getKey().doubleValue() > value.getKey().doubleValue() ? max.getKey() : value.getKey());
                max.setValue(max.getValue().doubleValue() > value.getValue().doubleValue() ? max.getValue() : value.getValue());
                orderedData.add(new NameValuePair<Date, IncomeExpense>(date, value));
            }
            maxValues.put(currency, max);
            dataAsSet.put(currency, orderedData);
        }
        for (Map.Entry<Currency, IncomeExpense> entry : maxValues.entrySet()) {
            totalMax.put(entry.getKey(), entry.getValue().getKey().max(entry.getValue().getValue()));
        }
        modelMap.addAttribute("maxValues", maxValues);
        modelMap.addAttribute("totalMaxValue", totalMax);
        modelMap.addAttribute("dataSet", dataAsSet.entrySet());
    }

    protected Map<Currency, Map<Date, IncomeExpense>> calculateWealth(Date[] dates, Set<Account> accounts) {
        Map<Currency, Map<Date, IncomeExpense>> wealthOverTimeAndCurrency = new HashMap<Currency, Map<Date, IncomeExpense>>();
        if (!CollectionUtils.isEmpty(accounts)) {
            for (Account account : accounts) {
                Currency currency = Currency.getInstance(account.getLocale());
                if (!wealthOverTimeAndCurrency.containsKey(currency)) {
                    wealthOverTimeAndCurrency.put(currency, new HashMap<Date, IncomeExpense>());
                }
                Map<Date, IncomeExpense> wealthOverTime = wealthOverTimeAndCurrency.get(currency);
                for (Date month : dates) {
                    if (!wealthOverTime.containsKey(month)) {
                        wealthOverTime.put(month, new IncomeExpense());
                    }
                    IncomeExpense data = wealthOverTime.get(month);
                    data.setKey(data.getKey().add(account.calculateBalance(month)));
                    wealthOverTime.put(month, data);
                }
            }
        }
        return wealthOverTimeAndCurrency;
    }

    protected Map<Currency, Map<Date, IncomeExpense>> calculateIncomeVsExpensesOverTime(Date[] dates, Set<Account> accounts) {
        Map<Currency, Map<Date, IncomeExpense>> expensesOverTimeAndCurrency = new HashMap<Currency, Map<Date, IncomeExpense>>();
        if (!CollectionUtils.isEmpty(accounts)) {
            for (Account account : accounts) {
                Currency currency = Currency.getInstance(account.getLocale());
                if (!expensesOverTimeAndCurrency.containsKey(currency)) {
                    expensesOverTimeAndCurrency.put(currency, new HashMap<Date, IncomeExpense>());
                }
                Map<Date, IncomeExpense> incomeExpensesOverTime = expensesOverTimeAndCurrency.get(currency);
                for (Date month : dates) {
                    if (!incomeExpensesOverTime.containsKey(month)) {
                        incomeExpensesOverTime.put(month, new IncomeExpense());
                    }
                    IncomeExpense monthIncomeExpenses = incomeExpensesOverTime.get(month);
                    List<AccountTransaction> transactions = account.getTransactionsInPeriod(new Interval(DateUtils.getMonthStartDate(month), month), null, true);
                    if (!CollectionUtils.isEmpty(transactions)) {
                        for (AccountTransaction transaction : transactions) {
                            BigDecimal amount = transaction.getAmount();
                            if (amount.doubleValue() >= 0) {
                                monthIncomeExpenses.setKey(monthIncomeExpenses.getKey().add(amount.abs()));
                            } else {
                                monthIncomeExpenses.setValue(monthIncomeExpenses.getValue().add(amount.abs()));
                            }
                        }
                    }
                    incomeExpensesOverTime.put(month, monthIncomeExpenses);
                }
            }
        }
        return expensesOverTimeAndCurrency;
    }

    @RequestMapping("/remainder/{days}")
    public String remainders(@PathVariable int days, ModelMap modelMap) {
        UserDetails user = UserDetails.findCurrentUser();
        modelMap.addAttribute("bills", Bill.findPending(user, days));
        return "widgets/remainders";
    }
}

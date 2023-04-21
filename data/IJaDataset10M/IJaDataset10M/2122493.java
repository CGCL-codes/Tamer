package com.tysanclan.site.projectewok.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import nl.topicus.wqplot.data.BaseSeries;
import nl.topicus.wqplot.data.DateNumberSeries;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;
import com.jeroensteenbeeke.hyperion.util.MapUtil;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.Donation;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.DonationDAO;
import com.tysanclan.site.projectewok.entities.dao.ExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.DonationFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.ExpenseFilter;

/**
 * @author Jeroen Steenbeeke
 */
public class DonationTimeline {

    private static class MutationKey implements Comparable<MutationKey> {

        private final Date date;

        private int index;

        public MutationKey(Date date, int index) {
            this.date = date;
            this.index = 1;
        }

        public MutationKey(Date date, Map<MutationKey, ?> curr) {
            this.date = date;
            this.index = 1;
            while (curr.containsKey(this)) {
                index++;
            }
        }

        public Date getDate() {
            return date;
        }

        public Integer getIndex() {
            return index;
        }

        @Override
        public int compareTo(MutationKey k) {
            int dc = getDate().compareTo(k.getDate());
            if (dc == 0) return getIndex().compareTo(k.getIndex());
            return dc;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((date == null) ? 0 : date.hashCode());
            result = prime * result + index;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            MutationKey other = (MutationKey) obj;
            if (date == null) {
                if (other.date != null) return false;
            } else if (!date.equals(other.date)) return false;
            if (index != other.index) return false;
            return true;
        }
    }

    private static class Reserve {

        private final User user;

        private BigDecimal amount;

        private final Date date;

        private Reserve(User user, BigDecimal amount, Date date) {
            this.user = user;
            this.amount = amount;
            this.date = date;
        }

        /**
		 * @return the amount
		 */
        public BigDecimal getAmount() {
            return amount;
        }

        public BigDecimal consume(BigDecimal subtrahend) {
            if (subtrahend.compareTo(amount) > 0) {
                this.amount = BigDecimal.ZERO;
                return subtrahend.subtract(this.amount);
            }
            this.amount = this.amount.subtract(subtrahend);
            return BigDecimal.ZERO;
        }

        /**
		 * @return the user
		 */
        public User getUser() {
            return user;
        }

        /**
		 * @see java.lang.Object#hashCode()
		 */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((date == null) ? 0 : date.hashCode());
            result = prime * result + ((user == null) ? 0 : user.hashCode());
            return result;
        }

        /**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Reserve other = (Reserve) obj;
            if (date == null) {
                if (other.date != null) {
                    return false;
                }
            } else if (!date.equals(other.date)) {
                return false;
            }
            if (user == null) {
                if (other.user != null) {
                    return false;
                }
            } else if (!user.equals(other.user)) {
                return false;
            }
            return true;
        }

        /**
		 * @see java.lang.Object#toString()
		 */
        @Override
        public String toString() {
            return amount.toString() + " by " + user.getUsername();
        }
    }

    private List<Reserve> reserves;

    @SpringBean
    private ExpenseDAO expenseDAO;

    @SpringBean
    private DonationDAO donationDAO;

    @SpringBean
    private RoleService roleService;

    private final Date start;

    private final Date end;

    private final SortedMap<MutationKey, Object> mutations;

    public DonationTimeline(Date _start, Date end) {
        this.start = _start;
        this.end = end;
        InjectorHolder.getInjector().inject(this);
        mutations = new TreeMap<MutationKey, Object>();
        reserves = new LinkedList<Reserve>();
        DonationFilter filter = new DonationFilter();
        filter.setTo(end);
        List<Donation> donations = donationDAO.findByFilter(filter);
        for (Donation d : donations) {
            mutations.put(new MutationKey(d.getDonationTime(), mutations), d);
        }
        ExpenseFilter filter2 = new ExpenseFilter();
        filter2.setTo(end);
        List<Expense> expenses = expenseDAO.findByFilter(filter2);
        for (Expense e : expenses) {
            Date curr = e.getStart();
            while (curr.before(end) && (e.getEnd() == null || curr.before(e.getEnd()))) {
                mutations.put(new MutationKey(curr, mutations), e);
                curr = e.getPeriod().nextDate(curr);
            }
        }
        if (!mutations.isEmpty()) {
            Set<MutationKey> _mutations = mutations.keySet();
            for (MutationKey curr : _mutations) {
                Object mut = mutations.get(curr);
                if (Hibernate.getClass(mut) == Donation.class) {
                    Donation donation = (Donation) mut;
                    reserves.add(new Reserve(donation.getDonator(), donation.getAmount(), donation.getDonationTime()));
                } else if (Hibernate.getClass(mut) == Expense.class) {
                    Expense expense = (Expense) mut;
                    if (!reserves.isEmpty()) {
                        BigDecimal remainder = expense.getAmount();
                        while (remainder.compareTo(BigDecimal.ZERO) > 0) {
                            if (!reserves.isEmpty()) {
                                remainder = reserves.get(0).consume(remainder);
                                if (reserves.get(0).getAmount().equals(BigDecimal.ZERO)) {
                                    reserves.remove(0);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public DateNumberSeries<BigDecimal> getExpenses() {
        DateNumberSeries<BigDecimal> series = new DateNumberSeries<BigDecimal>();
        if (!mutations.isEmpty()) {
            SortedMap<MutationKey, Object> _mutations = mutations.headMap(new MutationKey(end, Integer.MAX_VALUE));
            if (start != null) {
                _mutations = mutations.tailMap(new MutationKey(start, 1));
                series.addEntry(start, BigDecimal.ZERO);
            }
            series.addEntry(end, BigDecimal.ZERO);
            Map<Date, BigDecimal> totals = new HashMap<Date, BigDecimal>();
            for (MutationKey curr : _mutations.keySet()) {
                Object next = _mutations.get(curr);
                if (next.getClass() == Expense.class) {
                    Expense ex = (Expense) next;
                    MapUtil.bdAdd(totals, curr.getDate(), ex.getAmount());
                }
            }
            for (Entry<Date, BigDecimal> e : totals.entrySet()) {
                series.addEntry(e.getKey(), e.getValue());
            }
        }
        return series;
    }

    public DateNumberSeries<BigDecimal> getDonations() {
        DateNumberSeries<BigDecimal> series = new DateNumberSeries<BigDecimal>();
        if (!mutations.isEmpty()) {
            SortedMap<MutationKey, Object> _mutations = mutations.headMap(new MutationKey(end, Integer.MAX_VALUE));
            if (start != null) {
                _mutations = mutations.tailMap(new MutationKey(start, 1));
                series.addEntry(start, BigDecimal.ZERO);
            }
            series.addEntry(end, BigDecimal.ZERO);
            Map<Date, BigDecimal> totals = new HashMap<Date, BigDecimal>();
            for (MutationKey curr : _mutations.keySet()) {
                Object next = _mutations.get(curr);
                if (next.getClass() == Donation.class) {
                    Donation d = (Donation) next;
                    MapUtil.bdAdd(totals, curr.getDate(), d.getAmount());
                }
            }
            for (Entry<Date, BigDecimal> e : totals.entrySet()) {
                series.addEntry(e.getKey(), e.getValue());
            }
        }
        return series;
    }

    public BaseSeries<String, BigDecimal> getParticipation() {
        if (roleService == null) {
            InjectorHolder.getInjector().inject(this);
        }
        BaseSeries<String, BigDecimal> participation = new BaseSeries<String, BigDecimal>();
        Map<String, BigDecimal> values = new HashMap<String, BigDecimal>();
        User caretaker = roleService.getCaretaker();
        if (!mutations.isEmpty()) {
            MutationKey _end = new MutationKey(end, Integer.MAX_VALUE);
            SortedMap<MutationKey, Object> _mutations = mutations.headMap(_end);
            if (start != null) {
                MutationKey _start = new MutationKey(start, 1);
                _mutations = _mutations.tailMap(_start);
            }
            List<Reserve> _reserves = new LinkedList<Reserve>();
            _reserves.addAll(reserves);
            Map<User, BigDecimal> amounts = new HashMap<User, BigDecimal>();
            for (MutationKey curr : _mutations.keySet()) {
                Object next = _mutations.get(curr);
                if (next.getClass() == Expense.class) {
                    Expense ex = (Expense) next;
                    BigDecimal total = ex.getAmount();
                    while (total.compareTo(BigDecimal.ZERO) > 0) {
                        if (_reserves.isEmpty()) {
                            if (amounts.containsKey(caretaker)) {
                                amounts.put(caretaker, amounts.get(caretaker).add(total));
                            } else {
                                amounts.put(caretaker, total);
                            }
                            total = BigDecimal.ZERO;
                        } else {
                            BigDecimal amt = BigDecimal.ZERO;
                            User user = null;
                            if (_reserves.get(0).getAmount().compareTo(total) >= 0) {
                                amt = total;
                                total = BigDecimal.ZERO;
                                user = _reserves.get(0).getUser();
                                _reserves.get(0).consume(amt);
                            } else {
                                Reserve last = _reserves.remove(0);
                                amt = last.getAmount();
                                user = last.getUser();
                                total = total.subtract(last.getAmount());
                            }
                            if (amounts.containsKey(user)) {
                                amounts.put(user, amounts.get(user).add(amt));
                            } else {
                                amounts.put(user, amt);
                            }
                        }
                    }
                } else if (next.getClass() == Donation.class) {
                    Donation d = (Donation) next;
                    _reserves.add(new Reserve(d.getDonator(), d.getAmount(), d.getDonationTime()));
                }
            }
            for (User user : amounts.keySet()) {
                BigDecimal amount = amounts.get(user);
                MapUtil.bdAdd(values, user != null ? user.getUsername() : "Anonymous", amount);
            }
        }
        for (Entry<String, BigDecimal> e : values.entrySet()) {
            participation.addEntry(e.getKey(), e.getValue());
        }
        return participation;
    }

    public Map<User, BigDecimal> getRemainingReserves() {
        Map<User, BigDecimal> retval = new HashMap<User, BigDecimal>();
        for (Reserve r : reserves) {
            User u = r.getUser();
            BigDecimal amount = r.getAmount();
            if (retval.containsKey(u)) {
                retval.put(u, retval.get(u).add(amount));
            } else {
                retval.put(u, amount);
            }
        }
        return retval;
    }
}

package org.nomadpim.module.money.calculation;

import org.eclipse.swt.graphics.RGB;
import org.nomadpim.core.entity.EntityPropertyFilter;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.util.filter.EqualsFilter;
import org.nomadpim.core.util.filter.IFilter;
import org.nomadpim.core.util.operations.IIntegerOperation;
import org.nomadpim.core.util.properties.IReadeablePropertyMap;
import org.nomadpim.core.util.text.IFormatter;
import org.nomadpim.module.money.MoneyFacade;
import org.nomadpim.module.money.calculator.ISumCalculatorRepository;
import org.nomadpim.module.money.transaction.Transaction;
import org.nomadpim.module.money.transaction.TransactionByFromFilter;
import org.nomadpim.module.money.transaction.TransactionByToFilter;

public class AmountCellContentProvider implements ICellContentProvider {

    private final IIntegerOperation calculator;

    private final IFilter<IEntity> filter;

    private final IFormatter<Integer> formatter;

    private RGB red = new RGB(255, 0, 0);

    private ISumCalculatorRepository sumCalculator;

    private IEntity currency;

    public AmountCellContentProvider(ISumCalculatorRepository sumCalculator, IFilter<IEntity> filter, IFormatter<Integer> formatter, IIntegerOperation calculator, IEntity currency) {
        if (sumCalculator == null) {
            throw new IllegalArgumentException();
        }
        this.sumCalculator = sumCalculator;
        this.filter = filter;
        this.calculator = calculator;
        this.currency = currency;
        this.formatter = formatter;
    }

    private int calculateAmount(RowData rowdata, IEntity currency) {
        IFilter<IEntity>[] preFilter = new IFilter[] { new EntityPropertyFilter<IEntity>(Transaction.CURRENCY, new EqualsFilter<IEntity>(currency)), filter, rowdata.getTransactionFilter() };
        IFilter<IEntity> accountFilter = rowdata.getAccountFilter();
        IFilter<IReadeablePropertyMap> fromFilter = new TransactionByFromFilter(accountFilter);
        IFilter<IReadeablePropertyMap> toFilter = new TransactionByToFilter(accountFilter);
        int value = sumCalculator.getCalculator(MoneyFacade.createCompositeFilter(preFilter), fromFilter, toFilter, currency).getSum();
        return calculator.calculate(value);
    }

    public IIntegerOperation getCalculator() {
        return calculator;
    }

    public String getCellContent(RowData rowdata) {
        return formatter.format(calculateAmount(rowdata, currency));
    }

    public RGB getCellForeground(RowData rowData) {
        return (calculateAmount(rowData, currency) < 0) ? red : null;
    }

    public IFilter<IEntity> getFilter() {
        return filter;
    }

    public IFormatter<Integer> getFormatter() {
        return formatter;
    }
}

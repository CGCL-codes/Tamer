package adc.budget.ui.web.wicket.transaction;

import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import adc.app.ui.wicket.BoListPanel;
import adc.app.ui.wicket.model.DomainObjectsDataProvider;
import adc.budget.spec.bo.ITransaction;
import adc.budget.spec.bs.ITransactionBs;
import adc.budget.ui.web.wicket.IMsgsMisc;
import com.google.common.collect.Lists;

public class TransactionListPanel extends BoListPanel<ITransaction> {

    private static final long serialVersionUID = 1L;

    public TransactionListPanel(String id) {
        super(id, new DomainObjectsDataProvider<ITransaction>(ITransactionBs.class, new SortParam("summary", true)));
    }

    @Override
    protected String[] modifLinkColumnProperty() {
        return new String[] { "summary", "summary" };
    }

    @Override
    protected IModel<String> modifLinkColumnTitle() {
        return new ResourceModel(IMsgsMisc.SUMMARY);
    }

    @Override
    protected List<IColumn<ITransaction>> additionalColumns() {
        List<IColumn<ITransaction>> l = Lists.newArrayListWithCapacity(3);
        l.add(new PropertyColumn<ITransaction>(new ResourceModel(IMsgsMisc.DATE), "date", "date"));
        l.add(new PropertyColumn<ITransaction>(new ResourceModel(IMsgsMisc.AMOUNT), "amount", "amount"));
        l.add(new PropertyColumn<ITransaction>(new ResourceModel(IMsgsMisc.SOURCE), "source.name", "source.name"));
        l.add(new PropertyColumn<ITransaction>(new ResourceModel(IMsgsMisc.DESTINATION), "destination.name", "destination.name"));
        return l;
    }
}

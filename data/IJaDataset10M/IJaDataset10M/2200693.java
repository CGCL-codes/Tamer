package cn.sh.fang.chenance.provider;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;
import cn.sh.fang.chenance.MainWindow;
import cn.sh.fang.chenance.data.dao.TransactionService;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;

public class BalanceSheetCellModifier implements ICellModifier {

    static final Logger LOG = Logger.getLogger(BalanceSheetCellModifier.class);

    TableViewer viewer;

    TransactionService ts = new TransactionService();

    private BalanceSheetContentProvider bsProv;

    private AccountContentProvider accountListProv;

    public BalanceSheetCellModifier(BalanceSheetContentProvider bsProv, TableViewer viewer, AccountContentProvider accountListProv) {
        this.viewer = viewer;
        this.bsProv = bsProv;
        this.accountListProv = accountListProv;
    }

    public boolean canModify(Object o, String prop) {
        if (((Transaction) o).getDate() == null) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public Object getValue(Object element, String property) {
        Column col = Column.valueOf(property);
        Object result = null;
        Transaction t = (Transaction) element;
        LOG.debug(col);
        switch(col) {
            case DATE:
                result = t.getDate();
                break;
            case CATEGORY:
                List<Category> l = (List<Category>) viewer.getData(MainWindow.CATEGORY_LIST);
                result = new Integer(l.indexOf(t.getCategory()));
                break;
            case DEBIT:
                result = t.getDebit() + "";
                break;
            case CREDIT:
                result = t.getCredit() + "";
                break;
            case DETAIL:
                result = t;
                break;
            default:
                result = "";
        }
        return result;
    }

    /**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
	 *      java.lang.String, java.lang.Object)
	 */
    public void modify(Object element, String property, Object value) {
        Column col = Column.valueOf(property);
        LOG.debug("modified column: " + col);
        TableItem item = (TableItem) element;
        Transaction t = (Transaction) item.getData();
        switch(col) {
            case DATE:
                t.setDate((Date) value);
                break;
            case CATEGORY:
                int i = (Integer) value;
                List<Category> l = (List<Category>) viewer.getData(MainWindow.CATEGORY_LIST);
                if (i < 0) {
                    i = 0;
                }
                t.setCategory(l.get(i));
                break;
            case DEBIT:
                try {
                    t.setDebit(Integer.valueOf((String) value));
                } catch (NumberFormatException e) {
                    t.setDebit(0);
                }
                break;
            case CREDIT:
                try {
                    t.setCredit(Integer.valueOf((String) value));
                } catch (NumberFormatException e) {
                    t.setCredit(0);
                }
                break;
            default:
        }
        bsProv.itemChanged(t);
        accountListProv.itemChanged(t.getAccount());
    }
}

package salto.test.fwk.mvc.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import salto.test.fwk.mvc.ajax.table.CustomerTestBean;

/**
 * @author p.mouawad / ubik-ingenierie.com
 *
 */
public class BoCustomer {

    private static final List CUSTOMERS = new ArrayList(23);

    static {
        for (int i = 0; i < 23; i++) {
            CustomerTestBean bean = new CustomerTestBean((long) i, "TOTO" + i, "TATA" + i, "TOTO" + i + "@google.fr", new Date());
            CUSTOMERS.add(bean);
        }
    }

    /**
     * 
     */
    public BoCustomer() {
        super();
    }

    /**
     * @return
     */
    public int getNumberOfCustomers() {
        return CUSTOMERS.size();
    }

    /**
     * @param rowStart
     * @param rowEnd
     * @return
     */
    public List getCustomers(int rowStart, int rowEnd) {
        return CUSTOMERS.subList(rowStart, rowEnd);
    }

    public List getSortedCustomers(int col2sort, int rowStart, int rowEnd) {
        List l = CUSTOMERS;
        Collections.sort(l, new BoCustomerComparator(col2sort));
        return l.subList(rowStart, rowEnd);
    }

    /**
     * @param inputData
     * @return
     */
    public CustomerTestBean[] getCustomersStartingWith(String inputData) {
        if (inputData != null && inputData.length() > 0) {
            List list = new ArrayList();
            for (int i = 0; i < CUSTOMERS.size(); i++) {
                CustomerTestBean bean = (CustomerTestBean) CUSTOMERS.get(i);
                if (bean.getName().startsWith(inputData)) {
                    list.add(bean);
                }
            }
            return (CustomerTestBean[]) list.toArray(new CustomerTestBean[list.size()]);
        }
        return (CustomerTestBean[]) CUSTOMERS.toArray(new CustomerTestBean[CUSTOMERS.size()]);
    }
}

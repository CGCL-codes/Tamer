package reports.utility.datamodel.sm;

/**
 *
 * @author Administrator
 */
public class SM_SUBSCRIPTION_INVOICE_KEY implements java.io.Serializable {

    /** Creates a new instance of SM_SUBSCRIPTION_INVOICE_KEY */
    public SM_SUBSCRIPTION_INVOICE_KEY() {
    }

    private Integer subscription_library_id;

    private Integer subscription_id;

    private String invoice_no;

    public Integer getSubscription_library_id() {
        return subscription_library_id;
    }

    public void setSubscription_library_id(Integer subscription_library_id) {
        this.subscription_library_id = subscription_library_id;
    }

    public Integer getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(Integer subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }
}

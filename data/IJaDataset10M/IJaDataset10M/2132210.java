package reports.utility.datamodel.administration;

/**

 *
 * @author Administrator
 */
public class CIR_TRANSACTION_KEY implements java.io.Serializable {

    /** Creates a new instance of CIR_TRANSACTION_KEY */
    public CIR_TRANSACTION_KEY() {
    }

    private Integer ta_id;

    private Integer library_id;

    public Integer getTa_id() {
        return ta_id;
    }

    public void setTa_id(Integer ta_id) {
        this.ta_id = ta_id;
    }

    public Integer getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(Integer library_id) {
        this.library_id = library_id;
    }
}

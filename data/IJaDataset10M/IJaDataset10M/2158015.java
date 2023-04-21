package ejb.objectmodel.circulation.bluelibrary;

import javax.persistence.Embeddable;

@Embeddable
public class LIBRARY_DEBIT_VOUCHER_CONFIGKey implements java.io.Serializable {

    private java.lang.Integer id;

    private java.lang.Integer library_Id;

    @Override
    public boolean equals(Object obj) {
        boolean retValue;
        retValue = super.equals(obj);
        return retValue;
    }

    @Override
    public int hashCode() {
        int retValue;
        retValue = super.hashCode();
        return retValue;
    }

    /**
     * @return the id
     */
    public java.lang.Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    /**
     * @return the library_Id
     */
    public java.lang.Integer getLibrary_Id() {
        return library_Id;
    }

    /**
     * @param library_Id the library_Id to set
     */
    public void setLibrary_Id(java.lang.Integer library_Id) {
        this.library_Id = library_Id;
    }
}

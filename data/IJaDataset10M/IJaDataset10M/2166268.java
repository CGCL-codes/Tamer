package ejb.objectmodel.circulation;

import javax.ejb.*;

/**
 * Created Jun 7, 2003 5:06:36 PM
 * Code generated by the Sun ONE Studio EJB Builder
 * @author vasu praveen
 */
public interface LocalCIR_BINDER_DOCUMENTS extends javax.ejb.EJBLocalObject {

    public abstract java.lang.Integer getLibrary_Id();

    public abstract java.lang.Integer getOrder_No();

    public abstract java.lang.String getAccession_Number();

    public abstract java.lang.Integer getBind_Type_Id();

    public abstract void setBind_Type_Id(java.lang.Integer bind_Type_Id);
}

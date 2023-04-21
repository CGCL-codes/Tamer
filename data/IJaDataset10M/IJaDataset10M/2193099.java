package x36dip.converter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import x36dip.sb.EmployeeSessionBeanLocal;
import x36dip.model.Maid;

/**
 *
 * @author Petr
 */
@FacesConverter(value = "maidConver")
public class MaidConverter implements Converter {

    EmployeeSessionBeanLocal employeeSessionBean = lookupEmployeeSessionBean();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals("") || value.equals("null")) {
            return null;
        } else {
            return employeeSessionBean.getMaid(Integer.valueOf(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(((Maid) value).getId_employee());
    }

    private EmployeeSessionBeanLocal lookupEmployeeSessionBean() {
        try {
            Context c = new InitialContext();
            return (EmployeeSessionBeanLocal) c.lookup("java:global/halaspet-x36dip/halaspet-x36dip-ejb/EmployeeSessionBean!x36dip.sb.EmployeeSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

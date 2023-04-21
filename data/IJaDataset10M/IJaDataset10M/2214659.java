package DAOs;

import beans.JobPromotion;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Ioana C
 */
public class JobPromotionConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        JobPromotionJpaController controller = (JobPromotionJpaController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "jobPromotionJpa");
        return controller.findJobPromotion(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof JobPromotion) {
            JobPromotion o = (JobPromotion) object;
            return o.getJobPromotionID() == null ? "" : o.getJobPromotionID().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.JobPromotion");
        }
    }
}

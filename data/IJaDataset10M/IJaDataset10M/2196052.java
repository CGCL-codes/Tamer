package ar.com.AmberSoft.iEvenTask.client.validaciones;

import java.util.Date;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

public class StarDateValidator implements Validator {

    Date starDate;

    Date endDate;

    public StarDateValidator(Date value) {
        starDate = value;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String validate(Field<?> field, String value) {
        try {
            endDate = (Date) field.getValue();
        } catch (Exception e) {
            return "Este campo solo admite fecha.";
        }
        try {
            if (endDate.after(starDate)) {
                return "La fecha de fin no puede ser anterior a la de comienzo.";
            }
        } catch (Exception e) {
            return e.toString();
        }
        return null;
    }
}

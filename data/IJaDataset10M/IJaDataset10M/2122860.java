package controllers.generic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import constants.Telefones;

/**
 * 
 * @author Flávio Gomes da Silva Lisboa
 * @version 0.1
 *
 */
public abstract class AbstractUsuarioMB extends AccessControllerMB {

    public AbstractUsuarioMB(boolean checkUser) {
        super(checkUser);
    }

    public List<SelectItem> getTiposTelefone() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        Field[] tipos = Telefones.class.getFields();
        for (int i = 0; i < tipos.length; i++) {
            Field field = tipos[i];
            String tipo = "";
            try {
                tipo = (String) field.get(null);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }
            items.add(new SelectItem(tipo, tipo));
        }
        return items;
    }
}

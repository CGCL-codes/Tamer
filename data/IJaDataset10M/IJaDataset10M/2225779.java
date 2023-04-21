package de.uni_leipzig.lots.webfrontend.formbeans;

import de.uni_leipzig.lots.webfrontend.formbeans.property.PersistentLongIdProperty;
import de.uni_leipzig.lots.webfrontend.formbeans.property.DateProperty;
import org.jetbrains.annotations.Nullable;

/**
 * @author Alexander Kiel
 * @version $Id: ThemeEditForm.java,v 1.4 2007/10/23 06:29:40 mai99bxd Exp $
 */
public class ThemeEditForm extends ThemeForm implements EditForm {

    public ThemeEditForm() {
        autoRegisterProperties(ThemeEditForm.class);
        id.setRequired(true);
        lastChange.setRequired(true);
    }

    protected PersistentLongIdProperty id = new PersistentLongIdProperty();

    @Nullable
    public String getId() {
        return id.getValue();
    }

    public void setId(String id) {
        this.id.setValue(id);
    }

    protected DateProperty lastChange = new DateProperty();

    @Nullable
    public String getLastChange() {
        return lastChange.getValue();
    }

    public void setLastChange(String lastChange) {
        this.lastChange.setValue(lastChange);
    }
}

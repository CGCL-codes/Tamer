package de.uni_leipzig.lots.webfrontend.formbeans;

import de.uni_leipzig.lots.webfrontend.formbeans.property.*;
import org.jetbrains.annotations.Nullable;

/**
 * @author Stephan Kühn
 * @author Alexander Kiel
 * @version $Id: GroupForm.java,v 1.10 2007/10/23 06:29:39 mai99bxd Exp $
 */
public abstract class GroupForm extends AutoValidateFormSupport {

    public GroupForm() {
        autoRegisterProperties(GroupForm.class);
        name.setRequired(true);
    }

    protected DefaultProperty name = new DefaultProperty(new MaxLengthValidator(255));

    @Nullable
    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    protected DefaultProperty note = new DefaultProperty();

    @Nullable
    public String getNote() {
        return note.getValue();
    }

    public void setNote(String note) {
        this.note.setValue(note);
    }

    protected PersistentLongIdProperty suspension = new PersistentLongIdProperty();

    @Nullable
    public String getSuspension() {
        return suspension.getValue();
    }

    public void setSuspension(String suspension) {
        this.suspension.setValue(suspension);
    }

    protected BooleanProperty sql = new BooleanProperty();

    public boolean isSql() {
        return sql.getValue();
    }

    public void setSql(boolean sql) {
        this.sql.setValue(sql);
    }

    protected BooleanProperty xQuery = new BooleanProperty();

    public boolean isXQuery() {
        return xQuery.getValue();
    }

    public void setXQuery(boolean xQuery) {
        this.xQuery.setValue(xQuery);
    }

    protected PersistentIdArrayProperty databases = new PersistentIdArrayProperty();

    public String[] getDatabases() {
        return databases.getValue();
    }

    public void setDatabases(String[] value) {
        this.databases.setValue(value);
    }
}

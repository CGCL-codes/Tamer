package de.bea.domingo.proxy;

import java.util.List;
import lotus.domino.Form;
import lotus.domino.NotesException;
import de.bea.domingo.DDatabase;
import de.bea.domingo.DForm;
import de.bea.domingo.DNotesMonitor;

/**
 * A notes Form.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class FormProxy extends BaseProxy implements DForm {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 6895768801526231827L;

    /** The name of the view for fast access. */
    private String fName = null;

    /**
     * Constructor for DFormProxy.
     *
     * @param theFactory the controlling factory
     * @param database Notes database
     * @param form the Notes Form
     * @param monitor the monitor
     */
    private FormProxy(final NotesProxyFactory theFactory, final DDatabase database, final Form form, final DNotesMonitor monitor) {
        super(theFactory, database, form, monitor);
        fName = getNameIntern();
    }

    /**
     * Returns a form object.
     *
     * @param theFactory the controlling factory
     * @param database Notes database
     * @param form the notes form object
     * @param monitor the monitor
     * @return a view object
     */
    static FormProxy getInstance(final NotesProxyFactory theFactory, final DDatabase database, final Form form, final DNotesMonitor monitor) {
        if (form == null) {
            return null;
        }
        FormProxy formProxy = (FormProxy) theFactory.getBaseCache().get(form);
        if (formProxy == null) {
            formProxy = new FormProxy(theFactory, database, form, monitor);
            formProxy.setMonitor(monitor);
            theFactory.getBaseCache().put(form, formProxy);
        }
        return formProxy;
    }

    /**
     * Returns the associated Notes form.
     *
     * @return associated Notes form
     */
    private Form getForm() {
        return (Form) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.proxy.BaseProxy#toString()
     */
    public String toString() {
        return fName;
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getAliases()
     */
    public List getAliases() {
        getFactory().preprocessMethod();
        try {
            return getForm().getAliases();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get aliases", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getFieldType(java.lang.String)
     */
    public int getFieldType(final String fieldName) {
        getFactory().preprocessMethod();
        try {
            return getForm().getFieldType(fieldName);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get field name", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getFields()
     */
    public List getFields() {
        getFactory().preprocessMethod();
        try {
            return getForm().getFields();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get fields", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getFormUsers()
     */
    public List getFormUsers() {
        getFactory().preprocessMethod();
        try {
            return getForm().getFormUsers();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get form users", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getHttpURL()
     */
    public String getHttpURL() {
        getFactory().preprocessMethod();
        try {
            return getForm().getHttpURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get Http URL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getLockHolders()
     */
    public List getLockHolders() {
        getFactory().preprocessMethod();
        try {
            return getForm().getLockHolders();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get lock holders", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getName()
     */
    public String getName() {
        return fName;
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getNotesURL()
     */
    public String getNotesURL() {
        getFactory().preprocessMethod();
        try {
            return getForm().getNotesURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get notes URL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getReaders()
     */
    public List getReaders() {
        getFactory().preprocessMethod();
        try {
            return getForm().getReaders();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get readers", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#getURL()
     */
    public String getURL() {
        getFactory().preprocessMethod();
        try {
            return getForm().getURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get URL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#isProtectReaders()
     */
    public boolean isProtectReaders() {
        getFactory().preprocessMethod();
        try {
            return getForm().isProtectReaders();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get protect readers", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#isProtectUsers()
     */
    public boolean isProtectUsers() {
        getFactory().preprocessMethod();
        try {
            return getForm().isProtectUsers();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get protect users", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#isSubForm()
     */
    public boolean isSubForm() {
        getFactory().preprocessMethod();
        try {
            return getForm().isSubForm();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get is subform", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lock()
     */
    public boolean lock() {
        getFactory().preprocessMethod();
        try {
            return getForm().lock();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot .lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lock(boolean)
     */
    public boolean lock(final boolean provisionalok) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(provisionalok);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lock(java.lang.String)
     */
    public boolean lock(final String name) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(name);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lock(java.lang.String, boolean)
     */
    public boolean lock(final String name, final boolean provisionalok) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(name, provisionalok);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lock(java.util.List)
     */
    public boolean lock(final List names) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(convertListToVector(names));
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lock(java.util.List, boolean)
     */
    public boolean lock(final List names, final boolean provisionalok) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(convertListToVector(names), provisionalok);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lockProvisional()
     */
    public boolean lockProvisional() {
        getFactory().preprocessMethod();
        try {
            return getForm().lockProvisional();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lockProvisional(java.lang.String)
     */
    public boolean lockProvisional(final String name) {
        getFactory().preprocessMethod();
        try {
            return getForm().lockProvisional(name);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock provisional", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#lockProvisional(java.util.List)
     */
    public boolean lockProvisional(final List names) {
        getFactory().preprocessMethod();
        try {
            return getForm().lockProvisional(convertListToVector(names));
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock provisional", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#remove()
     */
    public void remove() {
        getFactory().preprocessMethod();
        try {
            getForm().remove();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot remove form", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#setFormUsers(java.util.List)
     */
    public void setFormUsers(final List users) {
        getFactory().preprocessMethod();
        try {
            getForm().setFormUsers(convertListToVector(users));
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set form users", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#setProtectReaders(boolean)
     */
    public void setProtectReaders(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getForm().setProtectReaders(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set protect readers", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#setProtectUsers(boolean)
     */
    public void setProtectUsers(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getForm().setProtectUsers(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set protect users", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#setReaders(java.util.List)
     */
    public void setReaders(final List readers) {
        getFactory().preprocessMethod();
        try {
            getForm().setReaders(convertListToVector(readers));
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set readers", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DForm#unlock()
     */
    public void unlock() {
        getFactory().preprocessMethod();
        try {
            getForm().unlock();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot unlock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DView#getName()
     */
    private String getNameIntern() {
        getFactory().preprocessMethod();
        try {
            return getForm().getName();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get name", e);
        }
    }
}

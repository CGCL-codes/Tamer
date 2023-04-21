package com.mrroman.linksender.settings;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mrroman.linksender.settings.beanutils.BeanSaverLoader;
import com.mrroman.linksender.settings.beanutils.BeanSetterGetter;

/**
 *
 * @author dmn
 */
public abstract class Settings<T extends Settings> implements Serializable {

    private transient String settingsFileName = null;

    @SuppressWarnings("unchecked")
    public void load(String settingsFileName) {
        this.settingsFileName = settingsFileName;
        T tmp = null;
        try {
            tmp = (T) BeanSaverLoader.loadBean(settingsDirectory() + settingsFileName);
        } finally {
            if (tmp != null) {
                this.assignProperties(tmp);
            } else {
                save();
            }
        }
    }

    public void save() {
        BeanSaverLoader.saveBean(settingsDirectory() + settingsFileName, this);
    }

    public void assignProperties(Settings settings) {
        if (settings == null) {
            return;
        }
        try {
            BeanInfo bi_old = Introspector.getBeanInfo(this.getClass(), Object.class);
            PropertyDescriptor[] pd_old = bi_old.getPropertyDescriptors();
            BeanInfo bi_new = Introspector.getBeanInfo(settings.getClass(), Object.class);
            PropertyDescriptor[] pd_new = bi_new.getPropertyDescriptors();
            for (int i = 0; i < pd_new.length; i++) {
                PropertyDescriptor pd1 = pd_old[i];
                PropertyDescriptor pd2 = pd_new[i];
                Object value = BeanSetterGetter.getBeanPropertyEasy(settings, pd2);
                if (value != null) {
                    BeanSetterGetter.setBeanProperty(this, pd1, value);
                }
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntrospectionException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected transient java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected transient java.beans.VetoableChangeSupport vetoableChangeSupport = new java.beans.VetoableChangeSupport(this);

    /**
     * Add VetoableChangeListener.
     *
     * @param listener
     */
    public void addVetoableChangeListener(java.beans.VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    /**
     * Remove VetoableChangeListener.
     *
     * @param listener
     */
    public void removeVetoableChangeListener(java.beans.VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        vetoableChangeSupport = new java.beans.VetoableChangeSupport(this);
        propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    /**
     * Zwraca ścieżkę, gdzie powinny znajdować się ustawienia programów.
     * @return
     */
    public String settingsDirectory() {
        String result = null;
        Properties p = System.getProperties();
        String os = p.getProperty("os.name");
        String separator = p.getProperty("file.separator");
        if (os.toLowerCase().indexOf("windows") > -1) {
            result = System.getenv("LOCALAPPDATA");
            if (result == null) {
                result = System.getenv("APPDATA");
            }
            result = result + separator + applicationName().toLowerCase() + separator;
        } else {
            result = p.getProperty("user.home") + separator + "." + applicationName().toLowerCase() + separator;
        }
        File f = (new File(result));
        try {
            if (!f.exists() && !f.mkdirs()) {
                return null;
            }
        } catch (SecurityException e) {
            return null;
        }
        return result;
    }

    public abstract String applicationName();
}

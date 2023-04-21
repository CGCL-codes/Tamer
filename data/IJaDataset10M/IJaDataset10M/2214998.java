package net.sourceforge.mandalore.aspect.impl;

import net.sourceforge.beanprocessor.objectcloner.ObjectCloner;
import net.sourceforge.beanprocessor.objectcloner.RTCloneConfig;
import net.sourceforge.beanprocessor.objectcloner.RTCloneSettings;
import net.sourceforge.beanprocessor.objectcloner.ReplaceFactory;
import net.sourceforge.mandalore.aspect.base.*;
import net.sourceforge.mandalore.bean.ModelBean;
import net.sourceforge.mandalore.dto.DtoFactory;
import net.sourceforge.mandalore.dto.base.Dto;
import net.sourceforge.mandalore.spring.TransientFieldsInjector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.jsf.FacesContextUtils;
import javax.faces.context.FacesContext;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: ctoth
 * Date: Sep 5, 2006
 * Time: 15:15:22 PM
 */
public abstract class AbstractAspect<E extends Serializable> implements UIAspect<E>, CollapsibleUIAspect<E>, EditableUIAspect<E>, PersistenceUIAspect<E>, ModelBean {

    private static final Log log = LogFactory.getLog(AbstractAspect.class);

    private E _uiObject;

    private UIAspectMap _aspectMap;

    private Serializable _backupObject = null;

    private boolean _collapsed = false;

    private boolean _editing = false;

    protected AbstractAspect(E uiObject, UIAspectMap aspectMap) {
        _uiObject = uiObject;
        _aspectMap = aspectMap;
    }

    /**
     * UiObject and AspectMap must be set with setter methods explicitly!
     */
    protected AbstractAspect() {
    }

    public E getUIObject() {
        return _uiObject;
    }

    public void setUIObject(E object) {
        _uiObject = object;
    }

    public void setAspectMap(UIAspectMap aspectMap) {
        _aspectMap = aspectMap;
    }

    protected UIAspectMap getAspectMap() {
        return _aspectMap;
    }

    public Object readResolve() throws ObjectStreamException {
        return TransientFieldsInjector.inject(this);
    }

    /**
     * Utility method for convenience.
     */
    protected CollapsibleUIAspect getCollapsibleAspect(Object key) {
        return getAspectMap().getCollapsibleAspect(key);
    }

    /**
     * Utility method for convenience.
     */
    protected EditableUIAspect getEditableAspect(Object key) {
        return getAspectMap().getEditableAspect(key);
    }

    /**
     * Utility method for convenience.
     */
    protected PersistenceUIAspect getPersistenceAspect(Object key) {
        return getAspectMap().getPersistenceAspect(key);
    }

    public static final RTCloneSettings DEFAULT_BACKUP_SETTINGS = ObjectCloner.newSettings().includeAll().shallowCopyByType(".*").deepCopyByType(Dto.class).replaceClass(Dto.class, new DtoReplaceFactory()).exclude("callback").exclude("transient");

    public static final RTCloneConfig DEFAULT_BACKUP_CC = ObjectCloner.newConfig(DEFAULT_BACKUP_SETTINGS);

    public static final RTCloneSettings DEFAULT_REVERT_SETTINGS = ObjectCloner.newSettings().includeAll().shallowCopyByType(".*").exclude("callback").exclude("transient");

    public static final RTCloneConfig DEFAULT_REVERT_CC = ObjectCloner.newConfig(DEFAULT_BACKUP_SETTINGS);

    public String startEdit() {
        return startEdit(DEFAULT_BACKUP_CC);
    }

    protected String startEdit(RTCloneConfig backupCloneConfig) {
        _backupObject = (Serializable) ObjectCloner.clone(_uiObject, backupCloneConfig);
        uncollapse();
        setEditing(true);
        return null;
    }

    public String endEdit() {
        _backupObject = null;
        setEditing(false);
        return null;
    }

    public void setEditing(boolean editing) {
        if (editing) uncollapse();
        _editing = editing;
    }

    public boolean isEditing() {
        return _editing;
    }

    public String revertEdit() {
        if (_backupObject == null) {
            log.info("No backup object available - nothing to restore");
        } else {
            if (_uiObject instanceof Collection) {
                ((Collection) _uiObject).clear();
            }
            ObjectCloner.clone(_backupObject, _uiObject, DEFAULT_REVERT_CC);
        }
        return endEdit();
    }

    public boolean isCollapsed() {
        return _collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        _collapsed = collapsed;
    }

    public String collapse() {
        setCollapsed(true);
        return null;
    }

    public String uncollapse() {
        setCollapsed(false);
        return null;
    }

    public String persist() {
        throw new UnsupportedOperationException();
    }

    private static class DtoReplaceFactory implements ReplaceFactory {

        private DtoFactory _dtoFactory;

        private DtoFactory getDtoFactory() {
            if (_dtoFactory == null) {
                BeanFactory beanFactory = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
                _dtoFactory = (DtoFactory) beanFactory.getBean("dtoFactory");
            }
            return _dtoFactory;
        }

        public Object createInstance(String propertyName, Object instanceToReplace, Object parentBean) {
            return getDtoFactory().newInstance(instanceToReplace.getClass(), ((Dto) instanceToReplace).getId());
        }
    }
}

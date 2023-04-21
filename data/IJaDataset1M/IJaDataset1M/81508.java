package net.taylor.seam;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import net.taylor.jpa.EntityPersistenceContextMap;
import net.taylor.richfaces.EntityTreeNode;
import net.taylor.richfaces.RootTreeNode;
import net.taylor.validator.ValidationUtil;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;
import net.taylor.richfaces.TreeNode;

@Scope(ScopeType.CONVERSATION)
public abstract class AbstractQuickEntryBean<E> extends EntityHome<E> {

    private static final long serialVersionUID = 1L;

    @Override
    protected abstract E createInstance();

    protected abstract void prepareForEdit(E instance);

    protected abstract void preSave();

    public void setRequestedId(Long id) {
        setId(id);
    }

    public Long getRequestedId() {
        return (Long) getId();
    }

    @Override
    protected void initInstance() {
        super.initInstance();
        initialExtensions(instance);
    }

    @Override
    protected E loadInstance() {
        E instance = super.loadInstance();
        if (instance != null) {
            prepareForEdit(instance);
        }
        return instance;
    }

    private EntityTreeNode<E> extensions;

    public EntityTreeNode<E> getExtensions() {
        getInstance();
        return extensions;
    }

    protected void initialExtensions(E instance) {
        Contexts.getConversationContext().set(getInstanceName(), getInstance());
        try {
            Class<?> clazz = instance.getClass();
            String pkgName = clazz.getPackage().getName();
            String simpleName = clazz.getSimpleName();
            String className = pkgName + ".editor." + simpleName + "TreeNode";
            Class<EntityTreeNode<E>> entityTreeNodeClass;
            entityTreeNodeClass = (Class<EntityTreeNode<E>>) Class.forName(className);
            Constructor<EntityTreeNode<E>> entityTreeNodeConstructor = entityTreeNodeClass.getConstructor(new Class[] { TreeNode.class, clazz });
            extensions = entityTreeNodeConstructor.newInstance(null, instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RootTreeNode.instance().init(extensions);
    }

    @End
    public String save() {
        if (!validate()) {
            return null;
        }
        preSave();
        if (!validate()) {
            return null;
        }
        if (isManaged()) {
            update();
        } else {
            persist();
        }
        saveExtensions();
        return "find" + getEntityClass().getSimpleName();
    }

    protected boolean validate() {
        return ValidationUtil.validate(getInstance(), getStatusMessages());
    }

    protected void saveExtensions() {
        getExtensions().setInstance(getInstance());
        getExtensions().saveExtensions();
        getEntityManager().flush();
    }

    protected String getInstanceName() {
        return StringUtils.uncapitalize(getEntityClass().getSimpleName());
    }

    @Override
    public EntityManager getEntityManager() {
        return EntityPersistenceContextMap.instance().getEntityManagerNameFor(getEntityClass());
    }

    @SuppressWarnings("unchecked")
    protected <T> T find(Class<T> type, String property, Object value) {
        return (T) getEntityManager().createQuery("from " + type.getSimpleName() + " where " + property + " = :" + property).setParameter(property, value).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> findAll(Class<T> type, String property, Object value) {
        return (List<T>) getEntityManager().createQuery("from " + type.getSimpleName() + " where " + property + " = :" + property).setParameter(property, value).getResultList();
    }

    protected Long generateNumber() {
        Random generator = new Random();
        int randomInt = generator.nextInt(10000);
        return new Long(randomInt);
    }
}

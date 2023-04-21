package net.sf.balm.persistence.hibernate3;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 由于对HibernateTemplate进行了扩展，Spring自带的HibernateDaoSupport已经无法满足需要，
 * 因此重新实现了HibernateDaoSupport，功能和Spring自带的HibernateDaoSupport完全一致，
 * 只是将原有的HibernateTemplate改为了HibernateTemplateExtension。
 * <p>
 * 本类中的英文注释均来自Spring
 * 
 * @author dz
 * @see HibernateTemplateExt
 */
public class HibernateDaoSupportExt extends HibernateDaoSupport {

    /**
     * @param sessionFactory
     */
    public HibernateDaoSupportExt(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    /**
     * Create a HibernateTemplateExtension for the given SessionFactory. Only
     * invoked if populating the DAO with a SessionFactory reference!
     * <p>
     * Can be overridden in subclasses to provide a HibernateTemplateExtension
     * instance with different configuration, or a custom HibernateTemplate
     * subclass.
     * 
     * @param sessionFactory
     *            the Hibernate SessionFactory to create a
     *            HibernateTemplateExtension for
     * @return the new HibernateTemplateExtension instance
     * @see #setSessionFactory
     */
    protected HibernateTemplateExt createHibernateTemplate(SessionFactory sessionFactory) {
        return new HibernateTemplateExt(sessionFactory);
    }

    /**
     * Return the HibernateTemplateExtension for this DAO, pre-initialized with
     * the SessionFactory or set explicitly.
     * <p>
     * <b>Note: The returned HibernateTemplateExtension is a shared instance.</b>
     * You may introspect its configuration, but not modify the configuration
     * (other than from within an {@link #initDao} implementation). Consider
     * creating a custom HibernateTemplateExtension instance via
     * <code>new HibernateTemplateExtension(getSessionFactory())</code>, in
     * which case you're allowed to customize the settings on the resulting
     * instance.
     */
    public HibernateTemplateExt getHibernateTemplateExt() {
        return (HibernateTemplateExt) super.getHibernateTemplate();
    }
}

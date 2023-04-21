package org.crud4j.ejb3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang.ClassUtils;
import static org.apache.commons.lang.SystemUtils.*;
import org.apache.log4j.Logger;
import org.crud4j.core.AbstractCrudService;
import org.crud4j.core.BeforeCreateException;
import org.crud4j.core.BeforeRemoveException;
import org.crud4j.core.BeforeUpdateException;
import org.crud4j.core.CallbackMethodsCrudListener;
import org.crud4j.core.CrudBean;
import org.crud4j.core.CrudBeanDef;
import org.crud4j.core.CrudBrowsePage;
import org.crud4j.core.CrudField;
import org.crud4j.core.CrudFieldDef;
import org.crud4j.core.CrudListener;
import org.crud4j.core.IdInUseException;
import org.crud4j.core.RemoveMarkException;
import org.crud4j.core.defprovider.CrudBeanDefFactory;

public abstract class AbstractJPACrudService extends AbstractCrudService {
	/**
	 * Logger
	 */
	private static Logger log = Logger.getLogger(AbstractJPACrudService.class);

	/**
	 * Date pattern
	 */
	private static DateLocaleConverter DATE_CONVERTER = new DateLocaleConverter(
			Locale.getDefault(), DATE_PATTERN);

	/**
	 * class to entity manager address mapping
	 */
	protected static Map<Class, Ejb3BeanProvider> PERSISTENCE_UNITS = new HashMap<Class, Ejb3BeanProvider>();

	/**
	 * Crud Listeners
	 */
	private static Collection<CrudListener> crudListeners = new ArrayList<CrudListener>();

	public AbstractJPACrudService() {
		super();
		if (ConvertUtils.lookup(Date.class) == null) {
			ConvertUtils.register(DATE_CONVERTER, Date.class);
		}

		crudListeners.add(new CallbackMethodsCrudListener());
	}

	/**
	 * Register a new Ejb3 Crud Bean Provider
	 * 
	 * @param provider
	 *            el provider de beans
	 */
	public synchronized static void registerProvider(Ejb3BeanProvider provider) {
		for (Class clazz : provider.listClasses()) {
			PERSISTENCE_UNITS.put(clazz, provider);
		}
	}

	public synchronized static void registerCrudListener(CrudListener listener) {
		crudListeners.add(listener);
	}

	static {
		CrudBeanDefFactory.registerProvider(new Ejb3CrudBeanDefProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	public CrudBrowsePage browseLast(Class type, Integer pageSize) {
		log.debug("browse last page: " + type.getName() + ". page size: "
				+ pageSize);
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	CrudBrowsePage filteredBrowse(EntityManager em, Class type,
			Integer pageSize, Integer page, Map<String, String> filters) {
		if (log.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("filtered browse: " + type.getName() + ". page size: "
					+ pageSize + ". page: " + page + LINE_SEPARATOR);
			for (String f : filters.keySet()) {
				sb.append("filter: " + f + ". value: " + filters.get(f) + LINE_SEPARATOR);
			}
		F	log.debug(sb.toString());
		}

		CrudBrowsePage crudPage = new CrudBrowsePage();
		crudPage.setPosition(page);

		// page size
		if (pageSize == null) {
			pageSize = 20;
		}

		// page number
		if (page == null) {
			page = 0;
		}

		CrudBeanDef beanDef = CrudBeanDefFactory.getDefinition(type);

		String table = ClassUtils.getShortClassName(type);

		Query q = null;
		CrudFieldDef removeMark = beanDef.getRemoveMark();

		StringBuffer query = new StringBuffer("from " + table);

		if (removeMark != null || filters.size() > 0) {
			query.append(" WHERE");
		}

		// remove mark
		if (removeMark != null) {
			query.append(" " + removeMark.getName() + " IS NULL");
		}

		// filters
		// FIXME: more types needed :)
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (CrudFieldDef fieldDef : beanDef.getBrowseFilterFields()) {

			if (filters.containsKey(fieldDef.getName())) {
				if (!query.toString().endsWith("WHERE")) {
					query.append(" AND ");
				}

				// Crud field
				String filterValue = filters.get(fieldDef.getName());

				if (fieldDef.isCrudBean()) { // bean filter
					CrudBeanDef fkBeanDef = CrudBeanDefFactory
							.getDefinition(  fieldDef.getType());

					if (fkBeanDef.getIds().size() > 1) {
						throw new UnsupportedOperationException(
								"filters fields of crud beans with compound pk is not supported");
					}

					CrudFieldDef idFieldDef = fkBeanDef.getIds().iterator()
							.next();

					query.append(" " + fieldDef.getName() + "."
							+ idFieldDef.getName() + " = :parameter"
							+ parameters.size());
					parameters.put("parameter" + parameters.size(),
							ConvertUtils.convert(filterValue, idFieldDef
									.getType()));
				} else { // simple filter
					
					if (fieldDef.getType().equals(String.class )) {
						query.append(" lower(" + fieldDef.getName() + ") like :parameter"
								+ parameters.size());
						parameters.put("parameter" + parameters.size(), "%"+
								ConvertUtils.convert(filterValue.toLowerCase(), fieldDef
										.getType())+"%"	);
					}else {
						query.append(" " + fieldDef.getName() + " = :parameter"
								+ parameters.size());
						
						parameters.put("parameter" + parameters.size(),
								ConvertUtils.convert(filterValue, fieldDef
										.getType()));
					}
					

				}
			}
		}

		// create query
		q = em.createQuery(query.toString());
		for (String key : parameters.keySet()) {
			q.setParameter(key, parameters.get(key));
		}

		// bring one more result
		if (pageSize < Integer.MAX_VALUE) {
			q.setMaxResults(pageSize + 1);
			q.setFirstResult(page * pageSize);
		}

		// remove last if it's not last
		List l = q.getResultList();
		if (l.size() <= pageSize) {
			crudPage.setLast(true);
		} else {
			l.remove(l.size() - 1);
		}

		// collection of beans
		List<CrudBean> beans = new ArrayList<CrudBean>();
		for (Object bean : l) {
			CrudBean crudBean = create(type);
			crudBean.setNewBean(false);
			this.populateCRUDBean(bean, crudBean, true);
			beans.add(crudBean);
		}
		crudPage.setBeans(beans);

		return crudPage;
	}

	/**
	 * {@inheritDoc}
	 */
	public CrudBrowsePage filteredBrowseLast(Class type, Integer pageSize,
			Map<String, String> filters) {
		if (log.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("filtered browse last: " + type.getName()
					+ ". page size: " + pageSize + LINE_SEPARATOR);
			for (String f : filters.keySet()) {
				sb.append("filter: " + f + ". value: " + filters.get(f) + LINE_SEPARATOR);
			}
			log.debug(sb.toString());
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	CrudBean find(EntityManager em, Class type, Map<String, String> ids) {
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("find: " + type.getName());
			for (String id : ids.keySet()) {
				sb.append("Id: " + id + ". value: " + ids.get(id) + LINE_SEPARATOR);
			}
			log.debug(sb.toString());
		}
		// create cbean
		CrudBean cBean = this.create(type);
		cBean.setNewBean(false);

		// pass the parameters
		for (CrudField crudField : cBean.getIds()) {
			crudField.setValue(ids.get(crudField.getDef().getName()));
		}

		Object bean = em.find(type, cBean.getId());

		if (bean == null) {
			return null;
		}

		// populate crud bean
		this.populateCRUDBean(bean, cBean, true);

		// return cBean
		return cBean;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] listBeanClasses() {
		return PERSISTENCE_UNITS.keySet().toArray(
				new Class[PERSISTENCE_UNITS.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	CrudBean persist(EntityManager em, String user, CrudBean crudBean)
			throws RemoveMarkException, IdInUseException,
			BeforeCreateException, BeforeUpdateException {
		log.debug("persist bean " + crudBean.getDef().getType() + ". id: "
				+ crudBean.getId());

		Object id = crudBean.getId();

		// Create ID if needed
		Object bean = null;

		if (id != null) {
			bean = em.find(crudBean.getDef().getType(), id);
			if (bean != null && crudBean.isNewBean()) {
				throw new IdInUseException(id, crudBean.getDef().getType());
			}
		}

		try {
			if (id == null) {
				bean = crudBean.getDef().getType().newInstance();
			} else {
				if (bean == null) {
					bean = crudBean.getDef().getType().newInstance();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"unable to create a new instance of bean "
							+ crudBean.getDef().getType(), e);
		}

		// validate it's not already removed
		CrudField removeMark = crudBean.getRemoveMark();
		if (removeMark != null) {
			boolean deleted = false;
			try {
				if (PropertyUtils.getProperty(bean, removeMark.getDef()
						.getName()) != null) {
					deleted = true;
				}
			} catch (Exception e) {
				throw new RuntimeException("unable to evaluate remove mark", e);
			}
			if (deleted) {
				throw new RemoveMarkException(removeMark.getDef().getName(),
						crudBean.getDef().getType());
			}
		}

		Collection<CrudField> fields = crudBean.getFields();
		for (CrudField field : fields) {
			try {
				// only non null values
				if (field.getValue() != null
						&& !Collection.class.isAssignableFrom(field.getDef()
								.getType())) {
					// Fks
					if (this.getEntityManagerFactory(field.getDef().getType()) != null) {
						// FIXME only simple fks
						CrudBeanDef fkCrudBeanDef = CrudBeanDefFactory
								.getDefinition(field.getDef().getType());
						if (fkCrudBeanDef.getIds().size() != 1) {
							throw new UnsupportedOperationException(
									"compount fks aren't supported for fks");
						}
						CrudFieldDef fkIdField = fkCrudBeanDef.getIds()
								.iterator().next();
						Object fkId = ConvertUtils.convert(field.getValue(),
								fkIdField.getType());
						Object fk = em.find(field.getDef().getType(), fkId);
						PropertyUtils.setProperty(bean, field.getDef()
								.getName(), fk);
					} else {
						// No fk
						// pass the value only if the value is not empty or
						// respectEmptyFieldValue is set to true
						if (field.getValue().length() > 0
								|| field.getDef().isRespectEmptyValue()) {
							// BeanUtils do the conversion for us :)
							BeanUtils.setProperty(bean, field.getDef()
									.getName(), field.getValue());
						} else {
							PropertyUtils.setProperty(bean, field.getDef()
									.getName(), null);
						}
					}
				}

				// Crud generated values
				if (crudBean.isNewBean() && field.getValue() == null
						&& field.getDef().isGeneratedValue()) {
					if (field.getDef().getType().equals(UUID.class)) {
						BeanUtils.setProperty(bean, field.getDef().getName(),
								UUID.randomUUID());
					} else if (field.getDef().getType().equals(String.class)) {
						BeanUtils.setProperty(bean, field.getDef().getName(),
								UUID.randomUUID().toString());
					}
				}

			} catch (Exception e) {
				String msg = "unexpected exception while setting value from field "
						+ field.getDef().getName()
						+ " to bean "
						+ bean.getClass().getName();
				log.error(msg, e);
				throw new RuntimeException(msg, e);
			}
		}

		for (CrudListener listener : AbstractJPACrudService.crudListeners) {
			if (crudBean.isNewBean()) {
				listener.onCreate(user, bean);
			} else {
				EntityManager oldObjectEntityManager = null;
				try {
					oldObjectEntityManager = this.getEntityManagerFactory(
							crudBean.getDef().getType()).createEntityManager();
					listener.onUpdate(user, oldObjectEntityManager.find(bean
							.getClass(), id), bean);
				} finally {
					if (oldObjectEntityManager != null) {
						oldObjectEntityManager.close();
					}
				}

			}

		}

		em.persist(bean);

		// return the new persisted bean
		CrudBean newBean = this.create(crudBean.getDef().getType());
		this.populateCRUDBean(bean, newBean, true);
		newBean.setNewBean(false);

		return newBean;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	void remove(EntityManager em, String user, CrudBean crudBean)
			throws BeforeRemoveException {
		log.debug("remove bean " + crudBean.getDef().getType() + ". id: "
				+ crudBean.getId());

		// create id
		Object id = crudBean.getId();

		// find bean
		Object bean = em.find(crudBean.getDef().getType(), id);

		// validate bean found
		if (bean == null) {
			throw new RuntimeException("Unable to remove. No bean for id " + id);
		}

		// remove bean
		CrudField removeMark = crudBean.getRemoveMark();

		// notify listeners
		for (CrudListener listener : AbstractJPACrudService.crudListeners) {
			listener.onRemove(user, bean);
		}

		if (removeMark == null) {
			em.remove(bean);
		} else {
			try {
				if (!removeMark.getDef().getType().equals(Date.class)) {
					throw new UnsupportedOperationException(
							"only remove marks of type java.util.Date are supported");
				}
				PropertyUtils.setProperty(bean, removeMark.getDef().getName(),
						Calendar.getInstance().getTime());
				em.persist(bean);
			} catch (Exception e) {
				throw new RuntimeException("unable to mark removed bean", e);
			}
		}
	}

	protected EntityManagerFactory getEntityManagerFactory(Class type) {
		Ejb3BeanProvider provider = PERSISTENCE_UNITS.get(type);

		if (provider != null) {
			return provider.getEntityManagerFactory();
		} else {
			return null;
		}
	}

}

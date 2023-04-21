package net.infordata.ifw2m.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import net.infordata.ifw2.msgs.SimpleMessage;
import net.infordata.ifw2.props.ClassInfo;
import net.infordata.ifw2.props.Property;
import net.infordata.ifw2.web.jpa.JpaUtil;
import net.infordata.ifw2m.mdl.flds.IFieldSet;
import net.infordata.ifw2m.mdl.flds.IFieldSetValidator;
import net.infordata.ifw2m.mdl.flds.IPojoFieldSet;

public class ShouldNotExistValidator<T> implements IFieldSetValidator, Serializable {

    private static final long serialVersionUID = 1L;

    protected static enum MT {

        TK
    }

    private final ClassInfo<T> ivCInfo;

    private final Property<T, ?> ivProperty;

    private final String ivQuery;

    private final String ivFieldName;

    private final String ivPersistenceUnitName;

    public ShouldNotExistValidator(Class<T> pojoClass, String idProperty, String query, String fieldName) {
        this(null, pojoClass, idProperty, query, fieldName);
    }

    /**
   * @param pojoClass
   * @param idProperty - the property of the pojo object containing the internal
   *   system generated id.
   * @param query
   * @param fieldName
   */
    public ShouldNotExistValidator(String persistanceUnitName, Class<T> pojoClass, String idProperty, String query, String fieldName) {
        if (query == null) throw new NullPointerException("query is null");
        if (fieldName == null) throw new NullPointerException("fieldName is null");
        ivCInfo = ClassInfo.getInstance(pojoClass);
        ivProperty = ivCInfo.getProperty(idProperty);
        if (ivProperty == null) throw new IllegalArgumentException("Property not found: " + idProperty);
        ivQuery = query;
        ivFieldName = fieldName;
        ivPersistenceUnitName = persistanceUnitName;
    }

    @Override
    public boolean isApplicable() {
        return true;
    }

    @Override
    public final String[] getFieldNames() {
        return new String[] { ivFieldName };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(IFieldSet fs, String... fieldsToValidate) {
        fs.get(ivFieldName).setMessage(MT.TK, null);
        Object value = fs.get(ivFieldName).getValue();
        if (value == null) return;
        IPojoFieldSet<T> pfs = (IPojoFieldSet<T>) fs;
        T fsPojo = pfs.getPojo();
        Object fsPojoId = ivProperty.get(fsPojo);
        EntityManager session = JpaUtil.getCurrentEntityManager(ivPersistenceUnitName);
        EntityTransaction trans = session.getTransaction();
        trans.begin();
        try {
            Query query = session.createQuery(ivQuery).setHint("org.hibernate.readOnly", true);
            query.setParameter(ivFieldName.toString(), value);
            List<?> list = query.getResultList();
            if (list.size() == 1) {
                T lkPojo = (T) list.get(0);
                Object lkId = ivProperty.get(lkPojo);
                if (fsPojoId == null || !fsPojoId.equals(lkId)) {
                    fs.get(ivFieldName).setMessage(MT.TK, new SimpleMessage("" + value + Messages.getString("ShouldNotExistValidator.0")));
                }
            } else if (list.size() > 1) throw new IllegalStateException("More than one result returned for " + value);
        } finally {
            if (trans.isActive()) trans.rollback();
        }
    }

    @Override
    public boolean equals(IFieldSetValidator fsv) {
        return equals((Object) fsv);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ShouldNotExistValidator<?> other = (ShouldNotExistValidator<?>) obj;
        if (ivCInfo == null) {
            if (other.ivCInfo != null) return false;
        } else if (!ivCInfo.equals(other.ivCInfo)) return false;
        if (ivFieldName == null) {
            if (other.ivFieldName != null) return false;
        } else if (!ivFieldName.equals(other.ivFieldName)) return false;
        if (ivProperty == null) {
            if (other.ivProperty != null) return false;
        } else if (!ivProperty.equals(other.ivProperty)) return false;
        if (ivQuery == null) {
            if (other.ivQuery != null) return false;
        } else if (!ivQuery.equals(other.ivQuery)) return false;
        if (ivPersistenceUnitName == null) {
            if (other.ivPersistenceUnitName != null) return false;
        } else if (!ivPersistenceUnitName.equals(other.ivPersistenceUnitName)) return false;
        return true;
    }
}

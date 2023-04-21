package net.infordata.ifw2m.jpa;

import java.util.HashMap;
import java.util.Map;
import net.infordata.ifw2m.mdl.flds.APojoLookupValidator;

/**
 * {@link APojoLookupValidator} using JPA to validate the pojo object reference. 
 * @author valentino.proietti
 * @param <T>
 */
public class LookupValidator<T> extends ALookupValidator<T> {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> ivQueryes;

    public LookupValidator(String persistenceUnitName, Class<T> pojoClass, String pojoField, String[] properties, String[] fields, String[] ejbQLQueryes) {
        super(persistenceUnitName, pojoClass, pojoField, properties, fields);
        if (ejbQLQueryes == null) throw new NullPointerException();
        if (ejbQLQueryes.length != fields.length) throw new IllegalArgumentException(ejbQLQueryes.length + " vs " + fields.length);
        ivQueryes = new HashMap<String, String>(fields.length);
        for (int i = 0; i < fields.length; i++) ivQueryes.put(fields[i], ejbQLQueryes[i]);
    }

    public LookupValidator(Class<T> pojoClass, String pojoField, String[] properties, String[] fields, String[] ejbQLQueryes) {
        this(null, pojoClass, pojoField, properties, fields, ejbQLQueryes);
    }

    @Override
    protected String getQuery(String... lookupFields) {
        if (lookupFields.length <= 0) throw new IllegalArgumentException();
        return ivQueryes.get(lookupFields[0]);
    }

    @Override
    public boolean isApplicable() {
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final LookupValidator<?> other = (LookupValidator<?>) obj;
        if (ivQueryes == null) {
            if (other.ivQueryes != null) return false;
        } else if (!ivQueryes.equals(other.ivQueryes)) return false;
        return true;
    }
}

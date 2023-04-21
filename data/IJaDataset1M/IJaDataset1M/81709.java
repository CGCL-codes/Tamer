package org.hl7.merger;

import java.util.Iterator;
import org.hl7.meta.Feature;
import org.hl7.rim.Act;
import org.hl7.types.CD;
import org.hl7.types.II;
import org.hl7.types.SET;
import org.hl7.types.enums.ActMood;
import org.hl7.util.ApplicationContext;
import org.xml.sax.Locator;

/** A merger for Act definitions with same code. */
public class MergerOfActDefinitionsByCode<C extends Act, T extends CD> extends MergerUsingCache<C, T> implements Merger<C, T> {

    public MergerOfActDefinitionsByCode(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    public boolean isStaticallyApplicable(Object object) {
        return object != null && (object instanceof Act);
    }

    public boolean isApplicable(C object, Object value) {
        return value != null && value instanceof CD && ((CD) value).isNull().isFalse();
    }

    public C merge(C object, Feature feature, T value, Locator loc) {
        SET<II> currentId = object.getId();
        if (feature.getName().equals("code") && (currentId == null || currentId.isNull().isTrue() || currentId.isEmpty().isTrue()) && object.getMoodCode().implies(ActMood.Definition).isTrue()) return doMerge(object, feature, value, loc); else return object;
    }

    private C doMerge(C object, Feature feature, T code, Locator loc) {
        C cachedObject = (C) findObjectInCache(code);
        if (cachedObject != null) {
            LOGGER.finest("OBJECT CODE IN CACHE: " + code.toString() + " --> " + cachedObject.getInternalId());
            return cachedObject;
        } else {
            if (useHibernate() && !getApplicationContext().getPersistence().isPersistent(object)) {
                Iterator<C> results = getQuery("actDefinitionByCode").setParameter("code", code.code().toString()).setParameter("codeSystem", code.codeSystem().toString()).list().iterator();
                if (results.hasNext()) {
                    object = results.next();
                    LOGGER.finest(addLoc("FOUND ACT BY CODE: " + code.code() + "@" + code.codeSystem() + " --> " + object, loc));
                }
            }
            putObjectInCache(code, object);
        }
        return object;
    }
}

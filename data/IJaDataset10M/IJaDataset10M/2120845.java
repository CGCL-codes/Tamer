package org.apache.openjpa.sdo.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.PersistenceException;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.sdo.ImplHelper;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;
import commonj.sdo.helper.DataFactory;

/**
 * Maps SDO Type in terms of constituent Property.
 * 
 * @author Pinaki Poddar
 * @since 0.2.0
 */
public class ContainedIdenticalTypeMapping extends IdenticalTypeMapping {

    public ContainedIdenticalTypeMapping(Type type, Class cls, Property owner) {
        super(type, cls);
        this.owner = owner;
        addMapping(this.owner, null);
    }
}

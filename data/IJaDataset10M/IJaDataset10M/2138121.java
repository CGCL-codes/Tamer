package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;

public interface ObjectMapping {

    void createObject(DatabaseConnector connector, NakedObject object);

    void destroyObject(DatabaseConnector connector, NakedObject object);

    NakedObject[] getInstances(DatabaseConnector connector, NakedObjectSpecification cls);

    NakedObject[] getInstances(DatabaseConnector connector, NakedObjectSpecification cls, String pattern);

    NakedObject getObject(DatabaseConnector connector, Oid oid, NakedObjectSpecification hint);

    boolean hasInstances(DatabaseConnector connector, NakedObjectSpecification cls);

    void resolve(DatabaseConnector connector, NakedObject object);

    void resolveCollection(DatabaseConnector connector, NakedObject object, NakedObjectAssociation field);

    void save(DatabaseConnector connector, NakedObject object);

    void shutdown();

    void startup(DatabaseConnector connection, ObjectMappingLookup objectMapperLookup);
}

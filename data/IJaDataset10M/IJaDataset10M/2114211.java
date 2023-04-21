package com.objectwave.persist;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;
import java.util.Collection;

/**
 * Generic Query interface
 * @author Dave Hoag
 * @version $Date: 2005/02/21 03:56:36 $ $Revision: 2.2 $
 */
public interface ObjectQuery {

    void setSubject(Persistence obj);

    void setObjectLimit(int num);

    void setAsLike(boolean value);

    void setBroker(Broker b);

    void setCollectionAdapter(CollectionAdapter adapter);

    void setConstraint(Persistence p);

    void setConstraintString(String constraints) throws ParseException;

    void setDistinct(boolean b);

    void setFieldConstraint(Persistence search, String fieldPath, String value, String compareConstraintType);

    void setSubSelect(Persistence p, String fieldPath, String subSelectFieldPath, ObjectQuery subSelectQuery, boolean isNot);

    void setIsanyOf(Persistence p, String fieldName, List anyOf);

    void setIsNotNull(Persistence p, String fieldName);

    void setIsNull(Persistence p, String fieldName);

    void setNotAnyOf(Persistence p, String fieldName, Vector anyOf);

    void setOrderByList(Vector v);

    Persistence getJoinConstraint();

    boolean isAttributeSearch();

    boolean isDistinct();

    int getObjectLimit();

    Broker getBroker();

    CollectionAdapter getCollectionAdapter();

    java.util.List getChildConstraintFor(String field);

    Persistence getSubject();

    boolean isLike();

    Vector getOrderByList();

    void addConstraint(Constraint constraint);

    Constraint getConstraintFor(String field);

    int count() throws QueryException;

    boolean fieldIsConstrained(String field);

    Collection find() throws QueryException;

    Object findCollection() throws QueryException;

    Vector findAttributes(String[] attributes) throws QueryException;

    Object findCollection(Class clazz) throws QueryException;

    Collection findIn(Persistence obj) throws QueryException;

    Persistence findUnique() throws QueryException;

    void addOrderByField(String pathToField);

    void addOrderByField(String pathToField, boolean descend);

    void deleteAll() throws QueryException;
}

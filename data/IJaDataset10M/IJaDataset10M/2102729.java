package org.genwork.business;

import java.util.Collection;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.genwork.data.SearchDataObject;
import org.genwork.mapping.Criterion;
import org.genwork.mapping.entity.GenericObjectEntity;
import org.genwork.util.exception.GenericException;

public interface ObjectLocal {

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public GenericObjectEntity getObject(String id) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public SearchDataObject searchAllObjects(String type) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public SearchDataObject searchObjects(String type, Criterion[] criteria, int page, String sortedColumn, String sortedOrder, int pageSize) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public GenericObjectEntity searchObject(String type, Criterion[] criteria) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public Collection<GenericObjectEntity> getLinkedObjects(String id, String property) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public GenericObjectEntity getLinkedObject(String id, String property) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public int getObjectsNumber() throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void removeLinkedObject(String id, String property, String linkedId) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void addLinkedObject(String id, String property, String linkedId) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void setLinkedObject(String id, String property, String linkedId) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public String createObject(String type, String id, String[] properties, String[] values) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public String createObject(String type, String[] properties, String[] values) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public String createObject(String type, String id, String[] properties, String[] values, String[] fileProperties, byte[][] fileValues) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public String createObject(String type, String[] properties, String[] values, String[] fileProperties, byte[][] fileValues) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void updateObject(String id, String[] properties, String[] values) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void updateObject(String id, String[] properties, String[] values, String[] fileProperties, byte[][] fileValues) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void removeObject(String id) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void removeObjects(String type) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public int importDatas(String xml) throws GenericException;

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public String exportDatas(String type, Criterion[] criteria, int page, String sortedColumn, String sortedOrder, int pageSize) throws GenericException;
}

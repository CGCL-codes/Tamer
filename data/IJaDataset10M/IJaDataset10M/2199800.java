package it.aco.mandragora.serviceFacade.sessionFacade.remoteFacade.SLSB.business;

import javax.ejb.EJBObject;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.rmi.RemoteException;

public interface BusinessSLSBFacade extends EJBObject {

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use {@link #buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName,Class mapValueClass)}
     */
    public HashMap buildHashMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use {@link #buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName,Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName)}
     *
     */
    public HashMap buildHashMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use {@link #buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, String valueObjectValueAttributeName)}
     */
    public HashMap buildHashMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public Map buildMap(Object pInstance, String pAttributeName, String valueObjectKeyAttributeName) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated see{@link #buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass)}
     */
    public HashMap buildHashMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, Class mapValueClass) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use {@link #buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName,String valueObjectValueAttributeName)}
     */
    public HashMap buildHashMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public Map buildMap(Collection valueObjectsCollection, String valueObjectKeyAttributeName) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use  {@link #addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName,Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName,Class mapValueClass)}
     */
    public HashMap addToHashMap(Object pInstance, HashMap map, String pAttributeName, String valueObjectKeyAttributeName, boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use  {@link #addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName)}
     */
    public HashMap addToHashMap(Object pInstance, HashMap map, String pAttributeName, String valueObjectKeyAttributeName, boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use {@link #addToMap(Object pInstance,String pAttributeName, Map map, String valueObjectKeyAttributeName,String valueObjectValueAttributeName)}
     */
    public HashMap addToHashMap(Object pInstance, HashMap map, String pAttributeName, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public Map addToMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use  {@link #addToMap(Collection valueObjectsCollection, Map map,  String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName,Class mapValueClass)}
     */
    public HashMap addToHashMap(Collection valueObjectsCollection, HashMap map, String valueObjectKeyAttributeName, boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use {@link #addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName,Boolean isValueObjectKeyAttributeNameToSet,String valueObjectValueAttributeName)}
     */
    public HashMap addToHashMap(Collection valueObjectsCollection, HashMap map, String valueObjectKeyAttributeName, boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet, String valueObjectValueAttributeName) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, Boolean isValueObjectKeyAttributeNameToSet) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, Class mapValueClass) throws RemoteException;

    /**
     * @deprecated use {@link #addToMap(Collection valueObjectsCollection ,Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName)}
     */
    public HashMap addToHashMap(Collection valueObjectsCollection, HashMap map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public Map addToMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName) throws RemoteException;

    public void updateCollectionWithMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public void updateCollectionWithMap(Object pInstance, String pAttributeName, Map map, String valueObjectKeyAttributeName) throws RemoteException;

    public void updateCollectionWithMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws RemoteException;

    public void updateCollectionWithMap(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName) throws RemoteException;

    /**
     *  @deprecated use {@link it.aco.mandragora.common.utils.BeanCollectionUtils#getTreeLeaves(Object valueobjectOrCollection, String path)}
     * @param valueobjectOrCollection
     * @param path
     * @return
     * @throws RemoteException
     */
    public Collection getTreeLeaves(Object valueobjectOrCollection, String path) throws RemoteException;

    public Collection retrieveTreeLeaves(Object valueobjectOrCollection, String path) throws RemoteException;

    /**
     * @deprecated use {@link #retrieveTreeLeaves(Object valueobjectOrCollection, String path)}
     */
    public Collection getCollectionOfRelatedMToNElements(Object pInstance, String collectionName, String pAttributeName) throws RemoteException;

    public Collection retrieveNullPathTreeLeaves(Object valueobjectOrCollection, String path) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String[] attributesComparator, String path, String[] sourcePAttributeNames, String[] targetPAttributeNames, Object[] valuesArray, String[] pAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String[] attributesComparator, String path, String[] sourcePAttributeNames, String[] targetPAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String[] attributesComparator, String path, String[] sourcePAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String[] attributesComparator, String path) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String[] attributesComparator, String path, String[] sourcePAttributeNames, Object[] valuesArray, String[] pAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String[] attributesComparator, String path, Object[] valuesArray, String[] pAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String path, String[] sourcePAttributeNames, String[] targetPAttributeNames, Object[] valuesArray, String[] pAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String path, String[] sourcePAttributeNames, String[] targetPAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String path, String[] sourcePAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String path) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String path, String[] sourcePAttributeNames, Object[] valuesArray, String[] pAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeLeaves(Object pInstance, String pAttributeName, String path, Object[] valuesArray, String[] pAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String[] pkNames, String path, String[][] nodeSourcePAttributeNames, String[][] nodeTargetPAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String[] pkNames, String path, String[][] nodeSourcePAttributeNames, String[][] nodeTargetPAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String[] pkNames, String path, String[][] nodeSourcePAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String[] pkNames, String path) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String path) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String path, String[][] nodeSourcePAttributeNames, String[][] nodeTargetPAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String path, String[][] nodeSourcePAttributeNames) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String[] pkNames, String path, String[][] nodeSourcePAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String[] pkNames, String path, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String path, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String path, String[][] nodeSourcePAttributeNames, String[][] nodeTargetPAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String path, String[][] nodeSourcePAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    /**
     * @deprecated use {@link #addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String[] pkNames,  String path, String[][] nodeSourcePAttributeNames,  String[] pAttributeNames,Object[] valuesArray)}
     */
    public void align(Object pInstance, String pAttributeName, String[] treePathCollectionSource, String[] pkNames, String[] pAttributeNames, Object[] valuesArray, String[] sourcePAttributeNames) throws RemoteException;

    /**
     * @deprecated use {@link #addToRetainInCollectionTreeNodes(Object pInstance, String pAttributeName, String path, String[][] nodeSourcePAttributeNames,   String[] pAttributeNames,Object[] valuesArray)}
     */
    public void align(Object pInstance, String pAttributeName, String[] treePathCollectionSource, String[] pAttributeNames, Object[] valuesArray, String[] sourcePAttributeNames) throws RemoteException;

    public void createValueObjectsTreeWithCollection(Object pInstance, String pAttributeName, String path, String[][] groupedPkNames, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    public void createValueObjectsTreeWithCollection(Object pInstance, String pAttributeName, String path, String[][] groupedPkNames) throws RemoteException;

    public void createValueObjectsTreeWithCollection(Object pInstance, String pAttributeName, String path) throws RemoteException;

    public void createValueObjectsTreeWithCollection(Object pInstance, String pAttributeName, String path, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    public void createValueObjectsTreeWithCollection(Collection valueObjectsCollection, Object pInstance, String path, String[][] groupedPkNames, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    public void createValueObjectsTreeWithCollection(Collection valueObjectsCollection, Object pInstance, String path, String[][] groupedPkNames) throws RemoteException;

    public void createValueObjectsTreeWithCollection(Collection valueObjectsCollection, Object pInstance, String path) throws RemoteException;

    public void createValueObjectsTreeWithCollection(Collection valueObjectsCollection, Object pInstance, String path, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    public void addToValueObjectsTreeWithCollection(Object pInstance, String pAttributeName, String path, String[][] groupedPkNames, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    public void addToValueObjectsTreeWithCollection(Object pInstance, String pAttributeName, String path, String[][] groupedPkNames) throws RemoteException;

    public void addToValueObjectsTreeWithCollection(Object pInstance, String pAttributeName, String path) throws RemoteException;

    public void addToValueObjectsTreeWithCollection(Object pInstance, String pAttributeName, String path, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    public void addToValueObjectsTreeWithCollection(Collection valueObjectsCollection, Object pInstance, String path, String[][] groupedPkNames, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    public void addToValueObjectsTreeWithCollection(Collection valueObjectsCollection, Object pInstance, String path, String[][] groupedPkNames) throws RemoteException;

    public void addToValueObjectsTreeWithCollection(Collection valueObjectsCollection, Object pInstance, String path) throws RemoteException;

    public void addToValueObjectsTreeWithCollection(Collection valueObjectsCollection, Object pInstance, String path, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    /**
     * @deprecated use {@link #addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName,  String[] pAttributeNames,Object[] valuesArray)}
     */
    public void createMissingRelationshipElement(Object pInstance, String collectionName, String pAttributeName, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    /**
     * @deprecated use {@link #addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName)}
     */
    public void createMissingRelationshipElement(Object pInstance, String collectionName, String pAttributeName) throws RemoteException;

    /**
     * @deprecated use {@link #addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName, Collection valueObjectsCollection)}
     */
    public void createMissingRelationshipElement(Object pInstance, String collectionName, String pAttributeName, Collection valueObjectsCollection) throws RemoteException;

    /**
     * @deprecated use {@link #addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName, Collection valueObjectsCollection,  String[] pAttributeNames,Object[] valuesArray)}
     */
    public void createMissingRelationshipElement(Object pInstance, String collectionName, String pAttributeName, Collection valueObjectsCollection, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName) throws RemoteException;

    /**
     * This method has the same behavior  of {@link #addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName, Collection valueObjectsCollection,  String[] pAttributeNames,Object[] valuesArray)}
     * where <code>pAttributeNames</code> and <code>valuesArray</code> are null.
     *
     * @param pInstance:value object that has to be related
     * @param collectionName name of the attribute of <code>pInstance</code> holding the collecton ofteh relationships
     * @param pAttributeName property name of the generic elements of the collection <code>pInstance.collectionName</code> representig the value object which <code>pInstance</code> has to be related to
     * @param valueObjectsCollection Collection of the value objects which  <code>pInstance</code>  has to be related to. If it is null all instance in the media store will be used
     * @throws RemoteException  -
     *      If <code>pInstance</code> is null.</br>
     */
    public void addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName, Collection valueObjectsCollection) throws RemoteException;

    public void addToRelationshipCollectionMissingElements(Object pInstance, String collectionName, String pAttributeName, Collection valueObjectsCollection, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void createRelationshipCollection(Object pInstance, String collectionName, String pAttributeName, Collection valueObjectsCollection, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public void createRelationshipCollectionByOrValues(Object pInstance, String collectionName, String pAttributeName, String orPAttributeName, Collection valuesCollection, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    /**
     * @deprecated use {@link #createRelationshipCollectionByOrValues(Object pInstance, String collectionName, String pAttributeName, String orPAttributeName, Collection valuesCollection,  String[] pAttributeNames,Object[] valuesArray)}
     */
    public void setRelationshipElementWithQueryByOrValues(Object pInstance, String collectionName, String pAttributeName, String[] pAttributeNames, Object[] valuesArray, String orPAttributeName, Collection valuesCollection) throws RemoteException;

    public void createRelationshipCollectionBySearchValueInFields(Object pInstance, String collectionName, String pAttributeName, String[] toSearchInPAttributeNames, Object value, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    /**
     *
     * @deprecated use {@link #createRelationshipCollectionBySearchValueInFields(Object pInstance, String collectionName, String pAttributeName, String[] toSearchInPAttributeNames, Object value,  String[] pAttributeNames,Object[] valuesArray)}
     */
    public void setRelationshipElementWithQueryBySearchValueInFields(Object pInstance, String collectionName, String pAttributeName, String[] pAttributeNames, Object[] valuesArray, String[] toSearchInPAttributeNames, Object value) throws RemoteException;

    public boolean addToCollection(Object pInstance, String pAttributeName, Object toAddPInstance) throws RemoteException;

    /**
     * @deprecated use {@link #addToCollection(Object pInstance, String pAttributeName, Object toAddPInstance)}
     */
    public boolean addElementToCollectionReference(Object pInstance, String pAttributeName, Object toAddPInstance) throws RemoteException;

    public boolean addAllToCollection(Object pInstance, String pAttributeName, Collection valueObjectsCollectionToAdd) throws RemoteException;

    /**
     * @deprecated use {@link #addAllToCollection(Object pInstance, String pAttributeName, Collection  valueObjectsCollectionToAdd)}
     */
    public Object addCollectionToCollectionReference(Object pInstance, String pAttributeName, Collection valueObjectsCollectionToAdd) throws RemoteException;

    public boolean addAllToCollection(Class targetRealClass, Collection valueObjectsCollection, Collection valueObjectsCollectionToAdd, String[] sourcePAttributeNames, String[] targetPAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    /**
     * @deprecated use {@link #addAllToCollection(Class targetRealClass, Collection valueObjectsCollection, Collection  valueObjectsCollectionToAdd,String[] sourcePAttributeNames,String[] targetPAttributeNames, String[] pAttributeNames,Object[] valuesArray)}
     */
    public Collection addCollectionFromCollection(Class targetRealClass, Collection valueObjectsCollectionToAdd, Collection valueObjectsCollection, String[] sourcePAttributeNames, String[] targetPAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    /**
     * @deprecated use {@link #addAllToCollection(Class targetRealClass, Collection valueObjectsCollection, Collection  valueObjectsCollectionToAdd,String[] sourcePAttributeNames,String[] targetPAttributeNames)}
     */
    public Collection addCollectionFromCollection(Class targetRealClass, Collection valueObjectsCollectionToAdd, Collection valueObjectsCollection, String[] sourcePAttributeNames, String[] targetPAttributeNames) throws RemoteException;

    public boolean addAllToCollection(Collection valueObjectsCollection, Collection valueObjectsCollectionToAdd, String[] pAttributeNames) throws RemoteException;

    public boolean addAllToCollection(Collection valueObjectsCollection, Collection valueObjectsCollectionToAdd, Class realClass) throws RemoteException;

    /**
     * @deprecated use {@link #addAllToCollection(Collection valueObjectsCollection, Collection  valueObjectsCollectionToAdd, Class realClass)}
     */
    public Collection mergeTwoCollections(Collection valueObjectsCollection, Collection valueObjectsCollectionToAdd, Class realClass) throws RemoteException;

    public boolean addAllToCollection(Class targetRealClass, Collection valueObjectsCollection, Collection valueObjectsCollectionToAdd, String[] sourcePAttributeNames, String[] targetPAttributeNames) throws RemoteException;

    public boolean addAllToCollection(Collection valueObjectsCollection, Map map, Collection valueObjectsCollectionToAdd, String pAttributeNameMapKey) throws RemoteException;

    public boolean retainAllInCollection(Object pInstance, String pAttributeName, Collection toRetainValueObjectsCollection) throws RemoteException;

    public boolean retainAllInCollection(Collection valueObjectsCollection, Collection toRetainValueObjectsCollection, Class realClass) throws RemoteException;

    public boolean retainAllInCollection(Collection valueObjectsCollection, Collection toRetainValueObjectsCollection, String[] pAttributeNames) throws RemoteException;

    public boolean retainAllInCollection(Collection valueObjectsCollection, Map map, String pAttributeNameMapKey) throws RemoteException;

    /**
     * @deprecated use {@link #retainAllInCollection(Collection valueObjectsCollection, Map map, String pAttributeNameMapKey)}
     */
    public void removeFromCollectionElementsNotInMap(Collection valueObjectsCollection, HashMap map, String pAttributeNameMapKey) throws RemoteException;

    public boolean addToRetainInCollection(Object pInstance, String pAttributeName, Collection valueObjectsCollectionToAddAndRetain) throws RemoteException;

    public boolean addToRetainInCollection(Collection valueObjectsCollection, Collection valueObjectsCollectionToAddAndRetain, Class realClass) throws RemoteException;

    public boolean addToRetainInCollection(Collection valueObjectsCollection, Collection valueObjectsCollectionToAddAndRetain, String[] pAttributeNames) throws RemoteException;

    public boolean addToRetainInCollection(Collection valueObjectsCollection, Map map, Collection valueObjectsCollectionToAddAndRetain, String pAttributeNameMapKey) throws RemoteException;

    /**
     * @deprecated use {@link #addToRetainInCollection(Collection valueObjectsCollection, Map map, Collection  valueObjectsCollectionToAddAndRetain, String pAttributeNameMapKey)}
     *
     */
    public void refreshCollectionWithOtherCollectionUsingMapAsRelation(Collection valueObjectsCollection, HashMap map, Collection valueObjectsCollectionToAddAndRetain, String pAttributeNameMapKey) throws RemoteException;

    public boolean removeFromCollection(Object pInstance, String pAttributeName, Object toRemovePInstance) throws RemoteException;

    public boolean removeFromCollection(Collection valueObjectsCollection, Object toRemovePInstance) throws RemoteException;

    /**
     * @deprecated use {@link #removeFromCollection(Object pInstance, String pAttributeName, Object toRemovePInstance)}
     */
    public boolean removeElementFromCollectionReference(Object pInstance, String pAttributeName, Object toRemovePInstance) throws RemoteException;

    public boolean removeAllFromCollection(Object pInstance, String pAttributeName, Collection valueObjectsCollectionToRemove) throws RemoteException;

    public Object createVOfromVO(Class sourceRealClass, String[] pkFieldNames, Object[] pkValues, Class targetRealClass, String[] sourcePAttributeNames, String[] targetPAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public Object createVOfromVO(Class sourceRealClass, String[] pkFieldNames, Object[] pkValues, Class targetRealClass, String[] sourcePAttributeNames, String[] targetPAttributeNames) throws RemoteException;

    public Object createVOfromVO(Class sourceRealClass, Object[] pkValues, Class targetRealClass, String[] sourcePAttributeNames, String[] targetPAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public Object createVOfromVO(Class sourceRealClass, Object[] pkValues, Class targetRealClass, String[] sourcePAttributeNames, String[] targetPAttributeNames) throws RemoteException;

    public Object createVOfromVO(Object sourcePInstance, Class targetRealClass, String[] sourcePAttributeNames, String[] targetPAttributeNames, String[] pAttributeNames, Object[] valuesArray) throws RemoteException;

    public Object createVOfromVO(Object sourcePInstance, Class targetRealClass, String[] sourcePAttributeNames, String[] targetPAttributeNames) throws RemoteException;

    public boolean addTreeToTree(Object sourceRootVO, Object targetRootVO, Collection sourceTreePaths, Collection targetTreePaths, Collection treeNodeSourcePAttributeNames, Collection treeNodeTargetPAttributeNames, Collection treeNodePAttributeNames, Collection treeNodeValuesArray) throws RemoteException;

    /**
     * @deprecated use {@link #addTreeToTree(Object sourceRootVO, Object targetRootVO,Collection sourceTreePaths,Collection targetTreePaths, Collection treeNodeSourcePAttributeNames, Collection treeNodeTargetPAttributeNames, Collection treeNodePAttributeNames, Collection treeNodeValuesArray )}
     * */
    public Object addTreesToTrees(Object sourceRootVO, Object targetRootVO, Collection sourceTreePaths, Collection targetTreePaths, Collection treeNodeSourcePAttributeNames, Collection treeNodeTargetPAttributeNames, Collection treeNodePAttributeNames, Collection treeNodeValuesArray) throws RemoteException;

    public boolean addTreeToTree(Object sourceRootVO, Object targetRootVO, String sourceTreePath, String targetTreePath, String[][] nodeSourcePAttributeNames, String[][] nodeTargetPAttributeNames, String[][] nodePAttributeNames, Object[][] nodeValuesArray) throws RemoteException;

    public boolean addTreeToTree(Object sourceRootVO, Object targetRootVO, String sourceTreePath, String targetTreePath, Collection nodeSourcePAttributeNamesCollection, Collection nodeTargetPAttributeNamesCollection, Collection nodePAttributeNamesCollection, Collection nodeValuesArrayCollection) throws RemoteException;
}

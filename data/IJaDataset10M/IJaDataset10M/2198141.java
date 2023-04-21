package net.sf.csutils.core.registry;

import java.util.Collection;
import java.util.List;
import javax.xml.registry.JAXRException;
import javax.xml.registry.LifeCycleManager;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.RegistryEntry;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.User;
import net.sf.csutils.core.model.QName;
import net.sf.csutils.core.model.ROClassification;
import net.sf.csutils.core.model.ROMetaModel;
import net.sf.csutils.core.model.RORelation;
import net.sf.csutils.core.model.ROSlot;
import net.sf.csutils.core.model.ROType;
import net.sf.csutils.core.query.CsqlStatement;

/**
 * <p>The {@link ROModelAccessor model accessor} is used to operate on the
 * registry model. In other words, it is used to read and edit model
 * instances.<p>
 * <p>Methods in this interface are typically present in three flavours.
 * For example, there are {@link #getSlotValue(ROSlot, RegistryObject)},
 * {@link #getSlotValue(ROType, String, RegistryObject)}, and
 * {@link #getSlotValue(String, RegistryObject)}. Basically, these are
 * the same methods, performing the same operation. The main difference
 * is performance: For example, {@link #getSlotValue(String, RegistryObject)}
 * will first {@link #findType(String) lookup} the type of the registry
 * object and then invoke {@link #getSlotValue(ROType, String, RegistryObject)}
 * internally. In other words, the cost of looking up the type will be added.
 * Likewise, {@link #getSlotValue(String, RegistryObject)} will
 * {@link #findSlot(ROType, String) lookup} the definition of the given slot
 * first and then invoke {@link #getSlotValue(ROSlot, RegistryObject)}
 * internally.</p>
 * <p>If you have to perform multiple operations in turn, for example
 * set multiple slot values at once, then you should try to avoid
 * the cost of these lookups. For example, if you have to configure
 * the same slot on multiple registry objects, then it is worth
 * to lookup the {@link ROSlot slot} first and invoke
 * {@link #setSlotValue(ROSlot, RegistryObject, String)} on all the registry
 * objects. Likewise, if you have to configure multiple slots on the
 * same registry object, then it might be worth to lookup the
 * registry objects {@link ROType type} first and use
 * {@link #setSlotValue(ROType, String, RegistryObject, String)}.</p>
 */
public interface ROModelAccessor {

    /**
     * Returns the accessors registry facade.
     * @return The registry facade to use.
     */
    ModelDrivenRegistryFacade getRegistryFacade();

    /**
     * Returns the meta model.
     * @return The meta model.
     */
    ROMetaModel getMetaModel();

    /**
     * Finds the registry object type with the given name.
     * @param pTypeName The registry object types name.
     * @return ROType The matching registry object type, if any, or null.
     * @throws JAXRException The operation failed.
     * @see #requireType(QName)
     * @see #findType(String)
     */
    ROType findType(QName pTypeName) throws JAXRException;

    /**
     * Finds the registry object type with the given name.
     * @param pTypeName The registry object types name.
     * @return ROType The matching registry object type; never null,
     *   because an exception is thrown in that case.
     * @throws JAXRException The operation failed.
     * @see #findType(QName)
     * @see #requireType(String)
     */
    ROType requireType(QName pTypeName) throws JAXRException;

    /**
     * Finds the registry object type with the given name.
     * @param pTypeName The registry object types name.
     * @return The matching registry object type, if any, or null.
     * @throws JAXRException The operation failed.
     * @see #requireType(String)
     * @see #findType(QName)
     */
    ROType findType(String pTypeName) throws JAXRException;

    /**
     * Finds the registry object type with the given name.
     * @param pTypeName The registry object types name.
     * @return The matching registry object type; never null, because
     *   an exception is thrown in that case.
     * @throws JAXRException The operation failed. In particular,
     *   this might be the case, if no type with the given name
     *   was found.
     * @see #findType(String)
     * @see #requireType(QName)
     */
    ROType requireType(String pTypeName) throws JAXRException;

    /**
     * Finds the registry object type of the given registry object instance.
     * @param pInstance The instance for which the type is being queried.
     * @return The matching registry object type, if any, or null.
     * @throws JAXRException The operation failed. In particular,
     *   this might be the case, if no type with the given name
     *   was found.
     * @see #findType(String)
     * @see #requireType(RegistryObject)
     */
    ROType findType(RegistryObject pInstance) throws JAXRException;

    /**
     * Finds the registry object type of the given registry object instance.
     * @param pInstance The instance for which the type is being queried.
     * @return The matching registry object type; never null, because
     *   an exception is thrown in that case.
     * @throws JAXRException The operation failed. In particular,
     *   this might be the case, if no type with the given name
     *   was found.
     * @see #requireType(String)
     * @see #findType(RegistryObject)
     */
    ROType requireType(RegistryObject pInstance) throws JAXRException;

    /**
     * Finds a slot attribute with the given name in the given registry object type.
     * @param pType The registry object type.
     * @param pSlotName Then slots name.
     * @return The matching slot, if any, or null.
     * @throws JAXRException The operation failed. In particular, this
     *   might be the case, because an attribute with the given name was
     *   found, but the attributes type is no slot attribute.
     * @see #requireSlot(ROType, String)
     */
    ROSlot findSlot(ROType pType, String pSlotName) throws JAXRException;

    /**
     * Finds a slot attribute with the given name in the given registry object type.
     * @param pType The registry object type.
     * @param pSlotName Then slots name.
     * @return The matching slot; never null, because an exception is thrown in
     *   that case.
     * @throws JAXRException The operation failed. In particular, this
     *   might be the case, if no attribute with the given name was found.
     *   Likewise, because an attribute with the given name was
     *   found, but the attributes is no slot attribute.
     * @see #findSlot(ROType, String)
     */
    ROSlot requireSlot(ROType pType, String pSlotName) throws JAXRException;

    /**
     * Finds a relationship attribute with the given name in the given registry object type.
     * @param pType The registry object type.
     * @param pRelationName The attributes name.
     * @return The matching slot, if any, or null.
     * @throws JAXRException The operation failed.
     */
    RORelation findRelation(ROType pType, String pRelationName) throws JAXRException;

    /**
     * Finds a relationship attribute with the given name in the given registry object type.
     * @param pType The registry object type.
     * @param pRelationName The attributes name.
     * @return The matching relation; never null, as an exception is thrown in that case.
     * @throws JAXRException The operation failed. In particular, this
     *   might be the case, if no attribute with the given name was found.
     *   Likewise, because an attribute with the given name was
     *   found, but the attributes is no relationship attribute.
     */
    RORelation requireRelation(ROType pType, String pRelationName) throws JAXRException;

    /**
     * Finds a classification attribute with the given name in the given registry object type.
     * @param pType The registry object type.
     * @param pClassificationName The attributes name.
     * @return The matching classification attribute, if any, or null.
     * @throws JAXRException The operation failed.
     */
    ROClassification findClassification(ROType pType, String pClassificationName) throws JAXRException;

    /**
     * Finds a classification attribute with the given name in the given registry object type.
     * @param pType The registry object type.
     * @param pRelationName The attributes name.
     * @return The matching classification attribute; never null, as an exception is thrown in that case.
     * @throws JAXRException The operation failed. In particular, this
     *   might be the case, if no attribute with the given name was found.
     *   Likewise, because an attribute with the given name was
     *   found, but the attributes is no relationship attribute.
     */
    ROClassification requireClassification(ROType pType, String pClassificationName) throws JAXRException;

    /**
     * Creates a registry object with the given type.
     * @param pType The registry object type.
     * @param <RO> The type of the returned instance depends on the given
     *   registry object type. For example, it will be {@link Organization},
     *   if the type is {@link LifeCycleManager#ORGANIZATION}, {@link User}
     *   in the case of {@link LifeCycleManager#USER}, or
     *   {@link RegistryEntry} for {@link LifeCycleManager#REGISTRY_ENTRY}
     *   or a custom object type.
     * @return The created registry object instance.
     * @throws JAXRException The operation failed.
     */
    <RO extends RegistryObject> RO createInstance(ROType pType) throws JAXRException;

    /**
     * Creates a registry object with the given type.
     * @param pType The registry object type.
     * @param <RO> The type of the returned instance depends on the given
     *   registry object type. For example, it will be {@link Organization},
     *   if the type is {@link LifeCycleManager#ORGANIZATION}, {@link User}
     *   in the case of {@link LifeCycleManager#USER}, or
     *   {@link RegistryEntry} for {@link LifeCycleManager#REGISTRY_ENTRY}
     *   or a custom object type.
     * @return The created registry object instance.
     * @throws JAXRException The operation failed.
     */
    <RO extends RegistryObject> RO createInstance(QName pType) throws JAXRException;

    /**
     * Creates a registry object with the given type.
     * @param pType The registry object type.
     * @param <RO> The type of the returned instance depends on the given
     *   registry object type. For example, it will be {@link Organization},
     *   if the type is {@link LifeCycleManager#ORGANIZATION}, {@link User}
     *   in the case of {@link LifeCycleManager#USER}, or
     *   {@link RegistryEntry} for {@link LifeCycleManager#REGISTRY_ENTRY}
     *   or a custom object type.
     * @return The created registry object instance.
     * @throws JAXRException The operation failed.
     */
    <RO extends RegistryObject> RO createInstance(String pType) throws JAXRException;

    /**
     * Creates a query with the given CSQL statement.
     * @param pQuery The query to perform.
     * @return The prepared CSQL statement.
     * @throws JAXRException Preparing the statement failed.
     */
    CsqlStatement prepareStatement(String pQuery) throws JAXRException;

    /**
     * Creates a query with the given CSQL statement, executes it,
     * and returns the result.
     * @param pQuery The query to perform.
     * @param pParameters The parameters to set on the statement.
     * @return The prepared CSQL statement.
     * @throws JAXRException Preparing the statement failed.
     */
    List<RegistryObject> executeQuery(String pQuery, Object... pParameters) throws JAXRException;

    /**
     * Creates a query with the given CSQL statement, executes it,
     * and returns the result.
     * @param pQuery The query to perform.
     * @param pParameters The parameters to set on the statement.
     * @return The prepared CSQL statement.
     * @throws JAXRException Preparing the statement failed.
     */
    List<RegistryObject[]> executeArrayQuery(String pQuery, Object... pParameters) throws JAXRException;

    /**
     * Sets the given slots value on the given registry object.
     * @param pSlot The slot to set.
     * @param pObject The object on which to set the slot value.
     * @param pValue The value to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValue(ROSlot pSlot, RegistryObject pObject, String pValue) throws JAXRException;

    /**
     * Sets the given slots value on the given registry object.
     * @param pType The registry objects type.
     * @param pSlotName The slots name.
     * @param pObject The object on which to set the slot value.
     * @param pValue The value to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValue(ROType pType, String pSlotName, RegistryObject pObject, String pValue) throws JAXRException;

    /**
     * Sets the given slots value on the given registry object.
     * @param pSlotName The slots name.
     * @param pObject The object on which to set the slot value.
     * @param pValue The value to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValue(String pSlotName, RegistryObject pObject, String pValue) throws JAXRException;

    /**
     * Sets the given slots values on the given registry object.
     * @param pSlot The slot to set.
     * @param pObject The object on which to set the slot values.
     * @param pValues The values to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValues(ROSlot pSlot, RegistryObject pObject, Collection<String> pValues) throws JAXRException;

    /**
     * Sets the given slots values on the given registry object.
     * @param pType The registry objects type.
     * @param pSlotName The slots name.
     * @param pObject The object on which to set the slot values.
     * @param pValues The values to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValues(ROType pType, String pSlotName, RegistryObject pObject, Collection<String> pValues) throws JAXRException;

    /**
     * Sets the given slots values on the given registry object.
     * @param pSlotName The slots name.
     * @param pObject The object on which to set the slot values.
     * @param pValues The values to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValues(String pSlotName, RegistryObject pObject, Collection<String> pValues) throws JAXRException;

    /**
     * Sets the given slots values on the given registry object.
     * @param pSlot The slot to set.
     * @param pObject The object on which to set the slot values.
     * @param pValues The values to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValues(ROSlot pSlot, RegistryObject pObject, String... pValues) throws JAXRException;

    /**
     * Sets the given slots values on the given registry object.
     * @param pType The registry objects type.
     * @param pSlotName The slots name.
     * @param pObject The object on which to set the slot values.
     * @param pValues The values to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValues(ROType pType, String pSlotName, RegistryObject pObject, String... pValues) throws JAXRException;

    /**
     * Sets the given slots values on the given registry object.
     * @param pSlotName The slots name.
     * @param pObject The object on which to set the slot values.
     * @param pValues The values to use.
     * @throws JAXRException The operation failed.
     */
    void setSlotValues(String pSlotName, RegistryObject pObject, String... pValues) throws JAXRException;

    /**
     * Returns the given slots values from the given registry object.
     * @param pSlot The slot to read.
     * @param pObject The object from which to read the slot value.
     * @throws JAXRException The operation failed.
     * @return The slot value.
     */
    String getSlotValue(ROSlot pSlot, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given slots values from the given registry object.
     * @param pType The registry objects type.
     * @param pSlotName The slots name.
     * @param pObject The object from which to read the slot value.
     * @throws JAXRException The operation failed.
     * @return The slot value.
     */
    String getSlotValue(ROType pType, String pSlotName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given slots values from the given registry object.
     * @param pSlotName The slots name.
     * @param pObject The object from which to read the slot value.
     * @throws JAXRException The operation failed.
     * @return The slot values.
     */
    String getSlotValue(String pSlotName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given slots values from the given registry object.
     * @param pSlot The slot to read.
     * @param pObject The object from which to read the slot value.
     * @throws JAXRException The operation failed.
     * @return The slot values.
     */
    Collection<String> getSlotValues(ROSlot pSlot, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given slots values from the given registry object.
     * @param pType The registry objects type.
     * @param pSlotName The slots name.
     * @param pObject The object from which to read the slot value.
     * @throws JAXRException The operation failed.
     * @return The slot values.
     */
    Collection<String> getSlotValues(ROType pType, String pSlotName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given slots values from the given registry object.
     * @param pType The registry objects type.
     * @param pSlotName The slots name.
     * @param pObject The object from which to read the slot value.
     * @throws JAXRException The operation failed.
     * @return The slot values.
     */
    Collection<String> getSlotValues(String pSlotName, RegistryObject pObject) throws JAXRException;

    /**
     * Sets the given relations value on the given registry object.
     * @param pRelation The relation to set.
     * @param pObject The object on which to set the relation value.
     * @param pValue The target object to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValue(RORelation pRelation, RegistryObject pObject, RegistryObject pValue) throws JAXRException;

    /**
     * Sets the given relations value on the given registry object.
     * @param pType The registry objects type.
     * @param pRelationName The relations name.
     * @param pObject The object on which to set the relation value.
     * @param pValue The target object to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValue(ROType pType, String pRelationName, RegistryObject pObject, RegistryObject pValue) throws JAXRException;

    /**
     * Sets the given relations value on the given registry object.
     * @param pRelationName The relations name.
     * @param pObject The object on which to set the relation value.
     * @param pValues The target object to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValue(String pRelationName, RegistryObject pObject, RegistryObject pValues) throws JAXRException;

    /**
     * Sets the given relations values on the given registry object.
     * @param pRelation The relation to set.
     * @param pObject The object on which to set the relation value.
     * @param pValues The target objects to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValues(RORelation pRelation, RegistryObject pObject, RegistryObject... pValues) throws JAXRException;

    /**
     * Sets the given relations values on the given registry object.
     * @param pType The registry objects type.
     * @param pRelationName The relations name.
     * @param pObject The object on which to set the relation value.
     * @param pValues The target objects to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValues(ROType pType, String pRelationName, RegistryObject pObject, RegistryObject... pValues) throws JAXRException;

    /**
     * Sets the given relations values on the given registry object.
     * @param pRelationName The relations name.
     * @param pObject The object on which to set the relation value.
     * @param pValues The target objects to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValues(String pRelationName, RegistryObject pObject, RegistryObject... pValues) throws JAXRException;

    /**
     * Sets the given relations values on the given registry object.
     * @param pRelation The relation to set.
     * @param pObject The object on which to set the relation value.
     * @param pValues The target objects to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValues(RORelation pRelation, RegistryObject pObject, Collection<RegistryObject> pValues) throws JAXRException;

    /**
     * Sets the given relations values on the given registry object.
     * @param pType The registry objects type.
     * @param pRelationName The relations name.
     * @param pObject The object on which to set the relation value.
     * @param pValues The target objects to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValues(ROType pType, String pRelationName, RegistryObject pObject, Collection<RegistryObject> pValues) throws JAXRException;

    /**
     * Sets the given relations values on the given registry object.
     * @param pRelationName The relations name.
     * @param pObject The object on which to set the relation value.
     * @param pValues The target objects to use as the relations value.
     * @throws JAXRException The operation failed.
     */
    void setRelationValues(String pRelationName, RegistryObject pObject, Collection<RegistryObject> pValues) throws JAXRException;

    /**
     * Returns the given relations value from the given registry object.
     * @param pRelation The relation to read.
     * @param pObject The object from which to read the relation value.
     * @throws JAXRException The operation failed.
     * @return relations target object.
     */
    RegistryObject getRelationValue(RORelation pRelation, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given relations value from the given registry object.
     * @param pType The registry objects type.
     * @param pRelationName The relations name.
     * @param pObject The obThe object from which to read the relation value.
     * @throws JAXRException The operation failed.
     * @return relations target object.
     */
    RegistryObject getRelationValue(ROType pType, String pRelationName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given relations value from the given registry object.
     * @param pRelationName The relations name.
     * @param pObject The object from which to read the relation value.
     * @throws JAXRException The operation failed.
     * @return relations target object.
     */
    RegistryObject getRelationValue(String pRelationName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given relations values from the given registry object.
     * @param pRelation The relation to read.
     * @param pObject The object from which to read the relation values.
     * @throws JAXRException The operation failed.
     * @return relations target objects.
     */
    Collection<RegistryObject> getRelationValues(RORelation pRelation, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given relations values from the given registry object.
     * @param pType The registry objects type.
     * @param pRelationName The relations name.
     * @param pObject The object from which to read the relation values.
     * @throws JAXRException The operation failed.
     * @return relations target objects.
     */
    Collection<RegistryObject> getRelationValues(ROType pType, String pRelationName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given relations values from the given registry object.
     * @param pRelationName The relations name.
     * @param pObject The object from which to read the relation values.
     * @throws JAXRException The operation failed.
     * @return relations target objects.
     */
    Collection<RegistryObject> getRelationValues(String pRelationName, RegistryObject pObject) throws JAXRException;

    /**
     * Sets the given classifications value on the given registry object.
     * @param pClassification The classification to set.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classification value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValue(ROClassification pClassification, RegistryObject pObject, Concept pValue) throws JAXRException;

    /**
     * Sets the given classifications value on the given registry object.
     * @param pType The registry objects type.
     * @param pClassificationName The classifications name.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classifications value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValue(ROType pType, String pClassificationName, RegistryObject pObject, Concept pValue) throws JAXRException;

    /**
     * Sets the given classifications value on the given registry object.
     * @param pClassificationName The classifications name.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classification value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValue(String pClassificationName, RegistryObject pObject, Concept pValues) throws JAXRException;

    /**
     * Sets the given classifications values on the given registry object.
     * @param pClassification The classification to set.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classification value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValues(ROClassification pClassification, RegistryObject pObject, Concept... pValues) throws JAXRException;

    /**
     * Sets the given classifications values on the given registry object.
     * @param pType The registry objects type.
     * @param pClassificationName The classifications name.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classification value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValues(ROType pType, String pClassificationName, RegistryObject pObject, Concept... pValues) throws JAXRException;

    /**
     * Sets the given classifications values on the given registry object.
     * @param pClassificationName The classifications name.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classification value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValues(String pClassificationName, RegistryObject pObject, Concept... pValues) throws JAXRException;

    /**
     * Sets the given classifications values on the given registry object.
     * @param pClassification The classification to set.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classification value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValues(ROClassification pClassification, RegistryObject pObject, Collection<Concept> pValues) throws JAXRException;

    /**
     * Sets the given classifications values on the given registry object.
     * @param pType The registry objects type.
     * @param pClassificationName The classifications name.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classification value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValues(ROType pType, String pClassificationName, RegistryObject pObject, Collection<Concept> pValues) throws JAXRException;

    /**
     * Sets the given classifications values on the given registry object.
     * @param pClassificationName The classifications name.
     * @param pObject The object on which to set the classification value.
     * @param pValue The target object to use as the classification value.
     * @throws JAXRException The operation failed.
     */
    void setClassificationValues(String pClassificationName, RegistryObject pObject, Collection<Concept> pValues) throws JAXRException;

    /**
     * Returns the given classifications value from the given registry object.
     * @param pClassification The classification to read.
     * @param pObject The object from which to read the classification value.
     * @throws JAXRException The operation failed.
     * @return classifications target object.
     */
    Concept getClassificationValue(ROClassification pClassification, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given classifications value from the given registry object.
     * @param pType The registry objects type.
     * @param pClassificationName The classifications name.
     * @param pObject The object from which to read the classification value.
     * @throws JAXRException The operation failed.
     * @return classifications target object.
     */
    Concept getClassificationValue(ROType pType, String pClassificationName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given relaticlassifications from the given registry object.
     * @param pClassificationName The classifications name.
     * @param pObject The object from which to read the classification value.
     * @throws JAXRException The operation failed.
     * @return classifications target object.
     */
    Concept getClassificationValue(String pClassificationName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given classifications values from the given registry object.
     * @param pClassification The classification to read.
     * @param pObject The object from which to read the classification values.
     * @throws JAXRException The operation failed.
     * @return classifications target objects.
     */
    Collection<Concept> getClassificationValues(ROClassification pClassification, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given classifications values from the given registry object.
     * @param pType The registry objects type.
     * @param pClassificationName The classifications name.
     * @param pObject The object from which to read the classification values.
     * @throws JAXRException The operation failed.
     * @return classifications target objects.
     */
    Collection<Concept> getClassificationValues(ROType pType, String pClassificationName, RegistryObject pObject) throws JAXRException;

    /**
     * Returns the given classifications values from the given registry object.
     * @param pClassificationName The classifications name.
     * @param pObject The object from which to read the classification values.
     * @throws JAXRException The operation failed.
     * @return classifications target objects.
     */
    Collection<Concept> getClassificationValues(String pClassificationName, RegistryObject pObject) throws JAXRException;
}

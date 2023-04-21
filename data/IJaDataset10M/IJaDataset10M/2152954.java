package org.datanucleus.store.rdbms.scostore;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.ManagedConnection;
import org.datanucleus.ObjectManager;
import org.datanucleus.StateManager;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.CollectionMetaData;
import org.datanucleus.metadata.DiscriminatorStrategy;
import org.datanucleus.metadata.FieldRole;
import org.datanucleus.metadata.MetaDataUtils;
import org.datanucleus.metadata.Relation;
import org.datanucleus.metadata.OrderMetaData.FieldOrder;
import org.datanucleus.store.mapped.DatastoreClass;
import org.datanucleus.store.mapped.DatastoreIdentifier;
import org.datanucleus.store.mapped.IdentifierFactory;
import org.datanucleus.store.mapped.expression.LogicSetExpression;
import org.datanucleus.store.mapped.expression.QueryExpression;
import org.datanucleus.store.mapped.expression.ScalarExpression;
import org.datanucleus.store.mapped.expression.UnboundVariable;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.mapped.mapping.ReferenceMapping;
import org.datanucleus.store.rdbms.SQLController;
import org.datanucleus.store.rdbms.mapping.RDBMSMapping;
import org.datanucleus.store.rdbms.query.DiscriminatorIteratorStatement;
import org.datanucleus.store.rdbms.query.IncompatibleQueryElementTypeException;
import org.datanucleus.store.rdbms.query.UnionIteratorStatement;
import org.datanucleus.store.rdbms.table.CollectionTable;
import org.datanucleus.util.ClassUtils;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.StringUtils;

/**
 * Representation of the backing store for a join-table List.
 * Uses a Join table, so we use 3 tables - owner table, join table and element table.
 *
 * @version $Revision: 1.51 $
 **/
public class JoinListStore extends AbstractListStore {

    /**
     * Constructor.
     * @param joinTable The join table
     * @param clr ClassLoader resolver
     */
    public JoinListStore(AbstractMemberMetaData fmd, CollectionTable joinTable, ClassLoaderResolver clr) {
        super(joinTable.getStoreManager(), clr);
        this.containerTable = joinTable;
        setOwnerMemberMetaData(fmd);
        listName = "list";
        ownerMapping = joinTable.getOwnerMapping();
        elementMapping = joinTable.getElementMapping();
        orderMapping = joinTable.getOrderMapping();
        if (ownerMemberMetaData.getOrderMetaData() != null && !ownerMemberMetaData.getOrderMetaData().isIndexedList()) {
            indexedList = false;
        }
        if (orderMapping == null && indexedList) {
            throw new NucleusUserException(LOCALISER.msg("056044", ownerMemberMetaData.getFullFieldName(), joinTable.toString()));
        }
        relationDiscriminatorMapping = joinTable.getRelationDiscriminatorMapping();
        relationDiscriminatorValue = joinTable.getRelationDiscriminatorValue();
        elementType = fmd.getCollection().getElementType();
        elementsAreEmbedded = joinTable.isEmbeddedElement();
        elementsAreSerialised = joinTable.isSerialisedElement();
        if (elementsAreSerialised) {
            elementInfo = null;
        } else {
            Class element_class = clr.classForName(elementType);
            if (ClassUtils.isReferenceType(element_class)) {
                String[] implNames = MetaDataUtils.getInstance().getImplementationNamesForReferenceField(ownerMemberMetaData, FieldRole.ROLE_COLLECTION_ELEMENT, clr);
                elementInfo = new ElementInfo[implNames.length];
                for (int i = 0; i < implNames.length; i++) {
                    DatastoreClass table = storeMgr.getDatastoreClass(implNames[i], clr);
                    AbstractClassMetaData cmd = storeMgr.getOMFContext().getMetaDataManager().getMetaDataForClass(implNames[i], clr);
                    elementInfo[i] = new ElementInfo(cmd, table);
                }
            } else {
                emd = storeMgr.getOMFContext().getMetaDataManager().getMetaDataForClass(element_class, clr);
                if (emd != null) {
                    if (!elementsAreEmbedded) {
                        elementInfo = getElementInformationForClass();
                    } else {
                        elementInfo = null;
                    }
                } else {
                    elementInfo = null;
                }
            }
        }
    }

    /**
     * Internal method to add element(s) to the List.
     * Performs the add in 2 steps.
     * <ol>
     * <li>Shift all existing elements into their new positions so we can insert.</li>
     * <li>Insert all new elements directly at their desired positions>/li>
     * </ol>
     * Both steps can be batched (separately).
     * @param sm The state manager
     * @param start The start location (if required)
     * @param atEnd Whether to add the element at the end
     * @param c The collection of objects to add.
     * @param size Current size of list if known. -1 if not known
     * @return Whether it was successful
     */
    protected boolean internalAdd(StateManager sm, int start, boolean atEnd, Collection c, int size) {
        if (c == null || c.size() == 0) {
            return true;
        }
        int shift = c.size();
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            validateElementForWriting(sm, element, null);
            if (ownerMemberMetaData.getRelationType(clr) == Relation.ONE_TO_MANY_BI && sm.getObjectManager().getOMFContext().getPersistenceConfiguration().getBooleanProperty("datanucleus.manageRelationships")) {
                StateManager elementSM = sm.getObjectManager().findStateManager(element);
                if (elementSM != null) {
                    AbstractMemberMetaData[] relatedMmds = ownerMemberMetaData.getRelatedMemberMetaData(clr);
                    Object elementOwner = elementSM.provideField(relatedMmds[0].getAbsoluteFieldNumber());
                    if (elementOwner == null) {
                        NucleusLogger.JDO.info(LOCALISER.msg("056037", StringUtils.toJVMIDString(sm.getObject()), ownerMemberMetaData.getFullFieldName(), StringUtils.toJVMIDString(elementSM.getObject())));
                        elementSM.replaceField(relatedMmds[0].getAbsoluteFieldNumber(), sm.getObject(), false);
                    } else if (elementOwner != sm.getObject() && sm.getReferencedPC() == null) {
                        throw new NucleusUserException(LOCALISER.msg("056038", StringUtils.toJVMIDString(sm.getObject()), ownerMemberMetaData.getFullFieldName(), StringUtils.toJVMIDString(elementSM.getObject()), StringUtils.toJVMIDString(elementOwner)));
                    }
                }
            }
        }
        int currentListSize = 0;
        if (size < 0) {
            currentListSize = size(sm);
        } else {
            currentListSize = size;
        }
        String addStmt = getAddStmt();
        try {
            ObjectManager om = sm.getObjectManager();
            ManagedConnection mconn = storeMgr.getConnection(om);
            SQLController sqlControl = storeMgr.getSQLController();
            try {
                if (!atEnd && start != currentListSize) {
                    boolean batched = currentListSize - start > 0;
                    for (int i = currentListSize - 1; i >= start; i--) {
                        internalShift(sm, mconn, batched, i, shift, (i == start));
                    }
                } else {
                    start = currentListSize;
                }
                int jdbcPosition = 1;
                boolean batched = (c.size() > 1);
                iter = c.iterator();
                while (iter.hasNext()) {
                    Object element = iter.next();
                    PreparedStatement ps = sqlControl.getStatementForUpdate(mconn, addStmt, batched);
                    try {
                        jdbcPosition = 1;
                        jdbcPosition = populateOwnerInStatement(sm, om, ps, jdbcPosition);
                        jdbcPosition = populateElementInStatement(om, ps, element, jdbcPosition);
                        if (orderMapping != null) {
                            jdbcPosition = populateOrderInStatement(om, ps, start, jdbcPosition);
                        }
                        if (relationDiscriminatorMapping != null) {
                            jdbcPosition = populateRelationDiscriminatorInStatement(om, ps, jdbcPosition);
                        }
                        start++;
                        sqlControl.executeStatementUpdate(mconn, addStmt, ps, !iter.hasNext());
                    } finally {
                        sqlControl.closeStatement(mconn, ps);
                    }
                }
            } finally {
                mconn.release();
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER.msg("056009", addStmt), e);
        }
        return true;
    }

    /**
     * Method to set an object in the List.
     * @param sm The state manager
     * @param index The item index
     * @param element What to set it to.
     * @param allowDependentField Whether to allow dependent field deletes
     * @return The value before setting.
     **/
    public Object set(StateManager sm, int index, Object element, boolean allowDependentField) {
        validateElementForWriting(sm, element, null);
        Object o = get(sm, index);
        String setStmt = getSetStmt();
        try {
            ObjectManager om = sm.getObjectManager();
            ManagedConnection mconn = storeMgr.getConnection(om);
            SQLController sqlControl = storeMgr.getSQLController();
            try {
                PreparedStatement ps = sqlControl.getStatementForUpdate(mconn, setStmt, false);
                try {
                    int jdbcPosition = 1;
                    jdbcPosition = populateElementInStatement(om, ps, element, jdbcPosition);
                    jdbcPosition = populateOwnerInStatement(sm, om, ps, jdbcPosition);
                    jdbcPosition = populateOrderInStatement(om, ps, index, jdbcPosition);
                    if (relationDiscriminatorMapping != null) {
                        jdbcPosition = populateRelationDiscriminatorInStatement(om, ps, jdbcPosition);
                    }
                    sqlControl.executeStatementUpdate(mconn, setStmt, ps, true);
                } finally {
                    sqlControl.closeStatement(mconn, ps);
                }
            } finally {
                mconn.release();
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER.msg("056015", setStmt), e);
        }
        CollectionMetaData collmd = ownerMemberMetaData.getCollection();
        if (collmd.isDependentElement() && !collmd.isEmbeddedElement() && allowDependentField) {
            if (o != null && !contains(sm, o)) {
                sm.getObjectManager().deleteObjectInternal(o);
            }
        }
        return o;
    }

    /**
     * Generates the statement for setting an item.
     * <PRE>
     * UPDATE LISTTABLE SET [ELEMENTCOL = ?]
     * [EMBEDDEDFIELD1=?, EMBEDDEDFIELD2=?, ...]
     * WHERE OWNERCOL = ? 
     * AND INDEXCOL = ?
     * [AND DISTINGUISHER=?]
     * </PRE>
     * @return The Statement for setting an item
     */
    protected String getSetStmt() {
        if (setStmt == null) {
            StringBuffer stmt = new StringBuffer();
            stmt.append("UPDATE ");
            stmt.append(containerTable.toString());
            stmt.append(" SET ");
            for (int i = 0; i < elementMapping.getNumberOfDatastoreFields(); i++) {
                if (i > 0) {
                    stmt.append(",");
                }
                stmt.append(elementMapping.getDataStoreMapping(i).getDatastoreField().getIdentifier().toString());
                stmt.append(" = ");
                stmt.append(((RDBMSMapping) elementMapping.getDataStoreMapping(i)).getUpdateInputParameter());
            }
            stmt.append(" WHERE ");
            for (int i = 0; i < ownerMapping.getNumberOfDatastoreFields(); i++) {
                if (i > 0) {
                    stmt.append(" AND ");
                }
                stmt.append(ownerMapping.getDataStoreMapping(i).getDatastoreField().getIdentifier().toString());
                stmt.append(" = ");
                stmt.append(((RDBMSMapping) ownerMapping.getDataStoreMapping(i)).getUpdateInputParameter());
            }
            for (int i = 0; i < orderMapping.getNumberOfDatastoreFields(); i++) {
                stmt.append(" AND ");
                stmt.append(orderMapping.getDataStoreMapping(i).getDatastoreField().getIdentifier().toString());
                stmt.append(" = ");
                stmt.append(((RDBMSMapping) orderMapping.getDataStoreMapping(i)).getUpdateInputParameter());
            }
            if (relationDiscriminatorMapping != null) {
                for (int i = 0; i < relationDiscriminatorMapping.getNumberOfDatastoreFields(); i++) {
                    stmt.append(" AND ");
                    stmt.append(relationDiscriminatorMapping.getDataStoreMapping(i).getDatastoreField().getIdentifier().toString());
                    stmt.append(" = ");
                    stmt.append(((RDBMSMapping) relationDiscriminatorMapping.getDataStoreMapping(i)).getUpdateInputParameter());
                }
            }
            setStmt = stmt.toString();
        }
        return setStmt;
    }

    /**
     * Convenience method to remove the specified element from the List.
     * @param element The element
     * @param ownerSM StateManager of the owner
     * @param size Current size of list if known. -1 if not known
     * @return Whether the List was modified
     */
    protected boolean internalRemove(StateManager ownerSM, Object element, int size) {
        boolean modified = false;
        if (indexedList) {
            Collection elements = new ArrayList();
            elements.add(element);
            int[] indices = getIndicesOf(ownerSM, elements);
            for (int i = 0; i < indices.length; i++) {
                removeAt(ownerSM, indices[i], size);
                modified = true;
            }
        } else {
            ObjectManager om = ownerSM.getObjectManager();
            ManagedConnection mconn = storeMgr.getConnection(om);
            try {
                int[] rcs = internalRemove(ownerSM, mconn, false, element, true);
                if (rcs != null) {
                    if (rcs[0] > 0) {
                        modified = true;
                    }
                }
            } catch (SQLException sqe) {
                String msg = LOCALISER.msg("056012", getRemoveStmt());
                NucleusLogger.DATASTORE.error(msg, sqe);
                throw new NucleusDataStoreException(msg, sqe, ownerSM.getObject());
            } finally {
                mconn.release();
            }
        }
        return modified;
    }

    /**
     * Remove all elements from a collection from the association owner vs
     * elements. Performs the removal in 3 steps. The first gets the indices
     * that will be removed (and the highest index present). The second step
     * removes these elements from the list. The third step updates the indices
     * of the remaining indices to fill the holes created.
     * @param sm State Manager for the container
     * @param elements Collection of elements to remove 
     * @return Whether the database was updated 
     */
    public boolean removeAll(StateManager sm, Collection elements, int size) {
        if (elements == null || elements.size() == 0) {
            return false;
        }
        boolean modified = false;
        int currentListSize = size(sm);
        int[] indices = getIndicesOf(sm, elements);
        String removeAllStmt = getRemoveAllStmt(elements);
        SQLController sqlControl = storeMgr.getSQLController();
        try {
            ObjectManager om = sm.getObjectManager();
            ManagedConnection mconn = storeMgr.getConnection(om);
            try {
                PreparedStatement ps = sqlControl.getStatementForUpdate(mconn, removeAllStmt, false);
                try {
                    int jdbcPosition = 1;
                    Iterator iter = elements.iterator();
                    while (iter.hasNext()) {
                        Object element = iter.next();
                        jdbcPosition = populateOwnerInStatement(sm, om, ps, jdbcPosition);
                        jdbcPosition = populateElementInStatement(om, ps, element, jdbcPosition);
                        if (relationDiscriminatorMapping != null) {
                            jdbcPosition = populateRelationDiscriminatorInStatement(om, ps, jdbcPosition);
                        }
                    }
                    int[] number = sqlControl.executeStatementUpdate(mconn, removeAllStmt, ps, true);
                    if (number[0] > 0) {
                        modified = true;
                    }
                } finally {
                    sqlControl.closeStatement(mconn, ps);
                }
            } finally {
                mconn.release();
            }
        } catch (SQLException e) {
            NucleusLogger.DATASTORE.error(e);
            throw new NucleusDataStoreException(LOCALISER.msg("056012", removeAllStmt), e);
        }
        try {
            boolean batched = allowsBatching();
            ObjectManager om = sm.getObjectManager();
            ManagedConnection mconn = storeMgr.getConnection(om);
            try {
                for (int i = 0; i < currentListSize; i++) {
                    int shift = 0;
                    boolean removed = false;
                    for (int j = 0; j < indices.length; j++) {
                        if (indices[j] == i) {
                            removed = true;
                            break;
                        }
                        if (indices[j] < i) {
                            shift++;
                        }
                    }
                    if (!removed && shift > 0) {
                        internalShift(sm, mconn, batched, i, -1 * shift, (i == currentListSize - 1));
                    }
                }
            } finally {
                mconn.release();
            }
        } catch (SQLException e) {
            NucleusLogger.DATASTORE.error(e);
            throw new NucleusDataStoreException(LOCALISER.msg("056012", removeAllStmt), e);
        }
        if (ownerMemberMetaData.getCollection().isDependentElement()) {
            sm.getObjectManager().deleteObjects(elements.toArray());
        }
        return modified;
    }

    /**
     * Method to remove an element from the specified position
     * @param sm The State Manager for the list
     * @param index The index of the element
     * @param size Current size of list (if known). -1 if not known
     */
    protected void removeAt(StateManager sm, int index, int size) {
        internalRemoveAt(sm, index, getRemoveAtStmt(), size);
    }

    /**
     * Generate statement for removing a collection of items from the List.
     * <PRE>
     * DELETE FROM LISTTABLE 
     * WHERE (OWNERCOL=? AND ELEMENTCOL=?) OR
     *      (OWNERCOL=? AND ELEMENTCOL=?) OR
     *      (OWNERCOL=? AND ELEMENTCOL=?)
     * </PRE>
     * @param elements Collection of elements to remove
     * @return Statement for deleting items from the List.
     **/
    protected String getRemoveAllStmt(Collection elements) {
        if (elements == null || elements.size() == 0) {
            return null;
        }
        StringBuffer stmt = new StringBuffer();
        stmt.append("DELETE FROM ");
        stmt.append(containerTable.toString());
        stmt.append(" WHERE ");
        Iterator elementsIter = elements.iterator();
        boolean first = true;
        while (elementsIter.hasNext()) {
            elementsIter.next();
            if (first) {
                stmt.append("(");
            } else {
                stmt.append(" OR (");
            }
            for (int i = 0; i < ownerMapping.getNumberOfDatastoreFields(); i++) {
                if (i > 0) {
                    stmt.append(" AND ");
                }
                stmt.append(ownerMapping.getDataStoreMapping(i).getDatastoreField().getIdentifier().toString());
                stmt.append(" = ");
                stmt.append(((RDBMSMapping) ownerMapping.getDataStoreMapping(i)).getUpdateInputParameter());
            }
            for (int i = 0; i < elementMapping.getNumberOfDatastoreFields(); i++) {
                stmt.append(" AND ");
                stmt.append(elementMapping.getDataStoreMapping(i).getDatastoreField().getIdentifier().toString());
                if (elementsAreSerialised) {
                    stmt.append(" LIKE ");
                } else {
                    stmt.append(" = ");
                }
                stmt.append(((RDBMSMapping) elementMapping.getDataStoreMapping(i)).getUpdateInputParameter());
            }
            if (relationDiscriminatorMapping != null) {
                for (int i = 0; i < relationDiscriminatorMapping.getNumberOfDatastoreFields(); i++) {
                    stmt.append(" AND ");
                    stmt.append(relationDiscriminatorMapping.getDataStoreMapping(i).getDatastoreField().getIdentifier().toString());
                    stmt.append(" = ");
                    stmt.append(((RDBMSMapping) relationDiscriminatorMapping.getDataStoreMapping(i)).getUpdateInputParameter());
                }
            }
            stmt.append(")");
            first = false;
        }
        return stmt.toString();
    }

    /**
     * Accessor for the iterator statement to retrieve element(s) in a range
     * from the List. This has 5 modes
     * <UL>
     * <LI>start and end have the same value and &ge;0 then get that
     * element</LI>
     * <LI>start and end have values &ge;0 and are different then get a
     * range</LI>
     * <LI>start is &ge;0 and end is -1 the get all from start (inclusive).</LI>
     * <LI>start is -1 and end is &ge;0 the get all up to end(exclusive).</LI> 
     * <LI>start and end values are both -1 then get all elements</LI> 
     * </UL>
     * @param ownerSM The StateManager
     * @param start_index The start position in the List. 
     * @param end_index The end position in the List. 
     * @return The QueryStatement.
     */
    protected QueryExpression getIteratorStatement(StateManager ownerSM, int start_index, int end_index) {
        QueryExpression stmt = null;
        final ClassLoaderResolver clr = ownerSM.getObjectManager().getClassLoaderResolver();
        if (elementsAreEmbedded || elementsAreSerialised) {
            stmt = dba.newQueryStatement(containerTable, clr);
            stmt.select(elementMapping);
        } else if (elementMapping instanceof ReferenceMapping) {
            stmt = dba.newQueryStatement(containerTable, clr);
        } else if (elementInfo != null) {
            for (int i = 0; i < elementInfo.length; i++) {
                final int elementNo = i;
                final Class elementCls = clr.classForName(elementInfo[elementNo].getClassName());
                QueryExpression elementStmt = null;
                if (elementInfo[elementNo].getDiscriminatorStrategy() != null && elementInfo[elementNo].getDiscriminatorStrategy() != DiscriminatorStrategy.NONE) {
                    if (ClassUtils.isReferenceType(clr.classForName(ownerMemberMetaData.getCollection().getElementType()))) {
                        String[] clsNames = storeMgr.getOMFContext().getMetaDataManager().getClassesImplementingInterface(ownerMemberMetaData.getCollection().getElementType(), clr);
                        Class[] cls = new Class[clsNames.length];
                        for (int j = 0; j < clsNames.length; j++) {
                            cls[j] = clr.classForName(clsNames[j]);
                        }
                        elementStmt = new DiscriminatorIteratorStatement(clr, cls, true, this.storeMgr, true, allowsNull, containerTable, elementMapping, elmIdentifier).getQueryStatement(null);
                    } else {
                        elementStmt = new DiscriminatorIteratorStatement(clr, new Class[] { elementCls }, true, this.storeMgr, true, allowsNull, containerTable, elementMapping, elmIdentifier).getQueryStatement(null);
                    }
                    iterateUsingDiscriminator = true;
                } else {
                    elementStmt = new UnionIteratorStatement(clr, elementCls, true, this.storeMgr, elementCls, elementMapping, containerTable, false, Boolean.TRUE, true, allowsNull).getQueryStatement(null);
                }
                if (stmt == null) {
                    stmt = elementStmt;
                } else {
                    stmt.union(elementStmt);
                }
            }
        } else {
            throw new NucleusUserException("Attempt to get iterator for List when insufficient information is available to perform the operation.");
        }
        ScalarExpression ownerExpr = ownerMapping.newScalarExpression(stmt, stmt.getMainTableExpression());
        ScalarExpression ownerVal = ownerMapping.newLiteral(stmt, ownerSM.getObject());
        stmt.andCondition(ownerExpr.eq(ownerVal), true);
        if (relationDiscriminatorMapping != null) {
            ScalarExpression distinguisherExpr = relationDiscriminatorMapping.newScalarExpression(stmt, stmt.getMainTableExpression());
            ScalarExpression distinguisherVal = relationDiscriminatorMapping.newLiteral(stmt, relationDiscriminatorValue);
            stmt.andCondition(distinguisherExpr.eq(distinguisherVal), true);
        }
        if (indexedList) {
            boolean returning_range = false;
            if (start_index == -1 && end_index == -1) {
                returning_range = true;
            } else if (start_index == end_index && start_index >= 0) {
                ScalarExpression indexExpr = orderMapping.newScalarExpression(stmt, stmt.getMainTableExpression());
                ScalarExpression indexVal = orderMapping.newLiteral(stmt, new Integer(start_index));
                stmt.andCondition(indexExpr.eq(indexVal), true);
            } else {
                if (start_index >= 0) {
                    ScalarExpression indexExpr = orderMapping.newScalarExpression(stmt, stmt.getMainTableExpression());
                    ScalarExpression indexVal = orderMapping.newLiteral(stmt, new Integer(start_index));
                    stmt.andCondition(indexExpr.gteq(indexVal), true);
                }
                if (end_index >= 0) {
                    ScalarExpression indexExpr = orderMapping.newScalarExpression(stmt, stmt.getMainTableExpression());
                    ScalarExpression indexVal = orderMapping.newLiteral(stmt, new Integer(end_index));
                    stmt.andCondition(indexExpr.lt(indexVal), true);
                }
                returning_range = true;
            }
            if (returning_range) {
                ScalarExpression exprIndex[] = new ScalarExpression[orderMapping.getNumberOfDatastoreFields()];
                boolean descendingOrder[] = new boolean[orderMapping.getNumberOfDatastoreFields()];
                exprIndex = orderMapping.newScalarExpression(stmt, stmt.getMainTableExpression()).getExpressionList().toArray();
                stmt.setOrdering(exprIndex, descendingOrder);
            }
        } else {
            if (elementInfo != null) {
                DatastoreClass elementTbl = elementInfo[0].getDatastoreClass();
                FieldOrder[] orderComponents = ownerMemberMetaData.getOrderMetaData().getFieldOrders();
                for (int i = 0; i < orderComponents.length; i++) {
                    String fieldName = orderComponents[i].getFieldName();
                    JavaTypeMapping fieldMapping = elementTbl.getFieldMapping(elementInfo[0].getAbstractClassMetaData().getMetaDataForMember(fieldName));
                    ScalarExpression exprIndex[] = new ScalarExpression[fieldMapping.getNumberOfDatastoreFields()];
                    boolean descendingOrder[] = new boolean[fieldMapping.getNumberOfDatastoreFields()];
                    for (int j = 0; j < descendingOrder.length; j++) {
                        descendingOrder[j] = !orderComponents[i].isForward();
                    }
                    exprIndex = fieldMapping.newScalarExpression(stmt, stmt.getTableExpression(elmIdentifier)).getExpressionList().toArray();
                    stmt.setOrdering(exprIndex, descendingOrder);
                }
            } else {
            }
        }
        return stmt;
    }

    /**
     * Utility to return a new QueryStatement.
     * @param sm The state manager
     * @param candidateClass The base class
     * @param candidateAlias Alias for the candidate
     * @return The QueryStatement
     **/
    public QueryExpression newQueryStatement(StateManager sm, String candidateClass, DatastoreIdentifier candidateAlias) {
        if (elementsAreEmbedded || elementsAreSerialised) {
            throw new NucleusUserException(LOCALISER.msg("056021"));
        }
        if (!clr.isAssignableFrom(elementType, candidateClass)) {
            throw new IncompatibleQueryElementTypeException(elementType, candidateClass);
        }
        ClassLoaderResolver clr = sm.getObjectManager().getClassLoaderResolver();
        DatastoreIdentifier listTableAlias = storeMgr.getIdentifierFactory().newIdentifier(IdentifierFactory.TABLE, listName);
        QueryExpression stmt = dba.newQueryStatement(containerTable, listTableAlias, clr);
        ScalarExpression ownerExpr = ownerMapping.newScalarExpression(stmt, stmt.getTableExpression(listTableAlias));
        ScalarExpression ownerVal = ownerMapping.newLiteral(stmt, sm.getObject());
        stmt.andCondition(ownerExpr.eq(ownerVal), true);
        if (storeMgr.getMappedTypeManager().isSupportedMappedType(candidateClass)) {
            stmt.select(listTableAlias, elementMapping);
        } else {
            DatastoreClass candidateTable = storeMgr.getDatastoreClass(candidateClass, clr);
            DatastoreIdentifier elementTblAlias = storeMgr.getIdentifierFactory().newIdentifier(IdentifierFactory.TABLE, "LIST_ELEMENTS");
            LogicSetExpression elementTblExpr = stmt.newTableExpression(candidateTable, elementTblAlias);
            JavaTypeMapping elementTableID = candidateTable.getIDMapping();
            ScalarExpression elmListExpr = elementMapping.newScalarExpression(stmt, stmt.getTableExpression(listTableAlias));
            ScalarExpression elmExpr = elementTableID.newScalarExpression(stmt, elementTblExpr);
            stmt.innerJoin(elmExpr, elmListExpr, elementTblExpr, true, true);
            stmt.selectScalarExpression(elementTableID.newScalarExpression(stmt, elementTblExpr));
        }
        return stmt;
    }

    /**
     * Method used in queries when contains() has been invoked to join the collection table to the element table.
     * @param stmt The Query Statement
     * @param parentStmt the parent Query Statement. If no parent, "parentStmt" must be equal to "stmt"
     * @param ownerMapping the mapping for the owner
     * @param ownerTblExpr Table Expression for the owner
     * @param filteredElementType The Class Type for the filtered element
     * @param elementExpr Expression for the element
     * @param elementTableAlias The SQL alias to assign to the element table expression
     * @param listTableAlias The alias for the "List" table. 
     * @param existsQuery Whether this is joining for an EXISTS query
     * @return expression for the join
     */
    public ScalarExpression joinElementsTo(QueryExpression stmt, QueryExpression parentStmt, JavaTypeMapping ownerMapping, LogicSetExpression ownerTblExpr, DatastoreIdentifier listTableAlias, Class filteredElementType, ScalarExpression elementExpr, DatastoreIdentifier elementTableAlias, boolean existsQuery) {
        ClassLoaderResolver clr = stmt.getClassLoaderResolver();
        if (!clr.isAssignableFrom(elementType, filteredElementType) && !clr.isAssignableFrom(filteredElementType, elementType)) {
            throw new IncompatibleQueryElementTypeException(elementType, filteredElementType.getName());
        }
        if (!existsQuery) {
            LogicSetExpression ownTblExpr = stmt.newTableExpression(containerTable, listTableAlias);
            if (!parentStmt.hasCrossJoin(ownTblExpr) && !stmt.getMainTableExpression().equals(ownTblExpr)) {
                stmt.crossJoin(ownTblExpr, true);
            }
            ScalarExpression ownerExpr = ownerMapping.newScalarExpression(stmt, ownerTblExpr);
            ScalarExpression ownerSetExpr = this.ownerMapping.newScalarExpression(stmt, stmt.getTableExpression(listTableAlias));
            stmt.andCondition(ownerExpr.eq(ownerSetExpr), true);
        }
        if (storeMgr.getMappedTypeManager().isSupportedMappedType(filteredElementType.getName())) {
            return elementMapping.newScalarExpression(stmt, stmt.getTableExpression(listTableAlias));
        } else if (elementsAreEmbedded || elementsAreSerialised) {
            return elementMapping.newScalarExpression(stmt, stmt.getTableExpression(listTableAlias));
        } else {
            DatastoreClass elementTable = storeMgr.getDatastoreClass(filteredElementType.getName(), clr);
            DatastoreClass joiningClass = (elementExpr.getLogicSetExpression() == null ? elementTable : (DatastoreClass) elementExpr.getLogicSetExpression().getMainTable());
            JavaTypeMapping elementTableID = joiningClass.getIDMapping();
            LogicSetExpression elmTblExpr = stmt.getTableExpression(elementTableAlias);
            if (elmTblExpr == null) {
                if (!(elementExpr instanceof UnboundVariable) && parentStmt != stmt) {
                    elmTblExpr = parentStmt.getTableExpression(elementTableAlias);
                }
                if (elmTblExpr == null) {
                    elmTblExpr = stmt.newTableExpression(elementTable, elementTableAlias);
                }
            }
            if (!parentStmt.getMainTableExpression().equals(elmTblExpr) && !parentStmt.hasCrossJoin(elmTblExpr)) {
                stmt.crossJoin(elmTblExpr, true);
            }
            ScalarExpression elmListExpr = elementMapping.newScalarExpression(stmt, stmt.getTableExpression(listTableAlias));
            if (elementExpr.getLogicSetExpression() != null && !elementTable.equals(elementExpr.getLogicSetExpression().getMainTable())) {
                if (existsQuery) {
                    stmt.andCondition(elmListExpr.eq(elementExpr), true);
                    return elmListExpr;
                } else {
                    return elmListExpr;
                }
            } else {
                if (existsQuery) {
                    ScalarExpression elementIdExpr = elementTableID.newScalarExpression(stmt, elmTblExpr);
                    stmt.andCondition(elmListExpr.eq(elementIdExpr), true);
                    return elementIdExpr;
                } else {
                    return elmListExpr;
                }
            }
        }
    }
}

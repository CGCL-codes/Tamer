package org.datanucleus.store.rdbms.fieldmanager;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.Relation;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.fieldmanager.AbstractFieldManager;
import org.datanucleus.store.mapped.StatementClassMapping;
import org.datanucleus.store.mapped.StatementMappingIndex;
import org.datanucleus.store.mapped.mapping.EmbeddedPCMapping;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.mapped.mapping.SerialisedPCMapping;
import org.datanucleus.store.mapped.mapping.SerialisedReferenceMapping;
import org.datanucleus.store.query.ResultObjectFactory;
import org.datanucleus.store.rdbms.RDBMSStoreManager;

/**
 * ResultSet getter implementation of a field manager.
 */
public class ResultSetGetter extends AbstractFieldManager {

    private final RDBMSStoreManager storeMgr;

    private final ObjectProvider op;

    private final AbstractClassMetaData cmd;

    private final ExecutionContext ec;

    private final Object resultSet;

    private final StatementClassMapping resultMappings;

    /**
     * Constructor where we know the object to put the field values in.
     * @param storeMgr RDBMS StoreManager
     * @param op ObjectProvider where we are putting the results
     * @param results the ResultSet
     * @param resultMappings Mappings for the results for this class
     */
    public ResultSetGetter(RDBMSStoreManager storeMgr, ObjectProvider op, Object results, StatementClassMapping resultMappings) {
        this.storeMgr = storeMgr;
        this.op = op;
        this.cmd = op.getClassMetaData();
        this.ec = op.getExecutionContext();
        this.resultSet = results;
        this.resultMappings = resultMappings;
    }

    /**
     * Constructor without the StateManager, where we know the result set but don't have the object yet.
     * @param storeMgr RDBMS StoreManager
     * @param ec Execution Context
     * @param results the ResultSet
     * @param resultMappings Mappings for the results for this class
     * @param cmd Metadata for the class
     */
    public ResultSetGetter(RDBMSStoreManager storeMgr, ExecutionContext ec, Object results, StatementClassMapping resultMappings, AbstractClassMetaData cmd) {
        this.storeMgr = storeMgr;
        this.op = null;
        this.cmd = cmd;
        this.ec = ec;
        this.resultSet = results;
        this.resultMappings = resultMappings;
    }

    public boolean fetchBooleanField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getBoolean(ec, resultSet, mapIdx.getColumnPositions());
    }

    public char fetchCharField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getChar(ec, resultSet, mapIdx.getColumnPositions());
    }

    public byte fetchByteField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getByte(ec, resultSet, mapIdx.getColumnPositions());
    }

    public short fetchShortField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getShort(ec, resultSet, mapIdx.getColumnPositions());
    }

    public int fetchIntField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getInt(ec, resultSet, mapIdx.getColumnPositions());
    }

    public long fetchLongField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getLong(ec, resultSet, mapIdx.getColumnPositions());
    }

    public float fetchFloatField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getFloat(ec, resultSet, mapIdx.getColumnPositions());
    }

    public double fetchDoubleField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getDouble(ec, resultSet, mapIdx.getColumnPositions());
    }

    public String fetchStringField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        return mapIdx.getMapping().getString(ec, resultSet, mapIdx.getColumnPositions());
    }

    public Object fetchObjectField(int fieldNumber) {
        StatementMappingIndex mapIdx = resultMappings.getMappingForMemberPosition(fieldNumber);
        JavaTypeMapping mapping = mapIdx.getMapping();
        Object value;
        if (mapping instanceof EmbeddedPCMapping || mapping instanceof SerialisedPCMapping || mapping instanceof SerialisedReferenceMapping) {
            value = mapping.getObject(ec, resultSet, mapIdx.getColumnPositions(), op, fieldNumber);
        } else {
            AbstractMemberMetaData mmd = cmd.getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
            int relationType = mmd.getRelationType(ec.getClassLoaderResolver());
            if (relationType == Relation.ONE_TO_ONE_BI || relationType == Relation.ONE_TO_ONE_UNI || relationType == Relation.MANY_TO_ONE_BI) {
                StatementClassMapping relationMappings = resultMappings.getMappingDefinitionForMemberPosition(fieldNumber);
                if (relationMappings != null) {
                    ClassLoaderResolver clr = ec.getClassLoaderResolver();
                    AbstractClassMetaData relatedCmd = ec.getMetaDataManager().getMetaDataForClass(mmd.getType(), clr);
                    ResultObjectFactory relationROF = storeMgr.newResultObjectFactory(relatedCmd, relationMappings, false, ec.getFetchPlan(), mmd.getType());
                    value = relationROF.getObject(ec, resultSet);
                } else {
                    value = mapping.getObject(ec, resultSet, mapIdx.getColumnPositions());
                }
            } else {
                value = mapping.getObject(ec, resultSet, mapIdx.getColumnPositions());
            }
        }
        if (op != null) {
            return op.wrapSCOField(fieldNumber, value, false, false, false);
        } else {
            return value;
        }
    }
}

package br.ufmg.lcc.pcollecta.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.commons.NVLHelper;
import br.ufmg.lcc.arangi.commons.StringHelper;
import br.ufmg.lcc.arangi.model.IPersistenceObject;
import br.ufmg.lcc.pcollecta.dto.DataRepository;
import br.ufmg.lcc.pcollecta.dto.Etl;
import br.ufmg.lcc.pcollecta.dto.ExtendedField;
import br.ufmg.lcc.pcollecta.dto.Field;
import br.ufmg.lcc.pcollecta.dto.GlobalParameter;
import br.ufmg.lcc.pcollecta.dto.KeyMapping;
import br.ufmg.lcc.pcollecta.dto.ProcessingResult;
import br.ufmg.lcc.pcollecta.dto.RelationalDataBase;
import br.ufmg.lcc.pcollecta.model.data.DataRepositoryDAO;
import br.ufmg.lcc.pcollecta.model.data.RelationalDatabaseDAO;
import br.ufmg.lcc.pcollecta.model.data.Type;
import br.ufmg.lcc.pcollecta.ws.PCollectaServiceException;

/**
 * @author Andr� Bigonha Toledo
 * @author Cesar Correia
 *
 */
public class KeyMappingBO extends PCollectaBO {

    private final DataRepositoryBO dataRepositoryBO;

    private final GlobalParameterBO globalParameterBO;

    private final ProcessingResultBO processingResultBO;

    private static final String getTargetIDQuery = "select keyMapping.targetId from KeyMapping keyMapping " + " where keyMapping.etlId = :etlId and keyMapping.sourceId = :sourceId";

    private static final String getMappingByTargetIdQuery = "select keyMapping from KeyMapping keyMapping " + " where keyMapping.etlId = :etlId and keyMapping.targetId = :targetId";

    private static final String getMappingBySourceIdQuery = "select keyMapping from KeyMapping keyMapping " + " where keyMapping.etlId = :etlId and keyMapping.sourceId = :sourceId";

    public KeyMappingBO() throws BasicException {
        super();
        dataRepositoryBO = (DataRepositoryBO) this.getBusinessObject(DataRepositoryBO.class.getName());
        globalParameterBO = (GlobalParameterBO) getBusinessObject(GlobalParameterBO.class.getName());
        processingResultBO = (ProcessingResultBO) getBusinessObject(ProcessingResultBO.class.getName());
    }

    /**
	 * Given the origen key and the etl , return the mapping
	 * @param dao
	 * @param idETCChaveEstrangeira
	 * @param sourceKey
	 * @return
	 * @throws BasicException
	 */
    public KeyMapping retrieveMappingBySourceId(IPersistenceObject dao, Long etlId, String sourceKey) throws BasicException {
        KeyMapping keyMapping = new KeyMapping();
        keyMapping.setSourceId(sourceKey);
        keyMapping.setEtlId(etlId);
        List lista = this.executeQuery(dao, getMappingBySourceIdQuery, keyMapping);
        if (lista != null && lista.size() > 0) {
            return (KeyMapping) lista.get(0);
        }
        return null;
    }

    /**
	 * Given the origen key and the etl , return the mapping
	 * @param dao
	 * @param idETCChaveEstrangeira
	 * @param sourceKey
	 * @return
	 * @throws BasicException
	 */
    public KeyMapping retrieveMappingBySourceId(IPersistenceObject dao, KeyMapping keyMapping) throws BasicException {
        List list = this.executeQuery(dao, getMappingBySourceIdQuery, keyMapping);
        if (list != null && list.size() > 0) {
            return (KeyMapping) list.get(0);
        }
        return null;
    }

    /**
	 * Given the origen key and the etl , return the mapping
	 * @param dao
	 * @param idETCChaveEstrangeira
	 * @param sourceKey
	 * @return
	 * @throws BasicException
	 */
    public String retrieveTargetId(IPersistenceObject dao, Long etlId, String sourceKey) throws BasicException {
        KeyMapping keyMapping = new KeyMapping();
        keyMapping.setSourceId(sourceKey);
        keyMapping.setEtlId(etlId);
        List lista = this.executeQuery(dao, getTargetIDQuery, keyMapping);
        if (lista != null && lista.size() > 0) {
            return (String) lista.get(0);
        }
        return null;
    }

    /**
	 * Given the origen key and the etl , return the mapping
	 * @param dao
	 * @param idETCChaveEstrangeira
	 * @param targetId
	 * @return
	 * @throws BasicException
	 */
    public KeyMapping retrieveMappingByTargetId(IPersistenceObject dao, Etl etl, String targetId) throws BasicException {
        KeyMapping keyMapping = new KeyMapping();
        keyMapping.setTargetId(targetId);
        keyMapping.setEtlId(etl.getId());
        List lista = this.executeQuery(dao, getMappingByTargetIdQuery, keyMapping);
        if (lista != null && lista.size() > 0) {
            return (KeyMapping) lista.get(0);
        }
        return null;
    }

    /**
	 * Merge composed keys into  unique string key. Formed by the  concatenated keys, linked by "_" characters   
	 * @param sourceRecord
	 *            Mapping of the column names and their values(Row of the table)
	 * @param keyFields
	 *            sorted key names.(Merge Order). Used to select and order the
	 *            names to merge
	 * @return
	 */
    public String mergeComposedId(IPersistenceObject executionStatusDAO, ProcessingResult processingResult, Map<String, Object> sourceRecord, List<Field> keyFields, long rownumber) throws BasicException {
        String mergedId = "";
        if (keyFields.size() == 0) {
            throw BasicException.errorHandling("Use Mapping is true but none key field was defined", "msgErrorNotDefinedKeyFields", StringHelper.EMPTY_STRING_VECTOR, log);
        }
        boolean hasOneFieldNotNull = false;
        for (Field field : keyFields) {
            Object value = sourceRecord.get(field.getUpperCaseName());
            boolean isNull = false;
            if (value == null) {
                isNull = true;
            } else if (value instanceof String) {
                String str = (String) value;
                if (NVLHelper.isEmpty(str)) {
                    value = "";
                }
            }
            if (isNull) {
                processingResultBO.logMessageln(executionStatusDAO, processingResult, ProcessingResult.ERROR_MESSAGE.MERGING_SOURCE_KEY, new String[] { field.getName(), rownumber + "" }, false);
                throw new InsertionErrorException();
            }
            mergedId = mergedId + value + "_";
        }
        mergedId = mergedId.substring(0, mergedId.length() - 1);
        String aux = mergedId.replaceAll("_", "");
        if (mergedId.equals("") || aux.equals("")) {
            processingResultBO.logMessageln(executionStatusDAO, processingResult, ProcessingResult.ERROR_MESSAGE.MERGING_SOURCE_KEY_IS_EMPTY, new String[] { rownumber + "" }, false);
            throw new InsertionErrorException();
        }
        if (mergedId.length() > 512) {
            StringBuffer s = new StringBuffer();
            for (Field key : keyFields) {
                s.append(key.getName());
                s.append(" = ");
                s.append(sourceRecord.get(key.getUpperCaseName()));
                s.append(",");
            }
            processingResultBO.logMessageln(executionStatusDAO, processingResult, ProcessingResult.ERROR_MESSAGE.MERGING_KEY, new String[] { s.toString(), rownumber + "" }, false);
            throw new InsertionErrorException();
        }
        return mergedId;
    }

    /**
	 * UnMerge composed keys from unique string key to separated string keys mapped to the field name   
	 * @param key
	 *            merged key
	 * @param keys
	 *            sorted key fields.(Merge Order). Show the order of the fields in the merged key
	 * @return
	 */
    public Map<String, Object> unMergeComposedKey(String key, List<Field> keys) throws BasicException {
        Map<String, Object> result = new HashMap<String, Object>();
        String keysData[] = key.split("_");
        if (keysData.length != keys.size()) {
            StringBuilder s = new StringBuilder();
            s.append("Fields names {");
            for (Field f : keys) {
                s.append(f.getName());
                s.append(",");
            }
            if (s.length() > 0) s.deleteCharAt(s.length() - 1);
            s.append("}");
            s.append(" data {");
            for (String f : keysData) {
                s.append(f);
                s.append(",");
            }
            if (s.length() > 0) s.deleteCharAt(s.length() - 1);
            s.append("}");
            throw BasicException.errorHandling("Error restoring key from merged key. Number of field doesnt match the number of data. " + s, "msgErrorCreatingMergedKey", StringHelper.EMPTY_STRING_VECTOR, log);
        }
        Iterator<Field> it = keys.iterator();
        ;
        for (int i = 0; i < keysData.length; i++) {
            result.put(it.next().getName(), keysData[i]);
        }
        return result;
    }

    /**
	 * remove the mappings that are related to etls Etls where has the targetId as the target key
	 */
    public void removeKeyMappingByTableAndTargetId(IPersistenceObject dao, List<Etl> etls, String targetId) throws BasicException {
        List<Long> etlIds = new LinkedList<Long>();
        for (Etl etl : etls) {
            etlIds.add(etl.getId());
        }
        KeyMapping mapping = new KeyMapping();
        mapping.setTargetId(targetId);
        mapping.setEtlIds(etlIds);
        String hql = "select mapping from mapping in class br.ufmg.lcc.pcollecta.dto.KeyMapping" + " where mapping.targetId = :targetId and " + " mapping.etlId in (:etlIds) ";
        List lista = this.executeQuery(dao, hql, mapping);
        if (lista != null && lista.size() > 0) {
            for (int i = 0; i < lista.size(); i++) {
                KeyMapping map = (KeyMapping) lista.get(i);
                this.doDelete(dao, map);
            }
        }
    }

    public void insertUpdateMapping(IPersistenceObject dao, KeyMapping mapping) throws BasicException {
        KeyMapping keyMapping = this.retrieveMappingBySourceId(dao, mapping);
        if (keyMapping == null) {
            mapping.setLastDateAudit(new Date());
            mapping.setLastUserAudit("pcollecta");
            this.doInsert(dao, mapping);
            this.removeFromPersistenceCache(dao, mapping);
        } else {
            keyMapping.setTargetId(mapping.getTargetId());
            keyMapping.setLastDateAudit(new Date());
            keyMapping.setLastUserAudit("pcollecta");
            this.doUpdate(dao, keyMapping, null);
            this.removeFromPersistenceCache(dao, keyMapping);
        }
    }

    public void deleteListInvalidMapping(IPersistenceObject dao, List invalidMappingList) throws BasicException {
        if (invalidMappingList != null && invalidMappingList.size() > 0) {
            KeyMappingDAO keyMappingDAO = new KeyMappingDAO(dao.getConnectionInUse());
            for (int i = 0; i < invalidMappingList.size(); i++) {
                Long id = (Long) invalidMappingList.get(i);
                keyMappingDAO.removeKeyMappingById(id);
                if ((i % 1000) == 0) {
                    keyMappingDAO.commitDeleteMappings();
                }
            }
        }
        invalidMappingList.clear();
    }

    /**
	* 
	* checkInvalidMappingTargetRepositoryEtl
	* 
	* This method create a list of invalid KeyMapping. To find this invalid keys is necessary search every 
	* destination key, and compare it with the source Id. The Keys is declared invalid when destination Id 
	* don't exists in source Id.
	* 
	* Initialy start the curosor for the query and after that get the target 
	* 	
	* @param dao
	* @param etl
	* @return
	* @throws BasicException
	*/
    @SuppressWarnings("unchecked")
    public List checkInvalidMappingTargetRepositoryEtl(IPersistenceObject dao, Etl etl) throws BasicException {
        KeyMapping keyMapping = new KeyMapping();
        List<Long> invalidMappingList = new ArrayList<Long>();
        DataRepository targetRepository = etl.getTargetRepository();
        targetRepository = (DataRepository) dataRepositoryBO.doLoad(dao, targetRepository);
        etl.setTargetRepository(targetRepository);
        DataRepositoryDAO dataRepository = preparerRepository(dao, etl);
        keyMapping.setEtlId(etl.getId());
        RelationalDatabaseDAO relationalDatabase;
        if (targetRepository instanceof RelationalDataBase) {
            relationalDatabase = (RelationalDatabaseDAO) dataRepository;
            relationalDatabase.setTargetTable(etl.getTargetTable());
            relationalDatabase.open();
        } else {
            throw BasicException.errorHandling("This option is avaliable only for relational database type", "msgErrorNotRelationalDatabase", StringHelper.EMPTY_STRING_VECTOR, log);
        }
        String primaryKey = getPrimaryKey(etl);
        StringBuffer sql = new StringBuffer();
        sql.append("select " + primaryKey + " from ");
        sql.append(etl.getTargetTable());
        sql.append(" where ");
        sql.append(primaryKey);
        sql.append(" = ?");
        relationalDatabase.prepareReading(sql.toString());
        KeyMappingDAO keyMappingDAO = new KeyMappingDAO(dao.getConnectionInUse());
        keyMappingDAO.startReadingKeyMappingsForEtl(etl);
        int primaryKeyType = this.getPrimaryKeyType(etl);
        Object parameter = null;
        while (keyMappingDAO.hasMoreMappingForEtl()) {
            KeyMapping mapping = keyMappingDAO.nextMappingForEtl();
            if (primaryKeyType == Type.TYPE_STRING) {
                parameter = mapping.getTargetId();
            } else {
                parameter = new Long(mapping.getTargetId());
            }
            if (!relationalDatabase.hasRecordsForParameter(parameter)) {
                invalidMappingList.add(mapping.getId());
            }
        }
        relationalDatabase.close();
        keyMappingDAO.close();
        return invalidMappingList;
    }

    /**
    * Prepare the statment for the database
    * 	
    * @param dao
    * @param etl
    * @return
    * @throws BasicException
    */
    @SuppressWarnings("unchecked")
    private DataRepositoryDAO preparerRepository(IPersistenceObject dao, Etl etl) throws BasicException {
        List<GlobalParameter> parameters = globalParameterBO.doLoadAll(dao, null, GlobalParameter.class.getName(), null);
        Map<String, String> globalParameterMap = globalParameterBO.mountGlobalParameterMap(parameters);
        return dataRepositoryBO.createRepository(dao, etl, globalParameterMap, DataRepositoryDAO.TARGET, null, false);
    }

    /**
	* getPrimaryKey
	* 
	* Get the primary key from the destination Layout. If it don't have a primaryKey
	* return a empty String.
	* 
	* @param etl - The Etl that will be verified
	* 
	* @return String with the name of PrimaryKey
	* @throws PCollectaServiceException 
	*/
    private String getPrimaryKey(Etl etl) {
        String primaryKey = "";
        for (ExtendedField field : etl.getTargetLayout()) {
            if (field.getPrimaryKey()) {
                return field.getName();
            }
        }
        return primaryKey;
    }

    /**
	* getPrimaryKey
	* 
	* Get the primary key from the destination Layout. If it don't have a primaryKey
	* return a empty String.
	* 
	* @param etl - The Etl that will be verified
	* 
	* @return String with the name of PrimaryKey
	* @throws PCollectaServiceException 
	*/
    private int getPrimaryKeyType(Etl etl) {
        for (ExtendedField field : etl.getTargetLayout()) {
            if (field.getPrimaryKey()) {
                return Type.convertFieldTypeToType(field.getFieldType());
            }
        }
        return 0;
    }
}

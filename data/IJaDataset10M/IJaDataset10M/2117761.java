package controller;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.execution.RequestContext;
import services.DTOAccessService;
import services.DTOFlowService;
import services.DTOService;
import services.GeneralFlowService;
import services.GeneralFlowService.FlowState;
import blomo.access.SQLRightsContainer;
import blomo.converter.BLoMoConverterFactory;
import blomo.dto.BLoMoDTO;
import blomo.dto.BLoMoDTOIdentifier;
import blomo.dto.BLoMoDTOIdentifierImpl;
import blomo.dto.DTOSession;
import blomo.dto.DTOSessionFactory;
import blomo.util.BLoMoUtil;
import blomo.util.Pair;
import blomo.validator.BLoMoResult;
import blomo.validator.BLoMoResultType;
import flow.dtoFlow.DTOFlowConfiguration;
import flow.dtoFlow.FlowRow;
import flow.dtoFlow.SearchConfiguration;

/**
 * @author Malte Schulze
 */
@Controller(value = "dtoController")
public class DTOController {

    private DTOFlowService dtoFlowService;

    private DTOAccessService dtoAccessService;

    private DTOService dtoService;

    private GeneralFlowService generalFlowService;

    private String flowName = "dto";

    /**
	 * @param conf used to determine the user role
	 * @param request can be used to determine session property's for the access check
	 * @return checks if the flow can be accessed by the user at the current time
	 */
    public boolean isFlowActive(DTOFlowConfiguration conf, final RequestContext request) {
        Map<Object, Object> parameters = new Hashtable<Object, Object>();
        parameters.put("role", conf.getRole());
        FlowState fs = generalFlowService.isFlowActive(flowName, conf.getRole(), parameters);
        if (fs.compareTo(FlowState.flowIsAccessible) == 0) return true;
        return false;
    }

    /**
	 * Sets up the DTOFlowConfiguration Object, mainly carries the given parameter and determines
	 * the classes for value and key entrys of the given parent relation.
	 * @param dtoClass string representation of the DTO type that is being worked on
	 * @param role string representation of the BLoMoRole from the logged in user
	 * @param parentDto string representation of the DTO type that was active in the parent flow 
	 * @param parentRelation name of the relation that connects the parent DTO to the current DTO
	 * @return Group object for display and configuration content 
	 */
    public DTOFlowConfiguration prepareListview(String dtoClass, String role, BLoMoDTO parentDto, String parentRelation) {
        DTOFlowConfiguration conf = new DTOFlowConfiguration();
        conf.getSearchConfRows().setLimit(20);
        conf.getSearchConfRowKeys().setLimit(20);
        conf.setParentDto(parentDto);
        conf.setParentRelation(parentRelation);
        conf.setRole(role);
        if (dtoClass.indexOf(',') == -1) {
            conf.setDtoClass(dtoClass);
            if (BLoMoUtil.isDtoClass(dtoClass)) {
                try {
                    conf.setEntityClass(((BLoMoDTO) Class.forName(dtoClass).newInstance()).getEntityClass().getName());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                conf.setValueIsSimple(Boolean.FALSE);
            } else conf.setValueIsSimple(Boolean.TRUE);
        } else {
            String parts[] = dtoClass.split(",");
            conf.setDtoClass(parts[1]);
            conf.setDtoKeyClass(parts[0]);
            try {
                if (BLoMoUtil.isDtoClass(parts[1])) {
                    conf.setEntityClass(((BLoMoDTO) Class.forName(parts[1]).newInstance()).getEntityClass().getName());
                    conf.setValueIsSimple(Boolean.FALSE);
                } else {
                    conf.setEntityClass(parts[1]);
                    conf.setValueIsSimple(Boolean.TRUE);
                }
                if (BLoMoUtil.isDtoClass(parts[1])) {
                    conf.setEntityKeyClass(((BLoMoDTO) Class.forName(parts[0]).newInstance()).getEntityClass().getName());
                    conf.setKeyIsSimple(Boolean.FALSE);
                } else {
                    conf.setEntityKeyClass(parts[0]);
                    conf.setKeyIsSimple(Boolean.TRUE);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (conf.getDtoClass() != null) {
            if (BLoMoUtil.isDtoClass(conf.getDtoClass())) conf.setDtoRights(dtoAccessService.getRightsForDTOClassName(conf.getDtoClass(), role)); else conf.setDtoRights(new SQLRightsContainer(true, true, true, true));
        }
        if (conf.getDtoKeyClass() != null) {
            if (BLoMoUtil.isDtoClass(conf.getDtoKeyClass())) conf.setDtoKeyRights(dtoAccessService.getRightsForDTOClassName(conf.getDtoKeyClass(), role)); else conf.setDtoKeyRights(new SQLRightsContainer(true, true, true, true));
        }
        return conf;
    }

    /**
	 * If no session is given a new one will be created. 
	 * Initiates the search for entity's that will be
	 * displayed.
	 * @param oldSession the session from the parent flow or null
	 * @param conf the configuration of the current flow 
	 * @return the session id from a new or old session
	 */
    public Long prepareDtoSession(Long oldSession, DTOFlowConfiguration conf) {
        Long sessionId;
        if (oldSession != null) sessionId = oldSession; else sessionId = DTOSessionFactory.createSession(conf.getRole(), dtoAccessService);
        doSearch(conf, sessionId);
        return sessionId;
    }

    /**
	 * @param savableDtos all unsaved DTO's from the parent flow
	 * @return a list of all unsaved DTO's
	 */
    public List<BLoMoDTOIdentifier> prepareSavableDtos(List<BLoMoDTOIdentifier> savableDtos) {
        if (savableDtos != null) return savableDtos; else return new LinkedList<BLoMoDTOIdentifier>();
    }

    /**
	 * @param savableDtos the current list of unsaved DTO's
	 * @param dto the DTO to add to the list
	 */
    public void addSavableDtos(List<BLoMoDTOIdentifier> savableDtos, BLoMoDTO dto) {
        BLoMoDTOIdentifier identifier = dto.toIdentifier();
        if (!savableDtos.contains(identifier)) savableDtos.add(identifier);
    }

    /**
	 * Stores the given DTO in the DTOSession, if changes 
	 * were made the template in the session must be changed 
	 * since serialization breaks the pointer references.
	 * The synchronization will update all entity's that are
	 * accessed by the mappings
	 * @param dto The DTO that has changed
	 * @param mappings All attributes that have been accessed
	 * @param sessionId The id to find the DTOSession
	 */
    public void acceptChanges(BLoMoDTO dto, List<String> mappings, Long sessionId) {
        DTOSession session = DTOSessionFactory.getSession(sessionId);
        session.putInCache(dto, dto.toIdentifier());
        for (String mapping : mappings) {
            dto.synchronize(mapping, session);
        }
    }

    /**
	 * @param dto The DTO to validate
	 * @param conf The flow configuration object
	 * @param sessionId The id to find the DTOSession
	 * @param ignoreWarnings Should warnings during validation be ignored
	 * @return Is the processing of the DTO allowed
	 */
    public Boolean validateDto(BLoMoDTO dto, DTOFlowConfiguration conf, Long sessionId, String ignoreWarnings) {
        DTOSession session = DTOSessionFactory.getSession(sessionId);
        BLoMoDTO dtoOriginal = session.load(dto);
        Object[] objects = new Object[conf.getHeadlines().size()];
        int i = 0;
        for (Pair<String, String> mapping : conf.getHeadlines()) {
            objects[i++] = dtoOriginal.getAttribute(mapping.getKey());
        }
        Pair<List<String>, List<String>> p = dtoService.getAttributes(dto.getClass().getName(), conf.getRole(), flowName);
        acceptChanges(dto, p.getKey(), sessionId);
        List<Pair<BLoMoResultType, String>> messages = new LinkedList<Pair<BLoMoResultType, String>>();
        BLoMoResult result = session.validate(dto, messages, p.getKey());
        conf.setMessages(messages);
        if (result.getRollbackAll() || result.getRollbackThis()) {
            i = 0;
            for (Pair<String, String> mapping : conf.getHeadlines()) {
                dto.addToAttribute(mapping.getKey(), objects[i++], null);
            }
        }
        if ("on".equals(ignoreWarnings) && result.getType() == BLoMoResultType.warning) {
            result.setContinueProcessingAll(Boolean.TRUE);
            result.setContinueProcessingThis(Boolean.TRUE);
        }
        return result.getContinueProcessingAll().booleanValue() && result.getContinueProcessingThis().booleanValue();
    }

    /**
	 * Fills the displayed tables with entry's from the Database 
	 * and DTOSession.
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void doSearch(DTOFlowConfiguration conf, Long sessionId) {
        DTOSession session = DTOSessionFactory.getSession(sessionId);
        dtoFlowService.transformRelationData(conf, session);
        dtoFlowService.transformSessionData(conf, session);
        dtoFlowService.searchAllEntitys(conf, sessionId);
    }

    /**
	 * Applies a sort filter to database searches.
	 * @param columnId The index of the column to be sorted
	 * @param table The name of the table to be sorted
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void sortUp(String columnId, String table, DTOFlowConfiguration conf, Long sessionId) {
        int cid = Integer.parseInt(columnId);
        SearchConfiguration searchConf = getSearchConfigurationForTable(conf, table);
        if (searchConf == null) return;
        searchConf.setAsc(true);
        searchConf.setSortColumn(cid);
        doSearch(conf, sessionId);
    }

    /**
	 * Applies a sort filter to database searches.
	 * @param columnId The index of the column to be sorted
	 * @param table The name of the table to be sorted
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void sortDown(String columnId, String table, DTOFlowConfiguration conf, Long sessionId) {
        int cid = Integer.parseInt(columnId);
        SearchConfiguration searchConf = getSearchConfigurationForTable(conf, table);
        if (searchConf == null) return;
        searchConf.setAsc(false);
        searchConf.setSortColumn(cid);
        doSearch(conf, sessionId);
    }

    /**
	 * Increases the number of displayed results by doubling 
	 * the number of currently displayed results.
	 * @param table The name of the table with the results
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void showMore(String table, DTOFlowConfiguration conf, Long sessionId) {
        SearchConfiguration searchConf = getSearchConfigurationForTable(conf, table);
        if (searchConf == null) return;
        int limit = searchConf.getLimit();
        if (limit <= 0) searchConf.setLimit(20); else searchConf.setLimit(limit * 2);
        doSearch(conf, sessionId);
    }

    /**
	 * Decreases the number of displayed results by doubling 
	 * the number of currently displayed results.
	 * @param table The name of the table with the results
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void showLess(String table, DTOFlowConfiguration conf, Long sessionId) {
        SearchConfiguration searchConf = getSearchConfigurationForTable(conf, table);
        if (searchConf == null) return;
        int limit = searchConf.getLimit() / 2;
        if (limit <= 20) searchConf.setLimit(20); else searchConf.setLimit(limit);
        doSearch(conf, sessionId);
    }

    private SearchConfiguration getSearchConfigurationForTable(DTOFlowConfiguration conf, String table) {
        if ("existingRelations".equals(table)) return conf.getSearchConfRelationRows(); else if ("existingRelationKeys".equals(table)) return conf.getSearchConfRelationRowKeys(); else if ("allentitys".equals(table)) return conf.getSearchConfRows(); else if ("allentityKeys".equals(table)) return conf.getSearchConfRowKeys(); else return null;
    }

    /**
	 * Sets the first result for database searches.
	 * @param soffset Index of the result as String
	 * @param table The name of the table with the results
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void setOffset(String soffset, String table, DTOFlowConfiguration conf, Long sessionId) {
        int offset = Integer.parseInt(soffset);
        SearchConfiguration searchConf = getSearchConfigurationForTable(conf, table);
        if (searchConf == null) return;
        searchConf.setOffset(offset);
        doSearch(conf, sessionId);
    }

    private Object getObjectForMap(String s, boolean isKey, DTOFlowConfiguration conf, Long sessionId) {
        Object o;
        List<FlowRow> rows;
        boolean addressesSession;
        Object id = null;
        if (s.startsWith("s")) {
            addressesSession = true;
            s = s.substring(1);
        } else addressesSession = false;
        int pos = Integer.parseInt(s);
        if (isKey) {
            if (addressesSession) rows = conf.getSessionRowKeys(); else rows = conf.getRowKeys();
        } else {
            if (addressesSession) rows = conf.getSessionRows(); else rows = conf.getRows();
        }
        for (FlowRow row : rows) if (row.getIndex() == pos) {
            id = row.getId();
        }
        if (id == null) return null;
        DTOSession session = DTOSessionFactory.getSession(sessionId);
        if (addressesSession) {
            if (isKey) o = loadFromTable(id.toString(), "sessionRowKeys", conf, session); else o = loadFromTable(id.toString(), "sessionRows", conf, session);
        } else {
            if (isKey) o = loadFromTable(id.toString(), "allentityKeys", conf, session); else o = loadFromTable(id.toString(), "allentitys", conf, session);
        }
        return o;
    }

    /**
	 * The user submitted a value or a position for the 
	 * new value or key that shall be added to 
	 * the previously traveled map relation of the parent DTO.
	 * s[number] references a session table
	 * [number] references a database table
	 * xxx is a value if the key or value type of the map is 
	 * simple 
	 * @param sposKey Position in the key table or a value
	 * @param sposValue Position in the value table or a value
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void addToMap(String sposKey, String sposValue, DTOFlowConfiguration conf, Long sessionId) {
        Object oKey, oValue;
        if (!conf.getKeyIsSimple()) {
            oKey = getObjectForMap(sposKey, true, conf, sessionId);
        } else {
            oKey = BLoMoConverterFactory.instance().convert(conf.getDtoKeyClass(), sposKey);
        }
        if (!conf.getValueIsSimple()) {
            oValue = getObjectForMap(sposValue, false, conf, sessionId);
        } else {
            oValue = BLoMoConverterFactory.instance().convert(conf.getDtoClass(), sposValue);
        }
        BLoMoDTO parentDto = DTOSessionFactory.getSession(sessionId).load(conf.getParentDto());
        if (oKey != null && oValue != null) {
            parentDto.addToAttribute(conf.getParentRelation(), oValue, oKey);
        }
        acceptChanges(parentDto, dtoService.getAttributes(parentDto.getClass().getName(), conf.getRole(), flowName).getKey(), sessionId);
        doSearch(conf, sessionId);
    }

    private Object getIdFromTable(String sid, String table, DTOFlowConfiguration conf) {
        Object oId = null;
        List<FlowRow> rows;
        if ("allentitys".equals(table)) rows = conf.getRows(); else if ("sessionRows".equals(table)) rows = conf.getSessionRows(); else if ("existingRelations".equals(table)) rows = conf.getRelationRows(); else if ("sessionRowKeys".equals(table)) rows = conf.getSessionRowKeys(); else if ("existingRelationKeys".equals(table)) rows = conf.getRelationRowKeys(); else if ("allentityKeys".equals(table)) rows = conf.getRowKeys(); else return null;
        for (FlowRow row : rows) if (row.getId().toString().equals(sid)) oId = row.getId();
        return oId;
    }

    /**
	 * The user submitted a value or a position for the 
	 * new value that shall be added to 
	 * the previously traveled collection relation of 
	 * the parent DTO.
	 * @param sid id of the table entry
	 * @param table Name of the table with the value
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void add(String sid, String table, DTOFlowConfiguration conf, Long sessionId) {
        if (conf.getDtoKeyClass() != null) return;
        if (conf.getParentDto() == null) return;
        Object oId = getIdFromTable(sid, table, conf);
        if (oId != null) {
            DTOSession session = DTOSessionFactory.getSession(sessionId);
            Object oValue = loadFromTable(oId.toString(), table, conf, session);
            BLoMoDTO parentDto = DTOSessionFactory.getSession(sessionId).load(conf.getParentDto());
            parentDto.addToAttribute(conf.getParentRelation(), oValue, null);
            acceptChanges(parentDto, dtoService.getAttributes(parentDto.getClass().getName(), conf.getRole(), flowName).getKey(), sessionId);
            doSearch(conf, sessionId);
        }
    }

    /**
	 * A string that holds the new value will be added to 
	 * the last traveled relation. If a converter is present
	 * it will transform the string to the correct object.
	 * @param value New value
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void simpleAdd(String value, DTOFlowConfiguration conf, Long sessionId) {
        if (conf.getDtoKeyClass() != null) return;
        if (!conf.getValueIsSimple()) return;
        if (conf.getParentDto() == null) return;
        BLoMoDTO parentDto = DTOSessionFactory.getSession(sessionId).load(conf.getParentDto());
        parentDto.addToAttribute(conf.getParentRelation(), BLoMoConverterFactory.instance().convert(conf.getDtoKeyClass(), value), null);
        acceptChanges(parentDto, dtoService.getAttributes(parentDto.getClass().getName(), conf.getRole(), flowName).getKey(), sessionId);
        doSearch(conf, sessionId);
    }

    /**
	 * The user references an entry in the search tables that 
	 * he wants to remove from the active relation. The 
	 * entity itself exist but the association with the last 
	 * traveled DTO is removed.
	 * @param sid id of the table entry
	 * @param table Name of the table with the value
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    public void remove(String sid, String table, DTOFlowConfiguration conf, Long sessionId) {
        if (conf.getParentDto() == null) return;
        Object oId = getIdFromTable(sid, table, conf);
        if (oId != null) {
            BLoMoDTO parentDto = DTOSessionFactory.getSession(sessionId).load(conf.getParentDto());
            if ("existingRelationKeys".equals(table)) parentDto.removeFromAttribute(conf.getParentRelation(), null, (Serializable) oId); else if ("existingRelations".equals(table)) parentDto.removeFromAttribute(conf.getParentRelation(), (Serializable) oId);
            Object o = parentDto.getAttribute(conf.getParentRelation());
            if (o instanceof BLoMoDTO) {
                acceptChanges((BLoMoDTO) o, dtoService.getAttributes(o.getClass().getName(), conf.getRole(), flowName).getKey(), sessionId);
            }
            acceptChanges(parentDto, dtoService.getAttributes(parentDto.getClass().getName(), conf.getRole(), flowName).getKey(), sessionId);
            doSearch(conf, sessionId);
        }
    }

    /**
	 * Deletes a database entry.
	 * @param sid id of the table entry
	 * @param table Name of the table with the value
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 */
    @SuppressWarnings("unchecked")
    public void delete(String sid, String table, DTOFlowConfiguration conf, Long sessionId) {
        Object oId = getIdFromTable(sid, table, conf);
        String dtoClass = null;
        if ("allentitys".equals(table)) {
            dtoClass = conf.getDtoClass();
        } else if ("allentityKeys".equals(table)) {
            dtoClass = conf.getDtoKeyClass();
        }
        if (oId != null) {
            DTOSession dtoSession = DTOSessionFactory.getSession(sessionId);
            try {
                Class<? extends BLoMoDTO> c = (Class<? extends BLoMoDTO>) Class.forName(dtoClass);
                dtoService.doDelete(new BLoMoDTOIdentifierImpl(c, (Serializable) oId, null), dtoSession);
                doSearch(conf, sessionId);
            } catch (ClassCastException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareAttributeMapping(BLoMoDTO dto, DTOFlowConfiguration conf) {
        Pair<List<String>, List<String>> p = dtoService.getAttributes(dto.getClass().getName(), conf.getRole(), flowName);
        dtoService.initializeAttributes(dto, p.getKey());
        conf.setHeadlines(p);
        conf.setRelations(dtoService.getRelations(dto, conf.getRole()));
    }

    private Object loadFromTable(String sid, String table, DTOFlowConfiguration conf, DTOSession session) {
        Object oId = getIdFromTable(sid, table, conf);
        if (oId == null) return null;
        if ("sessionRows".equals(table)) {
            return session.load((BLoMoDTOIdentifier) oId);
        } else if ("sessionRowKeys".equals(table)) {
            return session.load((BLoMoDTOIdentifier) oId);
        }
        SearchConfiguration searchConf = getSearchConfigurationForTable(conf, table);
        if (oId != null) {
            return dtoService.doLoad((Serializable) oId, searchConf.getDtoClass(), session);
        }
        return null;
    }

    /**
	 * Loads the DTO for an entity and prepares 
	 * the flow configuration for editing.
	 * @param sid id of the table entry
	 * @param table Name of the table with the value
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 * @return The loaded DTO
	 */
    public BLoMoDTO editEntity(String sid, String table, DTOFlowConfiguration conf, Long sessionId) {
        DTOSession session = DTOSessionFactory.getSession(sessionId);
        BLoMoDTO dto = (BLoMoDTO) loadFromTable(sid, table, conf, session);
        prepareAttributeMapping(dto, conf);
        return dto;
    }

    /**
	 * Returns a new DTO.
	 * @param className Class of the new DTO
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 * @return The new DTO
	 */
    public Object newEntity(String className, DTOFlowConfiguration conf, Long sessionId) {
        DTOSession session = DTOSessionFactory.getSession(sessionId);
        BLoMoDTO dto = dtoService.doLoad(null, className, session);
        prepareAttributeMapping(dto, conf);
        return dto;
    }

    /**
	 * Returns a new DTO.
	 * @param conf The flow configuration object 
	 * @param sessionId The id to find the DTOSession
	 * @param id The id of the savable DTO.
	 * @param savableDtos All savable DTO's
	 */
    public void saveDto(DTOFlowConfiguration conf, Long sessionId, Integer id, List<BLoMoDTOIdentifier> savableDtos) {
        DTOSession session = DTOSessionFactory.getSession(sessionId);
        BLoMoDTO dto = session.load(savableDtos.get(id));
        Boolean valid = validateDto(dto, conf, sessionId, "on");
        if (valid.booleanValue()) {
            boolean saved = dtoService.doSave(session.load(savableDtos.get(id)), session);
            if (saved == false) {
                for (BLoMoDTOIdentifier identifier : savableDtos) {
                    session.load(identifier).setDirty(true);
                }
                conf.getMessages().add(new Pair<BLoMoResultType, String>(BLoMoResultType.error, "Saving failed."));
                return;
            }
            Iterator<BLoMoDTOIdentifier> it = savableDtos.iterator();
            while (it.hasNext()) {
                BLoMoDTOIdentifier identifier = it.next();
                if (!session.load(identifier).isDirty()) it.remove();
            }
        }
    }

    /**
	 * Removes all validation messages.
	 * @param conf The flow configuration object 
	 */
    public void clearMessages(DTOFlowConfiguration conf) {
        conf.getMessages().clear();
    }

    /**
	 * @param dtoFlowService instance of service
	 */
    public void setDtoFlowService(DTOFlowService dtoFlowService) {
        this.dtoFlowService = dtoFlowService;
    }

    /**
	 * @param dtoAccessService instance of service
	 */
    public void setDtoAccessService(DTOAccessService dtoAccessService) {
        this.dtoAccessService = dtoAccessService;
    }

    /**
	 * @param dtoService instance of service
	 */
    public void setDtoService(DTOService dtoService) {
        this.dtoService = dtoService;
    }

    /**
	 * @param generalFlowService the generalFlowService to set
	 */
    public void setGeneralFlowService(GeneralFlowService generalFlowService) {
        this.generalFlowService = generalFlowService;
    }
}

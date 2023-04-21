package org.isi.monet.core.producers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.LinkedHashMap;
import org.isi.monet.core.agents.AgentDatabase;
import org.isi.monet.core.constants.Database;
import org.isi.monet.core.constants.ErrorCode;
import org.isi.monet.core.constants.Producers;
import org.isi.monet.core.constants.Strings;
import org.isi.monet.core.exceptions.DataException;
import org.isi.monet.core.model.BusinessUnit;
import org.isi.monet.core.model.Context;
import org.isi.monet.core.model.DataLink;
import org.isi.monet.core.model.DataRequest;
import org.isi.monet.core.model.DataResultItem;
import org.isi.monet.core.model.DataResultItemList;
import org.isi.monet.core.model.DataStore;
import org.isi.monet.core.model.Dictionary;

public class ProducerDataLink extends ProducerDataStore {

    public ProducerDataLink() {
        super();
    }

    private Boolean createViews(DataLink oDataLink, DataRequest oDataRequest) {
        String[] aKeywords = this.getKeywords(oDataRequest.getCondition());
        AgentDatabase oAgentDatabase = AgentDatabase.getInstance();
        String idOwner = Context.getInstance().getCurrentSession().getAccount().getUser().getId();
        Dictionary oDictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
        String sSelectionTable = oDictionary.getNodeSelectionTableName(oDataLink.getCodeNode());
        String sQuery = Database.Queries.DATA_LINK_CREATE_VIEW;
        ProducerNodeReference oProducerNodeReference = (ProducerNodeReference) this.oProducersFactory.get(Producers.NODEREFERENCE);
        if ((sSelectionTable == null) || (!oProducerNodeReference.existsIndexTable(sSelectionTable))) sQuery = Database.Queries.DATA_LINK_CREATE_VIEW_FROM_SYSTEM;
        for (int iPos = 0; iPos < aKeywords.length; iPos++) {
            String sKeyword = aKeywords[iPos];
            LinkedHashMap<String, Object> hmParameters = new LinkedHashMap<String, Object>();
            hmParameters.put(Database.QueryFields.REFERENCE_TABLE, sSelectionTable);
            hmParameters.put(Database.QueryFields.DATA_STORE, oDataLink.getCode());
            hmParameters.put(Database.QueryFields.CODE, oDataLink.getCodeNode());
            hmParameters.put(Database.QueryFields.WORD, sKeyword);
            hmParameters.put(Database.QueryFields.ID_USER, idOwner);
            hmParameters.put(Database.QueryFields.ID_OWNER, idOwner);
            hmParameters.put(Database.QueryFields.ID_ROOT, oAgentDatabase.getRootId());
            hmParameters.put(Database.QueryFields.TABLE_NAME, this.getTableName(sKeyword, idOwner));
            oAgentDatabase.executeUpdateQuery(sQuery, hmParameters);
        }
        return true;
    }

    private Boolean removeViews(DataLink oDataLink, DataRequest oDataRequest) {
        String[] aKeywords = this.getKeywords(oDataRequest.getCondition());
        AgentDatabase oAgentDatabase = AgentDatabase.getInstance();
        String idUser = Context.getInstance().getCurrentSession().getAccount().getUser().getId();
        for (int iPos = 0; iPos < aKeywords.length; iPos++) {
            String sKeyword = aKeywords[iPos];
            LinkedHashMap<String, Object> hmParameters = new LinkedHashMap<String, Object>();
            hmParameters.put(Database.QueryFields.DATA_STORE, oDataLink.getCode());
            hmParameters.put(Database.QueryFields.ID_USER, idUser);
            hmParameters.put(Database.QueryFields.WORD, sKeyword);
            hmParameters.put(Database.QueryFields.TABLE_NAME, this.getTableName(sKeyword, idUser));
            oAgentDatabase.executeUpdateQuery(Database.Queries.DATA_LINK_REMOVE_VIEW, hmParameters);
        }
        return true;
    }

    private DataResultItemList readItems(DataLink oDataLink, DataRequest oDataRequest) {
        DataResultItemList oList = new DataResultItemList();
        AgentDatabase oAgentDatabase = AgentDatabase.getInstance();
        String[] aKeywords = this.getKeywords(oDataRequest.getCondition());
        String sFirstWord, sQueryWords;
        ResultSet oResult = null;
        String idUser = Context.getInstance().getCurrentSession().getAccount().getUser().getId();
        this.createViews(oDataLink, oDataRequest);
        sFirstWord = Strings.EMPTY;
        sQueryWords = Strings.EMPTY;
        for (int iPos = 0; iPos < aKeywords.length; iPos++) {
            LinkedHashMap<String, Object> hmParameters = new LinkedHashMap<String, Object>();
            hmParameters.put(Database.QueryFields.DATA_STORE, oDataLink.getCode());
            hmParameters.put(Database.QueryFields.ID_USER, idUser);
            hmParameters.put(Database.QueryFields.WORD, aKeywords[iPos]);
            hmParameters.put(Database.QueryFields.TABLE_NAME, this.getTableName(aKeywords[iPos], idUser));
            if (iPos == 0) sFirstWord = aKeywords[iPos]; else {
                hmParameters.put(Database.QueryFields.RELATED_WORD, sFirstWord);
                hmParameters.put(Database.QueryFields.RELATED_TABLE_NAME, this.getTableName(sFirstWord, idUser));
                sQueryWords += Strings.SPACE + oAgentDatabase.getQuery(Database.Queries.DATA_LINK_LOAD_PART_1, hmParameters);
            }
        }
        try {
            LinkedHashMap<String, Object> hmParameters = new LinkedHashMap<String, Object>();
            hmParameters.put(Database.QueryFields.DATA_STORE, oDataLink.getCode());
            hmParameters.put(Database.QueryFields.CODE, oDataLink.getCodeNode());
            hmParameters.put(Database.QueryFields.ID_USER, idUser);
            hmParameters.put(Database.QueryFields.WORD, sFirstWord);
            hmParameters.put(Database.QueryFields.PART_1, sQueryWords);
            hmParameters.put(Database.QueryFields.START_POS, oDataRequest.getStartPos());
            hmParameters.put(Database.QueryFields.LIMIT, oDataRequest.getLimit());
            hmParameters.put(Database.QueryFields.TABLE_NAME, this.getTableName(sFirstWord, idUser));
            oResult = oAgentDatabase.executeSelectQuery(Database.Queries.DATA_LINK_LOAD, hmParameters);
            oResult.beforeFirst();
            while (oResult.next()) {
                DataResultItem oItem = new DataResultItem(oResult.getString("id_node"), oResult.getString("label"), oResult.getString("description"));
                oList.add(oItem);
            }
            oResult = this.oAgentDatabase.executeSelectQuery(Database.Queries.DATA_LINK_LOAD_COUNT, hmParameters);
            oList.setTotalCount(oResult.getInt("counter") + 1);
        } catch (SQLException oException) {
            throw new DataException(ErrorCode.DATAINDEX_LOAD, oDataRequest.getCondition(), oException);
        } finally {
            if (oResult != null) this.oAgentDatabase.closeQuery(oResult);
            this.removeViews(oDataLink, oDataRequest);
        }
        return oList;
    }

    public DataLink load(String code) {
        DataLink oDataLink = new DataLink();
        oDataLink.setCode(code);
        return oDataLink;
    }

    public DataResultItemList loadItems(String codeDataLink, String codeIndicator, DataRequest oDataRequest) {
        return new DataResultItemList();
    }

    public DataResultItemList searchItems(String codeDataLink, String codeIndicator, DataRequest oDataRequest) {
        DataLink oDataLink = this.load(codeDataLink);
        return this.readItems(oDataLink, oDataRequest);
    }

    public DataResultItemList searchItems(DataLink oDataLink, String codeIndicator, DataRequest oDataRequest) {
        return this.readItems(oDataLink, oDataRequest);
    }

    public DataResultItem locateItem(DataLink oDataLink, String sCondition) {
        DataRequest oDataRequest;
        DataResultItemList oList;
        oDataRequest = new DataRequest();
        oDataRequest.setCondition(sCondition);
        oList = this.readItems(oDataLink, oDataRequest);
        return oList.first();
    }

    public Boolean addItem(String codeDataStore, String codeIndicator, String sValue) {
        return true;
    }

    public Boolean save(DataStore oDataStore) {
        return true;
    }

    public Object newObject() {
        return new DataLink();
    }

    public Boolean loadAttribute(EventObject oEventObject, String sAttribute) {
        return true;
    }
}

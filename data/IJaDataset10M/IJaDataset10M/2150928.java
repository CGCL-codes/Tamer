package org.monet.kernel.producers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import org.monet.kernel.agents.AgentDatabase;
import org.monet.kernel.constants.Database;
import org.monet.kernel.constants.ErrorCode;
import org.monet.kernel.constants.Producers;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.library.LibraryDate;
import org.monet.kernel.library.LibraryString;
import org.monet.kernel.model.Context;
import org.monet.kernel.model.DataLink;
import org.monet.kernel.model.DataRequest;
import org.monet.kernel.model.Language;
import org.monet.kernel.model.NodeItem;
import org.monet.kernel.model.NodeItemList;
import org.monet.kernel.model.Thesaurus;
import org.monet.kernel.model.definition.CatalogDefinition;
import org.monet.kernel.model.definition.FilterDeclaration;
import org.monet.kernel.model.definition.internal.DescriptorDefinition;
import org.monet.kernel.sql.QueryBuilder;

public class ProducerDataLink extends Producer {

    public ProducerDataLink() {
        super();
    }

    private void createViews(DataLink dataLink, DataRequest dataRequest) {
        String condition = dataRequest.getCondition();
        String[] keyWords = LibraryString.getKeywordsWithEmpty(condition);
        AgentDatabase agentDatabase = AgentDatabase.getInstance();
        String idOwner = Context.getInstance().getCurrentSession().getAccount().getUser().getId();
        String queryName;
        StringBuilder queryParametersBuilder = new StringBuilder();
        ProducerReference producerReference = (ProducerReference) this.oProducersFactory.get(Producers.REFERENCE);
        String codeReference = DescriptorDefinition.CODE;
        if (!dataLink.getDomainCodeNode().equals(Strings.EMPTY)) {
            CatalogDefinition domainDefinition = this.getDictionary().getCatalogDefinition(dataLink.getDomainCodeNode());
            if (domainDefinition != null) {
                codeReference = domainDefinition.getUse().getReference();
                List<FilterDeclaration> filters = domainDefinition.getDefaultView().getFilterDeclarationList();
                for (FilterDeclaration filter : filters) {
                    String result = filter.getValue(dataRequest.getParameters());
                    String baseQuery = agentDatabase.getRepositoryQuery(Database.Queries.DATA_LINK_CREATE_VIEW_PARAMETER);
                    QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
                    queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, producerReference.getReferenceTableName(codeReference));
                    queryBuilder.insertSubQuery(Database.QueryFields.NAME, filter.getAttribute());
                    queryBuilder.insertSubQuery(Database.QueryFields.OPERATOR, filter.getOperatorAsString());
                    queryBuilder.insertSubQuery(Database.QueryFields.DATA, "'" + result.replace("'", "''") + "'");
                    queryParametersBuilder.append(Strings.SPACE);
                    queryParametersBuilder.append(queryBuilder.build());
                }
            }
        }
        if (condition.equals("")) {
            if (codeReference.equals(DescriptorDefinition.CODE)) queryName = Database.Queries.DATA_LINK_CREATE_VIEW_ALL_FROM_SYSTEM; else queryName = Database.Queries.DATA_LINK_CREATE_VIEW_ALL;
        } else {
            if (codeReference.equals(DescriptorDefinition.CODE)) queryName = Database.Queries.DATA_LINK_CREATE_VIEW_FROM_SYSTEM; else queryName = Database.Queries.DATA_LINK_CREATE_VIEW;
        }
        for (int iPos = 0; iPos < keyWords.length; iPos++) {
            String keyword = keyWords[iPos];
            HashMap<String, String> subQueries = new HashMap<String, String>();
            subQueries.put(Database.QueryFields.TABLE_NAME, this.getTableName(keyword));
            if (queryName.equals(Database.Queries.DATA_LINK_CREATE_VIEW_ALL) || queryName.equals(Database.Queries.DATA_LINK_CREATE_VIEW)) {
                subQueries.put(Database.QueryFields.REFERENCE_TABLE, producerReference.getReferenceTableName(codeReference));
                subQueries.put(Database.QueryFields.PARAMETERS, queryParametersBuilder.toString());
            }
            subQueries.put(Database.QueryFields.CODE, dataLink.getCodeNode());
            subQueries.put(Database.QueryFields.ID_OWNER, idOwner);
            subQueries.put(Database.QueryFields.ID_ROOT, agentDatabase.getRootId());
            if (!condition.isEmpty()) subQueries.put(Database.QueryFields.WORD, keyword);
            agentDatabase.executeRepositoryUpdateQuery(queryName, null, subQueries);
        }
    }

    private void removeViews(DataLink dataLink, DataRequest dataRequest) {
        String[] keywords = LibraryString.getKeywordsWithEmpty(dataRequest.getCondition());
        AgentDatabase agentDatabase = AgentDatabase.getInstance();
        for (int iPos = 0; iPos < keywords.length; iPos++) {
            String keyword = keywords[iPos];
            HashMap<String, String> subQueries = new HashMap<String, String>();
            subQueries.put(Database.QueryFields.TABLE_NAME, this.getTableName(keyword));
            agentDatabase.executeRepositoryUpdateQuery(Database.Queries.DATA_LINK_REMOVE_VIEW, null, subQueries);
        }
    }

    private NodeItemList readItems(DataLink dataLink, DataRequest dataRequest) {
        NodeItemList list = new NodeItemList();
        AgentDatabase agentDatabase = AgentDatabase.getInstance();
        String[] keyWords = LibraryString.getKeywordsWithEmpty(dataRequest.getCondition());
        String firstWord;
        StringBuilder queryWords;
        ResultSet result = null;
        this.createViews(dataLink, dataRequest);
        firstWord = Strings.EMPTY;
        queryWords = new StringBuilder();
        for (int pos = 0; pos < keyWords.length; pos++) {
            if (pos == 0) firstWord = keyWords[pos]; else {
                String baseQuery = agentDatabase.getRepositoryQuery(Database.Queries.DATA_LINK_LOAD_PART_1);
                QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
                queryBuilder.insertSubQuery(Database.QueryFields.TABLE_NAME, this.getTableName(keyWords[pos]));
                queryBuilder.insertSubQuery(Database.QueryFields.RELATED_TABLE_NAME, this.getTableName(firstWord));
                queryWords.append(Strings.SPACE);
                queryWords.append(queryBuilder.build());
            }
        }
        try {
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
            parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());
            HashMap<String, String> subQueries = new HashMap<String, String>();
            subQueries.put(Database.QueryFields.TABLE_NAME, this.getTableName(firstWord));
            subQueries.put(Database.QueryFields.PART_1, queryWords.toString());
            result = agentDatabase.executeRepositorySelectQuery(Database.Queries.DATA_LINK_LOAD, parameters, subQueries);
            while (result.next()) {
                NodeItem item = new NodeItem();
                int columnCount;
                ResultSetMetaData resultMetaData;
                resultMetaData = result.getMetaData();
                columnCount = resultMetaData.getColumnCount();
                for (int pos = 1; pos < columnCount + 1; pos++) {
                    String columnName = resultMetaData.getColumnName(pos).toLowerCase();
                    int type = resultMetaData.getColumnType(pos);
                    if ((type == Types.DATE) || (type == Types.TIME) || (type == Types.TIMESTAMP)) item.addAttribute(columnName, LibraryDate.getDateAndTimeString(result.getTimestamp(columnName), Language.getCurrent(), LibraryDate.Format.NUMERIC, true, Strings.MINUS)); else if (type == Types.INTEGER) item.addAttribute(columnName, String.valueOf(result.getInt(pos))); else if (type == Types.BOOLEAN) item.addAttribute(columnName, String.valueOf(result.getBoolean(pos))); else item.addAttribute(columnName, result.getString(pos));
                }
                list.add(item);
            }
            this.agentDatabase.closeQuery(result);
            subQueries = new HashMap<String, String>();
            subQueries.put(Database.QueryFields.TABLE_NAME, this.getTableName(firstWord));
            subQueries.put(Database.QueryFields.PART_1, queryWords.toString());
            result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.DATA_LINK_LOAD_COUNT, null, subQueries);
            if (!result.next()) throw new Exception("Can't get total count of data links");
            list.setTotalCount(result.getInt("counter"));
        } catch (Exception exception) {
            throw new DataException(ErrorCode.THESAURUS_INDEX_LOAD, dataRequest.getCondition(), exception);
        } finally {
            if (result != null) this.agentDatabase.closeQuery(result);
            this.removeViews(dataLink, dataRequest);
        }
        return list;
    }

    public DataLink load(String code) {
        DataLink dataLink = new DataLink();
        dataLink.setCode(code);
        return dataLink;
    }

    public NodeItemList loadItems(String codeDataLink, String codeIndicator, DataRequest dataRequest) {
        return new NodeItemList();
    }

    public NodeItemList searchItems(String codeDataLink, String codeIndicator, DataRequest dataRequest) {
        DataLink dataLink = this.load(codeDataLink);
        return this.readItems(dataLink, dataRequest);
    }

    public NodeItemList searchItems(DataLink dataLink, String codeIndicator, DataRequest dataRequest) {
        return this.readItems(dataLink, dataRequest);
    }

    public NodeItem locateItem(DataLink dataLink, String condition) {
        DataRequest dataRequest;
        NodeItemList list;
        dataRequest = new DataRequest();
        dataRequest.setCondition(condition);
        list = this.readItems(dataLink, dataRequest);
        return list.first();
    }

    public void addItem(String codeDataStore, String codeIndicator, String value) {
    }

    public void save(Thesaurus dataStore) {
    }

    public Object newObject() {
        return new DataLink();
    }

    public void loadAttribute(EventObject eventObject, String attribute) {
    }
}

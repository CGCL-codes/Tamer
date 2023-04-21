package edu.uga.galileo.voci.db.dao;

import static edu.uga.galileo.voci.db.QueryParserElement.INT;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import edu.uga.galileo.voci.bo.SearchBO;
import edu.uga.galileo.voci.db.QueryParser;
import edu.uga.galileo.voci.exception.DataTypeMismatchException;
import edu.uga.galileo.voci.logging.Logger;
import edu.uga.galileo.voci.model.Configuration;
import edu.uga.galileo.voci.model.ContentType;
import edu.uga.galileo.voci.model.SearchType;

/**
 * PSQLSearchDAO.java
 * 
 * @author <a href="mailto:cbking@uga.edu">Charles King</a>
 * @author <a href="mailto:cbking@uga.edu">Mark Durant</a>
 * @version 1.0
 */
public class PSQLSearchDAO extends SearchDAO {

    /**
	 * @see edu.uga.galileo.voci.db.dao.SearchDAO#deleteSearchContentObjects(int,
	 *      int, int)
	 */
    public void deleteSearchContentObjects(int elementId, int projectID, int contentType) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from search_index ");
        sql.append("where element_id=? and project_id=? and element_type=?");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(INT, elementId);
        qp.addPreparedStmtElementDefinition(INT, projectID);
        qp.addPreparedStmtElementDefinition(INT, contentType);
        try {
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.fatal("Couldn't execute deletion of search content objects in search_index table AND elementID=" + elementId, e);
        }
    }

    /**
	 * 
	 * @see edu.uga.galileo.voci.db.dao.SearchDAO#updateSearch(edu.uga.galileo.voci.bo.SearchBO)
	 */
    public void updateSearch(SearchBO sb) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into search_index(search_term,element_id, ");
        sql.append("metadata_reg_context,word_position,project_id,element_type) ");
        sql.append("values(?,?,?,?,?,?)");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(sb.getSearchTerm());
        qp.addPreparedStmtElementDefinition(INT, sb.getElementId());
        qp.addPreparedStmtElementDefinition(INT, sb.getMetadataRegContext());
        qp.addPreparedStmtElementDefinition(INT, sb.getWordPosition());
        qp.addPreparedStmtElementDefinition(INT, sb.getProjectId());
        qp.addPreparedStmtElementDefinition(INT, sb.getElementType());
        try {
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.error("Couldn't insert new search content into search_index table.. AND elementID= " + sb.getElementId(), e);
            throw e;
        }
    }

    /**
	 * 
	 * @see edu.uga.galileo.voci.db.dao.SearchDAO#deleteFromSearchIndex(int,
	 *      int, int)
	 */
    public void deleteFromSearchIndex(int projectID, int metadataRegContext, int elementType) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from search_index ");
        sql.append("where element_type=? and project_id=? and metadata_reg_context=?");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(INT, elementType);
        qp.addPreparedStmtElementDefinition(INT, projectID);
        qp.addPreparedStmtElementDefinition(INT, metadataRegContext);
        try {
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.fatal("Method:PSQLSearchDAO.deleteFromSearchIndex -- \n" + "Couldn't execute deletion from search_index table AND projectID=" + projectID, e);
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.SearchDAO#deleteFromSearchIndex(int,
	 *      int)
	 */
    @Override
    public void deleteFromSearchIndex(int projectId, int elementId) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from search_index ");
        sql.append("where project_id=? and element_id=? ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(INT, projectId);
        qp.addPreparedStmtElementDefinition(INT, elementId);
        try {
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.fatal("Method:PSQLSearchDAO.deleteFromSearchIndex -- \n" + "Couldn't execute deletion from search_index table AND projectID=" + projectId + ", elementID=" + elementId, e);
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.SearchDAO#getAllValuesAndMultipliersForMetadata(int,
	 *      int)
	 */
    @Override
    public ArrayList<String> getAllValuesAndMultipliersForMetadata(int metadataId, int type) {
        ArrayList<String> results = new ArrayList<String>();
        String contentType = ContentType.valueOf(type).toString().toLowerCase();
        StringBuffer sql = new StringBuffer();
        sql.append("select a.");
        sql.append(contentType);
        sql.append("_id, a.value, b.index_multiplier ");
        sql.append("from metadata2");
        sql.append(contentType);
        sql.append(" a, metadata_registry b ");
        sql.append("where a.metadata_id=? ");
        sql.append("and a.metadata_id=b.metadata_id ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(metadataId);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResultCount() == 0) {
                return null;
            }
            ArrayList row;
            for (int m = 0; m < qp.getResultCount(); m++) {
                row = qp.getRowResults(m);
                results.add(((Integer) row.get(0)).intValue() + "|" + ((String) row.get(1)) + "|" + ((Integer) row.get(2)).intValue());
            }
        } catch (SQLException e) {
            Logger.fatal("Couldn't matchValuesUsingRegularExpression b/c of SQL Exception.", e);
        }
        return results;
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.SearchDAO#performQueryOfWordsAndPhrases(java.util.ArrayList,
	 *      edu.uga.galileo.voci.model.SearchType, int, boolean, boolean, int,
	 *      java.util.ArrayList, edu.uga.galileo.voci.model.ContentType,
	 *      boolean, java.lang.String, java.lang.String, java.lang.String)
	 */
    @Override
    public ArrayList<ArrayList<Integer>> performQueryOfWordsAndPhrases(ArrayList<String> wordsAndOrPhrases, SearchType searchType, int projectId, boolean activeOnly, boolean allowsZeroes, int metadataId, ArrayList<Integer> includeOnly, ContentType contentType, boolean includeSupportProjects, String username, String updateDateStart, String updateDateEnd) {
        ArrayList<ArrayList<Integer>> finalResults = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> projectIds = new ArrayList<Integer>();
        ArrayList<Integer> elementIds = new ArrayList<Integer>();
        ArrayList<Integer> weights = new ArrayList<Integer>();
        ArrayList<Integer> contentTypes = new ArrayList<Integer>();
        StringBuffer sql = new StringBuffer();
        sql.append("select a.project_id, element_id, element_type, sum(c.index_multiplier) ");
        sql.append("from search_index a, ");
        if (activeOnly) {
            sql.append("elements b, ");
        }
        sql.append("metadata_registry c ");
        if (includeSupportProjects) {
            sql.append("where a.project_id in (");
            sql.append("   select project_id ");
            sql.append("   from projects ");
            sql.append("   where (project_id=? ");
            sql.append("      or (parent_id=? and searchable=true)) ");
            sql.append(") ");
        } else {
            sql.append("where a.project_id=? ");
        }
        sql.append("and (");
        boolean didOne = false;
        for (String word : wordsAndOrPhrases) {
            if (didOne) {
                sql.append("or ");
            }
            if (word.indexOf(' ') == -1) {
                if (word.indexOf('*') >= 0) {
                    sql.append("a.search_term like ? ");
                } else {
                    sql.append("a.search_term=? ");
                }
            } else {
                sql.append('(');
                sql.append(constructPhraseQuery(word));
                sql.append(") ");
            }
            didOne = true;
        }
        sql.append(") ");
        if (metadataId != -1) {
            sql.append("and metadata_reg_context=? ");
        }
        if (!allowsZeroes) {
            sql.append("and c.index_multiplier>0 ");
        }
        sql.append("and c.metadata_id=a.metadata_reg_context ");
        if (activeOnly) {
            sql.append("and b.active=true ");
            sql.append("and b.id=a.element_id ");
        }
        if (includeOnly != null) {
            sql.append("and element_id in (");
            sql.append(join(includeOnly, ", "));
            sql.append(") ");
        }
        if ((username != null) || ((updateDateStart != null) && (updateDateEnd != null))) {
            sql.append("and element_id in (");
            sql.append("(select content_id ");
            sql.append(" from audit_log ");
            sql.append(" where project_id=? ");
            if (username != null) {
                sql.append(" and user_id=");
                sql.append("  (select user_id ");
                sql.append("   from users ");
                sql.append("   where user_name=?) ");
            }
            if ((updateDateStart != null) && (updateDateEnd != null)) {
                sql.append("and update_date>=? ");
                sql.append("and update_date<=? ");
            }
            sql.append("and content_type>0 ");
            sql.append("and content_type<=");
            sql.append(ContentType.ITEM.getValue());
            sql.append(' ');
            sql.append(")) ");
        }
        if (contentType != null) {
            sql.append("and element_type=? ");
        } else {
            sql.append("and element_type<=");
            sql.append(ContentType.ITEM.getValue());
            sql.append(' ');
            sql.append("and element_type>0");
            sql.append(' ');
        }
        sql.append("group by a.project_id, element_id, element_type ");
        sql.append("order by sum desc ");
        sql.append("limit 200 ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(projectId);
        if (includeSupportProjects) {
            qp.addPreparedStmtElementDefinition(projectId);
        }
        for (String word : wordsAndOrPhrases) {
            if (word.indexOf(' ') == -1) {
                qp.addPreparedStmtElementDefinition(word.replace('*', '%'));
            } else {
                String[] terms = word.split(" ");
                for (int m = terms.length - 1; m >= 0; m--) {
                    if (m != terms.length - 1) {
                        qp.addPreparedStmtElementDefinition(projectId);
                    }
                    qp.addPreparedStmtElementDefinition(terms[m].replace('*', '%'));
                }
            }
        }
        if (metadataId != -1) {
            qp.addPreparedStmtElementDefinition(metadataId);
        }
        if ((username != null) || ((updateDateStart != null) && (updateDateEnd != null))) {
            qp.addPreparedStmtElementDefinition(projectId);
            if (username != null) {
                qp.addPreparedStmtElementDefinition(username);
            }
            if ((updateDateStart != null) && (updateDateEnd != null)) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    qp.addPreparedStmtElementDefinition(new Timestamp(sdf.parse(updateDateStart).getTime()));
                } catch (ParseException e) {
                    try {
                        qp.addPreparedStmtElementDefinition(new Timestamp(sdf.parse("01/01/2006").getTime()));
                    } catch (ParseException e1) {
                    }
                }
                try {
                    qp.addPreparedStmtElementDefinition(new Timestamp(sdf.parse(updateDateEnd).getTime()));
                } catch (ParseException e) {
                    try {
                        qp.addPreparedStmtElementDefinition(new Timestamp(sdf.parse("01/01/9999").getTime()));
                    } catch (ParseException e1) {
                    }
                }
            }
        }
        if (contentType != null) {
            qp.addPreparedStmtElementDefinition(contentType.getValue());
        }
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResultCount() == 0) {
                return null;
            }
            projectIds = qp.getResults(Integer.class, "project_id");
            elementIds = qp.getResults(Integer.class, "element_id");
            weights = qp.getResults(Integer.class, "sum");
            contentTypes = qp.getResults(Integer.class, "element_type");
            finalResults.add(elementIds);
            finalResults.add(weights);
            finalResults.add(contentTypes);
            finalResults.add(projectIds);
            return finalResults;
        } catch (SQLException e) {
            Logger.fatal("Couldn't matchValuesUsingRegularExpression b/c of SQL Exception.", e);
        } catch (DataTypeMismatchException e) {
            Logger.fatal("Coding problem: " + e.getMessage(), e);
        }
        return null;
    }

    /**
	 * Helper method to join an <code>ArrayList</code> of <code>Integer</code>
	 * objects together using a specified delimiter.
	 * 
	 * @param values
	 *            The array of <code>Integer</code> objects to join.
	 * @param delimiter
	 *            The <code>String</code> to insert between elements.
	 * @return The result of the join.
	 */
    private static String join(ArrayList<Integer> values, String delimiter) {
        StringBuffer joinedString = new StringBuffer();
        if (values == null) {
            return "";
        } else if (values.size() == 1) {
            return String.valueOf(values.get(0));
        } else {
            Integer integer;
            for (int m = 0; m < values.size(); m++) {
                integer = values.get(m);
                joinedString.append(String.valueOf(integer) == null ? "{null}" : integer);
                if (m != values.size() - 1) {
                    joinedString.append(delimiter);
                }
            }
            return joinedString.toString();
        }
    }

    /**
	 * Helper method for constructing correlated subqueries for phrase matching.
	 * 
	 * @param phrase
	 *            The phrase to match.
	 * @return A correlated subquery for finding it in the database.
	 */
    private String constructPhraseQuery(String phrase) {
        if ((phrase == null) || (phrase.trim().length() == 0)) {
            return "";
        }
        String[] splitPhrase = phrase.split(" ");
        return constructPhraseQuery(splitPhrase, splitPhrase.length - 1);
    }

    /**
	 * Recursive method to support the
	 * <code>contructPhraseQuery(String phrase)</code> method above.
	 * 
	 * @param terms
	 *            The terms in the phrase.
	 * @param currentPosition
	 *            The current position in the recursion.
	 * @return A correlated subquery.
	 */
    private String constructPhraseQuery(String[] terms, int currentPosition) {
        StringBuffer sql = new StringBuffer();
        if (currentPosition == terms.length - 1) {
            if (terms[currentPosition].indexOf('*') >= 0) {
                sql.append("search_term like ? ");
            } else {
                sql.append("search_term=? ");
            }
            sql.append("and word_position in (");
            sql.append(constructPhraseQuery(terms, currentPosition - 1));
            sql.append(") ");
        } else {
            sql.append("select word_position+1 ");
            sql.append("from search_index ");
            sql.append("where project_id=? ");
            if (terms[currentPosition].indexOf('*') >= 0) {
                sql.append("and search_term like ? ");
            } else {
                sql.append("and search_term=? ");
            }
            sql.append("and metadata_reg_context=a.metadata_reg_context ");
            sql.append("and element_id=a.element_id ");
            if (currentPosition > 0) {
                sql.append("and word_position in (");
                sql.append(constructPhraseQuery(terms, currentPosition - 1));
                sql.append(") ");
            }
        }
        return sql.toString();
    }
}

package org.verus.ngl.sl.objectmodel.technicalprocessing;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity class SAVE_SEARCH_HISTORY
 * 
 * @author root
 */
@Entity
@Table(name = "save_search_history")
@NamedQueries({ @NamedQuery(name = "SAVE_SEARCH_HISTORY.findByHistoryId", query = "SELECT s FROM SAVE_SEARCH_HISTORY s WHERE s.history_id = :history_id"), @NamedQuery(name = "SAVE_SEARCH_HISTORY.findByLibraryId", query = "SELECT s FROM SAVE_SEARCH_HISTORY s WHERE s.library_id = :library_id"), @NamedQuery(name = "SAVE_SEARCH_HISTORY.findByName", query = "SELECT s FROM SAVE_SEARCH_HISTORY s WHERE s.name = :name"), @NamedQuery(name = "SAVE_SEARCH_HISTORY.findByQueryXml", query = "SELECT s FROM SAVE_SEARCH_HISTORY s WHERE s.query_xml = :query_xml"), @NamedQuery(name = "SAVE_SEARCH_HISTORY.findByUserId", query = "SELECT s FROM SAVE_SEARCH_HISTORY s WHERE s.user_id = :user_id"), @NamedQuery(name = "SAVE_SEARCH_HISTORY.findByCreatedDate", query = "SELECT s FROM SAVE_SEARCH_HISTORY s WHERE s.created_date = :created_date"), @NamedQuery(name = "SAVE_SEARCH_HISTORY.findByMaxId", query = "SELECT MAX(s.history_id) FROM SAVE_SEARCH_HISTORY s ") })
public class SAVE_SEARCH_HISTORY implements Serializable {

    /**
     * EmbeddedId primary key field
     */
    @Id
    private Integer history_id;

    private Integer library_id;

    private String name;

    private String query_xml;

    private String user_id;

    private Timestamp created_date;

    private String description;

    /** Creates a new instance of SAVE_SEARCH_HISTORY */
    public SAVE_SEARCH_HISTORY() {
    }

    public Integer getHistory_id() {
        return history_id;
    }

    public void setHistory_id(Integer history_id) {
        this.history_id = history_id;
    }

    public Integer getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(Integer library_id) {
        this.library_id = library_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery_xml() {
        return query_xml;
    }

    public void setQuery_xml(String query_xml) {
        this.query_xml = query_xml;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

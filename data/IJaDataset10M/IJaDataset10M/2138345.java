package org.exist.eclipse.auto.internal.model;

import org.exist.eclipse.auto.query.IQuery;

/**
 * This class represents a single query entity.
 * 
 * @author Markus Tanner
 */
public class QueryEntity implements IQuery {

    private String _name;

    private String _notes;

    private int _quantity = 1;

    private String _query;

    protected IAutoModel _model;

    /**
	 * QueryEntity constructor
	 * 
	 * @param name
	 * @param notes
	 * @param quantity
	 * @param query
	 */
    public QueryEntity(String name, String notes, int quantity, String query) {
        _name = name;
        _notes = notes;
        _quantity = quantity;
        _query = query;
    }

    public String getName() {
        return _name;
    }

    /**
	 * Sets the name of the query.
	 * 
	 * @param name
	 */
    public void setName(String name) {
        _name = name;
    }

    public String getNotes() {
        return _notes;
    }

    /**
	 * Notes information will be set
	 * 
	 * @param notes
	 */
    public void setNotes(String notes) {
        _notes = notes;
    }

    public int getQuantity() {
        return _quantity;
    }

    /**
	 * Quantity information will be set
	 * 
	 * @param quantity
	 */
    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public String getQuery() {
        return _query;
    }

    /**
	 * The query String as such will be set
	 * 
	 * @param query
	 */
    public void setQuery(String query) {
        _query = query;
    }

    /**
	 * Sets the auto model
	 * 
	 * @param model
	 */
    public void setModel(IAutoModel model) {
        _model = model;
    }

    public int getId() {
        return 0;
    }

    public String getCollection() {
        return "/db";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + ((_notes == null) ? 0 : _notes.hashCode());
        result = prime * result + _quantity;
        result = prime * result + ((_query == null) ? 0 : _query.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        QueryEntity other = (QueryEntity) obj;
        if (_name == null) {
            if (other._name != null) return false;
        } else if (!_name.equals(other._name)) return false;
        if (_notes == null) {
            if (other._notes != null) return false;
        } else if (!_notes.equals(other._notes)) return false;
        if (_quantity != other._quantity) return false;
        if (_query == null) {
            if (other._query != null) return false;
        } else if (!_query.equals(other._query)) return false;
        return true;
    }
}

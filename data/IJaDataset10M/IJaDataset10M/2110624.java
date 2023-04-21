package edu.uga.galileo.voci.bo;

import java.util.ArrayList;
import edu.uga.galileo.voci.exception.*;
import edu.uga.galileo.voci.model.Configuration;
import edu.uga.galileo.voci.logging.Logger;
import edu.uga.galileo.voci.db.ColumnMetadata;

public class SearchBO extends VociBusinessObject {

    /**
	 * Default serial version UID.
	 * 
	 * TODO: Generate a real one when we're done...?
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * The max size of a search term compared to the db column size.
	 */
    private static int maxSearchTermSize;

    /**
	 * The projectID.
	 */
    private int projectId;

    /**
	 * The elementID.
	 */
    private int elementId;

    /**
	 * The word position.
	 */
    private int wordPosition;

    /**
	 * The metadataRegContext references the metadataId.
	 */
    private int metadataRegContext;

    /**
	 * The search term used in the search.
	 */
    private String searchTerm;

    /**
	 * The element type.
	 */
    private int elementType;

    /**
	 * The index multiplier is used to weigh the search term.
	 */
    private int indexMultiplier;

    /**
	 * Holder for database column metadata.
	 */
    private static ArrayList<ColumnMetadata> columnMetadata;

    /**
	 * Get the column metadata and set the max search term size.
	 */
    static {
        try {
            columnMetadata = Configuration.getConnectionPool().getColumnMetadata("search_index");
            for (ColumnMetadata cm : columnMetadata) {
                if (cm.getColumnName().equalsIgnoreCase("search_term")) {
                    maxSearchTermSize = cm.getDataSize();
                }
            }
        } catch (CouldNotRetrieveMetadataException e) {
            Logger.fatal("Couldn't get metadata for metadata_registry table AND " + "couln't set max search term size.... >  ", e);
        }
    }

    /**
	 * Empty constructor used for reflection purposes.
	 * 
	 */
    public SearchBO() {
    }

    /**
	 * Initialize the SearchBO object with following variables.
	 * 
	 * @param projectID
	 * @param elementID
	 * @param wordPosition
	 * @param metadataRegContext
	 * @param searchTerm
	 * @param elementType
	 */
    public SearchBO(int projectID, int elementID, int wordPosition, int metadataRegContext, String searchTerm, int elementType) {
        this.projectId = projectID;
        this.elementId = elementID;
        this.wordPosition = wordPosition;
        this.metadataRegContext = metadataRegContext;
        this.searchTerm = searchTerm;
        setSearchTerm(searchTerm);
        this.elementType = elementType;
    }

    /**
	 * @see edu.uga.galileo.voci.bo.VociBusinessObject#isActive()
	 * 
	 */
    public boolean isActive() {
        return true;
    }

    /**
	 * Get index multiplier
	 * 
	 * @return indexMultiplier
	 */
    public int getIndexMultiplier() {
        return indexMultiplier;
    }

    /**
	 * Set index multiplier
	 * 
	 * @param indexMultiplier
	 */
    public void setIndexMultiplier(int indexMultiplier) {
        this.indexMultiplier = indexMultiplier;
    }

    /**
	 * Get Element ID.
	 * 
	 * @return elementID
	 */
    public int getElementId() {
        return elementId;
    }

    /**
	 * Set element ID.
	 * 
	 * @param elementId
	 */
    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    /**
	 * Get element type
	 * 
	 * @return The element type.
	 */
    public int getElementType() {
        return elementType;
    }

    /**
	 * Set element type
	 * 
	 * @param elementType
	 */
    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    /**
	 * Get MetadataRegContext
	 * 
	 * @return metadataRegContext
	 */
    public int getMetadataRegContext() {
        return metadataRegContext;
    }

    /**
	 * Set metatdataRegContext
	 * 
	 * @param metadataRegContext
	 */
    public void setMetadataRegContext(int metadataRegContext) {
        this.metadataRegContext = metadataRegContext;
    }

    /**
	 * Get project ID
	 * 
	 * @return projectID
	 */
    public int getProjectId() {
        return projectId;
    }

    /**
	 * Set project ID
	 * 
	 * @param projectId
	 */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
	 * Get search term
	 * 
	 * @return searchTerm
	 */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
	 * Set search term and check the size against column metadata and truncate
	 * it at the max column size
	 * 
	 * @param searchTerm
	 */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
        if (this.searchTerm.length() > maxSearchTermSize) {
            this.searchTerm = this.searchTerm.substring(0, maxSearchTermSize);
        }
    }

    /**
	 * Get word position
	 * 
	 * @return The position of the word.
	 */
    public int getWordPosition() {
        return wordPosition;
    }

    /**
	 * Set word position
	 * 
	 * @param wordPosition
	 */
    public void setWordPosition(int wordPosition) {
        this.wordPosition = wordPosition;
    }

    /**
	 * Get gui elments
	 * 
	 * @see edu.uga.galileo.voci.bo.VociBusinessObject#getGUIElements()
	 */
    @Override
    public ArrayList<GUIElement> getGUIElements() {
        return null;
    }

    /**
	 * @see edu.uga.galileo.voci.bo.VociBusinessObject#getDBColumnMetadata()
	 */
    @Override
    protected ArrayList<ColumnMetadata> getDBColumnMetadata() {
        return null;
    }

    /**
	 * @see edu.uga.galileo.voci.bo.VociBusinessObject#getVariablesToString()
	 */
    @Override
    public String[] getVariablesToString() {
        String vars[] = new String[6];
        vars[0] = ("projectId");
        vars[1] = ("elementId");
        vars[2] = ("wordPosition");
        vars[3] = ("metadataRegContext");
        vars[4] = ("searchTerm");
        vars[5] = ("elementType");
        return vars;
    }

    /**
	 * @see edu.uga.galileo.voci.bo.VociBusinessObject#getType()
	 */
    @Override
    public int getType() {
        return -1;
    }

    /**
	 * @see edu.uga.galileo.voci.bo.VociBusinessObject#getId()
	 */
    @Override
    public int getId() {
        return 0;
    }

    /**
	 * @see edu.uga.galileo.voci.bo.VociBusinessObject#destroy()
	 */
    @Override
    public void destroy() {
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return this.getClass().getName() + " ::: projectId: " + projectId + "; elementId: " + elementId + "; elementType: " + elementType + "; indexMultiplier: " + indexMultiplier + "; metadataRegContext: " + metadataRegContext + "; searchTerm: " + searchTerm + "; wordPosition: " + wordPosition;
    }
}

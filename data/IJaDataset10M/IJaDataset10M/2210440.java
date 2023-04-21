package medi.swing.panel;

import javatools.db.DbException;
import javatools.db.DbIterator;
import javatools.util.StringUtils;

/** It is a panel showing the found data after a search.
 * @author Antonio Petrelli
 * @version 0.2.1
 */
public class SearchDataPanel extends AbstractDataPanel {

    /** Creates new SearchDataPanel */
    public SearchDataPanel() {
        spMessage = java.util.ResourceBundle.getBundle("res/MediBundle").getString("Loading_data_item_details");
        setTableModelType("");
    }

    /** Links a new data ID. Actually it does nothing here.
     * @param dataID The data ID to link.
     * @throws DbException If something goes wrong.
     */
    protected void linkNewData(Long dataID) throws DbException {
    }

    /** Shows contained items.
     * @throws DbException If something goes wrong.
     */
    public void showItems() throws DbException {
        DbIterator rowIt;
        init();
        rowIt = prv.searchData(StringUtils.toTokenList(dataName.toUpperCase()), StringUtils.toTokenList(fileName.toUpperCase()), StringUtils.toTokenList(author.toUpperCase()), StringUtils.toTokenList(editor.toUpperCase()), StringUtils.toTokenList(genre.toUpperCase()), StringUtils.toTokenList(dataSet.toUpperCase()), fileTypes).iterator();
        fillTable(rowIt);
    }

    /** Sets the search terms to find out.
     * @param dataName The data name.
     * @param fileName The file name.
     * @param author The author.
     * @param editor The editor.
     * @param genre The genre.
     * @param dataSet The data set.
     * @param fileTypes The file-types-IDs-array.
     */
    public void setSearchTerms(String dataName, String fileName, String author, String editor, String genre, String dataSet, Integer[] fileTypes) {
        this.dataName = dataName;
        this.fileName = fileName;
        this.author = author;
        this.editor = editor;
        this.genre = genre;
        this.dataSet = dataSet;
        this.fileTypes = fileTypes;
    }

    protected void unlinkData(Long[] dataIDs) throws DbException {
    }

    private String dataName, fileName, author, editor, genre, dataSet;

    private Integer[] fileTypes;
}

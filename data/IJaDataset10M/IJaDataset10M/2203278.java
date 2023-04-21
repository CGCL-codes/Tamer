package editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import editor.model.DocumentBase.Type;
import editor.utility.EditState;
import editor.utility.EditorProperty;
import editor.view.EditorViewBase;

/**
 * Interface of a editor model,
 * <br>which is specified the method and data model for a basic editor.
 * <br>View and controller can get corresponce data by invoke the method here.
 * 
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/
public interface IEditorModel {

    HashMap<Integer, DocumentBase> docTable = new HashMap<Integer, DocumentBase>();

    /**
     * Add known document both in model and view
     * @param doc
     * @return true if add success
     */
    public boolean addDocument(DocumentBase doc);

    /**
     * Create new document with essensial parames
     * @param docName
     * @param content
     * @param type
     * @return
     */
    public DocumentBase getDocumentBase(String docName, String content, Type type);

    /**
     * Add document model when user opens or creates a code file
     * 
     * @param docName
     * @return add finished or not 
     */
    public void addDocumentToEditor(String docName, EditState editState, Type type);

    /**
     * Get document according the given index number
     * @param index
     * @return the document for given index or null
     */
    public DocumentBase getDocument(int index);

    /**
     * Remove document from editor
     * 
     * @param docIndex
     * @return
     */
    public boolean removeDocumentFromEditor(String docName);

    /**
     * Check the editor is opening or not
     * 
     * @return true if the editor is opening
     */
    public boolean isEditorOpened();

    /**
     * Get the editor menu bar
     * 
     * @return menu bar and tool bar object for the editor
     */
    public List<IMenuModel> getEditorMenuList();

    /**
     * Open the  editor
     *
     */
    public void initializeEditor();

    /**
     * Close the editor
     *
     */
    public void closeEditor();

    /**
     * Check the document is modified or not
     * 
     * @param docIndex
     * @return true if modified
     */
    public boolean isDocumentModified(String docName);

    /**
     * Change edit state for one document
     * 
     * @param docIndex
     * @param newState
     */
    public void changeEditState(String docName, EditState newState);

    /**
     * Get one docment's edit state
     * @param docIndex
     * @return its edit state
     */
    public EditState getEditState(String docName);

    /**
     * Set one document modified or not
     * 
     * @param docIndex
     * @param modified
     */
    public void setModified(String docName, boolean modified);

    /**
     * Get whole property list of the editor
     * 
     * @return editor list
     */
    public HashMap<EditorProperty, String> getEditorPropertyList();

    /**
     * Get the property value 
     * 
     * @param property
     * @return property value 
     */
    public String getEditorProperty(EditorProperty property);

    /**
     * Set the property list of the editor
     * 
     * @param propertyList
     */
    public void setProperty(HashMap<String, String> propertyList);

    /**
     * Set the view to the model
     * @param view
     */
    public void setView(EditorViewBase view);

    /**
     * Check out the editor model has document or not
     * @return true if has 
     */
    public boolean hasDocument();

    /**
     * Get document number
     * @return
     */
    public int getDocmentNumber();

    /**
     * Get main frame popup menu model
     * @return main frame popup menu model
     */
    public List<IMenuModel> getPopUpMenu(String menuLocation);

    /**
     * Set main frame popup menu model
     * @param menu
     */
    public void setPopUpMenu(String menuLocation, List<IMenuModel> menu);

    /**
     * Remove document base by known index
     * @param docIndex
     */
    public void removeDocument(int docIndex);
}

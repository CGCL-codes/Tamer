package fr.soleil.bensikin.data.context;

import javax.swing.JTextField;
import fr.soleil.bensikin.components.BensikinMenuBar;
import fr.soleil.bensikin.components.context.detail.IDTextField;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.containers.context.ContextDataPanel;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;

/**
 * Represents the non-attribute dependant informations attached to a context
 * 
 * @author CLAISSE
 */
public class ContextData {

    public static final int COLUMNS_COUNT = 6;

    private int id;

    private java.sql.Date creation_date = null;

    private String name = "";

    private String author_name = "";

    private String reason = "";

    private String description = "";

    private String path = null;

    /**
	 * The XML tag name used for the "id" property
	 */
    public static final String ID_PROPERTY_XML_TAG = "ID";

    /**
	 * The XML tag name used for the "creation date" property
	 */
    public static final String CREATION_DATE_PROPERTY_XML_TAG = "CreationDate";

    /**
	 * The XML tag name used for the "name" property
	 */
    public static final String NAME_PROPERTY_XML_TAG = "Name";

    /**
	 * The XML tag name used for the "author" property
	 */
    public static final String AUTHOR_PROPERTY_XML_TAG = "Author";

    /**
	 * The XML tag name used for the "reason" property
	 */
    public static final String REASON_PROPERTY_XML_TAG = "Reason";

    /**
	 * The XML tag name used for the "description" property
	 */
    public static final String DESCRIPTION_DATE_PROPERTY_XML_TAG = "Description";

    public static final String IS_MODIFIED_PROPERTY_XML_TAG = "isModified";

    public static final String PATH_PROPERTY_XML_TAG = "path";

    private Context context;

    /**
	 * Converts a SnapContext object to a ContextData
	 * 
	 * @param source
	 */
    public ContextData(SnapContext source) {
        this.setAuthor_name(source.getAuthor_name());
        this.setCreation_date(source.getCreation_date());
        this.setDescription(source.getDescription());
        this.setId(source.getId());
        this.setName(source.getName());
        this.setReason(source.getReason());
    }

    /**
	 * Builds a ContextData from parameters
	 * 
	 * @param _id
	 * @param _creation_date
	 * @param _name
	 * @param _author_name
	 * @param _reason
	 * @param _description
	 */
    public ContextData(int _id, java.sql.Date _creation_date, String _name, String _author_name, String _reason, String _description) {
        this.id = _id;
        this.creation_date = _creation_date;
        this.name = _name;
        this.author_name = _author_name;
        this.reason = _reason;
        this.description = _description;
    }

    /**
	 * Default constructor, does nothing
	 */
    public ContextData() {
    }

    /**
	 * Displays this data in the current context's data area
	 */
    public void push() {
        ContextDataPanel contextDataPanel = ContextDataPanel.getInstance();
        IDTextField idField = contextDataPanel.getIDField();
        String id_s = this.getId() == -1 ? "" : String.valueOf(this.getId());
        idField.setText(id_s);
        JTextField timeField = contextDataPanel.getCreationDateField();
        String creation_date_s = this.getCreation_date() == null ? "" : this.getCreation_date().toString();
        timeField.setText(creation_date_s);
        JTextField nameField = contextDataPanel.getNameField();
        if (!nameField.getText().equals(this.getName())) {
            nameField.setText(this.getName());
        }
        JTextField authorField = contextDataPanel.getAuthorNameField();
        if (!authorField.getText().equals(this.getAuthor_name())) {
            authorField.setText(this.getAuthor_name());
        }
        JTextField reasonField = contextDataPanel.getReasonField();
        if (!reasonField.getText().equals(this.getReason())) {
            reasonField.setText(this.getReason());
        }
        JTextField descriptionField = contextDataPanel.getDescriptionField();
        if (!descriptionField.getText().equals(this.getName())) {
            descriptionField.setText(this.getDescription());
        }
        BensikinMenuBar.getInstance().updateRegisterItem();
        ContextActionPanel.getInstance().updateRegisterButton();
    }

    /**
	 * @return Returns the description.
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 *            The description to set.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return Returns the id.
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id
	 *            The id to set.
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the reason.
	 */
    public String getReason() {
        return reason;
    }

    /**
	 * @param reason
	 *            The reason to set.
	 */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
	 * @return Returns the context.
	 */
    public Context getContext() {
        return context;
    }

    /**
	 * @param context
	 *            The context to set.
	 */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
	 * @return Returns the author_name.
	 */
    public String getAuthor_name() {
        return author_name;
    }

    /**
	 * @param author_name
	 *            The author_name to set.
	 */
    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    /**
	 * @return Returns the creation_date.
	 */
    public java.sql.Date getCreation_date() {
        return creation_date;
    }

    /**
	 * @param creation_date
	 *            The creation_date to set.
	 */
    public void setCreation_date(java.sql.Date creation_date) {
        this.creation_date = creation_date;
    }

    /**
	 * @return Returns the path.
	 */
    public String getPath() {
        return path;
    }

    /**
	 * @param path
	 *            The path to set.
	 */
    public void setPath(String path) {
        this.path = path;
    }
}

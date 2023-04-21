package wicket.contrib.tinymce.settings;

/**
 * This plugin adds table management functionality to TinyMCE.
 * <p>
 * Note: Only basic functionality is implemented, more work is needed.
 * 
 * @author Iulian-Corneliu COSTAN
 */
public class TablePlugin extends Plugin {

    private static final long serialVersionUID = 1L;

    /** table button * */
    public PluginButton tableControls;

    /**
	 * Construct.
	 */
    public TablePlugin() {
        super("table");
        tableControls = new PluginButton("tablecontrols", this);
    }

    /**
	 * @return the table button
	 */
    public PluginButton getTableControls() {
        return tableControls;
    }
}

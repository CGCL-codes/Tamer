package mipt.common.appl;

import mipt.common.Utils;

/**
 * Singleton that manages help showng. Help item address is formed
 *   from root (common), chapter, topic and extension (common)
 * Despite true Singleton pattern this class (and subclasses)
 *   has public constructor - creation must have arguments
 * Root can be null here (but not in some subclasses!); null root results
 *  in addres started with chapter, and "" root -//- separator+chapter
 * Extension can be null or "" for no extension
 */
public abstract class HelpManager {

    protected String root;

    protected String extension = ".html";

    protected boolean showErrors = true;

    protected static HelpManager instance = null;

    /**
 * 
 * @param root java.lang.String
 */
    public HelpManager(String root) {
        this.root = root;
        instance = this;
    }

    public HelpManager(String root, String extension) {
        this(root);
        this.extension = extension;
    }

    /**
 * Stops showing help
 */
    public abstract void exit();

    /**
 * Returns a single instance of this class, that will show help
 *  (from the user directory as root if construstor was not invoked)
 * @return mipt.HelpManager
 */
    public static HelpManager getInstance() {
        if (instance == null) {
            new FileHelpManager(".");
        }
        return instance;
    }

    /**
 * Shows the given help topic from the given help chapter
 * @return boolean : true if successful, false can be used to show error by caller
 * @param topic java.lang.String
 * @param chapter java.lang.String : as usual = null
 */
    public abstract boolean help(String topic, String chapter);

    /**
 * Call this from your help() method
 * @return java.lang.String
 * @param topic java.lang.String
 * @param chapter java.lang.String
 * @param separator java.lang.String - separates all parts except extension
 */
    protected String getPath(String topic, String chapter, String separator) {
        String path = root == null ? "" : root.endsWith(separator) ? root : root + separator;
        if ((chapter != null) && !chapter.equals("")) path += (chapter + separator);
        path += topic;
        if (extension != null) path += extension;
        return path;
    }

    /**
 * Call this from your help() method
 * Show (or doesn`t show, if showErrors==false) help error in standard dialog
 * Overriders should have "if(!showErrors) return;" here
 */
    protected void helpError(String resourceName) {
        if (!showErrors) return;
        Utils.showError(Utils.getFileNotFoundString(resourceName));
    }

    /**
 * If showErrors is true standard dialog of invisible frame can be shown
 * @param showErrors boolean
 */
    public void setShowErrors(boolean showErrors) {
        this.showErrors = showErrors;
    }

    public final String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}

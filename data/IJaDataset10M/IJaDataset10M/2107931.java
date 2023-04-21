package unbbayes.gui;

/**
 * 
 * Interface for JInternalWindow's subclasses (like NetworkWindow, MSBNWindow and OOBNWindow) 
 * which are aware of what file extension is to be used and what extensions should be used
 * for file chooser's filter
 * @author Shou Matsumoto
 *
 */
public interface IFileExtensionAwareWindow {

    /**
	 * Obtains an array of file extensions supported by this network window.
	 * The file extensions should come without the dot
	 * @return
	 */
    public String[] getSupportedFileExtensions();

    /**
	 * Gets a description of supported file extensions,
	 * which may be shown to the user through file chooser's fileter to explain what
	 * file format are supported.
	 * E.g. "Net (.net), XMLBIF(.xml), UnBBayes File (.ubf)"
	 */
    public String getSupportedFilesDescription();

    /**
	 * Obtains a message to be shown to user while saving a net editted by a window implementing this
	 * interface.
	 * For example, you may want a FileChooser to show personalized title depending on what you are saving.
	 * @return
	 */
    public String getSavingMessage();
}

package eclient;

/**
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public interface EMenuBarListener {

    /**
     * The File-&gt;Close menu item was selected. Close the
     * frame.
     */
    void doClose();

    /**
     * The File-&gt;Save menu item was selected.
     */
    void doSave();

    /**
     * The File-&gt;Save As menu item was selected.
     */
    void doSaveAs();

    /**
     * The Edit-&gt;Rename menu item was selected.
     */
    void doRename();

    /**
     * Sets the message key.
     */
    void doMessageKey();
}

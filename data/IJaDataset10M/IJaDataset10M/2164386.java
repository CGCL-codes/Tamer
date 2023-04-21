package net.sourceforge.jcpusim.ui.workspace;

import java.io.File;
import net.sourceforge.jcpusim.ui.tabs.Document;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

/**
 * The Workspace interface defines what a workspace should provide
 */
public interface Workspace {

    /**
	 * Create a new folder
	 * @param composite the parent composite
	 * @return cTabFolder (view)
	 */
    public CTabFolder newFolder(Composite composite);

    /**
	 * Initialises the workspace
	 */
    public void initialiseWorkspace();

    /**
	 * Sets up the layout of the workspace
	 */
    public void layout();

    /**
	 * Get the current document
	 * @return current document
	 */
    public Document getActiveDocument();

    /**
	 * Create a new document
	 * @return true on success
	 */
    public boolean newDocument();

    /**
	 * Load a document
	 * @param file to load
	 * @return true on success
	 */
    public boolean loadDocument(File file);

    /**
	 * Dispose of the workspace
	 */
    public void dispose();

    /**
	 * Initialise the tabs
	 */
    public void initialiseTabs();

    /**
	 * Update the tabs
	 */
    public void updateTabs();

    public Composite getComposite();
}

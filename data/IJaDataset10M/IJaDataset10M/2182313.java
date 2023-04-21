package net.sf.iauthor.ui.editors;

import java.io.ByteArrayInputStream;
import net.sf.iauthor.core.Plugin;
import net.sf.iauthor.core.Scene;
import net.sf.iauthor.core.Session;
import net.sf.iauthor.ui.editors.pages.ActorsPage;
import net.sf.iauthor.ui.editors.pages.GoalsPage;
import net.sf.iauthor.ui.editors.pages.ImagePage;
import net.sf.iauthor.ui.editors.pages.ItemsPage;
import net.sf.iauthor.ui.editors.pages.LocationsPage;
import net.sf.iauthor.ui.editors.pages.NotesPage;
import net.sf.iauthor.ui.editors.pages.SceneContentPage;
import net.sf.iauthor.ui.editors.pages.SceneDetailsPage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * @author Andreas Beckers
 */
public class SceneEditor extends FormEditor {

    public static final String ID = "net.sf.iauthor.ui.editors.scene";

    private boolean _dirty;

    protected void markDirty() {
        if (_dirty) return;
        _dirty = true;
        firePropertyChange(PROP_DIRTY);
    }

    /**
	 * 
	 */
    @Override
    public void doSave(IProgressMonitor monitor) {
        Session session = Plugin.getPlugin().getSession();
        session.beginTransaction();
        commitPages(true);
        Scene scene = (Scene) getEditorInput().getAdapter(Scene.class);
        session.commitTransaction();
        setPartName(scene.getTitle());
        _dirty = false;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
    @Override
    protected void addPages() {
        try {
            addPage(new SceneContentPage(this));
            addPage(new SceneDetailsPage(this));
            addPage(new ActorsPage(this));
            addPage(new LocationsPage(this));
            addPage(new ItemsPage(this));
            addPage(new NotesPage(this));
            addPage(new ImagePage(this));
            addPage(new GoalsPage(this));
            Scene scene = (Scene) getEditorInput().getAdapter(Scene.class);
            setPartName(scene.getTitle());
            if (scene.getImageThumb() != null) {
                setTitleImage(new Image(Display.getDefault(), new ByteArrayInputStream(scene.getImageThumb().getData())));
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}

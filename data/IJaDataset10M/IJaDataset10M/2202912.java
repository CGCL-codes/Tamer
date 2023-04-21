package hu.cubussapiens.owl.editor;

import hu.cubussapiens.owl.editor.internal.OWLOntologyContentProvider;
import hu.cubussapiens.owl.editor.internal.OWLOntologyLabelProvider;
import java.io.File;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

/**
 * @author balage
 *
 */
public class OWLEditor extends EditorPart {

    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

    OWLOntology input;

    TreeViewer viewer;

    /**
	 * 
	 */
    public OWLEditor() {
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        if (input instanceof IFileEditorInput) {
            File f = ((IFileEditorInput) input).getFile().getLocation().toFile();
            try {
                this.input = manager.loadOntologyFromPhysicalURI(f.toURI());
                if (viewer != null) viewer.setInput(this.input);
            } catch (OWLOntologyCreationException e) {
                throw new PartInitException("Can't load given file as an ontology", e);
            }
        }
        setSite(site);
        setInput(input);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());
        viewer = new TreeViewer(parent);
        viewer.setContentProvider(new OWLOntologyContentProvider());
        viewer.setLabelProvider(new OWLOntologyLabelProvider());
        if (input != null) viewer.setInput(input);
    }

    @Override
    public void setFocus() {
        if (viewer != null) viewer.getControl().setFocus();
    }
}

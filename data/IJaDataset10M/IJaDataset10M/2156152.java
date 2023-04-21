package pi.eclipse.cle.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import pi.eclipse.cle.builders.CupLexNature;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
class AbstractEditor extends TextEditor {

    /**
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        if (input instanceof IFileEditorInput) {
            final IFileEditorInput fei = (IFileEditorInput) input;
            CupLexNature.updateProject(fei.getFile().getProject());
        }
    }
}

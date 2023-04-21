package org.dozer.eclipse.plugin;

import org.dozer.eclipse.plugin.editorpage.DozerFormEditor;
import org.dozer.eclipse.plugin.editorpage.Messages;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.ui.internal.Logger;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

@SuppressWarnings("restriction")
public class DozerMultiPageEditor extends XMLMultiPageEditorPart {

    private int fDozerPageIndex;

    private int fLastChangeIndex;

    private DozerFormEditor dozerFormEditor;

    @Override
    protected void createPages() {
        super.createPages();
        try {
            dozerFormEditor = new DozerFormEditor(this);
            fDozerPageIndex = this.addPage(dozerFormEditor, this.getEditorInput());
            setPageText(fDozerPageIndex, Messages.getString("DozerMappingEditor.label"));
            setActivePage(fDozerPageIndex);
        } catch (PartInitException e) {
            Logger.logException(e);
        }
    }

    @Override
    protected void pageChange(int newPageIndex) {
        super.pageChange(newPageIndex);
        fLastChangeIndex = newPageIndex;
    }

    /**
	 * Returns the WTP Source Editor
	 * 
	 * @return WTP Source Editor
	 */
    public StructuredTextEditor getSourceEditor() {
        return (StructuredTextEditor) this.getEditor(1);
    }

    public void changeToSourcePage() {
        this.setActivePage(1);
    }
}

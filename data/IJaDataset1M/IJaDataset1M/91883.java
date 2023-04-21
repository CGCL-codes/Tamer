package com.teaminabox.eclipse.wiki.outline;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.model.AdaptableList;
import com.teaminabox.eclipse.wiki.WikiConstants;
import com.teaminabox.eclipse.wiki.WikiPlugin;
import com.teaminabox.eclipse.wiki.editors.WikiEditor;
import com.teaminabox.eclipse.wiki.renderer.ContentRenderer;
import com.teaminabox.eclipse.wiki.renderer.RendererFactory;
import com.teaminabox.eclipse.wiki.renderer.StructureClosure;

public final class WikiModelFactory {

    private static final WikiModelFactory INSTANCE = new WikiModelFactory();

    private WikiModelFactory() {
        super();
    }

    /**
	 * Returns the content outline for the given manifest file.
	 * 
	 * @param adaptable
	 *            the element for which to return the content outline
	 * @return the content outline for the argument
	 */
    public AdaptableList getContentOutline(IAdaptable adaptable, WikiEditor editor) {
        return new AdaptableList(getContents((IFile) adaptable, editor));
    }

    private MarkElement[] getContents(final IFile file, final WikiEditor editor) {
        try {
            final MarkElement root = new MarkElement(file, editor.getContext().getWikiNameBeingEdited(), 0, 0, WikiPlugin.getDefault().getImageRegistry().getDescriptor(WikiConstants.WIKI_ICON));
            ContentRenderer renderer = RendererFactory.createContentRenderer();
            renderer.forEachHeader(editor.getContext(), new StructureClosure() {

                public void acceptHeader(String header, int line) throws BadLocationException {
                    int offset = editor.getOffset(line);
                    new MarkElement(root, header, offset, 0, WikiPlugin.getDefault().getImageRegistry().getDescriptor(WikiConstants.WIKI_ICON));
                }
            });
            return new MarkElement[] { root };
        } catch (Exception e) {
            WikiPlugin.getDefault().log(e.getLocalizedMessage(), e);
        }
        return new MarkElement[0];
    }

    public static WikiModelFactory getInstance() {
        return WikiModelFactory.INSTANCE;
    }
}

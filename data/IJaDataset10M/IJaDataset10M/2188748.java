package org.deft.projecttype.texlipse;

import org.deft.repository.integrator.ArtifactReferencePosition;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

public class TexlipseArtifactReferencePosition implements ArtifactReferencePosition {

    private LatexEditorChapterDocument document;

    private Position position;

    public TexlipseArtifactReferencePosition(LatexEditorChapterDocument document, Position position) {
        this.document = document;
        this.position = position;
    }

    @Override
    public LatexEditorChapterDocument getChapterDocument() {
        return document;
    }

    /**
	 * Tries to read the reference tag ( <artifactreference id="some_id"> )
	 * If the reference tag cannot be read for some reason, an empty string is returned.
	 */
    @Override
    public String extractReferenceTag() {
        IDocument doc = document.getDocument();
        String tag;
        try {
            tag = doc.get(position.getOffset(), position.getLength());
            return tag;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Position getPosition() {
        return position;
    }
}

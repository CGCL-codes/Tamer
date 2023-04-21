package flattree.eclipse.jface.text;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import flattree.eclipse.jface.text.section.LeafSection;
import flattree.eclipse.jface.text.section.Section;
import flattree.eclipse.jface.text.section.SectionIterator;

public abstract class SectionPresentationRepairer implements IPresentationRepairer {

    private Color evenColor;

    private Color oddColor;

    public SectionPresentationRepairer(Color evenColor, Color oddColor) {
        this.evenColor = evenColor;
        this.oddColor = oddColor;
    }

    public void setDocument(IDocument document) {
    }

    protected abstract Section getSection();

    protected Section getSection(int offset) {
        Section section = getSection();
        if (section == null) {
            return null;
        } else {
            return section.get(offset, 0);
        }
    }

    public void createPresentation(TextPresentation presentation, ITypedRegion region) {
        createPresentation(presentation, (IRegion) region);
    }

    public void createPresentation(TextPresentation presentation, IRegion region) {
        Section section = getSection(region.getOffset());
        if (section == null) {
        } else {
            colorLeaves(presentation, new SectionIterator(section), region);
        }
    }

    protected void colorLeaves(TextPresentation presentation, SectionIterator iterator, IRegion region) {
        boolean even = true;
        int offset = region.getOffset();
        int endOffset = region.getOffset() + region.getLength();
        for (Section section : iterator) {
            if (section instanceof LeafSection) {
                if (section.getLength() > 0) {
                    if (offset < section.getOffset()) {
                        addRange(presentation, offset, section.getOffset() - offset, null);
                    }
                    offset = section.getOffset();
                    if (offset >= endOffset) {
                        break;
                    }
                    Color background = getLeafColor(even);
                    even = !even;
                    addRange(presentation, offset, Math.min(endOffset - offset, section.getLength()), background);
                    offset += section.getLength();
                }
            } else {
                even = true;
            }
            if (offset >= endOffset) {
                break;
            }
        }
        if (offset < endOffset) {
            addRange(presentation, offset, endOffset - offset, null);
        }
    }

    private Color getLeafColor(boolean even) {
        if (even) {
            return evenColor;
        } else {
            return oddColor;
        }
    }

    protected void addRange(TextPresentation presentation, int offset, int length, Color background) {
        StyleRange styleRange = new StyleRange(offset, length, null, background, SWT.NORMAL);
        presentation.addStyleRange(styleRange);
    }
}

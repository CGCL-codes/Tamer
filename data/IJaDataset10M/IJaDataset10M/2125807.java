package net.sf.elbe.ui.editors.ldif.text;

import net.sf.elbe.core.model.ldif.LdifEOFPart;
import net.sf.elbe.core.model.ldif.LdifFile;
import net.sf.elbe.core.model.ldif.LdifInvalidPart;
import net.sf.elbe.core.model.ldif.LdifPart;
import net.sf.elbe.core.model.ldif.container.LdifContainer;
import net.sf.elbe.core.model.ldif.lines.LdifLineBase;
import net.sf.elbe.core.model.ldif.lines.LdifSepLine;
import net.sf.elbe.core.model.ldif.lines.LdifValueLineBase;
import net.sf.elbe.core.model.ldif.parser.LdifParser;
import net.sf.elbe.ui.ELBEUIConstants;
import net.sf.elbe.ui.ELBEUIPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultTextDoubleClickStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;

public class LdifDoubleClickStrategy implements ITextDoubleClickStrategy {

    private static final int OFFSET = 0;

    private static final int LENGTH = 1;

    /**
	 * Default double click strategy
	 */
    private DefaultTextDoubleClickStrategy delegateDoubleClickStrategy;

    public LdifDoubleClickStrategy() {
        this.delegateDoubleClickStrategy = new DefaultTextDoubleClickStrategy();
    }

    public void doubleClicked(ITextViewer viewer) {
        if (!ELBEUIPlugin.getDefault().getPreferenceStore().getBoolean(ELBEUIConstants.PREFERENCE_LDIFEDITOR_DOUBLECLICK_USELDIFDOUBLECLICK)) {
            delegateDoubleClickStrategy.doubleClicked(viewer);
        } else {
            int cursorPos = viewer.getSelectedRange().x;
            if (cursorPos < 0) {
                return;
            }
            try {
                LdifParser parser = new LdifParser();
                IDocument document = viewer.getDocument();
                ITypedRegion partition = document.getPartition(cursorPos);
                int offset = partition.getOffset();
                int relativePos = cursorPos - offset;
                String s = document.get(partition.getOffset(), partition.getLength());
                LdifFile model = parser.parse(s);
                LdifContainer container = LdifFile.getContainer(model, relativePos);
                if (container != null) {
                    LdifPart part = LdifFile.getContainerContent(container, relativePos);
                    if (part != null && !(part instanceof LdifSepLine) && !(part instanceof LdifInvalidPart) && !(part instanceof LdifEOFPart)) {
                        int[] range = null;
                        if (part instanceof LdifValueLineBase) {
                            LdifValueLineBase line = (LdifValueLineBase) part;
                            range = getRange(relativePos, part.getOffset(), new String[] { line.getRawLineStart(), line.getRawValueType(), line.getRawValue() });
                        } else if (part instanceof LdifLineBase) {
                            LdifLineBase line = (LdifLineBase) part;
                            range = new int[] { part.getOffset(), part.getLength() - line.getRawNewLine().length() };
                        }
                        int start = range != null ? range[OFFSET] : part.getOffset();
                        start += offset;
                        int length = range != null ? range[LENGTH] : part.getLength();
                        viewer.setSelectedRange(start, length);
                    } else {
                        delegateDoubleClickStrategy.doubleClicked(viewer);
                    }
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private int[] getRange(int pos, int offset, String[] parts) {
        for (int i = 0; i < parts.length; i++) {
            if (parts[i] != null) {
                if (pos < offset + parts[i].length()) {
                    return new int[] { offset, parts[i].length() };
                }
                offset += parts[i].length();
            }
        }
        return null;
    }
}

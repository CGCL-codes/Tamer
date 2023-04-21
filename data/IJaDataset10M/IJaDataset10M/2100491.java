package com.ilog.translator.java2cs.translation.textrewriter;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import com.ilog.translator.java2cs.translation.ITranslationContext;

public class InsertHereTransformer extends TextRewriter {

    protected int pos = 1;

    public InsertHereTransformer(ITranslationContext context) {
        super(context);
        transformerName = "Insert text transformer";
    }

    @Override
    public boolean transform(IProgressMonitor pm, ASTNode cunit) {
        return true;
    }

    @Override
    public List<TextEdit> computeEdit(IProgressMonitor pm, IBuffer buffer) throws CoreException {
        final List<TextEdit> edits = new ArrayList<TextEdit>();
        final String insert_C = "/* insert_here:";
        final String remove_C = "/* remove_here:";
        final int insert_lC = insert_C.length();
        final int remove_lC = remove_C.length();
        final int size = buffer.getLength() - 1;
        try {
            if (pos > -1) {
                for (int i = pos; i < size; i++) {
                    if (searchFor(insert_C, size, buffer, i)) {
                        final int start_comment = i;
                        int end_comment = i;
                        boolean end = false;
                        for (int j = i + insert_lC; !end; j++) {
                            if (j + " */".length() < size && buffer.getText(j, " */".length()).equals(" */")) {
                                end = true;
                                end_comment = j;
                                final String insertexpr = buffer.getText(start_comment + insert_lC, end_comment - start_comment - insert_lC);
                                edits.add(new ReplaceEdit(start_comment, end_comment - start_comment + 3, insertexpr));
                            }
                        }
                    } else if (searchFor(remove_C, size, buffer, i)) {
                        final int start_comment = i;
                        int end_comment = i;
                        boolean end = false;
                        for (int j = i + remove_lC; !end; j++) {
                            if (j + " */".length() < size && buffer.getText(j, " */".length()).equals(" */")) {
                                end = true;
                                end_comment = j;
                                final String removeCpt = buffer.getText(start_comment + insert_lC, end_comment - start_comment - insert_lC);
                                final int cpt = Integer.parseInt(removeCpt);
                                final int length = end_comment - start_comment + 3 + cpt;
                                edits.add(new ReplaceEdit(start_comment, length, ""));
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            context.getLogger().logException("Exception during keywords replacement in " + fCu.getElementName(), e);
        }
        return edits;
    }

    private boolean searchFor(String pattern, int sizeOfbuffer, IBuffer buffer, int pos) {
        final int patternLength = pattern.length();
        if ((pos + patternLength < sizeOfbuffer) && buffer.getText(pos, patternLength).equals(pattern)) {
            return true;
        } else return false;
    }
}

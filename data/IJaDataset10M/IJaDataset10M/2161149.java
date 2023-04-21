package org.rubypeople.rdt.internal.ui.text.comment;

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextUtilities;

public class RubyCommentAutoIndentStrategy extends DefaultIndentLineAutoEditStrategy {

    private String fPartitioning;

    public RubyCommentAutoIndentStrategy(String partitioning) {
        fPartitioning = partitioning;
    }

    public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
        if (command.text != null) {
            if (command.length == 0) {
                String[] lineDelimiters = document.getLegalLineDelimiters();
                int index = TextUtilities.endsWith(lineDelimiters, command.text);
                if (index > -1) {
                    if (lineDelimiters[index].equals(command.text)) indentAfterNewLine(document, command);
                    return;
                }
            }
        }
    }

    /**
	 * Copies the indentation of the previous line and adds a #.
	 * 
	 * @param d
	 *            the document to work on
	 * @param c
	 *            the command to deal with
	 */
    private void indentAfterNewLine(IDocument d, DocumentCommand c) {
        int offset = c.offset;
        if (offset == -1 || d.getLength() == 0) return;
        try {
            int p = (offset == d.getLength() ? offset - 1 : offset);
            int lineNumber = d.getLineOfOffset(p);
            IRegion line = d.getLineInformation(lineNumber);
            if (lineNumber != 0) {
                String nextLine = getLine(d, lineNumber + 1);
                if (!(isComment(nextLine) || isClassDefinition(nextLine) || isMethodDeclaration(nextLine) || isAttributeCall(nextLine) || isAliasCall(nextLine) || isModuleDeclaration(nextLine) || isConstantAssignment(nextLine))) {
                    String previousLine = getLine(d, lineNumber - 1);
                    if (!isComment(previousLine)) return;
                }
            }
            int lineOffset = line.getOffset();
            int firstNonWS = findEndOfWhiteSpace(d, lineOffset, offset);
            Assert.isTrue(firstNonWS >= lineOffset, "indentation must not be negative");
            StringBuffer buf = new StringBuffer(c.text);
            IRegion prefix = findPrefixRange(d, line);
            String indentation = d.get(prefix.getOffset(), prefix.getLength());
            int lengthToAdd = Math.min(offset - prefix.getOffset(), prefix.getLength());
            buf.append(indentation.substring(0, lengthToAdd));
            if (lengthToAdd < prefix.getLength()) c.caretOffset = offset + prefix.getLength() - lengthToAdd;
            c.text = buf.toString();
        } catch (BadLocationException excp) {
        }
    }

    private boolean isComment(String nextLineText) {
        return nextLineText.matches("^\\s*#.*");
    }

    private boolean isClassDefinition(String nextLineText) {
        return nextLineText.matches("^\\s*class\\s+.+\\s*");
    }

    private boolean isAliasCall(String nextLineText) {
        return nextLineText.matches("^\\s*alias\\s+.+\\s*");
    }

    private boolean isModuleDeclaration(String nextLineText) {
        return nextLineText.matches("^\\s*module\\s+.+\\s*");
    }

    private boolean isMethodDeclaration(String nextLineText) {
        return nextLineText.matches("^\\s*def\\s+.+\\s*");
    }

    private boolean isAttributeCall(String nextLineText) {
        return nextLineText.matches("^\\s*attr.+\\s*");
    }

    private boolean isConstantAssignment(String nextLineText) {
        return nextLineText.matches("^\\s*[A-Z_]+\\s?=\\s+.+\\s*");
    }

    private String getLine(IDocument d, int lineNum) throws BadLocationException {
        IRegion nextLineRegion = d.getLineInformation(lineNum + 1);
        return d.get(nextLineRegion.getOffset(), nextLineRegion.getLength());
    }

    /**
	 * Returns the range of the comment prefix on the given line in
	 * <code>document</code>. The prefix greedily matches the following regex
	 * pattern: <code>\w*#\w*</code>, that is, any number of whitespace
	 * characters, followed by an pound symbol ('#'), followed by any number of
	 * whitespace characters.
	 * 
	 * @param document
	 *            the document to which <code>line</code> refers
	 * @param line
	 *            the line from which to extract the prefix range
	 * @return an <code>IRegion</code> describing the range of the prefix on
	 *         the given line
	 * @throws BadLocationException
	 *             if accessing the document fails
	 */
    private IRegion findPrefixRange(IDocument document, IRegion line) throws BadLocationException {
        int lineOffset = line.getOffset();
        int lineEnd = lineOffset + line.getLength();
        int indentEnd = findEndOfWhiteSpace(document, lineOffset, lineEnd);
        if (indentEnd < lineEnd && document.getChar(indentEnd) == '#') {
            indentEnd++;
            while (indentEnd < lineEnd && document.getChar(indentEnd) == ' ') indentEnd++;
        }
        return new Region(lineOffset, indentEnd - lineOffset);
    }
}

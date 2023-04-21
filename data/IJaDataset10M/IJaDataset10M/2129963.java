package org.eclipse.mylyn.internal.tasks.ui.editors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;

/**
 * Source from {@link org.eclipse.jface.text.hyperlink.URLHyperlinkDetector}
 * 
 * @author Rob Elves
 */
public class TaskUrlHyperlinkDetector extends AbstractHyperlinkDetector {

    public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
        if (region == null || textViewer == null) return null;
        IDocument document = textViewer.getDocument();
        int offset = region.getOffset();
        String urlString = null;
        if (document == null) return null;
        IRegion lineInfo;
        String line;
        try {
            lineInfo = document.getLineInformationOfOffset(offset);
            line = document.get(lineInfo.getOffset(), lineInfo.getLength());
        } catch (BadLocationException ex) {
            return null;
        }
        int offsetInLine = offset - lineInfo.getOffset();
        boolean startDoubleQuote = false;
        int urlOffsetInLine = 0;
        int urlLength = 0;
        int urlSeparatorOffset = line.indexOf("://");
        while (urlSeparatorOffset >= 0) {
            urlOffsetInLine = urlSeparatorOffset;
            char ch;
            do {
                urlOffsetInLine--;
                ch = ' ';
                if (urlOffsetInLine > -1) ch = line.charAt(urlOffsetInLine);
                startDoubleQuote = ch == '"';
            } while (Character.isUnicodeIdentifierStart(ch));
            urlOffsetInLine++;
            StringTokenizer tokenizer = new StringTokenizer(line.substring(urlSeparatorOffset + 3), " \t\n\r\f<>", false);
            if (!tokenizer.hasMoreTokens()) return null;
            urlLength = tokenizer.nextToken().length() + 3 + urlSeparatorOffset - urlOffsetInLine;
            if (offsetInLine >= urlOffsetInLine && offsetInLine <= urlOffsetInLine + urlLength) break;
            urlSeparatorOffset = line.indexOf("://", urlSeparatorOffset + 1);
        }
        if (urlSeparatorOffset < 0) return null;
        if (startDoubleQuote) {
            int endOffset = -1;
            int nextDoubleQuote = line.indexOf('"', urlOffsetInLine);
            int nextWhitespace = line.indexOf(' ', urlOffsetInLine);
            if (nextDoubleQuote != -1 && nextWhitespace != -1) endOffset = Math.min(nextDoubleQuote, nextWhitespace); else if (nextDoubleQuote != -1) endOffset = nextDoubleQuote; else if (nextWhitespace != -1) endOffset = nextWhitespace;
            if (endOffset != -1) urlLength = endOffset - urlOffsetInLine;
        }
        try {
            urlString = line.substring(urlOffsetInLine, urlOffsetInLine + urlLength);
            new URL(urlString);
        } catch (MalformedURLException ex) {
            urlString = null;
            return null;
        }
        IRegion urlRegion = new Region(lineInfo.getOffset() + urlOffsetInLine, urlLength);
        return new IHyperlink[] { new TaskUrlHyperlink(urlRegion, urlString) };
    }
}

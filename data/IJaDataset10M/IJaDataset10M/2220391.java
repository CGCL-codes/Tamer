package code2html;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.text.Segment;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.gjt.sp.jedit.syntax.Token;
import org.gjt.sp.jedit.textarea.Selection;
import org.gjt.sp.util.Log;
import code2html.html.HtmlCssStyle;
import code2html.html.HtmlDocument;
import code2html.html.HtmlGutter;
import code2html.html.HtmlPainter;
import code2html.html.HtmlStyle;

public class Code2HTML {

    private Buffer buffer = null;

    private Selection[] selection = null;

    private HtmlStyle style = null;

    private HtmlGutter gutter = null;

    private HtmlPainter painter = null;

    private HtmlDocument document = null;

    public Code2HTML(Buffer buffer, SyntaxStyle[] syntaxStyle, Selection[] selection) {
        this.buffer = buffer;
        this.selection = selection;
        Config config = new JEditConfig(syntaxStyle, buffer.getTabSize());
        this.style = config.getStyle();
        this.gutter = config.getGutter();
        this.painter = config.getPainter();
        this.document = new HtmlDocument(jEdit.getProperty("view.bgColor", "#ffffff"), jEdit.getProperty("view.fgColor", "#000000"), syntaxStyle, this.style, this.gutter, buffer.getName(), "\n");
    }

    public Buffer getHtmlBuffer() {
        String htmlString = this.getHtmlString();
        if (htmlString == null) {
            return null;
        }
        Buffer newBuffer = jEdit.newFile(null);
        newBuffer.insert(0, htmlString);
        return newBuffer;
    }

    public String getHtmlString() {
        int physicalFirst = 0;
        int physicalLast = this.buffer.getLineCount() - 1;
        StringWriter sw = new StringWriter();
        try {
            BufferedWriter out = new BufferedWriter(sw);
            this.document.htmlOpen(out);
            if (this.selection == null) {
                this.htmlText(out, physicalFirst, physicalLast);
            } else {
                int last = 0;
                for (int i = 0; i < selection.length; i++) {
                    if (selection[i].getEndLine() > last) {
                        last = selection[i].getEndLine();
                    }
                }
                Arrays.sort(selection, new SelectionStartLineComparator());
                if (this.gutter != null) {
                    this.gutter.setGutterSize(Integer.toString(last + 1).length());
                }
                int lastLine = -1;
                for (int i = 0; i < selection.length; i++) {
                    physicalFirst = selection[i].getStartLine();
                    physicalLast = selection[i].getEndLine();
                    if (physicalLast <= lastLine) {
                        continue;
                    }
                    this.htmlText(out, Math.max(physicalFirst, lastLine + 1), physicalLast);
                    lastLine = physicalLast;
                }
            }
            this.document.htmlClose(out);
            out.flush();
            out.close();
        } catch (IOException ioe) {
            Log.log(Log.ERROR, this, ioe);
            return null;
        }
        return sw.toString();
    }

    private void htmlText(Writer out, int first, int last) throws IOException {
        long start = System.currentTimeMillis();
        this.paintLines(out, this.buffer, first, last);
        long end = System.currentTimeMillis();
        Log.log(Log.DEBUG, this, "Time: " + (end - start) + " ms");
    }

    private void paintLines(Writer out, Buffer buffer, int first, int last) throws IOException {
        Segment line = new Segment();
        Token tokens = null;
        for (int i = first; i <= last; i++) {
            buffer.getLineText(i, line);
            tokens = buffer.markTokens(i).getFirstToken();
            this.painter.setPos(0);
            if (tokens == null) {
                this.painter.paintPlainLine(out, i + 1, line, null);
            } else {
                SyntaxToken syntaxTokens = SyntaxTokenUtilities.convertTokens(tokens);
                this.painter.paintSyntaxLine(out, i + 1, line, syntaxTokens);
            }
            out.write("\n");
        }
    }

    private class SelectionStartLineComparator implements Comparator {

        public int compare(Object obj1, Object obj2) {
            Selection s1 = (Selection) obj1;
            Selection s2 = (Selection) obj2;
            int diff = s1.getStartLine() - s2.getStartLine();
            if (diff == 0) {
                return 0;
            } else if (diff > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}

package net.sf.xpontus.plugins.lexer.html;

import net.sf.xpontus.syntax.*;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Simple HtmlVisitor which dumps out the document to the specified
 * output stream.
 *
 * @author Brian Goetz, Quiotix
 */
public class HtmlDebugDumper extends HtmlVisitor {

    protected PrintWriter out;

    public HtmlDebugDumper(OutputStream os) {
        out = new PrintWriter(os);
    }

    public void finish() {
        out.flush();
    }

    public void visit(HtmlDocument.Tag t) {
        out.print("Tag(" + t + ")");
    }

    public void visit(HtmlDocument.EndTag t) {
        out.print("Tag(" + t + ")");
    }

    public void visit(HtmlDocument.Comment c) {
        out.print("Comment(" + c + ")");
    }

    public void visit(HtmlDocument.Text t) {
        out.print(t);
    }

    public void visit(HtmlDocument.Newline n) {
        out.println("-NL-");
    }

    public void visit(HtmlDocument.Annotation a) {
        out.print(a);
    }

    public void visit(HtmlDocument.TagBlock bl) {
        out.print("<BLOCK>");
        visit(bl.startTag);
        visit(bl.body);
        visit(bl.endTag);
        out.print("</BLOCK>");
    }

    public static void main(String[] args) throws ParseException {
        HtmlParser parser = new HtmlParser(new LexerInputStream(new InputStreamReader(System.in)));
        HtmlDocument doc = parser.HtmlDocument();
        doc.accept(new HtmlDebugDumper(System.out));
    }
}

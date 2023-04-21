package jvc.web.action.lucene;

import java.io.*;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.visitors.HtmlPage;

/** 
 * Simple HtmlVisitor which dumps out the document to the specified 
 * output stream. 
 *
 * @author Brian Goetz, Quiotix
 */
public class HtmlReader {

    private String body = "";

    private String Title = "";

    private String summary = "";

    public HtmlReader() {
    }

    public HtmlReader(String FileName) {
        InputStream r = null;
        try {
            r = new FileInputStream(FileName);
            init(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String combineNodeText(Node[] nodes) {
        if (nodes == null) return "";
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < nodes.length; i++) {
            Node anode = (Node) nodes[i];
            String str = anode.toPlainTextString();
            result.append(str.trim().replaceAll("&nbsp;", " ").replace((char) 13, ' ').replace((char) 10, ' '));
        }
        return result.toString();
    }

    private void init(InputStream r) {
        try {
            byte[] data = new byte[r.available()];
            r.read(data);
            String str = new String(data);
            int iStart = str.indexOf("<title>");
            int iEnd = str.indexOf("</title>");
            if (iStart >= 0 && iEnd > 0) {
                this.Title = str.substring(iStart + 7, iEnd);
                this.body = str.substring(iEnd + 8);
            } else this.body = str;
            Parser parser = Parser.createParser("<body>" + body + "</body>", "GBK");
            HtmlPage hp = new HtmlPage(parser);
            parser.visitAllNodesWith(hp);
            if (hp != null) if (hp.getBody() != null) this.summary = combineNodeText(hp.getBody().toNodeArray());
            if (this.summary.length() > 200) this.summary = this.summary.substring(0, 200);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (r != null) r.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ;
    }

    public HtmlReader(InputStream r) {
        init(r);
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return this.Title;
    }

    public String getSummary() {
        return this.summary;
    }

    public static void main(String[] args) throws Exception {
        HtmlReader hb = new HtmlReader("D:\\eclipse\\workspace\\itil\\kmsdata\\icontrol\\40288aac0bc79c13010bc7a36cca0139.htm");
        System.out.println(hb.getSummary());
    }
}

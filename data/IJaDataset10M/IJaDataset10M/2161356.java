package cn.edu.dutir.corpus.ntcir;

import java.io.StringReader;
import org.dutir.util.dom.DomNodeUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import cn.edu.dutir.corpus.TopicHandler;
import cn.edu.dutir.parser.TextParser;

public class NtcirDocParser<H extends TopicHandler> extends TextParser<H> {

    private static final String DOCNO = "DOCNO";

    private static final String LANG = "LANG";

    private static final String HEADLINE = "HEADLINE";

    private static final String DATE = "DATE";

    private static final String TEXT = "TEXT";

    private StringBuilder docnoBuf = new StringBuilder();

    private StringBuilder hlBuf = new StringBuilder();

    private StringBuilder textBuf = new StringBuilder();

    private StringBuilder langBuf = new StringBuilder();

    private StringBuilder dateBuf = new StringBuilder();

    public NtcirDocParser(String mark) {
        this(null, mark);
    }

    public NtcirDocParser(H handler, String mark) {
        super(mark);
        setHandler(handler);
    }

    @Override
    public void parseText(String textObj) {
        Node node = DomNodeUtils.parserFragment(new StringReader(textObj));
        clear();
        getText(node, docnoBuf, hlBuf, textBuf, langBuf, dateBuf);
        NTCIRDoc doc = new NTCIRDoc(docnoBuf.toString(), langBuf.toString(), hlBuf.toString(), textBuf.toString(), dateBuf.toString());
        getHandler().handle(doc);
    }

    private void getText(Node node, StringBuilder docnoBuf, StringBuilder hlBuf, StringBuilder textBuf, StringBuilder langBuf, StringBuilder dateBuf) {
        String name = node.getNodeName();
        if (name.equalsIgnoreCase(DOCNO)) {
            docnoBuf.append(DomNodeUtils.getText(node).trim());
        }
        if (name.equalsIgnoreCase(HEADLINE)) {
            hlBuf.append(DomNodeUtils.getText(node).trim());
            return;
        }
        if (name.equalsIgnoreCase(LANG)) {
            langBuf.append(DomNodeUtils.getText(node).trim());
            return;
        }
        if (name.equalsIgnoreCase(DATE)) {
            dateBuf.append(DomNodeUtils.getText(node).trim());
            return;
        }
        if (name.equalsIgnoreCase(TEXT)) {
            textBuf.append(DomNodeUtils.getText(node).trim());
            return;
        }
        NodeList children = node.getChildNodes();
        if (children != null) {
            int len = children.getLength();
            for (int i = 0; i < len; i++) {
                Node nd = children.item(i);
                getText(nd, docnoBuf, hlBuf, textBuf, langBuf, dateBuf);
            }
        }
    }

    public void clear() {
        docnoBuf.setLength(0);
        hlBuf.setLength(0);
        langBuf.setLength(0);
        dateBuf.setLength(0);
        textBuf.setLength(0);
    }

    @Override
    public void close() {
        getHandler().close();
    }
}

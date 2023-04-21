package cn.edu.dutir.corpus.trec;

import java.io.StringReader;
import org.dutir.util.dom.DomNodeUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import cn.edu.dutir.corpus.TopicHandler;
import cn.edu.dutir.parser.TextParser;

public class TrecDocParser<H extends TopicHandler> extends TextParser<H> {

    public static final String DOCNO = "DOCNO";

    public static final String HEADLINE = "TITLE";

    public static final String TEXT = "TEXT";

    public TrecDocParser(String mark) {
        this(null, mark);
    }

    public TrecDocParser(H handler, String mark) {
        super(mark);
        setHandler(handler);
    }

    @Override
    public void parseText(String textObj) {
        StringBuilder docnoBuf = new StringBuilder();
        StringBuilder hlBuf = new StringBuilder();
        StringBuilder textBuf = new StringBuilder();
        Node node = DomNodeUtils.parserFragment(new StringReader(textObj));
        getText(node, docnoBuf, hlBuf, textBuf);
        TrecDoc trecDoc = new TrecDoc(docnoBuf.toString(), hlBuf.toString(), textBuf.toString());
        getHandler().handle(trecDoc);
    }

    private void getText(Node node, StringBuilder docnoBuf, StringBuilder hlBuf, StringBuilder textBuf) {
        String name = node.getNodeName();
        if (name.equalsIgnoreCase(DOCNO)) {
            docnoBuf.append(DomNodeUtils.getText(node).trim());
        }
        if (name.equalsIgnoreCase(HEADLINE)) {
            hlBuf.append(DomNodeUtils.getText(node).trim());
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
                getText(nd, docnoBuf, hlBuf, textBuf);
            }
        }
    }

    @Override
    public void close() {
        getHandler().close();
    }
}

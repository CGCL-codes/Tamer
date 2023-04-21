package com.jaeksoft.searchlib.analysis;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import org.apache.lucene.analysis.TokenStream;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import com.jaeksoft.searchlib.util.XPathParser;
import com.jaeksoft.searchlib.util.XmlWriter;

public class SynonymFilter extends FilterFactory {

    private SynonymMap synonymMap = null;

    private String filePath = null;

    private static TreeMap<File, SynonymMap> synonymMaps = new TreeMap<File, SynonymMap>();

    @Override
    public void setParams(XPathParser xpp, Node node) throws IOException {
        filePath = XPathParser.getAttributeString(node, "file");
        File file = new File(xpp.getCurrentFile().getParentFile(), filePath);
        synchronized (synonymMaps) {
            synonymMap = synonymMaps.get(file);
            if (synonymMap == null) {
                synonymMap = new SynonymMap(file);
                synonymMaps.put(file, synonymMap);
            }
        }
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new SynonymTokenFilter(tokenStream, synonymMap);
    }

    @Override
    public String getDescription() {
        return "Add synonyms support. File: " + filePath + ". " + synonymMap.getSize() + " synonym(s).";
    }

    @Override
    public void writeXmlConfig(XmlWriter writer) throws SAXException {
        writer.startElement("filter", "class", getClassName(), "file", filePath);
        writer.endElement();
    }
}

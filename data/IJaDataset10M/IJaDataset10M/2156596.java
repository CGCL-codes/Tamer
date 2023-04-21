package edu.pku.sei.pgie.persistence.xml;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import edu.pku.sei.pgie.analyzer.core.SymbolTable;
import edu.pku.sei.pgie.analyzer.internal.Tokenizer;
import edu.pku.sei.pgie.persistence.xml.template.XMLRelationSerializer;
import edu.pku.sei.pgie.persistence.xml.template.XMLTokenSerializer;

/**
 * @author HeLi
 */
public class XMLWriter {

    String path;

    PrintWriter writer;

    public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"GBK\"?>";

    public static final String ROOT_START_TAG = "<Artifact>";

    public static final String ROOT_END_TAG = "</Artifact>";

    public XMLWriter(String path) throws FileNotFoundException {
        this.path = path;
        writer = new PrintWriter(path);
        writer.println(XML_HEAD);
        writer.println(ROOT_START_TAG);
    }

    public void writeTokenizer(Tokenizer tokenizer, SymbolTable symbolTable) {
        XMLTokenSerializer serializer = new XMLTokenSerializer();
        String output = serializer.generate(new Object[] { tokenizer, symbolTable });
        writer.println(output);
    }

    public void writeRelation(String[] relation, SymbolTable symbolTable, String type) {
        XMLRelationSerializer serializer = new XMLRelationSerializer();
        writer.println(serializer.generate(new Object[] { relation, symbolTable, type }));
    }

    public void commit() {
        writer.println(ROOT_END_TAG);
        writer.flush();
        writer.close();
    }
}

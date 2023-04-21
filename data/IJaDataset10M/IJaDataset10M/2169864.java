package serene.validation.schema.parsed;

import java.util.Map;
import org.xml.sax.SAXException;
import serene.bind.util.DocumentIndexedData;

public class AttributeWithNameInstance extends Attribute {

    int nameRecordIndex;

    AttributeWithNameInstance(int xmlBase, int ns, int datatypeLibrary, int name, int defaultValue, ParsedComponent[] children, int recordIndex, DocumentIndexedData documentIndexedData) {
        super(xmlBase, ns, datatypeLibrary, defaultValue, children, recordIndex, documentIndexedData);
        this.nameRecordIndex = name;
    }

    public void accept(ParsedComponentVisitor v) {
        v.visit(this);
    }

    public void accept(SimplifyingVisitor v) throws SAXException {
        v.visit(this);
    }

    public String getName() {
        if (nameRecordIndex == DocumentIndexedData.NO_RECORD) return null;
        return documentIndexedData.getStringValue(nameRecordIndex);
    }

    public int getNameRecordIndex() {
        return nameRecordIndex;
    }

    public String toString() {
        String s = "AttributeWithNameInstance " + getName();
        return s;
    }
}

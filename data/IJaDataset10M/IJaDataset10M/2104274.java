package serene.validation.schema.parsed;

import java.util.Map;
import org.xml.sax.SAXException;
import serene.bind.util.DocumentIndexedData;

public class Param extends ParsedComponent {

    int nameRecordIndex;

    String characterContent;

    Param(int xmlBase, int ns, int datatypeLibrary, int name, String characterContent, int recordIndex, DocumentIndexedData documentIndexedData) {
        super(xmlBase, ns, datatypeLibrary, recordIndex, documentIndexedData);
        this.characterContent = characterContent;
        this.nameRecordIndex = name;
    }

    public void accept(ParsedComponentVisitor v) {
        v.visit(this);
    }

    public void accept(SimplifyingVisitor v) throws SAXException {
        v.visit(this);
    }

    public int getNameRecordIndex() {
        return nameRecordIndex;
    }

    public String getName() {
        if (nameRecordIndex == DocumentIndexedData.NO_RECORD) return null;
        return documentIndexedData.getStringValue(nameRecordIndex);
    }

    public String getCharacterContent() {
        return characterContent;
    }

    public String toString() {
        String s = "Param name " + getName() + ", charContent " + getCharacterContent();
        return s;
    }
}

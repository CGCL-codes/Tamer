package serene.validation.schema.simplified.components;

import org.xml.sax.SAXException;
import org.relaxng.datatype.Datatype;
import serene.validation.schema.simplified.RestrictingVisitor;
import serene.validation.schema.simplified.SimplifiedComponentVisitor;
import serene.bind.util.DocumentIndexedData;

public class SValue extends AbstractNoChildrenPattern {

    String ns;

    Datatype datatype;

    String charContent;

    public SValue(String ns, Datatype datatype, String charContent, int recordIndex, DocumentIndexedData documentIndexedData) {
        super(recordIndex, documentIndexedData);
        this.ns = ns;
        this.datatype = datatype;
        this.charContent = charContent;
    }

    public String getNamespaceURI() {
        return ns;
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public String getCharContent() {
        return charContent;
    }

    public void accept(SimplifiedComponentVisitor v) {
        v.visit(this);
    }

    public void accept(RestrictingVisitor v) throws SAXException {
        v.visit(this);
    }

    public String toString() {
        return "SValue datatype " + datatype + " charContent " + charContent;
    }
}

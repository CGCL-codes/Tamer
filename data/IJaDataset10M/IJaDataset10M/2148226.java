package serene.validation.schema.parsed;

import java.util.Map;
import serene.bind.util.DocumentIndexedData;

abstract class NoChildrenPattern extends Pattern {

    NoChildrenPattern(int xmlBase, int ns, int datatypeLibrary, int recordIndex, DocumentIndexedData documentIndexedData) {
        super(xmlBase, ns, datatypeLibrary, recordIndex, documentIndexedData);
    }

    public String toString() {
        String s = "NoChildrenPattern";
        return s;
    }
}

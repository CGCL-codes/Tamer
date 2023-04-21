package self.amigo.translator;

import org.xml.sax.Attributes;
import self.amigo.io.DiagramWriter;

public abstract class ABasicAmsTranslator extends ABasicSaxBasedTranslator {

    protected abstract void checkType(String type);

    protected void checkDocument(String name, Attributes attrs) {
        if (!name.equals(DiagramWriter.ELEMENT_NAME)) throw new IllegalArgumentException("err.aid.translator.ams_doc_expected");
        int max = attrs.getLength();
        for (int cntr = 0; cntr < max; cntr++) {
            String attrAsString = attrs.getLocalName(cntr);
            if (attrAsString.equals(DiagramWriter.ELEMENT_TYPE)) {
                String typeAsString = attrs.getValue(cntr);
                checkType(typeAsString);
            }
        }
    }
}

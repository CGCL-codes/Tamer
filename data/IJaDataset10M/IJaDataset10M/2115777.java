package org.fao.waicent.attributes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.fao.waicent.util.Translate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SampleDefinition extends Definition {

    public SampleDefinition() {
        super("");
    }

    public SampleDefinition(String value) {
        super(value);
    }

    public SampleDefinition(String code, String value) {
        super(value);
        this.code = code;
    }

    public SampleDefinition(Document doc, Element element, ExtentManager extents) throws IOException {
        super(doc, element, extents);
    }

    public String getSample() {
        return code;
    }

    public void setSample(String value) {
        this.code = value;
    }

    public void save(DataOutputStream out) throws IOException {
        super.save(out);
    }

    public SampleDefinition(Element element) {
        super(element.getAttribute("code"));
        defaultCode = code;
        codes = new Translate(defaultCode, "code", element);
    }

    public SampleDefinition(DataInputStream in, Document doc, Element element) throws IOException {
        super(in);
        element.setAttribute("code", getCode());
    }

    public SampleDefinition(DataInputStream in) throws IOException {
        this(in, new Integer(-1));
    }

    public SampleDefinition(DataInputStream in, Integer version) throws IOException {
        super(in);
    }

    public void toXML(Document doc, Element element) {
        if (defaultCode == null) {
            defaultCode = code;
        }
        element.setAttribute("code", defaultCode);
        if (codes != null) {
            codes.toXML(element);
        }
    }

    String defaultCode = null;

    Translate codes = null;

    public void changeLanguage(String language) {
        if (codes != null) {
            code = codes.getLabel(language);
        }
    }
}

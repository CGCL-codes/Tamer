package com.pbonhomme.xf.xml.transformer;

import java.io.File;
import java.util.Map;
import javax.xml.transform.TransformerFactory;

public class ReloadableXMLTransformer extends DefaultXMLTransformer {

    private TransformerFactory transformerFactory;

    public ReloadableXMLTransformer() {
        super();
    }

    public void load(TransformerFactory transformerFactory) throws TransformerException {
        this.transformerFactory = transformerFactory;
        super.load(transformerFactory);
    }

    public void transform(String inFilename, String outFilename, Map<String, Object> parameters) throws TransformerException {
        File _xslFile = new File(xslFilepath);
        if (!_xslFile.exists()) throw new TransformerException("ReloadableXMLTransformer error: no XSL source [" + _xslFile + "] for Transformer [" + getName() + "].");
        long _now = _xslFile.lastModified();
        if (_now > lastModified) {
            load(xslFilepath, transformerFactory);
        }
        super.transform(inFilename, outFilename, parameters);
    }
}

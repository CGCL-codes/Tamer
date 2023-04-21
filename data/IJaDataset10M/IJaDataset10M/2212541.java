package com.google.gdata.wireformats.output;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.introspection.IServiceDocument;
import com.google.gdata.data.introspection.ServiceDocument;
import com.google.gdata.wireformats.AltFormat;
import java.io.IOException;

/**
 * Generates the metadata for an Atom Service (introspection) document.
 *
 * 
 */
public class AtomServiceGenerator extends XmlGenerator<IServiceDocument> {

    public AltFormat getAltFormat() {
        return AltFormat.ATOM_SERVICE;
    }

    public Class<IServiceDocument> getSourceType() {
        return IServiceDocument.class;
    }

    /**
   * Writes the Service document for the target feed.
   */
    @Override
    public void generateXml(XmlWriter xw, OutputProperties outProps, IServiceDocument source) throws IOException {
        if (source instanceof ServiceDocument) {
            ((ServiceDocument) source).generate(xw, outProps.getExtensionProfile());
        } else if (source != null) {
            throw new IllegalStateException("Unexpected source type: " + source.getClass());
        }
    }
}

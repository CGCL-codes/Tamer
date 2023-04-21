package org.in4ama.documentengine.converter.pack;

import org.in4ama.documentengine.converter.ConverterRepository;
import org.in4ama.documentengine.output.PngOutputDocument;

/** Converts packs */
public class Pack2PngConverter extends PackConverter {

    /** Creates a new instance of the Pack2PngConverter */
    public Pack2PngConverter(ConverterRepository converterRepository) {
        super(converterRepository);
    }

    /** Returns this output type of this converter */
    @Override
    public String getOutputType() {
        return PngOutputDocument.TYPE;
    }
}

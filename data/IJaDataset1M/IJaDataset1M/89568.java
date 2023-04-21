package de.offis.semanticmm4u.generators.smil;

import de.offis.semanticmm4u.generators.AbstractContentGenerator;
import de.offis.semanticmm4u.generators.AbstractLayoutGenerator;
import de.offis.semanticmm4u.generators.AbstractMetadataCollector;
import de.offis.semanticmm4u.generators.GeneratorToolkit;
import de.offis.semanticmm4u.generators.RDFMetadataCollector;

public class SMIL2_0GeneratorFactory extends GeneratorToolkit {

    @Override
    public AbstractContentGenerator createContentGenerator() {
        return new SMILContentGenerator(".2_0.smil");
    }

    @Override
    public AbstractLayoutGenerator createLayoutGenerator() {
        return new SMILLayoutGenerator();
    }

    @Override
    public AbstractMetadataCollector createMetadataCollector() {
        return new RDFMetadataCollector();
    }
}

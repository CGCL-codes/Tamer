package de.offis.semanticmm4u.generators.svg;

import de.offis.semanticmm4u.generators.AbstractContentGenerator;
import de.offis.semanticmm4u.generators.AbstractLayoutGenerator;
import de.offis.semanticmm4u.generators.AbstractMetadataCollector;
import de.offis.semanticmm4u.generators.GeneratorToolkit;
import de.offis.semanticmm4u.generators.RDFMetadataCollector;

public class SVGBasicGeneratorFactory extends GeneratorToolkit {

    @Override
    public AbstractContentGenerator createContentGenerator() {
        return new SVGContentGenerator();
    }

    @Override
    public AbstractLayoutGenerator createLayoutGenerator() {
        return null;
    }

    @Override
    public AbstractMetadataCollector createMetadataCollector() {
        return new RDFMetadataCollector();
    }
}

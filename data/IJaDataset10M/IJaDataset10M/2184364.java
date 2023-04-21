package de.tum.in.botl.adapter.impl;

import de.tum.in.botl.adapter.AdapterFactory;
import de.tum.in.botl.adapter.FileExporter;
import de.tum.in.botl.adapter.FileImporter;
import de.tum.in.botl.adapter.JavaExporter;
import de.tum.in.botl.metamodel.Metamodel;
import de.tum.in.botl.model.ModelFactory;
import de.tum.in.botl.util.Config;
import de.tum.in.botl.util.stringHelper.StringHelper;

public class AdapterFactoryImpl implements AdapterFactory {

    private Config config;

    private StringHelper stringHelper;

    private ModelFactory modelFactory;

    public AdapterFactoryImpl() {
        config = Config.getInstance();
        modelFactory = config.getDefaultModelFactory();
        stringHelper = config.getStringHelper();
    }

    public FileExporter getArgoUmlExporter() {
        return new ArgoUmlExporter();
    }

    public FileImporter getArgoUmlImporter(Metamodel metamodel) {
        return new ArgoUmlImporter(metamodel, modelFactory);
    }

    public FileExporter getGenericXMLExporter() {
        return new GenericXMLExporter();
    }

    public FileImporter getGenericXMLImporter(Metamodel metamodel) {
        return new GenericXMLImporter(metamodel, modelFactory);
    }

    public JavaExporter getJavaExporter() {
        return new JavaExporterImpl(stringHelper);
    }

    public JavaExporter getJavaExporter(int verbosity) {
        return new JavaExporterImpl(stringHelper, verbosity);
    }

    public JavaImporterImpl getJavaImporter(Metamodel metamodel, int verbosity) throws ImportException {
        return new JavaImporterImpl(metamodel, modelFactory, verbosity);
    }

    public JavaImporterImpl getJavaImporter(Metamodel metamodel) throws ImportException {
        return new JavaImporterImpl(metamodel, modelFactory);
    }

    public FileExporter getPoseidon16Exporter() {
        return new Poseidon16Exporter(modelFactory);
    }

    public FileExporter getPoseidon16ExporterUuid() {
        return new Poseidon16ExporterUuid(modelFactory);
    }

    public FileImporter getPoseidon16Importer(Metamodel metamodel) {
        return new Poseidon16Importer(metamodel, modelFactory);
    }

    public FileImporter getPoseidon16ImporterUuid(Metamodel metamodel) {
        return new Poseidon16ImporterUuid(metamodel, modelFactory);
    }

    public FileImporter getPoseidon16ImporterOptimized(Metamodel metamodel) {
        return new Poseidon16ImporterOptimized(metamodel, modelFactory);
    }
}

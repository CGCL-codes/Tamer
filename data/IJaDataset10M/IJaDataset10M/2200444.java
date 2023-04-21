package edu.asu.vogon.export.embryo.extension;

import edu.asu.vogon.export.embryo.Activator;
import edu.asu.vogon.export.embryo.Constants;
import edu.asu.vogon.export.extension.IExportEngine;
import edu.asu.vogon.export.extension.ITextExporter;
import edu.asu.vogon.export.extension.ITextTransformater;
import edu.asu.vogon.util.properties.PropertyHandler;
import edu.asu.vogon.util.properties.PropertyHandlerRegistry;

public class EmbryoExportEngine implements IExportEngine {

    @Override
    public ITextExporter getTextExporter() {
        return null;
    }

    @Override
    public ITextTransformater getTextTransformator() {
        return null;
    }

    @Override
    public String getTypeId() {
        return "XHTML";
    }

    @Override
    public String getTypeText() {
        PropertyHandler handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILE);
        return handler.getProperty("_export_type");
    }
}

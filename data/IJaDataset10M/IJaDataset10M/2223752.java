package de.sonivis.tool.ontology.view.tables;

/**
 * This {@link TableExporter} can export the content of a metrics table into a LaTeX file.
 * 
 * @author Sebastian
 */
public class CSVTableExporter extends TemplateTableExporter {

    @Override
    public String getName() {
        return "CSV";
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }
}

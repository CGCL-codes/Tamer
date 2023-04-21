package org.objectwiz.uibuilder.model.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.objectwiz.model.impl.SpreadSheetExport;
import org.objectwiz.process.io.ExportProcessDescriptor;
import org.objectwiz.uibuilder.EvaluationContext;
import org.objectwiz.uibuilder.model.value.Value;

/**
 * Describes how to start a complex export.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
@Table(name = "process_descriptor_spreadsheet_export")
public class ComplexSpreadsheetExportDescriptor extends ProcessDescriptor {

    private SpreadSheetExport export;

    private Map<String, Value> bookmarkParameters;

    @ManyToOne(optional = false)
    public SpreadSheetExport getExport() {
        return export;
    }

    public void setExport(SpreadSheetExport export) {
        this.export = export;
    }

    @ManyToMany
    public Map<String, Value> getBookmarkParameters() {
        return bookmarkParameters;
    }

    public void setBookmarkParameters(Map<String, Value> bookmarkParameters) {
        this.bookmarkParameters = bookmarkParameters;
    }

    @Override
    public org.objectwiz.process.ProcessDescriptor createDescriptor(EvaluationContext ctx) {
        try {
            List<Map> parametersMap = new ArrayList();
            parametersMap.add(ctx.resolveValues(bookmarkParameters));
            return new ExportProcessDescriptor(export, parametersMap.toArray(new Map[0]));
        } catch (Exception e) {
            throw new RuntimeException("Error while creating descriptor", e);
        }
    }
}

package org.objectwiz.plugin.async.iop.dataset;

import java.util.Locale;
import org.objectwiz.core.Application;
import org.objectwiz.core.dataset.DataSet;
import org.objectwiz.core.representation.ObjectProxy;
import org.objectwiz.core.ui.rendering.table.ColumnInformation;
import org.objectwiz.plugin.async.AsynchroneousProcess;
import org.objectwiz.plugin.async.ProcessDescriptor;

/**
 * Descriptor for a process that exports data based on a {@link DataSet} to
 * various destination formats.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class DataSetExportProcessDescriptor implements ProcessDescriptor<DataSetExportProcessDescriptor> {

    private DataSet ds;

    private String destinationFormat;

    private ColumnInformation[] columnsLayout;

    private Locale locale;

    public DataSetExportProcessDescriptor(DataSet ds, String destinationFormat, ColumnInformation[] columnsLayout, Locale locale) {
        this.ds = ds;
        this.destinationFormat = destinationFormat;
        this.columnsLayout = columnsLayout;
        this.locale = locale;
    }

    public DataSet getDataSet() {
        return this.ds;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getDestinationFormat() {
        return destinationFormat;
    }

    public ColumnInformation[] getColumnsLayout() {
        return columnsLayout;
    }

    @Override
    public AsynchroneousProcess createProcess(Application application) {
        AsynchroneousProcess process = DataSetExportFormatFactory.createExportProcess(application, this);
        if (process == null) throw new RuntimeException("Format not supported: " + destinationFormat);
        return process;
    }

    @Override
    public DataSetExportProcessDescriptor toPojoRepresentation(Application app, ObjectProxy inProxy) {
        return new DataSetExportProcessDescriptor(ds.createRepresentationWrapper(app.getRepresentation()), destinationFormat, columnsLayout, locale);
    }
}

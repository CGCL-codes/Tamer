package com.scythebill.birdlist.ui.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.AbstractAction;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.scythebill.birdlist.model.sighting.ReportSet;
import com.scythebill.birdlist.model.sighting.ReportSets;
import com.scythebill.birdlist.model.taxa.Taxonomy;
import com.scythebill.birdlist.model.xml.XmlReportSetExport;
import com.scythebill.birdlist.model.xml.XmlReportSetImport;
import com.scythebill.birdlist.ui.panels.FrameRegistry;
import com.scythebill.birdlist.ui.util.Alerts;
import com.scythebill.birdlist.ui.util.FileDialogs;

/**
 * Action for creating new ReportSets.
 * TODO: the proper course would be showing a save dialog only
 * after adding entries.
 */
public final class NewReportSetAction extends AbstractAction {

    private final FrameRegistry frameRegistry;

    private final Supplier<Taxonomy> taxonomySupplier;

    private final FileDialogs fileDialogs;

    @Inject
    public NewReportSetAction(FrameRegistry frameRegistry, FileDialogs fileDialogs, Supplier<Taxonomy> taxonomySupplier) {
        this.frameRegistry = frameRegistry;
        this.fileDialogs = fileDialogs;
        this.taxonomySupplier = taxonomySupplier;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        File saveFile = fileDialogs.saveFile(null, "New List", "List" + XmlReportSetImport.REPORT_SET_SUFFIX, null);
        if (saveFile != null) {
            if (!saveFile.getName().endsWith(XmlReportSetImport.REPORT_SET_SUFFIX)) {
                saveFile = new File(saveFile.getParent(), saveFile.getName() + XmlReportSetImport.REPORT_SET_SUFFIX);
                if (saveFile.exists()) {
                    Alerts.showError(null, "File already exists", "File \"%s\" already exists.  Please choose another name.", saveFile.getName());
                    return;
                }
            }
            ReportSet newReportSet = ReportSets.newReportSet();
            try {
                if (!saveFile.exists()) {
                    if (!saveFile.createNewFile()) {
                        throw new IOException();
                    }
                }
                FileOutputStream output = new FileOutputStream(saveFile);
                try {
                    Taxonomy taxonomy = Preconditions.checkNotNull(taxonomySupplier.get(), "Taxonomy should be loaded before this is available");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(output, Charsets.UTF_8));
                    XmlReportSetExport rse = new XmlReportSetExport();
                    rse.export(writer, Charsets.UTF_8.name(), newReportSet, taxonomy);
                } finally {
                    output.close();
                }
                frameRegistry.startLoadingReportSet(saveFile.getAbsolutePath());
            } catch (IOException ioe) {
                Alerts.showError(null, "Could not create file", "An error occurred. Please try again.");
            }
        }
    }
}

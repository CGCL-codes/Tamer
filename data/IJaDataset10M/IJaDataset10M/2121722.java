package de.bwb.ekp.export;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import de.bwb.ekp.commons.jasper.JasperPrintService;
import de.bwb.ekp.commons.jasper.ReportData;
import de.bwb.ekp.entities.Ausschreibung;
import de.bwb.ekp.entities.Dokument;
import de.bwb.ekp.entities.DokumentHome;
import de.bwb.ekp.entities.FileUploadException;
import de.bwb.ekp.entities.LogEintrag;
import de.bwb.ekp.interceptors.MeasureCalls;

/**
 * 
 * 
 * @author Dorian Gloski Copyright akquinet AG, 2007
 */
@MeasureCalls
@Name("reportBuilder")
public class ReportBuilder {

    @In(create = true)
    private JasperPrintService jasperPrintService;

    @Logger
    private Log log;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");

    public Dokument createMassnahmeLog(final Ausschreibung ausschreibung, final List<LogEintrag> logEintraege) {
        final ReportData reportData = new MassnahmeLogReportData(ausschreibung, logEintraege);
        return this.createDokument(reportData, this.createReportName("MassnahmeLog", ausschreibung));
    }

    private String createReportName(final String praefix, final Ausschreibung ausschreibung) {
        return praefix + "-" + ausschreibung.getMassnahmenummer() + "-" + this.dateFormat.format(new Date());
    }

    public Dokument createBewerbungsListe(final Ausschreibung ausschreibung) {
        final ReportData reportData = new BewerbungListReportData(ausschreibung);
        return this.createDokument(reportData, this.createReportName("BewerbungsListe", ausschreibung));
    }

    private Dokument createDokument(final ReportData reportData, final String filename) {
        final byte[] pdfStream = this.jasperPrintService.createPDFStream(reportData, Locale.GERMAN);
        try {
            final Dokument dokument = DokumentHome.createDokument(pdfStream, filename, filename + ".pdf");
            return dokument;
        } catch (final FileUploadException fue) {
            this.log.error(fue.getMessage(), fue);
            throw new RuntimeException();
        }
    }
}

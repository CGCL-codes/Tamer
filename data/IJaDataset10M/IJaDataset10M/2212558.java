package uk.ac.ebi.pride.tools.converter.gui.forms;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.error.ErrorLevel;
import psidev.psi.tools.validator.ValidatorMessage;
import uk.ac.ebi.pride.tools.converter.conversion.io.PrideXmlWriter;
import uk.ac.ebi.pride.tools.converter.dao.DAO;
import uk.ac.ebi.pride.tools.converter.dao.DAOFactory;
import uk.ac.ebi.pride.tools.converter.gui.NavigationPanel;
import uk.ac.ebi.pride.tools.converter.gui.component.panels.FilterPanel;
import uk.ac.ebi.pride.tools.converter.gui.model.ConverterData;
import uk.ac.ebi.pride.tools.converter.gui.model.FileBean;
import uk.ac.ebi.pride.tools.converter.gui.model.GUIException;
import uk.ac.ebi.pride.tools.converter.gui.util.IOUtilities;
import uk.ac.ebi.pride.tools.converter.gui.util.error.ErrorDialogHandler;
import uk.ac.ebi.pride.tools.converter.report.io.ReportReader;
import uk.ac.ebi.pride.tools.converter.report.io.ReportReaderDAO;
import uk.ac.ebi.pride.tools.converter.utils.FileUtils;
import uk.ac.ebi.pride.tools.converter.utils.xml.validation.ValidatorFactory;
import uk.ac.ebi.pride.tools.filter.io.PrideXmlFilter;
import uk.ac.ebi.pride.validator.PrideXmlValidator;
import javax.swing.*;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

/**
 * @author User #3
 */
public class FileExportForm extends AbstractForm {

    private static final Logger logger = Logger.getLogger(FileExportForm.class);

    private boolean filterOnly = false;

    public FileExportForm(boolean filterOnly) {
        initComponents();
        this.filterOnly = filterOnly;
    }

    private void initComponents() {
        filterPanel1 = new FilterPanel();
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup().addContainerGap().addComponent(filterPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(7, 7, 7)));
        layout.setVerticalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup().addContainerGap().addComponent(filterPanel1, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE).addContainerGap()));
    }

    private FilterPanel filterPanel1;

    @Override
    public Collection<ValidatorMessage> validateForm() {
        return Collections.emptyList();
    }

    @Override
    public void clear() {
        filterPanel1.reset();
    }

    @Override
    public void save(ReportReaderDAO dao) {
    }

    @Override
    public void load(ReportReaderDAO dao) {
    }

    @Override
    public String getFormName() {
        return "File Export";
    }

    @Override
    public String getFormDescription() {
        return config.getString("fileexport.form.description");
    }

    @Override
    public Icon getFormIcon() {
        return getFormIcon("fileexport.form.icon");
    }

    @Override
    public String getHelpResource() {
        return "help.ui.export";
    }

    @Override
    public void start() {
        validationListerner.fireValidationListener(true);
    }

    @Override
    public void finish() throws GUIException {
        Set<FileBean> files = ConverterData.getInstance().getDataFiles();
        String outputPath = filterPanel1.getOutputPath();
        File ouptPutFolder = new File(outputPath);
        if (!ouptPutFolder.exists()) {
            ouptPutFolder.mkdir();
        }
        Properties options = ConverterData.getInstance().getOptions();
        for (FileBean fileBean : files) {
            try {
                String finalPrideXmlFile = null;
                String workingPrideXmlFile = null;
                if (!filterOnly) {
                    File inputFile = new File(fileBean.getInputFile());
                    String reportFileName = fileBean.getReportFile();
                    if (filterPanel1.isRemoveWorkfiles()) {
                        ConverterData.getInstance().getFilesToDelete().add(reportFileName);
                    }
                    File reportFile = new File(reportFileName);
                    String prideFile = ouptPutFolder.getAbsolutePath() + FileUtils.FILE_SEPARATOR + inputFile.getName() + ConverterData.PRIDE_XML;
                    if (filterPanel1.isGzipped()) {
                        prideFile += FileUtils.gz;
                    }
                    DAO dao = DAOFactory.getInstance().getDAO(inputFile.getAbsolutePath(), ConverterData.getInstance().getDaoFormat());
                    ReportReader reader = new ReportReader(reportFile);
                    dao.setConfiguration(options);
                    if (fileBean.getSpectrumFile() != null) {
                        dao.setExternalSpectrumFile(fileBean.getSpectrumFile());
                    }
                    PrideXmlWriter out = new PrideXmlWriter(prideFile, reader, dao, filterPanel1.isGzipped(), filterPanel1.isSubmitToIntact());
                    out.setIncludeOnlyIdentifiedSpectra(filterPanel1.isIncludeOnlyIdentifiedSpectra());
                    logger.warn("Writing PRIDE XML to " + out.getOutputFilePath());
                    NavigationPanel.getInstance().setWorkingMessage("Writing PRIDE XML file: " + prideFile);
                    out.writeXml();
                    workingPrideXmlFile = prideFile;
                } else {
                    workingPrideXmlFile = fileBean.getInputFile();
                }
                if (filterPanel1.isFilterXml()) {
                    PrideXmlFilter filter = filterPanel1.getFilter(workingPrideXmlFile, workingPrideXmlFile);
                    logger.info("Filtering PRIDE XML file: " + filter.getOutputFilePath());
                    NavigationPanel.getInstance().setWorkingMessage("Filtering PRIDE XML file: " + filter.getOutputFilePath());
                    filter.writeXml();
                    finalPrideXmlFile = filter.getOutputFilePath();
                    if (filterPanel1.isRemoveWorkfiles()) {
                        ConverterData.getInstance().getFilesToDelete().add(workingPrideXmlFile);
                    }
                }
                if (finalPrideXmlFile == null) {
                    finalPrideXmlFile = workingPrideXmlFile;
                }
                fileBean.setOutputFile(finalPrideXmlFile);
                if (!filterOnly) {
                    logger.info("Validating PRIDE XML file: " + finalPrideXmlFile);
                    NavigationPanel.getInstance().setWorkingMessage("Validating PRIDE XML file: " + finalPrideXmlFile);
                    PrideXmlValidator validator = ValidatorFactory.getInstance().getPrideXmlValidator();
                    Collection<ValidatorMessage> msgs;
                    if (filterPanel1.isGzipped()) {
                        msgs = validator.validateGZFile(new File(finalPrideXmlFile));
                    } else {
                        msgs = validator.validate(new File(finalPrideXmlFile));
                    }
                    ConverterData.getInstance().setValidationMessages(finalPrideXmlFile, msgs);
                }
                if (filterPanel1.isRemoveWorkfiles()) {
                    Runtime.getRuntime().addShutdownHook(new Thread() {

                        @Override
                        public void run() {
                            IOUtilities.deleteFiles(ConverterData.getInstance().getFilesToDelete());
                        }
                    });
                }
            } catch (Exception e) {
                logger.fatal("Error in Converting to PrideXML files, error is " + e.getMessage(), e);
                ErrorDialogHandler.showErrorDialog(this, ErrorLevel.FATAL, "An error occurred while generating the PRIDE XML File.", "Error while generating PRIDE XML file: " + e.getMessage(), "WRAPPER-EXPORT", e);
                throw new GUIException("Error while generating PRIDE XML Files", e);
            }
        }
    }
}

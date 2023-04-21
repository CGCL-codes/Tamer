package com.datas.form.frame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import com.datas.bean.enums.PrintType;
import com.datas.component.common.CommonJRViewer;
import com.datas.form.common.CommonModalInternalFrame;
import com.datas.form.frame.main.MainFrame;
import com.datas.form.modul.common.ReportModul;
import com.datas.form.modul.manager.ModulManager;
import com.datas.form.modul.manager.StarterManager;
import com.datas.util.Constants;
import com.datas.util.LinePrinter;

@SuppressWarnings("serial")
public class JasperReportPreviewFrame extends CommonModalInternalFrame implements ActionListener {

    private ModulManager modul;

    private PrintType printType;

    private JasperPrint jasperPrint;

    private CommonJRViewer reportViewer;

    private JMenuBar menuBar;

    private JMenu fileMenu = new JMenu();

    private JMenu exportMenu = new JMenu();

    private JMenuItem emailReportMenuItem;

    private JMenuItem pageSetupMenuItem;

    private JMenuItem printMenuItem;

    private JMenuItem closeMenuItem;

    private JMenuItem exportPDFMenuItem;

    private JMenuItem exportExcelMenuItem;

    private JMenuItem exportRTFMenuItem;

    private JMenuItem exportHTMLMenuItem;

    private JMenuItem exportCSVMenuItem;

    private static final Logger LOG = Logger.getLogger(JasperReportPreviewFrame.class);

    public JasperReportPreviewFrame() {
        super();
    }

    @Override
    protected void init(String arg0) {
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setMinimumSize(new Dimension(900, 650));
        setPreferredSize(new Dimension(900, 650));
        if (printType == PrintType.LASER_PRINT) {
            setTitle(modul.getFullTitle() + " - " + getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_LASER_PRINT"));
        } else if (printType == PrintType.MATRIX_PRINT) {
            setTitle(modul.getFullTitle() + " - " + getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_MATRIX_PRINT"));
        } else {
            setTitle(modul.getFullTitle());
        }
        setLayout();
        setBehavior();
        setLookAndFeel();
        setDefaultLocation();
    }

    @Override
    protected void setLayout() {
        createMenuBar();
        setFont();
        this.setJMenuBar(menuBar);
    }

    @Override
    protected void setBehavior() {
        emailReportMenuItem.addActionListener(this);
        pageSetupMenuItem.addActionListener(this);
        printMenuItem.addActionListener(this);
        closeMenuItem.addActionListener(this);
        exportPDFMenuItem.addActionListener(this);
        exportExcelMenuItem.addActionListener(this);
        exportRTFMenuItem.addActionListener(this);
        exportHTMLMenuItem.addActionListener(this);
        exportCSVMenuItem.addActionListener(this);
    }

    @Override
    protected void setLookAndFeel() {
    }

    public void run(ModulManager modul, String filePath, PrintType printType) {
        this.modul = modul;
        this.printType = printType;
        init(null);
        HashMap parameters = new HashMap();
        populateParameters(parameters);
        JasperReport jasperReport = null;
        String url = getServiceContainer().getPropertiesService().getAppHome() + "/resources/print/" + filePath + ".jasper";
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(url);
        } catch (JRException e) {
            LOG.error(e);
        }
        try {
            if (modul instanceof ReportModul) {
                jasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, new JRTableModelDataSource(((ReportModul) modul).getReportData()));
            } else {
                jasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, new JRTableModelDataSource(modul.getTablePanel().getTable().getTableSorter()));
            }
        } catch (JRException e) {
            LOG.error(e);
        }
        reportViewer = new CommonJRViewer(jasperPrint);
        if (printType == PrintType.LASER_PRINT) {
            addCustomPrintButtonAction();
        }
        add(reportViewer);
    }

    private void populateParameters(HashMap parameters) {
        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
        Calendar calendar = new GregorianCalendar();
        parameters.put(Constants.REPORT_DATE, simpleDateTimeFormat.format(calendar.getTime()));
        parameters.put(Constants.REPORT_TITLE, modul.getModel().getSysModul().getTitle());
        parameters.put(Constants.REPORT_MODUL_TITLE, modul.getFullTitle());
        parameters.put(Constants.REPORT_USERNAME, MainFrame.getInstance().getLoginData().getSysUser().getIdUser());
        parameters.put(Constants.REPORT_NAZ_ORG, MainFrame.getInstance().getLoginData().getOrganizacija().getNazivOrganizacije());
        parameters.put(Constants.REPORT_ADRESA_ORG, MainFrame.getInstance().getLoginData().getOrganizacija().getAdresa());
        parameters.put(Constants.REPORT_MESTO_ORG, MainFrame.getInstance().getLoginData().getOrganizacija().getAdresa());
        parameters.put(Constants.REPORT_POSLOVNA_GODINA, MainFrame.getInstance().getLoginData().getOrganizacija().getPib());
        if (modul instanceof ReportModul) {
            HashMap<String, String> additionalParameters = ((ReportModul) modul).getReportParameters();
            for (Iterator<String> it = additionalParameters.keySet().iterator(); it.hasNext(); ) {
                String paramName = it.next();
                parameters.put(paramName, additionalParameters.get(paramName));
            }
        }
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        exportMenu = new JMenu();
        emailReportMenuItem = new JMenuItem();
        pageSetupMenuItem = new JMenuItem();
        printMenuItem = new JMenuItem();
        closeMenuItem = new JMenuItem();
        exportPDFMenuItem = new JMenuItem();
        exportExcelMenuItem = new JMenuItem();
        exportRTFMenuItem = new JMenuItem();
        exportHTMLMenuItem = new JMenuItem();
        exportCSVMenuItem = new JMenuItem();
        fileMenu.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_MENU_BASIC"));
        fileMenu.setMnemonic(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_MENU_BASIC_MNEMONIC").toCharArray()[0]);
        exportMenu.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_MENU_EXPORT"));
        exportMenu.setMnemonic(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_MENU_EXPORT_MNEMONIC").toCharArray()[0]);
        String appHome = getServiceContainer().getPropertiesService().getAppHome();
        ImageIcon[] iconPageSetupButton = { new ImageIcon(appHome + Constants.IMG_REPORT_PAGE_SETUP), new ImageIcon(appHome + Constants.IMG_REPORT_PRINT), new ImageIcon(appHome + Constants.IMG_REPORT_CLOSE), new ImageIcon(appHome + Constants.IMG_REPORT_EMAIL), new ImageIcon(Constants.IMG_REPORT_SAVE_AS) };
        KeyStroke ctrlM = KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK);
        emailReportMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EMAIL"));
        emailReportMenuItem.setIcon(iconPageSetupButton[3]);
        emailReportMenuItem.setAccelerator(ctrlM);
        KeyStroke ctrlG = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK);
        pageSetupMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_PAGE_SETUP"));
        pageSetupMenuItem.setIcon(iconPageSetupButton[0]);
        pageSetupMenuItem.setAccelerator(ctrlG);
        KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK);
        printMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_PRINT"));
        printMenuItem.setIcon(iconPageSetupButton[1]);
        printMenuItem.setAccelerator(ctrlP);
        KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK);
        closeMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXIT"));
        closeMenuItem.setIcon(iconPageSetupButton[2]);
        closeMenuItem.setAccelerator(ctrlX);
        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);
        exportPDFMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_PDF"));
        exportPDFMenuItem.setIcon(iconPageSetupButton[4]);
        exportPDFMenuItem.setAccelerator(ctrlS);
        KeyStroke ctrlE = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK);
        exportExcelMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_XLS"));
        exportExcelMenuItem.setIcon(iconPageSetupButton[4]);
        exportExcelMenuItem.setAccelerator(ctrlE);
        KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK);
        exportRTFMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_RTF"));
        exportRTFMenuItem.setIcon(iconPageSetupButton[4]);
        exportRTFMenuItem.setAccelerator(ctrlR);
        KeyStroke ctrlH = KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK);
        exportHTMLMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_HTML"));
        exportHTMLMenuItem.setIcon(iconPageSetupButton[4]);
        exportHTMLMenuItem.setAccelerator(ctrlH);
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK);
        exportCSVMenuItem.setText(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_CSV"));
        exportCSVMenuItem.setIcon(iconPageSetupButton[4]);
        exportCSVMenuItem.setAccelerator(ctrlC);
        fileMenu.add(emailReportMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(pageSetupMenuItem);
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(closeMenuItem);
        exportMenu.add(exportPDFMenuItem);
        exportMenu.addSeparator();
        exportMenu.add(exportRTFMenuItem);
        exportMenu.add(exportHTMLMenuItem);
        exportMenu.add(exportExcelMenuItem);
        exportMenu.add(exportCSVMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(exportMenu);
    }

    private void setFont() {
        fileMenu.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        exportMenu.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        emailReportMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        pageSetupMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        printMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        closeMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        exportPDFMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        exportRTFMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        exportHTMLMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        exportExcelMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
        exportCSVMenuItem.setFont(getServiceContainer().getLookAndFeelService().getFontMenu());
    }

    private void emailReport() {
        StarterManager.startAskExportPrintDialog(modul, jasperPrint);
    }

    private void exportPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_PDF"));
        fileChooser.showSaveDialog(this);
        String fileName = fileChooser.getSelectedFile().getAbsolutePath() + ".pdf";
        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(fileName));
        try {
            pdfExporter.exportReport();
        } catch (JRException e1) {
            LOG.error(e1);
        }
    }

    private void exportExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_XLS"));
        fileChooser.showSaveDialog(this);
        String fileName = fileChooser.getSelectedFile().getAbsolutePath() + ".xls";
        JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(fileName));
        try {
            xlsExporter.exportReport();
        } catch (JRException e1) {
            LOG.error(e1);
        }
    }

    private void exportRTF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_RTF"));
        fileChooser.showSaveDialog(this);
        String fileName = fileChooser.getSelectedFile().getAbsolutePath() + ".rtf";
        JRRtfExporter rtfExporter = new JRRtfExporter();
        rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(fileName));
        try {
            rtfExporter.exportReport();
        } catch (JRException e1) {
            LOG.error(e1);
        }
    }

    private void exportHTML() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_HTML"));
        fileChooser.showSaveDialog(this);
        String fileName = fileChooser.getSelectedFile().getAbsolutePath() + ".htm";
        JRHtmlExporter htmlExporter = new JRHtmlExporter();
        htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        htmlExporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(fileName));
        try {
            htmlExporter.exportReport();
        } catch (JRException e1) {
            LOG.error(e1);
        }
    }

    private void exportCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(getServiceContainer().getLocalizationService().getTranslation(getClass(), "REPORT_EXPORT_CSV"));
        fileChooser.showSaveDialog(this);
        String fileName = fileChooser.getSelectedFile().getAbsolutePath() + ".csv";
        JRCsvExporter csvExporter = new JRCsvExporter();
        csvExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        csvExporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(fileName));
        try {
            csvExporter.exportReport();
        } catch (JRException e1) {
            LOG.error(e1);
        }
    }

    private StringBuffer exportText() {
        StringBuffer buffer = new StringBuffer();
        JRTextExporter textExporter = new JRTextExporter();
        textExporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, getServiceContainer().getPropertiesService().getPageWidth());
        textExporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, getServiceContainer().getPropertiesService().getPageHeight());
        textExporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, getServiceContainer().getPropertiesService().getCharacterWidth());
        textExporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, getServiceContainer().getPropertiesService().getCharacterHeight());
        textExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        textExporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File("c:/aaa.txt"));
        try {
            textExporter.exportReport();
        } catch (JRException e1) {
            LOG.error(e1);
        }
        return buffer;
    }

    private void addCustomPrintButtonAction() {
        reportViewer.addCustomPrintButtonAction(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                runMatrixPrint();
            }
        });
    }

    private void runMatrixPrint() {
        List<String> list = new ArrayList<String>();
        LinePrinter dp = new LinePrinter(new Font("roman", Font.PLAIN, 10), true);
        StringBuffer buffer = exportText();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(emailReportMenuItem)) {
            emailReport();
        } else if (e.getSource().equals(pageSetupMenuItem)) {
        } else if (e.getSource().equals(printMenuItem)) {
        } else if (e.getSource().equals(closeMenuItem)) {
            dispose();
        } else if (e.getSource().equals(exportPDFMenuItem)) {
            exportPDF();
        } else if (e.getSource().equals(exportRTFMenuItem)) {
            exportRTF();
        } else if (e.getSource().equals(exportHTMLMenuItem)) {
            exportHTML();
        } else if (e.getSource().equals(exportExcelMenuItem)) {
            exportExcel();
        } else if (e.getSource().equals(exportCSVMenuItem)) {
            exportCSV();
        }
    }
}

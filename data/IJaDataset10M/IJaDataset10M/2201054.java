package com.das.misc.app;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.hibernate.Session;
import com.das.core.app.AppBase;
import com.das.document.logic.Document;
import com.das.document.logic.DocumentModel;
import com.das.preference.logic.Preference;
import com.das.preference.logic.PreferenceModel;
import com.das.sampler.logic.Sampler;
import com.das.sampler.logic.SamplerModel;
import com.das.test.capture.ImageArea;
import com.das.user.logic.User;
import com.das.util.AppConstants;
import com.das.util.DateFormatter;
import com.das.util.HibernateUtil;
import com.google.zxing.MonochromeBitmapSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageMonochromeBitmapSource;
import com.swtdesigner.SwingResourceManager;

public class Home extends AppBase {

    private JTextField txtTtlDocumentScan;

    private JTextField txtSuccessRatio;

    private JTextField txtTtlPendingFix;

    private JTextField txtTtlCompleteScan;

    private class TblImgFixModel extends DefaultTableModel {

        private final String[] COLUMNS = new String[] { "Name" };

        public int getColumnCount() {
            return COLUMNS.length;
        }

        public String getColumnName(int column) {
            return COLUMNS[column];
        }
    }

    private JTable tblImgFix;

    private class TblImgCompleteModel extends DefaultTableModel {

        private final String[] COLUMNS = new String[] { "Name", "Barcode", "Year" };

        public int getColumnCount() {
            return COLUMNS.length;
        }

        public String getColumnName(int column) {
            return COLUMNS[column];
        }
    }

    private JTable tblImgComplete;

    private class TblImgScanModel extends DefaultTableModel {

        private final String[] COLUMNS = new String[] { "Name", "Size (KB)", "Date" };

        public int getColumnCount() {
            return COLUMNS.length;
        }

        public String getColumnName(int column) {
            return COLUMNS[column];
        }
    }

    private JTable tblImgScan;

    private boolean isScanning;

    private final Timer timer = new Timer();

    final long interval = 7000L;

    private SimpleDateFormat sdf = new SimpleDateFormat(DateFormatter.YYYYMMDDHHMM);

    private List<Document> documents;

    public static final String HOME_FIX_NAME = "fix.name";

    public static final String HOME_FIX_DOCUMENTS = "fix.documents";

    public static final String HOME_TTLCOMPLETESCAN = "home.ttlcompletescan";

    public static final String HOME_TTLPENDINGFIX = "home.ttlpendingfix";

    public static final String HOME_TTLDOCUMENTSCAN = "home.ttldocumentscan";

    public static final String HOME_SUCCESSRATE = "home.successrate";

    private ImageArea imgArea;

    private Robot robot;

    private Rectangle rectScreenSize;

    private Dimension dimScreenSize;

    private int ttlCompleteScan;

    private int ttlPendingFix;

    private int ttlDocumentScan;

    private Double successRate;

    private User user;

    /**
	 * Create the panel
	 */
    public Home(Container container, Map<String, Object> params) {
        super();
        setName("Home");
        setPreferredSize(new Dimension(1000, 700));
        setParams(params);
        initPanel(this, container, getParams());
        setSuccessRate(0.0);
        if (getParams().get(HOME_TTLCOMPLETESCAN) != null) {
            setTtlCompleteScan((Integer) getParams().get(HOME_TTLCOMPLETESCAN));
        } else {
            setTtlCompleteScan(0);
        }
        if (getParams().get(HOME_TTLPENDINGFIX) != null) {
            setTtlPendingFix((Integer) getParams().get(HOME_TTLPENDINGFIX));
        } else {
            setTtlPendingFix(0);
        }
        if (getParams().get(HOME_TTLDOCUMENTSCAN) != null) {
            setTtlDocumentScan((Integer) getParams().get(HOME_TTLDOCUMENTSCAN));
        } else {
            setTtlDocumentScan(0);
        }
        if (getParams().get(HOME_FIX_DOCUMENTS) != null) {
            setDocuments((List<Document>) getParams().get(HOME_FIX_DOCUMENTS));
        } else {
            setDocuments(new ArrayList<Document>());
        }
        final JLabel testing123Label = new JLabel();
        testing123Label.setText("Home");
        testing123Label.setBounds(34, 30, 205, 20);
        add(testing123Label);
        final JButton preferenceButton = new JButton();
        preferenceButton.setText("Preference");
        preferenceButton.setToolTipText("Preference");
        preferenceButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                setParams(populateParams());
                show(getContainer(), Screen.PREFERENCE, getParams());
            }
        });
        preferenceButton.setIcon(SwingResourceManager.getIcon(Home.class, "/img/Config.png"));
        preferenceButton.setBounds(34, 56, 120, 30);
        add(preferenceButton);
        final JButton userButton = new JButton();
        userButton.setText("User");
        userButton.setToolTipText("User Account");
        userButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                setParams(populateParams());
                show(getContainer(), Screen.USER_MGM, getParams());
            }
        });
        userButton.setIcon(SwingResourceManager.getIcon(Home.class, "/img/Users.png"));
        userButton.setBounds(160, 56, 85, 30);
        add(userButton);
        final JLabel listOfImageLabel = new JLabel();
        listOfImageLabel.setText("List of image scanned");
        listOfImageLabel.setBounds(34, 92, 125, 20);
        add(listOfImageLabel);
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(34, 118, 635, 128);
        add(scrollPane);
        tblImgScan = new JTable();
        tblImgScan.setModel(new TblImgScanModel());
        scrollPane.setViewportView(tblImgScan);
        final JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(30, 280, 406, 144);
        add(scrollPane_1);
        tblImgComplete = new JTable();
        tblImgComplete.setModel(new TblImgCompleteModel());
        scrollPane_1.setViewportView(tblImgComplete);
        final JLabel listOfImageLabel_1 = new JLabel();
        listOfImageLabel_1.setText("Success Completed");
        listOfImageLabel_1.setBounds(30, 255, 125, 20);
        add(listOfImageLabel_1);
        final JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(442, 280, 227, 144);
        add(scrollPane_2);
        tblImgFix = new JTable();
        tblImgFix.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblImgFix.setModel(new TblImgFixModel());
        scrollPane_2.setViewportView(tblImgFix);
        final JLabel listOfImageLabel_1_1 = new JLabel();
        listOfImageLabel_1_1.setText("Pending Fix");
        listOfImageLabel_1_1.setBounds(557, 255, 125, 20);
        add(listOfImageLabel_1_1);
        final JButton fixButton = new JButton();
        fixButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                fixDocument();
            }
        });
        fixButton.setIcon(SwingResourceManager.getIcon(Home.class, "/img/Edit.png"));
        fixButton.setText("Fix");
        fixButton.setBounds(599, 430, 70, 30);
        add(fixButton);
        final JButton findButton = new JButton();
        findButton.setText("Find");
        findButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                setParams(populateParams());
                show(getContainer(), Screen.FIND, getParams());
            }
        });
        findButton.setIcon(SwingResourceManager.getIcon(Home.class, "/img/Zoom-in.png"));
        findButton.setBounds(248, 56, 80, 30);
        add(findButton);
        final JButton startScanningButton = new JButton();
        startScanningButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                startStopScan(startScanningButton);
            }
        });
        startScanningButton.setText("Start Scan");
        startScanningButton.setBounds(540, 49, 130, 45);
        add(startScanningButton);
        try {
            robot = new Robot();
        } catch (AWTException awt_ex) {
            awt_ex.printStackTrace();
        }
        dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        rectScreenSize = new Rectangle(dimScreenSize);
        setImgArea(new ImageArea());
        final JLabel totalCompletedSuccessLabel = new JLabel();
        totalCompletedSuccessLabel.setText("Total Success Completed");
        totalCompletedSuccessLabel.setBounds(675, 119, 145, 20);
        add(totalCompletedSuccessLabel);
        txtTtlCompleteScan = new JTextField();
        txtTtlCompleteScan.setEditable(false);
        txtTtlCompleteScan.setBounds(733, 144, 87, 20);
        add(txtTtlCompleteScan);
        final JLabel totalPendingFixLabel = new JLabel();
        totalPendingFixLabel.setText("Total Pending Fix");
        totalPendingFixLabel.setBounds(720, 253, 100, 20);
        add(totalPendingFixLabel);
        txtTtlPendingFix = new JTextField();
        txtTtlPendingFix.setEditable(false);
        txtTtlPendingFix.setBounds(733, 282, 87, 20);
        add(txtTtlPendingFix);
        final JLabel ratioOfSuccessLabel = new JLabel();
        ratioOfSuccessLabel.setText("Success Ratio");
        ratioOfSuccessLabel.setBounds(735, 324, 85, 20);
        add(ratioOfSuccessLabel);
        txtSuccessRatio = new JTextField();
        txtSuccessRatio.setEditable(false);
        txtSuccessRatio.setBounds(733, 350, 87, 20);
        add(txtSuccessRatio);
        final JLabel ratioOfSuccessLabel_1 = new JLabel();
        ratioOfSuccessLabel_1.setText("Total Document Scanned");
        ratioOfSuccessLabel_1.setBounds(675, 187, 145, 20);
        add(ratioOfSuccessLabel_1);
        txtTtlDocumentScan = new JTextField();
        txtTtlDocumentScan.setEditable(false);
        txtTtlDocumentScan.setBounds(733, 213, 87, 20);
        add(txtTtlDocumentScan);
        if (!documents.isEmpty() && !isScanning()) {
            startStopScan(startScanningButton);
        }
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    private void startStopScan(final JButton button) {
        if (isScanning() == false) {
            button.setIcon(SwingResourceManager.getIcon(Home.class, "/img/running.gif"));
            button.setText("Scanning");
            setScanning(true);
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                    try {
                        session.beginTransaction();
                        Preference preference = new PreferenceModel();
                        preference = (Preference) preference.doList(preference).get(0);
                        Sampler sampler = new SamplerModel();
                        sampler = (Sampler) sampler.doList(sampler).get(0);
                        if (sampler != null && preference != null && preference.getScanLocation() != null) {
                            File scannedFolder = new File(preference.getScanLocation());
                            File[] files = scannedFolder.listFiles();
                            String name = "";
                            if (files != null) {
                                for (int i = 0; i < files.length; i++) {
                                    name = files[i].getName();
                                    if (name.toLowerCase().endsWith("gif")) {
                                        boolean isFoundSameName = false;
                                        for (Iterator<Document> iter = documents.iterator(); iter.hasNext(); ) {
                                            Document doc = iter.next();
                                            if (doc != null && name.equals(doc.getName())) {
                                                isFoundSameName = true;
                                                break;
                                            }
                                        }
                                        if (!isFoundSameName) {
                                            Document document = new DocumentModel();
                                            document = processDocument(document, files[i], sampler, preference);
                                            documents.add(document);
                                        }
                                    }
                                }
                            }
                            if (documents != null && !documents.isEmpty()) {
                                refreshTblImgScan(documents);
                                refreshTblImgComplete(documents);
                                refreshTblImgFix(documents);
                                refreshStatistics(documents, txtTtlCompleteScan, txtTtlPendingFix, txtSuccessRatio, txtTtlDocumentScan);
                                setDocuments(storeDocuments(documents));
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, 0, interval);
        } else {
            setScanning(false);
            button.setIcon(null);
            button.setText("Start Scan");
            timer.cancel();
        }
    }

    public boolean isScanning() {
        return isScanning;
    }

    public void setScanning(boolean isScanning) {
        this.isScanning = isScanning;
    }

    private static final String getDecodeText(Image imageInput) {
        BufferedImage image;
        try {
            image = convert(imageInput);
        } catch (Exception e) {
            return e.toString();
        }
        if (image == null) {
            return "Could not decode image";
        }
        MonochromeBitmapSource source = new BufferedImageMonochromeBitmapSource(image);
        Result result;
        try {
            result = new MultiFormatReader().decode(source);
        } catch (ReaderException re) {
            return re.toString();
        }
        return result.getText();
    }

    private static final BufferedImage convert(Image im) {
        BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    private void refreshTblImgScan(List<Document> documents) {
        DefaultTableModel tableModel = new TblImgScanModel();
        if (documents != null && !documents.isEmpty()) {
            Vector<String> rowData = null;
            for (Iterator<Document> iter = documents.iterator(); iter.hasNext(); ) {
                Document doc = iter.next();
                rowData = new Vector<String>();
                rowData.add(doc.getName());
                rowData.add(doc.getSize().toString());
                rowData.add(sdf.format(doc.getDate()));
                tableModel.addRow(rowData);
            }
        }
        this.tblImgScan.setModel(tableModel);
        this.tblImgScan.revalidate();
    }

    private void refreshTblImgComplete(List<Document> documents) {
        DefaultTableModel tableModel = new TblImgCompleteModel();
        if (documents != null && !documents.isEmpty()) {
            Vector<String> rowData = null;
            for (Iterator<Document> iter = documents.iterator(); iter.hasNext(); ) {
                Document doc = iter.next();
                if (doc.getStatus().equals(AppConstants.STATUS_ACTIVE)) {
                    rowData = new Vector<String>();
                    rowData.add(doc.getName());
                    rowData.add(doc.getBarcode());
                    rowData.add(doc.getYear() + "");
                    tableModel.addRow(rowData);
                }
            }
        }
        this.tblImgComplete.setModel(tableModel);
        this.tblImgComplete.revalidate();
    }

    private void refreshTblImgFix(List<Document> documents) {
        DefaultTableModel tableModel = new TblImgFixModel();
        if (documents != null && !documents.isEmpty()) {
            Vector<String> rowData = null;
            for (Iterator<Document> iter = documents.iterator(); iter.hasNext(); ) {
                Document doc = iter.next();
                if (doc.getStatus().equals(AppConstants.STATUS_INACTIVE)) {
                    rowData = new Vector<String>();
                    rowData.add(doc.getName());
                    rowData.add(doc.getSize().toString());
                    rowData.add(sdf.format(doc.getDate()));
                    tableModel.addRow(rowData);
                }
            }
        }
        this.tblImgFix.setModel(tableModel);
        this.tblImgFix.revalidate();
    }

    private void fixDocument() {
        timer.cancel();
        int rowIndex = this.tblImgFix.getSelectedRow();
        TableModel tableModel = this.tblImgFix.getModel();
        String value = (String) tableModel.getValueAt(rowIndex, 0);
        Map<String, Object> params = getParams();
        params.put(HOME_FIX_NAME, value);
        params.put(HOME_FIX_DOCUMENTS, documents);
        setParams(params);
        setParams(populateParams());
        show(getContainer(), Screen.FIX, getParams());
    }

    private List<Document> storeDocuments(List<Document> documents) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Document> newDocuments = new ArrayList<Document>();
        try {
            session.beginTransaction();
            Preference preference = new PreferenceModel();
            preference = (Preference) preference.doList(preference).get(0);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            if (documents != null && !documents.isEmpty()) {
                for (Iterator<Document> iter = documents.iterator(); iter.hasNext(); ) {
                    Document document = iter.next();
                    if (AppConstants.STATUS_ACTIVE.equals(document.getStatus())) {
                        try {
                            document = (Document) preAdd(document, getParams());
                            File fileIn = new File(preference.getScanLocation() + File.separator + document.getName());
                            File fileOut = new File(preference.getStoreLocation() + File.separator + document.getName());
                            FileInputStream in = new FileInputStream(fileIn);
                            FileOutputStream out = new FileOutputStream(fileOut);
                            int c;
                            while ((c = in.read()) != -1) out.write(c);
                            in.close();
                            out.close();
                            document.doAdd(document);
                            boolean isDeleted = fileIn.delete();
                            System.out.println("Deleted scan folder file: " + document.getName() + ":" + isDeleted);
                            if (isDeleted) {
                                document.setStatus(AppConstants.STATUS_PROCESSING);
                                int uploadCount = 0;
                                if (document.getUploadCount() != null) {
                                    uploadCount = document.getUploadCount();
                                }
                                uploadCount++;
                                document.setUploadCount(uploadCount);
                                newDocuments.add(document);
                            }
                        } catch (Exception add_ex) {
                            add_ex.printStackTrace();
                        }
                    } else if (AppConstants.STATUS_PROCESSING.equals(document.getStatus())) {
                        int uploadCount = document.getUploadCount();
                        if (uploadCount < 5) {
                            uploadCount++;
                            document.setUploadCount(uploadCount);
                            System.out.println("increase upload count: " + document.getName() + ":" + uploadCount);
                            newDocuments.add(document);
                        } else {
                            System.out.println("delete from documents list: " + document.getName());
                        }
                    } else if (AppConstants.STATUS_INACTIVE.equals(document.getStatus())) {
                        document.setFixFlag(AppConstants.FLAG_NO);
                        newDocuments.add(document);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newDocuments;
    }

    private Map<String, Object> populateParams() {
        Map<String, Object> params = getParams();
        params.put(HOME_TTLCOMPLETESCAN, getTtlCompleteScan());
        params.put(HOME_TTLPENDINGFIX, getTtlPendingFix());
        params.put(HOME_TTLDOCUMENTSCAN, getTtlDocumentScan());
        return params;
    }

    private void refreshStatistics(List<Document> documents, JTextField txtCS, JTextField txtPF, JTextField txtSR, JTextField txtDS) {
        int ttlCompleteScan = getTtlCompleteScan();
        int ttlPendingFix = getTtlPendingFix();
        int ttlDocumentScan = getTtlDocumentScan();
        for (Iterator<Document> iter = documents.iterator(); iter.hasNext(); ) {
            Document doc = iter.next();
            if (doc != null) {
                if (AppConstants.STATUS_ACTIVE.equals(doc.getStatus())) {
                    ttlCompleteScan++;
                    System.out.println("++ counter: " + doc.getName());
                } else if (AppConstants.STATUS_INACTIVE.equals(doc.getStatus())) {
                    if (!AppConstants.FLAG_NO.equals(doc.getFixFlag())) {
                        ttlPendingFix++;
                    }
                }
                if (!AppConstants.STATUS_PROCESSING.equals(doc.getStatus()) && (!AppConstants.FLAG_NO.equals(doc.getFixFlag()))) {
                    ttlDocumentScan++;
                }
            }
        }
        setTtlCompleteScan(ttlCompleteScan);
        setTtlPendingFix(ttlPendingFix);
        setTtlDocumentScan(ttlDocumentScan);
        if (ttlDocumentScan > 0) {
            setSuccessRate(ttlCompleteScan * 100.0 / ttlDocumentScan);
        }
        txtCS.setText(ttlCompleteScan + "");
        txtPF.setText(ttlPendingFix + "");
        txtDS.setText(ttlDocumentScan + "");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        txtSR.setText(df.format(getSuccessRate()) + "%");
    }

    private Document processDocument(Document document, File file, Sampler sampler, Preference preference) throws Exception {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        ImageIcon imageIcon = new ImageIcon(file.toURI().toURL());
        Image image = imageIcon.getImage();
        image = createImage(new FilteredImageSource(image.getSource(), new CropImageFilter(sampler.getX(), sampler.getY(), sampler.getWidth(), sampler.getHeight())));
        imageIcon = new ImageIcon(image);
        String barcode = getDecodeText(image);
        document.setYear(year);
        document.setName(file.getName());
        document.setSize(file.length() / 1024);
        document.setDate(new Date());
        if (preference.getLocation() != null && preference.getLocation().length() > 0) {
            document.setLocation(preference.getLocation());
        }
        if (preference.getBackLogFlag() != null && AppConstants.BACKLOG_TRUE.equals(preference.getBackLogFlag())) {
            document.setBackLogFlag(AppConstants.BACKLOG_YES);
            document.setDataEntryFlag(AppConstants.DATAENTRY_NEW);
        } else {
            document.setBackLogFlag(AppConstants.BACKLOG_NO);
            document.setDataEntryFlag(AppConstants.DATAENTRY_NEW);
        }
        if (barcode != null && barcode.length() == 7) {
            Document docModel = new DocumentModel();
            List<Document> docs = docModel.findByBarcode(barcode, year);
            if (docs != null && !docs.isEmpty()) {
                document.setBarcode(barcode);
                document.setStatus(AppConstants.STATUS_INACTIVE);
                document.setFixReason(AppConstants.FIXREASON_DUPLICATEBARCODE);
            } else {
                document.setBarcode(barcode);
                document.setStatus(AppConstants.STATUS_ACTIVE);
            }
        } else {
            document.setBarcode("");
            document.setStatus(AppConstants.STATUS_INACTIVE);
            document.setFixReason(AppConstants.FIXREASON_INVALIDBARCODE);
        }
        System.out.println("Decoded Text is " + barcode);
        return document;
    }

    public ImageArea getImgArea() {
        return imgArea;
    }

    public void setImgArea(ImageArea imgArea) {
        this.imgArea = imgArea;
    }

    public int getTtlCompleteScan() {
        return ttlCompleteScan;
    }

    public void setTtlCompleteScan(int ttlCompleteScan) {
        this.ttlCompleteScan = ttlCompleteScan;
    }

    public int getTtlPendingFix() {
        return ttlPendingFix;
    }

    public void setTtlPendingFix(int ttlPendingFix) {
        this.ttlPendingFix = ttlPendingFix;
    }

    public int getTtlDocumentScan() {
        return ttlDocumentScan;
    }

    public void setTtlDocumentScan(int ttlDocumentScan) {
        this.ttlDocumentScan = ttlDocumentScan;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}

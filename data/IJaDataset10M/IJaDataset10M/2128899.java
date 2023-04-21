package ishima.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import ishima.core.AlternateProcessor;
import ishima.core.Cleaner;
import ishima.core.DevelopExtractor;
import ishima.core.Downloader;
import ishima.core.ProjectErazer;
import ishima.core.ProjectProcesor;
import ishima.core.TroveListCrawler;
import ishima.logger.Logger;
import ishima.qa.CAPTester;
import ishima.qa.CKJMTester;
import ishima.qa.JavaNCSSTester;
import ishima.qa.PMDTester;
import ishima.qa.StatisticsProvider;
import ishima.sql.SQLConnection;
import ishima.sql.SQLManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Scale;

public class MainForm {

    public static Integer pobieranychProjektwNumber = 0;

    public static Integer przeliczanychProjektwNumber = 0;

    public static Integer pobieranychProjektwMAX = 3;

    private static Boolean downloadInProgress = new Boolean(false);

    static Downloader[] dwnldr;

    static TroveListCrawler crw;

    static List listLog;

    private static JavaNCSSTester javancssTester = null;

    private static PMDTester pmdTester = null;

    private static CKJMTester ckjmTester = null;

    private static CAPTester capTester = null;

    private static Text text;

    public static String processMsg;

    public static Button processButton;

    public static boolean processing;

    public static Button btnPobieranie;

    private static Cleaner cln;

    private static ProjectErazer erz;

    public static Integer zbadanychProjektow = 0;

    private static Label lblZbadanychProjektw;

    private static Label label_1;

    private static Label label_2;

    private static Label label_3;

    private static Label label_4;

    private static Label label_5;

    private static Label label_6;

    private static Label label_7;

    private static Label label_8;

    private static Text text_2;

    private static Text text_3;

    private static Text text_1;

    private static StatisticsProvider statProvider;

    private static Label lblWPeniPrzetworzonych;

    private static Label lblbrak;

    private static Integer wPelniPrzetworzonych;

    private static String aktualnieBadany;

    private static DevelopExtractor de;

    private static Text text_4;

    private static String[] axisList = { "rank", "activity", "downloads", "avgNCSS", "avgCCN", "avgJVDC", "NCSS", "WMC", "DIT", "CBO", "RFC", "LCOM", "NPM", "Files", "Lines", "Code", "CodeComment", "Comment", "Blank", "Basic Rules", "Design Rules", "Unused Code Rules", "Coupling Rules", "Security Code Guidelines", "Optimization Rules", "Import Statement Rules", "Java Logging Rules", "Strict Exception Rules", "Naming Rules", "Clone Implementation Rules", "String and StringBuffer Rules", "Priority1", "Priority2", "Priority3", "Priority4", "BugsOpen", "BugsTotal", "SupportRequestOpen", "SupportRequestTotal", "PatchesTotal", "PatchesOpen", "FeatureRequestOpen", "FeatureRequestTotal", "PublicForums", "PublicForumsMessages", "MailingLists", "CVScommits", "SVNcommits", "CVSreads", "SVNreads", "All Errors", "Lines/File", "Lines / 1 Error", "% Comment", "API Size", "Code Density" };

    private static Text txtDmgr;

    private static Text txtReportpng;

    private static Button btnUsuPrzypadkiBrzegowe;

    private static Combo combo_2;

    private static Combo combo_1;

    public static void main(String[] args) {
        final Display display = new Display();
        Shell shlIshimaProject = new Shell(display);
        shlIshimaProject.setText("Ishima Project");
        shlIshimaProject.setLayout(new FillLayout(SWT.HORIZONTAL));
        shlIshimaProject.setMinimumSize(new Point(500, 400));
        shlIshimaProject.pack();
        {
            TabFolder tabFolder = new TabFolder(shlIshimaProject, SWT.NONE);
            {
                TabItem tbtmAnaliza = new TabItem(tabFolder, SWT.NONE);
                tbtmAnaliza.setText("Przeliczenia");
                {
                    SashForm sashForm = new SashForm(tabFolder, SWT.NONE);
                    tbtmAnaliza.setControl(sashForm);
                    {
                        Group grpStatystykiPrzelicze = new Group(sashForm, SWT.NONE);
                        grpStatystykiPrzelicze.setLayout(new RowLayout(SWT.VERTICAL));
                        grpStatystykiPrzelicze.setText("Statystyki przeliczeń");
                        lblZbadanychProjektw = new Label(grpStatystykiPrzelicze, SWT.NONE);
                        lblZbadanychProjektw.setLayoutData(new RowData(223, 13));
                        lblZbadanychProjektw.setText("Zbadanych: 0");
                        lblWPeniPrzetworzonych = new Label(grpStatystykiPrzelicze, SWT.NONE);
                        lblWPeniPrzetworzonych.setLayoutData(new RowData(218, 13));
                        lblWPeniPrzetworzonych.setText("W pełni przetworzonych: 0");
                        Composite composite_1 = new Composite(grpStatystykiPrzelicze, SWT.NONE);
                        RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
                        rowLayout.marginTop = 0;
                        rowLayout.marginLeft = 0;
                        composite_1.setLayout(rowLayout);
                        composite_1.setLayoutData(new RowData(217, 22));
                        Label lblAktualniePrzetwarzany = new Label(composite_1, SWT.NONE);
                        lblAktualniePrzetwarzany.setText("Aktualnie przetwarzany: ");
                        lblbrak = new Label(composite_1, SWT.NONE);
                        lblbrak.setLayoutData(new RowData(92, 13));
                        lblbrak.setText("(BRAK)");
                        {
                            Composite composite = new Composite(grpStatystykiPrzelicze, SWT.NONE);
                            composite.setLayoutData(new RowData(218, 255));
                            composite.setLayout(new FillLayout(SWT.HORIZONTAL));
                            {
                                listLog = new List(composite, SWT.BORDER | SWT.V_SCROLL);
                            }
                        }
                    }
                    {
                        Composite composite = new Composite(sashForm, SWT.NONE);
                        composite.setLayout(new FillLayout(SWT.HORIZONTAL));
                        {
                            SashForm sashForm_1 = new SashForm(composite, SWT.NONE);
                            sashForm_1.setOrientation(SWT.VERTICAL);
                            {
                                Group grpStatusPracy = new Group(sashForm_1, SWT.NONE);
                                grpStatusPracy.setLayout(new FillLayout(SWT.HORIZONTAL));
                                grpStatusPracy.setText("Status pracy");
                                {
                                    Composite composite_1 = new Composite(grpStatusPracy, SWT.NONE);
                                    composite_1.setLayout(new FillLayout(SWT.VERTICAL));
                                    Group grpPobieranie = new Group(composite_1, SWT.NONE);
                                    grpPobieranie.setLayout(new FillLayout(SWT.VERTICAL));
                                    grpPobieranie.setText("Pobieranie");
                                    {
                                        Composite composite_2 = new Composite(grpPobieranie, SWT.NONE);
                                        composite_2.setLayout(new RowLayout(SWT.HORIZONTAL));
                                        {
                                            Button btnStartCrawler = new Button(composite_2, SWT.NONE);
                                            btnStartCrawler.addSelectionListener(new SelectionAdapter() {

                                                @Override
                                                public void widgetSelected(SelectionEvent e) {
                                                    toogleCrawling();
                                                }
                                            });
                                            btnStartCrawler.setText("Pobieranie informacji");
                                        }
                                        {
                                            label_1 = new Label(composite_2, SWT.NONE);
                                            label_1.setText("OFF");
                                            label_1.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
                                        }
                                    }
                                    Composite composite_2 = new Composite(grpPobieranie, SWT.NONE);
                                    composite_2.setLayout(new RowLayout(SWT.HORIZONTAL));
                                    {
                                        Button btnStartDownloader = new Button(composite_2, SWT.NONE);
                                        btnStartDownloader.addSelectionListener(new SelectionAdapter() {

                                            @Override
                                            public void widgetSelected(SelectionEvent e) {
                                                toogleDownloader();
                                            }
                                        });
                                        btnStartDownloader.setText("Pobieranie projektów");
                                    }
                                    {
                                        label_2 = new Label(composite_2, SWT.NONE);
                                        label_2.setText("OFF");
                                        label_2.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
                                    }
                                    Group grpAnaliza = new Group(composite_1, SWT.NONE);
                                    grpAnaliza.setLayout(new FillLayout(SWT.HORIZONTAL));
                                    grpAnaliza.setText("Analiza");
                                    Composite composite_3 = new Composite(grpAnaliza, SWT.NONE);
                                    composite_3.setLayout(new FillLayout(SWT.VERTICAL));
                                    Composite composite_5 = new Composite(composite_3, SWT.NONE);
                                    composite_5.setLayout(new RowLayout(SWT.VERTICAL));
                                    Button btnJavancss = new Button(composite_5, SWT.NONE);
                                    btnJavancss.addSelectionListener(new SelectionAdapter() {

                                        @Override
                                        public void widgetSelected(SelectionEvent e) {
                                            MainForm.toogleJavaNCSS();
                                        }
                                    });
                                    btnJavancss.setText("JavaNCSS");
                                    label_3 = new Label(composite_5, SWT.NONE);
                                    label_3.setLayoutData(new RowData(30, 21));
                                    label_3.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
                                    label_3.setText("OFF");
                                    Composite composite_6 = new Composite(composite_3, SWT.NONE);
                                    composite_6.setLayout(new RowLayout(SWT.VERTICAL));
                                    Button btnCodeanalyzerpro = new Button(composite_6, SWT.NONE);
                                    btnCodeanalyzerpro.addSelectionListener(new SelectionAdapter() {

                                        @Override
                                        public void widgetSelected(SelectionEvent e) {
                                            MainForm.toogleCAP();
                                        }
                                    });
                                    btnCodeanalyzerpro.setText("CAP");
                                    label_4 = new Label(composite_6, SWT.NONE);
                                    label_4.setText("OFF");
                                    label_4.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
                                    Composite composite_4 = new Composite(grpAnaliza, SWT.NONE);
                                    composite_4.setLayout(new FillLayout(SWT.VERTICAL));
                                    Composite composite_7 = new Composite(composite_4, SWT.NONE);
                                    composite_7.setLayout(new RowLayout(SWT.HORIZONTAL));
                                    Button btnCkjm = new Button(composite_7, SWT.NONE);
                                    btnCkjm.addSelectionListener(new SelectionAdapter() {

                                        @Override
                                        public void widgetSelected(SelectionEvent e) {
                                            MainForm.toogleCKJM();
                                        }
                                    });
                                    btnCkjm.setText("CKJM");
                                    {
                                        label_5 = new Label(composite_7, SWT.NONE);
                                        label_5.setText("OFF");
                                        label_5.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
                                    }
                                    Composite composite_8 = new Composite(composite_4, SWT.NONE);
                                    composite_8.setLayout(new RowLayout(SWT.HORIZONTAL));
                                    Button btnPmd = new Button(composite_8, SWT.NONE);
                                    btnPmd.addSelectionListener(new SelectionAdapter() {

                                        @Override
                                        public void widgetSelected(SelectionEvent e) {
                                            MainForm.tooglePMD();
                                        }
                                    });
                                    btnPmd.setText("PMD");
                                    {
                                        label_6 = new Label(composite_8, SWT.NONE);
                                        label_6.setText("OFF");
                                        label_6.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
                                    }
                                    Group grpCzyszczenie = new Group(composite_1, SWT.NONE);
                                    grpCzyszczenie.setLayout(new FillLayout(SWT.VERTICAL));
                                    grpCzyszczenie.setText("Czyszczenie");
                                    {
                                        Composite composite_9 = new Composite(grpCzyszczenie, SWT.NONE);
                                        composite_9.setLayout(new RowLayout(SWT.HORIZONTAL));
                                        {
                                            Button btnRozpocznijCzyszczenie = new Button(composite_9, SWT.NONE);
                                            btnRozpocznijCzyszczenie.addSelectionListener(new SelectionAdapter() {

                                                @Override
                                                public void widgetSelected(SelectionEvent e) {
                                                    toogleCleanUp();
                                                }
                                            });
                                            btnRozpocznijCzyszczenie.setText("Czyszczenie");
                                        }
                                        label_7 = new Label(composite_9, SWT.NONE);
                                        label_7.setText("OFF");
                                        label_7.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
                                    }
                                    {
                                        Composite composite_9 = new Composite(grpCzyszczenie, SWT.NONE);
                                        composite_9.setLayout(new RowLayout(SWT.HORIZONTAL));
                                        {
                                            Button btnRozpocznijUsuwanie = new Button(composite_9, SWT.NONE);
                                            btnRozpocznijUsuwanie.addSelectionListener(new SelectionAdapter() {

                                                @Override
                                                public void widgetSelected(SelectionEvent e) {
                                                    toogleEraze();
                                                }
                                            });
                                            btnRozpocznijUsuwanie.setText("Usuwanie\r\n");
                                        }
                                        label_8 = new Label(composite_9, SWT.NONE);
                                        label_8.setText("OFF");
                                        label_8.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
                                    }
                                    Group grpAnalizaIndywidualna = new Group(composite_1, SWT.NONE);
                                    grpAnalizaIndywidualna.setText("Analiza indywidualna");
                                    grpAnalizaIndywidualna.setLayout(new FillLayout(SWT.HORIZONTAL));
                                    Composite composite_4_1 = new Composite(grpAnalizaIndywidualna, SWT.NONE);
                                    composite_4_1.setLayout(new RowLayout(SWT.HORIZONTAL));
                                    Composite composite_5_1 = new Composite(composite_4_1, SWT.NONE);
                                    composite_5_1.setLayout(new RowLayout(SWT.VERTICAL));
                                    {
                                        text = new Text(composite_5_1, SWT.BORDER);
                                        text.setLayoutData(new RowData(85, SWT.DEFAULT));
                                        text.setText("ishimaproject");
                                    }
                                    Composite composite_2_1_1 = new Composite(composite_5_1, SWT.NONE);
                                    composite_2_1_1.setLayoutData(new RowData(64, 6));
                                    {
                                        btnPobieranie = new Button(composite_5_1, SWT.CHECK);
                                        btnPobieranie.setText("Pobieranie");
                                    }
                                    Composite composite_6_1 = new Composite(composite_4_1, SWT.NONE);
                                    composite_6_1.setLayout(new RowLayout(SWT.VERTICAL));
                                    {
                                        processButton = new Button(composite_6_1, SWT.NONE);
                                        processButton.addSelectionListener(new SelectionAdapter() {

                                            @Override
                                            public void widgetSelected(SelectionEvent e) {
                                                processProject();
                                            }

                                            private void processProject() {
                                                ProjectProcesor p = new ProjectProcesor(text.getText(), btnPobieranie.getSelection());
                                                p.start();
                                            }
                                        });
                                        processButton.setText("Przelicz projekt");
                                    }
                                    Button button = new Button(composite_6_1, SWT.NONE);
                                    button.addSelectionListener(new SelectionAdapter() {

                                        @Override
                                        public void widgetSelected(SelectionEvent e) {
                                            alternateProcess();
                                        }
                                    });
                                    button.setText("Pe�ne przetwarzanie");
                                }
                            }
                            sashForm_1.setWeights(new int[] { 1 });
                        }
                    }
                    sashForm.setWeights(new int[] { 232, 249 });
                }
            }
            TabItem tbtmPojedynczaAnaliza = new TabItem(tabFolder, SWT.NONE);
            tbtmPojedynczaAnaliza.setText("Statystyki");
            Composite composite = new Composite(tabFolder, SWT.NONE);
            composite.setLayout(new FillLayout(SWT.HORIZONTAL));
            tbtmPojedynczaAnaliza.setControl(composite);
            Composite composite_1 = new Composite(composite, SWT.NONE);
            composite_1.setLayout(new RowLayout(SWT.VERTICAL));
            {
                Composite composite_2 = new Composite(composite_1, SWT.NONE);
                composite_2.setLayoutData(new RowData(231, 29));
                composite_2.setLayout(new RowLayout(SWT.HORIZONTAL));
                {
                    text_1 = new Text(composite_2, SWT.BORDER);
                    text_1.setLayoutData(new RowData(120, 16));
                }
                {
                    Button btnPokaStatystyki = new Button(composite_2, SWT.NONE);
                    btnPokaStatystyki.addSelectionListener(new SelectionAdapter() {

                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            getProjectFullInfo();
                        }

                        public void getProjectFullInfo() {
                            String projectUNIXName = text_1.getText();
                            if (statProvider == null) {
                                statProvider = new StatisticsProvider();
                            }
                            showProjectFullInfo(statProvider.getProjectFullInfo(projectUNIXName));
                        }
                    });
                    btnPokaStatystyki.setText("Pokaż statystyki");
                }
            }
            {
                Composite composite_2 = new Composite(composite_1, SWT.NONE);
                composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));
                composite_2.setLayoutData(new RowData(232, 300));
                {
                    text_2 = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);
                }
            }
            Composite composite_2 = new Composite(composite, SWT.NONE);
            composite_2.setLayout(new FillLayout(SWT.VERTICAL));
            {
                text_3 = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
            }
            Button btnOdwie = new Button(composite_2, SWT.NONE);
            btnOdwie.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    showStatusStats();
                }

                private void showStatusStats() {
                    if (statProvider == null) {
                        statProvider = new StatisticsProvider();
                    }
                    String stats = statProvider.getProjectStatusStats();
                    setTextValue(text_3, stats);
                }
            });
            btnOdwie.setText("Odśwież");
            TabItem tbtmPrezentacjaGraficzna = new TabItem(tabFolder, SWT.NONE);
            tbtmPrezentacjaGraficzna.setText("Administracja");
            Composite composite_2_1_1 = new Composite(tabFolder, SWT.NONE);
            composite_2_1_1.setLayout(new FillLayout(SWT.HORIZONTAL));
            tbtmPrezentacjaGraficzna.setControl(composite_2_1_1);
            Composite composite_3 = new Composite(composite_2_1_1, SWT.NONE);
            composite_3.setLayout(new RowLayout(SWT.HORIZONTAL));
            text_4 = new Text(composite_3, SWT.BORDER);
            text_4.setLayoutData(new RowData(187, 19));
            Button btnWykres = new Button(composite_3, SWT.NONE);
            btnWykres.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    String UNIXName = text_4.getText();
                    deleteAllInfo(UNIXName);
                }
            });
            btnWykres.setText("Usuń projekt z systemu");
            TabItem tbtmPrezentacjaGraficzna_1 = new TabItem(tabFolder, SWT.NONE);
            tbtmPrezentacjaGraficzna_1.setText("Prezentacja Graficzna");
            Composite composite_4 = new Composite(tabFolder, SWT.NONE);
            tbtmPrezentacjaGraficzna_1.setControl(composite_4);
            {
                Button btnGeneruj = new Button(composite_4, SWT.NONE);
                btnGeneruj.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        String dir = txtDmgr.getText();
                        String file = txtReportpng.getText();
                        MyChartData mcd = new MyChartData();
                        String xAxis = combo_1.getItem(combo_1.getSelectionIndex());
                        String yAxis = combo_2.getItem(combo_2.getSelectionIndex());
                        mcd.setDimensions(xAxis, yAxis);
                        mcd.execute();
                        if (btnUsuPrzypadkiBrzegowe.getSelection()) {
                            mcd.filter();
                        }
                        MyChart mc = new MyChart(mcd);
                        mc.generate(dir, file);
                    }
                });
                btnGeneruj.setBounds(10, 203, 68, 23);
                btnGeneruj.setText("Generuj");
            }
            {
                Group grpLokalizacja = new Group(composite_4, SWT.NONE);
                grpLokalizacja.setText("Lokalizacja");
                grpLokalizacja.setBounds(10, 117, 466, 80);
                {
                    Label lblDirectory = new Label(grpLokalizacja, SWT.NONE);
                    lblDirectory.setBounds(10, 20, 41, 13);
                    lblDirectory.setText("Katalog");
                }
                {
                    txtDmgr = new Text(grpLokalizacja, SWT.BORDER);
                    txtDmgr.setText("D:\\mgr\\java\\Ishima\\Reports\\");
                    txtDmgr.setBounds(52, 20, 337, 22);
                }
                {
                    Button btnOtwrz = new Button(grpLokalizacja, SWT.NONE);
                    btnOtwrz.setBounds(396, 19, 60, 23);
                    btnOtwrz.setText("Otwórz");
                }
                Label lblPlik = new Label(grpLokalizacja, SWT.NONE);
                lblPlik.setBounds(10, 57, 35, 13);
                lblPlik.setText("Plik");
                {
                    txtReportpng = new Text(grpLokalizacja, SWT.BORDER);
                    txtReportpng.setText("Report.png");
                    txtReportpng.setBounds(52, 51, 404, 19);
                }
            }
            {
                Group grpWykres = new Group(composite_4, SWT.NONE);
                grpWykres.setText("Wykres");
                grpWykres.setBounds(10, 10, 466, 101);
                {
                    Label lblOX = new Label(grpWykres, SWT.NONE);
                    lblOX.setBounds(10, 20, 49, 13);
                    lblOX.setText("Oś X");
                }
                combo_2 = new Combo(grpWykres, SWT.NONE);
                combo_2.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        if (combo_1.getSelectionIndex() >= 0 && combo_2.getSelectionIndex() >= 0) {
                            String xAxis = combo_1.getItem(combo_1.getSelectionIndex());
                            String yAxis = combo_2.getItem(combo_2.getSelectionIndex());
                            String fileName = yAxis + "(" + xAxis + ").png";
                            fileName = fileName.replace("/", "");
                            txtReportpng.setText(fileName);
                        }
                    }
                });
                combo_2.setBounds(238, 39, 208, 21);
                combo_2.setVisibleItemCount(10);
                {
                    Label lblOY = new Label(grpWykres, SWT.NONE);
                    lblOY.setBounds(238, 20, 49, 13);
                    lblOY.setText("Oś Y");
                }
                combo_1 = new Combo(grpWykres, SWT.NONE);
                combo_1.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        if (combo_1.getSelectionIndex() >= 0 && combo_2.getSelectionIndex() >= 0) {
                            String xAxis = combo_1.getItem(combo_1.getSelectionIndex());
                            String yAxis = combo_2.getItem(combo_2.getSelectionIndex());
                            String fileName = yAxis + "(" + xAxis + ").png";
                            fileName = fileName.replace("/", "");
                            txtReportpng.setText(fileName);
                        }
                    }
                });
                combo_1.setBounds(10, 39, 215, 21);
                combo_1.setVisibleItemCount(10);
                {
                    btnUsuPrzypadkiBrzegowe = new Button(grpWykres, SWT.CHECK);
                    btnUsuPrzypadkiBrzegowe.setBounds(10, 66, 144, 16);
                    btnUsuPrzypadkiBrzegowe.setSelection(true);
                    btnUsuPrzypadkiBrzegowe.setText("Usuń przypadki brzegowe");
                }
                {
                    for (String s : getAxisList()) {
                        combo_2.add(s);
                    }
                }
                {
                    for (String s : getAxisList()) {
                        combo_1.add(s);
                    }
                }
            }
        }
        shlIshimaProject.open();
        while (!shlIshimaProject.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    protected static void deleteAllInfo(String name) {
        SQLConnection con = new SQLConnection("settings.ini");
        SQLManager man = new SQLManager(con);
        man.removeProject(name);
    }

    protected static void toogleEraze() {
        if (erz == null) startErazing(); else stopErazing();
        setLabelText(label_8, erz);
    }

    protected static void toogleCleanUp() {
        if (cln == null) startCleanUp(); else stopCleaning();
        setLabelText(label_7, cln);
    }

    protected static void tooglePMD() {
        if (pmdTester == null) startPMD(); else stopPMD();
        setLabelText(label_6, pmdTester);
    }

    protected static void toogleCKJM() {
        if (ckjmTester == null) startCKJM(); else stopCKJM();
        setLabelText(label_5, ckjmTester);
    }

    protected static void toogleCAP() {
        if (capTester == null) startCAP(); else stopCAP();
        setLabelText(label_4, capTester);
    }

    protected static void toogleJavaNCSS() {
        if (javancssTester == null) startJavaNCSS(); else stopJavaNCSS();
        setLabelText(label_3, javancssTester);
    }

    protected static void toogleDownloader() {
        if (dwnldr == null) {
            startDownloading();
            setLabelText(label_2, dwnldr[0]);
        } else {
            stopDownloading();
            setLabelText(label_2, null);
        }
    }

    protected static void toogleCrawling() {
        if (crw == null) startCrawling(); else stopCrawling();
        setLabelText(label_1, crw);
    }

    protected static void alternateProcess() {
        AlternateProcessor ap = new AlternateProcessor(true);
        ap.start();
    }

    private static void stopCleaning() {
        cln = (Cleaner) stopProcess(cln);
    }

    private static Thread stopProcess(Thread process) {
        if (process != null) {
            process.interrupt();
        }
        return null;
    }

    private static void stopJavaNCSS() {
        javancssTester = (JavaNCSSTester) stopProcess(javancssTester);
    }

    private static void stopPMD() {
        pmdTester = (PMDTester) stopProcess(pmdTester);
    }

    private static void stopCKJM() {
        ckjmTester = (CKJMTester) stopProcess(ckjmTester);
    }

    private static void stopCAP() {
        capTester = (CAPTester) stopProcess(capTester);
    }

    private static void stopCrawling() {
        crw = (TroveListCrawler) stopProcess(crw);
        de = (DevelopExtractor) stopProcess(de);
    }

    private static void stopErazing() {
        erz = (ProjectErazer) stopProcess(erz);
    }

    private static void stopDownloading() {
        for (int i = 0; dwnldr != null && i < pobieranychProjektwMAX; i++) {
            dwnldr[i] = (Downloader) stopProcess(dwnldr[i]);
        }
        dwnldr = null;
    }

    private static void startCleanUp() {
        cln = new Cleaner();
        cln.setName("Ishima Projects Cleaner");
        cln.start();
    }

    private static void startErazing() {
        erz = new ProjectErazer();
        erz.setName("Ishima Projects Erazer");
        erz.start();
    }

    private static void startCKJM() {
        ckjmTester = new CKJMTester();
        ckjmTester.setName("CKJM Tester");
        ckjmTester.start();
    }

    private static void startPMD() {
        pmdTester = new PMDTester();
        pmdTester.setName("PMD Tester");
        pmdTester.start();
    }

    private static void startJavaNCSS() {
        javancssTester = new JavaNCSSTester();
        javancssTester.setName("JavaNCSS tester");
        javancssTester.start();
    }

    private static void startCAP() {
        capTester = new CAPTester();
        capTester.setName("CodeAnalyzerPro tester");
        capTester.start();
    }

    private static void startDownloading() {
        dwnldr = new Downloader[pobieranychProjektwMAX];
        for (int i = 0; i < pobieranychProjektwMAX; i++) {
            dwnldr[i] = new Downloader();
            dwnldr[i].setName("Ishima Downloader #" + i);
            dwnldr[i].start();
            System.out.println("Odpalono w�tek #" + i);
        }
    }

    protected static void startCrawling() {
        crw = new TroveListCrawler();
        crw.setName("TroveList Crawler");
        crw.start();
        de = new DevelopExtractor();
        de.setName("Develop Extractor");
        de.start();
    }

    public static void updateZbadanychProjektow(final Integer zbadanychProjektow) {
        MainForm.zbadanychProjektow = zbadanychProjektow;
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                lblZbadanychProjektw.setText("Zbadanych: " + zbadanychProjektow.toString());
            }
        });
    }

    public static void updateWPelniPrzetworzonych(final Integer wPelniPrzetworzonych) {
        MainForm.wPelniPrzetworzonych = wPelniPrzetworzonych;
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                lblWPeniPrzetworzonych.setText("W pe�ni przetworzonych: " + wPelniPrzetworzonych.toString());
            }
        });
    }

    public static void updateAktualnieBadany(final String aktualnieBadany) {
        MainForm.aktualnieBadany = aktualnieBadany;
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                lblbrak.setText(aktualnieBadany);
            }
        });
    }

    public static void setDownloadInProgress(Boolean downloadInProgress) {
        MainForm.downloadInProgress = downloadInProgress;
    }

    public static Boolean getDownloadInProgress() {
        return downloadInProgress;
    }

    public static void add2Log(final String msg) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                listLog.add(addDate(msg));
                listLog.setSelection(listLog.getItemCount() - 1);
            }
        });
    }

    public static void setProcessing(boolean processingArg) {
        processing = processingArg;
        if (processing) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    processButton.setText("Przeliczanie...");
                    processButton.setEnabled(false);
                }
            });
        } else {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    processButton.setText("Przelicz");
                    processButton.setEnabled(true);
                }
            });
        }
    }

    public static void setLabelText(final Label lbl, final Thread state) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                String text;
                Color c;
                if (state == null) {
                    text = "OFF";
                    c = new Color(Display.getDefault(), 0, 0, 0);
                } else {
                    text = "ON";
                    c = new Color(Display.getDefault(), 60, 240, 60);
                }
                lbl.setText(text);
                lbl.setForeground(c);
            }
        });
    }

    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String addDate(String message) {
        return "[" + getDateTime() + "] " + message;
    }

    public static void showProjectFullInfo(final String info) {
        setTextValue(text_2, info);
    }

    public static void showStatusStats(final String info) {
        setTextValue(text_3, info);
    }

    public static void setTextValue(final Text t, final String info) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                t.setText(info);
            }
        });
    }

    public static void updateResults() {
        SQLConnection con = new SQLConnection("settings.ini");
        SQLManager man = new SQLManager(con);
        Integer zbadanych = man.getCompletedSize();
        MainForm.updateZbadanychProjektow(zbadanych);
        Integer uzytecznych = man.getUsefulSize();
        MainForm.updateWPelniPrzetworzonych(uzytecznych);
    }

    public static void setAxisList(String[] axisList) {
        MainForm.axisList = axisList;
    }

    public static String[] getAxisList() {
        return axisList;
    }
}

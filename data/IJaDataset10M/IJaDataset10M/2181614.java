package LabDB;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import com.sun.media.sound.JavaSoundAudioClip;
import LabDBComponents.VTextIcon;

public class LabDBProjectReport extends JPanel {

    private LabDBAccess db;

    private String user, userID, userRealName, projectID;

    private SimpleDateFormat dFull;

    private JEditorPane reportPane;

    private DefaultStyledDocument doc;

    private StyleContext sc;

    private LabDBProjectReportActionListener al;

    private JButton createReportBtn, saveReportBtn;

    private JCheckBox involvedPersonsCheckBox, fundingCheckBox, subgroupCheckBox, statusCheckBox;

    private JCheckBox includeConceptCheckBox, includeBackgroundCheckBox, includeIdeaCheckBox, includeDiscussionCheckBox, includeTodoCheckBox, includeOtherCheckBox;

    private JCheckBox includeSetupCheckBox, includeHardwareCheckBox, includeMethodsCheckBox, includeExperimentsCheckBox, includeStimuliCheckBox;

    private JCheckBox includeItemPurposeCheckBox, includeItemVendorCheckBox, includeItemFundingCheckBox, includeItemOwnerCheckBox, includeItemTextCheckBox, includeItemInventoryCheckBox, includeItemCategoryCheckBox;

    private JCheckBox includeDiaryCheckBox;

    private JTabbedPane tabPane;

    public LabDBProjectReport(LabDBAccess db) {
        super(new BorderLayout());
        dFull = new SimpleDateFormat("yyyy-MM-dd");
        projectID = "-1";
        this.db = db;
        user = db.getCurrentUser();
        userID = db.getColumnValue("persons", "personID", "userName = '" + user + "'").toString();
        userRealName = db.getColumnValue("persons p", "CONCAT(firstName,' ',lastName)", "p.personID = '" + userID + "'").toString();
        al = new LabDBProjectReportActionListener();
        this.setBackground(Color.white);
        setGUI(1000, 500);
        setVisible(true);
    }

    public void setProject(String projectID) {
        this.projectID = projectID;
        if (projectID.equals("-1")) {
            createReportBtn.setEnabled(false);
            saveReportBtn.setEnabled(false);
            projectID = "-1";
            reportPane.setText("");
        } else {
            createReportBtn.setEnabled(true);
            saveReportBtn.setEnabled(true);
            reportPane.setText("");
        }
    }

    private void setGUI(int width, int height) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(createTabPane());
        splitPane.add(createCenterPanel());
        this.add(splitPane, BorderLayout.CENTER);
        this.add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JTabbedPane createTabPane() {
        tabPane = new JTabbedPane(JTabbedPane.LEFT);
        tabPane.setBackground(Color.white);
        tabPane.setBorder(BorderFactory.createTitledBorder("include options"));
        tabPane.addTab("", new VTextIcon(this, "content", VTextIcon.ROTATE_LEFT), createDocumentsIncludeTab());
        tabPane.addTab("", new VTextIcon(this, "data details", VTextIcon.ROTATE_LEFT), createDataDetailsTab());
        return tabPane;
    }

    private JScrollPane createDocumentsIncludeTab() {
        involvedPersonsCheckBox = new JCheckBox("persons", true);
        fundingCheckBox = new JCheckBox("funding", true);
        subgroupCheckBox = new JCheckBox("subgroup", true);
        statusCheckBox = new JCheckBox("status", true);
        JPanel projectDetailsPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        projectDetailsPanel.setBackground(Color.white);
        projectDetailsPanel.setBorder(BorderFactory.createTitledBorder("project details"));
        projectDetailsPanel.add(involvedPersonsCheckBox);
        projectDetailsPanel.add(fundingCheckBox);
        projectDetailsPanel.add(subgroupCheckBox);
        projectDetailsPanel.add(statusCheckBox);
        includeConceptCheckBox = new JCheckBox("concept", true);
        includeBackgroundCheckBox = new JCheckBox("background", true);
        includeIdeaCheckBox = new JCheckBox("ideas", true);
        includeDiscussionCheckBox = new JCheckBox("discussions", true);
        includeTodoCheckBox = new JCheckBox("todos", true);
        includeOtherCheckBox = new JCheckBox("other", true);
        includeMethodsCheckBox = new JCheckBox("methods", true);
        JPanel documentDetailsPanel = new JPanel(new GridLayout(8, 1, 0, 0));
        documentDetailsPanel.setBackground(Color.white);
        documentDetailsPanel.setBorder(BorderFactory.createTitledBorder("documents"));
        documentDetailsPanel.add(includeConceptCheckBox);
        documentDetailsPanel.add(includeBackgroundCheckBox);
        documentDetailsPanel.add(includeIdeaCheckBox);
        documentDetailsPanel.add(includeDiscussionCheckBox);
        documentDetailsPanel.add(includeTodoCheckBox);
        documentDetailsPanel.add(includeOtherCheckBox);
        documentDetailsPanel.add(includeMethodsCheckBox);
        includeExperimentsCheckBox = new JCheckBox("experiments", true);
        includeStimuliCheckBox = new JCheckBox("stimuli", true);
        includeSetupCheckBox = new JCheckBox("setup description", true);
        includeSetupCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                includeHardwareCheckBox.doClick();
            }
        });
        includeHardwareCheckBox = new JCheckBox("hardware descr.", true);
        includeHardwareCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                includeItemCategoryCheckBox.setSelected(includeHardwareCheckBox.isSelected());
                includeItemPurposeCheckBox.setSelected(includeHardwareCheckBox.isSelected());
                includeItemVendorCheckBox.setSelected(includeHardwareCheckBox.isSelected());
                includeItemFundingCheckBox.setSelected(includeHardwareCheckBox.isSelected());
                includeItemOwnerCheckBox.setSelected(includeHardwareCheckBox.isSelected());
                includeItemTextCheckBox.setSelected(includeHardwareCheckBox.isSelected());
                includeItemInventoryCheckBox.setSelected(includeHardwareCheckBox.isSelected());
            }
        });
        JPanel experimentDetailsPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        experimentDetailsPanel.setBackground(Color.white);
        experimentDetailsPanel.setBorder(BorderFactory.createTitledBorder("experiments"));
        experimentDetailsPanel.add(includeExperimentsCheckBox);
        experimentDetailsPanel.add(includeStimuliCheckBox);
        experimentDetailsPanel.add(includeSetupCheckBox);
        experimentDetailsPanel.add(includeHardwareCheckBox);
        includeItemPurposeCheckBox = new JCheckBox("purpose", true);
        includeItemCategoryCheckBox = new JCheckBox("category", true);
        includeItemVendorCheckBox = new JCheckBox("vendor", true);
        includeItemFundingCheckBox = new JCheckBox("funding", true);
        includeItemOwnerCheckBox = new JCheckBox("owner", true);
        includeItemTextCheckBox = new JCheckBox("description", true);
        includeItemInventoryCheckBox = new JCheckBox("inventory number", true);
        JPanel itemDetailsPanel = new JPanel(new GridLayout(8, 1, 0, 0));
        itemDetailsPanel.setBackground(Color.white);
        itemDetailsPanel.setBorder(BorderFactory.createTitledBorder("hardware"));
        itemDetailsPanel.add(includeItemInventoryCheckBox);
        itemDetailsPanel.add(includeItemCategoryCheckBox);
        itemDetailsPanel.add(includeItemPurposeCheckBox);
        itemDetailsPanel.add(includeItemVendorCheckBox);
        itemDetailsPanel.add(includeItemFundingCheckBox);
        itemDetailsPanel.add(includeItemOwnerCheckBox);
        itemDetailsPanel.add(includeItemTextCheckBox);
        includeDiaryCheckBox = new JCheckBox("diary entries", true);
        includeDiaryCheckBox.setToolTipText("Include project related diary entries. Will only work for your own entries.");
        JPanel diaryDetailsPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        diaryDetailsPanel.setBackground(Color.white);
        diaryDetailsPanel.setBorder(BorderFactory.createTitledBorder("diary"));
        diaryDetailsPanel.add(includeDiaryCheckBox);
        JPanel includePanel = new JPanel();
        includePanel.setLayout(new BoxLayout(includePanel, BoxLayout.PAGE_AXIS));
        includePanel.setBackground(Color.white);
        includePanel.add(projectDetailsPanel);
        includePanel.add(documentDetailsPanel);
        includePanel.add(experimentDetailsPanel);
        includePanel.add(itemDetailsPanel);
        includePanel.add(diaryDetailsPanel);
        JScrollPane pane = new JScrollPane(includePanel);
        pane.setBackground(Color.white);
        pane.setPreferredSize(new Dimension(130, 400));
        return pane;
    }

    private JPanel createDataDetailsTab() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        return panel;
    }

    private JPanel createCenterPanel() {
        reportPane = new JEditorPane();
        reportPane.setBackground(Color.white);
        reportPane.setContentType("text/html");
        reportPane.setFont(new Font("Serif", Font.PLAIN, 12));
        reportPane.setCaretPosition(0);
        reportPane.setEditable(false);
        JScrollPane reportScrollPane = new JScrollPane(reportPane);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.white);
        centerPanel.setPreferredSize(new Dimension(200, 500));
        centerPanel.setBorder(BorderFactory.createTitledBorder("report preview"));
        centerPanel.add(reportScrollPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createButtonPanel() {
        createReportBtn = new JButton("create report");
        createReportBtn.setToolTipText("create report, may take a moment depending on the number of datasets");
        createReportBtn.setFont(new Font("SansSerif", Font.BOLD, 10));
        createReportBtn.setActionCommand("createReportBtn");
        createReportBtn.setEnabled(false);
        createReportBtn.addActionListener(al);
        saveReportBtn = new JButton("save report");
        saveReportBtn.setToolTipText("save report to disc");
        saveReportBtn.setFont(new Font("SansSerif", Font.BOLD, 10));
        saveReportBtn.setActionCommand("saveBtn");
        saveReportBtn.setEnabled(false);
        saveReportBtn.addActionListener(al);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.white);
        ((FlowLayout) panel.getLayout()).setVgap(2);
        panel.add(createReportBtn);
        panel.add(saveReportBtn);
        return panel;
    }

    private void saveBtnPressed() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setSelectedFile(new File("ProjectReport.html"));
            int state = chooser.showSaveDialog(null);
            File file = chooser.getSelectedFile();
            if ((file != null) && (state == JFileChooser.APPROVE_OPTION)) {
                FileOutputStream fStream = new FileOutputStream(file);
                fStream.write(reportPane.getText().getBytes());
                fStream.close();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void createReportBtnPressed() {
        if (projectID.equals("-1")) {
            return;
        }
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(hourglassCursor);
        String head = "", body = "", report = "<html>";
        body = "<body>";
        body = body + createProjectPart();
        body = body + createDocumentsPart();
        body = body + createExperimentsPart();
        body = body + createDiaryPart();
        body = body + createDataPart();
        body = body + "</body>";
        report = report + head + body + "</html>";
        reportPane.setText(report);
        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(normalCursor);
    }

    private String createProjectPart() {
        String text = "";
        text = text + "<h1>" + db.getColumnValue("projects", "name", "projectID = '" + projectID + "'") + "</h1>";
        text = text + "<p STYLE=\"margin-left: 1cm\">" + db.getColumnValue("projects", "description", "projectID = '" + projectID + "'") + "</p>";
        text = text + "<p STYLE=\"margin-left: 1cm\"><b>responsible investigator: </b><br>" + db.getColumnValue("persons p,projects pr", "CONCAT(p.lastName,', ',p.firstName)", "p.personID = pr.RI AND pr.projectID = '" + projectID + "'");
        if (involvedPersonsCheckBox.isSelected()) {
            Object[] persons = db.getColumnValues("projects pr,persons p, involvements i", "CONCAT(p.lastName,', ',p.firstName)", "p.personID = i.personID AND pr.RI != p.personID AND pr.projectID = i.projectID AND pr.projectID ='" + projectID + "'");
            if (persons != null && persons.length > 0) {
                text = text + "<p STYLE=\"margin-left: 1cm\"><b>further involved persons: </b><br>";
                for (int i = 0; i < persons.length; i++) {
                    text = text + persons[i];
                    if (i < persons.length - 1) {
                        text = text + "; ";
                    }
                }
                text = text + "</p>";
            }
        }
        if (subgroupCheckBox.isSelected()) {
            text = text + "<p STYLE=\"margin-left: 1cm\"><b>assigned to subgroup: </b><br>";
            text = text + db.getColumnValue("projects p, subgroups s", "s.subgroupname", "p.subgroup = s.subgroupID AND projectID = '" + projectID + "'") + "</p>";
        }
        if (fundingCheckBox.isSelected()) {
            text = text + "<p STYLE=\"margin-left: 1cm\"><b>funding by: </b><br>";
            text = text + db.getColumnValue("projects", "funding", "projectID = '" + projectID + "'");
        }
        if (statusCheckBox.isSelected()) {
            text = text + "<p STYLE=\"margin-left: 1cm\"><b>status by " + db.executeCommand("select curdate();") + ":</b><br>";
            text = text + db.getColumnValue("projects", "status", "projectID = '" + projectID + "'") + "</p>";
        }
        return text;
    }

    private String createDocumentsPart() {
        String text = "";
        if (includeConceptCheckBox.isSelected()) {
            Object[] documents = db.getColumnValues("documents", "ID", "type ='concept' AND projectID = '" + projectID + "'");
            if (documents != null && documents.length > 0) {
                text = text + "<h2>Concept documents: </h2>";
                text = text + writeDocuments(documents);
            }
        }
        if (includeBackgroundCheckBox.isSelected()) {
            Object[] documents = db.getColumnValues("documents", "ID", "type ='background' AND projectID = '" + projectID + "'");
            if (documents != null && documents.length > 0) {
                text = text + "<h2>Background documents: </h2>";
                text = text + writeDocuments(documents);
            }
        }
        if (includeDiscussionCheckBox.isSelected()) {
            Object[] documents = db.getColumnValues("documents", "ID", "type ='discussion' AND projectID = '" + projectID + "'");
            if (documents != null && documents.length > 0) {
                text = text + "<h2>Discussions: </h2>";
                text = text + writeDocuments(documents);
            }
        }
        if (includeIdeaCheckBox.isSelected()) {
            Object[] documents = db.getColumnValues("documents", "ID", "type ='idea' AND projectID = '" + projectID + "'");
            if (documents != null && documents.length > 0) {
                text = text + "<h2>Ideas: </h2>";
                text = text + writeDocuments(documents);
            }
        }
        if (includeTodoCheckBox.isSelected()) {
            Object[] documents = db.getColumnValues("documents", "ID", "type ='todo' AND projectID = '" + projectID + "'");
            if (documents != null && documents.length > 0) {
                text = text + "<h2>Todos: </h2>";
                text = text + writeDocuments(documents);
            }
        }
        if (includeOtherCheckBox.isSelected()) {
            Object[] documents = db.getColumnValues("documents", "ID", "type ='other' AND projectID = '" + projectID + "'");
            if (documents != null && documents.length > 0) {
                text = text + "<h2>Other documents: </h2>";
                text = text + writeDocuments(documents);
            }
        }
        if (includeMethodsCheckBox.isSelected()) {
            Object[] documents = db.getColumnValues("documents", "ID", "type ='methods' AND projectID = '" + projectID + "'");
            if (documents != null && documents.length > 0) {
                text = text + "<h2>methodological documents: </h2>";
                text = text + writeDocuments(documents);
            }
        }
        return text;
    }

    private String writeDocuments(Object[] documents) {
        String text = "";
        for (int i = 0; i < documents.length; i++) {
            text = text + "<p STYLE=\"margin-left: 1cm\"><b>" + db.getColumnValue("documents", "name", "id = '" + documents[i] + "'") + "</b>";
            text = text + " created by " + db.getColumnValue("documents d, persons p", "CONCAT(p.firstName,' ',p.lastName)", "p.personid = d.authorID AND d.ID ='" + documents[i] + "'");
            text = text + ", " + db.getColumnValue("documents", "date", "id = '" + documents[i] + "'") + "</p>";
            text = text + "<p STYLE=\"margin-left: 1cm\">" + db.getColumnValue("documents", "text", "id = '" + documents[i] + "'") + "</p>";
        }
        return text;
    }

    private String createExperimentsPart() {
        String text = "";
        if (includeExperimentsCheckBox.isSelected()) {
            Object[] experiments = db.getColumnValues("experimentRegister", "experimentID", "projectID = '" + projectID + "'");
            if (experiments != null && experiments.length > 0) {
                text = text + "<h2>Experiments: </h2>";
                text = text + writeExperiment(experiments);
            }
            if (includeSetupCheckBox.isSelected()) {
                Object[] setups = db.getDistinctColumnValues("experiments e, experimentRegister er", "e.setupID", "e.id = er.experimentID AND projectID = '" + projectID + "'");
                if (setups != null && setups.length > 0) {
                    text = text + "<h2>Setups: </h2>";
                    text = text + writeSetups(setups);
                }
            }
            if (includeHardwareCheckBox.isSelected()) {
                Object[] items = db.getDistinctColumnValues("experiments e, experimentRegister er, hardwareRegister hr", "hr.hardwareID", "e.id = er.experimentID AND projectID = '" + projectID + "' AND e.setupID = hr.setupID");
                if (items != null && items.length > 0) {
                    text = text + "<h2>Hardware: </h2>";
                    text = text + writeItems(items);
                }
            }
        }
        return text;
    }

    private String writeExperiment(Object[] documents) {
        String text = "";
        for (int i = 0; i < documents.length; i++) {
            text = text + "<p><b>" + db.getColumnValue("experiments", "name", "id = '" + documents[i] + "'") + "</b>";
            text = text + " created by " + db.getColumnValue("experiments e, persons p", "CONCAT(p.firstName,' ',p.lastName)", "p.personid = e.authorID AND e.ID ='" + documents[i] + "'");
            text = text + ", " + db.getColumnValue("experiments", "date", "id = '" + documents[i] + "'") + "</p>";
            text = text + "<p STYLE=\"margin-left: 1cm\"> <b>designed in project: </b>" + db.getColumnValue("projects p, experiments e", "p.name", "e.originalProjectID = p.projectID AND e.id= '" + documents[i] + "'") + "</p>";
            text = text + "<p STYLE=\"margin-left: 1cm\"> <b>using setup: </b>" + db.getColumnValue("experiments e, setups s", "s.name", "e.setupID = s.setupID AND e.id= '" + documents[i] + "'") + "</p>";
            text = text + "<p STYLE=\"margin-left: 1cm\">" + db.getColumnValue("experiments", "text", "id = '" + documents[i] + "'") + "</p>";
        }
        return text;
    }

    private String writeSetups(Object[] setups) {
        String text = "";
        for (int i = 0; i < setups.length; i++) {
            text = text + "<p><b>" + db.getColumnValue("setups", "name", "setupid = '" + setups[i] + "'") + "</b></p>";
            text = text + "<p STYLE=\"margin-left: 1cm\">" + db.getColumnValue("setups", "description", "setupid = '" + setups[i] + "'") + "</p>";
            text = text + "<p STYLE=\"margin-left: 1cm\"><b>Used Hardware:</b><br>";
            Object[] items = db.getColumnValues("hardwareRegister h", "h.hardwareID", "h.setupID = '" + setups[i] + "'");
            for (int j = 0; j < items.length; j++) {
                text = text + db.getColumnValue("hardware", "CONCAT(name,' - ',purpose)", "hardwareID='" + items[j] + "'") + "<br>";
            }
            text = text + "</p>";
        }
        return text;
    }

    private String writeItems(Object[] items) {
        String text = "";
        for (int i = 0; i < items.length; i++) {
            text = text + "<p STYLE=\"margin-left: 1cm\"><b>" + db.getColumnValue("hardware", "name", "hardwareid = '" + items[i] + "'") + "</b><br>";
            if (includeItemInventoryCheckBox.isSelected()) {
                text = text + "<b>inventory number: </b>" + db.getColumnValue("hardware", "inventoryNO", "hardwareid = '" + items[i] + "'") + "<br>";
            }
            if (includeItemCategoryCheckBox.isSelected()) {
                text = text + "<b>category: </b>" + db.getColumnValue("hardware", "category", "hardwareID ='" + items[i] + "'") + "<br>";
            }
            if (includeItemPurposeCheckBox.isSelected()) {
                text = text + "<b>purpose: </b>" + db.getColumnValue("hardware", "purpose", "hardwareID ='" + items[i] + "'") + "<br>";
            }
            if (includeItemVendorCheckBox.isSelected()) {
                text = text + "<b>vendor: </b>" + db.getColumnValue("hardware", "builtBy", "hardwareid = '" + items[i] + "'") + "<br>";
            }
            if (includeItemFundingCheckBox.isSelected()) {
                text = text + "<b>funding: </b>" + db.getColumnValue("hardware h, funds f", "f.name", "f.ID = h.fundingID AND hardwareid = '" + items[i] + "'") + "<br>";
            }
            if (includeItemOwnerCheckBox.isSelected()) {
                text = text + "<b>owner: </b>" + db.getColumnValue("hardware", "owner", "hardwareid = '" + items[i] + "'") + "<br>";
            }
            if (includeItemTextCheckBox.isSelected()) {
                text = text + "<b>description:</b><br>" + db.getColumnValue("hardware", "description", "hardwareid = '" + items[i] + "'");
            }
            text = text + "</p>";
        }
        return text;
    }

    private String createDiaryPart() {
        if (!includeDiaryCheckBox.isSelected()) {
            return "";
        }
        Object[][] values = db.getColumnValues("diary", new String[] { "date", "text" }, "author = '" + userID + "'" + " AND project = '" + projectID + "' ORDER BY date");
        if (values == null) {
            return "";
        }
        String text = "<h2>Diary entries</h2>";
        for (int i = 0; i < values.length; i++) {
            text = text + "<p STYLE=\"margin-left: 1cm\"><b>" + values[i][0] + "</b><br></p>";
            text = text + "<p STYLE=\"margin-left: 1.5cm\">" + values[i][1] + "<br></p>";
            text = text + "<br>" + "<br>";
        }
        return text;
    }

    private String createDataPart() {
        String text = "<h2>Data</h2>";
        Object[][] subjectsTemp = db.getColumnValues("experimentRegister er, datasets d, subjects s", new String[] { "DISTINCT s.subjectID", "s.name" }, "d.subjectID is not null AND d.subjectID = s.subjectID AND " + "er.projectID = '" + projectID + "'");
        Hashtable<String, String> subjectHash = new Hashtable<String, String>();
        if (subjectsTemp != null) {
            for (int i = 0; i < subjectsTemp.length; i++) {
                Object[] temp = subjectsTemp[i];
                subjectHash.put(temp[0].toString(), temp[1].toString());
            }
        }
        Object[][] cellsTemp = db.getColumnValues("experimentRegister er, datasets d, cells c", new String[] { "DISTINCT c.cellID", "c.name" }, "d.cellID is not null AND d.cellId = c.cellID AND " + "er.projectID = '" + projectID + "'");
        Hashtable<String, String> cellHash = new Hashtable<String, String>();
        if (cellsTemp != null) {
            for (int i = 0; i < cellsTemp.length; i++) {
                Object[] temp = cellsTemp[i];
                cellHash.put(temp[0].toString(), temp[1].toString());
            }
        }
        String[] cols = { "d.datasetID", "d.name", "d.subjectID", "d.cellID", "d.experimentID", "e.name", "concat(d.datafolder,'\\\\',d.filename)", "d.recDate", "d.comment", "d.quality", "d.experimenterID" };
        Object[][] values = db.getColumnValues("datasets d, experimentRegister er, experiments e", cols, "d.experimentID = er.experimentID AND er.projectID ='" + projectID + "' AND e.id = d.experimentID " + "ORDER BY d.experimentID,d.subjectID,d.cellID,d.name");
        if (values != null) {
            String expID = "", cellID = "", subjectID = "";
            DefaultMutableTreeNode expNode = null, subjectNode = null, cellNode = null;
            for (int i = 0; i < values.length; i++) {
                Object[] temp = values[i];
                if (expID.isEmpty() || !expID.equals(temp[4].toString())) {
                    expID = temp[4].toString();
                    text = text + "<p font-size:large><br><b>Experiment: </b>" + db.getColumnValue("experiments", "name", "id = '" + expID + "'") + "</p>";
                    subjectID = "";
                    cellID = "";
                }
                if ((subjectID.isEmpty() || !subjectID.equals(temp[2].toString())) && !temp[2].toString().isEmpty()) {
                    subjectID = temp[2].toString();
                    Object[] subjectInfo = db.getSingleRowValues("subjects", new String[] { "species", "gender", "birthday", "comments" }, "subjectID='" + subjectID + "'");
                    text = text + "<p STYLE=\"margin-left: 1cm\"><b>Subject: </b>" + db.getColumnValue("subjects", "name", "subjectid = '" + subjectID + "'") + "</p>";
                    text = text + "<table width=\"100%\" border-style:none STYLE=\"margin-left: 1.75cm;font-size:small;border-style:solid\">" + "<colgroup>" + "<col width=\"4*\">" + "<col width=\"4*\">" + "<col width=\"4*\">" + "<col width=\"4*\">" + "</colgroup>" + "<tr>" + "<td><b>species</b></td>" + "<td>" + subjectInfo[0] + "</td>" + "<td><b>gender</b></td>" + "<td>" + subjectInfo[1] + "</td>" + "</tr>" + "<tr>" + "<td><b>brithday</b></td>" + "<td>" + subjectInfo[2] + "</td>" + "<td>&#160;</td>" + "<td>&#160;</td>" + "</tr>" + "<tr>" + "<td colspan=\"4\">" + subjectInfo[3] + "</td>" + "</tr>" + "</table>" + "<br>";
                } else if (temp[2].toString().isEmpty()) {
                    subjectNode = null;
                }
                if ((cellID.isEmpty() || !cellID.equals(temp[3].toString())) && !temp[3].toString().isEmpty()) {
                    cellID = temp[3].toString();
                    Object[] cellInfo = db.getSingleRowValues("cells", new String[] { "name", "type", "comments" }, "cellID='" + cellID + "'");
                    text = text + "<p STYLE=\"margin-left: 1cm\"><b>Cell: </b>" + cellInfo[0] + "</p>";
                    text = text + "<table width=\"100%\" border-style:none STYLE=\"margin-left: 1.75cm;font-size:small;border-style:solid\">" + "<colgroup>" + "<col width=\"4*\">" + "<col width=\"4*\">" + "<col width=\"4*\">" + "<col width=\"4*\">" + "</colgroup>" + "<tr>" + "<td><b>type</b></td>" + "<td>" + cellInfo[1] + "</td>" + "<td>&#160;</td>" + "<td>&#160;</td>" + "</tr>" + "<tr>" + "<td colspan=\"4\">" + cellInfo[2] + "</td>" + "</tr>" + "</table>" + "<br>";
                    text = text + "<p STYLE=\"margin-left: 1.75cm\"><b>Datasets: </b></p>";
                } else if (temp[3].toString().isEmpty()) {
                    cellNode = null;
                }
                text = text + "<table width=\"100%\" border-style:dotted border=\"1px\" STYLE=\"margin-left: 2cm;font-size:small;border-style:solid\">" + "<colgroup>" + "<col width=\"5*\">" + "<col width=\"5*\">" + "<col width=\"5*\">" + "<col width=\"5*\">" + "<col width=\"5*\">" + "</colgroup>" + writeDataset(temp) + "</table>";
            }
        }
        return text;
    }

    private String writeDataset(Object[] dataInfo) {
        String text = "<tr>" + "<td colspan=\"2\"><b>" + dataInfo[1] + "</b></td>" + "<td>ID: " + dataInfo[0] + "</td>" + "<td> Quality: " + dataInfo[9] + "</td>" + "<td> date: " + dataInfo[7] + "</td>" + "</tr>";
        return text;
    }

    class LabDBProjectReportActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("saveBtn")) {
                saveBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("createReportBtn")) {
                createReportBtnPressed();
            }
        }
    }
}

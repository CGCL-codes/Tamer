package org.kubiki.groupware;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.text.*;
import org.kubiki.base.*;
import org.kubiki.gui.*;
import org.kubiki.xml.*;
import org.kubiki.util.*;
import org.kubiki.ide.*;
import javax.mail.*;
import javax.mail.internet.*;

class EMLViewer extends AbstractApplication {

    String home;

    JTabbedPane jtp;

    String filename;

    PropertyWindow propertyWindow;

    PropertyWindow searchWindow;

    ObjectList mimeTypeList, searchList;

    StatusBar statusBar;

    MailMessageIndex index = null;

    MailMessageIndex searchIndex = null;

    MailMessageReference searchObject = null;

    public EMLViewer() {
        addProperty("RepositoryRootURL", "list", "", false, "Webrepository Basis URL");
        addProperty("width", "Integer", "0", true, "");
        addProperty("height", "Integer", "0", true, "");
        ObjectCollection oc = addObjectCollection("MailMessage", "MailMessage");
        oc.setSaved(false);
        oc = addObjectCollection("MimeTypes", "ConfigValue");
        getProperty("name").setHidden(true);
        init();
        parser = new XMLParser(this);
        String configuration = openFile(config_file);
        XMLElement root = parser.parseString(configuration);
        root.getSubelements();
        mainFrame = new BasicFrame();
        mainFrame.setTitle("EMLViewer");
        mainFrame.setLayout(null);
        jtp = new JTabbedPane();
        jtp.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight() - 25);
        mainFrame.getContentPane().add(jtp);
        statusBar = new StatusBar();
        statusBar.setBounds(0, mainFrame.getHeight() - 25, mainFrame.getWidth(), 25);
        mainFrame.getContentPane().add(statusBar);
        mainFrame.setVisible(true);
        mainFrame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                mainFrame.dispose();
                System.exit(0);
            }
        });
        mainFrame.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent evt) {
                resizeComponent();
            }

            public void componentHidden(ComponentEvent evt) {
            }

            public void componentShown(ComponentEvent evt) {
            }

            public void componentMoved(ComponentEvent evt) {
            }
        });
        JMenuBar jMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Datei");
        JMenu viewMenu = new JMenu("Ansicht");
        JMenu helpMenu = new JMenu("Hilfe");
        JMenuItem openMenu = new JMenuItem("Oeffnen");
        openMenu.addActionListener(this);
        fileMenu.add(openMenu);
        JMenuItem propMenu = new JMenuItem("Einstellungen");
        propMenu.addActionListener(this);
        fileMenu.add(propMenu);
        JMenuItem searchMenu = new JMenuItem("Suche");
        searchMenu.addActionListener(this);
        fileMenu.add(searchMenu);
        JMenuItem indexMenu = new JMenuItem("EML-Dateien indexieren");
        indexMenu.addActionListener(this);
        fileMenu.add(indexMenu);
        JMenuItem saveMenu = new JMenuItem("Text speichern unter");
        saveMenu.addActionListener(this);
        JMenuItem printMenu = new JMenuItem("Druck-Version");
        printMenu.addActionListener(this);
        fileMenu.add(printMenu);
        JMenuItem quitMenu = new JMenuItem("Beenden");
        quitMenu.addActionListener(this);
        fileMenu.add(quitMenu);
        JMenuItem changeViewMenu = new JMenuItem("Ausgeblendete Elemente anzeigen");
        changeViewMenu.addActionListener(this);
        JMenuItem openHelpMenu = new JMenuItem("Hilfe �ffnen");
        openHelpMenu.addActionListener(this);
        helpMenu.add(openHelpMenu);
        jMenuBar.add(fileMenu);
        jMenuBar.add(helpMenu);
        mainFrame.setJMenuBar(jMenuBar);
        setProperty("lastusedpath", System.getProperty("user.home"));
        FileChangeListener fcl = new FileChangeListener(this);
        new Thread(fcl).start();
        readConfigFile();
        initObject();
        mainFrame.pack();
        mainFrame.setSize(1024, 700);
        if (getInt("width") > 0 && getInt("width") > 0) {
            mainFrame.setSize(getInt("width"), getInt("height"));
        } else {
            mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        mainFrame.repaint();
        mainFrame.toFront();
        resizeComponent();
    }

    public void resizeComponent() {
        jtp.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight() - 70);
        for (int i = 0; i < jtp.getTabCount(); i++) {
            MailView mv = (MailView) jtp.getComponentAt(i);
            mv.resizeComponent();
        }
        mainFrame.validate();
        statusBar.setBounds(0, mainFrame.getHeight() - 70, mainFrame.getWidth(), 25);
    }

    public void loadData() {
        String indexFile = openFile(home + "/.EMLViewer/data/MailMessageIndex.xml");
        if (indexFile.length() == 0) {
            index = new MailMessageIndex();
            index.setParent(this);
        } else {
            XMLElement root = parser.parseString(indexFile);
            root.getSubelements();
        }
    }

    public void saveData() {
        if (index != null) {
            saveFile(home + "/.EMLViewer/data/MailMessageIndex.xml", index.getXMLString());
        }
    }

    public void init() {
        home = System.getProperty("user.home").replace("\\", "/");
        config_file = home + "/.EMLViewer/EMLViewerProperties.xml";
        File homedir = new File(home + "/.EMLViewer");
        File piddir = new File(home + "/.EMLViewer/PID");
        File tempdir = new File(home + "/.EMLViewer/temp");
        File datadir = new File(home + "/.EMLViewer/data");
        if (!homedir.exists()) {
            homedir.mkdirs();
        }
        if (!piddir.exists()) {
            piddir.mkdirs();
        }
        if (!tempdir.exists()) {
            tempdir.mkdirs();
        }
        if (!datadir.exists()) {
            datadir.mkdirs();
        }
    }

    public void initObject() {
        Vector v = getObjects("MimeTypes");
        for (int i = 0; i < v.size(); i++) {
            ConfigValue cv = (ConfigValue) v.elementAt(i);
            cv.getProperty("Value").setType("File");
        }
    }

    public void checkFileChanged() {
        String newfilename = openFile(home + "/.EMLViewer/PID/pid");
        if (!newfilename.equals(filename)) {
            openMessage(newfilename);
        }
    }

    public void readConfigFile() {
        System.out.println("Configfile:" + basedir);
        String config = openFile(basedir + "/emlviewer.cfg");
        String[] lines = config.split("\n|\n\r");
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
            String[] args = lines[i].split("\t{1,}");
            System.out.println(args.length);
            if (args.length > 2) {
                if (getObjectByName("MimeTypes", args[0]) == null) {
                    ConfigValue cv = new ConfigValue(args[0], args[2], args[1]);
                    cv.setParent(this);
                    cv.setProperty("Source", "true");
                    addSubobject("MimeTypes", cv);
                } else {
                    ConfigValue cv = (ConfigValue) getObjectByName("MimeTypes", args[0]);
                    if (cv.getBoolean("Source") == true) {
                        cv.setProperty("Value", args[2]);
                        cv.setProperty("Label", args[1]);
                    }
                }
            }
        }
    }

    public void openMessage(String filename) {
        boolean openFile = true;
        if (filename.length() == 0) {
            JFileChooser chooser = new JFileChooser(getString("lastusedpath"));
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("eml");
            filter.setDescription("EML Dateien");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(getMainFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    filename = chooser.getSelectedFile().getCanonicalPath();
                    setProperty("lastusedpath", chooser.getCurrentDirectory().toString());
                    File f = new File(home + "/.EMLViewer/PID/pid");
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(filename.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (jtp.getTabCount() == 0) {
                    quit();
                } else {
                    openFile = false;
                }
            }
        }
        if (filename != null && openFile == true) {
            this.filename = filename;
            MailMessage message = new MailMessage();
            message.setParent(this);
            message.openMessage(filename);
            MailView mailView = new MailView(message);
            jtp.addTab(message.getName(), mailView);
            jtp.setSelectedComponent(mailView);
            mailView.resizeComponent();
            mailView.repaint();
            mainFrame.toFront();
        }
    }

    public MailMessage openMessage(InputStream is) {
        MailMessage message = new MailMessage();
        message.setParent(this);
        message.parseMessage(is);
        return message;
    }

    public MailMessage downloadMessage(String url) {
        FileUpload fu = new FileUpload();
        byte[] b = fu.downloadRawFile(url, "", "");
        InputStream bais = new ByteArrayInputStream(b);
        MailMessage message = openMessage(bais);
        MailView mailView = new MailView(message);
        jtp.addTab(message.getName(), mailView);
        mailView.repaint();
        return message;
    }

    public static void main(String args[]) {
        System.out.println("Starting EMLViewer...");
        int c = 0;
        System.out.println(System.getProperty("os.name").toLowerCase());
        try {
            if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
                String line;
                String[] commands = { "ps", "x" };
                Process p = Runtime.getRuntime().exec(commands);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    if (line.indexOf("EMLViewer") > -1 && line.indexOf("java") > -1) {
                        System.out.println(line);
                        c++;
                    }
                }
                c--;
                input.close();
            } else {
                String line;
                String[] commands = { "cmd.exe", "/c", "tasklist.exe", "/FI", "\"WINDOWTITLE eq EMLViewer\"" };
                Process p = Runtime.getRuntime().exec(commands);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                    if (line.startsWith("java")) {
                        c++;
                    }
                }
                input.close();
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        String filename = null;
        if (args.length > 0) {
            filename = args[0];
        }
        String home = System.getProperty("user.home");
        if (c == 0) {
            if (filename != null) {
                EMLViewer mainFrame = new EMLViewer();
                File f = new File(home + "/.EMLViewer/PID/pid");
                try {
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(filename.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainFrame.openMessage(filename);
            } else {
                File f = new File(home + "/.EMLViewer/PID/pid");
                try {
                    FileOutputStream fos = new FileOutputStream(f);
                    filename = "";
                    fos.write(filename.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                EMLViewer mainFrame = new EMLViewer();
            }
        } else {
            File f = new File(home + "/.EMLViewer/PID/pid");
            try {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(filename.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BasicClass getRoot() {
        return this;
    }

    public void quit() {
        if (mainFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            setProperty("width", "0");
            setProperty("height", "0");
        } else {
            setProperty("width", "" + mainFrame.getWidth());
            setProperty("height", "" + mainFrame.getHeight());
        }
        saveData();
        saveProperties();
        try {
            File tempdir = new File(getTempDir());
            File[] tempfiles = tempdir.listFiles();
            for (int i = 0; i < tempfiles.length; i++) {
                tempfiles[i].delete();
            }
        } catch (java.lang.Exception e) {
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Beenden")) {
            quit();
        } else if (e.getActionCommand().equals("Oeffnen")) {
            openMessage("");
        } else if (e.getActionCommand().equals("Einstellungen")) {
            editProperties();
        } else if (e.getActionCommand().equals("Suche")) {
            searchMessages();
        } else if (e.getActionCommand().equals("newMimeType")) {
            ConfigValue cv = new ConfigValue();
            cv.setParent(this);
            cv.setName("(Extension)");
            cv.getProperty("Value").setType("File");
            addSubobject("MimeTypes", cv);
            mimeTypeList.initList();
            mimeTypeList.refresh(this);
            if (propertyWindow != null) {
                propertyWindow.refresh(this);
            }
        } else if (e.getActionCommand().equals("executeSearch")) {
            System.out.println("search");
            executeSearch();
        } else if (e.getActionCommand().equals("EML-Dateien indexieren")) {
            indexFiles();
        } else if (e.getActionCommand().equals("Hilfe �ffnen")) {
            openHelp();
        } else if (e.getActionCommand().equals("Text speichern unter")) {
            saveText();
        } else if (e.getActionCommand().equals("Druck-Version")) {
            printMessage();
        } else if (e.getActionCommand().equals("Ausgeblendete Elemente anzeigen")) {
        }
    }

    public void saveProperties() {
        String s = getXMLString();
        saveFile(config_file, s);
    }

    public void editProperties() {
        ObjectList[] subelements = new ObjectList[1];
        mimeTypeList = new ObjectList(this);
        Object[][] functions = new Object[2][2];
        Image iEdit = mainFrame.getToolkit().getImage(mimeTypeList.getClass().getResource("edit.gif"));
        functions[0][0] = "edit";
        functions[0][1] = iEdit;
        Image iDelete = mainFrame.getToolkit().getImage(mimeTypeList.getClass().getResource("trashcan.gif"));
        functions[1][0] = "delete";
        functions[1][1] = iDelete;
        mimeTypeList.setFunctions(functions);
        mimeTypeList.setSize(740, 200);
        mimeTypeList.setTableWidth(700);
        mimeTypeList.setRenderedObjects("MimeTypes");
        mimeTypeList.initList();
        mimeTypeList.addField("name", "Bezeichnung", 100);
        mimeTypeList.addField("Value", "Befehl", 300);
        mimeTypeList.addField("Label", "Beschreibung", 200);
        subelements[0] = mimeTypeList;
        propertyWindow = openPropertyWindow(this, mainFrame, subelements);
        BasicToolbar tb = new BasicToolbar(this);
        tb.addCommand("newMimeType", "Neue Dateiverkn�pfung");
        tb.setBounds(0, mimeTypeList.getY() - 25, 800, 25);
        propertyWindow.getContentPane().add(tb);
        propertyWindow.getContentPane().remove(propertyWindow.saveButton);
        propertyWindow.cancelButton.setSize(240, 25);
        propertyWindow.cancelButton.setText("Schliessen");
        propertyWindow.setTitle("EMLViewer Einstellungen");
        propertyWindow.validate();
        propertyWindow.repaint();
    }

    public boolean handleElement(org.kubiki.xml.XMLElement e) {
        boolean parsed = false;
        if (e.properties.containsKey("org.kubiki.groupware.EMLViewer")) {
            setProperties(e);
            parseSubelements(e);
            parsed = true;
        }
        if (e.properties.containsKey("org.kubiki.groupware.MailMessageIndex")) {
            index = new MailMessageIndex();
            System.out.println("loading index");
            index.setParent(this);
            index.setProperties(e);
            index.parseSubelements(e);
            index.initObject();
            parsed = true;
        }
        return parsed;
    }

    public void handleCommand(String command) {
        System.out.println(command);
        if (command.startsWith("edit")) {
            int pos = command.indexOf("?");
            String objectType = command.substring(4, pos);
            if (pos > -1) {
                command = command.substring(pos + 1);
                int pos2 = command.indexOf("=");
                if (pos2 > -1) {
                    String objectName = command.substring(pos2 + 1);
                    ConfigValue a = (ConfigValue) getObjectByName("MimeTypes", objectName);
                    a.getProperty("Type").setHidden(true);
                    a.getProperty("Source").setLabel("Vordefinierte Einstellungen");
                    a.getProperty("Source").setSelection(false_true);
                    a.getProperty("Value").setLabel("Befehl");
                    a.getProperty("Label").setLabel("Beschreibung");
                    a.getProperty("name").setLabel("Bezeichnung");
                    System.out.println("PropertyWindow: " + propertyWindow);
                    openPropertyWindow(a, propertyWindow, 3);
                }
            }
        } else if (command.startsWith("delete")) {
            int pos = command.indexOf("?");
            if (pos > -1) {
                command = command.substring(pos + 1);
                int pos2 = command.indexOf("=");
                if (pos2 > -1) {
                    String objectName = command.substring(pos2 + 1);
                    System.out.println(objectName);
                    ConfigValue cv = (ConfigValue) getObjectCollection("MimeTypes").getObjectByName(objectName);
                    deleteElement(cv);
                    mimeTypeList.initList();
                    mimeTypeList.refresh(this);
                    if (propertyWindow != null) {
                        propertyWindow.refresh(this);
                    }
                }
            }
        }
    }

    public String getTempDir() {
        return home + "/.EMLViewer/temp";
    }

    public void indexFiles() {
        if (index == null) {
            loadData();
        }
        FileIndexer fi = new FileIndexer(this);
        new Thread(fi).start();
    }

    public void setStatusBar(String message) {
        statusBar.setStatusMessage(message);
        mainFrame.validate();
    }

    public void searchMessages() {
        if (index == null) {
            loadData();
        }
        searchObject = new MailMessageReference();
        searchObject.getProperty("name").setHidden(true);
        searchObject.setParent(this);
        searchObject.setName("Suche");
        ObjectList[] subelements = new ObjectList[1];
        if (searchIndex == null) {
            searchIndex = new MailMessageIndex();
            searchIndex.setParent(this);
        }
        searchList = new ObjectList(searchIndex);
        searchList.setSize(740, 200);
        searchList.setTableWidth(720);
        searchList.setRenderedObjects("MailMessageReference");
        searchList.initList();
        Object[][] functions = new Object[1][2];
        Image iOpen = mainFrame.getToolkit().getImage(searchList.getClass().getResource("open.png"));
        functions[0][0] = "open";
        functions[0][1] = iOpen;
        searchList.setFunctions(functions);
        searchList.addField("Sender", "Absender", 200);
        searchList.addField("Time", "Datum", 200);
        searchList.addField("Subject", "Thema", 230);
        subelements[0] = searchList;
        searchWindow = openPropertyWindow(searchObject, mainFrame, subelements);
        BasicToolbar tb = new BasicToolbar(this);
        tb.addCommand("executeSearch", "Suche Starten");
        tb.setBounds(0, searchList.getY() - 25, 800, 25);
        searchWindow.getContentPane().add(tb);
        searchWindow.validate();
        searchWindow.repaint();
        searchWindow.getContentPane().remove(searchWindow.saveButton);
        searchWindow.cancelButton.setSize(240, 25);
        searchWindow.cancelButton.setText("Schliessen");
        searchWindow.setTitle("EMLViewer Suche");
    }

    public void executeSearch() {
        searchIndex.getObjectCollection("MailMessageReference").removeObjects();
        Vector v = index.getObjects("MailMessageReference");
        String sender1 = searchObject.getString("Sender").toLowerCase();
        String recipient1 = searchObject.getString("Recipients").toLowerCase();
        String recipientcc1 = searchObject.getString("RecipientsCC").toLowerCase();
        String subject1 = searchObject.getString("Subject").toLowerCase();
        String attachment1 = searchObject.getString("Attachments").toLowerCase();
        java.util.Calendar time = null;
        int timeoperator = 0;
        String timestring = searchObject.getString("Time").toLowerCase();
        if (timestring.length() > 0) {
            if (timestring.startsWith("<")) {
                timestring = timestring.substring(1);
                timeoperator = -1;
            } else if (timestring.startsWith(">")) {
                timestring = timestring.substring(1);
                timeoperator = 1;
            }
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                time = java.util.Calendar.getInstance();
                time.setTime(df.parse(timestring));
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
            System.out.println(timestring);
            System.out.println(time);
        }
        System.out.println(v.size());
        for (int i = 0; i < v.size(); i++) {
            String resString = "";
            MailMessageReference mmr = (MailMessageReference) v.elementAt(i);
            System.out.println(i);
            if (sender1.length() > 0) {
                String sender2 = mmr.getProperty("Sender").getValue().toString().toLowerCase();
                if (sender2.indexOf(sender1) > -1) {
                    resString += "y";
                } else {
                    resString += "n";
                }
            }
            if (recipient1.length() > 0) {
                String recipient2 = mmr.getProperty("Recipients").getValue().toString().toLowerCase();
                if (recipient2.indexOf(recipient1) > -1) {
                    resString += "y";
                } else {
                    resString += "n";
                }
            }
            if (recipientcc1.length() > 0) {
                String recipientcc2 = mmr.getProperty("RecipientsCC").getValue().toString().toLowerCase();
                if (recipientcc2.indexOf(recipientcc1) > -1) {
                    resString += "y";
                } else {
                    resString += "n";
                }
            }
            if (subject1.length() > 0) {
                String subject2 = mmr.getProperty("Subject").getValue().toString().toLowerCase();
                if (subject2.indexOf(subject1) > -1) {
                    resString += "y";
                } else {
                    resString += "n";
                }
            }
            if (attachment1.length() > 0) {
                String attachment2 = mmr.getProperty("Attachments").getValue().toString().toLowerCase();
                System.out.println(attachment2);
                if (attachment2.indexOf(attachment1) > -1) {
                    resString += "y";
                } else {
                    resString += "n";
                }
            }
            if (time != null) {
                java.util.Calendar c = java.util.Calendar.getInstance();
                c.setTime((Date) mmr.getProperty("Time").getObject());
                c.set(java.util.Calendar.HOUR, 0);
                c.set(java.util.Calendar.MINUTE, 0);
                c.set(java.util.Calendar.SECOND, 0);
                if (c.compareTo(time) == timeoperator) {
                    resString += "y";
                } else {
                    resString += "n";
                }
            }
            if (resString.length() > 0 && resString.indexOf("n") == -1) {
                searchIndex.addSubobject("MailMessageReference", mmr);
            }
        }
        searchList.initList();
        searchList.repaint();
        searchList.refresh(this);
        searchWindow.validate();
        searchWindow.repaint();
    }

    public MailMessageIndex getMailIndex() {
        return index;
    }

    public void openHelp() {
        BasicClass bc = new BasicClass();
        bc.setName("Hilfe");
        bc.getProperty("name").setHidden(true);
        bc.setParent(this);
        BasicPanel[] objectList = new BasicPanel[1];
        HelpPanel hp = new HelpPanel("file:///" + basedir + "EMLViewerHilfe/EMLViewerHilfe.html");
        hp.setSize(740, 400);
        objectList[0] = hp;
        PropertyWindow helpWindow = openPropertyWindow(bc, mainFrame, objectList);
        helpWindow.getContentPane().remove(helpWindow.saveButton);
        helpWindow.cancelButton.setSize(240, 25);
        helpWindow.cancelButton.setText("Schliessen");
        helpWindow.setTitle("EMLViewer Hilfe");
    }

    public void printMessage() {
        try {
            MailView mv = (MailView) jtp.getSelectedComponent();
            MailMessage message = mv.getMessage();
            String content = "";
            content = "<style type=\"text/css\">body{font-size: 10pt; font-family: Sans-serif;}p{font-size: 10pt; font-family: Sans-serif;} .emlviewer{font-size: 10pt; font-family: Sans-serif;}</style>";
            content += "<div style=\"font-size: 10;\">";
            content += "<html><table style=\"width:700px;\">";
            content += "<tr>";
            content += "\n<th class=\"emlviewer\" width=\"120\" valign=\"top\" style=\"text-align : left;\">Absender:</th>";
            content += "\n<td class=\"emlviewer\" width=\"800\">" + message.getProperty("Sender").getValue().toString().replace("<", "&lt;").replace(">", "&gt;") + "</td>";
            content += "</tr>";
            content += "<tr>";
            content += "\n<th class=\"emlviewer\" valign=\"top\" style=\"text-align : left;\">Datum:</th>";
            try {
                content += "\n<td class=\"emlviewer\">" + DateFormat.getDateInstance(DateFormat.LONG).format((Date) message.getObject("Time")) + "; " + DateFormat.getTimeInstance(DateFormat.DEFAULT).format((Date) message.getObject("Time")) + "</td>";
            } catch (java.lang.Exception ex) {
                content += "\n<td class=\"emlviewer\">" + message.getObject("Time").toString() + "</td>";
            }
            content += "</tr>";
            content += "<tr>";
            content += "\n<th class=\"emlviewer\" valign=\"top\" style=\"text-align : left;\">Betreff:</th>";
            content += "\n<td class=\"emlviewer\">" + message.getString("Subject") + "</td>";
            content += "</tr>";
            content += "<tr>";
            content += "\n<th class=\"emlviewer\" valign=\"top\" style=\"text-align : left;\">An:</th>";
            content += "\n<td class=\"emlviewer\">" + message.getProperty("Recipients").getValue().toString().replace("<", "&lt;").replace(">", "&gt;") + "</td>";
            content += "</tr>";
            if (message.getProperty("RecipientsCC").getValue().toString().length() > 0) {
                content += "<tr>";
                content += "\n<th class=\"emlviewer\" valign=\"top\" style=\"text-align : left;\">CC:</th>";
                content += "\n<td class=\"emlviewer\">" + message.getProperty("RecipientsCC").getValue().toString().replace("<", "&lt;").replace(">", "&gt;") + "</td>";
                content += "</tr>";
            }
            if (message.getProperty("RecipientsBCC").getValue().toString().length() > 0) {
                content += "<tr>";
                content += "\n<th class=\"emlviewer\" valign=\"top\" style=\"text-align : left;\">BCC:</th>";
                content += "\n<td class=\"emlviewer\">" + message.getProperty("RecipientsBCC").getValue().toString().replace("<", "&lt;").replace(">", "&gt;") + "</td>";
                content += "</tr>";
            }
            Vector attachments = message.getObjects("MailAttachment");
            if (attachments.size() > 0) {
                content += "<tr>";
                content += "\n<th class=\"emlviewer\" valign=\"top\" style=\"text-align : left;\">Attachments:</th>";
                content += "<td class=\"emlviewer\">";
                for (int i = 0; i < attachments.size(); i++) {
                    MailAttachment ma = (MailAttachment) attachments.elementAt(i);
                    content += ma.getString("FileName") + "<br>\n";
                }
                content += "</td>";
                content += "</tr>";
            }
            content += "</div>";
            content += "</table><br><br><p>";
            Vector bodies = message.getObjects("MessageBodies");
            for (int i = 0; i < bodies.size(); i++) {
                MailMessageBody mmb = (MailMessageBody) bodies.elementAt(i);
                if (mmb.getBoolean("IsHidden") == false) {
                    content += mmb.getString("MessageBody");
                    content += "<br><br>";
                }
            }
            saveFile(getTempDir() + "/emlprint.html", content.getBytes());
            ConfigValue cv = (ConfigValue) getObjectByName("MimeTypes", "HTML");
            if (cv == null) {
                showMessageBox("", "F�r den Dateityp \"*.HTML\" ist keine Programmverkn�pfung definiert.\nWenden Sie sich an Ihren Systemadministrator");
            } else {
                String[] commands = new String[2];
                commands[0] = cv.getString("Value").trim();
                commands[1] = "file:///" + getTempDir() + "/emlprint.html";
                Process p = Runtime.getRuntime().exec(commands);
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public void saveText() {
        try {
            MailView mv = (MailView) jtp.getSelectedComponent();
            MailMessage message = mv.getMessage();
            String content = message.getString("MessageBody");
            saveFileAs(content.getBytes(), "emlexport" + ".txt", null);
        } catch (java.lang.Exception e) {
        }
    }
}

class FileIndexer implements Runnable {

    EMLViewer viewer;

    int messageCount = 0;

    public FileIndexer(EMLViewer viewer) {
        this.viewer = viewer;
    }

    public void run() {
        Vector urls = viewer.getProperty("RepositoryRootURL").getList();
        for (int i = 0; i < urls.size(); i++) {
            ConfigValue cv = (ConfigValue) urls.elementAt(i);
            String url = cv.getString("Value");
            if (url.length() > 0) {
                indexFiles(url);
            }
        }
        viewer.setStatusBar("Indexieren der Nachrichten beendet");
    }

    public void indexFiles(String url) {
        MailMessageIndex index = viewer.getMailIndex();
        FileUpload fu = new FileUpload();
        String indexFile = fu.downloadFile(url, "", "");
        String[] tags = indexFile.split("><|<|>");
        System.out.println(tags.length);
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].startsWith("a href=")) {
                String[] parts = tags[i].split("\"");
                for (int j = 0; j < parts.length; j++) {
                    if (parts[j].toLowerCase().startsWith("http://") && parts[j].toLowerCase().endsWith(".eml")) {
                        messageCount++;
                        viewer.setStatusBar("Anzahl Nachrichten indexiert: " + messageCount);
                        byte[] b = fu.downloadRawFile(parts[j], "", "");
                        InputStream bais = new ByteArrayInputStream(b);
                        if (index.getObjectByName("MailMessageReference", parts[j]) == null) {
                            MailMessage message = viewer.openMessage(bais);
                            MailMessageReference ref = new MailMessageReference();
                            ref.setParent(index);
                            ref.setName(parts[j]);
                            ref.setProperty("Sender", message.getProperty("Sender").getValue().toString());
                            ref.setProperty("Subject", message.getString("Subject"));
                            ref.setProperty("Time", message.getObject("Time"));
                            ref.setProperty("Recipients", message.getString("Recipients"));
                            ref.setProperty("RecipientsCC", message.getString("RecipientsCC"));
                            String attachmentString = "";
                            Vector attachments = message.getObjects("MailAttachment");
                            for (int k = 0; k < attachments.size(); k++) {
                                MailAttachment attachment = (MailAttachment) attachments.elementAt(k);
                                attachmentString += attachment.getString("FileName");
                            }
                            ref.setProperty("Attachments", attachmentString);
                            index.addSubobject("MailMessageReference", ref);
                        }
                    }
                }
            }
        }
    }
}

class FileChangeListener implements Runnable {

    EMLViewer viewer;

    public FileChangeListener(EMLViewer viewer) {
        this.viewer = viewer;
    }

    public void run() {
        while (true) {
            try {
                Thread.currentThread().sleep(500);
                viewer.checkFileChanged();
            } catch (java.lang.Exception e) {
            }
        }
    }
}

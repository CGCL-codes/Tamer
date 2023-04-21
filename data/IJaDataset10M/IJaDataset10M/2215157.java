package gameditor.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import gameditor.io.*;
import gameditor.parser.Parser;
import gameditor.ui.tripleaGUI.*;

public class EditorFrame extends JFrame implements ActionListener {

    private static final String aboutMessage = "<HTML><H2>" + gameditor.Config.TITLE + " v" + gameditor.Config.VERSION + "</H2>" + "<P>Editor Engine Developed by: <B>" + gameditor.Config.ENGINE_AUTHOR + "</B><BR>" + "TripleA GUI by: <B>" + gameditor.Config.GUI_AUTHOR + "</B></P><BR>" + "<P>Visit TripleA at http://triplea.sf.net</P></HTML>";

    private static JTabbedPane tabs;

    private static Object[] dataArray;

    private static Parser parser = new Parser();

    private static MapPanel mapPanel = new MapPanel();

    private static ResourcePanel resourcePanel = new ResourcePanel();

    private static PlayerPanel playerPanel = new PlayerPanel();

    /**
	   constructor default
	   
	   Not to be used, but
	   if used... default settings are forced.
	*/
    public EditorFrame() {
        this(gameditor.Config.TITLE, gameditor.Config.VERSION);
    }

    /**
	   constructor (String , String) 
	   
	   Prepares the main frame of the
	   program and initializes all GUI elements.

	   @param String title
	   @param String ver
	*/
    public EditorFrame(String title, String ver) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setTitle(title + " v" + ver);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(780, 500);
        setLocation(screenWidth / 8, screenHeight / 8);
        makeMenuBar();
        setVisible(true);
    }

    /**
	   void makeMenuBar()
	   
	   Initialize the menu bar.
	*/
    public void makeMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.add(makeMenuItem("Open"));
        fileMenu.add(makeMenuItem("Save"));
        fileMenu.addSeparator();
        fileMenu.add(makeMenuItem("Exit"));
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        helpMenu.add(makeMenuItem("About"));
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    /**
	   void makeTabs()
	   
	   Call each panel to create their main data
	   holding components (ie. JTable) and then
	   add the panels to the JTabbedPane and,
	   lastly, add the JTabbedPane to the 
	   content pane and validate the GUI.
	*/
    public void makeTabs() {
        mapPanel.makeGUI();
        resourcePanel.makeGUI();
        playerPanel.makeGUI();
        tabs = new JTabbedPane();
        tabs.addTab("Map", mapPanel);
        tabs.addTab("Resources", resourcePanel);
        tabs.addTab("Players", playerPanel);
        Container contentPane = getContentPane();
        contentPane.add(tabs);
        validate();
    }

    /**
	   JMenuItem makeMenuItem(String)
	
	   Creates a new JMenuItem and attaches an
	   action listener to it with a command name.

	   @param  String    name
	   @return JMenuItem item
	*/
    private JMenuItem makeMenuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(this);
        return item;
    }

    /**
	   void manageDataArray(Object[])
	   
	   An intermediary step before sending everything
	   to the manageData() method. Here we convert the
	   Object[] into a String[] and omit the lines
	   starting with ?xml and !DOCTYPE, since we have
	   no use for them now.
	   
	   Then we call the Frame to create the tabs. This
	   is assuming that manageData() has sent all the
	   data to all the panels and they are ready to be
	   added to the content pane.
	   
	   @param Object[] oData
	*/
    private void manageDataArray(Object[] oData) {
        String[] strArr;
        for (int i = 0; i < oData.length; i++) {
            strArr = (String[]) oData[i];
            if (!strArr[0].startsWith("?") && !strArr[0].startsWith("!")) {
                manageData(strArr);
            }
        }
        makeTabs();
    }

    /**
	   void manageData(String[])
	   
	   This method manages the actual data before sending it
	   to its appropriate GUI Component. Because we know that
	   the first 2 cells of the String array are not needed
	   to be sent in as data (they are miscelaneous data used
	   for advanced sorting) we need to remove them from
	   the finalized array that gets sent.
	   
	   @param String[] sData
	*/
    private void manageData(String[] sData) {
        String[] newData = new String[sData.length - 2];
        String tagName = sData[0];
        int tagType = Integer.parseInt(sData[1]);
        int j = 0;
        for (int i = 2; i < sData.length; i++) {
            newData[j] = sData[i];
            j++;
        }
        if (tagName.equals("territory")) {
            if (newData.length == 1) {
                String[] tmpArr = new String[newData.length + 1];
                tmpArr[0] = newData[0];
                tmpArr[1] = "false";
                mapPanel.addTerritory(tmpArr, tagType);
            } else {
                mapPanel.addTerritory(newData, tagType);
            }
        }
        if (tagName.equals("connection")) {
            mapPanel.addConnection(newData, tagType);
        }
        if (tagName.equals("resource")) {
            resourcePanel.addResource(newData, tagType);
        }
        if (tagName.equals("player")) {
            playerPanel.addPlayer(newData, tagType);
        }
        if (tagName.equals("alliance")) {
            playerPanel.addAlliance(newData, tagType);
        }
    }

    /**
	   void actionPerformed(ActionEvent)
	
	   Grabs action events and matches their action
	   commands to see if they match our menu items.
	   If a match can't be made then throw an error.

	   @param ActionEvent event
	*/
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        if (cmd.equals("Exit")) {
            System.exit(0);
        } else if (cmd.equals("Open")) {
            openFile();
        } else if (cmd.equals("Save")) {
            saveFile();
        } else if (cmd.equals("About")) {
            showAbout();
        } else {
            System.out.println("Unrecognized Event Command");
        }
    }

    /**
	   void openFile()
	   
	   Opens a file

	   @see gameditor.io.FileOpen
	*/
    private void openFile() {
        File file = new FileOpen().getFile();
        if (file != null) {
            parser.reset();
            parser.readFile(file);
            parser.tokenize();
            dataArray = parser.parse();
            manageDataArray(dataArray);
        }
    }

    /**
	   void saveFile()
	   
	   Saves a file

	   @see gameditor.io.FileSave
	*/
    private void saveFile() {
        System.out.println("Not Implemented Yet");
    }

    /**
	   void showAbout()
	   
	   Display the about screen
	*/
    private void showAbout() {
        JOptionPane.showMessageDialog(EditorFrame.this, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}

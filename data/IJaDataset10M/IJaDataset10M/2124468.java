package edu.whitman.halfway.jigs.gui.imageSelector;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import edu.whitman.halfway.util.*;
import edu.whitman.halfway.jigs.*;
import org.apache.log4j.Logger;

public class ImageTypeSelector extends JFrame implements ActionListener, ListSelectionListener {

    public ImageTypeSelector() {
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                runOnClose();
            }
        });
        setTitle("Image Type Editor");
        Container contentPane = getContentPane();
        imageListModel = readConfig(new DefaultListModel());
        imageList = new JList(imageListModel);
        imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        imageList.addListSelectionListener(this);
        JScrollPane listScroller = new JScrollPane(imageList);
        listScroller.setMinimumSize(new Dimension(250, 150));
        listScroller.setPreferredSize(new Dimension(250, 150));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        p.add(listScroller);
        contentPane.add(p, BorderLayout.CENTER);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        newButton = new JButton(JigsPrefs.getIcon("New24.gif"));
        newButton.addActionListener(this);
        newButton.setActionCommand("new");
        newButton.setSize(new Dimension(24, 24));
        newButton.setBorderPainted(false);
        p.add(newButton);
        editButton = new JButton(JigsPrefs.getIcon("Edit24.gif"));
        editButton.addActionListener(this);
        editButton.setActionCommand("edit");
        editButton.setSize(new Dimension(24, 24));
        editButton.setBorderPainted(false);
        p.add(editButton);
        deleteButton = new JButton(JigsPrefs.getIcon("Delete24.gif"));
        deleteButton.addActionListener(this);
        deleteButton.setSize(new Dimension(24, 24));
        deleteButton.setBorderPainted(false);
        deleteButton.setActionCommand("delete");
        p.add(deleteButton);
        contentPane.add(p, BorderLayout.EAST);
        p = new JPanel();
        JButton jb = new JButton("Cancel");
        jb.addActionListener(this);
        jb.setActionCommand("cancel");
        p.add(jb);
        jb = new JButton("Ok");
        jb.addActionListener(this);
        jb.setActionCommand("ok");
        p.add(jb);
        contentPane.add(p, BorderLayout.SOUTH);
        updateButtons();
        this.pack();
        this.show();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            updateButtons();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cancel")) {
            runOnClose();
        } else if (e.getActionCommand().equals("ok")) {
            writeConfig();
            runOnClose();
        } else if (e.getActionCommand().equals("new")) {
            imageTypeEditor.loadType(null);
            imageTypeEditor.setVisible(true);
            updateImageType(imageTypeEditor.getImageType(), false);
        } else {
            int selectedIndex = imageList.getSelectedIndex();
            if (imageList.getSelectedIndex() < 0) return;
            if (e.getActionCommand().equals("delete")) {
                imageListModel.remove(selectedIndex);
                imageData.remove(selectedIndex);
                imageList.setSelectedIndex(-1);
                updateButtons();
            } else if (e.getActionCommand().equals("edit")) {
                imageTypeEditor.loadType((ImageType) imageData.get(selectedIndex));
                imageTypeEditor.setVisible(true);
                updateImageType(imageTypeEditor.getImageType(), true);
            }
        }
    }

    private void updateImageType(ImageType imageType, boolean isEdit) {
        if (imageType == null) return;
        int selectedIndex = imageList.getSelectedIndex();
        int listSize = imageListModel.getSize();
        if ((selectedIndex < 0) || (selectedIndex >= listSize)) {
            if (isEdit) {
                log.error("There should be a element we are editting here!");
                return;
            } else {
                imageData.add(imageType);
                imageListModel.addElement(imageType.getLongName());
                imageList.setSelectedIndex(listSize);
            }
        } else {
            if (isEdit) {
                imageData.set(selectedIndex, imageType);
                imageListModel.set(selectedIndex, imageType.getLongName());
                imageList.setSelectedIndex(selectedIndex);
            } else {
                imageData.add(selectedIndex + 1, imageType);
                imageListModel.add(selectedIndex + 1, imageType.getLongName());
                imageList.setSelectedIndex(selectedIndex + 1);
            }
        }
        updateButtons();
    }

    private void updateButtons() {
        if (imageList.getSelectedIndex() >= 0) {
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } else {
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }

    private DefaultListModel readConfig(DefaultListModel list) {
        File configFile = CONFIG_FILE;
        ImageType[] imageInfo = null;
        if (configFile.exists() && !configFile.isDirectory()) {
            imageInfo = (new ImageTypeSource(configFile)).getTypes();
        }
        for (int i = 0; i < imageInfo.length; i++) {
            list.addElement(imageInfo[i].getLongName());
        }
        imageData = new ArrayList(Arrays.asList(imageInfo));
        return list;
    }

    private void writeConfig() {
        Properties p = new Properties();
        Iterator iter = imageData.iterator();
        while (iter.hasNext()) {
            ImageType imageType = (ImageType) iter.next();
            PropUtil.append(p, imageType.getProps(), imageType.getShortName() + ".");
        }
        try {
            FileOutputStream out = new FileOutputStream(CONFIG_FILE);
            p.store(out, null);
            out.close();
        } catch (IOException e) {
        }
    }

    private void runOnClose() {
        System.exit(0);
    }

    public static void main(String[] args) {
        Log4jConfig.doConfig();
        new ImageTypeSelector();
    }

    private ImageTypeEditor imageTypeEditor = new ImageTypeEditor(this);

    private JButton newButton;

    private JButton editButton;

    private JButton deleteButton;

    private JList imageList;

    private DefaultListModel imageListModel;

    protected Logger log = Logger.getLogger(ImageTypeSelector.class.getName());

    private File CONFIG_DIR = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".jigs");

    private File CONFIG_FILE = new File(CONFIG_DIR, "link_types.txt");

    private ArrayList imageData = null;
}

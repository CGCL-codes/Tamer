package magictool.groupdisplay;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import magictool.DidNotFinishException;
import magictool.ExpFile;
import magictool.GrpFile;
import magictool.MainFrame;
import magictool.Project;
import magictool.filefilters.GifFilter;
import magictool.filefilters.NoEditFileChooser;

/**
 * JInternalFrame which displays a Circle Display in a JScrollPane
 */
public class CircleDisplayFrame extends JInternalFrame {

    /**expression file*/
    protected ExpFile exp;

    /**group file*/
    protected GrpFile grp;

    private JScrollPane scroll;

    private JMenuBar menuBar = new JMenuBar();

    private JMenu jMenu1 = new JMenu("File");

    private JMenuItem printMenu = new JMenuItem("Print");

    private JMenuItem closeMenu = new JMenuItem("Close");

    private JMenu jMenu2 = new JMenu("Display");

    private JMenuItem radiusMenu = new JMenuItem("Change Radius");

    private JMenuItem threshMenu = new JMenuItem("Change Threshold");

    /**circle display in frame*/
    protected CircleDisplay theDisplay;

    private JMenuItem imgChoice = new JMenuItem("Save As Image...");

    private JMenuItem saveGrpChoice = new JMenuItem("Save Selected As Group...");

    /**parent frame*/
    protected Frame parent;

    private Project project;

    /**
   * Constructs a frame with the circle display containing all genes in the expression file
   * @param exp expression file
   * @param parent parent frame
   * @param project open project
   */
    public CircleDisplayFrame(ExpFile exp, Frame parent, Project project) {
        this(exp, new GrpFile(), parent, project);
    }

    /**
   * Constructs a frame with the circle display containing all genes specified in the group file
   * @param exp expression file
   * @param grp group file
   * @param parent parent frame
   * @param project open project
   */
    public CircleDisplayFrame(ExpFile exp, GrpFile grp, Frame parent, Project project) {
        this.exp = exp;
        this.parent = parent;
        this.project = project;
        if (grp == null) grp = new GrpFile(); else this.grp = grp;
        if (grp.getNumGenes() == 0) {
            Object o[] = exp.getGeneVector();
            for (int i = 0; i < o.length; i++) {
                this.grp.addOne(o[i]);
            }
        }
        theDisplay = new CircleDisplay(exp, this.grp);
        scroll = new JScrollPane(theDisplay);
        scroll.getViewport().setBackground(Color.white);
        scroll.setBackground(Color.white);
        this.setBackground(Color.white);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setClosable(true);
        this.setJMenuBar(menuBar);
        this.setMaximizable(true);
        this.setResizable(true);
        this.setDoubleBuffered(true);
        scroll.setDoubleBuffered(true);
        printMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_P, java.awt.event.KeyEvent.CTRL_MASK));
        printMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                printMenu_actionPerformed(e);
            }
        });
        closeMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_W, java.awt.event.KeyEvent.CTRL_MASK));
        closeMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeMenu_actionPerformed(e);
            }
        });
        radiusMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_R, java.awt.event.KeyEvent.CTRL_MASK));
        radiusMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                radiusMenu_actionPerformed(e);
            }
        });
        threshMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_T, java.awt.event.KeyEvent.CTRL_MASK));
        threshMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                threshMenu_actionPerformed(e);
            }
        });
        imgChoice.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                imgChoice_actionPerformed(e);
            }
        });
        saveGrpChoice.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveGrpChoice_actionPerformed(e);
            }
        });
        this.getContentPane().add(scroll);
        menuBar.add(jMenu1);
        menuBar.add(jMenu2);
        jMenu1.add(imgChoice);
        jMenu1.add(saveGrpChoice);
        jMenu1.add(printMenu);
        jMenu1.addSeparator();
        jMenu1.add(closeMenu);
        jMenu2.add(radiusMenu);
        jMenu2.add(threshMenu);
    }

    private void threshMenu_actionPerformed(ActionEvent e) {
        String r = (String) JOptionPane.showInputDialog(parent, "Please Enter New Threshold", "", JOptionPane.QUESTION_MESSAGE, null, null, "" + theDisplay.getThresh());
        try {
            theDisplay.setThresh(Float.parseFloat(r));
            theDisplay.repaint();
        } catch (Exception e2) {
        }
    }

    private void radiusMenu_actionPerformed(ActionEvent e) {
        String r = (String) JOptionPane.showInputDialog(parent, "Please Enter New Radius", "", JOptionPane.QUESTION_MESSAGE, null, null, "" + theDisplay.getRadius());
        try {
            theDisplay.setRadius(Integer.parseInt(r));
            theDisplay.repaint();
        } catch (Exception e2) {
        }
    }

    private void closeMenu_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void printMenu_actionPerformed(ActionEvent e) {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        PageFormat pf = printJob.pageDialog(printJob.defaultPage());
        printJob.setPrintable(theDisplay, pf);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception PrintException) {
            }
        }
    }

    private void imgChoice_actionPerformed(ActionEvent e) {
        try {
            NoEditFileChooser jfc = new NoEditFileChooser(MainFrame.fileLoader.getFileSystemView());
            jfc.setFileFilter(new GifFilter());
            jfc.setDialogTitle("Create New Gif File...");
            jfc.setApproveButtonText("Select");
            File direct = new File(project.getPath() + "images" + File.separator);
            if (!direct.exists()) direct.mkdirs();
            jfc.setCurrentDirectory(direct);
            int result = jfc.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File fileobj = jfc.getSelectedFile();
                String name = fileobj.getPath();
                if (!name.endsWith(".gif")) name += ".gif";
                final String picture = name;
                Thread thread = new Thread() {

                    public void run() {
                        saveImage(picture);
                    }
                };
                thread.start();
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(parent, "Failed To Create Image");
        }
    }

    private void saveImage(String name) {
        try {
            int number = 1;
            theDisplay.saveImage(name, number = (int) Math.ceil(theDisplay.getMegaPixels() / project.getImageSize()));
            if (number > 1) {
                String tn = name.substring(name.lastIndexOf(File.separator), name.lastIndexOf("."));
                String tempname = name.substring(0, name.lastIndexOf(File.separator)) + tn + ".html";
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempname));
                bw.write("<html><header><title>" + name + "</title></header>");
                bw.write("<body>");
                bw.write("<table cellpadding=0 cellspacing=0 border=0");
                for (int i = 0; i < number; i++) {
                    bw.write("<tr><td><img src=" + tn.substring(1) + "_images" + tn + i + ".gif border=0></td></tr>");
                }
                bw.write("</table></body></html>");
                bw.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Failed To Create Image");
        }
    }

    private void saveGrpChoice_actionPerformed(ActionEvent e) {
        DefaultListModel groupModel = new DefaultListModel();
        JList groupGenes = new JList();
        groupGenes.setModel(groupModel);
        Object[] o = theDisplay.getAllSelected();
        if (o.length > 0) {
            for (int i = 0; i < o.length; i++) {
                groupModel.addElement(o[i].toString());
            }
            String s = JOptionPane.showInputDialog(parent, "Enter The Group Name:");
            if (s != null) {
                GrpFile newGrp = new GrpFile(s);
                for (int i = 0; i < groupModel.size(); i++) {
                    newGrp.addOne(groupModel.elementAt(i));
                }
                if (!s.endsWith(".grp")) s += ".grp";
                newGrp.setExpFile(exp.getName());
                try {
                    File file = new File(project.getPath() + exp.getName() + File.separator + s);
                    int result = JOptionPane.YES_OPTION;
                    if (file.exists()) {
                        result = JOptionPane.showConfirmDialog(parent, "The file " + file.getPath() + " already exists.  Overwrite this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) file.delete();
                    }
                    if (result == JOptionPane.YES_OPTION) newGrp.writeGrpFile(project.getPath() + exp.getName() + File.separator + s);
                } catch (DidNotFinishException e2) {
                    JOptionPane.showMessageDialog(parent, "Error Writing Group File");
                }
                project.addFile(exp.getName() + File.separator + s);
            }
        } else {
            JOptionPane.showMessageDialog(parent, "No Genes Selected");
        }
    }
}

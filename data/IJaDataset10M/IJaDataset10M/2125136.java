package SequenceJuxtaposer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import AccordionDrawer.AccordionDrawer;

/** * @author jeffrey *  * To change the template for this generated type comment go to * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments */
public class UI implements ActionListener, ChangeListener {

    Frame mainFrame;

    String title;

    GroupFrame groupFrame;

    boolean groupFrameVisible = false;

    AnnotationFrame annotationFrame;

    boolean annotationFrameVisible = false;

    boolean annotationOn = true;

    JPanel controlPanel;

    Panel drawPanel;

    GridLayout drawLayout;

    Toolkit toolkit = Toolkit.getDefaultToolkit();

    Dimension screendim = toolkit.getScreenSize();

    final JFileChooser fc = new JFileChooser();

    boolean firstTime = true;

    MenuBar menuBar;

    MenuItem menuItem[][];

    static final String MENUFONTTYPE = "Helvetica";

    static final int MENUFONTSTYLE = Font.BOLD;

    static final int MENUFONTSIZE = 10;

    static final String menu[] = { "Open", "Tools", "Annotation", "Help", "Quit" };

    static final int OPENOPT = 0;

    static final int TOOLOPT = 1;

    static final int ANNOOPT = 2;

    static final int HELPOPT = 3;

    static final int QUITOPT = 4;

    Button optionButton[];

    SequenceJuxtaposer sj;

    JSlider diffChangeSlider = new JSlider();

    JLabel diffChangeLabel = new JLabel("Difference");

    public UI(SequenceJuxtaposer sj, String title) {
        this.sj = sj;
        mainFrame = new Frame(title);
        mainFrame.setLocation(10, 30);
        mainFrame.setLayout(new BorderLayout());
        controlPanel = new JPanel();
        drawPanel = new Panel();
        drawLayout = new GridLayout(1, 1, 0, 4);
        drawPanel.setLayout(drawLayout);
        sj.searchPanel = new Panel();
        sj.optionBar = new JPanel(new BorderLayout());
        optionButton = new Button[menu.length];
        Panel smallPanel = new Panel();
        for (int i = 0; i < optionButton.length; i++) {
            optionButton[i] = new Button(menu[i]);
            optionButton[i].setFont(new Font(MENUFONTTYPE, MENUFONTSTYLE, MENUFONTSIZE));
            if (i < optionButton.length - 1) smallPanel.add(optionButton[i]); else sj.optionBar.add(optionButton[i], BorderLayout.EAST);
            optionButton[i].addActionListener(this);
        }
        sj.optionBar.add(smallPanel, BorderLayout.WEST);
        fc.addChoosableFileFilter(new FastaFilter());
        fc.setMultiSelectionEnabled(true);
        int diff = (int) (sj.getDifferenceValue() * 100);
        diffChangeSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, diff);
        diffChangeSlider.setPreferredSize(new Dimension(100, diffChangeSlider.getPreferredSize().height * 2));
        diffChangeSlider.addChangeListener(this);
        diffChangeSlider.setMajorTickSpacing(5);
        diffChangeSlider.setPaintTicks(true);
        sj.optionBar.add(diffChangeSlider, BorderLayout.CENTER);
        groupFrame = new GroupFrame(sj);
    }

    protected Frame getMainFrame() {
        return mainFrame;
    }

    protected Panel getDrawPanel() {
        return drawPanel;
    }

    protected JPanel getControlPanel() {
        return controlPanel;
    }

    protected GridLayout getDrawLayout() {
        return drawLayout;
    }

    protected void addAction() {
        if (firstTime) {
            fc.setCurrentDirectory(new File("."));
            firstTime = false;
        }
        int returnVal = fc.showOpenDialog(mainFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = fc.getSelectedFiles();
            for (int i = 0; i < files.length; i++) {
                boolean fasta_file;
                String s = null;
                if (fc.getFileFilter().accept(files[i])) fasta_file = true; else fasta_file = false;
                try {
                    s = files[i].getCanonicalPath();
                    if (AccordionDrawer.debugOutput) System.out.println("YZ File.getCanonicalPath() fname: " + s);
                    if (fasta_file) {
                        if (AccordionDrawer.debugOutput) System.out.println("Load fasta file " + s);
                        sj.loadFasta(s, Integer.MAX_VALUE);
                    }
                } catch (Exception ex) {
                    System.out.println("File not found: " + s);
                    ex.printStackTrace();
                }
            }
        }
    }

    protected void quitAction() {
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof Button)) return;
        Button b = (Button) e.getSource();
        if (b == optionButton[OPENOPT]) {
            sj.addAction();
        } else if (b == optionButton[TOOLOPT]) {
            showGroupFrame();
        } else if (b == optionButton[ANNOOPT]) {
            showAnnotationFrame();
        } else if (b == optionButton[HELPOPT]) {
            new Help().showAboutFrame();
        } else if (b == optionButton[QUITOPT]) {
            System.exit(0);
        }
    }

    public void showGroupFrame() {
        if (mainFrame.getLocation().y + mainFrame.getHeight() + 30 + groupFrame.getHeight() < screendim.height) groupFrame.setLocation(10, mainFrame.getLocation().y + mainFrame.getHeight() + 30); else groupFrame.setLocation(10, screendim.height - groupFrame.getHeight());
        groupFrameVisible = (groupFrameVisible == false) ? true : false;
        groupFrame.setVisible(groupFrameVisible);
    }

    public void showAnnotationFrame() {
        if (annotationOn) {
            annotationFrame = new AnnotationFrame(sj);
            annotationOn = false;
        }
        if (SequenceJuxtaposer.maxAnnotationNum > 0) {
            if (mainFrame.getLocation().y + mainFrame.getHeight() + 30 + annotationFrame.getHeight() < screendim.height) annotationFrame.setLocation(10, mainFrame.getLocation().y + mainFrame.getHeight() + 30); else annotationFrame.setLocation(10, screendim.height - annotationFrame.getHeight());
            annotationFrameVisible = (annotationFrameVisible == false) ? true : false;
            annotationFrame.setVisible(annotationFrameVisible);
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == diffChangeSlider) {
            float diff = (float) diffChangeSlider.getValue();
            if (sj.getDifferenceValue() != diff / 100.0f) sj.setDifferenceValue(diff / 100.0f);
        }
    }

    /**	 * @return Returns the groupFrame.	 */
    public GroupFrame getGroupFrame() {
        return groupFrame;
    }
}

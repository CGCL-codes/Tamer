package plp_converter.listeners;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import plp_converter.gui.Common;
import java.awt.Cursor;

/**
 * This listener class generates a window that contains a list of the selected
 * files. This is very practical when a multitude of files have been selected.
 * Of course it is not added to the importfield until some files have been
 * selected. So of course this is taken care of by FileImportListener.
 */
public class SelectedFilesListener implements MouseListener {

    protected File[] filelist = null;

    private static JFrame frame = null;

    private static JPanel panel = null;

    private static JTextArea filelistarea = null;

    private JTextField selectedfiles = null;

    public SelectedFilesListener(File[] f, JTextField jtf) {
        this.filelist = f;
        this.selectedfiles = jtf;
    }

    private static synchronized JFrame getInstance() {
        if (frame == null) {
            frame = new JFrame("Selected Files");
            panel = new JPanel();
            filelistarea = new JTextArea();
            filelistarea.setEditable(false);
            filelistarea.setLineWrap(true);
            filelistarea.setWrapStyleWord(false);
            JScrollPane scroller = new JScrollPane(filelistarea);
            scroller.setPreferredSize(new Dimension(550, 400));
            panel.add(scroller);
            frame.add(panel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setResizable(false);
        }
        return frame;
    }

    private String createSelectedList() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < filelist.length; i++) {
            sb = sb.append((i + 1) + ") \"").append(filelist[i].getName()).append("\"\n");
        }
        return sb.toString();
    }

    public void mouseClicked(MouseEvent me) {
        frame = getInstance();
        filelistarea.setText(this.createSelectedList());
        Common.centerWindow(frame);
        frame.setVisible(true);
    }

    public void mouseEntered(MouseEvent me) {
        selectedfiles.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent me) {
        selectedfiles.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }
}

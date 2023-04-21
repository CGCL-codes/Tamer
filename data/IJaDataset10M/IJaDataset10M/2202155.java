package net.sourceforge.sevents.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import static net.sourceforge.sevents.util.SBundle.tr;
import static java.lang.Math.*;
import net.sourceforge.sevents.scripthandle.Log;
import net.sourceforge.sevents.util.FileUtils;

/**
 * 
 * @author becase
 */
@SuppressWarnings("serial")
public class FileView extends JFrame {

    private JButton closeButton;

    private JTextArea ta = null;

    /** Creates a new instance of FileView */
    public FileView(final String name, final String fileName) {
        super(name);
        setSize(640, 480);
        Container c = getContentPane();
        JPanel p1 = new JPanel();
        BorderLayout bl = new BorderLayout();
        bl.setHgap(20);
        bl.setVgap(20);
        p1.setLayout(bl);
        ta = new JTextArea();
        ta.setFont(getFont());
        ta.setEditable(false);
        SwingWorker<Void, Object> worker = new SwingWorker<Void, Object>() {

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    File f = new File(fileName);
                    Scanner s = new Scanner(f, FileUtils.fileEncoding);
                    while (s.hasNext()) {
                        ta.append(" " + s.nextLine());
                        if (s.hasNext()) {
                            ta.append("\n");
                        }
                    }
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void done() {
                resize();
                pack();
            }
        };
        JScrollPane sp = new JScrollPane(ta);
        c.add(p1, BorderLayout.CENTER);
        p1.setLayout(bl);
        p1.add(sp, BorderLayout.CENTER);
        closeButton = new JButton(tr("close"));
        closeButton.addActionListener(new CloseButtonListener(this));
        JPanel p2 = new JPanel();
        p2.add(closeButton);
        c.add(p2, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        addWindowListener(new WinClose(this));
        worker.execute();
    }

    private void resize() {
        Dimension dim = this.getPreferredSize();
        dim.height += 40;
        dim.width += 30;
        this.setSize(dim);
        Dimension rec = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        x = max(0, (rec.width - dim.width) / 2);
        y = max(0, (rec.height - dim.height) / 2);
        Log.out(tr("window") + ": " + this.getClass().getName() + " (" + this.getName() + ") " + tr("resize") + "\n x = " + x + "\n y = " + y + "\n width = " + dim.width + "\n height = " + dim.height, Log.DEBUG);
        this.setBounds(x, y, dim.width, dim.height);
    }

    class CloseButtonListener implements ActionListener {

        private Frame parent;

        public CloseButtonListener(Frame parentFrame) {
            parent = parentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            parent.setVisible(false);
            parent.dispose();
            System.gc();
        }
    }

    class WinClose extends WindowAdapter {

        private Frame parent;

        public WinClose(Frame parentFrame) {
            parent = parentFrame;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            parent.setVisible(false);
            parent.dispose();
            System.gc();
        }
    }

    public static void main(String args[]) {
        new FileView(tr("File"), "LICENSE");
    }
}

package J2D;

import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.SpringLayout;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import viewer.com.sun.pdfview.PagePanel;
import java.io.File;
import java.io.RandomAccessFile;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.IOException;

public class JDraw extends JFrame implements WindowListener, ActionListener {

    Surface su0;

    MathematicalTree jt0 = new MathematicalTree();

    JButton btn;

    JButton btnNext;

    JButton btnPrevious;

    PagePanel pgp;

    JPanel jpa;

    int currentPage = 1;

    EntityLoader el = new EntityLoader();

    public JDraw() throws IOException {
        initButton();
        initPage();
        Container contentPane = this.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        contentPane.add(jt0);
        contentPane.add(btn);
        contentPane.add(pgp);
        contentPane.add(btnNext);
        contentPane.add(btnPrevious);
        layout.putConstraint(SpringLayout.WEST, jt0, 50, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, jt0, 50, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, pgp, 10, SpringLayout.EAST, jt0);
        layout.putConstraint(SpringLayout.NORTH, pgp, 50, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btn, 30, SpringLayout.SOUTH, pgp);
        layout.putConstraint(SpringLayout.WEST, btn, 10, SpringLayout.EAST, jt0);
        layout.putConstraint(SpringLayout.WEST, btnNext, 10, SpringLayout.EAST, btn);
        layout.putConstraint(SpringLayout.NORTH, btnNext, 25, SpringLayout.SOUTH, pgp);
        layout.putConstraint(SpringLayout.WEST, btnPrevious, 10, SpringLayout.EAST, btnNext);
        layout.putConstraint(SpringLayout.NORTH, btnPrevious, 25, SpringLayout.SOUTH, pgp);
        setPreferredSize(new Dimension(1500, 800));
        addWindowListener(this);
        pack();
        setVisible(true);
        viewPage(1);
        show();
    }

    public void initPage() {
        pgp = new PagePanel();
        setPreferredSize(new Dimension(750, 550));
    }

    public void viewPage(int pageIndex) throws IOException {
        ByteBuffer buf = EntityLoader.returnByteBuffer(17);
        PDFFile pdffile = new PDFFile(buf);
        PDFPage page = pdffile.getPage(pageIndex);
        pgp.showPage(page);
    }

    public void initButton() {
        btn = new JButton();
        btn.setText("RENDER");
        btn.setPreferredSize(new Dimension(100, 100));
        btnNext = new JButton();
        btnNext.setText("NEXT");
        btnNext.setSize(new Dimension(250, 100));
        btnPrevious = new JButton();
        btnPrevious.setText("PREVIOUS");
        btnPrevious.setSize(new Dimension(250, 100));
        btn.addActionListener(this);
        btnNext.addActionListener(this);
        btnPrevious.addActionListener(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    new JDraw();
                } catch (IOException ioexc) {
                    ioexc.printStackTrace();
                }
            }
        });
    }

    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent e) {
        Timer timer = new Timer(500, this);
        timer.setInitialDelay(2000);
        timer.setRepeats(false);
        timer.start();
    }

    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals("RENDER")) {
                setTitle("RENDERING");
                su0.render();
            }
            if (e.getActionCommand().equals("NEXT")) {
                try {
                    viewPage(currentPage++);
                } catch (IOException ioexc) {
                    ioexc.printStackTrace();
                }
            }
            if (e.getActionCommand().equals("PREVIOUS")) {
                try {
                    if (currentPage > 1) viewPage(currentPage--);
                } catch (IOException ioexc) {
                    ioexc.printStackTrace();
                }
            }
        } catch (NullPointerException npexc) {
            System.out.println("Window is closing");
            boolean alreadyDispose = false;
            if (isDisplayable()) {
                alreadyDispose = true;
                dispose();
            }
        } finally {
        }
    }
}

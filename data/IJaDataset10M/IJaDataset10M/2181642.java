package mandelbrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import padrmi.exception.PpAuthorizationException;
import padrmi.exception.PpException;
import pubweb.IntegrityException;
import pubweb.Job;
import pubweb.NotConnectedException;
import pubweb.NotEnoughWorkersException;
import pubweb.supernode.sched.RequirementsContainer;
import pubweb.user.Consumer;
import pubweb.user.JobDialog;

public class Mandelbrot extends JFrame implements ActionListener {

    private class InnerWindowAdapter extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            quit();
        }
    }

    private class NewJobThread extends Thread {

        private int nProcs;

        private Mandelbrot controlGUI;

        private String name;

        private String program;

        private MandelbrotArgs args;

        public NewJobThread(Mandelbrot controlGUI, String name, int nProcs, String program, MandelbrotArgs args) {
            this.controlGUI = controlGUI;
            this.name = name;
            this.nProcs = nProcs;
            this.program = program;
            this.args = args;
        }

        public void run() {
            try {
                consumer.newJob(name, nProcs, program, args, new RequirementsContainer());
            } catch (PpAuthorizationException ex) {
                JOptionPane.showMessageDialog(controlGUI, "error performing the requested operation: authentication error (" + ex.getMessage() + ")", "Mandelbrot", JOptionPane.ERROR_MESSAGE);
            } catch (IntegrityException ex) {
                JOptionPane.showMessageDialog(controlGUI, "error performing the requested operation: integrity check failed (" + ex.getMessage() + ")", "Mandelbrot", JOptionPane.ERROR_MESSAGE);
            } catch (NotConnectedException ex) {
                JOptionPane.showMessageDialog(controlGUI, "error performing the requested operation: not connected to supernode", "Mandelbrot", JOptionPane.ERROR_MESSAGE);
            } catch (NotEnoughWorkersException ex) {
                JOptionPane.showMessageDialog(controlGUI, "error performing the requested operation: not enough peers available", "Mandelbrot", JOptionPane.ERROR_MESSAGE);
            } catch (PpException ex) {
                JOptionPane.showMessageDialog(controlGUI, "error performing the requested operation: network error (" + ex.getMessage() + ")", "Mandelbrot", JOptionPane.ERROR_MESSAGE);
            } catch (Throwable ex) {
                JOptionPane.showMessageDialog(controlGUI, "error performing the requested operation (see stdout for details)", "Mandelbrot", JOptionPane.ERROR_MESSAGE);
                System.out.println("error performing the requested operation:");
                ex.printStackTrace();
            }
        }
    }

    protected Consumer consumer;

    private MandelbrotEventListenerImpl eventListener;

    private int argsN;

    private JTextField xLine, yLine, sizeLine, nLine, procsLine;

    private Picture pic;

    private JobDialog jobDialog;

    public Mandelbrot(String config) throws Exception {
        consumer = new Consumer(config);
        eventListener = new MandelbrotEventListenerImpl(this);
        consumer.addConsumerEventListener(eventListener);
        setTitle("Webcomputing Mandelbrot");
        addWindowListener(new InnerWindowAdapter());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Box lv = new Box(BoxLayout.Y_AXIS);
        lv.add(Box.createVerticalStrut(7));
        Box xBox = new Box(BoxLayout.X_AXIS);
        JLabel xLabel = new JLabel("x:");
        xLine = new JTextField();
        xLine.setMaximumSize(new Dimension(xLine.getMaximumSize().width, xLine.getPreferredSize().height));
        xLine.setText("-0.5");
        xBox.add(Box.createHorizontalStrut(5));
        xBox.add(xLabel);
        xBox.add(Box.createHorizontalStrut(5));
        xBox.add(xLine);
        xBox.add(Box.createHorizontalStrut(5));
        lv.add(xBox);
        lv.add(Box.createVerticalStrut(7));
        Box yBox = new Box(BoxLayout.X_AXIS);
        JLabel yLabel = new JLabel("y:");
        yLine = new JTextField();
        yLine.setMaximumSize(new Dimension(yLine.getMaximumSize().width, yLine.getPreferredSize().height));
        yLine.setText("0");
        yBox.add(Box.createHorizontalStrut(5));
        yBox.add(yLabel);
        yBox.add(Box.createHorizontalStrut(5));
        yBox.add(yLine);
        yBox.add(Box.createHorizontalStrut(5));
        lv.add(yBox);
        Box sizeBox = new Box(BoxLayout.X_AXIS);
        JLabel sizeLabel = new JLabel("s:");
        sizeLine = new JTextField();
        sizeLine.setMaximumSize(new Dimension(sizeLine.getMaximumSize().width, sizeLine.getPreferredSize().height));
        sizeLine.setText("2");
        sizeBox.add(Box.createHorizontalStrut(5));
        sizeBox.add(sizeLabel);
        sizeBox.add(Box.createHorizontalStrut(5));
        sizeBox.add(sizeLine);
        sizeBox.add(Box.createHorizontalStrut(5));
        lv.add(sizeBox);
        Box nBox = new Box(BoxLayout.X_AXIS);
        JLabel nLabel = new JLabel("Image Size:");
        nLine = new JTextField();
        nLine.setMaximumSize(new Dimension(nLine.getMaximumSize().width, nLine.getPreferredSize().height));
        nLine.setText("1024");
        nBox.add(Box.createHorizontalStrut(5));
        nBox.add(nLabel);
        nBox.add(Box.createHorizontalStrut(5));
        nBox.add(nLine);
        nBox.add(Box.createHorizontalStrut(5));
        lv.add(nBox);
        lv.add(Box.createVerticalStrut(7));
        Box procsBox = new Box(BoxLayout.X_AXIS);
        JLabel procsLabel = new JLabel("Number of Processors:");
        procsLine = new JTextField();
        procsLine.setMaximumSize(new Dimension(procsLine.getMaximumSize().width, procsLine.getPreferredSize().height));
        procsLine.setText("1");
        procsBox.add(Box.createHorizontalStrut(5));
        procsBox.add(procsLabel);
        procsBox.add(Box.createHorizontalStrut(5));
        procsBox.add(procsLine);
        procsBox.add(Box.createHorizontalStrut(5));
        lv.add(procsBox);
        JButton start = new JButton("Start");
        start.addActionListener(this);
        lv.add(Box.createVerticalGlue());
        lv.add(start);
        lv.add(Box.createVerticalGlue());
        getContentPane().add(lv);
        setSize(260, 185);
        setVisible(true);
        GraphicsDevice[] gds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        Rectangle screenBounds = gds[0].getDefaultConfiguration().getBounds();
        setLocation(screenBounds.width / 2 - getSize().width / 2 + screenBounds.x, screenBounds.height / 2 - getSize().height / 2 + screenBounds.y);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            double x = Double.parseDouble(xLine.getText());
            double y = Double.parseDouble(yLine.getText());
            double size = Double.parseDouble(sizeLine.getText());
            int n = Integer.parseInt(nLine.getText());
            int procs = Integer.parseInt(procsLine.getText());
            MandelbrotArgs args = new MandelbrotArgs();
            args.xc = x;
            args.yc = y;
            args.size = size;
            args.n = n;
            args.procs = procs;
            argsN = n;
            pic = new Picture(n, n);
            pic.show();
            new NewJobThread(this, "MandelbrotBSPMig", procs, "mandelbrot.MandelbrotBSPMig", args).start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "error performing the requested operation (see stdout for details)", "Mandelbrot", JOptionPane.ERROR_MESSAGE);
            System.out.println("error performing the requested operation:");
            ex.printStackTrace();
        }
    }

    private void quit() {
        consumer.removeConsumerEventListener(eventListener);
        System.exit(0);
    }

    private JobDialog getJobDialog() {
        if (jobDialog != null) {
            if (!jobDialog.isVisible()) {
                jobDialog.setVisible(true);
            }
            return jobDialog;
        } else {
            jobDialog = new JobDialog(this, "Mandelbrot Job");
            jobDialog.setVisible(true);
            return jobDialog;
        }
    }

    public void handleEventLogLine(Job job, String line) {
        getJobDialog().appendEventLog(line);
    }

    public void handleStdOutLine(Job job, String line) {
        getJobDialog().appendStdOut(line);
    }

    public void handleStdErrLine(Job job, String line) {
        getJobDialog().appendStdErr(line);
    }

    public void handleRawData(Job job, Serializable data) {
        int slice = ((Integer) (((Serializable[]) data)[0])).intValue();
        int pid = ((Integer) (((Serializable[]) data)[1])).intValue();
        int[][] p = (int[][]) (((Serializable[]) data)[2]);
        int i, j;
        double[] c = new double[3];
        pid = (pid % 26) + 1;
        c[0] = (double) (pid % 3) / 2.0;
        c[1] = (double) ((pid / 3) % 3) / 2.0;
        c[2] = (double) ((pid / 9) % 3) / 2.0;
        for (i = 0; i < p.length; ++i) {
            for (j = 0; j < argsN; ++j) {
                Color color = new Color((int) (c[0] * p[i][j]), (int) (c[1] * p[i][j]), (int) (c[2] * p[i][j]));
                pic.set(i + slice, j, color);
            }
        }
        pic.show();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("usage: java mandelbrot.Mandelbrot <config file>");
            System.exit(1);
        }
        new Mandelbrot(args[0]);
    }
}

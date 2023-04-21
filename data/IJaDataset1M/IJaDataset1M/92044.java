package de.miethxml.openoffice;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 */
public class Main {

    public static String[] args;

    private JTextField url;

    private JTextField oopath;

    private JTabbedPane tp;

    private ArrayList beans = new ArrayList();

    private OfficeBean activeBean;

    /**
     *
     */
    public Main() {
        super();
    }

    public static void main(String[] a) {
        args = a;
        Main m = new Main();
        m.doTest();
    }

    public void doTest() {
        JFrame frame = new JFrame("OpenOfficeBean Tester - DO NOT USE THIS!!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(2, 3));
        panel.add(new JLabel("OpenOfficePath:"));
        oopath = new JTextField("");
        panel.add(oopath);
        panel.add(new JLabel(""));
        panel.add(new JLabel("File"));
        url = new JTextField("");
        panel.add(url);
        tp = new JTabbedPane();
        tp.setPreferredSize(new Dimension(640, 480));
        tp.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int index = tp.getSelectedIndex();
                if (activeBean != null) {
                    activeBean.setActive(false);
                }
                OfficeBean bean = (OfficeBean) beans.get(index);
                bean.setActive(true);
                activeBean = bean;
            }
        });
        frame.getContentPane().add(tp, BorderLayout.CENTER);
        JButton button = new JButton("Open");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new Runnable() {

                    public void run() {
                        OfficeBean bean = OfficeBeanFactory.createBean(oopath.getText());
                        Component view = bean.getView();
                        beans.add(bean);
                        tp.addTab("Bean " + view.hashCode(), view);
                        tp.setSelectedIndex(tp.getComponentCount() - 1);
                        try {
                            StreamHandler handler = new StreamHandler("foo", new FileInputStream(url.getText()), new FileOutputStream("/home/simon/foo.sxw"));
                            bean.open(handler);
                            tp.invalidate();
                            tp.revalidate();
                            if (activeBean != null) {
                                activeBean.setActive(false);
                            }
                            bean.setActive(true);
                            activeBean = bean;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        });
        panel.add(button);
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        JPanel p = new JPanel(new FlowLayout());
        button = new JButton("Close Document");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = tp.getSelectedIndex();
                System.out.println("Tab selected:" + tp.getSelectedIndex());
                if (index > -1) {
                    try {
                        OfficeBean bean = (OfficeBean) beans.remove(index);
                        System.out.println("Dispose Bean:" + bean.getView().hashCode());
                        bean.dispose();
                    } catch (DisposeVetoException e1) {
                        e1.printStackTrace();
                    }
                    tp.remove(index);
                }
            }
        });
        p.add(button);
        button = new JButton("Terminate OpenOffice");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OfficeBeanFactory.terminateOpenOffice();
            }
        });
        p.add(button);
        frame.getContentPane().add(p, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}

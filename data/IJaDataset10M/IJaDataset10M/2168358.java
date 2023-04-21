package com.yerihyo.yeritools.swing;

import info.clearthought.layout.TableLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import com.yerihyo.yeritools.swing.SwingToolkit.TestFrame;
import com.yerihyo.yeritools.text.StringToolkit;

/**
 * 
 * @author __USER__
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        test03();
    }

    private static void test03() {
        double[][] size = new double[][] { { TableLayout.FILL, 40 }, { TableLayout.FILL } };
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        int n = 20;
        for (int i = 0; i < n; i++) {
            JPanel oneline = new JPanel();
            TableLayout layout = new TableLayout(size);
            oneline.setLayout(layout);
            layout.setHGap(2);
            JTextArea textArea = new JTextArea();
            String text = StringToolkit.generateRandomString(new Random(), 100).toString();
            textArea.append(text);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            oneline.add(textArea, "0,0");
            JLabel label = new JLabel("test " + i);
            label.setBackground(new Color(0xcc6666));
            oneline.add(label, "0,1");
            panel.add(oneline);
            panel.add(Box.createVerticalStrut(2));
        }
        panel.setBorder(BorderFactory.createLineBorder(new Color(0xcc3333)));
        TestFrame testFrame = new TestFrame(new JScrollPane(panel));
        testFrame.setSize(new Dimension(800, 600));
        testFrame.showFrame();
    }

    private static void rebuildSpringPanel(JComponent parent, Component[] cArray) {
        parent.removeAll();
        parent.setLayout(new BorderLayout());
        JPanel springPanel = new JPanel();
        springPanel.setLayout(new SpringLayout());
        for (Component c : cArray) {
            springPanel.add(c);
        }
        parent.add(springPanel, BorderLayout.CENTER);
        SpringUtilities.makeCompactGrid(springPanel, cArray.length / 2, 2, 6, 6, 6, 6);
    }

    protected static void test02() {
        JTextArea textArea = new JTextArea();
        String text = StringToolkit.generateRandomString(new Random(), 100000).toString();
        textArea.append(text);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        TestFrame testFrame = new TestFrame(new JScrollPane(textArea));
        testFrame.setSize(new Dimension(800, 600));
        testFrame.showFrame();
    }

    protected static void test01() throws InterruptedException {
        JPanel panel = new JPanel();
        JPanel springPanel = new JPanel();
        panel.setLayout(new BorderLayout());
        springPanel.setLayout(new SpringLayout());
        panel.add(springPanel, BorderLayout.CENTER);
        panel.addHierarchyBoundsListener(new HierarchyBoundsListener() {

            @Override
            public void ancestorMoved(HierarchyEvent e) {
            }

            @Override
            public void ancestorResized(HierarchyEvent e) {
                JPanel panel = (JPanel) e.getSource();
                JPanel oldSpringPanel = (JPanel) panel.getComponent(0);
                rebuildSpringPanel(panel, oldSpringPanel.getComponents());
            }
        });
        int n = 10;
        List<Component> componentList = new ArrayList<Component>();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            JTextArea textArea = new JTextArea(StringToolkit.generateRandomString(r, 1000).toString());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JLabel label = new JLabel(StringToolkit.generateRandomString(r, 5).toString());
            label.setBackground(Color.red);
            componentList.add(textArea);
            componentList.add(label);
        }
        rebuildSpringPanel(panel, componentList.toArray(new Component[0]));
        TestFrame testFrame = new TestFrame(panel);
        testFrame.setSize(new Dimension(800, 600));
        testFrame.showFrame();
    }
}

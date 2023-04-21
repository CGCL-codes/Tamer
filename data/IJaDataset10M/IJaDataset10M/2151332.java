package com.pallas.unicore.client.panels;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.pallas.unicore.container.HoldJobContainer;
import com.pallas.unicore.container.JobContainer;
import com.pallas.unicore.extensions.HoldJobTest;
import com.pallas.unicore.extensions.TestType;

/**
 * @author Ralf Ratering
 * @version $Id: HoldJobTestPanel.java,v 1.1 2004/05/25 14:58:48 rmenday Exp $
 */
public class HoldJobTestPanel extends AbstractTestPanel {

    private class SelectionListener implements ItemListener {

        public void itemStateChanged(ItemEvent ev) {
            if (ev.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
            if (ev.getSource() == testBox) {
                JComboBox comboBox = (JComboBox) ev.getSource();
                TestType test = ((TestType) comboBox.getSelectedItem());
                datePanel.setEnabled(test.isEnabled() && !test.equals(HoldJobTest.FOREVER));
                datePanel.updateValues();
            }
        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Hold Job Test");
        frame.getContentPane().add(new HoldJobTestPanel(new HoldJobContainer(new JobContainer())), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private HoldJobContainer container;

    private DatePanel datePanel;

    public HoldJobTestPanel(HoldJobContainer container) {
        super(container);
        this.container = container;
        buildComponents();
    }

    public void applyValues() {
        TestType type = (TestType) testBox.getSelectedItem();
        HoldJobTest test = container.getHoldJobTest();
        test.setType(type);
        test.setUseToday(datePanel.getUseToday());
        if (type.equals(HoldJobTest.FOREVER)) {
            test.setDate(new Date(Long.MAX_VALUE));
        } else {
            test.setDate(datePanel.getDate());
        }
    }

    private void buildComponents() {
        JPanel waitPanel = new JPanel();
        waitPanel.setLayout(new BoxLayout(waitPanel, BoxLayout.X_AXIS));
        JLabel waitLabel = new JLabel("Wait: ");
        waitLabel.setToolTipText("Wait until the chosen date has expired.");
        waitPanel.add(waitLabel);
        testBox = new JComboBox(HoldJobTest.getTypes());
        testBox.setToolTipText("Wait until the chosen date has expired.");
        testBox.addItemListener(new SelectionListener());
        waitPanel.add(testBox);
        datePanel = new DatePanel(waitLabel, testBox);
        setLayout(new BorderLayout());
        add(datePanel, BorderLayout.CENTER);
    }

    public void resetValues() {
        HoldJobTest test = container.getHoldJobTest();
        testBox.setSelectedItem(test.getType());
        datePanel.setDate(test.getDate());
        datePanel.setUseToday(test.getUseToday());
    }

    public void updateValues(boolean vsiteChanged) {
        datePanel.updateValues();
    }
}

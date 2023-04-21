package gov.sns.apps.mpsinputtest;

import java.util.Map;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TestMPSHPRFPanel extends JDialog implements CAValueListener {

    private JPanel basePanel;

    public static List _channelWrappers, HPRFtripWrap;

    public static Map HPRFtripMap = new LinkedHashMap();

    private static ChannelWrapper wrapper;

    public static TestMPSHPRFPanel showPanel(Component frameComp, Component locationComp, String title, String longValue, String labelText, String selTwo) {
        Frame frame = JOptionPane.getFrameForComponent(frameComp);
        TestMPSHPRFPanel newPanel = new TestMPSHPRFPanel(frame, locationComp, title, longValue, labelText, selTwo);
        newPanel.setSize(460, 200);
        newPanel.setVisible(true);
        return newPanel;
    }

    public TestMPSHPRFPanel() {
        JPanel readyPane = new JPanel();
    }

    private TestMPSHPRFPanel(final Frame frame, final Component locationComp, String title, String longValue, final String labelText, String selTwo) {
        super(frame, title, true);
        String[] columnNames = { "Flt_VSWR_PA", "Flt_VSWR_IPA", "Flt_Gnd", "Flt_PW", "Flt_PA", "Flt_IPA", "Flt_OT_Stk", "Flt_OT_Cab", "FPAR_MEBT_BS_cable_status" };
        String tripLabelStr = "Trip Status =   1   1   1   1   1   1   1   1   1";
        String[] checkTrips = { "1", "1", "1", "1", "1", "1", "1", "1", "1" };
        int[] HPRFtrips;
        int trips = 0;
        if (HPRFtripWrap == null) createChannelWrappers(labelText);
        JPanel cablePane = new JPanel();
        final JPanel oldCablePane;
        JPanel readyPane = new JPanel();
        readyPane.setLayout(new FlowLayout());
        String srcChain = longValue + " " + selTwo;
        JLabel testLabel = new JLabel(srcChain);
        readyPane.add(testLabel);
        JLabel tripLabel = new JLabel(tripLabelStr);
        readyPane.add(tripLabel);
        final JButton startButton;
        JButton closeButton, resetButton, contButton;
        closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
            }
        });
        resetButton = new JButton("Reset Trips");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetTrips(labelText);
            }
        });
        contButton = new JButton("Continue");
        contButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
                ContMPSHPRFPanel ContPanel = ContMPSHPRFPanel.showPanel(frame, locationComp, labelText);
            }
        });
        HPRFtrips = getMainWindow().getRdyToTestHPRFTrips(labelText);
        Object[][] newData = { { "", "", "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "", "", "" } };
        String str;
        for (int i = 0; i < checkTrips.length; i++) {
            str = String.valueOf(HPRFtrips[i]);
            if (str.equals(checkTrips[i])) newData[0][i] = str; else {
                newData[0][i] = "<html><body><font COLOR=#ff0000>" + HPRFtrips[i] + "</font></body></html>";
                trips++;
            }
        }
        if (trips > 0) {
            resetButton.setVisible(true);
            contButton.setVisible(false);
        } else {
            resetButton.setVisible(false);
            contButton.setVisible(true);
        }
        JTable TestTable = new JTable(newData, columnNames);
        TestTable.setShowVerticalLines(true);
        TestTable.setShowHorizontalLines(false);
        TestTable.setColumnSelectionAllowed(false);
        TestTable.setSelectionForeground(Color.white);
        TestTable.setSelectionBackground(Color.blue);
        TestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TestTable.setRowSelectionAllowed(true);
        TestTable.setVisible(true);
        JScrollPane listScroller = new JScrollPane(TestTable);
        listScroller.setPreferredSize(new Dimension(500, 80));
        listScroller.setAlignmentX(CENTER_ALIGNMENT);
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(labelText);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(resetButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(contButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(closeButton);
        Container contentPane = getContentPane();
        contentPane.add(readyPane, BorderLayout.PAGE_START);
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        pack();
        setLocationRelativeTo(locationComp);
    }

    public MPSWindow getMainWindow() {
        MPSDocument document = new MPSDocument();
        return document.getWindow();
    }

    void resetTrips(String initPV) {
        String PVname = initPV + ":Reset";
        String cmd = "caput " + PVname + " 1";
    }

    public void createChannelWrappers(String orgPvStr) {
        String[] chNames = { "", "", "", "", "", "", "", "", "", "" };
        HPRFtripWrap = new ArrayList();
        String chName = orgPvStr + ":Flt_VSWR_PA";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[0] = chName;
        String str;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[0], str);
        chName = orgPvStr + ":Flt_VSWR_IPA";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[1] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[1], str);
        chName = orgPvStr + ":Flt_Gnd";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[2] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[2], str);
        chName = orgPvStr + ":Flt_PW";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[3] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[3], str);
        chName = orgPvStr + ":Flt_PA";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[4] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[4], str);
        chName = orgPvStr + ":Flt_IPA";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[5] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[5], str);
        chName = orgPvStr + ":Flt_OT_Stk";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[6] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[6], str);
        chName = orgPvStr + ":Flt_OT_Cab";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[7] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[7], str);
        chName = orgPvStr + ":FPAR_MEBT_BS_cable_status";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[8] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[8], str);
        chName = orgPvStr + ":FPAR_MEBT_BS_chan_status";
        wrapper = new ChannelWrapper(chName);
        HPRFtripWrap.add(wrapper);
        wrapper.addCAValueListener(this);
        chNames[9] = chName;
        str = "" + wrapper.getValue();
        HPRFtripMap.put(chNames[9], str);
    }

    /** Indicates a new channel access value has been found for this wrapped channel. */
    public void newValue(final ChannelWrapper wrapper, int value) {
        value = wrapper.getValue();
        final String chName = wrapper.getName();
        final Runnable updateLabels = new Runnable() {

            public void run() {
            }
        };
        Thread appThread = new Thread() {

            @Override
            public void run() {
                try {
                    SwingUtilities.invokeAndWait(updateLabels);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        appThread.start();
    }

    public Map getRdyToTestHPRFtrip(String initPvStr) {
        List _channelWrappers = new ArrayList(30);
        if (HPRFtripWrap == null) createChannelWrappers(initPvStr);
        return HPRFtripMap;
    }

    public List getHPRFtripWrap() {
        return HPRFtripWrap;
    }
}

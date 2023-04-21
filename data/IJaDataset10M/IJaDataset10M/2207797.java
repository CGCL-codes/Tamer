package org.spantus.externals.recognition.ui;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;
import org.spantus.event.SpantusEvent;
import org.spantus.event.SpantusEventMulticaster;
import org.spantus.work.ui.ImageResourcesEnum;

public class RecognitionToolBar extends JToolBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private SpantusEventMulticaster eventMulticaster;

    public RecognitionToolBar(SpantusEventMulticaster eventMulticaster) {
        this.eventMulticaster = eventMulticaster;
    }

    ToolbarActionListener toolbarActionListener;

    private JButton recordBtn = null;

    private JButton stopBtn = null;

    private JCheckBox train;

    private boolean learnMode = false;

    public void initialize() {
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 3, 2);
        this.setLayout(layout);
        this.add(getRecordBtn());
        this.add(getStopBtn());
        this.add(getTrainCheckbox());
    }

    public JButton getRecordBtn() {
        if (recordBtn == null) {
            ImageIcon icon = createIcon(ImageResourcesEnum.record.getCode());
            recordBtn = createButton(icon, RecognitionCmdEnum.record);
        }
        return recordBtn;
    }

    public JButton getStopBtn() {
        if (stopBtn == null) {
            ImageIcon icon = createIcon(ImageResourcesEnum.stop.getCode());
            stopBtn = createButton(icon, RecognitionCmdEnum.stop);
            stopBtn.setEnabled(false);
        }
        return stopBtn;
    }

    protected ImageIcon createIcon(String name) {
        URL url = getClass().getClassLoader().getResource(name);
        if (url == null) {
            return null;
        }
        ImageIcon ii = new ImageIcon(url);
        return new ImageIcon(ii.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
    }

    public JCheckBox getTrainCheckbox() {
        if (train == null) {
            train = new JCheckBox(getResource(RecognitionCmdEnum.learn.name()));
            train.setSelected(learnMode);
            train.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    boolean learnMode = ((JCheckBox) e.getSource()).isSelected();
                    if (learnMode) {
                        getEventMulticaster().multicastEvent(SpantusEvent.createEvent(this, RecognitionCmdEnum.learn.name()));
                    } else {
                        getEventMulticaster().multicastEvent(SpantusEvent.createEvent(this, RecognitionCmdEnum.stopLearn.name()));
                    }
                }
            });
        }
        return train;
    }

    protected JButton createButton(ImageIcon icon, Enum<?> enumVal) {
        return createButton(icon, enumVal.name(), enumVal.name());
    }

    protected JButton createButton(ImageIcon icon, String cmd) {
        return createButton(icon, cmd, cmd);
    }

    protected JButton createButton(ImageIcon icon, String cmd, String name) {
        JButton btn = null;
        if (icon == null) {
            btn = new JButton(getResource(name), icon);
        } else {
            btn = new JButton(icon);
        }
        btn.setActionCommand(cmd);
        btn.setBorder(BorderFactory.createCompoundBorder());
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setFocusable(false);
        btn.setActionCommand(cmd);
        btn.setToolTipText(getResource(name));
        btn.addActionListener(getToolbarActionListener());
        return btn;
    }

    public String getResource(String key) {
        return key;
    }

    public ToolbarActionListener getToolbarActionListener() {
        if (toolbarActionListener == null) {
            toolbarActionListener = new ToolbarActionListener();
        }
        return toolbarActionListener;
    }

    public class ToolbarActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            getEventMulticaster().multicastEvent(SpantusEvent.createEvent(this, e.getActionCommand()));
            RecognitionCmdEnum cmd = RecognitionCmdEnum.valueOf(e.getActionCommand());
            switch(cmd) {
                case record:
                    getRecordBtn().setEnabled(false);
                    getStopBtn().setEnabled(true);
                    break;
                case stop:
                    getRecordBtn().setEnabled(true);
                    getStopBtn().setEnabled(false);
                    break;
            }
        }
    }

    public boolean isLearnMode() {
        return learnMode;
    }

    public void setLearnMode(boolean learnMode) {
        this.learnMode = learnMode;
    }

    public SpantusEventMulticaster getEventMulticaster() {
        return eventMulticaster;
    }

    public void setEventMulticaster(SpantusEventMulticaster eventMulticaster) {
        this.eventMulticaster = eventMulticaster;
    }
}

package saadadb.admintool.components.mapper;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import saadadb.admintool.components.AdminComponent;
import saadadb.admintool.components.input.AppendMappingTextField;
import saadadb.admintool.panels.editors.MappingKWPanel;
import saadadb.admintool.utils.HelpDesk;
import saadadb.command.ArgsParser;
import saadadb.exceptions.FatalException;
import saadadb.util.Messenger;

public class PositionMapperPanel extends PriorityPanel {

    private AppendMappingTextField positionField;

    private boolean forEntry;

    public PositionMapperPanel(MappingKWPanel mappingPanel, String title, boolean forEntry) {
        super(mappingPanel, title);
        this.forEntry = forEntry;
        JPanel panel = container.getContentPane();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(AdminComponent.LIGHTBACKGROUND);
        GridBagConstraints ccs = new GridBagConstraints();
        positionField = new AppendMappingTextField(mappingPanel, 2, forEntry, buttonGroup);
        ccs.gridx = 0;
        ccs.gridy = 0;
        ccs.weightx = 0;
        new MapperPrioritySelector(new JRadioButton[] { onlyBtn, firstBtn, lastBtn, noBtn }, noBtn, buttonGroup, new JComponent[] { positionField }, panel, ccs);
        ccs.gridx = 1;
        ccs.gridy = 0;
        ccs.weightx = 1;
        ccs.anchor = GridBagConstraints.LINE_START;
        panel.add(AdminComponent.getHelpLabel("Mapping priority Vs automatic detection"), ccs);
        ccs.gridx = 0;
        ccs.gridy = 1;
        ccs.weightx = 0;
        ccs.fill = GridBagConstraints.HORIZONTAL;
        positionField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (noBtn.isSelected()) {
                    firstBtn.setSelected(true);
                }
            }
        });
        panel.add(positionField, ccs);
        ccs.gridx++;
        ccs.weightx = 1;
        panel.add(helpLabel, ccs);
        this.setHelpLabel(HelpDesk.POSITION_MAPPING);
    }

    @Override
    public String getText() {
        return positionField.getText();
    }

    @Override
    public void setText(String text) {
        if (text == null) {
            positionField.setText("");
        } else {
            positionField.setText(text);
        }
    }

    @Override
    public void reset() {
        positionField.setText("");
        setMode("no");
    }

    /**
	 * @param parser
	 */
    public void setParams(ArgsParser parser) {
        try {
            setMode(parser.getPositionMappingPriority());
        } catch (FatalException e) {
            Messenger.trapFatalException(e);
        }
        setText(getMergedComponent(parser.getPositionMapping()));
    }

    /**
	 * @return
	 */
    public ArrayList<String> getParams() {
        ArrayList<String> retour = new ArrayList<String>();
        if (!this.isNo()) {
            if (forEntry) {
                retour.add("-posmapping=" + this.getMode());
                if (this.getText().length() > 0) {
                    retour.add("-position=" + this.getText());
                }
            } else {
                retour.add("-posmapping=" + this.getMode());
                if (this.getText().length() > 0) {
                    retour.add("-position=" + this.getText());
                }
            }
        }
        return retour;
    }
}

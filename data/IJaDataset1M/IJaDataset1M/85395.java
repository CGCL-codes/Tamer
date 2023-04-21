package whitespace.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.gjt.sp.jedit.jEdit;

public class OnSaveOptionPane extends WhiteSpaceAbstractOptionPane {

    private JCheckBox removeTrailingWhiteSpace;

    private JTextField escapeChars;

    private JCheckBox softTabifyLeadingWhiteSpace;

    private JCheckBox tabifyLeadingWhiteSpace;

    private JCheckBox untabifyLeadingWhiteSpace;

    public OnSaveOptionPane() {
        super("whitespace.saveOption");
    }

    public void _init() {
        this.removeTrailingWhiteSpace = this.createCheckBox("white-space.remove-trailing-white-space", false);
        this.escapeChars = new JTextField(jEdit.getProperty("white-space.escape-chars", "\\"), 10);
        ActionListener tabifyLogic = new TabifyLogicHandler();
        this.softTabifyLeadingWhiteSpace = this.createCheckBox("white-space.soft-tabify-leading-white-space", false);
        this.softTabifyLeadingWhiteSpace.addActionListener(tabifyLogic);
        this.tabifyLeadingWhiteSpace = this.createCheckBox("white-space.tabify-leading-white-space", false);
        this.tabifyLeadingWhiteSpace.addActionListener(tabifyLogic);
        this.untabifyLeadingWhiteSpace = this.createCheckBox("white-space.untabify-leading-white-space", false);
        this.untabifyLeadingWhiteSpace.addActionListener(tabifyLogic);
        addComponent(new JLabel(jEdit.getProperty("options.white-space.on-save")));
        addComponent(this.removeTrailingWhiteSpace);
        addComponent(jEdit.getProperty("options.white-space.escape-chars"), this.escapeChars);
        addComponent(this.softTabifyLeadingWhiteSpace);
        addComponent(this.tabifyLeadingWhiteSpace);
        addComponent(this.untabifyLeadingWhiteSpace);
    }

    public void _save() {
        jEdit.setBooleanProperty("white-space.remove-trailing-white-space", this.removeTrailingWhiteSpace.isSelected());
        jEdit.setProperty("white-space.escape-chars", this.escapeChars.getText());
        jEdit.setBooleanProperty("white-space.soft-tabify-leading-white-space", this.softTabifyLeadingWhiteSpace.isSelected());
        jEdit.setBooleanProperty("white-space.tabify-leading-white-space", this.tabifyLeadingWhiteSpace.isSelected());
        jEdit.setBooleanProperty("white-space.untabify-leading-white-space", this.untabifyLeadingWhiteSpace.isSelected());
    }

    private class TabifyLogicHandler implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            JCheckBox softTabify = OnSaveOptionPane.this.softTabifyLeadingWhiteSpace;
            JCheckBox tabify = OnSaveOptionPane.this.tabifyLeadingWhiteSpace;
            JCheckBox untabify = OnSaveOptionPane.this.untabifyLeadingWhiteSpace;
            if (evt.getSource() == softTabify) {
                tabify.setEnabled(!softTabify.isSelected());
                untabify.setEnabled(!softTabify.isSelected());
            } else {
                if (tabify.isSelected() && untabify.isSelected()) {
                    if (evt.getSource() == tabify) {
                        untabify.setSelected(false);
                    } else if (evt.getSource() == untabify) {
                        tabify.setSelected(false);
                    }
                }
            }
        }
    }
}

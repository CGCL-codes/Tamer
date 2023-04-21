package fedora.client.console;

import javax.swing.JCheckBox;

/**
 *
 * <p><b>Title:</b> BooleanInputPanel.java</p>
 * <p><b>Description:</b> </p>
 *
 * @author cwilper@cs.cornell.edu
 * @version $Id: BooleanInputPanel.java 5162 2006-10-25 00:49:06Z eddie $
 */
public class BooleanInputPanel extends InputPanel {

    private static final long serialVersionUID = 1L;

    private JCheckBox m_checkBox;

    public BooleanInputPanel(boolean primitive) {
        m_checkBox = new JCheckBox();
        m_checkBox.setSelected(false);
        add(m_checkBox);
    }

    public Object getValue() {
        return new Boolean(m_checkBox.isSelected());
    }
}

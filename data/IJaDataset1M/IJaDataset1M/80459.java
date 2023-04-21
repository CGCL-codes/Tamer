package gui;

import javax.swing.*;

public class EstimationResultsDialog extends JDialog {

    private JTextArea m_text;

    public EstimationResultsDialog(JFrame owner) {
        super(owner);
        JScrollPane js = new JScrollPane();
        m_text = new JTextArea(15, 20);
        m_text.setEditable(false);
        js.getViewport().add(m_text);
        add(js);
        setSize(300, 200);
    }

    public void setText(String text) {
        m_text.setText(text);
    }
}

package softwarekompetenz.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import softwarekompetenz.workbench.Workbench;
import softwarekompetenz.workbench.WorkbenchListener;
import softwarekompetenz.workbench.command.Command;

public class StatusBar implements WorkbenchListener {

    private JComponent m_panelComponent;

    private JLabel m_hintLabel;

    private Font m_font = new Font("Arial", Font.PLAIN, 11);

    public StatusBar() {
        Workbench.getWorkbench().addWorkbenchListener(this);
        buildGUI();
    }

    private void buildGUI() {
        m_panelComponent = new JPanel(new BorderLayout());
        m_panelComponent.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0, 0, 0)));
        m_panelComponent.setBackground(new Color(89, 107, 117));
        m_panelComponent.setPreferredSize(new Dimension(-1, 16));
        m_hintLabel = new JLabel("");
        m_hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        m_hintLabel.setForeground(new Color(255, 255, 255));
        m_hintLabel.setFont(m_font);
        m_hintLabel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
        m_panelComponent.add(m_hintLabel, BorderLayout.CENTER);
    }

    public Component getComponent() {
        return m_panelComponent;
    }

    public void commandExecuted(Command command) {
    }

    public void actionDescriptionChanged(String newDescription) {
        m_hintLabel.setText(newDescription);
    }

    public void propertyChanged(String key) {
    }
}

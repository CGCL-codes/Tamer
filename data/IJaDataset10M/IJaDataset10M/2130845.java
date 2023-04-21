package org.jfonia.view.main.header;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jfonia.constants.ViewConstants;
import org.jfonia.images.ImageConstants;
import org.jfonia.images.ScaledImageIcon;
import org.jfonia.language.DescriptionConstants;
import org.jfonia.language.LanguageResource;
import org.jfonia.view.components.UnifiedToolbarButtonUI;

/**
 *
 * @author Rik Bauwens
 */
public class NotationBar extends JPanel {

    private NotePanel notePanel = new NotePanel();

    private RestPanel restPanel = new RestPanel();

    private SymbolPanel symbolPanel = new SymbolPanel();

    private PerformancePanel performancePanel = new PerformancePanel();

    private NotationPanel currentPanel;

    public NotationBar() {
        super();
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JButton noteButton = createButton(new ScaledImageIcon(ImageConstants.LED_BLUE, LanguageResource.getInstance().getDescription(DescriptionConstants.NOTE_BAR)).setMaximumSide(ViewConstants.SWITCH_PANEL_BUTTON_SIZE).getImageIcon());
        noteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showNotePanel();
            }
        });
        JButton restButton = createButton(new ScaledImageIcon(ImageConstants.LED_GREEN, LanguageResource.getInstance().getDescription(DescriptionConstants.REST_BAR)).setMaximumSide(ViewConstants.SWITCH_PANEL_BUTTON_SIZE).getImageIcon());
        restButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showRestPanel();
            }
        });
        JButton symbolButton = createButton(new ScaledImageIcon(ImageConstants.LED_GRAY, LanguageResource.getInstance().getDescription(DescriptionConstants.SYMBOL_BAR)).setMaximumSide(ViewConstants.SWITCH_PANEL_BUTTON_SIZE).getImageIcon());
        symbolButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showSymbolPanel();
            }
        });
        JButton performanceButton = createButton(new ScaledImageIcon(ImageConstants.LED_ORANGE, LanguageResource.getInstance().getDescription(DescriptionConstants.PERFORMANCE_BAR)).setMaximumSide(ViewConstants.SWITCH_PANEL_BUTTON_SIZE).getImageIcon());
        performanceButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showPerformancePanel();
            }
        });
        notePanel.setVisible(false);
        restPanel.setVisible(false);
        symbolPanel.setVisible(false);
        performancePanel.setVisible(false);
        add(noteButton);
        add(notePanel);
        add(restButton);
        add(restPanel);
        add(symbolButton);
        add(symbolPanel);
        add(performanceButton);
        add(performancePanel);
        showNotePanel();
    }

    private void setCurrentPanel(NotationPanel notationPanel) {
        if (currentPanel != null) currentPanel.setVisible(false);
        notationPanel.setVisible(true);
        currentPanel = notationPanel;
        validate();
        repaint();
    }

    public void showNotePanel() {
        setCurrentPanel(notePanel);
    }

    public void showRestPanel() {
        setCurrentPanel(restPanel);
    }

    public void showSymbolPanel() {
        setCurrentPanel(symbolPanel);
    }

    public void showPerformancePanel() {
        setCurrentPanel(performancePanel);
    }

    private JButton createButton(Icon icon) {
        JButton button = new JButton();
        button.setIcon(icon);
        button.setUI(new UnifiedToolbarButtonUI());
        button.setContentAreaFilled(false);
        button.setBorder(null);
        return button;
    }
}

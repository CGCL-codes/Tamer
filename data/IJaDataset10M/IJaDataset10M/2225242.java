package be.vds.jtbdive.client.view.core.stats;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nRadioButton;
import be.vds.jtbdive.core.core.Diver;

public class DiverQueryDetailPanel extends JPanel {

    private static final long serialVersionUID = -949204641632784960L;

    private YearQueryPanel yearQueryPanel;

    private DiverQueryPanel diverQueryPanel;

    private I18nRadioButton fullRadioButton;

    private JXTaskPane yearTaskPane;

    private JXTaskPane diverTaskPane;

    public DiverQueryDetailPanel(List<Integer> years, List<Diver> divers) {
        init(years, divers);
        setDefaultValues();
    }

    private void setDefaultValues() {
        fullRadioButton.setSelected(true);
        yearTaskPane.setVisible(false);
    }

    private void init(List<Integer> years, List<Diver> divers) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.add(createButtons(), BorderLayout.NORTH);
        this.add(createCollapsible(years, divers), BorderLayout.CENTER);
        updateI18nTitle();
    }

    private Component createCollapsible(List<Integer> years, List<Diver> divers) {
        JXTaskPaneContainer collapse = new JXTaskPaneContainer();
        collapse.setOpaque(false);
        collapse.add(createYearsPanel(years));
        collapse.add(createDiversPanel(divers));
        JScrollPane scroll = new JScrollPane(collapse);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    private Component createDiversPanel(List<Diver> divers) {
        diverQueryPanel = new DiverQueryPanel(divers);
        diverTaskPane = new JXTaskPane();
        diverTaskPane.add(diverQueryPanel);
        return diverTaskPane;
    }

    public void updateI18nTitle() {
        yearTaskPane.setTitle(I18nResourceManager.sharedInstance().getString("years"));
        diverTaskPane.setTitle(I18nResourceManager.sharedInstance().getString("divers"));
    }

    private Component createYearsPanel(List<Integer> years) {
        yearQueryPanel = new YearQueryPanel(years);
        yearTaskPane = new JXTaskPane();
        yearTaskPane.add(yearQueryPanel);
        return yearTaskPane;
    }

    private Component createButtons() {
        JPanel durationTypeRadioButtonsPanel = new JPanel();
        durationTypeRadioButtonsPanel.setOpaque(false);
        durationTypeRadioButtonsPanel.setLayout(new FlowLayout());
        fullRadioButton = new I18nRadioButton("full");
        fullRadioButton.setOpaque(false);
        I18nRadioButton yearRadioButton = new I18nRadioButton("by.year");
        yearRadioButton.setOpaque(false);
        fullRadioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                yearTaskPane.setVisible(false);
            }
        });
        yearRadioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                yearTaskPane.setVisible(true);
            }
        });
        ButtonGroup periodGroup = new ButtonGroup();
        periodGroup.add(fullRadioButton);
        periodGroup.add(yearRadioButton);
        durationTypeRadioButtonsPanel.add(fullRadioButton);
        durationTypeRadioButtonsPanel.add(yearRadioButton);
        return durationTypeRadioButtonsPanel;
    }

    public boolean isSetToFull() {
        return fullRadioButton.isSelected();
    }

    public List<Diver> getSelectedDivers() {
        return diverQueryPanel.getSelectedDivers();
    }

    public List<Integer> getSelectedYears() {
        return yearQueryPanel.getSelectedYears();
    }

    public void setYears(List<Integer> years) {
        yearQueryPanel.setYears(years);
    }

    public void setDivers(ArrayList<Diver> divers) {
        diverQueryPanel.setDivers(divers);
    }
}

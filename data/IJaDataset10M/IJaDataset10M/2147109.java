package com.explodingpixels.macwidgets;

import com.explodingpixels.widgets.WindowUtils;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class DSourceListIMovie {

    public static SourceList createSourceList() {
        Icon eventIcon = new ImageIcon(DSourceListITunes.class.getResource("/com/explodingpixels/macwidgets/icons/Event.png"));
        Icon projectIcon = new ImageIcon(DSourceListITunes.class.getResource("/com/explodingpixels/macwidgets/icons/Project.png"));
        final SourceListModel model = new SourceListModel();
        SourceListCategory projectsCategory = new SourceListCategory("Projects");
        SourceListCategory eventsCategory = new SourceListCategory("Categories");
        model.addCategory(projectsCategory);
        model.addCategory(eventsCategory);
        SourceListItem projectTwo = new SourceListItem("Project two", projectIcon);
        projectTwo.setCounterValue(3);
        model.addItemToCategory(new SourceListItem("Project one", projectIcon), projectsCategory);
        model.addItemToCategory(projectTwo, projectsCategory);
        model.addItemToCategory(new SourceListItem("Project three", projectIcon), projectsCategory);
        model.addItemToCategory(new SourceListItem("Project four", projectIcon), projectsCategory);
        model.addItemToCategory(new SourceListItem("Event A", eventIcon), eventsCategory);
        model.addItemToCategory(new SourceListItem("Event B", eventIcon), eventsCategory);
        model.addItemToCategory(new SourceListItem("Event C", eventIcon), eventsCategory);
        SourceList sourceList = new SourceList(model);
        sourceList.setColorScheme(new SourceListDarkColorScheme());
        return sourceList;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                WindowUtils.createAndInstallRepaintWindowFocusListener(frame);
                frame.add(createSourceList().getComponent(), BorderLayout.CENTER);
                frame.setSize(225, 250);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

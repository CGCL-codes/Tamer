package de.offis.faint.gui.facedb;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import de.offis.faint.global.Constants;
import de.offis.faint.gui.MainFrame;

/**
 * @author maltech
 *
 */
public class FaceDBTab extends JPanel {

    private PersonPanel personPanel;

    private FaceGallery facePanel;

    public FaceDBTab(MainFrame mainFrame) {
        super(new BorderLayout());
        facePanel = new FaceGallery(mainFrame);
        this.personPanel = new PersonPanel(mainFrame);
        JScrollPane faceScrollPane = new JScrollPane(facePanel);
        faceScrollPane.setBorder(new TitledBorder("Face Gallery"));
        faceScrollPane.getViewport().setBackground(Color.WHITE);
        faceScrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel container = new JPanel(new BorderLayout());
        container.add(personPanel, BorderLayout.CENTER);
        container.setBorder(new TitledBorder("Known Persons"));
        JPanel container2 = new JPanel(new BorderLayout());
        container2.add(faceScrollPane, BorderLayout.CENTER);
        container2.add(new FaceDBMenuBar(mainFrame), BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, container, container2);
        splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        splitPane.setDividerLocation(Constants.INITIAL_HORIZONTAL_DIVIDERLOCATION);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);
    }

    public FaceGallery getFacePanel() {
        return facePanel;
    }

    public PersonPanel getPersonPanel() {
        return personPanel;
    }
}

package uk.ac.ebi.pride.gui.component.startup;

import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.action.impl.OpenDatabaseAction;
import uk.ac.ebi.pride.gui.action.impl.OpenFileAction;
import uk.ac.ebi.pride.gui.action.impl.OpenHelpAction;
import uk.ac.ebi.pride.gui.action.impl.OpenReviewAction;
import uk.ac.ebi.pride.gui.desktop.Desktop;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 25/05/11
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public class LaunchMenuViewer extends JPanel {

    public LaunchMenuViewer() {
        setupMainPane();
        addComponents();
    }

    private void setupMainPane() {
        this.setLayout(new BorderLayout());
    }

    public void addComponents() {
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createLineBorder(Color.gray));
        container.setBackground(Color.white);
        container.setLayout(new GridLayout(4, 1));
        PrideInspectorContext context = (PrideInspectorContext) Desktop.getInstance().getDesktopContext();
        ImageIcon openFileIcon = GUIUtilities.loadImageIcon(context.getProperty("open.file.icon.medium"));
        String openFileText = context.getProperty("open.file.title");
        String openFileTooltip = context.getProperty("open.file.tooltip");
        Action openFileAction = new OpenFileAction(openFileText, openFileIcon);
        JButton openFileButton = GUIUtilities.createLabelLikeButton(openFileAction);
        openFileButton.setToolTipText(openFileTooltip);
        openFileButton.setHorizontalAlignment(SwingConstants.LEFT);
        container.add(openFileButton);
        ImageIcon searchDBIcon = GUIUtilities.loadImageIcon(context.getProperty("open.database.icon.medium"));
        String searchDBText = context.getProperty("open.database.title");
        String searchDBTooltip = context.getProperty("open.database.tooltip");
        Action searchDBAction = new OpenDatabaseAction(searchDBText, searchDBIcon);
        JButton searchDBButton = GUIUtilities.createLabelLikeButton(searchDBAction);
        searchDBButton.setToolTipText(searchDBTooltip);
        searchDBButton.setHorizontalAlignment(SwingConstants.LEFT);
        container.add(searchDBButton);
        ImageIcon reviewerIcon = GUIUtilities.loadImageIcon(context.getProperty("reviewer.download.icon.medium"));
        String reviewerText = context.getProperty("reviewer.download.title");
        String reviewerTooltip = context.getProperty("reviewer.download.tooltip");
        Action reviewerAction = new OpenReviewAction(reviewerText, reviewerIcon);
        JButton reviewerButton = GUIUtilities.createLabelLikeButton(reviewerAction);
        reviewerButton.setToolTipText(reviewerTooltip);
        reviewerButton.setHorizontalAlignment(SwingConstants.LEFT);
        container.add(reviewerButton);
        ImageIcon helpIcon = GUIUtilities.loadImageIcon(context.getProperty("help.icon.medium"));
        String helpText = context.getProperty("help.title");
        Action helpAction = new OpenHelpAction(helpText, helpIcon);
        JButton helpButton = GUIUtilities.createLabelLikeButton(helpAction);
        helpButton.setHorizontalAlignment(SwingConstants.LEFT);
        container.add(helpButton);
        this.add(container, BorderLayout.CENTER);
    }
}

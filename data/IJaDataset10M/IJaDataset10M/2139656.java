package de.mse.mogwai.swingframework.buildin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * Wrapper class to implement a navigator content.
 * 
 * @author msertic
 */
public class BuildinNavigatorPanel extends JPanel {

    private JToolBar m_toolbar;

    private JPanel m_content;

    private JButton m_priorButton;

    private JButton m_nextButton;

    private JButton m_firstButton;

    private JButton m_lastButton;

    private JButton m_saveButton;

    private JButton m_deleteButton;

    private JButton m_newButton;

    private JButton m_backButton;

    public BuildinNavigatorPanel() {
        this.initialize();
    }

    private void initialize() {
        this.setLayout(new BorderLayout());
        this.add(this.getToolBar(), BorderLayout.NORTH);
        this.add(this.getContent());
    }

    public JToolBar getToolBar() {
        if (this.m_toolbar == null) {
            this.m_toolbar = new JToolBar();
            this.m_toolbar.setFloatable(false);
            this.m_toolbar.add(this.getBackButton());
            this.m_toolbar.addSeparator();
            this.m_toolbar.add(this.getFirstButton());
            this.m_toolbar.add(this.getPriorButton());
            this.m_toolbar.add(this.getNextButton());
            this.m_toolbar.add(this.getLastButton());
            this.m_toolbar.addSeparator();
            this.m_toolbar.add(this.getSaveButton());
            this.m_toolbar.add(this.getDeleteButton());
            this.m_toolbar.add(this.getNewButton());
            this.m_toolbar.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        }
        return this.m_toolbar;
    }

    public JButton getFirstButton() {
        if (this.m_firstButton == null) {
            this.m_firstButton = new JButton(new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/media/Rewind16.gif")));
            this.m_firstButton.setBorder(null);
        }
        return this.m_firstButton;
    }

    public JButton getBackButton() {
        if (this.m_backButton == null) {
            this.m_backButton = new JButton(new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/navigation/Home16.gif")));
            this.m_backButton.setBorder(null);
        }
        return this.m_backButton;
    }

    public JButton getPriorButton() {
        if (this.m_priorButton == null) {
            this.m_priorButton = new JButton(new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/navigation/Back16.gif")));
            this.m_priorButton.setBorder(null);
        }
        return this.m_priorButton;
    }

    public JButton getNextButton() {
        if (this.m_nextButton == null) {
            this.m_nextButton = new JButton(new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/navigation/Forward16.gif")));
            this.m_nextButton.setBorder(null);
        }
        return this.m_nextButton;
    }

    public JButton getLastButton() {
        if (this.m_lastButton == null) {
            this.m_lastButton = new JButton(new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/media/FastForward16.gif")));
            this.m_lastButton.setBorder(null);
        }
        return this.m_lastButton;
    }

    public JButton getSaveButton() {
        if (this.m_saveButton == null) {
            this.m_saveButton = new JButton(new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/general/Save16.gif")));
            this.m_saveButton.setBorder(null);
        }
        return this.m_saveButton;
    }

    public JButton getDeleteButton() {
        if (this.m_deleteButton == null) {
            this.m_deleteButton = new JButton(new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/general/Delete16.gif")));
            this.m_deleteButton.setBorder(null);
        }
        return this.m_deleteButton;
    }

    public JButton getNewButton() {
        if (this.m_newButton == null) {
            this.m_newButton = new JButton(new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/general/New16.gif")));
            this.m_newButton.setBorder(null);
        }
        return this.m_newButton;
    }

    public JPanel getContent() {
        if (this.m_content == null) {
            this.m_content = new JPanel(new BorderLayout());
            this.m_content.setBorder(null);
        }
        return this.m_content;
    }
}

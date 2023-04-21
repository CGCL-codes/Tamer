package oclac.view.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import oclac.data.Pipeline;
import oclac.util.I18N;
import oclac.util.IObservable;
import oclac.util.IObserver;
import oclac.view.ui.MainWindow;
import oclac.view.ui.models.PipelineGraphModel;

/**
 * This class handles a status bar at the bottom of the applications
 * main window.
 */
@SuppressWarnings("serial")
public class StatusBar extends JPanel implements IObservable, TreeModelListener, mxIEventListener, ChangeListener {

    /** List with all observers of this status */
    protected Vector<IObserver> observers = new Vector<IObserver>();

    /** The label with the status text */
    protected JLabel statusLabel = new JLabel();

    /** Panel that will be colored green or red if the current pipeline is valid or not */
    protected JPanel pipelineStatusIndicator = new JPanel();

    /** The default panel backgroun color that will be displayed if no pipelines is open */
    protected final Color defaultPanelColor;

    /** The current project solution file that is open. Is null if no file is open */
    protected File currentFile;

    /** The text that will be displayed in the status label */
    protected String statusText;

    /** True if the application status is 'dirty' */
    protected boolean dirty;

    /**
	 * Creates a new status bar.
	 */
    public StatusBar() {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setCurrentFile(null);
        setStatusText(I18N.instance.getMessage("StatusBar.ReadyStatus"));
        setDirty(false);
        defaultPanelColor = pipelineStatusIndicator.getBackground();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        addComponent(statusLabel, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.weightx = 0.0;
        Dimension preferredSize = statusLabel.getPreferredSize();
        preferredSize.width = 50;
        pipelineStatusIndicator.setPreferredSize(preferredSize);
        addComponent(pipelineStatusIndicator, gbc);
    }

    /**
	 * Adds a component to  the status bar.
	 * @param component The component to add.
	 * @param gbc The grid bag constraints with which the component should be added.
	 */
    protected void addComponent(JComponent component, GridBagConstraints gbc) {
        component.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(0, 4, 0, 4)));
        add(component, gbc);
    }

    /**
	 * Updates the displayed status text.
	 * The text will have a leading '*' if the current status is dirty.
	 */
    protected void updateText() {
        String setText = isDirty() ? "* " : "";
        setText += statusText;
        statusLabel.setText(setText);
    }

    /**
	 * Sets a new status text to be displayed.
	 * @param text The text to set.
	 */
    public void setStatusText(String text) {
        statusText = text;
        updateText();
        notifyObservers();
    }

    /**
	 * Returns whether the current applications status is dirty.
	 * Dirty means that there are unsaved changes on the current project solution.
	 * @return The current dirty status.
	 */
    public boolean isDirty() {
        return dirty;
    }

    /**
	 * Sets a new dirty status.
	 * @param dirty The status to set.
	 */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        updateText();
        notifyObservers();
    }

    /**
	 * Invokes that something has changed.
	 * Sets the status to dirty.
	 */
    public void invokeChange() {
        setDirty(true);
    }

    /**
	 * Returns the currently active project solution file
	 * @return The current file.
	 */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
	 * Sets a new current file.
	 * @param currentFile The file to set.
	 */
    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        notifyObservers();
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.notify(this);
        }
    }

    /**
	 * Verifies the current pipeline and colors the pipeline status panel
	 * by the result.
	 */
    public void verifyPipeline() {
        Component component = MainWindow.instance.getCenterPane().getSelectedComponent();
        if (component instanceof mxGraphComponent) {
            Pipeline pipeline = ((PipelineGraphModel) ((mxGraphComponent) component).getGraph().getModel()).getPipeline();
            pipelineStatusIndicator.setOpaque(true);
            if (pipeline.isValid()) pipelineStatusIndicator.setBackground(Color.GREEN); else pipelineStatusIndicator.setBackground(Color.RED);
        } else pipelineStatusIndicator.setBackground(defaultPanelColor);
    }

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
        setDirty(true);
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
        setDirty(true);
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
        setDirty(true);
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
        setDirty(true);
    }

    @Override
    public void invoke(Object sender, mxEventObject evt) {
        setDirty(true);
        verifyPipeline();
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
        verifyPipeline();
    }
}

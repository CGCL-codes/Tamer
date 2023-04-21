package tico.components;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Vector;
import javax.swing.JPanel;

/**
 * Component which contains floating tool bars. The component height changes to
 * display all component tool bars.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TToolBarContainer extends JPanel {

    private static final int MINIMUM_HEIGHT = 5;

    private ToolBarEventsListener toolBarListener;

    private Vector toolBarsList;

    /**
	 * Creates a new <code>TToolBarContainer</code>.
	 */
    public TToolBarContainer() {
        super();
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        ToolBarContainerEventsListener listener = new ToolBarContainerEventsListener();
        addComponentListener(listener);
        addContainerListener(listener);
        toolBarListener = new ToolBarEventsListener();
        toolBarsList = new Vector();
    }

    /**
	 * Add a new <code>TToolBar</code> to the end of the container
	 * 
	 * @param toolBar The <code>TToolBar</code> to be added
	 */
    public void addToolBar(TToolBar toolBar) {
        toolBar.addComponentListener(toolBarListener);
        toolBarsList.add(toolBar);
        super.add(toolBar);
    }

    /**
	 * Add a new <code>toolBar</code> in the specified <code>index</code>.
	 * 
	 * @param toolBar The <code>toolBar</code> to be added
	 * @param index The tool bar insertion <code>index</code>
	 */
    public void addToolBar(TToolBar toolBar, int index) {
        toolBar.addComponentListener(toolBarListener);
        toolBarsList.add(index, toolBar);
        super.add(toolBar, index);
    }

    /**
	 * Returns the <code>toolBar</code> in the specified <code>index</code>.
	 * 
	 * @param index The <code>index</code> of the value to return
	 */
    public TToolBar getToolBar(int index) {
        return (TToolBar) toolBarsList.elementAt(index);
    }

    /**
	 * Remove the specified <code>toolBar</code> from the container.
	 * 
	 * @param toolBar The <code>toolBar</code> to be removed
	 */
    public void removeToolBar(TToolBar toolBar) {
        super.remove(toolBar);
        toolBar.removeComponentListener(toolBarListener);
        toolBarsList.remove(toolBar);
    }

    /**
	 * Remove the toolBar in the specified <code>index</code> from the container.
	 * 
	 * @param index The <code>index</code> of the toolBar to be removed
	 */
    public void removeToolBar(int index) {
        TToolBar removalToolBar = (TToolBar) toolBarsList.elementAt(index);
        super.remove(removalToolBar);
        removalToolBar.removeComponentListener(toolBarListener);
        toolBarsList.remove(index);
    }

    private void updateContainerSize() {
        int toolPanelWidth = getWidth();
        int acumulatedRowWidth = 0;
        int currentRowHeight = 0;
        int newToolPanelHeight = 0;
        for (int i = 0; i < getComponentCount(); i++) if (((TToolBar) getComponent(i)).isVisible()) {
            int currentWidth = ((TToolBar) getComponent(i)).getWidth();
            int currentHeight = ((TToolBar) getComponent(i)).getHeight();
            acumulatedRowWidth += currentWidth;
            if (acumulatedRowWidth > toolPanelWidth) {
                acumulatedRowWidth = currentWidth;
                newToolPanelHeight += currentRowHeight;
                currentRowHeight = 0;
            }
            if (currentRowHeight < currentHeight) currentRowHeight = currentHeight;
        }
        newToolPanelHeight += currentRowHeight;
        if (newToolPanelHeight < MINIMUM_HEIGHT) newToolPanelHeight = MINIMUM_HEIGHT;
        setPreferredSize(new Dimension(0, newToolPanelHeight));
        updateUI();
    }

    private class ToolBarEventsListener implements ComponentListener {

        public void componentHidden(ComponentEvent e) {
            updateContainerSize();
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
            updateContainerSize();
        }

        public void componentShown(ComponentEvent e) {
            updateContainerSize();
        }
    }

    private class ToolBarContainerEventsListener implements ComponentListener, ContainerListener {

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
            updateContainerSize();
        }

        public void componentAdded(ContainerEvent e) {
            TToolBar addedToolBar = (TToolBar) e.getChild();
            addedToolBar.setOrientation(TToolBar.HORIZONTAL);
            setComponentZOrder(addedToolBar, toolBarsList.indexOf(addedToolBar));
            updateContainerSize();
        }

        public void componentRemoved(ContainerEvent e) {
            updateContainerSize();
        }
    }
}

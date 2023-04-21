package com.rapidminer.gui.processeditor.results;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.rapidminer.gui.MainFrame;
import com.rapidminer.gui.renderer.RendererService;
import com.rapidminer.gui.tools.ProgressThread;
import com.rapidminer.gui.tools.ResourceLabel;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ResultObject;
import com.rapidminer.tools.Tools;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

/** Dockable containing a single result.
 * 
 * @author Simon Fischer
 *
 */
public class ResultTab extends JPanel implements Dockable {

    private static final long serialVersionUID = 1L;

    public static final String DOCKKEY_PREFIX = "result_";

    private Component label;

    private Component component;

    private final DockKey dockKey;

    private final String id;

    public ResultTab(String id) {
        setLayout(new BorderLayout());
        this.id = id;
        this.dockKey = new DockKey(id, "Result " + id);
        this.dockKey.setDockGroup(MainFrame.DOCK_GROUP_RESULTS);
        this.dockKey.setName(id);
        this.dockKey.setFloatEnabled(true);
        label = makeStandbyLabel();
        add(label, BorderLayout.NORTH);
    }

    /** Creates a component for this object and displays it. 
	 *  This method does not have to be called on the EDT. It executes a
	 *  time consuming task and should be called from a {@link ProgressThread}. */
    public void showResult(final ResultObject resultObject) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (label != null) {
                    remove(label);
                    label = null;
                }
                if (resultObject != null) {
                    dockKey.setName(resultObject.getName() + " (" + resultObject.getSource() + ")");
                    dockKey.setTooltip(Tools.toString(resultObject.getProcessingHistory(), " → "));
                    label = makeStandbyLabel();
                    add(label, BorderLayout.NORTH);
                } else {
                    if (id.startsWith(DOCKKEY_PREFIX + "process_")) {
                        String number = id.substring((DOCKKEY_PREFIX + "process_").length());
                        label = new ResourceLabel("resulttab.cannot_be_restored_process_result", number);
                        dockKey.setName("Result #" + number);
                    } else {
                        label = new ResourceLabel("resulttab.cannot_be_restored");
                        dockKey.setName("Result " + id);
                    }
                    ((JComponent) label).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    add(label, BorderLayout.NORTH);
                }
                if (component != null) {
                    remove(component);
                }
            }
        });
        if (resultObject != null) {
            final Component newComponent = createComponent(resultObject, null);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (label != null) {
                        remove(label);
                        label = null;
                    }
                    component = newComponent;
                    add(component, BorderLayout.CENTER);
                    if (component instanceof JComponent) {
                        dockKey.setIcon((Icon) ((JComponent) component).getClientProperty(ResultDisplayTools.CLIENT_PROPERTY_RAPIDMINER_RESULT_ICON));
                    }
                }
            });
        }
    }

    private static JComponent makeStandbyLabel() {
        JComponent label = new ResourceLabel("resulttab.creating_display");
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    /** Creates an appropriate name, appending a number to make names unique, and calls 
	 *  {@link #createVisualizationComponent(IOObject, IOContainer, String)}. */
    private JPanel createComponent(ResultObject resultObject, IOContainer resultContainer) {
        final String resultName = RendererService.getName(resultObject.getClass());
        String usedResultName = resultObject.getName();
        if (usedResultName == null) {
            usedResultName = resultName;
        }
        return ResultDisplayTools.createVisualizationComponent(resultObject, resultContainer, id + ": " + usedResultName);
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public DockKey getDockKey() {
        return dockKey;
    }

    public void freeResources() {
        if (component != null) {
            remove(component);
            component = null;
        }
    }
}

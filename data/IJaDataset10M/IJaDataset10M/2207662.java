package net.sourceforge.jruntimedesigner.actions;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.common.WidgetHolder;
import net.sourceforge.jruntimedesigner.selection.IWidgetSelectionListener;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionEvent;

public class MoveFrontAction extends AbstractWidgetAction {

    public static final String NAME = "MoveFront";

    private WidgetHolder widgetHolder;

    public MoveFrontAction(WidgetHolder widgetHolder) {
        super(NAME, widgetHolder.getController());
        this.widgetHolder = widgetHolder;
    }

    public MoveFrontAction(final JRuntimeDesignerController controller) {
        super(NAME, controller);
        selectionManager.addWidgetSelectionListener(new IWidgetSelectionListener() {

            public void widgetSelectionChanged(WidgetSelectionEvent event) {
                updateActionState();
            }
        });
        updateActionState();
    }

    protected void updateActionState() {
        setEnabled(controller.isDesignMode() && (widgetHolder != null || selectionManager.hasSelection()));
    }

    public void doAction(ActionEvent e) throws Exception {
        if (widgetHolder != null) {
            int layer = widgetHolder.getLayer();
            if (layer < 100) {
                widgetHolder.setLayer(100);
                panel.setLayer(widgetHolder, 100);
            }
        }
        if ((widgetHolder == null || widgetHolder.isSelected()) && selectionManager.hasSelection()) {
            for (IWidgetHolder selectedWidget : selectionManager.getSelectedWidgets()) {
                if (widgetHolder != null && selectedWidget == widgetHolder.getWidget()) {
                    continue;
                }
                int layer = selectedWidget.getLayer();
                if (layer < 100) {
                    selectedWidget.setLayer(100);
                    if (selectedWidget instanceof JComponent) {
                        panel.setLayer((JComponent) selectedWidget, layer);
                    }
                }
            }
        }
        panel.repaint();
        controller.setDirty();
    }
}

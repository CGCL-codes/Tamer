package com.ivis.xprocess.ui.processdesigner.print;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.swt.printing.Printer;
import org.eclipse.ui.IWorkbenchPart;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.properties.ProcessDesignerMessages;

public class PrintAction extends WorkbenchPartAction {

    public PrintAction(IWorkbenchPart part) {
        super(part);
        setToolTipText(ProcessDesignerMessages.PrintAction_toolTip);
        setImageDescriptor(UIType.print_diagram.getImageDescriptor());
    }

    @Override
    protected boolean calculateEnabled() {
        return Printer.getPrinterList().length > 0;
    }

    @Override
    public void run() {
        IWorkbenchPart part = getWorkbenchPart();
        if (part != null) {
            GraphicalViewer viewer = (GraphicalViewer) part.getAdapter(GraphicalViewer.class);
            if (viewer != null) {
                LayerManager layerManager = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
                IFigure contentLayer = layerManager.getLayer(LayerConstants.PRIMARY_LAYER);
                IFigure connectionLayer = layerManager.getLayer(LayerConstants.CONNECTION_LAYER);
                Dimension size = ((AbstractGraphicalEditPart) viewer.getRootEditPart().getContents()).getFigure().getPreferredSize();
                PrintableFigure figure = new PrintableFigure(contentLayer, connectionLayer, size);
                String diagramLabel = part.getTitle();
                PreviewDialog pd = new PreviewDialog(figure, diagramLabel, part.getSite().getShell());
                pd.open();
            }
        }
    }
}

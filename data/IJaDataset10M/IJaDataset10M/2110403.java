package org.parallelj.mda.controlflow.diagram.extension.edit.parts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.RGB;
import org.parallelj.mda.controlflow.diagram.edit.parts.Data3EditPart;
import org.parallelj.mda.controlflow.diagram.extension.ControlFlowDiagramExtendedEditorPlugin;
import org.parallelj.mda.controlflow.diagram.extension.DataModeHelper;
import org.parallelj.mda.controlflow.diagram.extension.adapters.DataModeAdapter;
import org.parallelj.mda.controlflow.diagram.extension.tools.BoundsRefreshmentWorkaround;
import org.parallelj.mda.controlflow.diagram.extension.tools.Drawer;
import org.parallelj.mda.controlflow.diagram.extension.tools.StylesHelper;
import org.parallelj.mda.controlflow.model.controlflow.Data;

public class ExtendedData3EditPart extends Data3EditPart implements IDataMode {

    public ExtendedData3EditPart(View view) {
        super(view);
        this.addAdapters(view);
    }

    /**
	 * Adds adapters on view associated with Edit Part
	 * 
	 * @param view
	 */
    private void addAdapters(View view) {
        EObject element = view.getElement();
        if (element != null) {
            element.eAdapters().add(new DataModeAdapter(this));
        }
    }

    @Override
    public void activate() {
        super.activate();
        EObject eo = ((View) this.getModel()).getElement();
        Data d = (Data) eo;
        refreshMode(d.getMode().getName());
    }

    @Override
    protected void refreshBounds() {
        BoundsRefreshmentWorkaround.refreshBounds(this, null, 46);
    }

    protected IFigure createNodeShape() {
        final EditPart editPart = this;
        IFigure figure = new DataFigure() {

            public void paintFigure(Graphics graphics) {
                int frontColor = StylesHelper.getFillColor(editPart);
                RGB rgb = FigureUtilities.integerToRGB(frontColor);
                if (rgb.red == 255 && rgb.green == 255 && rgb.blue == 255) frontColor = FigureUtilities.RGBToInteger(new RGB(220, 220, 220));
                Drawer.gradient(graphics, getBounds(), ControlFlowDiagramExtendedEditorPlugin.getDefault().getColor(frontColor), ControlFlowDiagramExtendedEditorPlugin.getDefault().getColor(FigureUtilities.RGBToInteger(new RGB(250, 245, 245))), 28);
            }
        };
        return primaryShape = figure;
    }

    public void refreshMode(String mode) {
        DataModeHelper.refreshMode(this.getPrimaryShape().getFigureDataNameFigure(), this.getFigure(), mode);
    }
}

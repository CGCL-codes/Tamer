package org.plazmaforge.studio.reportdesigner.policies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.plazmaforge.studio.reportdesigner.model.table.Table;
import org.plazmaforge.studio.reportdesigner.requests.ResizeRequest;

public class TableResizingPolicy extends AbstractVerticalElementPolicy {

    private static final class ResizeBandCommand extends Command {

        private final Table band;

        private final int originalHeight;

        private final int newHeight;

        public ResizeBandCommand(final Table band, final int delta) {
            super("Change Table Height");
            this.band = band;
            originalHeight = band.getHeight();
            newHeight = originalHeight + delta;
        }

        public void execute() {
            band.setHeight(newHeight);
        }

        public void undo() {
            band.setHeight(originalHeight);
        }
    }

    protected Command createResizeCommand(final ResizeRequest request) {
        return new ResizeBandCommand((Table) getHost().getModel(), request.getDelta());
    }

    protected Rectangle getFeedbackBounds(final ResizeRequest request) {
        final IFigure hostFigure = getHostFigure();
        final Rectangle bounds = hostFigure.getBounds().getCopy();
        hostFigure.translateToAbsolute(bounds);
        bounds.y = bounds.bottom() + request.getDelta();
        bounds.height = 1;
        return bounds;
    }
}

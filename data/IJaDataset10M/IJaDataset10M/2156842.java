package cnaf.sidoc.ide.docflows.graphics.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import cnaf.sidoc.ide.docflows.graphics.ImageResource;
import cnaf.sidoc.ide.docflows.graphics.color.DocflowColorConstants;
import cnaf.sidoc.ide.docflows.graphics.color.FontRegistry;

public class StateFigure extends Figure {

    private final Label name;

    private CompartmentFigure availableFunctionsFigure = new CompartmentFigure();

    private CompartmentFigure doActionsOnEnterFigure = new CompartmentFigure();

    private CompartmentFigure doActionsOnExitFigure = new CompartmentFigure();

    public StateFigure() {
        Font classFont = FontRegistry.INSTANCE.getFont(FontRegistry.TAHOMA, 8, SWT.BOLD);
        this.name = new Label(ImageResource.getImage(ImageResource.IMG_STATE_OBJ));
        name.setFont(classFont);
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);
        setBorder(new LineBorder(ColorConstants.black, 1));
        super.setBackgroundColor(DocflowColorConstants.lightYellow);
        setOpaque(true);
        add(name);
    }

    public void setStartState(boolean startState) {
        if (startState) {
            name.setIcon(ImageResource.getImage(ImageResource.IMG_START_STATE_OBJ));
            super.setBackgroundColor(DocflowColorConstants.darkYellow);
        } else {
            name.setIcon(ImageResource.getImage(ImageResource.IMG_STATE_OBJ));
            super.setBackgroundColor(DocflowColorConstants.lightYellow);
        }
    }

    private CompartmentFigure getAvailableFunctionsCompartment() {
        return availableFunctionsFigure;
    }

    public void setStateType(String stateType) {
        name.setText(stateType);
    }

    public void setAvailableFunctions(NodeList availableFunctions) {
        if (availableFunctions == null || availableFunctions.getLength() < 1) {
            return;
        }
        add(availableFunctionsFigure);
        Element functionElement = null;
        for (int i = 0; i < availableFunctions.getLength(); i++) {
            functionElement = (Element) availableFunctions.item(i);
            Label functionLabel = new Label(DOMUtils.getTextNodeAsString(functionElement), ImageResource.getImage(ImageResource.IMG_FUNCTION_OBJ));
            getAvailableFunctionsCompartment().add(functionLabel);
        }
    }

    public void setDoActionsOnEnter(NodeList doActionsOnEnter) {
        if (doActionsOnEnter == null || doActionsOnEnter.getLength() < 1) {
            return;
        }
        add(doActionsOnEnterFigure);
        Element actionElement = null;
        for (int i = 0; i < doActionsOnEnter.getLength(); i++) {
            actionElement = (Element) doActionsOnEnter.item(i);
            Label actionLabel = new Label(DOMUtils.getTextNodeAsString(actionElement), ImageResource.getImage(ImageResource.IMG_ACTION_ENTER_OBJ));
            getDoActionsOnEnterCompartment().add(actionLabel);
        }
    }

    public void setDoActionsOnExit(NodeList doActionsOnExit) {
        if (doActionsOnExit == null || doActionsOnExit.getLength() < 1) {
            return;
        }
        add(doActionsOnExitFigure);
        Element actionElement = null;
        for (int i = 0; i < doActionsOnExit.getLength(); i++) {
            actionElement = (Element) doActionsOnExit.item(i);
            Label actionLabel = new Label(DOMUtils.getTextNodeAsString(actionElement), ImageResource.getImage(ImageResource.IMG_ACTION_EXIT_OBJ));
            getDoActionsOnExitCompartment().add(actionLabel);
        }
    }

    public CompartmentFigure getDoActionsOnEnterCompartment() {
        return doActionsOnEnterFigure;
    }

    public CompartmentFigure getDoActionsOnExitCompartment() {
        return doActionsOnExitFigure;
    }
}

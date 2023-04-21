package org.openscience.jchempaint;

import java.awt.Rectangle;
import java.util.Hashtable;
import javax.vecmath.Point2d;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;
import org.openscience.jchempaint.controller.ControllerModuleAdapter;
import org.openscience.jchempaint.controller.IChemModelRelay;
import org.openscience.jchempaint.renderer.Renderer;
import org.openscience.jchempaint.renderer.RendererModel;

public class SwingPopupModule extends ControllerModuleAdapter {

    private static ILoggingTool logger = LoggingToolFactory.createLoggingTool(SwingPopupModule.class);

    private static Hashtable<String, JChemPaintPopupMenu> popupMenus = new Hashtable<String, JChemPaintPopupMenu>();

    private RenderPanel rendererPanel;

    private String ID;

    public SwingPopupModule(RenderPanel renderer, IChemModelRelay chemModelRelay) {
        super(chemModelRelay);
        this.rendererPanel = renderer;
    }

    public String getDrawModeString() {
        return "Popup menu";
    }

    public void mouseClickedDownRight(Point2d worldCoord) {
        popupMenuForNearestChemObject(rendererPanel.getRenderer().toScreenCoordinates(worldCoord.x, worldCoord.y));
    }

    /**
	 *  Sets the popupMenu attribute of the Controller2D object
	 *
	 *@param  someClass  The new popupMenu value
	 *@param  jchemPaintPopupMenu        The new popupMenu value
	 */
    public void setPopupMenu(Class someClass, JChemPaintPopupMenu jchemPaintPopupMenu) {
        SwingPopupModule.popupMenus.put(someClass.getName(), jchemPaintPopupMenu);
    }

    /**
	 *  Returns the popup menu for this IChemObject if it is set, and null
	 *  otherwise.
	 *
	 *@param  someClass  Description of the Parameter
	 *@return             The popupMenu value
	 */
    public JChemPaintPopupMenu getPopupMenu(Class classSearched) {
        logger.debug("Searching popup for: ", classSearched.getName());
        while (classSearched.getName().startsWith("org.openscience.cdk")) {
            logger.debug("Searching popup for: ", classSearched.getName());
            if (SwingPopupModule.popupMenus.containsKey(classSearched.getName())) {
                return (JChemPaintPopupMenu) SwingPopupModule.popupMenus.get(classSearched.getName());
            } else {
                logger.debug("  recursing into super class");
                classSearched = classSearched.getSuperclass();
            }
        }
        return null;
    }

    private void popupMenuForNearestChemObject(Point2d mouseCoords) {
        Renderer renderer = rendererPanel.getRenderer();
        RendererModel rendererModel = renderer.getRenderer2DModel();
        IChemObject objectInRange = rendererModel.getHighlightedAtom();
        if (objectInRange == null) objectInRange = rendererModel.getHighlightedBond();
        IReactionSet reactionSet = rendererPanel.getChemModel().getReactionSet();
        if (objectInRange == null && reactionSet != null && reactionSet.getReactionCount() > 0) {
            for (int i = 0; i < reactionSet.getReactionCount(); i++) {
                Rectangle reactionbounds = renderer.calculateDiagramBounds(reactionSet.getReaction(i));
                if (reactionbounds.contains(mouseCoords.x, mouseCoords.y)) objectInRange = reactionSet.getReaction(i);
            }
        }
        if (objectInRange == null) objectInRange = chemModelRelay.getIChemModel();
        JChemPaintPopupMenu popupMenu = getPopupMenu(objectInRange.getClass());
        if (popupMenu != null) {
            popupMenu.setSource(objectInRange);
            logger.debug("Set popup menu source to: ", objectInRange);
            popupMenu.show(rendererPanel, (int) mouseCoords.x, (int) mouseCoords.y);
        } else {
            logger.warn("Popup menu is null! Could not set source!");
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

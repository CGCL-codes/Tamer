package fr.itris.glips.svgeditor.display.handle;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.manager.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import java.io.*;
import org.w3c.dom.*;

/**
 * the class handling all information and data for the svg file display
 * @author ITRIS, Jordi SUC
 */
public class SVGHandle {

    /**
	 * the name of this svg handle
	 */
    private String name;

    /**
	 * the internal frame that will be displayed in the desktop
	 */
    private SVGFrame svgFrame;

    /**
	 * the boolean set to true if the SVG picture has been modified 
	 */
    private boolean modified = false;

    /**
	 * the undo/redo 
	 */
    private UndoRedo undoRedo = new UndoRedo(this);

    /**
	 * the selection manager
	 */
    private Selection selection;

    /**
	 * the manager of the svg resources
	 */
    private SVGResourcesManager svgResourcesManager = new SVGResourcesManager(this);

    /**
	 * the svg dom normalizer
	 */
    private SVGDOMNormalizer svgDOMNormalizer = new SVGDOMNormalizer(this);

    /**
	 * the svg dom listeners manager
	 */
    private SVGDOMListenerManager svgDOMListenerManager = new SVGDOMListenerManager(this);

    /**
	 * the manager of the svg elements
	 */
    private SVGElementsManager svgElementsManager = new SVGElementsManager(this);

    /**
	 * the object provided methods to handle scale changes
	 */
    private TransformsManager handleScaleManager = new TransformsManager(this);

    /**
	 * the constuctor of the class
	 * @param name the name that will be linked with the SVG picture
	 */
    public SVGHandle(String name) {
        svgFrame = new SVGFrame(this);
        setName(name);
    }

    /**
	 * creates the selection handler on the svg canvas 
	 * (can only be called by the svg canvas object)
	 */
    public void createSelection() {
        selection = new Selection(this);
    }

    /**
	 * @return the selection manager
	 */
    public Selection getSelection() {
        return selection;
    }

    /**
	 * @return the svg resources manager
	 */
    public SVGResourcesManager getSvgResourcesManager() {
        return svgResourcesManager;
    }

    /**
	 * @return the svg dom normalizer
	 */
    public SVGDOMNormalizer getSvgDOMNormalizer() {
        return svgDOMNormalizer;
    }

    /**
	 * @return the svg dom listeners manager
	 */
    public SVGDOMListenerManager getSvgDOMListenerManager() {
        return svgDOMListenerManager;
    }

    /**
	 * @return the svg elements manager
	 */
    public SVGElementsManager getSvgElementsManager() {
        return svgElementsManager;
    }

    /**
	 * @return the object provided methods to handle conversion 
	 * of geometry shape on the canvas
	 */
    public TransformsManager getTransformsManager() {
        return handleScaleManager;
    }

    /**
	 * @return the undo/redo manager
	 */
    public UndoRedo getUndoRedo() {
        return undoRedo;
    }

    /**
	 * @return true if the SVG picture has been modified
	 */
    public boolean isModified() {
        return modified;
    }

    /**
	 * sets that the svg document has been modified
	 * @param modified must be true if the svg picture has been modified
	 */
    public void setModified(boolean modified) {
        this.modified = modified;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (svgFrame != null) {
                    svgFrame.setSVGFrameLabel(name);
                }
            }
        });
    }

    /**
	 * @return the svg frame linked to this handle
	 */
    public SVGFrame getSVGFrame() {
        return svgFrame;
    }

    /**
	 * @return the svg scrollpane linked to this handle
	 */
    public SVGScrollPane getScrollPane() {
        return svgFrame.getScrollpane();
    }

    /**
	 * @return the svg canvas linked to this handle
	 */
    public SVGCanvas getCanvas() {
        if (svgFrame != null) {
            return svgFrame.getCanvas();
        }
        return null;
    }

    /**
	 * @return the name of the handle, that is the name of the svg document
	 */
    public String getName() {
        return name;
    }

    /**
	 * sets the new name of the handle
	 * @param name the new name
	 */
    public void setName(String name) {
        this.name = name;
        svgFrame.setSVGFrameLabel(name);
    }

    /**
	 * @return the short name of this handle
	 */
    public String getShortName() {
        String shortName = "";
        try {
            File file = new File(name);
            shortName = file.getName();
        } catch (Exception ex) {
            shortName = null;
        }
        return shortName;
    }

    /**
	 * disposes this handle
	 */
    public void dispose() {
        svgFrame.setVisible(false);
        Editor.getEditor().getHandlesManager().removeHandleWithoutDisposing(getName());
        HandlesManager.handleDisposer.disposeHandle(this);
        svgDOMListenerManager.dispose();
        svgResourcesManager.dispose();
        undoRedo.dispose();
        if (selection != null) {
            selection.dispose();
        }
        if (svgFrame != null) {
            svgFrame.disposeFrame();
        }
        name = null;
        svgFrame = null;
        undoRedo = null;
        selection = null;
        svgResourcesManager = null;
        svgDOMNormalizer = null;
        svgDOMListenerManager = null;
        svgElementsManager = null;
        handleScaleManager = null;
    }

    /**
	 * closes the svg handle
	 */
    public void close() {
        Editor.getEditor().getIOManager().getFileCloseManager().closeHandle(this, getSVGFrame());
    }

    /**
	 * @return whether this handle is the current handle or not
	 */
    public boolean isSelected() {
        SVGHandle currentHandle = Editor.getEditor().getHandlesManager().getCurrentHandle();
        if (currentHandle != null && currentHandle.equals(this)) {
            return true;
        }
        return false;
    }

    /**
	 * enqueues the given runnable
	 * @param runnable a runnable
	 * @param elements the set of the elements that are modified
	 * @param inAWTThread whether the runnable should be invoked in the AWT thread
	 */
    public void enqueue(final Runnable runnable, final Set<Element> elements, boolean inAWTThread) {
        if (runnable != null) {
            if (inAWTThread && !SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        enqueue(runnable, elements);
                    }
                });
            } else {
                enqueue(runnable, elements);
            }
        }
    }

    /**
	 * enqueues the given runnable
	 * @param runnable a runnable
	 * @param elements the set of the elements that are modified
	 */
    private void enqueue(final Runnable runnable, final Set<Element> elements) {
        Area oldBoundsArea = null;
        Rectangle2D bounds = null, bounds2 = null;
        for (Element element : elements) {
            bounds = getSvgElementsManager().getSensitiveBounds(element);
            bounds2 = getSvgElementsManager().getNodeGeometryBounds(element);
            if (bounds != null && bounds.getWidth() > 0 && bounds.getHeight() > 0 && bounds2 != null && bounds2.getWidth() > 0 && bounds2.getHeight() > 0) {
                bounds.add(bounds2);
                bounds = getNormalizedRectangle(bounds);
                if (oldBoundsArea == null) {
                    oldBoundsArea = new Area(bounds);
                } else {
                    oldBoundsArea.add(new Area(bounds));
                }
            }
        }
        runnable.run();
        Area newBoundsArea = null;
        for (Element element : elements) {
            bounds = getSvgElementsManager().getSensitiveBounds(element);
            bounds2 = getSvgElementsManager().getNodeGeometryBounds(element);
            if (bounds != null && bounds.getWidth() > 0 && bounds.getHeight() > 0 && bounds2 != null && bounds2.getWidth() > 0 && bounds2.getHeight() > 0) {
                bounds.add(bounds2);
                bounds = getNormalizedRectangle(bounds);
                if (newBoundsArea == null) {
                    newBoundsArea = new Area(bounds);
                } else {
                    newBoundsArea.add(new Area(bounds));
                }
            }
        }
        Area allArea = null;
        if (oldBoundsArea != null) {
            allArea = new Area(oldBoundsArea);
            if (newBoundsArea != null) {
                allArea.add(newBoundsArea);
            }
        } else if (newBoundsArea != null) {
            allArea = new Area(newBoundsArea);
        }
        if (allArea != null) {
            svgFrame.getCanvas().requestUpdateContent(allArea);
            if (elements.size() > 0) {
                selection.setBlockSelectionItemsPaint(false);
            }
        }
    }

    /**
	 * enqueues the given runnable and waits until it's finished
	 * @param runnable a runnable
	 * @param elements the set of the elements that are modified
	 */
    public void enqueueAndWait(final Runnable runnable, final Set<Element> elements) {
        if (runnable != null) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        enqueue(runnable, elements);
                    }
                });
            } catch (Exception ex) {
            }
        }
    }

    /**
	 * computes the rectangle used for computing the bounds for redrawing shapes
	 * @param rect a rectangle
	 * @return the rectangle used for computing the bounds for redrawing shapes
	 */
    protected Rectangle getNormalizedRectangle(Rectangle2D rect) {
        int x0 = (int) Math.floor(rect.getX());
        int y0 = (int) Math.floor(rect.getY());
        int x1 = (int) Math.ceil(rect.getX() + rect.getWidth());
        int y1 = (int) Math.ceil(rect.getY() + rect.getHeight());
        return new Rectangle(x0 - 2, y0 - 2, x1 - x0 + 5, y1 - y0 + 5);
    }

    /**
	 * picks the color at the given point on a canvas
	 * @param point a point
	 * @return the color corresponding to the given point
	 */
    public Color pickColor(Point point) {
        Color color = new Color(255, 255, 255);
        if (point != null) {
            BufferedImage image = getScrollPane().getSVGCanvas().getOffscreen();
            int pos = image.getRGB(point.x, point.y);
            ColorModel model = image.getColorModel();
            if (pos != 0) {
                color = new Color(model.getRed(pos), model.getGreen(pos), model.getBlue(pos));
            }
        }
        return color;
    }
}

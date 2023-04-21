package visreed.extension.javaCC.view.layout;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import visreed.extension.javaCC.view.JavaCCLinkNodeView;
import visreed.view.TerminalNodeView;
import visreed.view.VisreedDropZone;
import visreed.view.VisreedNodeView;
import visreed.view.layout.TerminalLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCLinkLayoutManager extends TerminalLayoutManager {

    public static final int GO_TO_DEF_WIDTH_PIXEL = 16;

    public static final int GO_TO_DEF_HEIGHT_PIXEL = 16;

    public static final int GO_TO_DEF_TOP_OFFSET_PIXEL = 5;

    public static final int GO_TO_DEF_RIGHT_OFFSET_PIXEL = 5;

    @Override
    public void layoutZone(VisreedNodeView view, VisreedDropZone zone) {
        String zoneIdStr = zone.getId();
        if (zoneIdStr.equals(TerminalNodeView.ID_DROPZONE_TERMINAL)) {
            super.layoutZone(view, zone);
        } else if (zoneIdStr.equals(JavaCCLinkNodeView.ID_DROPZONE_GO_TO_DEFINITION)) {
            Rectangle2D parentExtent = view.getNextShapeExtent();
            Rectangle2D shape = new Rectangle.Double(parentExtent.getMaxX() - GO_TO_DEF_WIDTH_PIXEL - GO_TO_DEF_RIGHT_OFFSET_PIXEL, parentExtent.getY() + GO_TO_DEF_TOP_OFFSET_PIXEL, GO_TO_DEF_WIDTH_PIXEL, GO_TO_DEF_HEIGHT_PIXEL);
            zone.setNextShape(shape);
        }
    }

    @Override
    public void layoutNode(VisreedNodeView nv) {
        if (nv == null) {
            return;
        }
        String text = nv.getNode().getPayload().getDescription();
        if (text == null) {
            text = "";
        }
        double contentWidth = ((TerminalNodeView) nv).getDesiredWidth();
        contentWidth += GO_TO_DEF_WIDTH_PIXEL;
        contentWidth += GO_TO_DEF_RIGHT_OFFSET_PIXEL;
        Rectangle2D extent = nv.getNextShapeExtent();
        Rectangle.union(extent, new Rectangle2D.Double(extent.getX(), extent.getY(), HSPACE_PIXEL * 2 + contentWidth, VSPACE_PIXEL * 2), extent);
        nv.setNextShape(extent);
        nv.placeNext(0, 0);
    }

    private static JavaCCLinkLayoutManager instance = new JavaCCLinkLayoutManager();

    /**
     * Gets the globally unique instance of this class. (Singleton Pattern)
     * @return
     */
    public static VisreedNodeLayoutManager getInstance() {
        return instance;
    }
}

package org.sgx.espinillo.client.impl1.tools;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.sgx.espinillo.client.impl1.Constants;
import org.sgx.espinillo.client.impl1.commands.ChangeShapeAttrsCmd;
import org.sgx.espinillo.client.impl1.commands.CreateShapeCmd;
import org.sgx.espinillo.client.impl1.util.ShapeUtil;
import org.sgx.espinillo.client.impl1.util.ToolUtil;
import org.sgx.espinillo.client.model.Document;
import org.sgx.espinillo.client.model.tool.AbstractTool;
import org.sgx.raphael4gwt.raphael.Paper;
import org.sgx.raphael4gwt.raphael.PathCmd;
import org.sgx.raphael4gwt.raphael.Raphael;
import org.sgx.raphael4gwt.raphael.Rect;
import org.sgx.raphael4gwt.raphael.Set;
import org.sgx.raphael4gwt.raphael.Shape;
import org.sgx.raphael4gwt.raphael.base.Attrs;
import org.sgx.raphael4gwt.raphael.base.Point;
import org.sgx.raphael4gwt.raphael.event.DDListener;
import org.sgx.raphael4gwt.raphael.event.ForEachCallback;
import org.sgx.raphael4gwt.raphael.event.MouseEventListener;
import org.sgx.raphael4gwt.raphael.ft.FTCallback;
import org.sgx.raphael4gwt.raphael.ft.FTOptions;
import org.sgx.raphael4gwt.raphael.ft.FTSubject;
import org.sgx.raphael4gwt.raphael.ft.FreeTransform;
import org.sgx.raphael4gwt.raphael.jsutil.JsUtil;
import org.sgx.raphael4gwt.raphael.util.GUIUtil;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * draw shapes using smoothQuadBezierCurveTo curves
 * @author sg
 *
 */
public class FTTool extends AbstractTool {

    static Logger logger = Logger.getLogger("FTTool");

    private Rect mask;

    private Paper paper;

    private MouseEventListener clickHandler;

    protected FreeTransform ft;

    protected Attrs originalAttrs;

    protected Shape feedback;

    protected Shape fs;

    public FTTool(Document doc) {
        super(doc, Toolbar1.TOOL_FT, Toolbar1.TOOL_FT_LABEL);
    }

    protected Set filterOnlyShapes(Set els) {
        if (mask != null) els.exclude(mask);
        els.filter(new ForEachCallback() {

            @Override
            public boolean call(Shape shape, int index) {
                boolean result = !ShapeUtil.isShape(shape);
                logger.log(Level.INFO, "AAAA: " + result);
                return result;
            }
        });
        return els;
    }

    @Override
    public void install() {
        super.install();
        paper = getDocument().getPaper();
        mask = ToolUtil.getInstance().showPaperMask(getDocument());
        clickHandler = new MouseEventListener() {

            @Override
            public void notifyMouseEvent(final NativeEvent e) {
                Point coords = Raphael.getCoordsInPaper(paper, e);
                Set els = ToolUtil.getShapesUnderPoint(paper, coords);
                els = filterOnlyShapes(els);
                fs = els.firstShape();
                feedback = fs.clone();
                fs.hide();
                getDocument().setSelection(els);
                final FTOptions ftOpts = FTOptions.create();
                ftOpts.setRotate(true);
                ftOpts.setAttrs(Attrs.create().fill("red").strokeWidth(4).stroke("blue"));
                final FTCallback callback = new FTCallback() {

                    @Override
                    public void call(FTSubject s, JsArrayString events) {
                        if (JsUtil.arrayContains(events, FreeTransform.EVENT_ROTATE_END) || JsUtil.arrayContains(events, FreeTransform.EVENT_SCALE_END) || JsUtil.arrayContains(events, FreeTransform.EVENT_DRAG_END)) {
                            doTransformation();
                        }
                    }
                };
                originalAttrs = fs.attr();
                ft = paper.freeTransform(feedback, ftOpts, callback);
            }
        };
        mask.click(clickHandler);
    }

    void doTransformation() {
        Attrs newAttrs = feedback.attr();
        ChangeShapeAttrsCmd cmd = new ChangeShapeAttrsCmd(getDocument(), fs, newAttrs);
        getDocument().execute(cmd);
    }

    /**
	 * return the union and rest the intersection of both sets
	 * @param curSel
	 * @param els
	 * @return
	 */
    protected Set getSelDiff(Set s1, Set s2) {
        return s1.add(s2).substract(s1.intersect(s2));
    }

    public static Point getPaperPosition(Paper p) {
        return Raphael.createPoint(p.getCanvasElement().getOffsetLeft() + p.getCanvasElement().getParentElement().getAbsoluteLeft(), p.getCanvasElement().getOffsetTop() + p.getCanvasElement().getParentElement().getAbsoluteTop());
    }

    @Override
    public void uninstall() {
        super.uninstall();
        ToolUtil.getInstance().hidePaperMask(getDocument());
        try {
            if (mask != null) mask.undrag();
            if (clickHandler != null) mask.unclick(clickHandler);
            if (ft != null) ft.unplug();
        } catch (Exception e) {
        }
    }
}

package org.columba.calendar.ui.calendar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import com.miginfocom.ashape.AShapeUtil;
import com.miginfocom.ashape.interaction.DefaultInteractionBroker;
import com.miginfocom.ashape.interaction.MouseKeyInteractor;
import com.miginfocom.ashape.shapes.AShape;
import com.miginfocom.ashape.shapes.ContainerAShape;
import com.miginfocom.ashape.shapes.DrawAShape;
import com.miginfocom.ashape.shapes.FeatherAShape;
import com.miginfocom.ashape.shapes.FillAShape;
import com.miginfocom.ashape.shapes.RootAShape;
import com.miginfocom.ashape.shapes.TextAShape;
import com.miginfocom.calendar.datearea.DefaultDateArea;
import com.miginfocom.util.command.DefaultCommand;
import com.miginfocom.util.gfx.GfxUtil;
import com.miginfocom.util.gfx.RoundRectangle;
import com.miginfocom.util.gfx.SliceSpec;
import com.miginfocom.util.gfx.geometry.AbsRect;
import com.miginfocom.util.gfx.geometry.PlaceRect;
import com.miginfocom.util.gfx.geometry.numbers.AtEnd;
import com.miginfocom.util.gfx.geometry.numbers.AtStart;
import com.miginfocom.util.states.GenericStates;

public class ActivityShapeFactory {

    public static final String DEFAULT_CONTAINER_SHAPE_NAME = "defaultContainer";

    public static final String DEFAULT_BACKGROUND_SHAPE_NAME = "defaultBackground";

    public static final String DEFAULT_TITLE_TEXT_SHAPE_NAME = "defaultTitleText";

    public static final String DEFAULT_MAIN_TEXT_SHAPE_NAME = "defaultMainText";

    public static final String DEFAULT_OUTLINE_SHAPE_NAME = "defaultOutline";

    public static final String DEFAULT_SHADOW_SHAPE_SHAPE_NAME = "defaultShadowShape";

    public static final String DEFAULT_SHADOW_SHAPE_NAME = "defaultShadow";

    /**
	 * Creates the default shape.
	 * 
	 * @param dimension
	 *            <code>SwingConstants.VERTICAL</code> or
	 *            <code>SwingConstants.HORIZONTAL</code>.
	 */
    public static RootAShape createDefaultShape(int dimension) {
        Color bgPaint = new Color(0, 0, 255, 40);
        Color outlinePaint = new Color(100, 100, 150);
        Color textPaint = new Color(50, 50, 50);
        Color shadowPaint = null;
        Font textFont = UIManager.getFont("Label.font");
        RootAShape root = new RootAShape();
        ContainerAShape container = new ContainerAShape(DEFAULT_CONTAINER_SHAPE_NAME, AbsRect.FILL);
        FillAShape bgAShape = new FillAShape(DEFAULT_BACKGROUND_SHAPE_NAME, new RoundRectangle(0, 0, 1, 1, 8, 8), AbsRect.FILL_INSIDE, bgPaint, GfxUtil.AA_HINT_ON);
        PlaceRect titleTextAbsRect = new AbsRect(new AtStart(2), new AtStart(1), new AtEnd(0), new AtStart(14), null, null, null);
        TextAShape titleText = new TextAShape(DEFAULT_TITLE_TEXT_SHAPE_NAME, "$startTime$ - $endTimeExcl$ ($timeZoneShort$)", titleTextAbsRect, TextAShape.TYPE_SINGE_LINE, textFont, textPaint, new AtStart(0), new AtStart(-3), GfxUtil.AA_HINT_ON);
        titleText.setAttribute(AShape.A_CLIP_TYPE, AShape.CLIP_PARENT_BOUNDS);
        PlaceRect mainTextAbsRect = new AbsRect(new AtStart(2), new AtStart(16), new AtEnd(0), new AtEnd(0), null, null, null);
        TextAShape mainText = new TextAShape(DEFAULT_MAIN_TEXT_SHAPE_NAME, "$summary$", mainTextAbsRect, TextAShape.TYPE_WRAP_TEXT, textFont, textPaint, new AtStart(0), new AtStart(0), GfxUtil.AA_HINT_ON);
        DrawAShape outlineAShape = new DrawAShape(DEFAULT_OUTLINE_SHAPE_NAME, new RoundRectangle(0, 0, 1, 1, 8, 8), AbsRect.FILL, outlinePaint, new BasicStroke(1f), GfxUtil.AA_HINT_ON);
        outlineAShape.setAttribute(AShape.A_MOUSE_CURSOR, Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        outlineAShape.setAttribute(AShape.A_REPORT_HIT_AREA, Boolean.TRUE);
        PlaceRect bgAbsRect = new AbsRect(new AtStart(0), new AtStart(0), new AtEnd(0), new AtEnd(0), null, null, new Insets(-2, -2, 2, 2));
        FillAShape filledShadow = new FillAShape(DEFAULT_SHADOW_SHAPE_SHAPE_NAME, new RoundRectangle(0, 0, 1, 1, 5, 5), bgAbsRect, shadowPaint, GfxUtil.AA_HINT_ON);
        SliceSpec shwSI = new SliceSpec(new Insets(10, 10, 10, 10), SliceSpec.TYPE_TILE_CUT, SliceSpec.OPT_BORDER);
        FeatherAShape shadowShape = new FeatherAShape(DEFAULT_SHADOW_SHAPE_NAME, filledShadow, new Color(255, 255, 255, 0), 5, shwSI);
        bgAShape.addSubShape(titleText);
        bgAShape.addSubShape(mainText);
        container.addSubShape(shadowShape);
        container.addSubShape(bgAShape);
        container.addSubShape(outlineAShape);
        root.addSubShape(container);
        root.setRepaintPadding(new Insets(4, 4, 4, 4));
        if (dimension == SwingConstants.VERTICAL) {
            AShapeUtil.enableMouseOverCursor(root);
            AShapeUtil.enableMouseOverState(outlineAShape);
            AShapeUtil.setResizeBoxes(outlineAShape, dimension, 4);
            Integer button = new Integer(MouseEvent.BUTTON1);
            AShapeUtil.addMouseFireEvent(outlineAShape, MouseKeyInteractor.MOUSE_PRESS, DefaultDateArea.AE_SELECTED_PRESSED, true, false, button);
            AShapeUtil.addMouseFireEvent(outlineAShape, MouseKeyInteractor.MOUSE_PRESS, DefaultDateArea.AE_DRAG_PRESSED, true, true, button);
            DefaultCommand entCmd = new DefaultCommand(DefaultInteractionBroker.CMD_FIRE_INTERACTION_EVENT, null, DefaultDateArea.AE_MOUSE_ENTERED, null);
            DefaultCommand exitCmd = new DefaultCommand(DefaultInteractionBroker.CMD_FIRE_INTERACTION_EVENT, null, DefaultDateArea.AE_MOUSE_EXITED, null);
            AShapeUtil.addEnterExitCommands(outlineAShape, entCmd, exitCmd, true);
            AShapeUtil.addMouseFireEvent(outlineAShape, MouseKeyInteractor.MOUSE_CLICK, DefaultDateArea.AE_CLICKED, true, false, button);
            AShapeUtil.addMouseFireEvent(outlineAShape, MouseKeyInteractor.MOUSE_DOUBLE_CLICK, DefaultDateArea.AE_DOUBLE_CLICKED, true, true, button);
            AShapeUtil.addMouseFireEvent(outlineAShape, MouseKeyInteractor.MOUSE_POPUP_TRIGGER, DefaultDateArea.AE_POPUP_TRIGGER, true, true, null);
            AShapeUtil.addMouseEventBlock(outlineAShape, false, new Integer(MouseEvent.MOUSE_MOVED));
        } else {
            AShapeUtil.enableMouseOverCursor(root);
            AShapeUtil.enableMouseOverState(outlineAShape);
            Integer button = new Integer(MouseEvent.BUTTON1);
            AShapeUtil.addMouseFireEvent(outlineAShape, MouseKeyInteractor.MOUSE_PRESS, DefaultDateArea.AE_SELECTED_PRESSED, true, false, button);
            AShapeUtil.addMouseFireEvent(outlineAShape, MouseKeyInteractor.MOUSE_PRESS, DefaultDateArea.AE_DRAG_PRESSED, true, true, button);
            AShapeUtil.addMouseEventBlock(outlineAShape, false, new Integer(MouseEvent.MOUSE_MOVED));
        }
        AShapeUtil.setStateOverride(outlineAShape, GenericStates.SELECTED, AShape.A_STROKE, new BasicStroke(2.5f));
        return root;
    }
}

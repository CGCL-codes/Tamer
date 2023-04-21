package jpicedt.format.input.pstricks;

import jpicedt.format.input.util.*;
import jpicedt.format.input.latex.LaTeXPutExpression;
import jpicedt.graphic.*;
import jpicedt.graphic.model.*;
import static jpicedt.format.input.util.ExpressionConstants.*;
import static jpicedt.graphic.model.PicAttributeName.*;
import static jpicedt.graphic.model.StyleConstants.*;
import static jpicedt.graphic.model.PicText.*;

/**
 * Parses PsTricks boxes, i.e. (depending on the "type" parameter of the constructor)
 * <ul>
 * <li> \\psframebox[param]{text}}
 * <li> \\psovalbox[param]{text}}
 * <li> \\pscirclebox[param]{text}}
 * </ul>
 * This expression is to be used as a child expression of either
 * {@link jpicedt.format.input.latex.LaTeXPutExpression#LaTeXPutExpression jpicedt.format.input.latex.LaTeXPutExpression} or 
 * {@link PsRPutExpression PsRPutExpression}.
 * <p>
 * The parsing code makes use of the following Pool's key/value pairs (as set by the enclosing XXXPutExpression beforehands):
 * <ul>
 * <li> KEY_RPUT_VALIGN: \\rput's [bBt] vertical alignment parameter</li>
 * <li> KEY_RPUT_HALIGN: \\rput's [lr] horizontal alignment parameter </li>
 * <li> KEY_RPUT_POINT: \\rput's (x,y) KEY_RPUT_VALIGNparameter </li>
 * <li> KEY_RPUT_ROTATION: \\rput's {rotation} parameter </li>
 * </ul>
 * For each of these four keys, permitted values are the same as in PicAttributeSet. For instance, the value
 * associated with KEY_RPUT_VALIGN may be TEXT_VALIGN_BOTTOM, TEXT_VALIGN_BASELINE, ...
 * <p>
 * @author Sylvain Reynal
 * @since jpicedt 1.4pre2
 * @version $Id: PsBox.java,v 1.8 2011/07/23 05:15:24 vincentb1 Exp $ 
 */
public class PsBox extends SequenceExpression {

    /** expects a \\psframebox macro */
    public static final String RECTANGLE_BOX = "\\psframebox";

    /** expects a \\pscirclebox macro */
    public static final String CIRCLE_BOX = "\\pscirclebox";

    /** expects a \\psovalbox macro */
    public static final String OVAL_BOX = "\\psovalbox";

    private String type;

    private Pool pool;

    /**
	 * @param type RECTANGLE_BOX, CIRCLE_BOX or OVAL_BOX depending on the expected box type. 
	 */
    public PsBox(Pool pl, String type) {
        super(true);
        this.pool = pl;
        this.type = type;
        this.add(new PSTInstanciationExpression(type, new PicText(), pool));
        this.add(new OptionalExpression(new StarExpression(pool)));
        this.add(WHITE_SPACES_OR_EOL);
        this.add(new OptionalExpression(new EnclosingExpression("[", new PSTParametersExpression(pool, Pool.CURRENT_OBJ_ATTRIBUTES), "]")));
        this.add(WHITE_SPACES_OR_EOL);
        this.add(new EnclosedText());
    }

    public void action(ParserEvent e) {
        if (DEBUG) System.out.println(e);
        PicPoint putPoint = pool.get(PsRPutExpression.KEY_RPUT_POINT);
        if (putPoint == null) putPoint = pool.get(LaTeXPutExpression.KEY_PUT_POINT);
        if (putPoint == null) putPoint = new PicPoint();
        ((PicText) (pool.currentObj)).setCtrlPt(PicText.P_ANCHOR, putPoint, null);
        HorAlign horAlign = pool.get(PsRPutExpression.KEY_RPUT_HALIGN);
        VertAlign vertAlign = pool.get(PsRPutExpression.KEY_RPUT_VALIGN);
        if (horAlign != null) pool.currentObj.setAttribute(TEXT_HOR_ALIGN, horAlign);
        if (vertAlign != null) pool.currentObj.setAttribute(TEXT_VERT_ALIGN, vertAlign);
        if (type == RECTANGLE_BOX) ((PicText) (pool.currentObj)).setFrameType(FrameStyle.RECTANGLE); else if (type == CIRCLE_BOX) ((PicText) (pool.currentObj)).setFrameType(FrameStyle.CIRCLE);
        if (type == OVAL_BOX) ((PicText) (pool.currentObj)).setFrameType(FrameStyle.OVAL);
        Double rotation = (Double) pool.get(PsRPutExpression.KEY_RPUT_ROTATION);
        if (rotation != null) pool.currentObj.setAttribute(TEXT_ROTATION, rotation);
    }

    /**
	 * handles {text} content (for PsBox) by setting PicText's text content, replacing
	 * linefeeds by whitespaces beforehands (as TeX does...)
	 */
    class EnclosedText extends EnclosingExpression {

        public EnclosedText() {
            super("{", null, "}");
        }

        public void action(ParserEvent e) {
            if (DEBUG) System.out.println(e);
            String txt = getEnclosedString().replace('\n', ' ');
            txt = Context.removeRedundantWhiteSpaces(txt);
            ((PicText) (pool.currentObj)).setText(txt);
        }
    }
}

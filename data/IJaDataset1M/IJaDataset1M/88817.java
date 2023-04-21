package mw.client.gui.arrow;

public class ArrowFactory {

    public enum ArrowType {

        LINE, WITH_ARROWHEAD, BEZIER
    }

    ;

    public static Arrow getArrow(int from, ArrowType arrowType) {
        if (arrowType.equals(ArrowType.WITH_ARROWHEAD)) {
            return new LineArrowWithArrowHead(from);
        } else if (arrowType.equals(ArrowType.BEZIER)) {
            return new BezierArrow(from);
        } else {
            return new LineArrow(from);
        }
    }

    private static final long serialVersionUID = 1L;
}

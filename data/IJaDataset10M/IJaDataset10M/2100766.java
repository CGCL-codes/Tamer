package gumbo.ardor3d.app;

public class GraphicProps {

    private GraphicProps() {
    }

    /**
	 * Specifies the graphic platform type. Type is String: Name of a GraphicType
	 * enum constant.
	 */
    public static final String GRAPHIC_TYPE = "gumbo.ardor3d.app.graphic.GraphicProps.GRAPHIC_TYPE";

    /**
	 * If present and true, enables debugging of the render portion of the
	 * graphic life-cycle. Type is boolean.
	 */
    public static final String DEBUG_RENDER_CYCLE = "gumbo.ardor3d.app.graphic.GraphicProps.DEBUG_RENDER_CYCLE";

    /**
	 * If present and true, enables debugging of view geometry each time a
	 * view's geometry updates. Type is boolean.
	 */
    public static final String DEBUG_VIEW_GEOMETRY = "gumbo.ardor3d.app.graphic.GraphicProps.DEBUG_VIEW_GEOMETRY";

    /**
	 * If present and true, enables debugging of shadow generation (such as that
	 * produced by ParallelSplitShadowMap). Ignored if no shadow generation.
	 * Type is boolean.
	 */
    public static final String DEBUG_SHADOWS = "gumbo.ardor3d.app.graphic.GraphicProps.DEBUG_SHADOWS";
}

package net.community.chest.jfree.jfreechart.chart.renderer.area;

import org.jfree.chart.renderer.category.StackedAreaRenderer;

/**
 * <P>Copyright GPLv2</P>
 *
 * @param <R> Type of {@link StackedAreaRenderer} being reflected
 * @author Lyor G.
 * @since Jun 8, 2009 1:49:43 PM
 */
public class StackedAreaRendererReflectiveProxy<R extends StackedAreaRenderer> extends AreaRendererReflectiveProxy<R> {

    protected StackedAreaRendererReflectiveProxy(Class<R> objClass, boolean registerAsDefault) throws IllegalArgumentException, IllegalStateException {
        super(objClass, registerAsDefault);
    }

    public StackedAreaRendererReflectiveProxy(Class<R> objClass) throws IllegalArgumentException {
        this(objClass, false);
    }

    public static final StackedAreaRendererReflectiveProxy<StackedAreaRenderer> STACKED = new StackedAreaRendererReflectiveProxy<StackedAreaRenderer>(StackedAreaRenderer.class, true);
}

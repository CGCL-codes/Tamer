package info.monitorenter.gui.chart.annotations.bubble;

import javax.swing.JComponent;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.annotations.AAnnotationContentComponent;
import info.monitorenter.gui.chart.annotations.IAnnotationCreator;
import info.monitorenter.gui.chart.views.ChartPanel;

/**
 * Factory implementation for annotation view creation in tool tip bubble style.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 **/
public final class AnnotationCreatorBubble implements IAnnotationCreator {

    /** Singleton instance. */
    private static IAnnotationCreator instance;

    /** Generated <code>serialVersionUID</code>. **/
    private static final long serialVersionUID = -339733222044962043L;

    /**
   * Singleton retrieval method.
   * <p>
   * 
   * @return the sole instance in this VM.
   */
    public static IAnnotationCreator getInstance() {
        if (AnnotationCreatorBubble.instance == null) {
            AnnotationCreatorBubble.instance = new AnnotationCreatorBubble();
        }
        return AnnotationCreatorBubble.instance;
    }

    /**
   * Singleton constructor.
   * <p>
   */
    private AnnotationCreatorBubble() {
    }

    /**
   * @see info.monitorenter.gui.chart.annotations.IAnnotationCreator#createAnnotationView(info.monitorenter.gui.chart.views.ChartPanel,
   *      info.monitorenter.gui.chart.ITracePoint2D,
   *      info.monitorenter.gui.chart.annotations.AAnnotationContentComponent,
   *      boolean, boolean)
   */
    public JComponent createAnnotationView(final ChartPanel chart, final ITracePoint2D point, final AAnnotationContentComponent annotationPainter, final boolean useDragListenerOnAnnotationContent, final boolean useTitleBar) {
        AnnotationBubble annotationPanel = new AnnotationBubble(chart, annotationPainter, useDragListenerOnAnnotationContent, useTitleBar);
        return annotationPanel;
    }
}

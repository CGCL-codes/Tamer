package es.aeat.eett.rubik.tableRubik.jpivot;

import javax.swing.tree.DefaultMutableTreeNode;
import com.tonbeller.jpivot.table.span.Span;

/**
 * Created on 18.10.2002
 * 
 * @author av
 */
public abstract class SpanBuilderDecorator extends PartBuilderDecorator implements SpanBuilder {

    /**
   * Constructor for RowHeadingRendererDecorator.
   */
    public SpanBuilderDecorator(SpanBuilder delegate) {
        super(delegate);
    }

    public DefaultMutableTreeNode build(SBContext sbctx, Span span, boolean even) {
        return ((SpanBuilder) delegate).build(sbctx, span, even);
    }
}

package es.aeat.eett.rubik.tableRubik.jpivot;

import javax.swing.tree.DefaultMutableTreeNode;
import com.tonbeller.jpivot.olap.model.Member;

/**
 * Created on 18.10.2002
 *
 * @author av
 */
public abstract class SlicerBuilderDecorator extends PartBuilderDecorator implements SlicerBuilder {

    public SlicerBuilderDecorator(SlicerBuilder delegate) {
        super(delegate);
    }

    public DefaultMutableTreeNode build(Member m) {
        return ((SlicerBuilder) delegate).build(m);
    }
}

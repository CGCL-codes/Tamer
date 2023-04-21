package mctplugin.compute.basenodes;

import mctplugin.compute.baselettypes.GenericInletType;
import mctplugin.compute.ComputeNodeClass;
import mctplugin.compute.ComputeNodeFactory;
import mctplugin.compute.InOutLetType;

/**
 * A compute node class for printing generic object input.
 * @author m.weber
 */
public class GenericOutputNodeClass extends ComputeNodeClass<GenericOutputNode> {

    /** default constructor */
    public GenericOutputNodeClass() {
        this(ComputeNodeFactory.getInstance());
    }

    /** constructor with provided factory (this is somehow a HACK!)*/
    public GenericOutputNodeClass(ComputeNodeFactory fact) {
        super();
        inOutLetTypes = new InOutLetType[1];
        inOutLetTypes[0] = fact.getInOutLetType(GenericInletType.getTypeName());
    }

    @Override
    public final GenericOutputNode createNode() {
        return new GenericOutputNode(this);
    }

    @Override
    public final String getCategory() {
        return "Output";
    }

    @Override
    public final String getName() {
        return "GenericOutputNode";
    }

    @Override
    public final String getDescr() {
        return "Print generic object content.";
    }
}

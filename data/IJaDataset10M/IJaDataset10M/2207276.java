package mctplugin.compute.basenodes;

import mctplugin.compute.baselettypes.StringOutletType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mctplugin.compute.ComputeNode;
import mctplugin.compute.ComputeNodeFactory;
import mctplugin.compute.InOutLet;
import mctplugin.compute.InOutLetType;
import mctplugin.compute.TriggerType.Types;

/**
 * A generic object output node.
 * @author m.weber
 */
public class GenericInternalInputNode extends ComputeNode {

    /** inlet */
    protected InOutLet<Object> outlet;

    /** input */
    protected JTextField input;

    /** constructor */
    public GenericInternalInputNode(GenericInternalInputNodeClass compClass) {
        super(compClass, null);
        triggeredTypes.add(Types.START);
        triggeredTypes.add(Types.STEP);
        InOutLetType type = ComputeNodeFactory.getInstance().getInOutLetType(StringOutletType.getTypeName());
        outlet = new InOutLet<Object>("", type, this, InOutLet.Dir.OUT);
        addInOutLet(outlet);
        JPanel inPan = new JPanel();
        inPan.setLayout(new FlowLayout(FlowLayout.CENTER));
        inPan.add(new JLabel("Val: "));
        input = new JTextField("0");
        inPan.add(input);
        Dimension newSize = new Dimension(100, input.getMinimumSize().height);
        input.setSize(newSize);
        input.setMinimumSize(newSize);
        input.setPreferredSize(newSize);
        getWidget().addComp(inPan);
        register();
    }

    @Override
    public void compute(final int propagate) {
        outlet.setValue(input.getText(), propagate);
    }
}

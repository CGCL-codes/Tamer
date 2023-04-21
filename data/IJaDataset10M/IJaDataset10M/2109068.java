package cross.datastructures.workflow;

import java.util.ArrayList;
import java.util.List;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import cross.annotations.ProvidesVariables;
import cross.annotations.RequiresOptionalVariables;
import cross.annotations.RequiresVariables;
import cross.commands.fragments.AFragmentCommand;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tuple.TupleND;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiresVariables(names = { "variable1", "variable2" })
@RequiresOptionalVariables(names = { "variable3" })
@ProvidesVariables(names = { "variable2", "variable5" })
public class FragmentCommandMockB extends AFragmentCommand {

    /**
     * 
     */
    private static final long serialVersionUID = 7454449407054696377L;

    @Override
    public TupleND<IFileFragment> apply(TupleND<IFileFragment> in) {
        log.info("Running {}", getClass().getName());
        TupleND<IFileFragment> out = createWorkFragments(in);
        for (IFileFragment frag : out) {
            IVariableFragment ivf1 = frag.getChild("variable1");
            VariableFragment v4 = new VariableFragment(frag, "variable4");
            v4.setArray(ivf1.getArray());
            IVariableFragment ivf2 = frag.getChild("variable2");
            ivf2.getArray();
            VariableFragment v5 = new VariableFragment(frag, "variable5");
            List<Array> indexedList = new ArrayList<Array>();
            for (int i = 0; i < 10; i++) {
                indexedList.add(new ArrayDouble.D1(20));
            }
            v5.setIndexedArray(indexedList);
            frag.save();
        }
        return out;
    }

    @Override
    public WorkflowSlot getWorkflowSlot() {
        return WorkflowSlot.FILEIO;
    }

    @Override
    public String getDescription() {
        return "This is a mock b command";
    }
}

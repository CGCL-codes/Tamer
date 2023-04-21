package au.edu.swin.videoshop.ui.components;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import au.edu.swin.videoshop.bean.AddressState;

/**
 * @author julie
 *
 */
public class AddressStateComponent {

    private List mStates;

    private List mStatesWithEmptyOption;

    public AddressStateComponent() {
        mStates = new ArrayList();
        mStates.add(new SelectItem(AddressState.ACT.getName(), AddressState.ACT.getName()));
        mStates.add(new SelectItem(AddressState.NSW.getName(), AddressState.NSW.getName()));
        mStates.add(new SelectItem(AddressState.NT.getName(), AddressState.NT.getName()));
        mStates.add(new SelectItem(AddressState.SA.getName(), AddressState.SA.getName()));
        mStates.add(new SelectItem(AddressState.TAS.getName(), AddressState.TAS.getName()));
        mStates.add(new SelectItem(AddressState.VIC.getName(), AddressState.VIC.getName()));
        mStates.add(new SelectItem(AddressState.WA.getName(), AddressState.WA.getName()));
        mStatesWithEmptyOption = new ArrayList();
        mStatesWithEmptyOption.add(new SelectItem(null, "Select a state"));
        mStatesWithEmptyOption.addAll(mStates);
    }

    /**
	 * @return the states
	 */
    public List getStates() {
        return mStates;
    }

    /**
	 * @return the statesWithEmptyOption
	 */
    public List getStatesWithEmptyOption() {
        return mStatesWithEmptyOption;
    }
}

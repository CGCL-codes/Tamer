package org.systemsbiology.apps.corragui.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.systemsbiology.apps.corragui.client.data.project.ISetupOption;
import org.systemsbiology.apps.corragui.client.data.project.SetupOptionList;

public abstract class CorraStepSetup implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6103099406792102001L;

    protected String progName;

    protected List<ISetupOption> options;

    public CorraStepSetup() {
        this.progName = null;
        options = new ArrayList<ISetupOption>();
    }

    public CorraStepSetup(String progName, List<ISetupOption> options) {
        this.progName = progName;
        if (options == null) this.options = new ArrayList<ISetupOption>(0); else this.options = options;
    }

    public String getProgramName() {
        return progName;
    }

    public List<ISetupOption> getOptions() {
        return this.options;
    }

    public ISetupOption getOptionByName(String name) {
        for (ISetupOption opt : options) {
            if (opt.getName().equals(name)) return opt;
        }
        return null;
    }

    /**
     * Returns a list of condition pairs that have been selected for the step
     * @return
     */
    public List<ISetupOption> getSelectedConditionPairs() {
        SetupOptionList opt = getConditionsOption();
        if (opt == null) return new ArrayList<ISetupOption>();
        List<ISetupOption> pairs = new ArrayList<ISetupOption>();
        Iterator<ISetupOption> it = opt.iterator();
        while (it.hasNext()) {
            ISetupOption subOpt = (ISetupOption) it.next();
            if (Boolean.valueOf(subOpt.getValue()).booleanValue()) pairs.add(subOpt);
        }
        return pairs;
    }

    private SetupOptionList getConditionsOption() {
        Iterator<ISetupOption> it = getOptions().iterator();
        while (it.hasNext()) {
            ISetupOption opt = it.next();
            if (opt instanceof SetupOptionList && "Compare Conditions".equals(opt.getName())) return (SetupOptionList) opt;
        }
        return null;
    }

    protected final List<ISetupOption> cloneOptions() {
        List<ISetupOption> options = getOptions();
        List<ISetupOption> optionsCopy = new ArrayList<ISetupOption>(options.size());
        Iterator<ISetupOption> it = options.iterator();
        while (it.hasNext()) {
            ISetupOption option = it.next();
            optionsCopy.add(option.copy());
        }
        return optionsCopy;
    }

    public void addOption(ISetupOption option) {
        if (!options.contains(option)) options.add(option);
    }

    public abstract String getOutputFileName();
}

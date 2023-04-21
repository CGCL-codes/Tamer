package meteoric.metaDepth.web.client.comm.commands;

import meteoric.metaDepth.web.client.comm.MetaDepthServiceAsync;

public abstract class CreateClabject extends AsyncCommand {

    protected String name;

    protected int potency;

    protected int min;

    protected int max;

    public CreateClabject(MetaDepthServiceAsync mds, String name, int potency, int min, int max) {
        super(mds);
        this.name = name;
        this.potency = potency;
        this.min = min;
        this.max = max;
    }
}

package es.optsicom.lib.tablecreator;

import es.optsicom.lib.experiment.Execution;
import es.optsicom.lib.experiment.MethodDescription;

public abstract class SyntheticExecCreator {

    public abstract Execution createSyntheticExec(Execution exec);

    public abstract MethodDescription getSyntheticMethodDesc(MethodDescription method);
}

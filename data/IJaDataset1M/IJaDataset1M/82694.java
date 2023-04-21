package exitdistributions;

import ds.ca.evac.Individual;
import ds.ca.evac.TargetCell;

public abstract class IndividualToExitMapping {

    public abstract TargetCell getExit(Individual individual);
}

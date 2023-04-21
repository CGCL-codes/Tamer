package edu.fudan.software.CWFE.workflow.instance.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import edu.fudan.software.CWFE.workflow.IFlowManager;
import edu.fudan.software.CWFE.workflow.instance.IFlow;
import edu.fudan.software.CWFE.workflow.instance.IFork;

@Entity
@DiscriminatorValue("Fork")
public class Fork extends BaseActivityNode implements IFork {

    @Override
    public void execute(IFlowManager flowManager, IFlow flow) {
    }

    @Override
    public String getDescription(IFlowManager flowManager) {
        return "";
    }
}

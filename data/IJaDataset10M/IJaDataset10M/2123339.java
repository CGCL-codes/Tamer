package com.agentfactory.teleoreactive.debugger;

import java.util.List;
import com.agentfactory.visualiser.impl.DefaultSnapshot;

public class TeleoReactiveSnapshot extends DefaultSnapshot {

    public List<String> Functions;

    public List<String> currentBeliefs;

    public List<String> actionTrace;

    public List<String> programStack;

    public List<String> Actions;

    public List<String> Sensors;
}

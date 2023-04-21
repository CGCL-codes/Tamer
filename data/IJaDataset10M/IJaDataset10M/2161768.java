package com.griddynamics.openspaces.convergence.gigapult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestLab {

    private static final String DEFAULT_CONFIG = "default";

    private List<Machine> machines = new ArrayList<Machine>();

    private GigapultLauncher launcher = new GigapultLauncher("gigapult");

    public TestLab(String commonSettings) {
        launcher.setCommonSettings("testlab/" + commonSettings + ".rb");
    }

    public TestLab() {
        this(DEFAULT_CONFIG);
    }

    public void addMachine(String config) {
        Machine machine = new Machine(config);
        machine.setLauncher(launcher);
        machines.add(machine);
    }

    public List<Machine> getMachines() {
        return Collections.unmodifiableList(machines);
    }

    public static TestLab createConvergenceLab() {
        return createConvergenceLab(DEFAULT_CONFIG);
    }

    public static TestLab createConvergenceLab(String commonSettings) {
        TestLab lab = new TestLab(commonSettings);
        lab.addMachine("testlab/" + "machine-1.rb");
        lab.addMachine("testlab/" + "machine-2.rb");
        return lab;
    }

    public Machine getLocalMachine() {
        Machine machine = new Machine("");
        machine.setLauncher(launcher);
        return machine;
    }
}

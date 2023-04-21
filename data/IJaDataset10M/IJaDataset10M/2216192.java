package gui.configuration;

import gui.Gui;
import main.configuration.ProcessConfiguration;
import main.exception.TooManyProcessesException;
import main.exception.TooManyProcessesException;
import main.process.LogicReference;

public class ProcessConfigurationView {

    public Gui gui_;

    public ProcessConfigurationView(Gui gui) {
        gui_ = gui;
        gui_.processConfiguration = new ProcessConfiguration(gui_.memoryConfiguration);
    }

    public void defaultPagedProcessConfigurationSettings() {
        gui_.processConfiguration.setContextSwitchTime(2);
        gui_.processConfiguration.setQuantum(5);
        gui_.processConfiguration.useRRPolicy();
        int id;
        try {
            id = gui_.processConfiguration.addProcess(10, 10, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 8, LogicReference.Mode.WRITE, 10);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.WRITE, 60);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 70);
            gui_.processConfiguration.addLogicReference(id, 11, LogicReference.Mode.WRITE, 110);
            gui_.processConfiguration.addLogicReference(id, 14, LogicReference.Mode.WRITE, 120);
            gui_.processConfiguration.addLogicReference(id, 9, LogicReference.Mode.WRITE, 160);
            gui_.processConfiguration.addLogicReference(id, 12, LogicReference.Mode.WRITE, 161);
            gui_.processConfiguration.addLogicReference(id, 10, LogicReference.Mode.WRITE, 170);
            gui_.processConfiguration.addLogicReference(id, 13, LogicReference.Mode.WRITE, 171);
            System.out.println("RIMOSSO LOGIC?" + gui_.processConfiguration.removeLogicReference(id, 2, LogicReference.Mode.READ, 40));
            id = gui_.processConfiguration.addProcess(5, 2, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 60);
            System.out.println("PROCESSO RIMOSSO?: " + gui_.processConfiguration.removeProcess(id));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TooManyProcessesException e) {
            e.printStackTrace();
        }
        gui_.totalReferences = gui_.processConfiguration.createProcessConfiguration();
    }

    public void defaultPagedProcessConfigurationSettings2() {
        gui_.processConfiguration.setContextSwitchTime(2);
        gui_.processConfiguration.setQuantum(10);
        gui_.processConfiguration.useRRPolicy();
        int id;
        try {
            id = gui_.processConfiguration.addProcess(10, 10, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 8, LogicReference.Mode.WRITE, 10);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.WRITE, 60);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 70);
            gui_.processConfiguration.addLogicReference(id, 11, LogicReference.Mode.WRITE, 110);
            gui_.processConfiguration.addLogicReference(id, 14, LogicReference.Mode.WRITE, 120);
            gui_.processConfiguration.addLogicReference(id, 9, LogicReference.Mode.WRITE, 160);
            gui_.processConfiguration.addLogicReference(id, 12, LogicReference.Mode.WRITE, 161);
            gui_.processConfiguration.addLogicReference(id, 10, LogicReference.Mode.WRITE, 170);
            gui_.processConfiguration.addLogicReference(id, 13, LogicReference.Mode.WRITE, 171);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.WRITE, 360);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 370);
            gui_.processConfiguration.addLogicReference(id, 11, LogicReference.Mode.WRITE, 310);
            gui_.processConfiguration.addLogicReference(id, 14, LogicReference.Mode.WRITE, 320);
            gui_.processConfiguration.addLogicReference(id, 9, LogicReference.Mode.WRITE, 360);
            gui_.processConfiguration.addLogicReference(id, 12, LogicReference.Mode.WRITE, 361);
            gui_.processConfiguration.addLogicReference(id, 10, LogicReference.Mode.WRITE, 370);
            gui_.processConfiguration.addLogicReference(id, 13, LogicReference.Mode.WRITE, 371);
            id = gui_.processConfiguration.addProcess(5, 2, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 60);
            id = gui_.processConfiguration.addProcess(8, 4, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 60);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TooManyProcessesException e) {
            e.printStackTrace();
        }
        gui_.totalReferences = gui_.processConfiguration.createProcessConfiguration();
    }

    public void defaultPagedProcessConfigurationSettings3() {
        gui_.processConfiguration.setContextSwitchTime(2);
        gui_.processConfiguration.setQuantum(10);
        gui_.processConfiguration.useRRPolicy();
        int id;
        try {
            id = gui_.processConfiguration.addProcess(10, 10, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 8, LogicReference.Mode.WRITE, 10);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.WRITE, 60);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 70);
            gui_.processConfiguration.addLogicReference(id, 11, LogicReference.Mode.WRITE, 110);
            gui_.processConfiguration.addLogicReference(id, 14, LogicReference.Mode.WRITE, 120);
            gui_.processConfiguration.addLogicReference(id, 9, LogicReference.Mode.WRITE, 160);
            gui_.processConfiguration.addLogicReference(id, 12, LogicReference.Mode.WRITE, 161);
            gui_.processConfiguration.addLogicReference(id, 10, LogicReference.Mode.WRITE, 170);
            gui_.processConfiguration.addLogicReference(id, 13, LogicReference.Mode.WRITE, 171);
            id = gui_.processConfiguration.addProcess(5, 2, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 60);
            id = gui_.processConfiguration.addProcess(8, 4, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 60);
            id = gui_.processConfiguration.addProcess(8, 4, 0, 0, 1000, null, null, null);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.WRITE, 40);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.WRITE, 50);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.WRITE, 60);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TooManyProcessesException e) {
            e.printStackTrace();
        }
        gui_.totalReferences = gui_.processConfiguration.createProcessConfiguration();
    }

    public void defaultSegmentedProcessConfigurationSettings() {
        gui_.processConfiguration.setContextSwitchTime(2);
        gui_.processConfiguration.setQuantum(10);
        gui_.processConfiguration.useRRPolicy();
        int id;
        try {
            id = gui_.processConfiguration.addProcess(3, 3, 2, 0, 1000, new Integer[] { 4, 14, 8 }, new Integer[] { 5, 7, 16 }, new Integer[] { 4, 5 });
            gui_.processConfiguration.addLogicReference(id, 0, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.READ, 60);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.READ, 70);
            id = gui_.processConfiguration.addProcess(4, 2, 3, 0, 1000, new Integer[] { 4, 14, 8, 5 }, new Integer[] { 5, 7 }, new Integer[] { 4, 5, 6 });
            gui_.processConfiguration.addLogicReference(id, 0, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 50);
            gui_.processConfiguration.addLogicReference(id, 6, LogicReference.Mode.READ, 60);
            gui_.processConfiguration.addLogicReference(id, 7, LogicReference.Mode.READ, 70);
            id = gui_.processConfiguration.addProcess(1, 2, 3, 0, 1000, new Integer[] { 4 }, new Integer[] { 5, 7 }, new Integer[] { 4, 5, 6 });
            gui_.processConfiguration.addLogicReference(id, 0, LogicReference.Mode.READ, 0);
            gui_.processConfiguration.addLogicReference(id, 1, LogicReference.Mode.READ, 10);
            gui_.processConfiguration.addLogicReference(id, 2, LogicReference.Mode.READ, 20);
            gui_.processConfiguration.addLogicReference(id, 3, LogicReference.Mode.READ, 30);
            gui_.processConfiguration.addLogicReference(id, 4, LogicReference.Mode.READ, 40);
            gui_.processConfiguration.addLogicReference(id, 5, LogicReference.Mode.READ, 50);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TooManyProcessesException e) {
            e.printStackTrace();
        }
        gui_.processConfiguration.createProcessConfiguration();
    }
}

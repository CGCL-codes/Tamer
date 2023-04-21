package org.jalgo.module.am0c0.core;

import javax.swing.JOptionPane;
import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.gui.SimulationView;
import org.jalgo.module.am0c0.model.AM0History;
import org.jalgo.module.am0c0.model.am0.*;

/**
 * Controller to handle the AM0 simulation.
 * 
 * @author Max Leuth&auml;user
 */
public class Simulator extends Subcontroller {

    private AM0History<MachineConfiguration> history;

    private int currentStep;

    private AM0Program am0Program;

    private MachineConfiguration currentMc;

    /**
	 * Flag to allow or disallow scrolling, printing descriptions and
	 * highlighting. This flag is set to false during a simulation in one single
	 * step to avoid performance issues.
	 */
    private boolean drawAddon = true;

    /**
	 * Flag which is used to handle jump command failures.
	 */
    private boolean jumpFails = false;

    public Simulator(Controller controller) {
        this.controller = controller;
        view = new SimulationView(this);
        history = new AM0History<MachineConfiguration>();
        currentStep = 0;
    }

    @Override
    public SimulationView getView() {
        return (SimulationView) (super.getView());
    }

    /**
	 * @return the maximal available program counter;
	 */
    public int getMaxPC() {
        return am0Program.size();
    }

    /**
	 * @return the history of the simulation.
	 */
    public AM0History<MachineConfiguration> getHistory() {
        return history;
    }

    /**
	 * @param m
	 *            {@link MachineConfiguration} which is used here.
	 */
    public void setCurrentMachineConfiguration(MachineConfiguration m) {
        currentMc = m;
    }

    /**
	 * Show an error message to the user because an command could not be
	 * executed.
	 * 
	 * @param reason
	 */
    private void showError(String reason) {
        JOptionPane.showMessageDialog(null, Messages.getString("am0c0", "Simulator.0") + Messages.getString("am0c0", "Simulator.1") + am0Program.get(currentMc.getProgramCounter().get() - 1).getCodeText() + "\n\n" + Messages.getString("am0c0", "Simulator.3") + reason);
    }

    /**
	 * Show an info message to the user because an jump command could not be
	 * executed.
	 * 
	 * @param reason
	 */
    @SuppressWarnings("unused")
    private void showInformation(String reason) {
        JOptionPane.showMessageDialog(null, Messages.getString("am0c0", "Simulator.4") + am0Program.get(currentMc.getProgramCounter().get() - 1).getCodeText() + "\n\n" + Messages.getString("am0c0", "Simulator.6") + reason);
    }

    /**
	 * @param program
	 *            The {@link AM0Program} which should be used during the
	 *            simulation.
	 * 
	 * @throws IllegalArgumentException
	 */
    public void setAM0Program(AM0Program program) throws IllegalArgumentException {
        if (program != null) {
            am0Program = program;
            ((SimulationView) view).setAM0Program(program);
            getView().resetSimulation();
        } else throw new IllegalArgumentException("Null argument is not allowed for AM0Program.");
    }

    /**
	 * Show the description for the corresponding statement.
	 * 
	 * @param i
	 *            Position of the statement in the {@link AM0Program}.
	 */
    private void showDescription(int i) {
        if (drawAddon) {
            if (am0Program.size() < 1) {
                return;
            }
            if (i <= 0) {
                i = 1;
            }
            if (i > am0Program.size()) {
                i = am0Program.size();
            }
            ((SimulationView) view).showDescription(am0Program.get(i - 1).getCodeText() + "<br /><p>" + am0Program.get(i - 1).getDescription() + "</p>");
        }
    }

    /**
	 * Show the currently visible page range.
	 */
    private void showRange() {
        ((SimulationView) view).getRangeLabel().setText(Messages.getString("am0c0", "Simulator.12") + (((SimulationView) view).getTableModel().getPageOffset() == 0 ? 1 : (((SimulationView) view).getTableModel().getPageSize() * ((SimulationView) view).getTableModel().getPageOffset())) + Messages.getString("am0c0", "Simulator.13") + (((SimulationView) view).getTableModel().getPageSize() * (((SimulationView) view).getTableModel().getPageOffset() + 1)) + Messages.getString("am0c0", "Simulator.14"));
    }

    /**
	 * @param m
	 *            The {@link MachineConfiguration} to draw in the
	 *            {@link SimulationView}.
	 */
    public void addResultToTable(MachineConfiguration m, int position) {
        ((SimulationView) view).getTableModel().addRow(m);
        if (drawAddon) {
            if (position >= 1) {
                ((SimulationView) view).getStepLabel().setText(Messages.getString("am0c0", "Simulator.10") + (position + 1) + "  ");
                showRange();
                ((SimulationView) view).scroll(currentMc.getProgramCounter().get());
            }
            if (currentMc.getProgramCounter().get() <= getMaxPC() && currentMc.getProgramCounter().get() >= 1) {
                showDescription(currentMc.getProgramCounter().get());
            }
        }
    }

    /**
	 * Removes the last line in the table.
	 */
    public void removeLastTableEntry(int position) {
        ((SimulationView) view).getTableModel().removeLastRow();
        ((SimulationView) view).getStepLabel().setText(Messages.getString("am0c0", "Simulator.15") + (position + 1) + "  ");
        if (drawAddon) {
            ((SimulationView) view).scroll(currentMc.getProgramCounter().get());
            showDescription(currentMc.getProgramCounter().get());
        }
    }

    /**
	 * Do the next step and draw the result in the {@link SimulationView} using
	 * {@link SimulationView#getTableModel()}. We only calculate something new
	 * if the step is not already in the {@link AM0History}.
	 * 
	 * @return true if the next step could executed correctly, false otherwise.
	 */
    public boolean nextStep() {
        if (currentMc.getProgramCounter().get() > getMaxPC() || currentMc.getProgramCounter().get() < 1) {
            jumpFails = true;
            if (!jumpFails) {
                addResultToTable(currentMc, currentStep + 1);
                jumpFails = true;
            }
            return false;
        }
        currentStep++;
        if (!history.stepExists(currentStep)) {
            jumpFails = false;
            try {
                currentMc = am0Program.get(currentMc.getProgramCounter().get() - 1).apply(new MachineConfiguration(currentMc));
                history.add(currentMc);
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
                return false;
            } catch (ArithmeticException e) {
                showError(e.getMessage());
                return false;
            }
        } else {
            currentMc = history.getAtStep(currentStep);
        }
        addResultToTable(currentMc, currentStep);
        return true;
    }

    /**
	 * Do the previous step and draw the result in the {@link SimulationView}
	 * using {@link SimulationView#getTableModel()}. Nothing is calculated here,
	 * we just using the {@link AM0History}.
	 * 
	 * @return true if the previous step could executed correctly, false
	 *         otherwise.
	 */
    public boolean previousStep() {
        if (currentStep >= 1) {
            currentStep--;
            currentMc = history.getAtStep(currentStep);
            removeLastTableEntry(currentStep);
            jumpFails = false;
            return true;
        }
        return false;
    }

    /**
	 * Do the full (remaining) simulation in one single step. Basically we just
	 * use {@link Simulator#nextStep()}.
	 * 
	 * @return true if all steps could executed correctly, false otherwise.
	 */
    public boolean stepToEnd() {
        drawAddon = false;
        boolean result = true;
        while (result) {
            result = nextStep();
            if (currentStep % ((SimulationView) view).getStepsToStopAfter() == 0) {
                drawAddon = true;
                showDescription(currentMc.getProgramCounter().get());
                ((SimulationView) view).getStepLabel().setText(Messages.getString("am0c0", "Simulator.17") + currentStep + "  ");
                ((SimulationView) view).scroll(currentMc.getProgramCounter().get());
                drawAddon = false;
                int s = JOptionPane.showConfirmDialog(null, currentStep + Messages.getString("am0c0", "Simulator.19") + Messages.getString("am0c0", "Simulator.20") + Messages.getString("am0c0", "Simulator.21"), Messages.getString("am0c0", "Simulator.22"), JOptionPane.YES_NO_OPTION);
                if (s == JOptionPane.NO_OPTION || s == JOptionPane.CANCEL_OPTION) {
                    result = true;
                    break;
                }
            }
        }
        drawAddon = true;
        ((SimulationView) view).getStepLabel().setText(Messages.getString("am0c0", "Simulator.23") + (currentStep + 1) + "  ");
        ((SimulationView) view).scroll(currentMc.getProgramCounter().get());
        showDescription(currentMc.getProgramCounter().get() - (jumpFails ? 0 : 1));
        showRange();
        return result;
    }

    /**
	 * Clear the whole simulation.
	 */
    public void clear() {
        ((SimulationView) view).getTableModel().clear();
        history.clear();
        ((SimulationView) view).getStepLabel().setText(Messages.getString("am0c0", "Simulator.25"));
        ((SimulationView) view).showDescription("");
        currentStep = 0;
    }

    /**
	 * @return true if the simulation needs an initial configuration.
	 */
    public boolean simulatorNeedsInitialConfig() {
        return history.getCount() == 0;
    }

    public AM0Program getAM0Program() {
        return am0Program;
    }
}

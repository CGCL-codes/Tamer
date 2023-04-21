package org.jsens.project.internal.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jsens.businesslogic.EffectStructure;
import org.jsens.businesslogic.InfluenceMatrix;
import org.jsens.businesslogic.Simulation;
import org.jsens.project.DataHolder;
import org.jsens.project.internal.Simulation.SimulationOpenAction;
import org.jsens.project.internal.analyseinfluence.AnalyzeInfluenceOpenAction;
import org.jsens.project.internal.criteria.CriteriasOpenAction;
import org.jsens.project.internal.effects.EffectStructureOpenAction;
import org.jsens.project.internal.influence.InfluenceMatrixOpenAction;
import org.jsens.project.internal.variable.VariablesOpenAction;

public class ProjectViewDoubleClickListener implements IDoubleClickListener {

    VariablesOpenAction _varAction = new VariablesOpenAction();

    CriteriasOpenAction _critAction = new CriteriasOpenAction();

    InfluenceMatrixOpenAction _infAction = new InfluenceMatrixOpenAction();

    AnalyzeInfluenceOpenAction _anainfAction = new AnalyzeInfluenceOpenAction();

    EffectStructureOpenAction _effectAction = new EffectStructureOpenAction();

    SimulationOpenAction _simulationAction = new SimulationOpenAction();

    public void doubleClick(DoubleClickEvent event) {
        ISelection selection = event.getSelection();
        TreeObject treeObject = TreeObject.class.cast(IStructuredSelection.class.cast(selection).getFirstElement());
        if (ViewContentProvider.VARIABLES.equals(treeObject.getID())) {
            _varAction.run();
        } else if (ViewContentProvider.CRITERIAS.equals(treeObject.getID())) {
            _critAction.run();
        } else if (ViewContentProvider.INFLUENCE.equals(treeObject.getID())) {
            InfluenceMatrix matrix = DataHolder.getCurrentSensitivityModel().getInfluenceMatrix(treeObject.getName());
            _infAction.setInfluence(matrix);
            _infAction.run();
        } else if (ViewContentProvider.ANALYZEINFLUENCE.equals(treeObject.getID())) {
            _anainfAction.run();
        } else if (ViewContentProvider.EFFECTSTRUCTURE.equals(treeObject.getID())) {
            EffectStructure structure = DataHolder.getCurrentSensitivityModel().getEffectStructure(treeObject.getName());
            _effectAction.setEffectStructure(structure);
            _effectAction.run();
        } else if (ViewContentProvider.SIMULATION.equals(treeObject.getID())) {
            Simulation simulation = DataHolder.getCurrentSensitivityModel().getSimulation(treeObject.getName());
            _simulationAction.setSimulation(simulation);
            _simulationAction.run();
        }
    }
}

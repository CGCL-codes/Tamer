package eu.planets_project.pp.plato.action.interfaces;

import javax.ejb.Local;

@Local
public interface IDefineBasis extends IWorkflowStep {

    public String uploadPolicyMindMap();

    public void removePolicyTree();
}

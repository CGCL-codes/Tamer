package eu.planets_project.pp.plato.action.interfaces;

import javax.ejb.Local;

@Local
public interface IAnalyseResults extends IWorkflowStep {

    public String switchDisplayChangelogs();

    public void download(Object object);

    public String analyseSensitivity();

    public void init();
}

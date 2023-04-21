package sk.fiit.mitandao.core;

import java.util.Map;
import sk.fiit.mitandao.core.exceptions.AnalysisException;
import sk.fiit.mitandao.core.parameterreader.AnnotationProvider;
import sk.fiit.mitandao.modules.interfaces.Module;
import sk.fiit.mitandao.modules.manager.ModulesManager;
import sk.fiit.mitandao.modules.manager.ModulesManagerImpl;
import edu.uci.ics.jung.graph.Graph;

/**
 * The implementation of the <code>Mitandao</code> interface.
 * 
 * @see Mitandao
 * @author Lucia Jastrzembska
 */
public class MitandaoImpl implements Mitandao {

    private MitandaoManager mitandaoManager = new MitandaoManager();

    private AnnotationProvider annotationProvider = new AnnotationProvider();

    /**
	 * Default constructor.
	 */
    public MitandaoImpl() {
    }

    @Override
    public void addModule(Module module) {
        mitandaoManager.getWorkflow().add(module);
    }

    @Override
    public void addModule(Module module, int position) {
        mitandaoManager.getWorkflow().add(module, position);
    }

    @Override
    public void addModule(Module module, Map<String, Object> params) throws Exception {
        setModuleParameters(module, params);
        mitandaoManager.getWorkflow().add(module);
    }

    @Override
    public void addModule(Module module, Map<String, Object> params, int position) throws Exception {
        setModuleParameters(module, params);
        mitandaoManager.getWorkflow().add(module, position);
    }

    @Override
    public Graph analyze(Graph graph) throws AnalysisException {
        setGraph(graph);
        return mitandaoManager.analyze();
    }

    @Override
    public Module getModule(int position) {
        return mitandaoManager.getWorkflow().get(position);
    }

    @Override
    public ModulesManager getModulesManager() {
        return new ModulesManagerImpl();
    }

    @Override
    public Map<String, Object> getModuleParameters(Module module) throws Exception {
        return annotationProvider.getParameters(module);
    }

    @Override
    public void removeModule(Module module) {
        mitandaoManager.getWorkflow().remove(module);
    }

    @Override
    public void removeAllModules() {
        mitandaoManager.getWorkflow().clear();
    }

    @Override
    public void removeModule(int position) {
        mitandaoManager.getWorkflow().remove(position);
    }

    @Override
    public void setGraph(Graph graph) {
        mitandaoManager.setGraph(graph);
    }

    @Override
    public void setModuleParameters(Module module, Map<String, Object> parameters) throws Exception {
        annotationProvider.setParameters(module, parameters);
    }

    @Override
    public Graph getGraph() {
        return mitandaoManager.getGraph();
    }

    @Override
    public int getModulesCount() {
        return mitandaoManager.getWorkflow().size();
    }
}

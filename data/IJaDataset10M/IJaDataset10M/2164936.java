package org.azrul.liquidframe.domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Azrul Hasni MADISA
 */
public class Kernel {

    private List<Module> modules = new ArrayList<Module>();

    private AtomicLong elementIndex = new AtomicLong();

    private transient ClassLoader classLoader;

    private AtomicLong tokenIndex = new AtomicLong();

    /** Creates a new instance of Kernel */
    public Kernel() {
    }

    public void propagate(Module module) {
    }

    public void linkAction2Activity(Long activityId, Action action) {
        for (Module m : modules) {
            Activity activity = m.findActivityById(activityId);
            if (activity != null) {
                if (activity instanceof SimpleActivity) {
                    ((SimpleActivity) activity).setAction(action);
                }
            }
        }
    }

    public void linkCondition2Edge(Long edgeId, Condition condition) {
        for (Module m : modules) {
            Edge edge = m.findEdgeById(edgeId);
            if (edge != null) {
                if (edge instanceof SimpleEdge) {
                    ((SimpleEdge) edge).setCondition(condition);
                }
            }
        }
    }

    public void addModule(Module module) {
        this.getModules().add(module);
    }

    public void removeInputEdge(Long moduleId, String inputName) {
        Module module = findModuleById(moduleId);
        module.removeInputEdge(inputName);
    }

    public void removeOutputEdge(Long moduleId, String outputName) {
        Module module = findModuleById(moduleId);
        module.removeOutputEdge(outputName);
    }

    public List<Long> getWorkflowElementIds(Long moduleId) {
        Module module = findModuleById(moduleId);
        return module.getActivityIds();
    }

    public String getActivityType(Long activityId) {
        Activity activity = findActivityById(activityId);
        if (activity instanceof SimpleActivity) {
            return "SimpleActivity";
        } else if (activity instanceof Decision) {
            return "Decision";
        } else if (activity instanceof Merge) {
            return "Merge";
        } else if (activity instanceof Fork) {
            return "Fork";
        } else if (activity instanceof Join) {
            return "Join";
        } else if (activity instanceof ProxyModule) {
            return "ProxyModule";
        } else if (activity instanceof Module) {
            return "Module";
        } else {
            return "None";
        }
    }

    public Long getInputEdgeId(String inputName, Long moduleId) {
        Module module = findModuleById(moduleId);
        Edge edge = module.getInputEdge(inputName);
        return edge.getId();
    }

    public Long getOutputEdgeId(String outputName, Long moduleId) {
        Module module = findModuleById(moduleId);
        Edge edge = module.getOutputEdge(outputName);
        return edge.getId();
    }

    public Long getEdgeToActivity(Long edgeId) {
        Edge edge = findEdgeById(edgeId);
        if (edge.getToActivity() != null) {
            return edge.getToActivity().getId();
        }
        return null;
    }

    public Long getEdgeFromActivity(Long edgeId) {
        Edge edge = findEdgeById(edgeId);
        if (edge.getFromActivity() != null) {
            return edge.getFromActivity().getId();
        }
        return null;
    }

    public Long getSimpleActivityToEdge(Long activityId) {
        SimpleActivity activity = (SimpleActivity) findActivityById(activityId);
        if (activity.getToEdge() != null) {
            return activity.getToEdge().getId();
        }
        return null;
    }

    public Long getSimpleActivityFromEdge(Long activityId) {
        SimpleActivity activity = (SimpleActivity) findActivityById(activityId);
        if (activity.getFromEdge() != null) {
            return activity.getFromEdge().getId();
        }
        return null;
    }

    public List<Long> getManyToOneFromEdges(Long activityId) {
        ManyToOne activity = (ManyToOne) findActivityById(activityId);
        return activity.getFromEdgeIds();
    }

    public Long getManyToOneToEdge(Long activityId) {
        ManyToOne activity = (ManyToOne) findActivityById(activityId);
        if (activity.getToEdge() == null) {
            return activity.getToEdge().getId();
        }
        return null;
    }

    public List<Long> getOneToManyToEdges(Long activityId) {
        OneToMany activity = (OneToMany) findActivityById(activityId);
        return activity.getToEdgeIds();
    }

    public String getEdgeType(Long edgeId) {
        Edge edge = findEdgeById(edgeId);
        if (edge instanceof SimpleEdge) {
            return "SimpleEdge";
        } else if (edge instanceof AlwaysTrueEdge) {
            return "AlwaysTrueEdge";
        } else if (edge instanceof OutputProxyEdge) {
            return "OutputProxyEdge";
        } else if (edge instanceof InputProxyEdge) {
            return "InputProxyEdge";
        } else {
            return null;
        }
    }

    public Long getOneToManyFromEdge(Long activityId) {
        OneToMany activity = (OneToMany) findActivityById(activityId);
        if (activity.getFromEdge() != null) {
            return activity.getFromEdge().getId();
        }
        return null;
    }

    public Edge findEdgeById(Long edgeId) {
        for (Module module : modules) {
            Edge edge = module.findEdgeById(edgeId);
            if (edge != null) {
                return edge;
            }
        }
        return null;
    }

    public String getActivityName(Long activityId) {
        Activity activity = findActivityById(activityId);
        return activity.getName();
    }

    public String getActivityActionClassFQName(Long activityId) {
        SimpleActivity activity = (SimpleActivity) findActivityById(activityId);
        if (activity == null) {
            return null;
        }
        return activity.getActionClassFQName();
    }

    public Point getInputEdgeCoor(String inputName, Long moduleId) {
        Module module = findModuleById(moduleId);
        return module.getInputEdge(inputName).getCoor();
    }

    public Point getOutputEdgeCoor(String outputName, Long moduleId) {
        Module module = findModuleById(moduleId);
        return module.getOutputEdge(outputName).getCoor();
    }

    public List<String> getInputEdgeNames(Long moduleId) {
        ProxyModule activity = (ProxyModule) findActivityById(moduleId);
        if (activity == null) {
            return null;
        }
        return activity.getInputEdgeNames();
    }

    public List<String> getOutputEdgeNames(Long moduleId) {
        ProxyModule activity = (ProxyModule) findActivityById(moduleId);
        if (activity == null) {
            return null;
        }
        return activity.getOutputEdgeNames();
    }

    public Point getActivityCoor(Long activityId) {
        Activity activity = findActivityById(activityId);
        return activity.getCoor();
    }

    public Long getModulePointedTo(Long proxyModuleId) {
        ProxyModule activity = (ProxyModule) findActivityById(proxyModuleId);
        if (activity == null) {
            return null;
        }
        Module pointedToModule = activity.getPointedToModule();
        if (pointedToModule != null) {
            return pointedToModule.getId();
        }
        return null;
    }

    public boolean activityHasToken(Long activityId) {
        SimpleActivity activity = (SimpleActivity) findActivityById(activityId);
        if (activity == null) {
            return false;
        } else {
            return !activity.getTokens().isEmpty();
        }
    }

    public Module findModuleById(Long id) {
        for (Module module : modules) {
            if (module.getId().equals(id)) {
                return module;
            }
        }
        return null;
    }

    public ProxyModule getProxyModulePointingToModule(Module module) {
        if (module != null) {
            for (Module m : modules) {
                for (Activity activity : m.getActivities()) {
                    if (activity instanceof ProxyModule) {
                        ProxyModule proxyModule = (ProxyModule) activity;
                        if (proxyModule.getPointedToModule() != null) {
                            if (proxyModule.getPointedToModule().getId().equals(module.getId())) {
                                return proxyModule;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void removeModule(Long moduleId) throws ElementCannotBeDeletedException {
        Module module = findModuleById(moduleId);
        ProxyModule p = getProxyModulePointingToModule(module);
        if (p != null) {
            throw new ElementCannotBeDeletedException(p.getId());
        }
        Module parent = (Module) module.getCloneParent();
        if (parent != null) {
            parent.removeCloneChild(module);
        }
        for (Activity a : module.getCloneChildren()) {
            Module child = (Module) a;
            child.removeCloneParent();
        }
        getModules().remove(module);
    }

    public void getAllCloneModuleChildren(Module module, List<Module> children) {
        if (module.getCloneChildren().isEmpty()) {
            children = new ArrayList<Module>();
            return;
        } else {
            for (Activity child : module.getCloneChildren()) {
                children.add((Module) child);
                getAllCloneModuleChildren((Module) child, children);
            }
        }
    }

    public Activity findActivityById(Long id) {
        for (Module module : modules) {
            for (Activity activity : module.getActivities()) {
                if (activity.getId().equals(id)) {
                    return activity;
                }
            }
        }
        return null;
    }

    public Long findProxyModuleIdHavingIOEdge(Long edgeId) {
        Edge edge = findEdgeById(edgeId);
        ProxyModule proxyModule = findProxyModuleHavingIOEdge(edge);
        return proxyModule.getId();
    }

    public String findInputNameFromEdgeId(Long proxyModuleId, Long edgeId) {
        Edge edge = findEdgeById(edgeId);
        ProxyModule proxyModule = (ProxyModule) findActivityById(proxyModuleId);
        return proxyModule.findInputNameFromEdge(edge);
    }

    public String findOutputNameFromEdgeId(Long proxyModuleId, Long edgeId) {
        Edge edge = findEdgeById(edgeId);
        ProxyModule proxyModule = (ProxyModule) findActivityById(proxyModuleId);
        return proxyModule.findOutputNameFromEdge(edge);
    }

    public ProxyModule findProxyModuleHavingIOEdge(Edge edge) {
        for (Module module : modules) {
            for (Activity activity : module.getActivities()) {
                if (activity instanceof ProxyModule) {
                    ProxyModule proxyModule = (ProxyModule) activity;
                    if (edge instanceof InputProxyEdge) {
                        if (proxyModule.isInputEdgeExist((InputProxyEdge) edge)) {
                            return proxyModule;
                        }
                    } else {
                        if (proxyModule.isOutputEdgeExist((OutputProxyEdge) edge)) {
                            return proxyModule;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void run(Map<String, Object> params, Long tokenId) {
        run(params, tokenId, false);
    }

    public void runStepByStep(Map<String, Object> params, Long tokenId) {
        run(params, tokenId, true);
    }

    private void run(Map<String, Object> params, Long tokenId, boolean stepByStep) {
        boolean endLoop = false;
        while (endLoop == false) {
            for (Module module : modules) {
                for (Activity activity : module.getActivities()) {
                    if (activity instanceof SimpleActivity) {
                        SimpleActivity simpleActivity = (SimpleActivity) activity;
                        if (simpleActivity.isActive(tokenId) == true) {
                            if (simpleActivity.getToEdge() != null) {
                                if (simpleActivity.getToEdge().run(params, tokenId)) {
                                    Activity toActivity = simpleActivity.getToEdge().getToActivity();
                                    if (toActivity == null) {
                                        if (module.doesOutputContain(simpleActivity.getToEdge())) {
                                            Token token = simpleActivity.getAndRemoveToken(tokenId);
                                            Activity edgeToActivity = simpleActivity.getToEdge().getToActivityFromToken(token);
                                            if (edgeToActivity != null) {
                                                ((SimpleActivity) edgeToActivity).setUncommitedToken(token);
                                            }
                                        }
                                    } else if (toActivity instanceof SimpleActivity) {
                                        Token token = simpleActivity.getAndRemoveToken(tokenId);
                                        if (simpleActivity.getToEdge() instanceof InputProxyEdge) {
                                            InputProxyEdge proxyEdge = (InputProxyEdge) simpleActivity.getToEdge();
                                            ProxyModule proxyModule = findProxyModuleHavingIOEdge(proxyEdge);
                                            token.pushVisitedProxyModule(proxyModule);
                                        }
                                        ((SimpleActivity) toActivity).setUncommitedToken(token);
                                    } else if (toActivity instanceof OneToMany) {
                                        OneToMany oneToMany = (OneToMany) toActivity;
                                        if (oneToMany instanceof Decision) {
                                            loopAllEdges: for (Edge edge : oneToMany.getToEdges()) {
                                                if (edge.run(params, tokenId)) {
                                                    Token token = simpleActivity.getAndRemoveToken(tokenId);
                                                    if (edge instanceof InputProxyEdge) {
                                                        InputProxyEdge proxyEdge = (InputProxyEdge) simpleActivity.getToEdge();
                                                        ProxyModule proxyModule = findProxyModuleHavingIOEdge(proxyEdge);
                                                        token.pushVisitedProxyModule(proxyModule);
                                                    }
                                                    Activity edgeToActivity = edge.getToActivity();
                                                    if (edgeToActivity == null) {
                                                        edgeToActivity = edge.getToActivityFromToken(token);
                                                    }
                                                    ((SimpleActivity) edgeToActivity).setUncommitedToken(token);
                                                    break loopAllEdges;
                                                }
                                            }
                                        } else {
                                            Token origToken = simpleActivity.getAndRemoveToken(tokenId);
                                            for (Edge edge : oneToMany.getToEdges()) {
                                                Token token = origToken.copy();
                                                Activity edgeToActivity = edge.getToActivity();
                                                if (edgeToActivity == null) {
                                                    edgeToActivity = edge.getToActivityFromToken(token);
                                                }
                                                ((SimpleActivity) edgeToActivity).setUncommitedToken(token);
                                            }
                                        }
                                    } else if (toActivity instanceof ManyToOne) {
                                        ManyToOne manyToOne = (ManyToOne) toActivity;
                                        if (manyToOne instanceof Join) {
                                            Join join = (Join) manyToOne;
                                            boolean allActivitiesActive = true;
                                            for (Edge fromEdgeOfJoin : join.getFromEdges()) {
                                                allActivitiesActive = allActivitiesActive && ((SimpleActivity) fromEdgeOfJoin.getFromActivity()).isActive(tokenId);
                                            }
                                            if (allActivitiesActive) {
                                                if (join.getToEdge().run(params, tokenId)) {
                                                    Token token = null;
                                                    for (Edge edge : join.getFromEdges()) {
                                                        token = ((SimpleActivity) edge.getFromActivity()).getAndRemoveToken(tokenId);
                                                    }
                                                    Activity edgeToActivity = join.getToEdge().getToActivity();
                                                    if (edgeToActivity == null) {
                                                        edgeToActivity = join.getToEdge().getToActivityFromToken(token);
                                                    }
                                                    ((SimpleActivity) edgeToActivity).setUncommitedToken(token);
                                                }
                                            }
                                        } else {
                                            Token token = simpleActivity.getAndRemoveToken(tokenId);
                                            Activity edgeToActivity = manyToOne.getToEdge().getToActivity();
                                            if (edgeToActivity == null) {
                                                edgeToActivity = manyToOne.getToEdge().getToActivityFromToken(token);
                                            }
                                            ((SimpleActivity) edgeToActivity).setUncommitedToken(token);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (Module module : modules) {
                for (Activity activity : module.getActivities()) {
                    if (activity instanceof SimpleActivity) {
                        ((SimpleActivity) activity).commitToken(tokenId);
                    }
                }
            }
            for (Module module : modules) {
                for (Activity activity : module.getActivities()) {
                    if (activity instanceof SimpleActivity) {
                        SimpleActivity simpleActivity = (SimpleActivity) activity;
                        if (simpleActivity.isActive(tokenId) == true) {
                            simpleActivity.run(params, tokenId);
                        }
                    }
                }
            }
            endLoop = true;
            if (stepByStep == false) {
                for (Module module : modules) {
                    for (Activity activity : module.getActivities()) {
                        if (activity instanceof SimpleActivity) {
                            SimpleActivity simpleActivity = (SimpleActivity) activity;
                            if (simpleActivity.isActive(tokenId) == true) {
                                Activity toActivity = ((SimpleActivity) activity).getToEdge().getToActivity();
                                if (toActivity instanceof SimpleActivity || toActivity instanceof Merge || toActivity instanceof Fork) {
                                    if (((SimpleActivity) activity).getToEdge().run(params, tokenId) == true) {
                                        endLoop = false;
                                    }
                                } else if (toActivity instanceof Decision) {
                                    Decision decision = (Decision) toActivity;
                                    for (Edge toEdge : decision.getToEdges()) {
                                        if (toEdge.run(params, tokenId) == true) {
                                            endLoop = false;
                                        }
                                    }
                                } else if (toActivity instanceof Join) {
                                    Join join = (Join) toActivity;
                                    boolean allActivitiesActive = true;
                                    for (Edge fromEdgeOfJoin : join.getFromEdges()) {
                                        allActivitiesActive = allActivitiesActive && ((SimpleActivity) fromEdgeOfJoin.getFromActivity()).isActive(tokenId);
                                    }
                                    if (allActivitiesActive) {
                                        if (join.getToEdge().run(params, tokenId)) {
                                            endLoop = false;
                                        }
                                    }
                                } else if (toActivity == null) {
                                    Module chosenModule = null;
                                    for (Module m : modules) {
                                        if (m.getActivities().contains(activity)) {
                                            chosenModule = m;
                                        }
                                    }
                                    if (chosenModule != null) {
                                        if (chosenModule.doesOutputContain(((SimpleActivity) activity).getToEdge())) {
                                            endLoop = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Set<Long> findAllTokens() {
        Set<Long> tokens = new HashSet<Long>();
        for (Module m : modules) {
            for (Activity a : m.getActivities()) {
                if (a instanceof SimpleActivity) {
                    SimpleActivity sa = (SimpleActivity) a;
                    tokens.addAll(sa.getTokenIds());
                }
            }
        }
        return tokens;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void removeModule(Module module) {
        modules.remove(module);
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public AtomicLong getElementIndex() {
        if (elementIndex == null) {
            elementIndex = new AtomicLong();
        }
        return elementIndex;
    }

    public void setElementIndex(AtomicLong index) {
        this.elementIndex = index;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public AtomicLong getTokenIndex() {
        if (tokenIndex == null) tokenIndex = new AtomicLong();
        return tokenIndex;
    }

    public List<ProxyModule> getProxyModulesUnderModule(Module module) {
        List<ProxyModule> proxies = new ArrayList<ProxyModule>();
        for (Activity activity : module.getActivities()) {
            if (activity instanceof ProxyModule) {
                proxies.add((ProxyModule) activity);
            }
        }
        return proxies;
    }
}

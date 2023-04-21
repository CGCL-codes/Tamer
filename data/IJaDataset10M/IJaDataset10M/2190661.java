package wilos.business.services.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wilos.business.services.gen.ConcreteActivityService;
import wilos.business.services.gen.ConcreteIterationService;
import wilos.business.services.gen.ConcretePhaseService;
import wilos.business.services.gen.IterationService;
import wilos.business.services.gen.ProjectService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.misc.concretephase.ConcretePhase;
import wilos.model.misc.project.Project;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.iteration.Iteration;
import wilos.model.spem2.task.TaskDescriptor;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class IterationHomeService {

    private ActivityHomeService activityHomeService;

    private ConcreteActivityService concreteActivityService;

    private ConcreteIterationService concreteIterationService;

    private ConcretePhaseService concretePhaseService;

    private IterationService iterationService;

    private ProjectService projectService;

    private TaskDescriptorHomeService taskDescriptorHomeService;

    public Set<ConcreteIteration> getAllConcreteIterationsForAProject(Iteration _iteration, Project _project) {
        Set<ConcreteIteration> tmp = new HashSet<ConcreteIteration>();
        this.iterationService.getIterationDao().getSessionFactory().getCurrentSession().saveOrUpdate(_iteration);
        this.projectService.getProjectDao().getSessionFactory().getCurrentSession().saveOrUpdate(_project);
        for (ConcreteIteration cit : _iteration.getConcreteIterations()) {
            if (cit.getProject().getId().equals(_project.getId())) tmp.add(cit);
        }
        return tmp;
    }

    /**
	 * Instanciates an iteration for a project
	 * 
	 * @param _project
	 *            project for which the iteration shall be instanciated
	 * @param _phase
	 *            iteration to instanciates
	 */
    public void iterationInstanciation(Project _project, Iteration _iteration, ConcreteActivity _cact, List<HashMap<String, Object>> _list, int _occ, boolean _isInstanciated) {
        if (_occ > 0) {
            this.concreteActivityService.getConcreteActivityDao().getSessionFactory().getCurrentSession().saveOrUpdate(_cact);
            int nbCit = 0;
            for (ConcreteBreakdownElement tmp : _cact.getConcreteBreakdownElements()) {
                if (tmp instanceof ConcreteIteration) {
                    if (((ConcreteIteration) tmp).getIteration().getId().equals(_iteration.getId())) {
                        nbCit++;
                    }
                }
            }
            for (int i = nbCit + 1; i <= nbCit + _occ; i++) {
                ConcreteIteration ci = new ConcreteIteration();
                List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
                bdes.addAll(this.activityHomeService.getActivityService().getBreakdownElements(_iteration));
                if (_iteration.getPresentationName().equals("")) ci.setConcreteName(_iteration.getName() + "#" + i); else ci.setConcreteName(_iteration.getPresentationName() + "#" + i);
                ci.addIteration(_iteration);
                ci.setProject(_project);
                ci.setBreakdownElement(_iteration);
                ci.setInstanciationOrder(i);
                ci.setWorkBreakdownElement(_iteration);
                ci.setActivity(_iteration);
                _cact.setConcreteBreakdownElements(this.concreteActivityService.getConcreteBreakdownElements(_cact));
                ci.addSuperConcreteActivity(_cact);
                this.concreteIterationService.saveConcreteIteration(ci);
                System.out.println("### ConcreteIteration vide sauve");
                for (BreakdownElement bde : bdes) {
                    if (bde instanceof Iteration) {
                        Iteration it = (Iteration) bde;
                        int occ = this.giveNbOccurences(it.getId(), _list, false);
                        if (occ == 0 && _occ > 0) occ = _occ;
                        this.iterationInstanciation(_project, it, ci, _list, occ, _isInstanciated);
                    } else {
                        if (bde instanceof Activity) {
                            Activity act = (Activity) bde;
                            int occ = this.giveNbOccurences(act.getId(), _list, false);
                            if (occ == 0 && _occ > 0) occ = _occ;
                            this.activityHomeService.activityInstanciation(_project, act, ci, _list, occ, _isInstanciated);
                        } else {
                            if (bde instanceof TaskDescriptor) {
                                TaskDescriptor td = (TaskDescriptor) bde;
                                int occ = this.giveNbOccurences(td.getId(), _list, false);
                                if (occ == 0 && _occ > 0) occ = _occ;
                                this.taskDescriptorHomeService.taskDescriptorInstanciation(_project, td, ci, occ, _isInstanciated);
                            }
                        }
                    }
                }
                this.concreteIterationService.saveConcreteIteration(ci);
                System.out.println("### ConcreteIteration update");
            }
        }
    }

    public void iterationUpdate(Project _project, Iteration _it, Set<ConcreteActivity> _cacts, List<HashMap<String, Object>> _list, int _occ) {
        if (_occ > 0) {
            for (ConcreteActivity tmp : _cacts) {
                this.iterationInstanciation(_project, _it, tmp, _list, _occ, true);
                if (tmp instanceof Project) {
                    Project pj = (Project) tmp;
                    this.projectService.saveProject(pj);
                } else {
                    if (tmp instanceof ConcretePhase) {
                        ConcretePhase cph = (ConcretePhase) tmp;
                        this.concretePhaseService.saveConcretePhase(cph);
                    } else {
                        if (tmp instanceof ConcreteIteration) {
                            ConcreteIteration cit = (ConcreteIteration) tmp;
                            this.concreteIterationService.saveConcreteIteration(cit);
                        }
                    }
                }
            }
        } else {
            Set<BreakdownElement> bdes = new HashSet<BreakdownElement>();
            bdes.addAll(this.activityHomeService.getActivityService().getBreakdownElements(_it));
            Set<ConcreteActivity> cacts = new HashSet<ConcreteActivity>();
            cacts.addAll(this.getAllConcreteIterationsForAProject(_it, _project));
            for (BreakdownElement bde : bdes) {
                if (bde instanceof Iteration) {
                    Iteration it = (Iteration) bde;
                    int occ = this.giveNbOccurences(it.getId(), _list, true);
                    this.iterationUpdate(_project, it, cacts, _list, occ);
                } else {
                    if (bde instanceof Activity) {
                        Activity act = (Activity) bde;
                        int occ = this.giveNbOccurences(act.getId(), _list, true);
                        this.activityHomeService.activityUpdate(_project, act, cacts, _list, occ);
                    } else {
                        if (bde instanceof TaskDescriptor) {
                            TaskDescriptor td = (TaskDescriptor) bde;
                            int occ = this.giveNbOccurences(td.getId(), _list, true);
                            this.taskDescriptorHomeService.taskDescriptorUpdate(_project, td, cacts, occ);
                        }
                    }
                }
            }
        }
    }

    private int giveNbOccurences(String _id, List<HashMap<String, Object>> list, boolean _isInstanciated) {
        int nb;
        if (!_isInstanciated) nb = 1; else nb = 0;
        for (HashMap<String, Object> hashMap : list) {
            if (((String) hashMap.get("id")).equals(_id)) {
                nb = ((Integer) hashMap.get("nbOccurences")).intValue();
                break;
            }
        }
        return nb;
    }

    public IterationService getIterationService() {
        return iterationService;
    }

    public void setIterationService(IterationService iterationService) {
        this.iterationService = iterationService;
    }

    public ActivityHomeService getActivityHomeService() {
        return activityHomeService;
    }

    public void setActivityHomeService(ActivityHomeService activityHomeService) {
        this.activityHomeService = activityHomeService;
    }

    public ConcreteActivityService getConcreteActivityService() {
        return concreteActivityService;
    }

    public void setConcreteActivityService(ConcreteActivityService concreteActivityService) {
        this.concreteActivityService = concreteActivityService;
    }

    public ConcreteIterationService getConcreteIterationService() {
        return concreteIterationService;
    }

    public void setConcreteIterationService(ConcreteIterationService concreteIterationService) {
        this.concreteIterationService = concreteIterationService;
    }

    public ConcretePhaseService getConcretePhaseService() {
        return concretePhaseService;
    }

    public void setConcretePhaseService(ConcretePhaseService concretePhaseService) {
        this.concretePhaseService = concretePhaseService;
    }

    public ProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public TaskDescriptorHomeService getTaskDescriptorHomeService() {
        return taskDescriptorHomeService;
    }

    public void setTaskDescriptorHomeService(TaskDescriptorHomeService taskDescriptorHomeService) {
        this.taskDescriptorHomeService = taskDescriptorHomeService;
    }
}

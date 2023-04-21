package wilos.business.services.home;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wilos.business.services.gen.WorkOrderService;
import wilos.hibernate.gen.ActivityDao;
import wilos.hibernate.gen.BreakdownElementDao;
import wilos.hibernate.gen.CheckListDao;
import wilos.hibernate.gen.ElementDao;
import wilos.hibernate.gen.GuidanceDao;
import wilos.hibernate.gen.IterationDao;
import wilos.hibernate.gen.PhaseDao;
import wilos.hibernate.gen.ProcessDao;
import wilos.hibernate.gen.ProjectDao;
import wilos.hibernate.gen.RoleDefinitionDao;
import wilos.hibernate.gen.RoleDescriptorDao;
import wilos.hibernate.gen.SectionDao;
import wilos.hibernate.gen.StepDao;
import wilos.hibernate.gen.TaskDefinitionDao;
import wilos.hibernate.gen.TaskDescriptorDao;
import wilos.hibernate.gen.WorkBreakdownElementDao;
import wilos.model.spem2.activity.Activity;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.checklist.CheckList;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.iteration.Iteration;
import wilos.model.spem2.phase.Phase;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.role.RoleDefinition;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.model.spem2.section.Section;
import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.model.spem2.workbreakdownelement.WorkBreakdownElement;
import wilos.model.spem2.workbreakdownelement.WorkOrder;

/**
 * ProcessManagementService is a transactional class, that handle removing of processes, requested by web
 * pages
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ProcessManagementService {

    private ActivityDao activityDao;

    private PhaseDao phaseDao;

    private IterationDao iterationDao;

    private BreakdownElementDao breakdownElementDao;

    private ElementDao elementDao;

    private ProcessDao processDao;

    private RoleDefinitionDao roleDefinitionDao;

    private RoleDescriptorDao roleDescriptorDao;

    private StepDao stepDao;

    private TaskDefinitionDao taskDefinitionDao;

    private TaskDescriptorDao taskDescriptorDao;

    private WorkBreakdownElementDao workBreakdownElementDao;

    private ProjectDao projectDao;

    private GuidanceDao guidanceDao;

    private CheckListDao checkListDao;

    private SectionDao sectionDao;

    private WorkOrderService workOrderService;

    private Set<Object> objetsToRemoveLast = new HashSet<Object>();

    private Set<WorkOrder> workOrderToRemove = new HashSet<WorkOrder>();

    /**
	 * 
	 * Method to remove a process that has not been instanciated yet
	 * 
	 * @param _process
	 *            a process to remove
	 */
    public void removeProcess(String _processId) {
        this.objetsToRemoveLast.clear();
        Process p = this.processDao.getProcess(_processId);
        List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
        bdes.addAll(p.getBreakdownElements());
        Set<Guidance> guid = new HashSet<Guidance>();
        for (BreakdownElement bde : bdes) {
            if (bde instanceof Phase) {
                Phase ph = (Phase) bde;
                guid = this.removePhase(ph, guid);
            } else {
                if (bde instanceof Iteration) {
                    Iteration it = (Iteration) bde;
                    guid = this.removeIteration(it, guid);
                } else {
                    if (bde instanceof Activity) {
                        Activity act = (Activity) bde;
                        guid = this.removeActivity(act, guid);
                    } else {
                        if (bde instanceof RoleDescriptor) {
                            RoleDescriptor rd = (RoleDescriptor) bde;
                            guid = this.removeRoleDescriptor(rd, guid);
                        } else {
                            TaskDescriptor td = (TaskDescriptor) bde;
                            guid = this.removeTaskDescriptor(td, guid);
                        }
                    }
                }
            }
        }
        for (Guidance g : guid) {
            this.removeGuidance(g);
        }
        this.removeWorkOrders();
        for (Object obj : this.objetsToRemoveLast) {
            if (obj instanceof Activity) this.activityDao.deleteActivity((Activity) obj);
            if (obj instanceof RoleDescriptor) this.roleDescriptorDao.deleteRoleDescriptor((RoleDescriptor) obj);
            if (obj instanceof TaskDescriptor) this.taskDescriptorDao.deleteTaskDescriptor((TaskDescriptor) obj);
            if (obj instanceof Step) this.stepDao.deleteStep((Step) obj);
        }
        for (Object def : this.objetsToRemoveLast) {
            if (def instanceof RoleDefinition) this.roleDefinitionDao.deleteRoleDefinition((RoleDefinition) def);
            if (def instanceof TaskDefinition) this.taskDefinitionDao.deleteTaskDefinition((TaskDefinition) def);
        }
        this.processDao.deleteProcess(p);
    }

    /**
	 * Remove all workOrders object of the process	 *
	 */
    private void removeWorkOrders() {
        for (WorkOrder wo : this.workOrderToRemove) {
            if (wo.getPredecessor() != null) {
                TaskDescriptor prec = this.taskDescriptorDao.getTaskDescriptor(wo.getPredecessor().getId());
                wo.setPredecessor(null);
                this.taskDescriptorDao.saveOrUpdateTaskDescriptor(prec);
            }
            if (wo.getSuccessor() != null) {
                TaskDescriptor succ = this.taskDescriptorDao.getTaskDescriptor(wo.getSuccessor().getId());
                wo.setSuccessor(null);
                this.taskDescriptorDao.saveOrUpdateTaskDescriptor(succ);
            }
            this.workOrderService.removeWorkOrder(wo);
        }
    }

    /**
	 * 
	 * Method to remove a phase from a process
	 * 
	 * @param _phase
	 *            a phase of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return global guidance updated with guidances of the current phase
	 */
    private Set<Guidance> removePhase(Phase _phase, Set<Guidance> _guid) {
        _phase = this.phaseDao.getPhase(_phase.getId());
        Set<Guidance> tmp = new HashSet<Guidance>();
        tmp.addAll(_guid);
        List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
        bdes.addAll(_phase.getBreakdownElements());
        tmp.addAll(_phase.getGuidances());
        for (BreakdownElement bde : bdes) {
            if (bde instanceof Iteration) {
                Iteration it = (Iteration) bde;
                tmp = this.removeIteration(it, tmp);
            } else {
                if (bde instanceof Activity) {
                    Activity act = (Activity) bde;
                    tmp = this.removeActivity(act, tmp);
                } else {
                    if (bde instanceof RoleDescriptor) {
                        RoleDescriptor rd = (RoleDescriptor) bde;
                        tmp = this.removeRoleDescriptor(rd, tmp);
                    } else {
                        TaskDescriptor td = (TaskDescriptor) bde;
                        tmp = this.removeTaskDescriptor(td, tmp);
                    }
                }
            }
        }
        this.phaseDao.deletePhase(_phase);
        return tmp;
    }

    /**
	 * 
	 * Method to remove an iteration from a process
	 * 
	 * @param _phase
	 *            an iteration of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return global guidance updated with guidances of the current iteration
	 */
    private Set<Guidance> removeIteration(Iteration _it, Set<Guidance> guid) {
        _it = this.iterationDao.getIteration(_it.getId());
        Set<Guidance> tmp = new HashSet<Guidance>();
        tmp.addAll(guid);
        List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
        bdes.addAll(_it.getBreakdownElements());
        tmp.addAll(_it.getGuidances());
        for (BreakdownElement bde : bdes) {
            if (bde instanceof Activity) {
                Activity act = (Activity) bde;
                tmp = this.removeActivity(act, tmp);
            } else {
                if (bde instanceof RoleDescriptor) {
                    RoleDescriptor rd = (RoleDescriptor) bde;
                    tmp = this.removeRoleDescriptor(rd, tmp);
                } else {
                    TaskDescriptor td = (TaskDescriptor) bde;
                    tmp = this.removeTaskDescriptor(td, tmp);
                }
            }
        }
        this.iterationDao.deleteIteration(_it);
        return tmp;
    }

    /**
	 * 
	 * Method to remove an activity from a process
	 * 
	 * @param _it
	 *            an activity of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the current activity
	 */
    private Set<Guidance> removeActivity(Activity _act, Set<Guidance> guid) {
        _act = this.activityDao.getActivity(_act.getId());
        Set<Guidance> tmp = new HashSet<Guidance>();
        tmp.addAll(guid);
        List<BreakdownElement> bdes = new ArrayList<BreakdownElement>();
        bdes.addAll(_act.getBreakdownElements());
        tmp.addAll(_act.getGuidances());
        for (BreakdownElement bde : bdes) {
            if (bde instanceof Activity) {
                Activity act = (Activity) bde;
                tmp = this.removeActivity(act, tmp);
            } else {
                if (bde instanceof RoleDescriptor) {
                    RoleDescriptor rd = (RoleDescriptor) bde;
                    tmp = this.removeRoleDescriptor(rd, tmp);
                } else {
                    TaskDescriptor td = (TaskDescriptor) bde;
                    tmp = this.removeTaskDescriptor(td, tmp);
                }
            }
        }
        this.findWorkOrders(_act);
        this.objetsToRemoveLast.add(_act);
        return tmp;
    }

    /**
	 * 
	 * Method to remove a roleDescriptor from a process
	 * 
	 * @param _rd
	 *            a roleRoleDescriptor of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the current roleDescriptor
	 */
    private Set<Guidance> removeRoleDescriptor(RoleDescriptor _rd, Set<Guidance> guid) {
        _rd = this.roleDescriptorDao.getRoleDescriptor(_rd.getId());
        Set<Guidance> tmp = new HashSet<Guidance>();
        tmp.addAll(guid);
        RoleDefinition rdef = _rd.getRoleDefinition();
        if (rdef != null) {
            tmp = this.removeRoleDefinition(rdef, tmp);
        }
        this.objetsToRemoveLast.add(_rd);
        return tmp;
    }

    /**
	 * 
	 * Method to remove a roleDefinition from a process
	 * 
	 * @param _rdef
	 *            a roleDefinition of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the current roleDefinition
	 */
    private Set<Guidance> removeRoleDefinition(RoleDefinition _rdef, Set<Guidance> guid) {
        _rdef = this.roleDefinitionDao.getRoleDefinition(_rdef.getId());
        Set<Guidance> tmp = new HashSet<Guidance>();
        tmp.addAll(guid);
        tmp.addAll(_rdef.getGuidances());
        this.objetsToRemoveLast.add(_rdef);
        return tmp;
    }

    /**
	 * 
	 * Method to remove a tasDescriptor from a process
	 * 
	 * @param _td
	 *            a taskDescriptor of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the current taskDescriptor
	 */
    private Set<Guidance> removeTaskDescriptor(TaskDescriptor _td, Set<Guidance> guid) {
        _td = this.taskDescriptorDao.getTaskDescriptor(_td.getId());
        Set<Guidance> tmp = new HashSet<Guidance>();
        tmp.addAll(guid);
        TaskDefinition tdef = _td.getTaskDefinition();
        if (tdef != null) {
            tmp = this.removeTaskDefinition(tdef, tmp);
        }
        this.findWorkOrders(_td);
        this.objetsToRemoveLast.add(_td);
        return tmp;
    }

    /**
	 * Detect all workOrder objects related to workBreakdownElement
	 * @param _wbde WorkBreakdownElement object
	 */
    private void findWorkOrders(WorkBreakdownElement _wbde) {
        for (WorkOrder wo : _wbde.getPredecessors()) {
            this.workOrderToRemove.add(wo);
        }
        for (WorkOrder wo : _wbde.getSuccessors()) {
            this.workOrderToRemove.add(wo);
        }
    }

    /**
	 * 
	 * Method to remove a taskDefinition from a process
	 * 
	 * @param _tdef
	 *            a taskDefinition of the current process
	 * @param _guid
	 *            global guidances to remove from the database
	 * 
	 * @return Set<Guidance> global guidance updated with guidances of the current taskDefinition
	 */
    private Set<Guidance> removeTaskDefinition(TaskDefinition _tdef, Set<Guidance> guid) {
        _tdef = this.taskDefinitionDao.getTaskDefinition(_tdef.getId());
        Set<Guidance> tmp = new HashSet<Guidance>();
        tmp.addAll(guid);
        tmp.addAll(_tdef.getGuidances());
        List<Step> steps = new ArrayList<Step>();
        steps.addAll(_tdef.getSteps());
        for (Step step : steps) {
            step = this.stepDao.getStep(step.getId());
            this.objetsToRemoveLast.add(step);
        }
        this.objetsToRemoveLast.add(_tdef);
        return tmp;
    }

    /**
	 * 
	 * Method to remove a guidance from a process
	 * 
	 * @param _guidance
	 *            a guidance of the current process
	 */
    private void removeGuidance(Guidance _guidance) {
        if (_guidance instanceof CheckList) {
            CheckList cl = (CheckList) _guidance;
            this.removeCheckList(cl);
        } else {
            _guidance = this.guidanceDao.getGuidance(_guidance.getId());
            this.guidanceDao.deleteGuidance(_guidance);
        }
    }

    /**
	 * 
	 * Method to remove a checkList from a process
	 * 
	 * @param _checkList
	 *            a checkList of the current process
	 */
    private void removeCheckList(CheckList _checkList) {
        _checkList = this.checkListDao.getCheckList(_checkList.getId());
        List<Section> sections = new ArrayList<Section>();
        sections.addAll(_checkList.getSections());
        for (Section section : sections) {
            this.removeSection(section);
        }
        this.checkListDao.deleteCheckList(_checkList);
    }

    /**
	 * 
	 * Method to remove a section from a process
	 * 
	 * @param _section
	 *            a section of the current process
	 */
    private void removeSection(Section _section) {
        _section = this.sectionDao.getSection(_section.getId());
        this.sectionDao.deleteSection(_section);
    }

    /**
	 * Tests if a process has been already instanciated.
	 * 
	 * @param _processId identificator of the process to be tested
	 * @return true if process has a non-null list projects, false otherwise
	 */
    public boolean hasBeenInstanciated(String _processId) {
        Process currentProcess = this.processDao.getProcess(_processId);
        if (currentProcess.getProjects().isEmpty()) return false; else return true;
    }

    /**
	 * Getter of processDao.
	 * 
	 * @return the processDao.
	 */
    public ProcessDao getProcessDao() {
        return processDao;
    }

    /**
	 * Setter of processDao.
	 * 
	 * @param _processDao
	 *            The processDao to set.
	 */
    public void setProcessDao(ProcessDao _processDao) {
        this.processDao = _processDao;
    }

    /**
	 * Getter of activityDao.
	 * 
	 * @return the activityDao.
	 */
    public ActivityDao getActivityDao() {
        return activityDao;
    }

    /**
	 * Setter of activityDao.
	 * 
	 * @param activityDao
	 *            The activityDao to set.
	 */
    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    /**
	 * Getter of breakdownElementDao.
	 * 
	 * @return the breakdownElementDao.
	 */
    public BreakdownElementDao getBreakdownElementDao() {
        return breakdownElementDao;
    }

    /**
	 * Setter of breakdownElementDao.
	 * 
	 * @param breakdownElementDao
	 *            The breakdownElementDao to set.
	 */
    public void setBreakdownElementDao(BreakdownElementDao breakdownElementDao) {
        this.breakdownElementDao = breakdownElementDao;
    }

    /**
	 * Getter of elementDao.
	 * 
	 * @return the elementDao.
	 */
    public ElementDao getElementDao() {
        return elementDao;
    }

    /**
	 * Setter of elementDao.
	 * 
	 * @param elementDao
	 *            The elementDao to set.
	 */
    public void setElementDao(ElementDao elementDao) {
        this.elementDao = elementDao;
    }

    /**
	 * Getter of roleDefinitionDao.
	 * 
	 * @return the roleDefinitionDao.
	 */
    public RoleDefinitionDao getRoleDefinitionDao() {
        return roleDefinitionDao;
    }

    /**
	 * Setter of roleDefinitionDao.
	 * 
	 * @param roleDefinitionDao
	 *            The roleDefinitionDao to set.
	 */
    public void setRoleDefinitionDao(RoleDefinitionDao roleDefinitionDao) {
        this.roleDefinitionDao = roleDefinitionDao;
    }

    /**
	 * Getter of roleDescriptorDao.
	 * 
	 * @return the roleDescriptorDao.
	 */
    public RoleDescriptorDao getRoleDescriptorDao() {
        return roleDescriptorDao;
    }

    /**
	 * Setter of roleDescriptorDao.
	 * 
	 * @param roleDescriptorDao
	 *            The roleDescriptorDao to set.
	 */
    public void setRoleDescriptorDao(RoleDescriptorDao roleDescriptorDao) {
        this.roleDescriptorDao = roleDescriptorDao;
    }

    /**
	 * Getter of stepDao.
	 * 
	 * @return the stepDao.
	 */
    public StepDao getStepDao() {
        return stepDao;
    }

    /**
	 * Setter of stepDao.
	 * 
	 * @param stepDao
	 *            The stepDao to set.
	 */
    public void setStepDao(StepDao stepDao) {
        this.stepDao = stepDao;
    }

    /**
	 * Getter of taskDefinitionDao.
	 * 
	 * @return the taskDefinitionDao.
	 */
    public TaskDefinitionDao getTaskDefinitionDao() {
        return taskDefinitionDao;
    }

    /**
	 * Setter of taskDefinitionDao.
	 * 
	 * @param taskDefinitionDao
	 *            The taskDefinitionDao to set.
	 */
    public void setTaskDefinitionDao(TaskDefinitionDao taskDefinitionDao) {
        this.taskDefinitionDao = taskDefinitionDao;
    }

    /**
	 * Getter of taskDescriptorDao.
	 * 
	 * @return the taskDescriptorDao.
	 */
    public TaskDescriptorDao getTaskDescriptorDao() {
        return taskDescriptorDao;
    }

    /**
	 * Setter of taskDescriptorDao.
	 * 
	 * @param taskDescriptorDao
	 *            The taskDescriptorDao to set.
	 */
    public void setTaskDescriptorDao(TaskDescriptorDao taskDescriptorDao) {
        this.taskDescriptorDao = taskDescriptorDao;
    }

    /**
	 * Getter of workBreakdownElementDao.
	 * 
	 * @return the workBreakdownElementDao.
	 */
    public WorkBreakdownElementDao getWorkBreakdownElementDao() {
        return workBreakdownElementDao;
    }

    /**
	 * Setter of workBreakdownElementDao.
	 * 
	 * @param workBreakdownElementDao
	 *            The workBreakdownElementDao to set.
	 */
    public void setWorkBreakdownElementDao(WorkBreakdownElementDao workBreakdownElementDao) {
        this.workBreakdownElementDao = workBreakdownElementDao;
    }

    /**
	 * Getter of checkListDao.
	 * 
	 * @return the checkListDao.
	 */
    public CheckListDao getCheckListDao() {
        return this.checkListDao;
    }

    /**
	 * Setter of checkListDao.
	 * 
	 * @param checkListDao
	 *            The checkListDao to set.
	 */
    public void setCheckListDao(CheckListDao _checkListDao) {
        this.checkListDao = _checkListDao;
    }

    /**
	 * Getter of guidanceDao.
	 * 
	 * @return the guidanceDao.
	 */
    public GuidanceDao getGuidanceDao() {
        return this.guidanceDao;
    }

    /**
	 * Setter of guidanceDao.
	 * 
	 * @param guidanceDao
	 *            The guidanceDao to set.
	 */
    public void setGuidanceDao(GuidanceDao _guidanceDao) {
        this.guidanceDao = _guidanceDao;
    }

    /**
	 * Getter of iterationDao.
	 * 
	 * @return the iterationDao.
	 */
    public IterationDao getIterationDao() {
        return this.iterationDao;
    }

    /**
	 * Setter of iterationDao.
	 * 
	 * @param iterationDao
	 *            The iterationDao to set.
	 */
    public void setIterationDao(IterationDao _iterationDao) {
        this.iterationDao = _iterationDao;
    }

    /**
	 * Getter of phaseDao.
	 * 
	 * @return the phaseDao.
	 */
    public PhaseDao getPhaseDao() {
        return this.phaseDao;
    }

    /**
	 * Setter of phaseDao.
	 * 
	 * @param phaseDao
	 *            The phaseDao to set.
	 */
    public void setPhaseDao(PhaseDao _phaseDao) {
        this.phaseDao = _phaseDao;
    }

    /**
	 * Getter of projectDao.
	 * 
	 * @return the projectDao.
	 */
    public ProjectDao getProjectDao() {
        return this.projectDao;
    }

    /**
	 * Setter of projectDao.
	 * 
	 * @param projectDao
	 *            The projectDao to set.
	 */
    public void setProjectDao(ProjectDao _projectDao) {
        this.projectDao = _projectDao;
    }

    /**
	 * Getter of sectionDao.
	 * 
	 * @return the sectionDao.
	 */
    public SectionDao getSectionDao() {
        return this.sectionDao;
    }

    /**
	 * Setter of sectionDao.
	 * 
	 * @param sectionDao
	 *            The sectionDao to set.
	 */
    public void setSectionDao(SectionDao _sectionDao) {
        this.sectionDao = _sectionDao;
    }

    public WorkOrderService getWorkOrderService() {
        return workOrderService;
    }

    public void setWorkOrderService(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }
}

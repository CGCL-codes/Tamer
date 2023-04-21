package pathplanning.plan;

import pathplanning.plan.actions.LazyPRMGenerator;
import pathplanning.plan.actions.RePlannedWalk;
import planning.model.CollisionWorld;
import planning.plan.IController;
import simulation.shell.Shell;

public class LazyPRMRePlanController extends PathPlannerController implements IController<CollisionWorld> {

    public int neighborCount;

    public int sampleCount;

    public int sampleCountIncrement;

    public int sampleCountLimit;

    public int escapeTreeSize;

    public double collisionSaveEnlargement;

    public int maxEscapeTrial;

    public int maxSimpleEscapeTrial;

    public double savedCollisionEnlargement;

    public LazyPRMRePlanController(String name, Shell shell) {
        super(name, shell);
        takingStatistics = true;
        plannedWalk = new RePlannedWalk(world, shell, new LazyPRMGenerator());
    }

    public ControllerType getType() {
        return IController.ControllerType.LAZY_PRM_REPLAN_Controller;
    }

    public int getNeighborCount() {
        return neighborCount;
    }

    public void setNeighborCount(int count) {
        this.neighborCount = count;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int count) {
        this.sampleCount = count;
    }

    public void setSampleCountLimit(int sampleCountLimit) {
        this.sampleCountLimit = sampleCountLimit;
    }

    public int getSampleCountLimit() {
        return sampleCountLimit;
    }

    public void setSampleCountIncrement(int incrementCount) {
        this.sampleCountIncrement = incrementCount;
    }

    public int getSampleCountIncrement() {
        return sampleCountIncrement;
    }

    public boolean initialize() {
        RePlannedWalk rePlannedWalk = (RePlannedWalk) plannedWalk;
        rePlannedWalk.setEscapeTreeSize(escapeTreeSize);
        rePlannedWalk.setCollisionSaveEnlargement(collisionSaveEnlargement);
        rePlannedWalk.setMaxEscapeTrial(maxEscapeTrial);
        rePlannedWalk.setMaxSimpleEscapeTrial(maxSimpleEscapeTrial);
        LazyPRMGenerator plan = (LazyPRMGenerator) plannedWalk.getPathPlannerGenerator();
        plan.setParameters(sampleCount, neighborCount, sampleCountIncrement, sampleCountLimit);
        System.out.println("Enlargement : " + enlargement);
        plannedWalk.setEnlargement(enlargement);
        plannedWalk.reset();
        return plannedWalk.initialize();
    }

    public double getCollisionSaveEnlargement() {
        return collisionSaveEnlargement;
    }

    public void setCollisionSaveEnlargement(double collisionSaveEnlargement) {
        this.collisionSaveEnlargement = collisionSaveEnlargement;
    }

    public int getEscapeTreeSize() {
        return escapeTreeSize;
    }

    public void setEscapeTreeSize(int escapeTreeSize) {
        this.escapeTreeSize = escapeTreeSize;
    }

    public int getMaxEscapeTrial() {
        return maxEscapeTrial;
    }

    public void setMaxEscapeTrial(int maxEscapeTrial) {
        this.maxEscapeTrial = maxEscapeTrial;
    }

    public int getMaxSimpleEscapeTrial() {
        return maxSimpleEscapeTrial;
    }

    public void setMaxSimpleEscapeTrial(int maxSimpleEscapeTrial) {
        this.maxSimpleEscapeTrial = maxSimpleEscapeTrial;
    }

    public double getSavedCollisionEnlargement() {
        return savedCollisionEnlargement;
    }

    public void setSavedCollisionEnlargement(double savedCollisionEnlargement) {
        this.savedCollisionEnlargement = savedCollisionEnlargement;
    }
}

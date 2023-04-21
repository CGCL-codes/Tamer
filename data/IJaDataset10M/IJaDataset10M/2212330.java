package com.cube42.echoverse.entity;

import com.cube42.echoverse.model.Egram;
import com.cube42.echoverse.model.Entity;
import com.cube42.echoverse.model.EntityID;
import com.cube42.echoverse.model.WorldResource;
import com.cube42.echoverse.model.absorb.AbsorbInterception;
import com.cube42.echoverse.model.absorb.AbsorbReaction;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.logging.Logger;

/**
 * The action job responsible for the absorb action
 *
 * @author  Matt Paulin
 * @version $Id: AbsorbActionJob.java,v 1.2 2003/03/12 01:48:28 zer0wing Exp $
 */
public class AbsorbActionJob extends ActionJob {

    /**
     * The action type for this job
     */
    public static final ActionType ACTION_TYPE = new ActionType("Absorb");

    /**
     * The entity that will have appeared to have caused this action
     */
    private EntityID enactor;

    /**
     * The entity that is being acted on
     */
    private EntityID reactor;

    /**
     * The type of resource being absorbed
     */
    private WorldResource resource;

    /**
     * The amount of resource to absorb
     */
    private Egram amount;

    /**
     * Constucts the AbsorbActionJob
     *
     * @param   diactor     The entity that is actually sent this action
     * @param   enactor     The entity that will appear to want to be absorbd the
     *                      resources.
     * @param   reactor     The entity that will receive the absorb action
     * @param   resource    The type of resource being absorbed
     * @param   amount      The amount (in egram) of the resouce to absorb
     */
    public AbsorbActionJob(EntityID diactor, EntityID enactor, EntityID reactor, WorldResource resource, Egram amount) {
        super(ACTION_TYPE, diactor);
        Cube42NullParameterException.checkNullInConstructor(enactor, "enactor", this);
        Cube42NullParameterException.checkNullInConstructor(reactor, "reactor", this);
        Cube42NullParameterException.checkNullInConstructor(resource, "resource", this);
        Cube42NullParameterException.checkNullInConstructor(amount, "amount", this);
        this.enactor = enactor;
        this.reactor = reactor;
        this.resource = resource;
        this.amount = amount;
    }

    /**
     * Method where the work of the thread is done.  Just like
     * the run method in the runnable interface.
     */
    public void runJob() throws InterruptedException {
        Entity tempEnactor = EntityManagerImpl.getEntityManagerImpl().getEntity(this.enactor);
        Entity tempReactor = EntityManagerImpl.getEntityManagerImpl().getEntity(this.reactor);
        if (tempReactor == null) {
            Logger.error(EntityManagerSystemCodes.UNKNOWN_REACTOR_IN_ACTION, new Object[] { this.reactor, this.getActionType() });
            return;
        }
        if (tempEnactor == null) {
            Logger.error(EntityManagerSystemCodes.UNKNOWN_ENACTOR_IN_ACTION, new Object[] { this.enactor, this.getActionType() });
            return;
        }
        EntityManagerImpl.getEntityManagerImpl().submitObservationJob(new AbsorbObservationJob(this.enactor, this.enactor, tempEnactor.getPosition(), this.reactor, tempReactor.getPosition(), this.resource, this.amount));
        Entity interceptor = getInterceptor(this.enactor);
        if (interceptor != null) {
            if (interceptor instanceof AbsorbInterception) {
                AbsorbInterception ai = (AbsorbInterception) interceptor;
                ai.absorbInterception(this.enactor, this.reactor, this.resource, this.amount);
                return;
            }
        }
        interceptor = getInterceptor(this.reactor);
        if (interceptor != null) {
            if (interceptor instanceof AbsorbInterception) {
                AbsorbInterception ai = (AbsorbInterception) interceptor;
                ai.absorbInterception(this.enactor, this.reactor, this.resource, this.amount);
                return;
            }
        }
        if (tempReactor instanceof AbsorbReaction) {
            AbsorbReaction ar = (AbsorbReaction) tempReactor;
            ar.absorbReaction(this.enactor, this.resource, this.amount);
            EntityManagerImpl.getEntityManagerImpl().submitObservationJob(new AbsorbObservationJob(this.reactor, this.enactor, tempEnactor.getPosition(), this.reactor, tempReactor.getPosition(), this.resource, this.amount));
        }
    }

    /**
     * The maxium amount of time that the job should take.
     * If it runs over this time then the job will be shut
     * down and logged.
     *
     * @return  The maximum amount of time a job should take
     */
    public long getMaxJobTime() {
        return 5000;
    }
}

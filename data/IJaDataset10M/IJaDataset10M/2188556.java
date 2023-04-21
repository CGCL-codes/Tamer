package com.cube42.echoverse.entity;

import com.cube42.echoverse.model.Entity;
import com.cube42.echoverse.model.EntityID;
import com.cube42.echoverse.model.Information;
import com.cube42.echoverse.model.InformationKey;
import com.cube42.echoverse.model.inform.InformObservation;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.math.Position;

/**
 * The observation job responsible for the sending information to the
 * inform observers
 *
 * @author  Matt Paulin
 * @version $Id: InformObservationJob.java,v 1.2 2003/03/12 01:48:26 zer0wing Exp $
 */
public class InformObservationJob extends ObservationJob {

    /**
     * The entity that attempted to inform the reactor
     */
    private EntityID enactor;

    /**
     * The position of the enactor
     */
    private Position enactorPos;

    /**
     * The entity that was to be informed with the information
     */
    private EntityID reactor;

    /**
     * The position of the reactor
     */
    private Position reactorPos;

    /**
     * InformationKey describing what the information pertains to
     */
    private InformationKey key;

    /**
     * The information being sent
     */
    private Information info;

    /**
     * Constucts the InformObservationJob
     *
     * @param   observeEntity   The entity that was observed
     * @param   enactor         The entity that attempted to inform the reactor
     * @param   enactorPos      The position of the enactor
     * @param   reactor         The entity that was attempted to be informed
     * @param   reactorPos      The position of the reactor
     * @param   key             The key describing what the information is
     * @param   info            The information
     */
    public InformObservationJob(EntityID observeEntity, EntityID enactor, Position enactorPos, EntityID reactor, Position reactorPos, InformationKey key, Information info) {
        super(InformActionJob.ACTION_TYPE, observeEntity);
        Cube42NullParameterException.checkNullInConstructor(enactor, "enactor", this);
        Cube42NullParameterException.checkNullInConstructor(enactorPos, "enactorPos", this);
        Cube42NullParameterException.checkNullInConstructor(reactor, "reactor", this);
        Cube42NullParameterException.checkNullInConstructor(reactorPos, "reactorPos", this);
        Cube42NullParameterException.checkNullInConstructor(key, "key", this);
        Cube42NullParameterException.checkNullInConstructor(info, "info", this);
        this.enactor = enactor;
        this.enactorPos = enactorPos;
        this.reactor = reactor;
        this.reactorPos = reactorPos;
        this.key = key;
        this.info = info;
    }

    /**
     * Method that is called when an entity need to be notified of an
     * observation
     *
     * @param   entity      The entity to notify of an observation
     */
    public void notifyObservation(Entity entity) {
        if ((entity != null) && (entity instanceof InformObservation)) {
            InformObservation observation = (InformObservation) entity;
            observation.informObservation(this.enactor, this.enactorPos, this.reactor, this.reactorPos, this.key, this.info);
        }
    }
}

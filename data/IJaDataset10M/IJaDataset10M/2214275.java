package com.mw3d.swt.util.command;

import com.mw3d.core.entity.DynamicEntity;

/**
 * Class particle command is a class for undoing 
 * and redoing adding particles to the dynamic entity.
 * @author Tareq doufish
 * Created on Jul 12, 2005
 */
public class ParticleCreationCommand implements Command {

    private DynamicEntity entity;

    private String type;

    private int numberOfParticles;

    public ParticleCreationCommand(DynamicEntity entity) {
        this.entity = entity;
        this.type = entity.getEntityParticles().getType();
        this.numberOfParticles = entity.getEntityParticles().getNrOfParticles();
    }

    /**
	 * The redo command for the particle creation.
	 */
    public void redoCommand() {
        entity.getEntityParticles().createParticles(type, numberOfParticles);
    }

    /**
	 * Undo for the creation of the particle for some entities.
	 */
    public void undoCommand() {
        entity.getEntityNode().detachChild(entity.getEntityParticles().getParticleManager().getParticles());
        entity.getEntityNode().updateRenderState();
        entity.getEntityNode().updateGeometricState(0, true);
        entity.getEntityParticles().setParticleManager(null);
    }
}

package net.sourceforge.iwii.db.dev.logic.fascade.api;

import java.util.List;
import net.sourceforge.iwii.db.dev.bo.project.artifact.ProjectArtifactDataBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase6.RealWorldObjectBO;
import net.sourceforge.iwii.db.dev.common.exceptions.ValidationException;

/**
 * Interface represents artifact managment fascade.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public interface IArtifactManagmentFascade {

    public void performArtifactAccept(ProjectArtifactDataBO artifactData, boolean accepted) throws ValidationException;

    public List<RealWorldObjectBO> getAllRealWorldObjects();
}

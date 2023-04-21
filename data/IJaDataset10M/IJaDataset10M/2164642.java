package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.Date;
import java.util.List;
import nl.tranquilizedquality.adm.commons.domain.InsertableDomainObject;

/**
 * Representation of release history so you can see what happened during the
 * deployment of a release.
 * 
 * @author Salomo Petrus (salomo.petrus@gmail.com)
 * @since 3 jun. 2011
 */
public interface ReleaseExecution extends InsertableDomainObject<Long> {

    /**
	 * Retrieves the {@link Release} where this history is from.
	 * 
	 * @return Returns a {@link Release}.
	 */
    Release getRelease();

    /**
	 * Retrieves the status of the release.
	 * 
	 * @return Returns {@link DeployStatus}.
	 */
    DeployStatus getReleaseStatus();

    /**
	 * Retrieves the release date.
	 * 
	 * @return Returns a {@link Date}.
	 */
    Date getReleaseDate();

    /**
	 * Retrieves the steps that were executed during the deployment of the
	 * release.
	 * 
	 * @return Returns a {@link List} containing {@link ReleaseStepExecution}
	 *         objects.
	 */
    List<ReleaseStepExecution> getStepExecutions();

    /**
	 * Sets the status of the release.
	 * 
	 * @param releaseStatus
	 *            The status that will be set.
	 */
    void setReleaseStatus(DeployStatus releaseStatus);

    /**
	 * Sets the release where history will be registered for.
	 * 
	 * @param release
	 *            The release that will be set.
	 */
    void setRelease(Release release);

    /**
	 * Retrieves the artifacts that are part of this release execution.
	 * 
	 * @return Returns a {@link List} containing {@link MavenArtifactSnapshot}
	 *         objects.
	 */
    List<MavenArtifactSnapshot> getArtifacts();

    /**
	 * Sets the artifacts of this execution.
	 * 
	 * @param artifacts
	 *            The artifacts that will be set.
	 */
    void setArtifacts(List<MavenArtifactSnapshot> artifacts);

    /**
	 * Sets the logs for this release execution.
	 * 
	 * @param logs
	 *            The logs that will be set.
	 */
    void setLogs(String logs);

    /**
	 * Retrieves the logs of this release execution.
	 * 
	 * @return Returns a {@link String} containing the logs of this execution.
	 */
    String getLogs();
}

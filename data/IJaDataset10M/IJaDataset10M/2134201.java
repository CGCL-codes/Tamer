package by.brsu.portal.project;

import java.util.Date;
import java.util.List;
import by.brsu.portal.cv.ProgrammingLanguage;
import by.brsu.portal.cv.Technology;

/**
 * @author Artur Smaliuk
 *
 */
public interface IProjectDAO {

    /**
	 * add Project
	 *
	 */
    public Project createProject(int idOwner, String name, String description, Date dateOfCreation, Date dateOfClosing, ProjectCategory category, int version, String license, String stageOfDevelopment, List<Technology> technology, List<ProgrammingLanguage> languages);

    /**
	 * Delete Project
	 */
    public void deleteProject(String name);

    /**
	 * Find Project by id
	 * 
	 */
    public Project findProjectById(long idProject);

    /**
	 * Find Project by idOwner
	 */
    public Project findProjectByidOwner(int idOwner);

    /**
	 * Find Project by name
	 */
    public Project findProjectByName(String name);

    /**
	 * Find Project by Description
	 */
    public Project findProjectByDescription(String Description);

    /**
	 * Find Project by DateOfCreation
	 */
    public Project findProjectByDateOfCreation(Date DateOfCreation);

    /**
	 * Find Project by DateOfClosing
	 */
    public Project findProjectByDateOfClosing(Date DateOfClosing);

    /**
	 * Find Project by Version
	 */
    public Project findProjectByVersion(int Version);

    /**
	 * Find Project by License
	 */
    public Project findProjectByLicense(String License);

    /**
	 * Find Project by StageOfDevelopment
	 */
    public Project findProjectByStageOfDevelopment(String StageOfDevelopment);

    /**
	 * Find all Projects
	 */
    public List<Project> findAllProjectss();
}

package dao;

import java.util.List;
import domain.MigrationException;
import domain.MigrationProject;
import domain.OriginalTask;
import domain.QuoteTask;

public interface MigrationDao {

    /**
	 * Get all projects
	 */
    public List<MigrationProject> listAllMigrationProjects() throws MigrationException;

    public MigrationProject getProjectbyUid(String projectuid) throws MigrationException;

    public void storeMigrationProjectQandITasks(MigrationProject migrationProject) throws MigrationException;

    public List<OriginalTask> listMigrationProjectOriginalTasks(MigrationProject migrationProject) throws MigrationException;

    public void removeOriginalTasksFromMigrationProject(MigrationProject migrationProject) throws MigrationException;

    public List<QuoteTask> listProjectQTasks(MigrationProject migrationProject) throws MigrationException;
}

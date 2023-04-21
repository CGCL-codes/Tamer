package persister;

import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;

public interface PlannerDataChangeListener extends EventListener {

    /***************************************************************************
     * CREATE *
     **************************************************************************/
    public void createdBacklog(Backlog backlog);

    public void createdIteration(Iteration iteration);

    public void createdProjectOnInitialLoadFromServer(Project project);

    public void createdStoryCard(StoryCard storycard);

    public void createdOwner(TeamMember teamMember);

    /***************************************************************************
     * DELETE *
     **************************************************************************/
    public void deletedIteration(long id);

    public void deletedStoryCard(long id);

    public void deletedOwner(TeamMember teamMember);

    /***************************************************************************
     * UNDELETE *
     **************************************************************************/
    public void undeletedIteration(Iteration iteration);

    public void undeletedStoryCard(StoryCard storycard);

    /***************************************************************************
     * MOVE STORYCARD BETWEEN PARENTS *
     **************************************************************************/
    public void movedStoryCardToNewParent(StoryCard storycard);

    /***************************************************************************
     * UPDATE BACKLOG *
     **************************************************************************/
    public void updatedBacklog(Backlog backlog);

    public void updatedLegend(Legend leg);

    public void updatedOwner(TeamMember teamMember);

    /***************************************************************************
     * UPDATE ITERATION *
     **************************************************************************/
    public void updatedIteration(Iteration iteration);

    /***************************************************************************
     * UPDATE STORYCARD *
     **************************************************************************/
    public void updatedStoryCard(StoryCard storycard);

    /***************************************************************************
     * UPDATE PROJECT *
     **************************************************************************/
    public void gotProjectNames(List<String> str);

    /***************************************************************************
     * UPLOAD DOWNLOAD FILE *
     **************************************************************************/
    public void uploadedFile(boolean bool);

    public void downloadedFile(boolean bool);

    /***************************************************************************
     * Asynchronous Exceptions *
     **************************************************************************/
    public void asynchronousException(Exception exception, int errorMethod);

    /**
     * ArrangeProject
     */
    public void arrangeProject(Project project);

    public void createProjectOnSubsequentLoadsFromServer(Project project);

    public void gotProjectNamesForLoginEvent(List<String> list);

    public void lostConnectionEvent();

    /**
	 * Populates the drop down menu with
	 * Jazz Project Areas and their respective Team Areas.
	 */
    public void gotJazzProjectList(HashMap<String, String[]> jazzProjectList);
}

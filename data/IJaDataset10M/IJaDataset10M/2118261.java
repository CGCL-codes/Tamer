package edu.harvard.fas.rregan.requel.project.impl.command;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import edu.harvard.fas.rregan.command.CommandHandler;
import edu.harvard.fas.rregan.requel.annotation.Annotation;
import edu.harvard.fas.rregan.requel.annotation.command.AnnotationCommandFactory;
import edu.harvard.fas.rregan.requel.annotation.command.RemoveAnnotationFromAnnotatableCommand;
import edu.harvard.fas.rregan.requel.project.Actor;
import edu.harvard.fas.rregan.requel.project.GlossaryTerm;
import edu.harvard.fas.rregan.requel.project.Goal;
import edu.harvard.fas.rregan.requel.project.ProjectRepository;
import edu.harvard.fas.rregan.requel.project.Story;
import edu.harvard.fas.rregan.requel.project.StoryContainer;
import edu.harvard.fas.rregan.requel.project.command.DeleteStoryCommand;
import edu.harvard.fas.rregan.requel.project.command.ProjectCommandFactory;
import edu.harvard.fas.rregan.requel.project.command.RemoveActorFromActorContainerCommand;
import edu.harvard.fas.rregan.requel.project.command.RemoveGoalFromGoalContainerCommand;
import edu.harvard.fas.rregan.requel.project.command.RemoveStoryFromStoryContainerCommand;
import edu.harvard.fas.rregan.requel.project.impl.assistant.AssistantFacade;
import edu.harvard.fas.rregan.requel.user.User;
import edu.harvard.fas.rregan.requel.user.UserRepository;

/**
 * Delete a story from a project, cleaning up references from other project
 * entities, story relations and annotations.
 * 
 * @author ron
 */
@Controller("deleteStoryCommand")
@Scope("prototype")
public class DeleteStoryCommandImpl extends AbstractEditProjectCommand implements DeleteStoryCommand {

    private Story story;

    /**
	 * @param assistantManager
	 * @param userRepository
	 * @param projectRepository
	 * @param projectCommandFactory
	 * @param annotationCommandFactory
	 * @param commandHandler
	 */
    @Autowired
    public DeleteStoryCommandImpl(AssistantFacade assistantManager, UserRepository userRepository, ProjectRepository projectRepository, ProjectCommandFactory projectCommandFactory, AnnotationCommandFactory annotationCommandFactory, CommandHandler commandHandler) {
        super(assistantManager, userRepository, projectRepository, projectCommandFactory, annotationCommandFactory, commandHandler);
    }

    @Override
    public void setStory(Story story) {
        this.story = story;
    }

    protected Story getStory() {
        return story;
    }

    @Override
    public void execute() throws Exception {
        Story story = getRepository().get(getStory());
        User editedBy = getRepository().get(getEditedBy());
        Set<Annotation> annotations = new HashSet<Annotation>(story.getAnnotations());
        for (Annotation annotation : annotations) {
            RemoveAnnotationFromAnnotatableCommand removeAnnotationFromAnnotatableCommand = getAnnotationCommandFactory().newRemoveAnnotationFromAnnotatableCommand();
            removeAnnotationFromAnnotatableCommand.setEditedBy(editedBy);
            removeAnnotationFromAnnotatableCommand.setAnnotatable(story);
            removeAnnotationFromAnnotatableCommand.setAnnotation(annotation);
            getCommandHandler().execute(removeAnnotationFromAnnotatableCommand);
        }
        for (GlossaryTerm term : story.getProjectOrDomain().getGlossaryTerms()) {
            if (term.getReferers().contains(story)) {
                term.getReferers().remove(story);
            }
        }
        for (Actor actor : story.getActors()) {
            RemoveActorFromActorContainerCommand removeActorFromActorContainerCommand = getProjectCommandFactory().newRemoveActorFromActorContainerCommand();
            removeActorFromActorContainerCommand.setEditedBy(editedBy);
            removeActorFromActorContainerCommand.setActor(actor);
            removeActorFromActorContainerCommand.setActorContainer(story);
            getCommandHandler().execute(removeActorFromActorContainerCommand);
        }
        for (Goal goal : story.getGoals()) {
            RemoveGoalFromGoalContainerCommand removeGoalFromGoalContainerCommand = getProjectCommandFactory().newRemoveGoalFromGoalContainerCommand();
            removeGoalFromGoalContainerCommand.setEditedBy(editedBy);
            removeGoalFromGoalContainerCommand.setGoal(goal);
            removeGoalFromGoalContainerCommand.setGoalContainer(story);
            getCommandHandler().execute(removeGoalFromGoalContainerCommand);
        }
        Set<StoryContainer> storyReferers = new HashSet<StoryContainer>(story.getReferers());
        for (StoryContainer storyContainer : storyReferers) {
            RemoveStoryFromStoryContainerCommand removeStoryFromStoryContainerCommand = getProjectCommandFactory().newRemoveStoryFromStoryContainerCommand();
            removeStoryFromStoryContainerCommand.setEditedBy(editedBy);
            removeStoryFromStoryContainerCommand.setStory(story);
            removeStoryFromStoryContainerCommand.setStoryContainer(storyContainer);
            getCommandHandler().execute(removeStoryFromStoryContainerCommand);
        }
        story.getProjectOrDomain().getStories().remove(story);
        getRepository().delete(story);
    }
}

package cards.commands;

import org.eclipse.gef.commands.Command;
import persister.IndexCard;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;
import cards.model.StoryCardModel;

public class StoryCardSetStatusCommand extends Command {

    private StoryCardModel storyCardModel = null;

    private String newStatus = IndexCard.STATUS_DEFINED;

    private String oldStatus = IndexCard.STATUS_DEFINED;

    public StoryCardModel getStoryCardModel() {
        return this.storyCardModel;
    }

    public StoryCardSetStatusCommand(StoryCardModel storyCard, String status) {
        this.storyCardModel = storyCard;
        newStatus = status;
        oldStatus = storyCardModel.getStatus();
        this.setLabel("Set State of Story Card");
    }

    @Override
    public void execute() {
        this.storyCardModel.setStatus(newStatus);
        try {
            storyCardModel.getStoryCard().setStatus(newStatus);
            PersisterFactory.getPersister().updateStoryCard(storyCardModel.getStoryCard());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public boolean canExecute() {
        return (oldStatus != newStatus);
    }

    @Override
    public boolean canUndo() {
        return (oldStatus != newStatus);
    }

    @Override
    public void undo() {
        try {
            storyCardModel.setStatus(oldStatus);
            storyCardModel.getStoryCard().setStatus(oldStatus);
            PersisterFactory.getPersister().updateStoryCard(storyCardModel.getStoryCard());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
        }
    }
}

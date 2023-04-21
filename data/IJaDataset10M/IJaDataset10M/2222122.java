package cards.commands;

import java.util.ArrayList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.StoryCard;
import persister.factory.PersisterFactory;
import cards.model.IterationCardModel;
import cards.model.StoryCardModel;

public class UpdateIterationSizeLocationCommand extends Command {

    private Rectangle newBounds;

    private Rectangle oldBounds;

    @SuppressWarnings("unused")
    private final ChangeBoundsRequest request;

    private IterationCardModel card;

    private ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();

    public UpdateIterationSizeLocationCommand(IterationCardModel card, ChangeBoundsRequest request, Rectangle newBounds) {
        if ((card == null) || (request == null) || (newBounds == null)) {
            throw new IllegalArgumentException();
        }
        this.card = card;
        this.request = request;
        this.newBounds = newBounds;
        this.setLabel("Move");
        this.oldBounds = new Rectangle(card.getLocation(), card.getSize());
        this.storyCards.addAll(card.getAllChildren());
    }

    @Override
    public void execute() {
        this.redo();
    }

    @Override
    public void redo() {
        updateIteration();
    }

    @Override
    public void undo() {
        undoUpdateIteration();
    }

    private void updateIteration() {
        try {
            this.card.getIterationDataObject().setWidth(this.newBounds.getSize().width);
            this.card.getIterationDataObject().setHeight(this.newBounds.getSize().height);
            if (this.newBounds.x < 0) this.newBounds.setLocation(0, this.newBounds.y);
            if (this.newBounds.y < 0) this.newBounds.setLocation(this.newBounds.x, 0);
            this.card.getIterationDataObject().setLocationX(this.newBounds.getLocation().x);
            this.card.getIterationDataObject().setLocationY(this.newBounds.getLocation().y);
            for (StoryCard sc : this.card.getIterationDataObject().getStoryCardChildren()) {
                sc.setLocationX(sc.getLocationX() + this.newBounds.getLocation().x - this.oldBounds.getLocation().x);
                sc.setLocationY(sc.getLocationY() + this.newBounds.getLocation().y - this.oldBounds.getLocation().y);
            }
            PersisterFactory.getPersister().updateIteration(this.card.getIterationDataObject());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void undoUpdateIteration() {
        try {
            this.card.getIterationDataObject().setWidth(this.oldBounds.getSize().width);
            this.card.getIterationDataObject().setHeight(this.oldBounds.getSize().height);
            this.card.getIterationDataObject().setLocationX(this.oldBounds.getLocation().x);
            this.card.getIterationDataObject().setLocationY(this.oldBounds.getLocation().y);
            for (StoryCard sc : this.card.getIterationDataObject().getStoryCardChildren()) {
                sc.setLocationX(sc.getLocationX() + this.oldBounds.getLocation().x - this.newBounds.getLocation().x);
                sc.setLocationY(sc.getLocationY() + this.oldBounds.getLocation().y - this.newBounds.getLocation().y);
            }
            PersisterFactory.getPersister().updateIteration(this.card.getIterationDataObject());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
        }
    }
}

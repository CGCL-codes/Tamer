package fitintegration;

import persister.distributed.ServerCommunicator;
import cards.editpart.StoryCardEditPart;
import cards.figure.StoryCardFigure;

public class AgilePlannerFactory implements FactoryInterface {

    public ServerCommunicator getServerCommunicator() {
        return null;
    }

    public StoryCardEditPart getNewStoryCardEditPart() {
        return new StoryCardEditPart();
    }

    public StoryCardFigure getNewStoryCardFigure() {
        return new StoryCardFigure();
    }
}

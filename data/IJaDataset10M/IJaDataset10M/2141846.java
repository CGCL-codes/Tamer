package othello.client.service;

import java.util.List;
import othello.shared.model.GameStep;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OthelloServiceAsync {

    void getStepsFrom(String gameBoardId, int startStep, AsyncCallback<List<GameStep>> callback);

    void entryNextStep(String gameBoardId, GameStep nextStep, AsyncCallback<Void> callback);
}

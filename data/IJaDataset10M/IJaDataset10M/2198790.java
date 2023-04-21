package dk.mirasola.systemtraining.biddingsession.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dk.mirasola.systemtraining.biddingsession.shared.transfer.BidBoardTO;
import dk.mirasola.systemtraining.biddingsession.shared.transfer.BidInitDataTO;
import dk.mirasola.systemtraining.biddingsession.shared.transfer.FinishBoardTO;
import dk.mirasola.systemtraining.biddingsession.shared.transfer.FinishInitDataTO;
import dk.mirasola.systemtraining.biddingsession.shared.transfer.MakeBidResultTO;
import dk.mirasola.systemtraining.biddingsession.shared.transfer.StartSessionTO;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Bid;

public interface SessionServiceAsync {

    void startSession(String sessionId, AsyncCallback<StartSessionTO> async);

    void getBidInitData(String biddingSetId, AsyncCallback<BidInitDataTO> async);

    void getFinishInitData(String biddingSetId, AsyncCallback<FinishInitDataTO> async);

    void makeBid(String boardId, Bid bid, AsyncCallback<MakeBidResultTO> async);

    void getBidBoard(String boardId, AsyncCallback<BidBoardTO> async);

    void getFinishBoard(String boardId, AsyncCallback<FinishBoardTO> async);

    void addComment(String boardId, String comment, AsyncCallback<Void> async);

    void undoBid(String boardId, AsyncCallback<BidBoardTO> async);
}

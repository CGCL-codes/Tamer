package dk.mirasola.systemtraining.user.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dk.mirasola.systemtraining.user.shared.transfer.BiddingSetDetailsTO;
import dk.mirasola.systemtraining.user.shared.transfer.CreateBiddingSetTO;
import java.util.List;
import java.util.Set;

public interface BiddingSetDataProviderServiceAsync {

    void getBiddingSetDetails(int offset, int limit, AsyncCallback<List<BiddingSetDetailsTO>> async);

    void initialData(AsyncCallback<int[]> async);

    void createBiddingSetAndFirstBiddingSession(CreateBiddingSetTO createBiddingSetTO, AsyncCallback<Void> async);

    void deleteBiddingSets(Set<String> biddingSetIds, AsyncCallback<Void> async);
}

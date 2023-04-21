package au.org.tpac.portal.gwt.client.service;

import java.util.List;
import au.org.tpac.portal.gwt.client.data.RemoteDatasetInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DownloaderService extends RemoteService {

    Integer launchDownloader(List<RemoteDatasetInfo> remoteDatasets);

    Integer cancelDownlaoder();

    Integer getPercentageComplete();

    Boolean getDownloaderActiveStatus();
}

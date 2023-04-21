package tesla.app.mediainfo;

import java.util.ArrayList;
import java.util.Iterator;
import tesla.app.mediainfo.MediaInfo.MediaFormat;
import tesla.app.mediainfo.helper.CacheStoreHelper;
import tesla.app.mediainfo.helper.FailedQueryBlacklist;
import tesla.app.mediainfo.provider.CacheProvider;
import tesla.app.mediainfo.provider.IMediaInfoProvider;
import tesla.app.mediainfo.provider.LastfmProvider;

public class MediaInfoFactory {

    ArrayList<IMediaInfoProvider> providerScanner = new ArrayList<IMediaInfoProvider>();

    public MediaInfoFactory() {
        IMediaInfoProvider cacheProvider = new CacheProvider();
        IMediaInfoProvider lastfmProvider = new LastfmProvider();
        providerScanner.add(cacheProvider);
        providerScanner.add(lastfmProvider);
    }

    public MediaInfo process(MediaInfo info) {
        boolean success = false;
        Iterator<IMediaInfoProvider> providerOrderIt = providerScanner.iterator();
        while (!success && providerOrderIt.hasNext()) {
            IMediaInfoProvider currentProvider = providerOrderIt.next();
            MediaFormat providerFormat = currentProvider.getProviderFormat();
            if (providerFormat.equals(info.format) || providerFormat.equals(MediaFormat.BOTH) || info.format.equals(MediaFormat.BOTH)) {
                try {
                    success = currentProvider.populate(info);
                } catch (Exception e) {
                    e.printStackTrace();
                    success = false;
                }
            }
        }
        if (success == false) {
            FailedQueryBlacklist.getInstance().blacklist.add(new CacheStoreHelper().getArtworkPath(info));
        }
        return info;
    }
}

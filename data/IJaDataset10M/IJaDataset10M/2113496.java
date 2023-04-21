package neembuu.directuploader;

import neembuu.common.TimeInterval;

/**
 *
 * @author Shashank Tulsyan
 */
public interface UploadedContentLifeSpan {

    public TimeInterval lifeSpanJustAfterUploading();

    public TimeInterval lifeSpanAfterLastDownloadIfNotDownloadedFor(TimeInterval timeInterval);

    public TimeInterval lifeSpanAfterLastLoginIfNotLoggedInFor(TimeInterval timeInterval);

    public TimeInterval expectedLifeSpanIfContentViolatesTermsOfService(String description);

    public TimeInterval maximumTimeIntervalBetweenLoginsToContinueKeepingContentAlive();

    public TimeInterval maximumTimeIntervalBetweenDownloadsToContinueKeepingContentAlive();

    public TimeInterval lifeGainedIfLoggedInAfter(TimeInterval interval);

    public TimeInterval lifeGainedIfDownloadedAfter(TimeInterval interval);

    public String getDescription();

    public long minimumBytesToDownloadForIncreasingLifeSpan(UploadableContent uploadOperation);

    public boolean loggingInIncreasesLifeSpan();

    public boolean downloadingIncreasesLifeSpan();
}

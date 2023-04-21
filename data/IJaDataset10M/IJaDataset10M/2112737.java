package com.showdown.resource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;
import com.showdown.Main.ShowDownFlags;
import com.showdown.api.impl.ShowDownManager;
import com.showdown.log.ShowDownLog;
import com.showdown.util.FileUtil;

/**
 * Class which holds all of the messages displayed by ShowDown.
 * @author Mat DeLong
 */
public final class Messages {

    public static String AppFailTitle;

    public static String AppFailMessage;

    public static String UnknownAppFail;

    public static String LogPortTaken;

    public static String OkButton;

    public static String CloseButton;

    public static String ImportDialogTitle;

    public static String ExportDialogTitle;

    public static String ImportShowsButton;

    public static String IncludeTheseShows;

    public static String Cartoons;

    public static String EndedShows;

    public static String Run;

    public static String NoNewShows;

    public static String PopulatingNewShow;

    public static String ImportButton;

    public static String ExportButton;

    public static String GeneralSettingsButton;

    public static String FavoriteShowsButton;

    public static String EpDatesButton;

    public static String MergeOnImport;

    public static String DuplicateShowIdOnImport;

    public static String ImportShowFindFail;

    public static String WarningSearchHackBan;

    public static String NotifyShowEndedUpdate;

    public static String NotifyShowPopulationForLetter;

    public static String DestructiveAction;

    public static String RefreshShows;

    public static String SkipCurrentEp;

    public static String SkipToNextEp;

    public static String DownloadBestTorrent;

    public static String EditShowSettings;

    public static String EditShowProperties;

    public static String RemoveShow;

    public static String PauseShow;

    public static String PauseShows;

    public static String UnpauseShow;

    public static String UnpauseShows;

    public static String NoScheduledFor;

    public static String NoScheuledDialogTitle;

    public static String SelectionNoSchedule;

    public static String SelectionSomeNoSchedule;

    public static String DownloadBestFailedDialogMessage;

    public static String ConfirmRemoveSingleShow;

    public static String ConfirmRemoveMultipleShows;

    public static String SearchToAdd;

    public static String Season;

    public static String Episode;

    public static String Seeds;

    public static String Leeches;

    public static String SizeInMB;

    public static String GoodComments;

    public static String ErrorLoadingTorrentFile;

    public static String IncludedTypes;

    public static String AirDate;

    public static String LoadingTorrents;

    public static String NoTorrents;

    public static String DownloadButton;

    public static String DownloadSelectedTorrent;

    public static String SearchLinksButton;

    public static String SearchLinksView;

    public static String SearchLinksDialogTitle;

    public static String BackToPrevious;

    public static String TorrentsFor;

    public static String LoadingEpisodes;

    public static String NoScheduledEpisodes;

    public static String ChooseSelected;

    public static String SelectedEpToDownload;

    public static String ViewTorrents;

    public static String ViewTorrentsMessage;

    public static String BackToYourShows;

    public static String EpisodesFor;

    public static String UpdatingMessage;

    public static String SeasonNumber;

    public static String UseDefault;

    public static String UseDefaultFromSettingsPage;

    public static String EndedExplained;

    public static String MinFileSize;

    public static String MaxFileSize;

    public static String ShowName;

    public static String AddShow;

    public static String EditSelectedShow;

    public static String EditShow;

    public static String TVIdError;

    public static String Hide;

    public static String Show;

    public static String Exit;

    public static String DailyShow;

    public static String EndedShow;

    public static String CartoonShow;

    public static String AddFirst;

    public static String AddFirstMessage;

    public static String AddLast;

    public static String AddLastMessage;

    public static String AddNext;

    public static String AddNextMessage;

    public static String FilterByShowName;

    public static String HideEnded;

    public static String ShowEnded;

    public static String NoShowMatch;

    public static String UpdateAllShowsList;

    public static String UpdateEnded;

    public static String FindNewShows;

    public static String ValidateShowList;

    public static String InvalidShowList;

    public static String InvalidShowListMsg;

    public static String ValidShowListMsg;

    public static String UpdateEndedDialogTitle;

    public static String ImportNewShowsDialogTitle;

    public static String LoadingShowInfo;

    public static String NoShowInfo;

    public static String BackButton;

    public static String BackToAllShows;

    public static String NotScheduledToAir;

    public static String FeedNameTitle;

    public static String RequiredField;

    public static String UniqueFeedName;

    public static String UrlPropertiesRequired;

    public static String TorrentURL;

    public static String TorrentURLMessage;

    public static String FeedURL;

    public static String FeedURLMessage;

    public static String SeedProperties;

    public static String SeedsRSSElement;

    public static String SeedsRSSElementMessage;

    public static String SeedsRegex;

    public static String SeedsRegexMessage;

    public static String RegexGroupNumber;

    public static String SeedsRegexGroup;

    public static String LeechProperties;

    public static String LeechesElement;

    public static String LeechesElementMessage;

    public static String LeechesRegex;

    public static String LeechesRegexMessage;

    public static String LeechesRegexGroup;

    public static String SizeProperties;

    public static String SizeElement;

    public static String SizeElementMessage;

    public static String SizeRegex;

    public static String SizeRegexMessage;

    public static String SizeRegexGroup;

    public static String SizeUnits;

    public static String SizeUnitsMessage;

    public static String PositiveComments;

    public static String GoodCommentsRegex;

    public static String GoodCommentsRegexMessage;

    public static String GoodCommentRegexGroup;

    public static String BadComments;

    public static String BadCommentsRegex;

    public static String BadCommentsRegexMessage;

    public static String BadCommentsRegexGroup;

    public static String AutoResolve;

    public static String AutoResolveToolTip;

    public static String AddToMyShows;

    public static String AddToMyShowsTooltip;

    public static String CancelAddShowTooltip;

    public static String ApplyAddShowTooltip;

    public static String ApplicationSettings;

    public static String TorrentDownloadDirectory;

    public static String Browse;

    public static String UseSeasonSubDirs;

    public static String UseSeasonSubDirsTooltip;

    public static String TimeoutLength;

    public static String TimeoutLengthTooltip;

    public static String UpdateInterval;

    public static String UpdateIntervalTooltip;

    public static String SkipDownloads;

    public static String SkipDownloadsTooltip;

    public static String AutoDownloadTorrents;

    public static String AutoDownloadTorrentsTooltip;

    public static String AutoRunTorrent;

    public static String AutoRunTorrentTooltip;

    public static String DeleteTorrentAfterRun;

    public static String DeleteTorrentAfterRunTooltip;

    public static String UseSystemTray;

    public static String DefaultShowSettings;

    public static String ShowSettings;

    public static String MinimumSize;

    public static String MaximumSize;

    public static String MinimumSeeds;

    public static String MinimumGoodComments;

    public static String MinimumGoodCommentsTooltip;

    public static String ExcludedExtensions;

    public static String ExcludedExtensionsTooltips;

    public static String DownloadOnlySingleFile;

    public static String TorrentTitleIncludes;

    public static String TorrentTitleIncludesTooltip;

    public static String TorrentTitleExcludes;

    public static String TorrentTitleExcludesTooltip;

    public static String UseDefaultSettings;

    public static String Logging;

    public static String ImportExportGroup;

    public static String ImportData;

    public static String ExportData;

    public static String ImportDataTooltip;

    public static String ExportDataTooltip;

    public static String Info;

    public static String InfoMesage;

    public static String Warning;

    public static String WarningMessage;

    public static String Error;

    public static String ErrorMessage;

    public static String Debug;

    public static String DebugMessage;

    public static String DefaultFeedList;

    public static String ShowFeeds;

    public static String DefaultEpParsers;

    public static String ShowEpParsers;

    public static String UseDefaultFeeds;

    public static String UseDefaultEpisodeParsers;

    public static String AddNewFeed;

    public static String EditSelectedFeed;

    public static String DeleteSelectedFeed;

    public static String Cancel;

    public static String CancelSettings;

    public static String Apply;

    public static String ApplySettings;

    public static String GeneralSettings;

    public static String SettingsForShow;

    public static String SettingsForShows;

    public static String ConfirmDelete;

    public static String ConfirmDeleteMessage;

    public static String UpdateGroupTitle;

    public static String UpdateSite;

    public static String CheckUpdates;

    public static String DoUpdate;

    public static String CheckingForUpdates;

    public static String NoUpdatesFound;

    public static String InvalidUpdateSite;

    public static String UpdateDialogTitle;

    public static String DownloadFile;

    public static String UpdateDownloadSuccess;

    public static String UpdateDownloadFailure;

    public static String TabFilesToDelete;

    public static String TabFilesToReplace;

    public static String TabShows;

    public static String TabParsers;

    public static String TabFees;

    public static String UpdateTabMessageOverwrite;

    public static String UpdateTabMessageDelete;

    public static String UpdateTabMessageParsers;

    public static String UpdateTabMessageShows;

    public static String UpdateTabMessageFeeds;

    public static String UpdateMerge;

    public static String UpdateOptionYours;

    public static String UpdateOptionTheirs;

    public static String FailedToDownload;

    public static String EpisodeRefresh;

    public static String EpisodeRefreshTooltip;

    public static String SelectAll;

    public static String SelectNone;

    static {
        Properties defaultProperties = FileUtil.getDefaultLanguageProperties();
        Properties properties = FileUtil.getLanguageProperties();
        List<String> sdFlags = ShowDownManager.INSTANCE.getSDParams().getFlags();
        boolean dontFallback = sdFlags.contains(ShowDownFlags.NO_NL_FALLBACK.getFlag());
        Class<?> c = Messages.class;
        for (Field f : c.getFields()) {
            String name = f.getName();
            try {
                String value = properties.getProperty(name);
                if (value == null) {
                    if (!dontFallback) {
                        value = defaultProperties.getProperty(name);
                    }
                    if (value == null) {
                        value = "**[" + name + " MISSING]**";
                    }
                }
                f.set(null, value);
            } catch (Exception ex) {
                ShowDownLog.getInstance().logError(ex.getLocalizedMessage(), ex);
            }
        }
    }
}

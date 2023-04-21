package phex.prefs;

public class UploadPrefs extends PhexCorePrefs {

    public static final Setting<Integer> MaxParallelUploads;

    public static final Setting<Integer> MaxUploadsPerIP;

    public static final Setting<Boolean> AutoRemoveCompleted;

    /**
     * Indicates whether partial downloaded files are offered to others for download.
     */
    public static final Setting<Boolean> SharePartialFiles;

    /**
     * Indicates whether upload queuing is allowed or not.
     */
    public static final Setting<Boolean> AllowQueuing;

    /**
     * The maximal number of upload queue slots available.
     */
    public static final Setting<Integer> MaxQueueSize;

    /**
     * The minimum poll time for queued uploads.
     */
    public static final Setting<Integer> MinQueuePollTime;

    /**
     * The maximum poll time for queued uploads.
     */
    public static final Setting<Integer> MaxQueuePollTime;

    /**
     * The LogBuffer size used for upload state.
     */
    public static final Setting<Integer> UploadStateLogBufferSize;

    static {
        MaxParallelUploads = PreferencesFactory.createIntRangeSetting("Upload.MaxParallelUploads", 4, 1, 99, instance);
        MaxUploadsPerIP = PreferencesFactory.createIntRangeSetting("Upload.MaxUploadsPerIP", 1, 1, 99, instance);
        AutoRemoveCompleted = PreferencesFactory.createBoolSetting("Upload.AutoRemoveCompleted", false, instance);
        SharePartialFiles = PreferencesFactory.createBoolSetting("Upload.SharePartialFiles", true, instance);
        AllowQueuing = PreferencesFactory.createBoolSetting("Upload.AllowQueuing", true, instance);
        MaxQueueSize = PreferencesFactory.createIntRangeSetting("Upload.MaxQueueSize", 10, 1, 99, instance);
        MinQueuePollTime = PreferencesFactory.createIntRangeSetting("Upload.MinQueuePollTime", 45, 30, 120, instance);
        MaxQueuePollTime = PreferencesFactory.createIntRangeSetting("Upload.MaxQueuePollTime", 120, 90, 180, instance);
        UploadStateLogBufferSize = PreferencesFactory.createIntSetting("Upload.UploadStateLogBufferSize", 0, instance);
    }
}

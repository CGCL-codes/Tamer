package spindles.api.util;

public class ErrorMessages {

    public static final String FILENAME_INVALID = "File name is not valid";

    public static final String FILE_IMPORTED = "This file has already been imported";

    public static final String FILE_UNEXPECTED_FORMAT = "The file is not in the expected format";

    public static final String CHANNEL_NOT_FOUND = "Cannot find specified channel";

    public static final String INVALID_SAMPLING_RATE = "The sampling rate is invalid";

    public static final String MAX_PLOTS_EXCEEDED = "The maximum number of plots per page was exceeded";

    public static final String DATE_UNEXPECTED_FORMAT = "The date is not in the expected format";

    public static final String OUT_OF_LIMITS = "The value should be between %d and %d";

    public static final String DUPLICATE_THRESHOLDGROUP = "Threshold group is already used for spindle detection in this epoch.";

    public static final String TIME_UNEXPECTED_FORMAT = "Time is not in the expected format";

    public ErrorMessages() {
    }
}

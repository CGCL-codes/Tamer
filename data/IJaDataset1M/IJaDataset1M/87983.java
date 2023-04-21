package jokeboxjunior.core.model;

/**
 *
 * @author B1
 */
public class MediaType {

    public enum MediaTypeEnum {

        UNKNOWN, AUDIO, VIDEO
    }

    public static MediaTypeEnum getMediaType(int thisIndex) {
        switch(thisIndex) {
            case 1:
                return MediaTypeEnum.AUDIO;
            case 2:
                return MediaTypeEnum.VIDEO;
            default:
                return MediaTypeEnum.UNKNOWN;
        }
    }

    public static MediaTypeEnum getMediaType(String thisStr) {
        if (thisStr.length() == 1) {
            switch(thisStr.charAt(0)) {
                case 'A':
                    return MediaTypeEnum.AUDIO;
                case 'V':
                    return MediaTypeEnum.VIDEO;
                default:
                    return MediaTypeEnum.UNKNOWN;
            }
        } else {
            return MediaTypeEnum.valueOf(thisStr);
        }
    }

    public static int getMediaTypeInt(MediaTypeEnum thisType) {
        return thisType.ordinal();
    }

    public static String getMediaTypeShort(MediaTypeEnum thisType) {
        switch(thisType) {
            case AUDIO:
                return "A";
            case VIDEO:
                return "V";
            default:
                return "";
        }
    }

    public static String getMediaTypeLong(MediaTypeEnum thisType) {
        switch(thisType) {
            case AUDIO:
                return "AUDIO";
            case VIDEO:
                return "VIDEO";
            default:
                return "";
        }
    }
}

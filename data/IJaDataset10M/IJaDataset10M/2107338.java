package net.sf.istcontract.analyserfrontend.client.clientservice;

import java.io.Serializable;

/**
 *
 * @author hodik
 */
public class ClauseStatus implements Serializable {

    public static final int FULFILLED = 20001;

    public static final int INACTIVE = 20010;

    public static final int ACTIVE = 20020;

    public static final int VIOLATED = 20030;

    public static final int UNKNOWN = 20040;

    public static String getText(int status) {
        String labelText;
        switch(status) {
            case ClauseStatus.FULFILLED:
                labelText = "FULFILLED";
                break;
            case ClauseStatus.INACTIVE:
                labelText = "INACTIVE";
                break;
            case ClauseStatus.ACTIVE:
                labelText = "ACTIVE";
                break;
            case ClauseStatus.VIOLATED:
                labelText = "VIOLATED";
                break;
            case ClauseStatus.UNKNOWN:
                labelText = "UNKNOWN";
                break;
            default:
                labelText = "unknown status";
                break;
        }
        return labelText;
    }

    public static String getImage(int status) {
        String image;
        switch(status) {
            case ClauseStatus.FULFILLED:
                image = "images/statusFulfilled.png";
                break;
            case ClauseStatus.INACTIVE:
                image = "images/statusInactive.png";
                break;
            case ClauseStatus.ACTIVE:
                image = "images/statusActive.png";
                break;
            case ClauseStatus.VIOLATED:
                image = "images/statusViolated.png";
                break;
            case ClauseStatus.UNKNOWN:
                image = "images/statusUnknown.png";
                break;
            default:
                image = "images/statusUnknown.png";
                break;
        }
        return image;
    }
}

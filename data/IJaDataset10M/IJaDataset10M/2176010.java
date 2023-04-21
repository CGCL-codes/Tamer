package phsperformance.util;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconCollection {

    private static final String IMAGE_REPOSITORY = "/image/";

    public static final ImageIcon EXECUTE_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "lightning.png"));

    public static final ImageIcon EDIT_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "pencil.png"));

    public static final ImageIcon DELETE_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "cross.png"));

    public static final ImageIcon APP_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "gear_small.png"));

    ;

    public static final ImageIcon EXIT_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "system-shutdown.png"));

    public static final ImageIcon WORKER_IC0N = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "wrench.png"));

    public static final ImageIcon SERVER_IC0N = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "computer.png"));

    private static final ImageIcon RETRIEVE_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "retrieve.png"));

    private static final ImageIcon RETRIEVE_IDS_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "retrieve_ids.png"));

    public static final ImageIcon CAMPAIGN_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "server_lightning.png"));

    public static ImageIcon SUCCESS_TASK_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "flag_green.png"));

    public static ImageIcon EXCEPTION_TASK_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "exclamation.png"));

    public static ImageIcon ROOT_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "package.png"));

    public static ImageIcon SESSION_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "user.png"));

    public static ImageIcon TASK_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "script.png"));

    public static ImageIcon CLOCK_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "clock.png"));

    public static ImageIcon PAUSE_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "control_pause.png"));

    public static ImageIcon PING_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "server_connect.png"));

    public static ImageIcon SUBMIT_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "submit.png"));

    public static ImageIcon UPDATE_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "update.png"));

    public static ImageIcon VISIBILITY_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "weather_sun.png"));

    public static ImageIcon OVERLAY_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "images.png"));

    public static ImageIcon STARTED_TASK_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "control_play.png"));

    public static ImageIcon FINISHED_TASK_ICON = new ImageIcon(IconCollection.class.getResource(IMAGE_REPOSITORY + "control_stop.png"));

    public static ImageIcon getIcon(String name) {
        ImageIcon icon = TASK_ICON;
        if (name != null) {
            if (name.equals("clock")) {
                icon = CLOCK_ICON;
            } else if (name.equals("ping")) {
                icon = PING_ICON;
            } else if (name.equals("pause")) {
                icon = PAUSE_ICON;
            } else if (name.equals("submit")) {
                icon = SUBMIT_ICON;
            } else if (name.equals("visibility")) {
                icon = VISIBILITY_ICON;
            } else if (name.equals("overlay")) {
                icon = OVERLAY_ICON;
            } else if (name.equals("update")) {
                icon = UPDATE_ICON;
            } else if (name.equals("retrieveIds")) {
                icon = RETRIEVE_IDS_ICON;
            } else if (name.equals("retrieve")) {
                icon = RETRIEVE_ICON;
            }
        }
        return icon;
    }
}

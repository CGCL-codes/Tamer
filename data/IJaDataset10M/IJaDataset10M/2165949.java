package samples.strawberry;

import com.blackberry.facebook.FacebookContext;
import com.blackberry.facebook.ui.FacebookScreen;
import com.blackberry.util.log.Logger;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

final class HomeScreen extends FacebookScreen {

    private static final String LABEL_TITLE = "StrawBerry";

    private static final String LABEL_UPDATE_STATUS = "Update Status";

    private static final String LABEL_RECENT_UPDATES = "Recent Updates";

    private static final String LABEL_UPLOAD_PHOTO = "Upload Photos";

    private static final String LABEL_FRIENDS_LIST = "Friends List";

    private static final String LABEL_POKE_FRIEND = "Poke a Friend";

    private static final String LABEL_WRITE_WALL = "Write on a Wall";

    private static final String LABEL_SEND_MESSAGE = "Send a Message";

    private ButtonField updateStatusButton;

    private ButtonField recentUpdatesButton;

    private ButtonField uploadPhotoButton;

    private ButtonField friendListButton;

    private ButtonField pokeButton;

    private ButtonField wallButton;

    private ButtonField sendMessageButton;

    protected Logger log = Logger.getLogger(getClass());

    /**
	 * Default constructor.
	 * 
	 */
    HomeScreen(FacebookContext pfbc) {
        super(pfbc);
        LabelField titleLabel = new LabelField(LABEL_TITLE, LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
        setTitle(titleLabel);
        VerticalFieldManager topManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL);
        add(topManager);
        updateStatusButton = new ButtonField(LABEL_UPDATE_STATUS) {

            protected boolean invokeAction(int action) {
                fireAction(UpdateStatusScreen.ACTION_ENTER);
                return true;
            }
        };
        topManager.add(updateStatusButton);
        recentUpdatesButton = new ButtonField(LABEL_RECENT_UPDATES) {

            protected boolean invokeAction(int action) {
                fireAction(RecentUpdatesScreen.ACTION_ENTER);
                return true;
            }
        };
        topManager.add(recentUpdatesButton);
        uploadPhotoButton = new ButtonField(LABEL_UPLOAD_PHOTO) {

            protected boolean invokeAction(int action) {
                fireAction(UploadPhotoScreen.ACTION_ENTER);
                return true;
            }
        };
        topManager.add(uploadPhotoButton);
        friendListButton = new ButtonField(LABEL_FRIENDS_LIST) {

            protected boolean invokeAction(int action) {
                fireAction(FriendsListScreen.ACTION_ENTER);
                return true;
            }
        };
        topManager.add(friendListButton);
        pokeButton = new ButtonField(LABEL_POKE_FRIEND) {

            protected boolean invokeAction(int action) {
                fireAction(PokeFriendScreen.ACTION_ENTER);
                return true;
            }
        };
        topManager.add(pokeButton);
        wallButton = new ButtonField(LABEL_WRITE_WALL) {

            protected boolean invokeAction(int action) {
                fireAction(PostWallScreen.ACTION_ENTER);
                return true;
            }
        };
        topManager.add(wallButton);
        sendMessageButton = new ButtonField(LABEL_SEND_MESSAGE) {

            protected boolean invokeAction(int action) {
                fireAction(SendMessageScreen.ACTION_ENTER);
                return true;
            }
        };
        topManager.add(sendMessageButton);
        add(new SeparatorField());
    }

    public boolean onClose() {
        if (Dialog.ask("Please choose:", new String[] { "Exit", "Logout & Exit" }, 0) == 0) {
            log.info("User Exit.");
            ((StrawBerry) getApplication()).saveAndExit();
        } else {
            log.info("User Logout & Exit.");
            ((StrawBerry) getApplication()).logoutAndExit();
        }
        return true;
    }
}

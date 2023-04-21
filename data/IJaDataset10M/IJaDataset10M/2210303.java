package samples.strawberry;

import com.blackberry.facebook.FacebookContext;
import com.blackberry.facebook.ui.FacebookScreen;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;

final class PokeFriendScreen extends FacebookScreen {

    static final String ACTION_ENTER = "pokeFriend";

    static final String ACTION_SUCCESS = "friendPoked";

    static final String ACTION_ERROR = "error";

    private static final String LABEL_TITLE = "Poke Friend";

    /**
	 * Default constructor.
	 * 
	 */
    PokeFriendScreen(FacebookContext pfbc) {
        super(pfbc);
        LabelField titleLabel = new LabelField(LABEL_TITLE, LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
        setTitle(titleLabel);
        Dialog.alert("Not implemented yet.");
    }
}

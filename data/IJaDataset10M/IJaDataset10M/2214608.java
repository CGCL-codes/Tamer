package clavicom.core.listener;

import java.util.EventListener;
import clavicom.tools.TKeyClavicomActionType;

public interface OnClickKeyClavicomListener extends EventListener {

    public void onClickKeyClavicom(TKeyClavicomActionType actionType);
}

package clavicom.core.listener;

import java.util.EventListener;
import clavicom.core.keygroup.keyboard.key.CKeyLauncher;

public interface OnClickKeyLauncherListener extends EventListener {

    public void onClickKeyLauncher(CKeyLauncher keyLauncher);
}

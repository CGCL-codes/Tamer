package charismata.web.zk;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class NewConnectionListener implements EventListener {

    MainController mainCont;

    String connectionName;

    public NewConnectionListener(MainController mainCont, String connectionName) {
        this.mainCont = mainCont;
        this.connectionName = connectionName;
    }

    @Override
    public void onEvent(Event arg0) throws Exception {
        mainCont.onNewConnection(connectionName);
    }
}

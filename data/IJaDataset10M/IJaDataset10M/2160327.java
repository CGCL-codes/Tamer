package net.sf.doolin.app.sc.client.controller;

import java.util.Collection;
import java.util.List;
import net.sf.doolin.app.sc.engine.InstanceSummary;
import net.sf.doolin.app.sc.resources.bean.InstanceSummaryBean;
import net.sf.doolin.app.sc.resources.bean.InstanceSummaryBeanConverter;
import net.sf.doolin.bus.bean.Bean;
import net.sf.doolin.util.Utils;
import org.springframework.stereotype.Component;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.util.concurrent.Lock;

@Component
public class RemoteGameControllerConfig extends Bean implements GameControllerConfig {

    private static final long serialVersionUID = 1L;

    public static final String CLIENT_NAME = "clientName";

    public static final String GAME_PASSWORD = "gamePassword";

    public static final String URL = "url";

    public static final String SELECTED_ADDRESS = "selectedAddress";

    public static final String SELECTED_GAME = "selectedGame";

    private final EventList<String> addresses;

    private String selectedAddress;

    private final EventList<InstanceSummaryBean> gameList;

    private InstanceSummaryBean selectedGame;

    private String url;

    private char[] gamePassword;

    private String clientName;

    public RemoteGameControllerConfig() {
        this.gameList = new BasicEventList<InstanceSummaryBean>();
        this.addresses = new BasicEventList<String>();
    }

    public EventList<String> getAddresses() {
        return this.addresses;
    }

    public String getClientName() {
        return this.clientName;
    }

    public EventList<InstanceSummaryBean> getGameList() {
        return this.gameList;
    }

    public char[] getGamePassword() {
        return this.gamePassword;
    }

    public String getSelectedAddress() {
        return this.selectedAddress;
    }

    public InstanceSummaryBean getSelectedGame() {
        return this.selectedGame;
    }

    public String getUrl() {
        return this.url;
    }

    public void init(List<String> files) {
        Lock lock = this.addresses.getReadWriteLock().writeLock();
        lock.lock();
        try {
            this.addresses.clear();
            this.addresses.addAll(files);
        } finally {
            lock.unlock();
        }
    }

    public void refreshGameList(List<InstanceSummaryBean> instanceSummaryBeanList) {
        Lock lock = this.gameList.getReadWriteLock().writeLock();
        lock.lock();
        try {
            this.gameList.clear();
            this.gameList.addAll(instanceSummaryBeanList);
        } finally {
            lock.unlock();
        }
    }

    public void setClientName(String clientName) {
        notify(CLIENT_NAME, this.clientName, this.clientName = clientName);
    }

    public void setGameList(Collection<InstanceSummary> list) {
        List<InstanceSummaryBean> beanList = Utils.convertToList(list, new InstanceSummaryBeanConverter());
        Lock lock = this.gameList.getReadWriteLock().writeLock();
        lock.lock();
        try {
            this.gameList.clear();
            this.gameList.addAll(beanList);
        } finally {
            lock.unlock();
        }
    }

    public void setGamePassword(char[] password) {
        notify(GAME_PASSWORD, this.gamePassword, this.gamePassword = password);
    }

    public void setSelectedAddress(String selectedAddress) {
        notify(SELECTED_ADDRESS, this.selectedAddress, this.selectedAddress = selectedAddress);
        setUrl(selectedAddress);
    }

    public void setSelectedGame(InstanceSummaryBean selectedGameID) {
        notify(SELECTED_GAME, this.selectedGame, this.selectedGame = selectedGameID);
    }

    public void setUrl(String url) {
        notify(URL, this.url, this.url = url);
    }
}

package ch.fusun.baron.player.server.ui;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.graphics.Image;
import ch.fusun.baron.core.injection.Inject;
import ch.fusun.baron.core.rmi.User;
import ch.fusun.baron.data.DataListener;
import ch.fusun.baron.data.DataUpdate;
import ch.fusun.baron.player.Dynasty;
import ch.fusun.baron.player.api.PlayerService;
import ch.fusun.baron.serverapp.view.UserChildrenProvider;

/**
 * Dynasties of a user
 */
public class DynastyChildrenProvider implements UserChildrenProvider, DataListener {

    private transient PlayerService service;

    /**
	 * Kryo
	 */
    public DynastyChildrenProvider() {
    }

    /**
	 * @param service
	 *            The new service
	 */
    @Inject
    public void setService(PlayerService service) {
        this.service = service;
        service.addDataListener(this);
    }

    @Override
    public List<?> getChildren(User user) {
        List<Dynasty> dynasties = new LinkedList<Dynasty>();
        dynasties.add(service.getDynasty(user));
        return dynasties;
    }

    @Override
    public Image getImage(Object obj) {
        if (obj instanceof Dynasty) {
            return Activator.getImageForDynasty((Dynasty) obj);
        }
        return null;
    }

    @Override
    public void dataChanged(DataUpdate update) {
    }
}

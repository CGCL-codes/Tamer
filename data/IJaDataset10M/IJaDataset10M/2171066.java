package ru.nsu.ccfit.pm.econ.net;

import ru.nsu.ccfit.pm.econ.common.engine.events.IUGameEvent;

/**
 * @author orfest
 *
 */
public interface IClientCoordinator {

    void eventArrived(IUGameEvent event);
}

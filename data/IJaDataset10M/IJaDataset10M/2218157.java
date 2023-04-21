package org.ourgrid.worker.controller.actions.idlenessdetector;

import org.ourgrid.common.util.OS;
import org.ourgrid.worker.controller.IdlenessDetectorController;
import org.ourgrid.worker.dao.IdlenessDetectorDAO;

/**
 * @author alan
 *
 */
public class IdlenessDetectorActionFactory {

    private final IdlenessDetectorController controller;

    private final IdlenessDetectorDAO idlenessDAO;

    /**
	 * @param controller
	 * @param idlenessTime
	 */
    public IdlenessDetectorActionFactory(IdlenessDetectorController controller, IdlenessDetectorDAO idlenessDAO) {
        this.controller = controller;
        this.idlenessDAO = idlenessDAO;
    }

    public IdlenessDetectorAction createIdlenessDetectorAction() {
        if (OS.isFamilyWindows()) {
            return createWinIdlenessDetectorAction();
        }
        return createLinuxIdlenessDetectorAction();
    }

    public WinIdlenessDetectorAction createWinIdlenessDetectorAction() {
        return new WinIdlenessDetectorAction(this.controller, this.idlenessDAO);
    }

    public LinuxIdlenessDetectorAction createLinuxIdlenessDetectorAction() {
        return new LinuxIdlenessDetectorAction(this.controller, this.idlenessDAO);
    }
}

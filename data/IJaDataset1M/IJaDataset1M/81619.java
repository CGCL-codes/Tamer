package no.ugland.utransprod.gui;

import java.awt.Component;
import no.ugland.utransprod.model.ApplicationUser;
import no.ugland.utransprod.model.UserType;
import no.ugland.utransprod.service.AccidentManager;
import no.ugland.utransprod.service.DeviationManager;
import no.ugland.utransprod.service.ManagerRepository;

/**
 * Interface for klasser som skal h�ndtere oppstartsvinduer
 * @author atle.brekka
 *
 */
public interface MainWindow {

    /**
	 * Bygger oppstartsvindu
	 * @param listener 
	 * @return vindu
	 */
    Component buildMainWindow(SystemReadyListener listener, ManagerRepository managerRepository);

    /**
	 * Setter bruker
	 * @param currentUser
	 */
    void setLogin(Login aLogin);
}

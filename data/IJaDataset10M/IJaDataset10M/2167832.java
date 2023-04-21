package de.janrieke.contractmanager;

import java.rmi.RemoteException;
import de.janrieke.contractmanager.gui.action.ExportCancelationReminders;
import de.janrieke.contractmanager.rmi.ContractDBService;
import de.janrieke.contractmanager.server.ContractDBServiceImpl;
import de.janrieke.contractmanager.server.DBSupportH2Impl;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.Version;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * You need to have at least one class which inherits from
 * <code>AbstractPlugin</code>. If so, Jameica will detect your plug-in
 * automatically at startup.
 * 
 * @author willuhn, jrieke
 */
public class ContractManagerPlugin extends AbstractPlugin {

    private static ContractManagerPlugin instance = null;

    /**
	 * constructor.
	 */
    public ContractManagerPlugin() {
        super();
    }

    /**
	 * This method is invoked on every startup. You can make here some stuff to
	 * init your plug-in. If you get some errors here and you don't want to
	 * activate the plug-in, simply throw an ApplicationException.
	 * 
	 * @see de.willuhn.jameica.plugin.AbstractPlugin#init()
	 */
    @Override
    public void init() throws ApplicationException {
        assert instance == null;
        instance = this;
        call(new ServiceCall() {

            public void call(ContractDBService service) throws ApplicationException, RemoteException {
                service.checkConsistency();
            }
        });
    }

    /**
	 * This method is called only the first time, the plug-in is loaded (before
	 * executing init()). if your installation procedure was not successful,
	 * throw an ApplicationException.
	 * 
	 * @see de.willuhn.jameica.plugin.AbstractPlugin#install()
	 */
    @Override
    public void install() throws ApplicationException {
        call(new ServiceCall() {

            public void call(ContractDBService service) throws ApplicationException, RemoteException {
                service.install();
            }
        });
    }

    /**
	 * This method will be executed on every version change.
	 * 
	 * @see de.willuhn.jameica.plugin.AbstractPlugin#update(double)
	 */
    @Override
    public void update(Version oldVersion) throws ApplicationException {
    }

    /**
	 * Here you can do some cleanup stuff. The method will be called on every
	 * clean shutdown of Jameica.
	 * 
	 * @see de.willuhn.jameica.plugin.AbstractPlugin#shutDown()
	 */
    @Override
    public void shutDown() {
        try {
            if (Settings.getICalAutoExport()) {
                new ExportCancelationReminders().handleAction(null);
            }
        } catch (RemoteException e) {
            GUI.getStatusBar().setErrorText(Settings.i18n().tr("Error during cancellation reminder export."));
        } catch (ApplicationException e) {
            GUI.getStatusBar().setErrorText(Settings.i18n().tr("Error during cancellation reminder export."));
        }
    }

    public static ContractManagerPlugin getInstance() {
        return instance;
    }

    /**
	 * Hilfsmethode zum bequemen Ausfuehren von Aufrufen auf dem Service.
	 */
    private interface ServiceCall {

        /**
		 * @param service
		 * @throws ApplicationException
		 * @throws RemoteException
		 */
        public void call(ContractDBService service) throws ApplicationException, RemoteException;
    }

    /**
	 * Hilfsmethode zum bequemen Ausfuehren von Methoden auf dem Service.
	 * 
	 * @param call
	 *            der Call.
	 * @throws ApplicationException
	 */
    private void call(ServiceCall call) throws ApplicationException {
        if (Application.inClientMode()) return;
        ContractDBService service = null;
        try {
            service = new ContractDBServiceImpl();
            service.start();
            call.call(service);
        } catch (ApplicationException ae) {
            throw ae;
        } catch (Exception e) {
            Logger.error("Unable to init db service", e);
            I18N i18n = getResources().getI18N();
            String msg = i18n.tr("Unable to initialize database for ContractManager.\n\n{0} ", e.getMessage());
            String driver = ContractDBService.SETTINGS.getString("database.driver", null);
            if (driver != null && driver.equals(DBSupportH2Impl.class.getName())) {
                msg += "\n\nPossible database problem. Try restoring an old version.";
            }
            throw new ApplicationException(msg, e);
        } finally {
            if (service != null) {
                try {
                    service.stop(true);
                } catch (Exception e) {
                    Logger.error("Error while closing db service", e);
                }
            }
        }
    }
}

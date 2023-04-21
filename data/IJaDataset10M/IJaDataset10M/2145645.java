package conexion.startup;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import conexion.Conexion;
import conexion.LocateConexion;
import conexion.db.RDBMS;
import conexion.service.Control;

/**
 * Clase que permite inicializar o detener el servicio de conexion.
 *
 * @since Conexion 0.2.0
 * @author Marlon J. Manrique
 */
class StartUp {

    /**
	 * Directorio padre de la instalacion de Conexion.
	 */
    String home;

    /**
	 * Configuracion de Conexion.
	 */
    Setup setup;

    /**
	 * Registro de servicios.
	 */
    Conexion conexion;

    /**
	 * Retorna el nombre del equipo local.
	 *
	 * @return nombre de l equipo.
	 */
    String getLocalHostName() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    /**
	 * Crea el <i>rmiregistry</i> en el equipo local, en el puerto especificado.
	 *
	 * @param port puerto sobre el que se ejecutara el <i>rmiregistry</i>.
	 */
    void startRMIRegistry(int port) throws RemoteException {
        LocateRegistry.createRegistry(port);
    }

    /**
	 * Inicializa la prestacion del <i>rmiregistry</i>, de acuerdo a lo 
	 * especificado en la configuracion.
	 */
    void startRMI() throws RemoteException {
        RMISetup rmi = setup.getRMI();
        if (rmi.getStart() == true) {
            rmi.setRMIServer(getLocalHostName());
            startRMIRegistry(rmi.getPort());
        }
    }

    /**
	 * Instancia el servicio y lo registra en el registro de servicios.
	 *
	 * @param service Configuracion del servicio.
	 */
    void startService(ServiceSetup service) {
        try {
            RDBMSSetup rdbms = service.getRDBMS();
            if (rdbms == null) {
                Remote remote = (Remote) Class.forName(service.getClassName()).newInstance();
                conexion.bindService(service.getName(), remote);
                return;
            }
            Object[] parameterValues = new String[2];
            parameterValues[0] = rdbms.getDriver();
            parameterValues[1] = rdbms.getURL();
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = parameterValues[0].getClass();
            parameterTypes[1] = parameterValues[1].getClass();
            Class dbClass = Class.forName(rdbms.getClassName());
            Constructor constructor = dbClass.getConstructor(parameterTypes);
            Object db = constructor.newInstance(parameterValues);
            parameterValues = new Object[1];
            parameterValues[0] = db;
            Class dbInterface = Class.forName(rdbms.getInterfaceName());
            parameterTypes = new Class[1];
            parameterTypes[0] = dbInterface;
            Class serviceClass = Class.forName(service.getClassName());
            constructor = serviceClass.getConstructor(parameterTypes);
            Remote remote = (Remote) constructor.newInstance(parameterValues);
            conexion.bindService(service.getName(), remote);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
	 * Incializa la prestacion de servicios deacuerdo a lo especificado en el
	 * archivo de configuracion <code>/conf/conexion.xml</code> bajo el directorio home
	 * de conexion obtenido a traves de la propiedad del sistema <code>conexion.home</code>
	 */
    void startUp() {
        home = System.getProperty("conexion.home");
        if (home == null) {
            System.out.println("Set conexion.home Property");
            return;
        }
        try {
            setup = XMLSetup.getSetup(home + "/conf/conexion.xml");
            startRMI();
            RMISetup rmi = setup.getRMI();
            if (rmi.getStart() == true) conexion = new conexion.ConexionImpl(rmi.getRMIServer(), rmi.getPort()); else conexion = LocateConexion.getConexion(rmi.getRMIServer(), rmi.getPort());
            ServiceSetup[] service = setup.getServices();
            for (int i = 0; i < service.length; i++) startService(service[i]);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
	 * Detiene la prestacion de servicios deacuerdo a lo especificado en el
	 * archivo de configuracion <code>/conf/conexion.xml</code> bajo el directorio home
	 * de conexion obtenido a traves de la propiedad del sistema <code>conexion.home</code>
	 */
    void stop() {
        home = System.getProperty("conexion.home");
        if (home == null) {
            System.out.println("Set conexion.home Property");
            return;
        }
        try {
            setup = XMLSetup.getSetup(home + "/conf/conexion.xml");
            RMISetup rmi = setup.getRMI();
            if (rmi.getStart()) rmi.setRMIServer(getLocalHostName());
            conexion = LocateConexion.getConexion(rmi.getRMIServer(), rmi.getPort());
            ((Control) conexion).kill();
        } catch (RemoteException e) {
            if (!(e instanceof java.rmi.UnmarshalException)) System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
	 * Crea una nueva instancia de esta clase con valores vacios.
	 */
    StartUp() {
    }
}

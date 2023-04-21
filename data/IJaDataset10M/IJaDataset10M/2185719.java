package threading;

import general.Configuracion;
import general.ExclusionMutua;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import SimpleViz.MessageTrafficUsersVisualization;
import SimpleViz.ProfiledUsersVisualization;
import usuarios.URS;
import visualizaciones.GDF_Grupos;
import visualizaciones.GephiMasterVisualization;
import ddbb.GestorDB;
import debug.Debug;

/**
 * Manages social network evolution
 * 
 * @author u526963
 * 
 */
public class ExecutionManager {

    static Logger logger = Logger.getLogger(ExecutionManager.class.getName());

    /**
	 * Pool que proporcionar� threads a los usuarios para que ejecuten cada
	 * instante.
	 */
    ThreadPool pool;

    public ExecutionManager() {
        super();
        pool = new ThreadPool(Configuracion.TAMPOOLTHREADS, Configuracion.TAMPOOLTHREADS, Configuracion.TAMCOLATAREAS);
    }

    public ExecutionManager(ThreadPool pool) {
        super();
        this.pool = pool;
    }

    /**
	 * Executes one step in time for the social network
	 */
    public void executeStep() {
        boolean saturado = false;
        boolean ejecutar = true;
        int deb = 0;
        Enumeration<URS> lista = ExclusionMutua.dameUsuarios();
        URS u;
        while (lista.hasMoreElements()) {
            saturado = ExclusionMutua.getSaturacion();
            ejecutar = ExclusionMutua.getEjecutar();
            u = lista.nextElement();
            if (!pool.ejecutarTarea(u)) try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error("Interruption in ExecuteStep: Execution user tasks", e);
            }
            deb++;
            if (deb == 500) {
                deb = 0;
                Debug.infoMemoria();
            }
        }
        try {
            while (!(pool.tareasPendientes.isEmpty())) {
                logger.info("Hilos en uso: " + pool.numHilosUso);
                Thread.sleep(2000);
            }
            lista = null;
            System.gc();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error("Interruption in ExecuteStep: Cleaning thinds up", e);
        }
        new GephiMasterVisualization().flushData2GephiMaster_Mensajes(ExclusionMutua.getInstante(), ExclusionMutua.getIdRedActiva());
        ExclusionMutua.incrementaInstante();
        GestorDB.actualizarInstante();
    }
}

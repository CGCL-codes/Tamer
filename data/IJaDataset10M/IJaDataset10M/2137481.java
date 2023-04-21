package medim;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import medim.model.DiagramaTendencias;
import medim.model.ECGFragment;
import medim.model.ECGRequest;
import medim.model.HrvHora;
import medim.model.MorphologicalFamilies;
import medim.model.SaO2Fragment;
import medim.model.physiologicalEvents.BloodPressureEvent;
import medim.model.physiologicalEvents.IPhysiologicalEvent;
import medim.model.physiologicalEvents.WeightEvent;
import medim.model.systemEvents.ISystemEvent;
import servando.communications.CommunicableService;
import servando.communications.ObjectTransporter;
import servando.models.patients.Patient;

/**
 * Implementación de un servicio que dialoga con medim, capaz de recibir
 * diferentes eventos fisiológicos y del sistema procedentes de los
 * dispositivos de monitorización.
 * @author Tomás Teijeiro Campo
 */
public class MedimService implements CommunicableService {

    /**
     * Tabla en la que guardaremos a modo de caché las asociaciones entre
     * direcciones IP y pacientes, que iremos obteniendo desde la base
     * de datos.
     */
    private HashMap<String, Patient> patientCache;

    /**
     * Transporter que utilizaremos para solicitar los fragmentos de ECG a los
     * clientes.
     */
    private ObjectTransporter transporter;

    /**
     * Crea una nueva instancia del servicio de Medim
     */
    public MedimService() {
        patientCache = new HashMap<String, Patient>();
        transporter = new ObjectTransporter();
        transporter.setRemoteService("medim");
    }

    /**
     * Obtiene el identificador de este servicio.
     * @return
     */
    public String getId() {
        return "medim";
    }

    public boolean processSend(Object obj, String client) {
        try {
            Patient p = patientCache.get(client);
            if (obj instanceof DiagramaTendencias) {
                return true;
            } else if (obj instanceof SaO2Fragment) {
                return true;
            } else if (obj instanceof ECGFragment) {
                return true;
            } else if (obj instanceof MorphologicalFamilies) {
                return true;
            } else if (obj instanceof HrvHora) {
                return true;
            } else if (obj instanceof IPhysiologicalEvent) {
                return true;
            } else if (obj instanceof ISystemEvent) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(MedimService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void processISend(Object obj, String client) {
        if (obj instanceof DiagramaTendencias) {
            DiagramaTendencias dt = (DiagramaTendencias) obj;
        }
    }

    public Object processSendReceive(Object obj, String client) {
        if (obj instanceof ECGRequest) {
            ECGRequest req = (ECGRequest) obj;
        }
        return obj;
    }

    /**
     * Método que recibe un evento fisiológico, y comprueba contra la base de datos y
     * a partir de los datos de ese evento si se debe generar alguna alarma de acuerdo
     * al protocolo establecido para el seguimiento de enfermos con Insuficiencia Cardiaca.
     * @param event Evento fisiológico. Actualmente se comprueban alarmas sobre la tensión
     * arterial y sobre el peso.
     */
    private void generateAlarms(Patient p, IPhysiologicalEvent event) {
        if (event instanceof BloodPressureEvent) {
        } else if (event instanceof WeightEvent) {
        }
    }
}

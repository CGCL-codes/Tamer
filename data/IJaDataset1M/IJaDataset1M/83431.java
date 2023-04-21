package es.caib.bpm.admin.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.login.LoginException;
import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import es.caib.bpm.beans.local.BPMEngineLocal;
import es.caib.bpm.exception.BPMException;
import es.caib.bpm.vo.ProcessDefinition;

public class DespliegueUI extends Window {

    /**
	 * @throws LoginException 
	 * @throws RemoteException 
	 * 
	 */
    public void cargarListadoProcesos() throws LoginException, RemoteException {
        Session sesion = this.getDesktop().getSession();
        BPMEngineLocal engine = (BPMEngineLocal) sesion.getAttribute("engine");
        List list = null;
        Listbox listbox = null;
        ProcessDefinition definition = null;
        Listitem item = null;
        list = engine.findProcessDefinitions(null, true);
        listbox = (Listbox) this.getFellowIfAny("listaProcesos");
        if (listbox.getItems() != null) {
            listbox.getItems().clear();
        }
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            definition = (ProcessDefinition) it.next();
            item = new Listitem();
            item.getChildren().add(new Listcell(definition.getName()));
            item.getChildren().add(new Listcell(definition.getTag()));
            item.getChildren().add(new Listcell(new Long(definition.getId()).toString()));
            listbox.getItems().add(item);
        }
    }

    /**
	 * Sube un archivo a la aplicacion de workflow.
	 * @throws LoginException 
	 *
	 */
    public void upload() throws LoginException {
        Session sesion = this.getDesktop().getSession();
        BPMEngineLocal engine = (BPMEngineLocal) sesion.getAttribute("engine");
        Media dataSubida = null;
        InputStream streamLectura = null;
        int leidos = 0;
        byte[] buffer = new byte[4096];
        Textbox resultadoDespliegue = null;
        try {
            resultadoDespliegue = (Textbox) this.getFellow("txtResultadoDespliegue");
            dataSubida = Fileupload.get();
            if (dataSubida != null) {
                resultadoDespliegue.setText(Labels.getLabel("deploy.msgDesplegandoProceso"));
                streamLectura = dataSubida.getStreamData();
                log.debug("Abrimos la transferencia");
                engine.openDeployParDefinitionTransfer();
                log.debug("Enviamos los paquetes");
                while ((leidos = streamLectura.read(buffer)) != -1) {
                    engine.nextDeployParDefinitionPackage(buffer, leidos);
                }
                log.debug("Cerramos la transferencia del archivo");
                engine.endDeployParDefinitionTransfer();
                String messages[] = engine.getDeployMessages();
                this.cargarListadoProcesos();
                log.debug("Se despleg� el proceso correctamente.");
                StringBuffer b = new StringBuffer();
                b.append(Labels.getLabel("deploy.msgProcesoDesplegadoCorrectamente"));
                for (int i = 0; i < messages.length; i++) {
                    b.append('\n');
                    b.append(messages[i]);
                }
                resultadoDespliegue.setText(b.toString());
            }
        } catch (BPMException e) {
            String messages[] = engine.getDeployMessages();
            StringBuffer b = new StringBuffer();
            b.append(Labels.getLabel("deploy.msgErrorDesplegandoProceso"));
            for (int i = 0; i < messages.length; i++) {
                b.append('\n');
                b.append(messages[i]);
            }
            resultadoDespliegue.setText(b.toString());
            log.error(e);
        } catch (Exception e) {
            resultadoDespliegue.setText(Labels.getLabel("deploy.msgErrorDesplegandoProceso") + e.toString());
            log.error(e);
        } finally {
            if (streamLectura != null) {
                try {
                    streamLectura.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
    }

    private static Logger log = Logger.getLogger(DespliegueUI.class);
}

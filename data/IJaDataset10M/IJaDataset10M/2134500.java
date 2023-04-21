package client.update;

import java.io.IOException;
import javax.swing.JProgressBar;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;

/**
 * Manager de la coneccion FTP.
 * Se encarga mantener la coneccion FTP e interpretar 
 * los comandos de comunicacion con el servidor del servicio FTP
 * 
 * @author Guillermo Fiorini, Gabriel Alvarez, Gouvert Rodolfo
 * @version 1.0
 */
public class FTPManager {

    /** Instancia del Cliente FTP. */
    private FTPClient ftp;

    /** Instancia de los parametros de Configuracion. */
    private Config cfg;

    /** Instancia del monitor de progreso. */
    private ProgressMonitor pMonitor;

    /**
	 * Instancia un nuevo fTPManager.
	 * 
	 * @param pgrsBar La barra de progreso para 
	 * el recuento de transferencia de archivo
	 * @param ovPrgsBar La barra de 
	 * progreso para el recuento total de transferencia
	 */
    public FTPManager(final JProgressBar pgrsBar, final JProgressBar ovPrgsBar) {
        cfg = Config.getInstancia();
        pMonitor = new ProgressMonitor(pgrsBar, ovPrgsBar);
        ftp = new FTPClient();
    }

    /**
	 * Descarga un archivo desde el servidor FTP.
	 * 
	 * @param fileName El nombre del archivo
	 * @param localPath La carpeta local donde se desea almacenar
	 * 
	 * @throws IOException Se�al de que ocurrio una excepcion I/O.
	 * @throws FTPException Se�al de que ocurrio una excepcion FTP
	 */
    public final void downloadFile(final String fileName, final String localPath) throws IOException, FTPException {
        ftp.get(localPath, fileName);
    }

    /**
	 * Crea una coneccion con el servidor FTP.
	 * 
	 * @throws IOException Se�al de que ocurrio una excepcion I/O.
	 * @throws FTPException Se�al de que ocurrio una excepcion FTP
	 */
    public final void conectar() throws IOException, FTPException {
        ftp = null;
        ftp = new FTPClient();
        ftp.setRemoteHost(cfg.getFTPHost());
        ftp.connect();
        ftp.login(cfg.getFTPUser(), cfg.getFTPPass());
        ftp.setProgressMonitor(pMonitor);
        ftp.setConnectMode(FTPConnectMode.PASV);
        ftp.setType(FTPTransferType.BINARY);
    }

    /**
	 * Finaliza una coeccion FTP.
	 * Este metodo pregunta antes si la coneccion se encuentra 
	 * abirta, de no ser asi no realiza ninguna accion
	 * 
	 * @throws IOException Se�al de que ocurrio una excepcion I/O.
	 * @throws FTPException Se�al de que ocurrio una excepcion FTP
	 */
    public final void shutDown() throws IOException, FTPException {
        if (ftp.connected()) {
            ftp.quit();
        }
    }

    /**
	 * Cambia el directorio remoto actual.
	 * 
	 * @param directory El nombre del nuevo directorio
	 * 
	 * @throws IOException Se�al de que ocurrio una excepcion I/O.
	 * @throws FTPException Se�al de que ocurrio una excepcion FTP
	 */
    public final void changeDir(final String directory) throws IOException, FTPException {
        ftp.chdir(directory);
    }
}

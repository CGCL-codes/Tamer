package edu.uah.lazarillo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import edu.uah.lazarillo.comm.in.file.CommunicationEvent;
import edu.uah.lazarillo.comm.in.file.FileReaderRunnable;
import edu.uah.lazarillo.core.model.Rfid;
import edu.uah.lazarillo.core.model.Visit;
import edu.uah.lazarillo.mock.SimulatorMock;

/**
 * Este test crea un fichero en espacio de usuario, para poder ejecutar el test
 * sobre los ficheros de entrada de datos.
 * 
 * @author emoriana
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class FileReaderTest {

    private String dni = new String("2034780");

    private String rfidCode = new String("BE31C63300000001");

    private String targetArea = new String("6");

    private String dateCad = new String("05/12/2008 08:43:06");

    private String probori = "100";

    private String content = dni + " " + rfidCode + "  " + targetArea + " " + dateCad + probori;

    private String homeUser = System.getProperties().getProperty("user.home");

    private String path = homeUser + File.separator + ".testlazarillo" + File.separator;

    private String fileName = "2034780000.txt";

    private FileReaderRunnable fileReaderRunnable = null;

    private SimulatorMock simulatorMock = null;

    private Thread thread = null;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
	 * Creamos el fichero necesitado
	 * 
	 * @throws IOException
	 */
    @SuppressWarnings("unused")
    public void createfile(String fileName, String content) throws IOException {
        File fileDir = new File(path);
        fileDir.mkdir();
        File file = new File(path + fileName);
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                System.out.println("ERROR, no se pudo crear el fichero para las pruebas");
            }
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.write(content);
            printWriter.flush();
            printWriter.close();
            logger.debug("Fichero creado en " + path + fileName + " con contenido:\n" + content);
        }
    }

    @Before
    public void creaFicheroDeEntrada() throws IOException {
        createfile(fileName, content);
    }

    @SuppressWarnings("unused")
    public void deleteFile() {
        int length = fileName.length();
        String newName = fileName.substring(0, length - 3);
        newName += fileReaderRunnable.getReadedExtension();
        File fileDest = new File(path, newName);
        fileDest.delete();
        logger.debug("eliminado el fichero:" + fileDest.getAbsolutePath());
    }

    /**
	 * Inicializamos todo
	 */
    private void init() {
        fileReaderRunnable = new FileReaderRunnable();
        simulatorMock = new SimulatorMock();
        fileReaderRunnable.setSimulator(simulatorMock);
        fileReaderRunnable.setPath(path);
        thread = new Thread(fileReaderRunnable);
        thread.start();
        logger.debug("Inicilizamos con path" + path);
    }

    public void testNewVisit() throws IOException, ParseException {
        init();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommunicationEvent event = simulatorMock.getVisit();
        Visit visit = event.getVisit();
        assert (visit != null);
        assert (event.getType() == CommunicationEvent.NEWVISIT);
        Rfid rfid = new Rfid();
        rfid.setCode(rfidCode);
        assert (visit.getRfid().equals(rfid));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = dateFormat.parse(dateCad);
        assert (visit.getStart().equals(date));
        assert (visit.getPacient().getDni().equals(dni));
        logger.debug("Se obtuvo la visita y los datos concuerdan");
        File file = new File(path + fileName);
        assert (!file.exists());
        logger.debug("el fichero con extension txt ya no existe:" + file.getAbsolutePath());
        int length = fileName.length();
        String newName = fileName.substring(0, length - 3);
        newName += fileReaderRunnable.getReadedExtension();
        File fileDest = new File(path, newName);
        assert (fileDest.exists());
        logger.debug("El fichero con la nueva extensión es:" + fileDest.getAbsolutePath());
        createfile(dni + "0" + targetArea + "S.txt", "hola");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        event = simulatorMock.getVisit();
        visit = event.getVisit();
        assert (visit != null);
        assert (event.getType() == CommunicationEvent.OUTDOCTORROOM);
        assert (visit.getPacient().getDni().equals(dni));
    }
}

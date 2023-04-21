package it.univaq.di.chameleonclient.midlet.computation;

import it.univaq.di.chameleonclient.midlet.ChameleonMIDlet;
import it.univaq.di.chameleonclient.midlet.graphics.IncrementalGaugeForm;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import java.io.*;
import javax.microedition.io.file.FileConnection;

/**
 * <p>Title: QueryDataSet </p>
 *
 * <p>Description: QueryDataSet gestisce la connessione 
 * HttpConnection con il server.
 * Sono stati utilizzati i thread in modo tale da evitare 
 * il deadlock con gli eventi di actionCommand().
 * Nota: questa classe non solo gestisce la connessione ma 
 * � stata integrata per gestire i gauges grafici che offrono 
 * una rappresentazione visiva sullo
 * stato della connessione, ossia del downloading.
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Guido Di Campli
 * @version 1.0
 */
public class QueryDataSet extends Thread {

    /** strquery contiene la query da inviare al server */
    private String strquery = new String();

    /** httpConnection */
    private HttpConnection httpConnection = null;

    /** outputStream di httpConnection */
    private OutputStream os = null;

    /** inputStream di httpConnection */
    private InputStream is = null;

    /** host */
    private String host = null;

    /** FileConnection */
    private FileConnection filecon = null;

    /** OutputStream di FileConnection */
    private OutputStream osfile = null;

    /** nameFile */
    private String nameFile = new String();

    /** incrementalGauge visualizzato durante il downloading */
    IncrementalGaugeForm incrementalGauge = null;

    /**
     * Costruttore
     */
    public void QueryDataSet() {
    }

    /**
     * run Thread
     */
    public void run() {
        String path = ChameleonMIDlet.getFolder();
        try {
            if (this.nameFile.length() > 0) {
                filecon = (FileConnection) Connector.open(path + this.nameFile);
            } else {
                filecon = (FileConnection) Connector.open(path + "Default.txt");
            }
            if (!filecon.exists()) {
                System.out.println("Crea PRIMA");
                filecon.create();
                System.out.println("Crea DOPO");
            }
            System.out.println("HttpConnection in corso...");
            httpConnection = (HttpConnection) Connector.open(host);
            System.out.println("Connesso");
            httpConnection.setRequestMethod(HttpConnection.POST);
            httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (campotible; MSIE 6.0; Windows NT 5.1)");
            httpConnection.setRequestProperty("Content-Language", "it-IT");
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            System.out.println("\nTrasferimento in corso... \n");
            if (strquery.length() > 0) {
                System.out.println("Send query...");
                os = httpConnection.openOutputStream();
                byte[] query = (strquery).getBytes();
                os.write(query);
                os.close();
                System.out.println("Ok\n");
            }
            System.out.println("Receive response...");
            is = httpConnection.openInputStream();
            osfile = filecon.openOutputStream();
            long size = this.httpConnection.getLength();
            System.out.println("Lunghezza decodificata: " + size);
            int numberByteReceive = 0;
            int maxValue = 8;
            int value = 0;
            long gap = size / maxValue;
            System.out.println("Gap: " + gap);
            this.incrementalGauge.setMaxValue(maxValue);
            this.incrementalGauge.setValue(value);
            int ch;
            while ((ch = is.read()) != -1) {
                osfile.write(ch);
                numberByteReceive++;
                if (numberByteReceive % gap == 0) {
                    value++;
                    System.out.println("Value: " + value);
                    System.out.println("numberByteReceive: " + numberByteReceive);
                    this.incrementalGauge.setValue(value);
                }
            }
            System.out.println("Numero di Byte ricevuti: " + numberByteReceive);
            System.out.println("Ok\n");
            osfile.close();
            filecon.close();
            is.close();
            httpConnection.close();
            this.incrementalGauge.showNextDisplay();
            System.out.println("Connessione chiusa");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Eccezione run() QueryDataSet: " + ex.getMessage());
            this.incrementalGauge.showWarning(ex);
        }
    }

    /**
     * Setta l'indirizzo dell'host
     * @param host String
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Setta query da inviare al server
     * @param strquery String
     */
    public void setQuery(String strquery) {
        this.strquery = strquery;
    }

    /**
     * Setta il nome del file dove verra salvata la risposta
     * @param nameFile String
     */
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    /**
     * setIncrementalGauge(IncrementalGaugeForm incrementalGauge)
     * @param incrementalGauge IncrementalGaugeForm
     */
    public void setIncrementalGauge(IncrementalGaugeForm incrementalGauge) {
        this.incrementalGauge = incrementalGauge;
    }
}

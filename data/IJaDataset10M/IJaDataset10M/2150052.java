package model.DeviceHandlers;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

/**
 * <p>Title: TtyIHandler</p>
 * <p>Description: A class that deals with the /dev/ttyI devices</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Ynon
 * @version 1.0
 */
public class TtyIHandler {

    private FileWriter m_fout;

    private BufferedReader m_bfin;

    private String[] m_init = { "AT&F\n", "ATE0\n", "ATI\n", "AT+FCLASS=8\n", "AT+VSM=6\n", "ATS18=1\n", "ATS14=4\n", "ATS13.4=1\n", "ATS13.6=1\n", "ATS23=1\n" };

    private static String m_OK = "OK";

    private String m_device;

    private InputStream m_InputStream;

    public TtyIHandler(String device) {
        m_device = device;
        try {
            m_fout = new FileWriter(m_device);
            FileReader fin = new FileReader(m_device);
            m_bfin = new BufferedReader(fin);
            initDevice();
            m_InputStream = new FileInputStream(device);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
   * sets the msn for /dev/ttyI
   * @param msn String : the required msn
   * @throws IOException
   */
    public void setMsn(String msn) throws IOException {
        String command = "AT&E" + msn + "\n";
        write(command);
    }

    /**
   * returns an input stream used to read from this device
   * @return InputStream
   */
    public InputStream getInputStream() {
        return m_InputStream;
    }

    /**
   * inits /dev/ttyI by sending all the commands in m_init array
   * @throws IOException
   */
    private void initDevice() throws IOException {
        for (int i = 0; i < m_init.length; i++) {
            write(m_init[i]);
        }
    }

    /**
   * writes a command to /dev/ttyI
   * @param command String
   * @throws IOException
   */
    private void write(String command) throws IOException {
        m_fout.write(command);
        m_fout.flush();
        getOK();
    }

    public void answer() throws IOException {
        write("ATH\n");
    }

    /**
   * reads data from ttyI, until an OK found
   * @throws IOException
   */
    private void getOK() throws IOException {
        while (!(m_bfin.readLine().equals(m_OK))) ;
    }

    public static String getTtyIDeviceName() {
        return "/dev/ttyI0";
    }
}

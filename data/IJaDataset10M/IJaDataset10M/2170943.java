package kernel;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import comm.ChannelInterface;
import comm.SerialManager;
import data.DataPacket;
import gui.app.AppInterface;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import kml.KMLManager;
import net.udp.server.UDPServer;

/**
 *
 * @author David Escobar Sanabria
 */
public class Kernel extends Thread implements ChannelInterface, KernelInterface {

    private UDPServer udpServer = null;

    private SerialManager serialManager = null;

    private DataPacket dataPacket = null;

    private Vector vectorData = null;

    AppInterface gui;

    private int countPackets = 0;

    private int countPacketsCheck = 0;

    private String tempFile = null;

    private boolean runFlag = true;

    private int dataPeriod = 1000;

    private KMLManager kmlManager;

    /**
     *
     * @param _gui
     */
    public Kernel(AppInterface _gui) {
        gui = _gui;
        dataPacket = new DataPacket();
        reconnect(gui.isUDP());
        System.out.println("Software developed by David Escobar Sanabria");
        System.out.println("UAV research group, Aerospace Engineering and Mechanics, University of Minnesota");
        tempFile = "tempData.data";
        deleteFile(tempFile);
        start();
    }

    public void run() {
        while (runFlag) {
            try {
                Thread.sleep(dataPeriod + 500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (countPackets == countPacketsCheck) {
                gui.setMode((int) (0x0000));
            }
            countPacketsCheck = countPackets;
        }
    }

    public void joutln(String st, boolean b) {
        gui.joutln(st, b);
    }

    public void joutln(String st) {
        gui.joutln(st);
    }

    public void sendSerial(String st) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDataIn(String st) {
    }

    /**
     *
     * @param isUDP
     */
    public void reconnect(boolean isUDP) {
        try {
            udpServer.closeConnection();
            udpServer.stop();
            udpServer = null;
        } catch (Exception e) {
        }
        try {
            serialManager.closeConnection();
            serialManager.stop();
            serialManager = null;
        } catch (Exception e) {
        }
        if (isUDP) {
            try {
                udpServer = new UDPServer(Integer.parseInt(gui.getUDPPort()), this);
            } catch (SocketException ex) {
                Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            serialManager = new SerialManager(this, gui.getSerialPort(), gui.getRate(), gui.getFlowControl()[0], gui.getFlowControl()[1], gui.getDataBits(), gui.getStopBits(), gui.getParidad());
        }
    }

    public void setSerialPortAvaliable(boolean b) {
        gui.enableSerial(b);
    }

    public void setDataIn(int[] intArray) {
        dataPacket.setDataIntArray(intArray);
        countPackets++;
        if (dataPacket.isCheckSum()) {
            if (vectorData == null) {
                vectorData = new Vector(100);
            }
            vectorData.add(dataPacket.getDataArray());
            if (vectorData.size() == 500) {
                saveVectorData();
            }
            try {
                gui.setAirSpeed(dataPacket.getIAS(), dataPacket.getIas_ref());
                gui.setHeading(dataPacket.getPsi());
                gui.setPitchAndBank(-1 * dataPacket.getTheta(), -1 * dataPacket.getPhi());
                gui.setTime(dataPacket.getTime());
                int auxMode = (~(((int) dataPacket.getFlight_mode()) | 0xFE3F)) | (((int) dataPacket.getFlight_mode()) & 0xFE3F);
                gui.setMode((int) (0x0200 | auxMode));
                gui.setCPULoad(dataPacket.getCpuload());
                gui.setAltLonLat(dataPacket.getAltitude(), dataPacket.getLongitud(), dataPacket.getLatitud());
                gui.setAileron(dataPacket.getAileron());
                gui.setElevetor(dataPacket.getElevator());
                gui.setThrottle(100 * dataPacket.getThrottle());
                gui.setRudder(dataPacket.getRudder());
            } catch (Exception e) {
                System.out.println("Problems while painting GUI");
            }
        } else {
            gui.setMode((int) (0xFEFF & (int) dataPacket.getFlight_mode()));
            System.out.println("Data checksum Failed");
        }
    }

    /**
     *
     */
    public void quit() {
        try {
            serialManager.closeConnection();
            serialManager.stop();
        } catch (Exception e) {
            System.out.println("It is not possible to close the serial connection");
        }
        try {
            udpServer.closeConnection();
        } catch (Exception e) {
            System.out.println("It is not possible to close the UDP connection");
        }
    }

    private void saveVectorData() {
        Vector tempVector = (Vector) openObjectFile(tempFile);
        if (tempVector != null) {
            tempVector.addAll(vectorData);
        } else {
            tempVector = vectorData;
        }
        saveObjectFile(tempFile, tempVector);
        vectorData.removeAllElements();
    }

    /**
     *
     * @param path
     */
    public void deleteFile(String path) {
        File file = new File(path);
        try {
            file.delete();
        } catch (Exception e) {
        }
    }

    /**
     *
     * @param path
     * @param stData
     */
    public void saveFile(String path, String stData) {
        File file = new File(path);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
        } catch (IOException ex) {
            Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedWriter textOut = new BufferedWriter(fw);
        try {
            textOut.write(stData);
        } catch (IOException ex) {
            Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param path
     * @param obj
     */
    public void saveObjectFile(String path, Object obj) {
        File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
        } catch (IOException ex) {
        }
        try {
            oos.writeObject(obj);
        } catch (IOException ex) {
        }
    }

    /**
     *
     * @param path
     * @return
     */
    public Object openObjectFile(String path) {
        Object returnObject = null;
        File file = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (Exception e) {
        }
        ObjectInputStream ois = null;
        if (fis != null) {
            try {
                ois = new ObjectInputStream(fis);
            } catch (IOException ex) {
            }
        }
        try {
            returnObject = ois.readObject();
        } catch (Exception e) {
        }
        return returnObject;
    }

    /**
     *
     * @param file
     */
    public void genMatFile(File file) {
        String MfilePath = file.getPath();
        Vector vData = (Vector) openObjectFile(tempFile);
        if (vData != null) {
            vData.addAll(vectorData);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
            }
            double[][] matrix = getMatrix(vData);
            MLDouble mlDouble = new MLDouble(file.getName().substring(0, file.getName().length() - 4), matrix);
            ArrayList list = new ArrayList();
            list.add(mlDouble);
            try {
                new MatFileWriter(MfilePath, list);
            } catch (IOException ex) {
                Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
            }
            joutln("Data saved sucessfully");
        } else {
            if (vectorData != null) {
                if (vectorData.size() > 0) {
                    vData = vectorData;
                    double[][] matrix = getMatrix(vData);
                    MLDouble mlDouble = new MLDouble(file.getName().substring(0, file.getName().length() - 4), matrix);
                    ArrayList list = new ArrayList();
                    list.add(mlDouble);
                    try {
                        new MatFileWriter(MfilePath, list);
                    } catch (IOException ex) {
                        Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    joutln("Data saved sucessfully");
                }
            } else {
                joutln("No Data avaliable", true);
            }
        }
    }

    /**
     *
     * @param vData
     * @return
     */
    public double[][] getMatrix(Vector vData) {
        double[][] matrix = new double[vData.size()][((double[]) vData.elementAt(0)).length];
        for (int i = 0; i < vData.size(); i++) {
            matrix[i] = (double[]) vData.elementAt(i);
        }
        return matrix;
    }

    /**
     *
     * @param MfilePath
     */
    public void genMFile(final String MfilePath) {
        Vector vData = (Vector) openObjectFile(tempFile);
        if (vData != null) {
            vData.addAll(vectorData);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
            }
            String stMFile = getStringMFile(vData);
            saveFile(MfilePath, stMFile);
            joutln("Data saved sucessfully");
        } else {
            if (vectorData != null) {
                if (vectorData.size() > 0) {
                    vData = vectorData;
                    String stMFile = getStringMFile(vData);
                    saveFile(MfilePath, stMFile);
                    joutln("Data saved sucessfully");
                }
            } else {
                joutln("No Data avaliable", true);
            }
        }
    }

    /**
     *
     * @param vData
     * @return
     */
    public String getStringMFile(Vector vData) {
        String mFile = "data=[\n";
        for (int i = 0; i < vData.size(); i++) {
            double[] array = null;
            try {
                array = (double[]) vData.elementAt(i);
            } catch (Exception e) {
            }
            if (array != null) {
                for (int j = 0; j < array.length; j++) {
                    mFile = mFile + "" + array[j] + " ";
                }
                mFile = mFile + "\n";
            }
        }
        mFile = mFile + "]";
        return mFile;
    }

    public void setKMLPath(File file, int period) {
        System.out.println("KML ok");
        kmlManager = null;
        kmlManager = new KMLManager(this, file, period);
    }

    public double getLongitud() {
        return dataPacket.getLongitud();
    }

    public double getLatitud() {
        return dataPacket.getLatitud();
    }

    public double getAltitude() {
        return dataPacket.getAltitude();
    }

    public void kmlStop() {
        kmlManager.setBreak(true);
        kmlManager.stop();
    }
}

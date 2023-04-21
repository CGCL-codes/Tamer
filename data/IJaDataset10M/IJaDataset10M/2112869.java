package comm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Clase que gestiona la conexión con puertos seriales del PC o interfaz.
 * @author David Escobar Sanabria
 */
public class SerialManager extends Thread {

    private SerialParameters parameters;

    /**
     *Objeto de la clase SerialConnection que gestiona la conexio n con el puerto serial.
     */
    private SerialConnection connection;

    private ChannelInterface manager;

    private int tamTramaOut = 0;

    private int tamTramaIn = 0;

    private String stringPort;

    private String stringRate;

    private String stFlowCIn;

    private String stFlowCOut;

    private String stDataBits;

    private String stStopBits;

    private String stParidad;

    /**
     *Constructor de la clase.
     * @param _manager Objeto que implementa la interface KernelInterface que conecta
     * el nucleo del sistema para transmisión de informacion a este. 
     */
    public SerialManager(ChannelInterface _manager) {
        manager = _manager;
        this.start();
    }

    /**
     *Constructor de la clase.
     * @param _manager Objeto que implementa la interface KernelInterface que conecta
     * el nucleo del sistema para transmisión de informacion a este.
     * @param _stringPort String con puerto al cual se realizara la conexion.
     * @param _stringRate String con velocidad de transmision en bits por segundo
     * @param _stFlowCIn String que relaciona control de flujo-entrada.
     * @param _stFlowCOut String que relaciona control de flujo-salida.
     * @param _stDataBits String que relaciona numero de bits de datos.
     * @param _stStopBits String que relaciona numero de bits de parada.
     * @param _stParidad String que relaciona la paridad.
     */
    public SerialManager(ChannelInterface _manager, String _stringPort, String _stringRate, String _stFlowCIn, String _stFlowCOut, String _stDataBits, String _stStopBits, String _stParidad) {
        manager = _manager;
        this.stringPort = _stringPort;
        this.stringRate = _stringRate;
        this.stFlowCIn = _stFlowCIn;
        this.stFlowCOut = _stFlowCOut;
        this.stDataBits = _stDataBits;
        this.stStopBits = _stStopBits;
        this.stParidad = _stParidad;
        start();
    }

    /**
     *Ejecuta configuracion y ejecucion de serialConnection en hilo.
     */
    @Override
    public void run() {
        configSerial();
    }

    /**
     *Metodo para configuracion de conexión con puerto serial.
     */
    public void configSerial() {
        parameters = new SerialParameters();
        setParameters();
        connection = new SerialConnection(this, parameters);
        try {
            connection.openConnection();
            manager.joutln("Serial communication initialized !");
        } catch (SerialConnectionException e2) {
            manager.joutln("Error: Can't open the port", true);
            manager.setSerialPortAvaliable(true);
            return;
        }
    }

    /**
     *Metodo que inicializa parametros de conexión con puerto serial.
     */
    public void setParameters() {
        parameters.setPortName(this.stringPort);
        parameters.setBaudRate(this.stringRate);
        parameters.setFlowControlIn(this.stFlowCIn);
        parameters.setFlowControlOut(this.stFlowCOut);
        parameters.setDatabits(this.stDataBits);
        parameters.setStopbits(this.stStopBits);
        parameters.setParity(this.stParidad);
    }

    /**
     *Metodo que cierra la conexión con puerto serial.
     */
    public void serialExit() {
        this.connection.closeConnection();
    }

    /**
     *Metodo que envia un arreglo de enteros por puerto serial.
     * @param data Arreglo de enteros.
     */
    public void sendData(int[] data) {
        System.out.println();
        for (int i = 0; i < data.length; i++) {
            this.connection.writePort(data[i]);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *Metodo que envia un String a traves del puerto serial.
     * @param st String que se envia.
     */
    public void sendData(String st) {
        for (int i = 0; i < st.length(); i++) {
            this.connection.writePort(st.charAt(i));
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *metodo que asigna String que se lee en puerto serial para ser procesado.
     * @param st String leido en puerto serial.
     */
    public void setDataInSerial(String st) {
        manager.setDataIn(st);
    }

    /**
     *
     * @param intArray
     */
    public void setDataInSerial(int[] intArray) {
        manager.setDataIn(intArray);
    }

    /**
     *
     */
    public void closeConnection() {
        connection.closeConnection();
    }
}

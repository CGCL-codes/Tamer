package es.unizar.cps.tecnoDiscap.io;

import es.unizar.cps.tecnoDiscap.i18n.Messages;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.TooManyListenersException;

public class SerialCommunication implements SerialPortEventListener {

    private InputStream inputStream;

    private SerialPort serialPort;

    private static Enumeration portList;

    private static OutputStream outputStream;

    private boolean portFound = false;

    private SerialConector conector;

    private List puertos;

    private int tamagnoBuffer = 128;

    public SerialCommunication(SerialConector conector) {
        this.conector = conector;
        initSerial();
    }

    private void initSerial() {
        puertos = new LinkedList();
        CommPortIdentifier portId;
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                try {
                    serialPort = (SerialPort) portId.open("lee", 2000);
                    System.out.println(Messages.getString("io.SerialCommunication.1") + portId.getName());
                    puertos.add(serialPort);
                    inputStream = serialPort.getInputStream();
                    serialPort.addEventListener(this);
                    serialPort.notifyOnDataAvailable(true);
                    serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    initWriteToPort();
                    byte bArray[] = { 0x23, 0x03, 0x40 };
                    writeToPort(bArray);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TooManyListenersException e2) {
                    e2.printStackTrace();
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                } catch (PortInUseException e) {
                    System.out.println(Messages.getString("io.SerialCommunication.2") + portId.getName());
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public void serialEvent(SerialPortEvent event) {
        if (this.portFound == false) {
            this.serialPort = (SerialPort) event.getSource();
            System.out.println(Messages.getString("io.SerialCommunication.3") + serialPort.getName());
            try {
                inputStream = this.serialPort.getInputStream();
                outputStream = this.serialPort.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            byte[] readBuffer = new byte[this.tamagnoBuffer];
            int numBytes = 0;
            try {
                while (inputStream.available() > 0) {
                    numBytes = inputStream.read(readBuffer) * 2;
                }
                int[] hexadecimales = new int[numBytes / 2];
                for (int i = 0; i < numBytes / 2; i++) {
                    hexadecimales[i] |= readBuffer[i] & 0xFF;
                }
                if (this.portFound != false) {
                    conector.procesaCadena(hexadecimales);
                }
            } catch (IOException e) {
            }
        }
        if (this.portFound == false) {
            this.portFound = true;
        }
    }

    public void initWriteToPort() {
        try {
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param comando
	 * @param adr
	 */
    public void writeToPort(byte[] bArray) {
        initWriteToPort();
        try {
            System.out.println(Messages.getString("io.SerialCommunication.4") + serialPort.getName() + Messages.getString("io.SerialCommunication.5") + bArray);
            outputStream.write(bArray);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void closeSerialUSBPort() {
        if (serialPort != null) {
            System.out.println(Messages.getString("io.SerialCommunication.6") + serialPort.getName());
            serialPort.close();
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serialPort = null;
            this.portFound = false;
        } else {
            System.out.println(Messages.getString("io.SerialCommunication.7"));
        }
    }

    public String toHexadecimal(int[] datos) {
        String resultado = "";
        String cadAux;
        for (int i = 0; i < datos.length; i++) {
            cadAux = Integer.toHexString(datos[i]);
            if (cadAux.length() < 2) resultado += "0";
            resultado += cadAux;
        }
        return resultado;
    }

    /**
	 * @param hexStr
	 * @return bArray
	 */
    public byte[] toBinArray(String hexStr) {
        byte bArray[] = new byte[hexStr.length() / 2];
        for (int i = 0; i < (hexStr.length() / 2); i++) {
            byte firstNibble = Byte.parseByte(hexStr.substring(2 * i, 2 * i + 1), 16);
            byte secondNibble = Byte.parseByte(hexStr.substring(2 * i + 1, 2 * i + 2), 16);
            int finalByte = (secondNibble) | (firstNibble << 4);
            bArray[i] = (byte) finalByte;
        }
        return bArray;
    }

    public boolean isPortFound() {
        return portFound;
    }

    public void liberalizaPuertosNoUsados() {
        for (int i = 0; i < puertos.size(); i++) {
            SerialPort serialAux = (SerialPort) puertos.get(i);
            if (serialPort != serialAux) {
                System.out.println(Messages.getString("io.SerialCommunication.10") + serialAux.getName());
                serialAux.close();
                puertos.remove(serialAux);
                i--;
            }
        }
    }
}

package smsidserver;

import java.net.*;
import java.io.*;
import javax.comm.*;

public class FalcomModemInterface extends SMSModemInterface implements SerialPortEventListener {

    private OutputStream os;

    private BufferedReader in;

    private SerialPort sp;

    public FalcomModemInterface(String portName, SMSIDServer s) {
        super(portName, s);
        os = null;
        in = null;
        sp = null;
    }

    protected boolean initialise() {
        try {
            CommPortIdentifier commPort = CommPortIdentifier.getPortIdentifier(port);
            sp = (SerialPort) commPort.open("SMSID", 10);
            os = sp.getOutputStream();
            in = new BufferedReader(new InputStreamReader(sp.getInputStream()));
            sp.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            sp.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_OUT);
            sp.enableReceiveTimeout(5000);
            send("at");
            if (waitfor("OK", 5000)) {
                if (unlockPin("0000")) {
                    if (modemReady()) {
                        send("AT+CMGF=0");
                        send("AT+CNMI=2,1");
                        for (int i = 1; i < 11; i++) deleteMessage(i);
                    } else {
                        System.out.println("Modem not connected to GSM network.");
                        return (false);
                    }
                } else {
                    System.out.println("Could not unlock SIM/PIN.");
                    return (false);
                }
            } else {
                System.out.println("Modem not responding.");
                return (false);
            }
        } catch (NoSuchPortException e) {
            System.out.println("Port does not exist");
            return (false);
        } catch (Exception e) {
            System.out.println("Caught exception while setting up serial port");
            e.printStackTrace();
        }
        return (true);
    }

    public void serialEvent(SerialPortEvent ev) {
    }

    private void deleteMessage(int i) {
        send("AT+CMGD=" + Integer.toString(i));
    }

    private boolean unlockPin(String pin) {
        for (int i = 0; i < 2; i++) {
            send("AT+CPIN?");
            String response = getResponse(1000);
            if (response.endsWith("READY")) {
                return (true);
            } else {
                send("AT+CPIN=" + pin);
                response = getResponse(1000);
            }
        }
        return (false);
    }

    private boolean modemReady() {
        for (int i = 0; i < 4; i++) {
            send("AT+CREG?");
            String response = getResponse(1000);
            if (response.endsWith("1,1")) {
                return (true);
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
        }
        return (false);
    }

    private void send(String str) {
        try {
            os.write(str.getBytes());
            os.write('\r');
            waitfor(str, 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean waitfor(String str, int to) {
        String line;
        boolean found = false;
        try {
            sp.enableReceiveTimeout(to);
            while (!found) {
                line = in.readLine();
                if (line == null) {
                    return (false);
                }
                if (str.equals(line)) {
                    found = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return (found);
        }
    }

    private String getResponse(int to) {
        String line = "";
        boolean found = false;
        try {
            sp.enableReceiveTimeout(to);
            while (!found) {
                line = in.readLine();
                if (line == null) {
                    return ("");
                }
                if (line.length() > 0) {
                    found = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            line = "";
        } finally {
            return (line);
        }
    }

    protected boolean process() {
        try {
            String line = in.readLine();
            if (line != null) {
                System.out.println(line);
                if (line.startsWith("+CMTI")) {
                    int p = line.indexOf(',');
                    if (p != -1) {
                        retrieveSMS(line.substring(p + 1));
                    }
                }
            }
        } catch (IOException e) {
        }
        return (false);
    }

    private void retrieveSMS(String memory) {
        send("AT+CMGR=" + memory);
        String response = getResponse(500);
        if (response != null) {
            if (response.equals("ERROR")) {
            } else {
                response = getResponse(500);
                if (response != null) {
                    System.out.println(response);
                }
            }
        }
    }

    protected void cleanUp() {
        if (sp != null) {
            sp.close();
            sp = null;
        }
    }
}

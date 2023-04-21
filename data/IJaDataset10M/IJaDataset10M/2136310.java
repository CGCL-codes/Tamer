package de.tfh.pdvl.hp.serialServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import de.tfh.pdvl.hp.connections.SerialConnection;

/**
 * @author s717689
 *
 */
public class HpInterface {

    private SerialConnection conn;

    private static final String ERROR_NOERROR = "+0,\"No error\"";

    private static final String ERROR_CONFLICT = "-221,\"Settings conflict";

    private static final String QUERY_ERROR = "SYST:ERR?\n";

    private String returnString = null;

    private String error = null;

    private boolean debug = false;

    public HpInterface() throws FileNotFoundException, IOException, SerialServerException {
        if (SerialServerProperties.getInstance().getDebugLevel() > 2) {
            debug = true;
        }
        conn = new SerialConnection(SerialServerProperties.getInstance().getSerialPort());
        resetError();
    }

    private void resetError() throws SerialServerException {
        int i = 0;
        String retStr;
        if (debug) {
            System.err.print("reset error buffer:");
        }
        try {
            do {
                conn.write(QUERY_ERROR);
                Thread.sleep(500);
                i++;
                if (i == 30) {
                    System.err.println("\nERR: serial connection failed");
                    throw new SerialServerException("serial connection failed");
                }
                retStr = conn.read().trim();
                if (debug) System.err.print(".");
            } while (!retStr.equals(ERROR_NOERROR));
        } catch (IOException e) {
            throw new SerialServerException("serial connection failed", e);
        } catch (InterruptedException e) {
            throw new SerialServerException("serial connection failed", e);
        }
        if (debug) System.out.println("\n");
    }

    public void sendCommand(SCPICommand cmd) throws SerialServerException {
        try {
            conn.write(cmd.toString());
            Thread.sleep(500);
            if (cmd.getCommandType() != SCPICommandType.SET) {
                returnString = conn.read().trim();
                if (cmd instanceof StringCommand) {
                    ((StringCommand) cmd).setParameter(returnString);
                } else if (cmd instanceof ValueCommand) {
                    ((ValueCommand) cmd).setParameter(parseAnswer(returnString));
                }
            }
            conn.write(QUERY_ERROR);
            Thread.sleep(100);
            error = conn.read().trim();
            if ((!error.equals(ERROR_NOERROR)) && (!error.startsWith(ERROR_CONFLICT))) {
                resetError();
                throw new SerialServerException("serial connection failed");
            }
        } catch (IOException e) {
            throw new SerialServerException("serial connection failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SerialServerException("serial connection failed", e);
        }
        return;
    }

    private double parseAnswer(String answerString) throws SerialServerException {
        double val;
        try {
            val = Double.parseDouble(returnString);
            return val;
        } catch (NumberFormatException nfe) {
            throw new SerialServerException("serial connection failed", nfe);
        }
    }

    public double executeValueQuery(String cmd) {
        return 0;
    }

    public String getError() {
        return error;
    }
}

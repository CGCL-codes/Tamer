package jSXtester;

/**
 * <p>
 * Title: SX tester
 * </p>
 * 
 * <p>
 * Description: tester voor de SX bus
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: GSoft
 * </p>
 * 
 * @author G. vd. Sel
 * @version 1.0
 */
public class srcpdProcess {

    private int iBus;

    private String errorString;

    private boolean State;

    private int Input;

    public srcpdProcess(int Bus) {
        errorString = "";
        Input = 0;
        State = false;
        this.iBus = Bus;
    }

    public String getErrorReport() {
        return errorString;
    }

    public int getInput() {
        return Input;
    }

    public boolean getState() {
        return State;
    }

    public int executeNormal(String[] Commands) {
        int result;
        result = -1;
        if (Commands[1].startsWith("1") && Commands[2].equalsIgnoreCase("INFO")) {
            if (Commands[4].equalsIgnoreCase("GL")) {
                result = 1;
            } else if (Commands[4].equalsIgnoreCase("GA")) {
                Input = Integer.parseInt(Commands[5]);
                State = !Commands[6].equalsIgnoreCase("0");
                result = 2;
            } else if (Commands[4].equalsIgnoreCase("FB")) {
                Input = Integer.parseInt(Commands[5]);
                State = !Commands[6].equalsIgnoreCase("0");
                result = 3;
            }
        } else if (Commands[1].startsWith("2") && Commands[2].equalsIgnoreCase("OK")) {
            result = 0;
        }
        return result;
    }

    public boolean errorCheck(int commandNumber) {
        switch(commandNumber) {
            case 100:
                errorString = "Info device.";
                break;
            case 101:
                errorString = "Info initialisation.";
                break;
            case 102:
                errorString = "Info termination.";
                break;
            case 200:
                errorString = "Oke received (GO).";
                break;
            case 201:
                errorString = "Oke received (Protocol).";
                break;
            case 202:
                errorString = "Oke received (Connectionmode).";
                break;
            case 400:
                errorString = "Unsupported protocol.";
                break;
            case 401:
                errorString = "Unsupported connection mode.";
                break;
            case 402:
                errorString = "Unsufficient data.";
                break;
            case 410:
                errorString = "Unknown command.";
                break;
            case 411:
                errorString = "Unknown value.";
                break;
            case 412:
                errorString = "Wrong value.";
                break;
            case 413:
                errorString = "Temporarily prohibited.";
                break;
            case 414:
                errorString = "Device locked.";
                break;
            case 415:
                errorString = "Forbidden.";
                break;
            case 416:
                errorString = "No data.";
                break;
            case 417:
                errorString = "Timeout.";
                break;
            case 418:
                errorString = "List to long.";
                break;
            case 419:
                errorString = "List to short.";
                break;
            case 420:
                errorString = "Unsupported divice protocol.";
                break;
            case 421:
                errorString = "Unsupported divice.";
                break;
            case 422:
                errorString = "Unsupported divice group.";
                break;
            case 423:
                errorString = "Unsupported operation.";
                break;
            case 424:
                errorString = "Device reinitialised.";
                break;
            case 499:
                errorString = "Unspecified error.";
                break;
            case 500:
                errorString = "Out of ressources.";
                break;
            default:
                errorString = "Unknown errorcode.";
                break;
        }
        return (commandNumber < 400);
    }

    public String setPower(int OnOff) {
        String Power;
        Power = "SET " + Integer.toString(iBus) + " POWER ";
        switch(OnOff) {
            case 0:
                Power = Power + "OFF";
                break;
            case 1:
                Power = Power + "ON";
                break;
            default:
                Power = Power + "OFF";
                break;
        }
        return Power;
    }

    public String initTrein(int Adres) {
        String initEngine;
        initEngine = "INIT " + Integer.toString(iBus) + " GL ";
        initEngine = initEngine + Integer.toString(Adres) + " S 1 31 2";
        return initEngine;
    }

    public String initAccessory(int Adres) {
        String initAssessoire;
        initAssessoire = "INIT " + Integer.toString(iBus) + " GA ";
        initAssessoire = initAssessoire + Integer.toString(Adres) + " S";
        return initAssessoire;
    }

    public String initFeedback() {
        String initFeedback;
        initFeedback = "INIT " + Integer.toString(iBus) + " FB ";
        initFeedback = initFeedback + " -1 S 0";
        return initFeedback;
    }

    public String initFeedback(int Adres, int Index) {
        String initFeedback;
        initFeedback = "INIT " + Integer.toString(iBus) + " FB ";
        initFeedback = initFeedback + Integer.toString(Adres) + " S ";
        initFeedback = initFeedback + Integer.toString(Index);
        return initFeedback;
    }

    public String termTrein(int Address) {
        String termEngine;
        termEngine = "TERM " + Integer.toString(iBus) + " GL ";
        termEngine = termEngine + Integer.toString(Address);
        return termEngine;
    }

    public String makeTrein(int Adres, int Rijrichting, int Speed, int Light, int Function) {
        String comEngine;
        comEngine = "SET " + Integer.toString(iBus) + " GL ";
        comEngine = comEngine + Integer.toString(Adres) + " ";
        comEngine = comEngine + Integer.toString(Rijrichting) + " ";
        comEngine = comEngine + Integer.toString(Speed) + " 31 ";
        comEngine = comEngine + Integer.toString(Light) + " ";
        comEngine = comEngine + Integer.toString(Function);
        return comEngine;
    }

    public String makeAccessory(int Adres, int Bit, int Value) {
        String comAccessory;
        comAccessory = "SET " + Integer.toString(iBus) + " GA ";
        comAccessory = comAccessory + Integer.toString(Adres) + " ";
        comAccessory = comAccessory + Integer.toString(Bit) + " ";
        comAccessory = comAccessory + Integer.toString(Value) + " 0";
        return comAccessory;
    }

    public String makeFeedback(int Bit) {
        String comFeedback;
        comFeedback = "GET " + Integer.toString(iBus) + " FB ";
        comFeedback = comFeedback + Integer.toString(Bit);
        return comFeedback;
    }
}

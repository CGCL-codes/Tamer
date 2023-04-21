package atmon;

import java.awt.*;

public class FuseLockBitsCodec {

    private static final Color HEADER_CL = new Color(0xFF3333);

    private static final Color ITEM_CL = Color.DARK_GRAY;

    private MainFrame mf;

    public FuseLockBitsCodec(MainFrame mf, Comm comm) {
        this.mf = mf;
    }

    public void dispFLBits(byte[] bits) {
        if ((bits == null) || (bits.length < 4)) {
            System.err.println("Fuses / lock bits not read");
            return;
        }
        int locks = Utils.ub(bits[1]);
        int fusesL = Utils.ub(bits[0]);
        int fusesH = Utils.ub(bits[3]);
        dispLockBits(locks);
        dispFusesH(fusesH);
        dispFusesL(fusesL);
    }

    private void dispLockBits(int locks) {
        String s;
        mf.showMsgLn("Lock bits: [" + Utils.byteToHex(locks) + "]", HEADER_CL);
        switch(locks & 3) {
            case 3:
                s = "No memory lock features enabled";
                break;
            case 2:
                s = "Further programming of the Flash and EEPROM is disabled in Parallel and SPI/JTAG Serial Programming mode. The Fuse bits are locked in both Serial and Parallel Programming mode";
                break;
            case 1:
                s = "[Invalid lock bits combination]";
                break;
            case 0:
                s = "Further programming and verification of the Flash and EEPROM is disabled in Parallel and SPI/JTAG Serial Programming mode. The Fuse bits are locked in both Serial and Parallel Programming mode";
                break;
            default:
                s = "[Internal error]";
        }
        mf.showMsgLn(">" + s, ITEM_CL);
        switch((locks >>> 2) & 3) {
            case 3:
                s = "No restrictions for SPM or LPM accessing the Application section";
                break;
            case 2:
                s = "SPM is not allowed to write to the Application section";
                break;
            case 1:
                s = "LPM executing from the Boot Loader section is not allowed to read from the Application section. If interrupt vectors are placed in the Boot Loader section, interrupts are disabled while executing from the Application section";
                break;
            case 0:
                s = "SPM is not allowed to write to the Application section, and LPM executing from the Boot Loader section is not allowed to read from the Application section. If interrupt vectors are placed in the Boot Loader section, interrupts are disabled while executing from the Application section";
                break;
            default:
                s = "[Internal error]";
        }
        mf.showMsgLn(">" + s, ITEM_CL);
        switch((locks >>> 4) & 3) {
            case 3:
                s = "No restrictions for SPM or LPM accessing the Boot Loader section";
                break;
            case 2:
                s = "SPM is not allowed to write to the Boot Loader section";
                break;
            case 1:
                s = "LPM executing from the Application section is not allowed to read from the Boot Loader section. If interrupt vectors are placed in the Application section, interrupts are disabled while executing from the Boot Loader section";
                break;
            case 0:
                s = "SPM is not allowed to write to the Boot Loader section, and LPM executing from the Application section is not allowed to read from the Boot Loader section. If interrupt vectors are placed in the Application section, interrupts are disabled while executing from the Boot Loader section";
                break;
            default:
                s = "[Internal error]";
        }
        mf.showMsgLn(">" + s, ITEM_CL);
    }

    private void dispFusesH(int fusesH) {
        String s;
        mf.showMsgLn("Fuses high: [" + Utils.byteToHex(fusesH) + "]", HEADER_CL);
        mf.showMsgLn(">Enable OCD" + fuseToBoolean(fusesH, 7), ITEM_CL);
        mf.showMsgLn(">Enable JTAG" + fuseToBoolean(fusesH, 6), ITEM_CL);
        mf.showMsgLn(">Enable SPI Serial Program and Data Downloading" + fuseToBoolean(fusesH, 5), ITEM_CL);
        mf.showMsgLn(">Oscillator options" + fuseToBoolean(fusesH, 4), ITEM_CL);
        mf.showMsgLn(">EEPROM memory is preserved through the Chip Erase" + fuseToBoolean(fusesH, 3), ITEM_CL);
        switch((fusesH >>> 1) & 3) {
            case 3:
                s = "256";
                break;
            case 2:
                s = "512";
                break;
            case 1:
                s = "1024";
                break;
            case 0:
                s = "2048";
                break;
            default:
                s = "[Internal error]";
        }
        mf.showMsgLn(">Boot size: " + s + " words", ITEM_CL);
        mf.showMsgLn(">Select reset vector: " + (((fusesH & (1 << 0)) != 0) ? "normal" : "boot"), ITEM_CL);
    }

    private void dispFusesL(int fusesL) {
        String s;
        mf.showMsgLn("Fuses low: [" + Utils.byteToHex(fusesL) + "]", HEADER_CL);
        mf.showMsgLn(">Brown-out Detector trigger level: " + (((fusesL & (1 << 7)) != 0) ? "2.7V" : "4.0V"), ITEM_CL);
        mf.showMsgLn(">Brown-out Detector enable" + fuseToBoolean(fusesL, 6), ITEM_CL);
        boolean anotherSUT = false, yetAnotherSUT = false;
        switch(fusesL & 0xF) {
            case 0:
                s = "External Clock";
                break;
            case 1:
                s = "Internal Calibrated RC Oscillator 1.0 MHz";
                break;
            case 2:
                s = "Internal Calibrated RC Oscillator 2.0 MHz";
                break;
            case 3:
                s = "Internal Calibrated RC Oscillator 4.0 MHz";
                break;
            case 4:
                s = "Internal Calibrated RC Oscillator 8.0 MHz";
                break;
            case 5:
                s = "External RC Oscillator ? 0.9 MHz";
                break;
            case 6:
                s = "External RC Oscillator 0.9 - 3.0 MHz";
                break;
            case 7:
                s = "External RC Oscillator 3.0 - 8.0 MHz";
                break;
            case 8:
                s = "External RC Oscillator 8.0 - 12.0 MHz";
                break;
            case 9:
                s = "Low-frequency Crystal Oscillator";
                yetAnotherSUT = true;
                break;
            case 10:
            case 11:
                s = "Crystal Oscillator 0.4 - 0.9 MHz";
                anotherSUT = true;
                break;
            case 12:
            case 13:
                s = "Crystal Oscillator 0.9 - 3.0 MHz";
                anotherSUT = true;
                break;
            case 14:
            case 15:
                s = "Crystal Oscillator 3.0 - 8.0 MHz";
                anotherSUT = true;
                break;
            default:
                s = "[Internal error]";
        }
        mf.showMsgLn(">Clock source: " + s, ITEM_CL);
        if (yetAnotherSUT) {
            switch((fusesL >>> 4) & 3) {
                case 0:
                    s = "4.1 ms";
                    break;
                case 1:
                    s = "65 ms";
                    break;
                case 2:
                    s = "65 ms";
                    break;
                case 3:
                    s = "Reserved";
                    break;
                default:
                    s = "[Internal error]";
            }
        } else if (anotherSUT) {
            boolean cksel0 = (fusesL & 1) != 0;
            switch((fusesL >>> 4) & 3) {
                case 0:
                    if (cksel0) {
                        s = "1K ck + 65 ms";
                    } else {
                        s = "258 ck + 4.1 ms";
                    }
                    break;
                case 1:
                    if (cksel0) {
                        s = "16K ck + 0 ms";
                    } else {
                        s = "258 ck + 65 ms";
                    }
                    break;
                case 2:
                    if (cksel0) {
                        s = "16K ck + 4.1 ms";
                    } else {
                        s = "1K ck + 0 ms";
                    }
                    break;
                case 3:
                    if (cksel0) {
                        s = "16K ck + 65 ms";
                    } else {
                        s = "1K ck + 4.1 ms";
                    }
                    break;
                default:
                    s = "[Internal error]";
            }
        } else {
            switch((fusesL >>> 4) & 3) {
                case 0:
                    s = "0 ms";
                    break;
                case 1:
                    s = "4.1 ms";
                    break;
                case 2:
                    s = "65 ms";
                    break;
                case 3:
                    s = "4.1 ms";
                    break;
                default:
                    s = "[Internal error]";
            }
        }
        mf.showMsgLn(">Start-up time: " + s, ITEM_CL);
    }

    private String fuseToBoolean(int fuse, int num) {
        if ((fuse & (1 << num)) == 0) {
            return ": ON";
        } else {
            return ": OFF";
        }
    }
}

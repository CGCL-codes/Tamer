package com.primianotucci.smartcard.iso7816;

import javax.smartcardio.ResponseAPDU;

/**
 * ISO7816 message response to text renderer
 * @author Primiano Tucci - http://www.primianotucci.com/
 */
public class ISO7816Response {

    private int sw1;

    private int sw2;

    /**
     * Constructs a response from the given SW code
     * @param iSW1 SW1
     * @param iSW2 SW1
     */
    public ISO7816Response(int iSW1, int iSW2) {
        this.sw1 = iSW1;
        this.sw2 = iSW2;
    }

    /**
     * Constructs a response from the given ResponseAPDU
     * @param iAPDU APDU response
     */
    public ISO7816Response(ResponseAPDU iAPDU) {
        this(iAPDU.getSW1(), iAPDU.getSW2());
    }

    /**
     * Check whether response is good (or error)
     * @return True: Good / False: error
     */
    public boolean isGood() {
        return (sw1 == 0x90 && sw2 == 0x00) || sw1 == 0x61;
    }

    /**
     * Retrieves a textual representation of the response
     * @return Textual representation
     */
    @Override
    public String toString() {
        return getResponseString(sw1, sw2);
    }

    /**
     * Retrieve a string representation of the response from the SW status code
     * @param sw1 SW1
     * @param sw2 SW2
     * @return Text representation
     */
    public static String getResponseString(int sw1, int sw2) {
        if (sw1 == 0x90 && sw2 == 0x00) return "No further qualification"; else if (sw1 == 0x61) return "Response bytes still available: " + (int) sw2 + " bytes"; else if (sw1 == 0x62 && sw2 == 81) return "Non-volatile memory unchanged (Part of returned data may be corrupted)"; else if (sw1 == 0x62 && sw2 == 82) return "Non-volatile memory unchanged (End of file/record reached before reading Le bytes)"; else if (sw1 == 0x62 && sw2 == 83) return "Non-volatile memory unchanged (Selected file invalidated )"; else if (sw1 == 0x62 && sw2 == 84) return "Non-volatile memory unchanged (FCI not formatted)"; else if (sw1 == 0x62) return "Non-volatile memory unchanged"; else if (sw1 == 0x63) return "Non-volatile memory changed"; else if (sw1 == 0x64) return "Non-volatile memory unchanged"; else if (sw1 == 0x65 && sw2 == 0x81) return "Memory failure"; else if (sw1 == 0x65) return "Non-volatile memory changed"; else if (sw1 == 0x67 && sw2 == 0x00) return "Wrong length"; else if (sw1 == 0x68 && sw2 == 0x81) return "Logical channel not supported"; else if (sw1 == 0x68 && sw2 == 0x82) return "Secure messaging not supported"; else if (sw1 == 0x68) return "Functions in CLA not supported"; else if (sw1 == 0x69 && sw2 == 0x81) return "Command incompatible with file structure"; else if (sw1 == 0x69 && sw2 == 0x82) return "Security status not satisfied"; else if (sw1 == 0x69 && sw2 == 0x83) return "Authentication method blocked"; else if (sw1 == 0x69 && sw2 == 0x84) return "Referenced data invalidated"; else if (sw1 == 0x69 && sw2 == 0x85) return "Conditions of use not satisfied"; else if (sw1 == 0x69 && sw2 == 0x86) return "Command not allowed (no current EF)"; else if (sw1 == 0x69 && sw2 == 0x87) return "Expected SM data objects missing"; else if (sw1 == 0x69 && sw2 == 0x88) return "SM data objects incorrect"; else if (sw1 == 0x69) return "Command not allowed (" + sw2 + ")"; else if (sw1 == 0x6A && sw2 == 0x80) return "Incorrect parameters in the data field"; else if (sw1 == 0x6A && sw2 == 0x81) return "Function not supported "; else if (sw1 == 0x6A && sw2 == 0x82) return "File not found"; else if (sw1 == 0x6A && sw2 == 0x83) return "Record not found"; else if (sw1 == 0x6A && sw2 == 0x84) return "Not enough memory space in the file"; else if (sw1 == 0x6A && sw2 == 0x85) return "Lc inconsistent with TLV structure"; else if (sw1 == 0x6A && sw2 == 0x86) return "Incorrect parameters P1-P2"; else if (sw1 == 0x6A && sw2 == 0x87) return "Lc inconsistent with P1-P2"; else if (sw1 == 0x6A && sw2 == 0x88) return "Referenced data not found "; else if (sw1 == 0x6A) return "Wrong parameter(s) P1-P2"; else if (sw1 == 0x6B && sw2 == 0x00) return "Wrong parameter(s) P1-P2"; else if (sw1 == 0x6C) return "Wrong length (Correct length: " + (int) sw2 + ")"; else if (sw1 == 0x6D && sw2 == 0x00) return "Instruction code not supported or invalid"; else if (sw1 == 0x6E && sw2 == 0x00) return "Class not supported"; else if (sw1 == 0x6F && sw2 == 0x00) return "No precise diagnosis"; else return "Unkown response (SW1: " + Integer.toString(sw1, 16) + " SW2:" + Integer.toString(sw2, 16) + ")";
    }
}

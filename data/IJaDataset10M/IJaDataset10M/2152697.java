package org.dhs.munsie.gba;

import java.io.*;
import org.dhs.munsie.utils.Hex;

public class GBAROMInfo {

    private byte[] startAddressBytes = new byte[4];

    private int startAddress = 0x00000000;

    public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
        startAddressBytes[3] = (byte) 0xEA;
        int offset = startAddress - 0x08000008;
        offset >>= 2;
        startAddressBytes[2] = (byte) ((offset >>> 16) & 0xff);
        startAddressBytes[1] = (byte) ((offset >>> 8) & 0xff);
        startAddressBytes[0] = (byte) (offset & 0xff);
    }

    private static final byte[] validNintendoLogo = { (byte) 0x24, (byte) 0xFF, (byte) 0xAE, (byte) 0x51, (byte) 0x69, (byte) 0x9A, (byte) 0xA2, (byte) 0x21, (byte) 0x3D, (byte) 0x84, (byte) 0x82, (byte) 0x0A, (byte) 0x84, (byte) 0xE4, (byte) 0x09, (byte) 0xAD, (byte) 0x11, (byte) 0x24, (byte) 0x8B, (byte) 0x98, (byte) 0xC0, (byte) 0x81, (byte) 0x7F, (byte) 0x21, (byte) 0xA3, (byte) 0x52, (byte) 0xBE, (byte) 0x19, (byte) 0x93, (byte) 0x09, (byte) 0xCE, (byte) 0x20, (byte) 0x10, (byte) 0x46, (byte) 0x4A, (byte) 0x4A, (byte) 0xF8, (byte) 0x27, (byte) 0x31, (byte) 0xEC, (byte) 0x58, (byte) 0xC7, (byte) 0xE8, (byte) 0x33, (byte) 0x82, (byte) 0xE3, (byte) 0xCE, (byte) 0xBF, (byte) 0x85, (byte) 0xF4, (byte) 0xDF, (byte) 0x94, (byte) 0xCE, (byte) 0x4B, (byte) 0x09, (byte) 0xC1, (byte) 0x94, (byte) 0x56, (byte) 0x8A, (byte) 0xC0, (byte) 0x13, (byte) 0x72, (byte) 0xA7, (byte) 0xFC, (byte) 0x9F, (byte) 0x84, (byte) 0x4D, (byte) 0x73, (byte) 0xA3, (byte) 0xCA, (byte) 0x9A, (byte) 0x61, (byte) 0x58, (byte) 0x97, (byte) 0xA3, (byte) 0x27, (byte) 0xFC, (byte) 0x03, (byte) 0x98, (byte) 0x76, (byte) 0x23, (byte) 0x1D, (byte) 0xC7, (byte) 0x61, (byte) 0x03, (byte) 0x04, (byte) 0xAE, (byte) 0x56, (byte) 0xBF, (byte) 0x38, (byte) 0x84, (byte) 0x00, (byte) 0x40, (byte) 0xA7, (byte) 0x0E, (byte) 0xFD, (byte) 0xFF, (byte) 0x52, (byte) 0xFE, (byte) 0x03, (byte) 0x6F, (byte) 0x95, (byte) 0x30, (byte) 0xF1, (byte) 0x97, (byte) 0xFB, (byte) 0xC0, (byte) 0x85, (byte) 0x60, (byte) 0xD6, (byte) 0x80, (byte) 0x25, (byte) 0xA9, (byte) 0x63, (byte) 0xBE, (byte) 0x03, (byte) 0x01, (byte) 0x4E, (byte) 0x38, (byte) 0xE2, (byte) 0xF9, (byte) 0xA2, (byte) 0x34, (byte) 0xFF, (byte) 0xBB, (byte) 0x3E, (byte) 0x03, (byte) 0x44, (byte) 0x78, (byte) 0x00, (byte) 0x90, (byte) 0xCB, (byte) 0x88, (byte) 0x11, (byte) 0x3A, (byte) 0x94, (byte) 0x65, (byte) 0xC0, (byte) 0x7C, (byte) 0x63, (byte) 0x87, (byte) 0xF0, (byte) 0x3C, (byte) 0xAF, (byte) 0xD6, (byte) 0x25, (byte) 0xE4, (byte) 0x8B, (byte) 0x38, (byte) 0x0A, (byte) 0xAC, (byte) 0x72, (byte) 0x21, (byte) 0xD4, (byte) 0xF8, (byte) 0x07 };

    private byte[] nintendoLogo = new byte[156];

    public byte[] getNintendoLogo() {
        return nintendoLogo;
    }

    public void setNintendoLogo(byte[] nintendoLogo) {
        copyByteArray(nintendoLogo, this.nintendoLogo);
    }

    public void setNintendoLogo() {
        copyByteArray(validNintendoLogo, this.nintendoLogo);
    }

    private byte[] gameTitle = new byte[12];

    public byte[] getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(byte[] gameTitle) {
        copyByteArray(gameTitle, this.gameTitle);
    }

    public void setGameTitle(String gameTitle) {
        copyStringToByteArray(gameTitle, this.gameTitle);
    }

    private byte[] gameCode = new byte[4];

    public byte[] getGameCode() {
        return gameCode;
    }

    public void setGameCode(byte[] gameCode) {
        copyByteArray(gameCode, this.gameCode);
    }

    public void setGameCode(String gameCode) {
        copyStringToByteArray(gameCode, this.gameCode);
    }

    private byte[] developer = new byte[2];

    public byte[] getDeveloper() {
        return developer;
    }

    public void setDeveloper(byte[] developer) {
        copyByteArray(developer, this.developer);
    }

    public void setDeveloper(String developer) {
        copyStringToByteArray(developer, this.developer);
    }

    private byte hex96Byte = (byte) 0x96;

    public byte getHex96Byte() {
        return hex96Byte;
    }

    public void setHex96Byte(byte hex96Byte) {
        this.hex96Byte = hex96Byte;
    }

    private byte mainUnitCode = 0x00;

    public byte getMainUnitCode() {
        return mainUnitCode;
    }

    public void setMainUnitCode(byte mainUnitCode) {
        this.mainUnitCode = mainUnitCode;
    }

    private byte deviceType = 0x00;

    public byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(byte deviceType) {
        this.deviceType = deviceType;
    }

    private byte[] reservedAreaOne = new byte[7];

    public byte[] getReservedAreaOne() {
        return reservedAreaOne;
    }

    public void setReservedAreaOne(byte[] reservedAreaOne) {
        copyByteArray(reservedAreaOne, this.reservedAreaOne);
    }

    private byte version = 0x00;

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    private byte complement = 0x00;

    public byte getComplement() {
        return complement;
    }

    public void setComplement(byte complement) {
        this.complement = complement;
    }

    public void setComplement() {
        this.complement = calculateComplement();
    }

    private byte[] reservedAreaTwo = new byte[2];

    public byte[] getReservedAreaTwo() {
        return reservedAreaTwo;
    }

    public void setReservedAreaTwo(byte[] reservedAreaTwo) {
        copyByteArray(reservedAreaTwo, this.reservedAreaTwo);
    }

    private byte calculateComplement() {
        int total = 0;
        for (int i = 0; i < gameTitle.length; i++) total += convertByte(gameTitle[i]);
        for (int i = 0; i < gameCode.length; i++) total += convertByte(gameCode[i]);
        for (int i = 0; i < developer.length; i++) total += convertByte(developer[i]);
        total += convertByte(hex96Byte);
        total += convertByte(mainUnitCode);
        total += convertByte(deviceType);
        for (int i = 0; i < reservedAreaOne.length; i++) total += convertByte(reservedAreaOne[i]);
        total += convertByte(version);
        total = -total - 0x19;
        return (byte) (total & 0xff);
    }

    public boolean isValid() {
        for (int i = 0; i < validNintendoLogo.length; i++) if (nintendoLogo[i] != validNintendoLogo[i]) return false;
        if ((!isAllASCII(gameTitle)) || (!isAllASCII(gameCode)) || (!isAllASCII(developer)) || (hex96Byte != (byte) 0x96) || (!isAllZero(reservedAreaOne)) || (!isAllZero(reservedAreaTwo)) || (calculateComplement() != complement) || (startAddressBytes[3] != (byte) 0xEA)) return false;
        return true;
    }

    public void makeValid() {
        setNintendoLogo();
        hex96Byte = (byte) 0x96;
        for (int i = 0; i < reservedAreaOne.length; i++) reservedAreaOne[i] = 0x00;
        for (int i = 0; i < reservedAreaTwo.length; i++) reservedAreaTwo[i] = 0x00;
        for (int i = 0; i < gameTitle.length; i++) if (!isASCII(gameTitle[i])) gameTitle[i] = 0x20;
        for (int i = 0; i < gameCode.length; i++) if (!isASCII(gameCode[i])) gameCode[i] = 0x20;
        for (int i = 0; i < developer.length; i++) if (!isASCII(developer[i])) developer[i] = 0x20;
        startAddressBytes[3] = (byte) 0xEA;
        setComplement();
    }

    public void readFromStream(InputStream is) throws IOException {
        is.read(startAddressBytes);
        startAddress = ((int) startAddressBytes[0] & 0xff) + (((int) startAddressBytes[1] << 8) & 0xff) + (((int) startAddressBytes[2] << 16) & 0xff);
        startAddress <<= 2;
        if ((startAddress & 0x00200000) == 0x00200000) startAddress = (int) (startAddress + 0xFC000000);
        startAddress += 0x08000008;
        is.read(nintendoLogo);
        is.read(gameTitle);
        is.read(gameCode);
        is.read(developer);
        hex96Byte = (byte) is.read();
        mainUnitCode = (byte) is.read();
        deviceType = (byte) is.read();
        is.read(reservedAreaOne);
        version = (byte) is.read();
        complement = (byte) is.read();
        is.read(reservedAreaTwo);
    }

    public void writeToStream(OutputStream os) throws IOException {
        os.write(startAddressBytes);
        os.write(nintendoLogo);
        os.write(gameTitle);
        os.write(gameCode);
        os.write(developer);
        os.write(convertByte(hex96Byte));
        os.write(convertByte(mainUnitCode));
        os.write(convertByte(deviceType));
        os.write(reservedAreaOne);
        os.write(convertByte(version));
        os.write(convertByte(complement));
        os.write(reservedAreaTwo);
    }

    private static short convertByte(byte b) {
        return ((b >= 0) ? b : (short) (256 + b));
    }

    private static void copyByteArray(byte[] src, byte[] dest) {
        int length = Math.min(src.length, dest.length);
        for (int i = 0; i < length; i++) dest[i] = src[i];
    }

    private static void copyStringToByteArray(String src, byte[] dest) {
        char[] arraySrc = src.toCharArray();
        int length = Math.min(arraySrc.length, dest.length);
        for (int i = 0; i < length; i++) dest[i] = (byte) arraySrc[i];
        if (length < dest.length) for (int i = length; i < dest.length; i++) dest[i] = (byte) ' ';
    }

    private static boolean isAllASCII(byte[] array) {
        for (int i = 0; i < array.length; i++) if (!isASCII(array[i])) return false;
        return true;
    }

    private static boolean isASCII(byte b) {
        return !(((b < 0x20) || (b > 0x60)) && (b != 0x00));
    }

    private static boolean isAllZero(byte[] array) {
        for (int i = 0; i < array.length; i++) if (array[i] != 0x00) return false;
        return true;
    }
}

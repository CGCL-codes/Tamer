package org.myrobotlab.serial;

import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.TooManyListenersException;
import org.apache.log4j.Logger;
import org.myrobotlab.service.Wii;

public class WiiCommPort extends SerialPort {

    public static final Logger LOG = Logger.getLogger(WiiCommPort.class.getCanonicalName());

    private Wii wii = null;

    ByteArrayOutputStream out;

    InputStream is;

    WiiOutputStream os;

    public class WiiOutputStream extends OutputStream {

        Random generator = new Random();

        @Override
        public void write(int b) throws IOException {
            wii.sendSerial(b);
        }
    }

    public interface LineDriver {

        void pulseUp();

        void pulseDown();
    }

    ;

    public void setWii(Wii ld) {
        wii = ld;
    }

    @Override
    public void addEventListener(SerialPortEventListener arg0) throws TooManyListenersException {
        LOG.info("addEventListener");
    }

    @Override
    public int getBaudBase() throws UnsupportedCommOperationException, IOException {
        LOG.info("getBaudBase");
        return 9600;
    }

    @Override
    public int getBaudRate() {
        LOG.info("getBaudRate");
        return 9600;
    }

    @Override
    public boolean getCallOutHangup() throws UnsupportedCommOperationException {
        LOG.info("getCallOutHangup");
        return false;
    }

    @Override
    public int getDataBits() {
        LOG.info("getDataBits");
        return SerialPort.DATABITS_8;
    }

    @Override
    public int getDivisor() throws UnsupportedCommOperationException, IOException {
        return 0;
    }

    @Override
    public byte getEndOfInputChar() throws UnsupportedCommOperationException {
        return 0;
    }

    @Override
    public int getFlowControlMode() {
        return 0;
    }

    @Override
    public boolean getLowLatency() throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public int getParity() {
        return 0;
    }

    @Override
    public byte getParityErrorChar() throws UnsupportedCommOperationException {
        return 0;
    }

    @Override
    public int getStopBits() {
        return 0;
    }

    @Override
    public String getUARTType() throws UnsupportedCommOperationException {
        return null;
    }

    @Override
    public boolean isCD() {
        return false;
    }

    @Override
    public boolean isCTS() {
        return false;
    }

    @Override
    public boolean isDSR() {
        return false;
    }

    @Override
    public boolean isDTR() {
        return false;
    }

    @Override
    public boolean isRI() {
        return false;
    }

    @Override
    public boolean isRTS() {
        return false;
    }

    @Override
    public void notifyOnBreakInterrupt(boolean arg0) {
    }

    @Override
    public void notifyOnCTS(boolean arg0) {
    }

    @Override
    public void notifyOnCarrierDetect(boolean arg0) {
    }

    @Override
    public void notifyOnDSR(boolean arg0) {
    }

    @Override
    public void notifyOnDataAvailable(boolean arg0) {
    }

    @Override
    public void notifyOnFramingError(boolean arg0) {
    }

    @Override
    public void notifyOnOutputEmpty(boolean arg0) {
    }

    @Override
    public void notifyOnOverrunError(boolean arg0) {
    }

    @Override
    public void notifyOnParityError(boolean arg0) {
    }

    @Override
    public void notifyOnRingIndicator(boolean arg0) {
    }

    @Override
    public void removeEventListener() {
    }

    @Override
    public void sendBreak(int arg0) {
    }

    @Override
    public boolean setBaudBase(int arg0) throws UnsupportedCommOperationException, IOException {
        return false;
    }

    @Override
    public boolean setCallOutHangup(boolean arg0) throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public void setDTR(boolean arg0) {
    }

    @Override
    public boolean setDivisor(int arg0) throws UnsupportedCommOperationException, IOException {
        return false;
    }

    @Override
    public boolean setEndOfInputChar(byte arg0) throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public void setFlowControlMode(int arg0) throws UnsupportedCommOperationException {
    }

    @Override
    public boolean setLowLatency() throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public boolean setParityErrorChar(byte arg0) throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public void setRTS(boolean arg0) {
    }

    @Override
    public void setSerialPortParams(int arg0, int arg1, int arg2, int arg3) throws UnsupportedCommOperationException {
    }

    @Override
    public boolean setUARTType(String arg0, boolean arg1) throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public void disableReceiveFraming() {
    }

    @Override
    public void disableReceiveThreshold() {
    }

    @Override
    public void disableReceiveTimeout() {
    }

    @Override
    public void enableReceiveFraming(int arg0) throws UnsupportedCommOperationException {
    }

    @Override
    public void enableReceiveThreshold(int arg0) throws UnsupportedCommOperationException {
    }

    @Override
    public void enableReceiveTimeout(int arg0) throws UnsupportedCommOperationException {
    }

    @Override
    public int getInputBufferSize() {
        return 0;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        out = new ByteArrayOutputStream();
        is = new ByteArrayInputStream(out.toByteArray());
        return is;
    }

    @Override
    public int getOutputBufferSize() {
        return 0;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        os = new WiiOutputStream();
        return os;
    }

    @Override
    public int getReceiveFramingByte() {
        return 0;
    }

    @Override
    public int getReceiveThreshold() {
        return 0;
    }

    @Override
    public int getReceiveTimeout() {
        return 0;
    }

    @Override
    public boolean isReceiveFramingEnabled() {
        return false;
    }

    @Override
    public boolean isReceiveThresholdEnabled() {
        return false;
    }

    @Override
    public boolean isReceiveTimeoutEnabled() {
        return false;
    }

    @Override
    public void setInputBufferSize(int arg0) {
    }

    @Override
    public void setOutputBufferSize(int arg0) {
    }
}

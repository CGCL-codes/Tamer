package org.softmed.ATComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

public class DefaultSMSToATTranslator implements SMSToATTranslator {

    InputStream inputStream;

    OutputStream outputStream;

    BufferedReader bufferedReader;

    PrintStream printStream;

    public void sendSMS(String number, String msg, InputStream in, OutputStream out) throws IOException {
        inputStream = in;
        outputStream = out;
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        printStream = new PrintStream(outputStream, true);
        exchangeMessage("AT");
        exchangeMessage("AT+CMGF=1");
        String line = bufferedReader.readLine();
        validateResponse(line, "ERROR CHANGING TO SMS CREATION MODE ");
        printStream.print("AT+CMGW=\"" + number + "\"");
        printStream.print("\r");
        printStream.flush();
        bufferedReader.readLine();
        System.out.println("PREVIOUS COMMAND - " + bufferedReader.readLine());
        System.out.println("CR" + bufferedReader.read());
        System.out.println("LF" + bufferedReader.read());
        System.out.println(">" + bufferedReader.read());
        System.out.println("SPACE" + bufferedReader.read());
        System.out.println("SMS Text : " + msg);
        printStream.print(msg);
        byte b = 0x1A;
        printStream.write(b);
        printStream.flush();
        waitForInputReady();
        System.out.println("Available : " + inputStream.available());
        String msgId = null;
        System.out.println("1 - MESSAGE SENT:" + getPortResponseWhenReady());
        System.out.println("RESPONSE:" + (msgId = getPortResponseWhenReady()));
        validateResponse(msgId, "ERROR CREATING MESSAGE ");
        if (msgId != null && msgId.startsWith("\r\n+CMGW: ")) {
            msgId = msgId.substring(msgId.indexOf(' ') + 1);
            msgId = msgId.substring(0, msgId.indexOf("\r\n"));
            System.out.println("SMS IS : " + msgId);
        }
        shortExchangeMessage("AT+CMSS=" + msgId);
        line = getPortResponseWhenReady();
        validateResponse(line, "ERROR SENDING MESSAGE ");
        System.out.println("SENT RESPONSE: " + line);
        shortExchangeMessage("AT+CMGD=" + msgId);
        line = getPortResponseWhenReady();
        validateResponse(msgId, "ERROR DELETING MESSAGE ");
        System.out.println("DELETE RESPONSE: " + line);
    }

    protected void validateResponse(String response, String error) throws IOException {
        if (response.contains("ERROR")) throw new IOException(error);
    }

    protected String getPortResponseWhenReady() throws IOException {
        int size = waitForInputReady();
        return readTextFromLine(size);
    }

    protected int waitForInputReady() throws IOException {
        int available = -1;
        while ((available = inputStream.available()) <= 0) {
            waitForMiliSeconds(50);
        }
        return available;
    }

    protected void waitAShortWhile() {
        waitForMiliSeconds(100);
    }

    protected void waitForMiliSeconds(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected String readTextFromLine() {
        String text = "";
        try {
            while (true) text += (char) bufferedReader.read();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println(text);
        return text;
    }

    protected String readTextFromLine(int size) throws IOException {
        String text = "";
        for (int i = 0; i < size; i++) {
            text += (char) bufferedReader.read();
        }
        return text;
    }

    protected void sendIntValue(int value) throws IOException {
        printStream.print(value);
        printStream.print("\n");
        printStream.flush();
    }

    protected String exchangeMessage(String msg) throws IOException {
        System.out.println(msg);
        printStream.print(msg);
        printStream.print("\r\n");
        printStream.flush();
        bufferedReader.readLine();
        bufferedReader.readLine();
        String response = bufferedReader.readLine();
        System.out.println(response);
        return response;
    }

    protected void shortExchangeMessage(String msg) throws IOException {
        System.out.println(msg);
        printStream.print(msg);
        printStream.print("\r\n");
        printStream.flush();
        System.out.println("ECHO:" + bufferedReader.readLine());
    }

    protected String exchangeMessageEnd(String msg) throws IOException {
        System.out.println(msg);
        printStream.print(msg);
        printStream.append((char) 26);
        printStream.print("\r\n");
        printStream.print("\r\n");
        printStream.flush();
        System.out.println(bufferedReader.readLine());
        System.out.println(bufferedReader.readLine());
        String response = bufferedReader.readLine();
        System.out.println(response);
        return response;
    }

    protected void readData() {
        try {
            int count = -1;
            while ((count = inputStream.available()) > 0) {
                for (int i = 0; i < count; i++) {
                    int read = inputStream.read();
                    char c = (char) read;
                    System.out.print(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

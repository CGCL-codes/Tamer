package pubweb.bsp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import pubweb.AbortedException;
import pubweb.IntegrityException;

public interface BSP {

    public void printStdOut(String line);

    public void printStdErr(String line);

    public void writeRawData(Serializable data);

    public void flush();

    public void abort(Throwable cause) throws AbortedException;

    public void sync();

    public void send(int pid, Serializable msg) throws IntegrityException;

    public void send(int pidLow, int pidHigh, Serializable msg) throws IntegrityException;

    public void send(int[] pids, Serializable msg) throws IntegrityException;

    public int getNumberOfMessages();

    public Message getMessage(int index) throws IntegrityException;

    public Message[] getAllMessages();

    public Message findMessage(int pid, int index) throws IntegrityException;

    public Message[] findAllMessages(int pid) throws IntegrityException;

    public int getNumberOfProcessors();

    public int getProcessId();

    public long getTime();

    public String getHostname();

    public InputStream getResourceAsStream(String name) throws IOException, MalformedURLException;
}

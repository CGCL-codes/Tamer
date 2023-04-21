package com.mepping.snmpjaag;

import java.io.InputStream;
import java.util.Map;
import com.mepping.snmpjaag.nrpe.PluginResult;

public class CommandThread extends Thread {

    private String name;

    private String cmdline;

    private long frequencyMillis;

    private Map results;

    private boolean killed;

    public CommandThread(String name, String cmdline, long frequencyMillis, Map results) {
        this.name = name;
        this.cmdline = cmdline;
        this.frequencyMillis = frequencyMillis;
        this.results = results;
        this.killed = false;
        setDaemon(true);
    }

    public void kill() {
        killed = true;
    }

    public void run() {
        long lastCheckTimeMillis;
        long remainingMillis;
        while (!killed) {
            lastCheckTimeMillis = System.currentTimeMillis();
            PluginResult result = new PluginResult();
            try {
                Process process = Runtime.getRuntime().exec(cmdline);
                StringBuffer buffer = new StringBuffer();
                InputStream in = process.getInputStream();
                int ch;
                while (-1 != (ch = in.read())) {
                    buffer.append((char) ch);
                }
                in.close();
                try {
                    result.setState(process.exitValue());
                    result.setPluginOutput(buffer.toString());
                } catch (IllegalThreadStateException e) {
                    e.printStackTrace();
                    process.destroy();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            results.put(name, result);
            synchronized (results) {
                results.notifyAll();
            }
            while ((remainingMillis = frequencyMillis - System.currentTimeMillis() + lastCheckTimeMillis) > 0L) {
                try {
                    synchronized (results) {
                        results.wait(remainingMillis);
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }
}

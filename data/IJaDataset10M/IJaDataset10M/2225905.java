package org.deved.antlride.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.deved.antlride.core.AntlrConsole;
import org.deved.antlride.core.AntlrLanguageToolkit;

public class ProcessStreamMonitor {

    private class OutputStreamMonitor implements Runnable {

        private BufferedReader fIn;

        private boolean fError;

        public OutputStreamMonitor(boolean error, InputStream in) {
            this.fIn = new BufferedReader(new InputStreamReader(in));
            fError = error;
        }

        public void run() {
            AntlrConsole console = getConsole();
            try {
                String line = fIn.readLine();
                while (line != null) {
                    if (fError) {
                        console.error(line);
                    } else {
                        console.info(line);
                    }
                    line = fIn.readLine();
                }
            } catch (Exception ex) {
                PrintWriter writer = new PrintWriter(console.getErrorOutputStream());
                ex.printStackTrace(writer);
                writer.close();
            }
            try {
                fIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ProcessStreamMonitor(Process process, String label) {
        getConsole().info("");
        if (label != null) {
            getConsole().info(label);
        }
        startMonitoring(false, label, process.getInputStream());
        startMonitoring(true, label, process.getErrorStream());
    }

    private void startMonitoring(boolean err, String label, InputStream in) {
        Thread thread = new Thread(new OutputStreamMonitor(err, in));
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    private AntlrConsole getConsole() {
        AntlrLanguageToolkit languageToolkit = AntlrLanguageToolkit.getDefault();
        return languageToolkit.getConsole();
    }
}

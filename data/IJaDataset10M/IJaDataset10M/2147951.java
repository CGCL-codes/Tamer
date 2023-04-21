package org.echarts.edt.launcher.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;

public class ProcUtils {

    private static void dumpProcSysOut(Process p) throws IOException {
        InputStream is = p.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void runDumpAndWait(String[] cmd, ProcessBuilder pb, ILaunch launch) {
        try {
            Process p = pb.start();
            IProcess iProcess = DebugPlugin.newProcess(launch, p, "make", null);
            System.out.printf("Output of running %s is:\n", Arrays.toString(cmd));
            dumpProcSysOut(p);
            waitForExitValue(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void waitForExitValue(Process p) {
        try {
            int exitValue = p.waitFor();
            System.out.println("\n\nExit Value is " + exitValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

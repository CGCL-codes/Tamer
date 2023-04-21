package engine3D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class EngineLog {

    private static Calendar calendar;

    private BufferedWriter bw;

    public EngineLog(String fileName) {
        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            calendar = Calendar.getInstance();
        } catch (IOException ioe) {
            System.out.println("Error iniciando archivo log del engine3D: " + ioe.getMessage());
        }
    }

    private String time() {
        String date = "*";
        date = date + standType(calendar.get(calendar.DAY_OF_MONTH));
        date = date + "-" + standType(calendar.get(calendar.MONTH));
        date = date + "-" + standType(calendar.get(calendar.YEAR));
        date = date + " [" + standType(calendar.get(calendar.HOUR_OF_DAY));
        date = date + ":" + standType(calendar.get(calendar.MINUTE));
        date = date + ":" + standType(calendar.get(calendar.SECOND)) + "] ";
        return date;
    }

    private String standType(int value) {
        String add = "";
        if (value < 10) {
            add = "0";
        }
        return (add + Integer.toString(value));
    }

    public void writeEngineLogWithEmisor(String emisor, String text) {
        writeEngineLogAction(time() + "(" + emisor + ") -> " + text);
    }

    public void writeEngineLog(String text) {
        writeEngineLogAction(time() + " -> " + text);
    }

    private void writeEngineLogAction(String text) {
        try {
            bw.write(text);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            System.out.println("Error escribiendo el archivo log del engine3D: " + ioe.getMessage());
            closeEngineLog();
        }
    }

    public void closeEngineLog() {
        try {
            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            System.out.println("Error cerrando log del engine3D: " + ioe.getMessage());
        }
    }
}

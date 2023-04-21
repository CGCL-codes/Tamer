package ch.nostromo.tiffanys.clients.obcreate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class OBImporterCallable implements Callable<OBImporterResult> {

    File file;

    int limitMoves;

    ArrayList<String> result;

    public OBImporterCallable(File file, int limitMoves) {
        this.file = file;
        this.limitMoves = limitMoves;
        result = new ArrayList<String>();
    }

    public OBImporterResult call() throws Exception {
        long currMs = System.currentTimeMillis();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("1.")) {
                int pos7 = line.indexOf("8.");
                if (pos7 > 0) {
                    result.add(line.substring(0, pos7 - 1));
                }
            }
        }
        System.out.println("file: " + file.getName() + " : imported games = " + result.size() + " Time: " + (System.currentTimeMillis() - currMs));
        return new OBImporterResult(result);
    }
}

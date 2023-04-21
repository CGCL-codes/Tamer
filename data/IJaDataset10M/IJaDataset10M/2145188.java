package es.usc.citius.servando.android.medim.Drivers.File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class FileReader {

    private static final String DEBUG_TAG = FileReader.class.getSimpleName();

    /**
	 * FORMATO: unha mostra por liña
	 * 
	 * @param path
	 * @return
	 */
    public static ArrayList<Short> loadTextFile(String path) {
        ArrayList<Short> samples = new ArrayList<Short>();
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            Log.d(DEBUG_TAG, "Reading...");
            while ((line = br.readLine()) != null) {
                samples.add(new Float(line).shortValue());
            }
            Log.d(DEBUG_TAG, "File read successfully. " + samples.size() + " samples found.");
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error reading file " + path, e);
        }
        return samples;
    }

    /**
	 * FORMATO: csv, a primeira columns co numero de mostra e unha columna a
	 * maiores por cada derivación
	 */
    public static List<List<Short>> loadCsvFile(String path) {
        List<List<Short>> leads = new ArrayList<List<Short>>();
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Log.d(DEBUG_TAG, "Reading...");
            String linea = br.readLine();
            String[] values = linea.split(",");
            int lcount = values.length - 1;
            for (int i = 0; i < lcount; i++) {
                leads.add(i, new ArrayList<Short>());
                leads.get(i).add(new Short(values[i]));
            }
            while ((linea = br.readLine()) != null) {
                values = linea.split(",");
                for (int i = 1; i < values.length; i++) {
                    leads.get(i - 1).add(new Short(values[i]));
                }
            }
            Log.d(DEBUG_TAG, "File read successfully. " + leads.get(0).size() + " samples found.");
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error reading file " + path, e);
        }
        return leads;
    }

    /**
	 * FORMATO: csv, a primeira columns co numero de mostra e unha columna a
	 * maiores por cada derivación
	 */
    public static List<List<Short>> loadCsvFile(String path, int limit) {
        Log.d(DEBUG_TAG, "Loading samples from csv (limit: " + limit + ") ... ");
        List<List<Short>> leads = new ArrayList<List<Short>>();
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Log.d(DEBUG_TAG, "Reading...");
            String linea = br.readLine();
            String[] values = linea.split(",");
            int lcount = values.length - 1;
            for (int i = 0; i < lcount; i++) {
                leads.add(i, new ArrayList<Short>());
                leads.get(i).add(new Short(values[i]));
            }
            int currentSize = 0;
            while (((linea = br.readLine()) != null) && currentSize < limit) {
                values = linea.split(",");
                for (int i = 1; i < values.length; i++) {
                    leads.get(i - 1).add(new Short(values[i]));
                }
                currentSize++;
            }
            Log.d(DEBUG_TAG, "File read successfully. " + leads.get(0).size() + " samples found.");
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error reading file " + path, e);
        }
        return leads;
    }
}

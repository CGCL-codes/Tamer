package edu.cmu.lti.dimple.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads MS paraphrase data. 
 * 
 * @author Hideki Shima
 *
 */
public class MSParaphrasePairLoader {

    public static List<MSParaphrasePair> load(File f) {
        List<MSParaphrasePair> list = new ArrayList<MSParaphrasePair>();
        try {
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                if (values.length <= 1) continue;
                list.add(new MSParaphrasePair(values[0], values[1], values[2], values[3], values[4]));
            }
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

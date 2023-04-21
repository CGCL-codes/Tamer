package gov.ca.maps.bathymetry.processor;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;

public class CalculateDepths {

    public static void main(String[] args) throws Exception {
        String inputFileStr = "d:/dev/3dgate-sdip/HORBifurcation.txt";
        double depth = 2.8956;
        int startZoom = 16;
        int endZoom = 21;
        double originEasting = 647218;
        double originNorthing = 4185824;
        File inputFile = new File(inputFileStr);
        String dir = inputFile.getParent();
        dir = dir == null ? "./tiles" : dir + "/tiles";
        LineNumberReader lnr = new LineNumberReader(new FileReader(inputFile));
        String line = lnr.readLine();
        String[] fields = line.split(",");
        HashMap<String, Integer> nameToIndexMap = new HashMap<String, Integer>();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].trim();
            if (nameToIndexMap.containsKey(fieldName)) {
                continue;
            }
            nameToIndexMap.put(fieldName, new Integer(i));
        }
        int xci = nameToIndexMap.get("x-coordinate");
        int yci = nameToIndexMap.get("y-coordinate");
        int zci = nameToIndexMap.get("z-coordinate");
        int vmi = nameToIndexMap.get("velocity-magnitude");
        int vxi = nameToIndexMap.get("x-velocity");
        int vyi = nameToIndexMap.get("y-velocity");
        int count = 0;
        HashMap<String, Integer> depthMap = new HashMap<String, Integer>();
        while ((line = lnr.readLine()) != null) {
            fields = line.split(",");
            String zValue = fields[zci].trim();
            if (depthMap.containsKey(zValue)) {
                depthMap.put(zValue, depthMap.get(zValue) + 1);
            } else {
                depthMap.put(zValue, 1);
            }
        }
        lnr.close();
        for (String key : depthMap.keySet()) {
            System.out.println("depth: " + key + " has " + depthMap.get(key) + " values");
        }
    }
}

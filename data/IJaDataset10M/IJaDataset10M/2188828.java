package util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class PlotMap {

    private static final String SOURCE_FILENAME = "C:\\Documents and Settings\\Administrator\\Desktop\\Datasets\\QUERY-grippe_porcine-2009-06-07.txt";

    private static final String SOURCE_LOCATION_FILENAME = "C:\\Documents and Settings\\Administrator\\Desktop\\Datasets\\LOCATION-roland_garros-2009-06-07.txt";

    private static final String DESTIN_FILENAME = "C:\\Documents and Settings\\Administrator\\Desktop\\Datasets\\PRE-LOCATION-grippe_porcine-2009-06-07.txt";

    FileOutputStream fout;

    PrintStream ps;

    PlotMap() {
        try {
            fout = new FileOutputStream(DESTIN_FILENAME);
            ps = new PrintStream(fout);
            parserLocation2JavascriptArray();
            ps.flush();
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<List<Double>> coordinates = new ArrayList<List<Double>>();

    public boolean hasCoordinates(double lat, double lng) {
        boolean ret = false;
        for (List<Double> coord : coordinates) {
            double c_lat = coord.get(0);
            double c_lng = coord.get(1);
            if (c_lat == lat || c_lng == lng) return true;
            double lat_diff = 0;
            double lng_diff = 0;
            if (lat > 0 && c_lat > 0 || lat < 0 && c_lat < 0) lat_diff = Math.abs((lat - c_lat));
            if (lng > 0 && c_lng > 0 || lng < 0 && c_lng < 0) lng_diff = Math.abs((lng - c_lng));
            if (lat_diff < 0.5 && lng_diff < 0.5) return true;
        }
        return ret;
    }

    public void parserLocation2JavascriptArray() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(SOURCE_LOCATION_FILENAME));
        String str;
        int count = 0;
        while ((str = in.readLine()) != null) {
            if (str == null || str.length() == 0) continue;
            int index1 = str.indexOf('|');
            str = str.substring(index1 + 1);
            int index2 = str.indexOf('|');
            String[] entry = { str.substring(0, index2), str.substring(index2 + 1) };
            if (entry.length >= 2) {
                String lat = entry[0];
                String lng = entry[1];
                if (!lat.equals("0") && !lng.equals("0")) {
                    List<Double> point = new ArrayList<Double>();
                    try {
                        double coord_lat = Double.parseDouble(lat);
                        double coord_lng = Double.parseDouble(lng);
                        if (coord_lat > 37 && coord_lng > -34 && coord_lng < 55) {
                            if (!hasCoordinates(coord_lat, coord_lng)) {
                                point.add(coord_lat);
                                point.add(coord_lng);
                                coordinates.add(point);
                                count++;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        System.out.println(coordinates);
    }

    public void parserLocationString2JavascriptArray() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(SOURCE_FILENAME));
        List<List<String>> list = new ArrayList<List<String>>();
        String str;
        String data = "";
        List<String> dataArray = new ArrayList<String>();
        int count = 0;
        while ((str = in.readLine()) != null) {
            if (str == null || str.length() == 0 || str.charAt(0) != '#') continue;
            String[] entry = str.split(";");
            if (entry.length >= 2) {
                String location = entry[1];
                if (!location.equals("")) {
                    if (count % 30 == 0 && count > 0) {
                        list.add(dataArray);
                        dataArray = new ArrayList<String>();
                    }
                    dataArray.add(location.replaceAll("'", ""));
                    count++;
                }
            }
        }
        System.out.println("finished, total: " + count);
        System.out.println();
        int ct = 0;
        for (List<String> sublist : list) {
            for (String location : sublist) {
                data += ", '" + location + "'";
            }
            data = "[" + data.substring(2) + "];";
            System.out.println("var l" + ct + " = " + data);
            data = "";
            ct++;
        }
        String finalElements = "";
        for (int i = 0; i < ct; i++) {
            finalElements += ", " + "l" + i;
        }
        finalElements = "[" + finalElements.substring(2) + "];";
        System.out.println("var locations = " + finalElements);
    }

    public void parserLocation2Line() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(SOURCE_FILENAME));
        String str;
        String data = "";
        int count = 0;
        while ((str = in.readLine()) != null) {
            if (str == null || str.length() == 0 || str.charAt(0) != '#') continue;
            String[] entry = str.split(";");
            if (entry.length >= 2) {
                String location = entry[1].replaceAll("'", "");
                if (!location.equals("")) {
                    location.replaceAll("iPhone: ", "");
                    ps.println(location);
                    count++;
                }
            }
        }
        System.out.println("finished, total: " + count);
        System.out.println();
        System.out.println(data);
    }

    public static void main(String[] args) {
        new PlotMap();
    }
}

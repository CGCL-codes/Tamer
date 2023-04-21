package vobs.plugins.clivtplugin.iterator.customDimensions;

import java.io.FileReader;
import java.io.IOException;
import vobs.plugins.clivtplugin.iterator.Dimension;

public class VegetationEnd4C extends Dimension {

    private String stringContent;

    private final int yearsNumber = 58;

    private final int latNumber = 15;

    private final int lonNumber = 62;

    private final int startYear = 1950;

    private final int stepYear = 1;

    private final float startLat = 75.0f;

    private final float stepLat = -2.5f;

    private final float startLon = 25.0f;

    private final float stepLon = 2.5f;

    private int[] yearShape = new int[yearsNumber];

    private float[] latShape = new float[latNumber];

    private float[] lonShape = new float[lonNumber];

    private String name = "VegetationEnd4C";

    public VegetationEnd4C() {
        generateShapes();
        try {
            stringContent = getDocumentAsString("D:/work/dai_testers/IKI_requests_2008.07.07/4C/vegetationEndResult.txt");
        } catch (Exception e) {
            System.out.println("error: ");
            e.printStackTrace();
        }
    }

    private String getDate(String stringContent, int index) {
        int startIndex = stringContent.indexOf("<t>", index) + 3;
        int endIndex = stringContent.indexOf("</t>", index);
        return stringContent.substring(startIndex, endIndex);
    }

    private int generateIndex(int yearIndex, int latIndex, int lonIndex) {
        return yearIndex * (latNumber * lonNumber) + latIndex * lonNumber + lonIndex;
    }

    private boolean isValueExist(String stringContent, int index) {
        int substringIndex = stringContent.indexOf("</timePointsAmount>", index);
        String vs = stringContent.substring(substringIndex - 1, substringIndex);
        if (vs.equals("1")) return true;
        return false;
    }

    private String generateHeder(int yearIndex, int latIndex, int lonIndex) {
        int year = yearShape[yearIndex];
        float lat = latShape[latIndex];
        float lon = lonShape[lonIndex];
        return "year=" + year + " lat=" + lat + " lon=" + lon + "";
    }

    private String getDocumentAsString(String scriptPerformFullPath) throws IOException {
        FileReader fr = new FileReader(scriptPerformFullPath);
        StringBuffer sb = new StringBuffer();
        char[] buff = new char[1000];
        int flag = fr.read(buff);
        while (flag != -1) {
            sb.append(buff, 0, flag);
            flag = fr.read(buff);
        }
        return sb.toString();
    }

    private void generateShapes() {
        generateYearShape();
        generateLatShape();
        generateLonShape();
    }

    private void generateLonShape() {
        for (int i = 0; i < lonNumber; i++) {
            lonShape[i] = startLon + i * stepLon;
        }
    }

    private void generateLatShape() {
        for (int i = 0; i < latNumber; i++) {
            latShape[i] = startLat + i * stepLat;
        }
    }

    private void generateYearShape() {
        for (int i = 0; i < yearsNumber; i++) {
            yearShape[i] = startYear + i * stepYear;
        }
    }

    public String[] getAllValues() {
        return null;
    }

    public int getLength() {
        return yearsNumber * latNumber * lonNumber;
    }

    public String getName() {
        return name;
    }

    public String getValue(int indexValue) {
        int yearIndex = indexValue / (latNumber * lonNumber);
        int latIndex = (indexValue % (latNumber * lonNumber)) / lonNumber;
        int lonIndex = indexValue - (yearIndex * (latNumber * lonNumber) + latIndex * lonNumber);
        String heder = generateHeder(yearIndex, latIndex, lonIndex);
        int index = stringContent.indexOf(heder);
        boolean isValueExist = isValueExist(stringContent, index);
        if (isValueExist) {
            String date = getDate(stringContent, index);
            return date;
        } else {
            return "" + (startYear + yearIndex) + "-12-31T00:00:00UTC";
        }
    }

    public String getValue(int year, float lat, float lon) {
        int yearIndex = year - startYear;
        int latIndex = (int) ((lat - startLat) / 2.5f);
        int lonIndex = (int) ((lon - startLon) / 2.5f);
        int index = generateIndex(yearIndex, latIndex, lonIndex);
        return getValue(index);
    }
}

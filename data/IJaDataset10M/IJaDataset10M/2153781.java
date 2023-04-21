package org.idec.catalog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 * 
 * @author Victor Pascual
 * @author Wladmir Szczerban
 */
public class Utils {

    private static Logger logger = Logger.getLogger(Utils.class);

    /**
	 * Replace the slashes of a path depending on the operating system
	 * 
	 * @param path
	 *            The original path
	 * @return The new path
	 */
    public static String checkSlashes(String path) {
        boolean runningUnix = "/".equals(File.separator);
        if (path == null) path = "";
        return findAndReplace(path, runningUnix ? "\\" : "/", runningUnix ? "/" : "\\");
    }

    /**
	 * Function extracted from http://snipplr.com/view/2643/findandreplace/.
	 * Replaces each substring of this string that matches the given string with
	 * the given replacing string.
	 * 
	 * @param original
	 *            The original string
	 * @param toBeRemoved
	 *            The string to be replaced
	 * @param replacingStr
	 *            The replacing string
	 * @return The new string
	 */
    public static String findAndReplace(String original, String toBeRemoved, String replacingStr) {
        StringBuffer finalStr = new StringBuffer("");
        int charCounter = 0;
        int start = 0;
        while (charCounter < original.length()) {
            start = original.indexOf(toBeRemoved, charCounter);
            if (start >= 0) {
                finalStr.append(original.substring(charCounter, start));
                charCounter = start + toBeRemoved.length();
                finalStr.append(replacingStr);
            } else {
                finalStr.append(original.substring(charCounter, original.length()));
                return finalStr.toString();
            }
        }
        return finalStr.toString();
    }

    /**
	 * Create a string array with bounging box information
	 * 
	 * @param bboxRequest
	 *            String with bounging box.Coordinates must be separate with
	 *            coma(,)
	 * @return String array with bounding box information
	 */
    public static String[] parseBBOX(String bboxRequest) {
        String[] bbox = new String[4];
        bbox = bboxRequest.split(",");
        bbox[0] = Double.toString(Math.max(-180.0, Double.parseDouble(bbox[0])));
        bbox[1] = Double.toString(Math.max(-90.0, Double.parseDouble(bbox[1])));
        bbox[2] = Double.toString(Math.min(180.0, Double.parseDouble(bbox[2])));
        bbox[3] = Double.toString(Math.min(90.0, Double.parseDouble(bbox[3])));
        return bbox;
    }

    /**
	 * Read the content of the given file and return it as string.
	 * 
	 * @param filePath
	 *            The name of the file to read.
	 * @return String that contains the file information
	 * @throws java.io.IOException
	 */
    public static String file2string(String filePath) throws java.io.IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        try {
            StringBuffer fileData = new StringBuffer(1000);
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                fileData.append(buf, 0, numRead);
            }
            return fileData.toString();
        } finally {
            reader.close();
        }
    }

    /**
	 * Write a string into a specific file
	 * 
	 * @param filePath
	 *            The path of file to write
	 * @param fileContent
	 *            The content to write in the file
	 */
    public static void string2file(String filePath, String fileContent) {
        try {
            BufferedWriter xmlfile = new BufferedWriter(new FileWriter(filePath));
            xmlfile.write(fileContent);
            xmlfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Create a Map object with the request parameters
	 * 
	 * @param request
	 *            The servelt request
	 * @return Map with the request parameters
	 */
    public static HashMap<String, String> getParametersToMap(HttpServletRequest request) {
        HashMap<String, String> mapParameters = new HashMap<String, String>();
        String keyValue;
        String parameterValue;
        for (Enumeration enu = request.getParameterNames(); enu.hasMoreElements(); ) {
            keyValue = (String) enu.nextElement();
            System.out.println();
            parameterValue = request.getParameter(keyValue);
            if (!parameterValue.equalsIgnoreCase("")) {
                logger.debug("KEY:" + keyValue);
                logger.debug("VALUE:" + parameterValue);
                mapParameters.put(keyValue.toUpperCase().trim(), parameterValue.trim());
            }
        }
        return mapParameters;
    }

    /**
	 * Test if the first string is equal to a portion of the second string
	 * 
	 * @param nameCatalog
	 *            The first string
	 * @param parametersCatalogs
	 *            The second string.
	 * @return return true if the first string is in the second string
	 */
    public static boolean checkExistsCatalogue(String nameCatalog, String parametersCatalogs) {
        boolean prFound = false;
        if (parametersCatalogs.contains(",")) {
            String[] ctls = parametersCatalogs.split(",");
            for (int i = 0; i < ctls.length; i++) {
                if (ctls[i].equalsIgnoreCase(nameCatalog)) {
                    prFound = true;
                }
            }
        } else {
            if (parametersCatalogs.equalsIgnoreCase(nameCatalog)) {
                prFound = true;
            }
        }
        return prFound;
    }

    /**
	 * 
	 * @param paramsRequest
	 * @return
	 */
    public static boolean checkRequestParams(Map paramsRequest) {
        return true;
    }

    static Catalog generateRequestCatalog(String idVal, String urlVal, String versionVal, String encoding, String productVal) throws IOException {
        String name = "";
        String title = "";
        String description = "";
        String XMLRequestsPath = "";
        String product = productVal;
        String urlcatalog = urlVal;
        String cswversion = versionVal;
        String XMLencoding = encoding;
        String ProxyHost = "";
        int ProxyPort = -1;
        Catalog cat = new Catalog(name, title, description, urlcatalog, product, cswversion, XMLRequestsPath, XMLencoding, ProxyHost, ProxyPort);
        return cat;
    }
}

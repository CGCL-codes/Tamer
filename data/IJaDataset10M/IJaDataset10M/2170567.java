package vobs.plugins.clivtplugin;

import java.io.*;
import java.net.*;
import java.util.*;
import org.jdom.*;
import vobs.plugins.clivtplugin.iterator.*;
import vobs.store.*;

public class TemplateExecutor {

    public org.jdom.Element process(Map params, String paramsString, String basketObjectId) {
        Element returnElm = new Element("result");
        try {
            String performTemplate = getPerform(params);
            String perform = fillTemplate(performTemplate, params);
            byte[] response = performDocument(perform, params);
            ByteArrayInputStream bais = new ByteArrayInputStream(response);
            String format = getResponseFormat(params);
            FileStoreSave.storeFile(bais, basketObjectId, format + "Poyda", null, basketObjectId, "fileAction", null);
            returnElm.addContent((new Element("status")).setText("0"));
            returnElm.addContent((new Element("error")).setText(""));
        } catch (Exception e) {
            returnElm.addContent((new Element("status")).setText("1"));
            returnElm.addContent((new Element("error")).setText(e.getMessage()));
        }
        return returnElm;
    }

    private String fillTemplate(String performTemplate, Map params) {
        int indexStart = performTemplate.indexOf("<@");
        if (indexStart == -1) {
            throw new RuntimeException("script lines must be marked with '<@' and '@>' chra-sequences (PPs)");
        }
        int indexEnd = performTemplate.indexOf("@>");
        if (indexEnd == -1) {
            throw new RuntimeException("script lines must be marked with '<@' and '@>' chra-sequences (PPe)");
        }
        String scriptTile = performTemplate.substring(indexStart + 2, indexEnd);
        StringTokenizer st = new StringTokenizer(scriptTile, "\n", false);
        String shape = null;
        boolean isSaveToActiveStorage = (performTemplate.indexOf("saveToActiveStorage") != -1);
        String latValuesString = null;
        String lonValuesString = null;
        String groupName = null;
        String destinationHost = null;
        String destinationDatabase = null;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String trimToken = token.trim();
            if (!trimToken.startsWith("@") && !trimToken.endsWith("@") && !trimToken.equals("")) {
                throw new RuntimeException("all sentences in script must start and end with charackter '@'");
            }
            if (trimToken.equals("")) {
                continue;
            }
            String sentence = trimToken.substring(1, trimToken.length() - 1);
            sentence = sentence.trim();
            if (sentence.startsWith("<") && sentence.endsWith(">")) {
                shape = sentence;
                continue;
            }
            String name = getVariableName(sentence);
            String value = ((String[]) params.get(name))[0];
            if (trimToken.endsWith("]@")) {
                performTemplate = performTemplate.replace(trimToken, "@" + name + "=[\"" + value + "\"]" + "@");
                if (name.equals("groupName")) groupName = value;
                if (name.equals("destinationHost")) destinationHost = value;
                if (name.equals("destinationDatabase")) destinationDatabase = value;
            } else {
                performTemplate = performTemplate.replace(trimToken, "@" + name + "=" + value + "@");
                if (value.startsWith("latitude")) latValuesString = value;
                if (value.startsWith("longitude")) lonValuesString = value;
            }
        }
        if (shape == null) {
            throw new RuntimeException("script must has a coordinate system description that " + "must marked with '@<' and '@>' sharacter-sequences");
        }
        if (isSaveToActiveStorage) {
            preload(latValuesString, lonValuesString, groupName, destinationHost, destinationDatabase);
        }
        return performTemplate;
    }

    /**
   * preload
   *
   * @param latValuesString String
   * @param lonValuesString String
   * @param groupName String
   * @param destinationVariableName String
   * @param destinationHost String
   * @param destinationDatabase String
   */
    private void preload(String latValuesString, String lonValuesString, String groupName, String destinationHost, String destinationDatabase) {
        float[] latSE = getRegularStonePoints(latValuesString);
        float[] lonSE = getRegularStonePoints(lonValuesString);
        float[] lats = generateRegularGrid(latSE);
        float[] lons = generateRegularGrid(lonSE);
        String connectionString = "jdbc:sqlserver://" + destinationHost + ";database=" + destinationDatabase + ";user=sa;password=nimda!mssql";
        try {
            ru.wdcb.activestorage.EsseLoader.writeCoords(connectionString, groupName, lats, lons);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }

    /**
   * generateRegularGrid
   *
   * @param latSE float[]
   * @return float[]
   */
    private float[] generateRegularGrid(float[] startStepEnd) {
        float start = startStepEnd[0];
        float step = startStepEnd[1];
        float end = startStepEnd[2];
        float[] result = new float[(int) ((end - start) / step) + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = start + step * i;
        }
        return result;
    }

    /**
   * getRegularStonePoints
   *
   * @param latValuesString String
   * @return float[]
   */
    private float[] getRegularStonePoints(String valuesString) {
        StringTokenizer st = new StringTokenizer(valuesString, "(),", false);
        st.nextToken();
        st.nextToken();
        String startValueString = st.nextToken().trim();
        String stepString = st.nextToken().trim();
        String endValueString = st.nextToken().trim();
        float startValue = Float.parseFloat(startValueString);
        float step = Float.parseFloat(stepString);
        float endValue = Float.parseFloat(endValueString);
        float[] result = new float[3];
        result[0] = startValue;
        result[1] = step;
        result[2] = endValue;
        return result;
    }

    private String getVariableName(String sentence) {
        int index = sentence.indexOf("=");
        if (index == -1) {
            return sentence;
        }
        String tile1 = sentence.substring(0, index);
        String dimensionName = tile1.trim();
        return dimensionName;
    }

    private String getResponseFormat(Map params) {
        if (params.get("format") == null) return "noNameFormat";
        return ((String[]) params.get("format"))[0];
    }

    private byte[] performDocument(String perform, Map params) throws Exception {
        String dataServiceUrl = ((String[]) params.get("dataServiceUrl"))[0];
        String resourceKey = ((String[]) params.get("resourceKey"))[0];
        PerformRequestIterator prq = new PerformRequestIterator();
        String response = prq.iterate(perform, dataServiceUrl, resourceKey);
        return response.getBytes();
    }

    private String getPerform(Map params) {
        try {
            String url = "http://dimetra.wdcb.ru:8080/virtualobservatory/filestore?objId=" + ((String[]) params.get("performDocumentId"))[0];
            URL performDocURL = new URL(url);
            InputStream is = performDocURL.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            char[] buff = new char[1000];
            int flag = br.read(buff);
            while (flag != -1) {
                sb.append(buff, 0, flag);
                flag = br.read(buff);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String printFloatArray(float[] floatArray) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < floatArray.length; i++) {
            sb.append("value[" + i + "]=" + floatArray[i]);
            if (i != (floatArray.length - 1)) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}

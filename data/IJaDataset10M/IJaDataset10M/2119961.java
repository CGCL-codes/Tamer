package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public final class MyJSONReader {

    private static String JSONFILE = Messages.getString("rutaJson0");

    private static JSONObject parsedJSONFile = null;

    private static MyJSONReader sINSTANCE = null;

    /**
	 * M�todo para parsear un string json le�do de un fichero de configuraci�n a un
	 * objeto JSONObject.
	 * @return El objeto JSONObject con el json le�do, o null si no se puede acceder a �l.
	 */
    private MyJSONReader() {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(JSONFILE));
        } catch (FileNotFoundException e) {
            System.out.println("Fichero de configuraci�n " + JSONFILE + " no encontrado. Se carga la configuraci�n por defecto...");
            return;
        }
        String aString = "", linea;
        try {
            while ((linea = bf.readLine()) != null) {
                aString += linea;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            bf.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        JSONObject json;
        try {
            json = new JSONObject(aString);
        } catch (JSONException e) {
            System.err.println("Hay un error sint�ctico en el fichero de configuraci�n.");
            return;
        }
        MyJSONReader.parsedJSONFile = json;
    }

    /***
	 * Singleton instance returner
	 * @return Singleton MyJSONReader INSTANCE
	 */
    public static MyJSONReader getInstance() {
        if (sINSTANCE == null) {
            sINSTANCE = new MyJSONReader();
        }
        return sINSTANCE;
    }

    /**
	 * M�todo para configurar la aplicaci�n acorde a la configuraci�n que se recibe en
	 * una estructura json.
	 * @param json JSON con las directrices de configuraci�n de la aplicaci�n.
	 */
    private void setConfig() {
        try {
            MasterConfig.data1 = MyJSONReader.parsedJSONFile.getInt("XXX");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int readConfigInt(String pFieldName) {
        int data = -1;
        try {
            data = MyJSONReader.parsedJSONFile.getInt(pFieldName);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
        return data;
    }

    public String readConfigStr(String pFieldName) {
        String data = null;
        try {
            data = MyJSONReader.parsedJSONFile.getString(pFieldName);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public boolean readConfigBool(String pFieldName) {
        boolean data = false;
        try {
            data = MyJSONReader.parsedJSONFile.getBoolean(pFieldName);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return data;
    }
}

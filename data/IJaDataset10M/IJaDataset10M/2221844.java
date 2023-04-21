package blue.blueShare;

import org.apache.xmlrpc.*;
import electric.xml.*;
import java.util.Vector;
import blue.orchestra.Instrument;
import Silence.XMLSerializer;
import java.io.*;

public class BlueShareRemoteCaller {

    private static XmlRpcClient xrpc;

    public static void setServer(String server) throws java.net.MalformedURLException {
        xrpc = new XmlRpcClient(server);
    }

    public static InstrumentCategory[] getInstrumentCategoryTree() throws IOException, XmlRpcException, ParseException {
        Vector v = new Vector();
        String result;
        Document doc;
        result = (String) xrpc.execute("blueShare.getInstrumentCategoryTree", v);
        doc = new Document(result);
        Element root = doc.getRoot();
        return getSubCategories(root);
    }

    private static InstrumentCategory[] getSubCategories(Element parent) {
        Elements categories = parent.getElements();
        InstrumentCategory[] iCategories = new InstrumentCategory[categories.size()];
        int i = 0;
        int catId;
        String name;
        Element temp;
        InstrumentCategory[] tempCategories;
        while (categories.hasMoreElements()) {
            temp = categories.next();
            catId = Integer.parseInt(temp.getAttribute("instrumentCategoryId").getValue());
            name = temp.getAttribute("name").getValue();
            tempCategories = getSubCategories(temp);
            iCategories[i] = new InstrumentCategory(catId, name, null, tempCategories);
            i++;
        }
        return iCategories;
    }

    public static InstrumentOption[] getInstrumentOptions(InstrumentCategory iCategory) throws IOException, XmlRpcException, ParseException {
        Vector v = new Vector();
        String result;
        Document doc;
        v.add(new Integer(iCategory.getCategoryId()));
        result = (String) xrpc.execute("blueShare.getInstrumentList", v);
        doc = new Document(result);
        Element root = doc.getRoot();
        Elements instruments = root.getElements("instrument");
        InstrumentOption[] iOptions = new InstrumentOption[instruments.size()];
        int i = 0;
        int instrumentId;
        String screenName, name, type, description;
        Element temp;
        while (instruments.hasMoreElements()) {
            temp = instruments.next();
            instrumentId = Integer.parseInt(temp.getAttribute("instrumentId").getValue());
            screenName = temp.getElement("screenName").getTextString();
            name = temp.getElement("name").getTextString();
            type = temp.getElement("type").getTextString();
            description = temp.getElement("description").getTextString();
            iOptions[i] = new InstrumentOption(instrumentId, screenName, name, type, description);
            i++;
        }
        return iOptions;
    }

    public static Instrument getInstrument(InstrumentOption iOption) throws IOException, XmlRpcException, ParseException {
        Vector v = new Vector();
        String result;
        Document doc;
        v.add(new Integer(iOption.getInstrumentId()));
        result = (String) xrpc.execute("blueShare.getInstrument", v);
        Instrument instrument;
        try {
            XMLSerializer xmlSer = new XMLSerializer();
            BufferedReader reader = new BufferedReader(new StringReader(result));
            instrument = (Instrument) xmlSer.read(reader);
            return instrument;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean submitInstrument(String username, String password, int categoryId, String name, String instrumentType, String description, String instrumentText) throws IOException, XmlRpcException {
        Vector v = new Vector();
        v.add(username);
        v.add(password);
        v.add(new Integer(categoryId));
        v.add(name);
        v.add(instrumentType);
        v.add(description);
        v.add(instrumentText);
        String result;
        result = (String) xrpc.execute("blueShare.submitInstrument", v);
        return true;
    }
}

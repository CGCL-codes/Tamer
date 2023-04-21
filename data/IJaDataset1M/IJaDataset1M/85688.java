package preproc.ogip;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import nom.tam.fits.BasicHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import nom.tam.fits.TableHDU;
import nom.tam.util.Cursor;
import saadadb.meta.AttributeHandler;
import saadadb.util.Messenger;

/**
 * @author abouchacra
 *
 */
public class Product extends File {

    private static final long serialVersionUID = 1L;

    /** Name of the fits file */
    private String fileName;

    /** HashMap wich contains all the attributeHandler of the current spectrum */
    private HashMap<String, AttributeHandler> mapAttribute;

    /** Vector wich contains all the mapAttribute of each spectrum */
    private Vector<HashMap<String, AttributeHandler>> vMapAttribute;

    /** Vector wich contains all the parameters's combinations */
    private Vector<Vector<Float>> vCombi;

    /** Vector wich contains the name of each parameters */
    private Vector<String> vParamName;

    /** Vector wich contains all the ignored attributes for the attributeHandler */
    private Vector<String> vIgnored_att;

    /** Vector wich contain all the data field of each spectrum */
    private Vector<Vector<Float>> vSpectraData;

    /** The unit of the field */
    private String unit;

    /** The name of the field */
    private String fieldName;

    /** All the known ignored attributes */
    private static String[] tabIgnored = { "END", "COMMENT", "BITPIX", "SIMPLE", "EXTEND", "NAXIS" };

    public Product(String fileName) {
        super(fileName);
        this.fileName = fileName;
        this.mapAttribute = new HashMap<String, AttributeHandler>();
        this.vMapAttribute = new Vector<HashMap<String, AttributeHandler>>();
        this.vCombi = new Vector<Vector<Float>>();
        this.vParamName = new Vector<String>();
        this.vSpectraData = new Vector<Vector<Float>>();
        this.vIgnored_att = new Vector<String>();
        for (int i = 0; i < Product.tabIgnored.length; i++) this.vIgnored_att.add(Product.tabIgnored[i]);
        this.getFirstHDUAttributes();
        this.getParametersData();
        this.getParametersName();
        this.createAllMapAttributes();
        this.getSpectraData();
        this.getUnitAndName();
    }

    /**
	 * As we know, all the spectrum have identical attributeHandle.
	 * These attribute came from the first HDU, so this method gets all
	 * the comon attribute from the first HDU.
	 */
    public void getFirstHDUAttributes() {
        String key = "";
        String value = "";
        BasicHDU bHDU = null;
        try {
            Fits fits = new Fits(this.getAbsolutePath());
            bHDU = fits.getHDU(0);
        } catch (FitsException e) {
            Messenger.printStackTrace(e);
        } catch (IOException e) {
            Messenger.printStackTrace(e);
        }
        Header header = bHDU.getHeader();
        Cursor it = header.iterator();
        while (it.hasNext()) {
            HeaderCard hCard = (HeaderCard) it.next();
            AttributeHandler attribute = new AttributeHandler();
            key = hCard.getKey();
            if (!this.vIgnored_att.contains(key) && key != null) {
                attribute.setNameattr(key);
                attribute.setNameorg(key);
                attribute.setComment(hCard.getComment());
                value = hCard.getValue();
                attribute.setValue(value);
                if (value != null && !hCard.isStringValue()) {
                    if (value.charAt(0) == 'T' || value.charAt(0) == 'F') attribute.setType("boolean"); else if (value.indexOf("E") > 0) attribute.setType("double"); else if (value.indexOf(".") > 0) attribute.setType("float"); else attribute.setType("int");
                } else if (value != null) attribute.setType("String");
                this.mapAttribute.put(key, attribute);
            }
        }
    }

    /**
	 * This method get all values of each parameters.
	 * Then it creates all the possible combinations
	 * with those values.
	 */
    public void getParametersData() {
        TableHDU tHDU = null;
        float[] values = null;
        Vector<float[]> vValues = new Vector<float[]>();
        Vector<Vector<Float>> vCombiTmp = new Vector<Vector<Float>>();
        Vector<Float> vCombiCourant = new Vector<Float>();
        Vector<Float> vCombiCourantTmp = new Vector<Float>();
        try {
            Fits fits = new Fits(this.getAbsolutePath());
            tHDU = (TableHDU) fits.getHDU(1);
        } catch (FitsException e) {
            Messenger.printStackTrace(e);
        } catch (IOException e) {
            Messenger.printStackTrace(e);
        }
        int nbRow = tHDU.getNRows();
        for (int i = 0; i < nbRow; i++) {
            float[] tmp = null;
            try {
                tmp = (float[]) tHDU.getRow(i)[9];
                int[] tabNumbvals = (int[]) tHDU.getRow(i)[8];
                int numbVals = tabNumbvals[0];
                values = new float[numbVals];
                for (int j = 0; j < numbVals; j++) values[j] = tmp[j];
            } catch (FitsException e) {
                Messenger.printStackTrace(e);
            }
            vValues.add(values);
        }
        for (int i = 0; i < vValues.get(0).length; i++) {
            vCombiCourant = new Vector<Float>();
            vCombiCourant.add(vValues.get(0)[i]);
            this.vCombi.add(vCombiCourant);
        }
        for (int i = 1; i < vValues.size(); i++) {
            float[] tabCourant = vValues.get(i);
            for (int j = 0; j < this.vCombi.size(); j++) {
                vCombiCourant = this.vCombi.get(j);
                for (int k = 0; k < tabCourant.length; k++) {
                    vCombiCourantTmp = new Vector<Float>();
                    for (int m = 0; m < vCombiCourant.size(); m++) vCombiCourantTmp.add(vCombiCourant.get(m));
                    vCombiCourantTmp.add(tabCourant[k]);
                    vCombiTmp.add(vCombiCourantTmp);
                }
            }
            this.vCombi = new Vector<Vector<Float>>();
            for (int m = 0; m < vCombiTmp.size(); m++) this.vCombi.add(vCombiTmp.get(m));
            vCombiTmp = new Vector<Vector<Float>>();
        }
    }

    /**
	 * This method gets the name of each parameters.
	 */
    public void getParametersName() {
        TableHDU tHDU = null;
        String paramName = null;
        try {
            Fits fits = new Fits(this.getAbsolutePath());
            tHDU = (TableHDU) fits.getHDU(1);
            for (int i = 0; i < tHDU.getNRows(); i++) {
                paramName = (String) tHDU.getElement(i, 0);
                this.vParamName.add(paramName);
            }
        } catch (FitsException e) {
            Messenger.printStackTrace(e);
        } catch (IOException e) {
            Messenger.printStackTrace(e);
        }
    }

    /**
	 * This method creates all the mapAttributes for each spectrum.
	 * It takes the attributes from the first HDU, and add to it
	 * the attributes of each spectrum (parameters name and new combination of values).
	 */
    public void createAllMapAttributes() {
        Vector<Float> vCombiCourant = new Vector<Float>();
        for (int i = 0; i < this.vCombi.size(); i++) {
            vCombiCourant = this.vCombi.get(i);
            HashMap<String, AttributeHandler> currentMapAttribute = new HashMap<String, AttributeHandler>();
            currentMapAttribute.putAll(this.mapAttribute);
            for (int j = 0; j < this.vParamName.size(); j++) {
                AttributeHandler attribute = new AttributeHandler();
                attribute.setNameattr(this.vParamName.get(j));
                attribute.setNameorg(this.vParamName.get(j));
                attribute.setValue("" + vCombiCourant.get(j));
                attribute.setType("float");
                currentMapAttribute.put(this.vParamName.get(j), attribute);
            }
            this.vMapAttribute.add(currentMapAttribute);
        }
    }

    /**
	 * This method gets all the field's values of each spectrum.
	 */
    public void getSpectraData() {
        TableHDU tHDU = null;
        Vector<Float> vTmp = null;
        try {
            Fits fits = new Fits(this.getAbsolutePath());
            tHDU = (TableHDU) fits.getHDU(3);
        } catch (FitsException e) {
            Messenger.printStackTrace(e);
        } catch (IOException e) {
            Messenger.printStackTrace(e);
        }
        int nbRows = tHDU.getNRows();
        for (int i = 0; i < nbRows; i++) {
            vTmp = new Vector<Float>();
            float[] row = null;
            try {
                row = (float[]) tHDU.getRow(i)[1];
            } catch (FitsException e) {
                Messenger.printStackTrace(e);
            }
            for (int j = 0; j < row.length; j++) {
                vTmp.add(row[j]);
            }
            this.vSpectraData.add(vTmp);
        }
    }

    /**
	 * This method get the name and the unit of the field.
	 */
    public void getUnitAndName() {
        String key = "";
        BasicHDU bHDU = null;
        try {
            Fits fits = new Fits(this.getAbsolutePath());
            bHDU = fits.getHDU(3);
        } catch (FitsException e) {
            Messenger.printStackTrace(e);
        } catch (IOException e) {
            Messenger.printStackTrace(e);
        }
        Header header = bHDU.getHeader();
        Cursor it = header.iterator();
        while (it.hasNext()) {
            HeaderCard hCard = (HeaderCard) it.next();
            key = hCard.getKey();
            if (!this.vIgnored_att.contains(key) && key != null) {
                if (key.equals("TUNIT2")) this.unit = hCard.getValue();
                if (key.equals("TTYPE2")) this.fieldName = hCard.getValue();
            }
        }
    }

    public HashMap<String, AttributeHandler> getMapAttribute() {
        return mapAttribute;
    }

    public void setMapAttribute(HashMap<String, AttributeHandler> mapAttribute) {
        this.mapAttribute = mapAttribute;
    }

    public static String[] getTabIgnored() {
        return tabIgnored;
    }

    public static void setTabIgnored(String[] tabIgnored) {
        Product.tabIgnored = tabIgnored;
    }

    public Vector<String> getVIgnored_att() {
        return vIgnored_att;
    }

    public void setVIgnored_att(Vector<String> ignored_att) {
        vIgnored_att = ignored_att;
    }

    public Vector<Vector<Float>> getVCombi() {
        return vCombi;
    }

    public void setVCombi(Vector<Vector<Float>> combi) {
        vCombi = combi;
    }

    public Vector<HashMap<String, AttributeHandler>> getVMapAttribute() {
        return vMapAttribute;
    }

    public void setVMapAttribute(Vector<HashMap<String, AttributeHandler>> mapAttribute) {
        vMapAttribute = mapAttribute;
    }

    public Vector<String> getVParamName() {
        return vParamName;
    }

    public void setVParamName(Vector<String> paramName) {
        vParamName = paramName;
    }

    public Vector<Vector<Float>> getVSpectraData() {
        return vSpectraData;
    }

    public void setVSpectraData(Vector<Vector<Float>> spectraData) {
        vSpectraData = spectraData;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

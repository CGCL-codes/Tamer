package tpac.lib.DigitalLibrary;

import tpac.lib.DigitalLibrary.store.ArrayStore;
import java.util.Vector;
import java.util.Calendar;
import java.util.List;
import org.postgis.Geometry;
import org.postgis.Polygon;

/**
 * A "Product" is basically a standard name for an array.  This groups all arrays into a logical group despite varying
 * OPeNDAP names
 * @author Pauline Mak (pauline.mak@utas.edu.au)
 */
public class Product {

    private DBConnector connector;

    private String standardName;

    private String description;

    private int id;

    private ArrayStore arrayStore;

    /**
     * Empty constructor
     */
    public Product() {
    }

    /**
     * Basic constructor.  Values are meaningless when a Product is constructed using this method
     * @param _connector connection to the database
     */
    public Product(DBConnector _connector) {
        id = -1;
        connector = _connector;
        arrayStore = new ArrayStore(connector);
    }

    /**
     * Full constructor
     * @param _connector connection to the database
     * @param _id id of this Product
     * @param _standardName standard name of this Product (perhaps something out of ISO 19115)
     * @param _desc description of this product
     */
    public Product(DBConnector _connector, int _id, String _standardName, String _desc) {
        id = _id;
        connector = _connector;
        standardName = _standardName;
        description = _desc;
        arrayStore = new ArrayStore(connector);
    }

    /**
     * Setting the ID of this Product
     * @param _id id to set
     */
    public void setId(int _id) {
        id = _id;
    }

    /**
     * get the ID of this product
     * @return ID of this product
     */
    public int getId() {
        return id;
    }

    /**
     * Get the standard name of this product
     * @return standard name
     */
    public String getStandardName() {
        return standardName;
    }

    /**
     * Set the standard name of this product
     * @param _standardName
     */
    public void setStandardName(String _standardName) {
        standardName = _standardName;
    }

    /**
     * Get the description of this product
     * @return product description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this product
     * @param _description product description
     */
    public void setDescription(String _description) {
        description = _description;
    }
}

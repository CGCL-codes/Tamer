package jcontrol.conect.data;

import jcontrol.conect.data.settings.Settings;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * This class represents a hardware product
 */
public class HwProduct implements Serializable, Comparable {

    /** Class ID for object serialization */
    static final long serialVersionUID = 957880720372116540L;

    public static final int IS_ACCESSORY_PART = 0x0001;

    public static final int HAS_PHYSICAL_ADDR = 0x0002;

    public static final int DOWNLOAD_PHYS_ADDR = 0x0004;

    public static final int HAS_APPLIC_PROG = 0x0008;

    public static final int DOWNLOAD_APPLIC_PROG = 0x0010;

    public static final int IS_POWER_SUPPLY = 0x0020;

    public static final int IS_CHOKE = 0x0040;

    public static final int IS_COUPLER = 0x0080;

    public static final int HAS_PEI_PROGRAM = 0x0100;

    public static final int DOWNLOAD_PEI_PROGRAM = 0x0200;

    public static final int IS_REPEATER = 0x0400;

    public static final int IS_SIGNAL_FILTER = 0x0800;

    /** Name of the <code>productId</code> Field. */
    public static final String PRODUCT_ID = "productId";

    /** Name of the <code>manufacturerId</code> Field. */
    public static final String MANUFACTURER_ID = "manufacturerId";

    /** Name of the <code>symbolId</code> Field. */
    public static final String SYMBOL_ID = "symbolId";

    /** Name of the <code>bcuTypeId</code> Field. */
    public static final String BCU_TYPE_ID = "bcuTypeId";

    /** Name of the <code>originalManufacturerId</code> Field. */
    public static final String ORIGINAL_MANUFACTURER_ID = "originalManufacturerId";

    /** Name of the <code>productName</code> Field. */
    public static final String PRODUCT_NAME = "productName";

    /** Name of the <code>productVersionNumber</code> Field. */
    public static final String PRODUCT_VERSION_NUMBER = "productVersionNumber";

    /** Name of the <code>componentType</code> Field. */
    public static final String COMPONENT_TYPE = "componentType";

    /** Name of the <code>componentAttributes</code> Field. */
    public static final String COMPONENT_ATTRIBUTES = "componentAttributes";

    /** Name of the <code>busCurrent</code> Field. */
    public static final String BUS_CURRENT = "busCurrent";

    /** Name of the <code>productSerialNumber</code> Field. */
    public static final String PRODUCT_SERIAL_NUMBER = "productSerialNumber";

    /** Name of the <code>componentTypeNumber</code> Field. */
    public static final String COMPONENT_TYPE_NUMBER = "componentTypeNumber";

    /** Name of the <code>productPicture</code> Field. */
    public static final String PRODUCT_PICTURE = "productPicture";

    /** Name of the <code>productHandling</code> Field. */
    public static final String PRODUCT_HANDLING = "productHandling";

    /** Name of the <code>productClass</code> Field. */
    public static final String PRODUCT_CLASS = "productClass";

    /** Id of this HwProduct. (Key in Hashtable) */
    protected int productId;

    /** Id of Manufacturer. */
    protected int manufacturerId;

    /** Id of Symbol. */
    protected int symbolId;

    /** BcuType number. */
    protected int bcuTypeId;

    /** Original manufacturer id. */
    protected int originalManufacturerId;

    /** Manufacturer of product. */
    protected Manufacturer manufacturer;

    /** symbol of product. */
    protected Symbol symbol;

    /** BcuType of product. */
    protected BcuType bcuType;

    /** Original manufacturer of product. */
    protected Manufacturer originalManufacturer;

    /** ProductToProgram reference of product. */
    protected ProductToProgram[] productToProgram;

    /** Name of HwProduct. */
    protected String productName;

    /** Version number of HwProduct. */
    protected String productVersionNumber;

    /** Type of component. */
    protected String componentType;

    /** Attributes for component. */
    protected int componentAttributes;

    /** Bus current. */
    protected float busCurrent;

    /** Serial number of HwProduct. */
    protected String productSerialNumber;

    /** Number of component type. */
    protected int componentTypeNumber;

    /** Picture of product. */
    protected String productPicture;

    /** Handling of product (0: Standard, 1: Enhanced, 2: Complex, 3: dll) */
    protected int productHandling;

    /** Class filename for product handling. */
    protected String productClass;

    /**
     * Constructor with name of product.
     *
     * @param name name of HwProduct
     */
    public HwProduct(String name) {
        this.productName = name;
    }

    /**
     * Constructor with name and version number of product.
     *
     * @param name          name of HwProduct.
     * @param versionNumber version number of product
     */
    public HwProduct(String name, String versionNumber) {
        this(name);
        this.productVersionNumber = versionNumber;
    }

    /**
     * Default constructor.
     */
    public HwProduct() {
        super();
    }

    /**
     * Get the value of productId.
     * @return Value of productId.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Set the value of productId.
     * @param v  Value to assign to productId.
     */
    public void setProductId(int v) {
        this.productId = v;
    }

    /**
     * Get the value of manufacturerId.
     * @return Value of manufacturerId.
     */
    public int getManufacturerId() {
        return manufacturerId;
    }

    /**
     * Set the value of manufacturerId.
     * @param v  Value to assign to manufacturerId.
     */
    public void setManufacturerId(int v) {
        this.manufacturerId = v;
    }

    /**
     * Get the value of symbolId.
     * @return Value of symbolId.
     */
    public int getSymbolId() {
        return symbolId;
    }

    /**
     * Set the value of symbolId.
     * @param v  Value to assign to symbolId.
     */
    public void setSymbolId(int v) {
        this.symbolId = v;
    }

    /**
     * Get the value of originalManufacturerId.
     * @return Value of originalManufacturerId.
     */
    public int getOriginalManufacturerId() {
        return originalManufacturerId;
    }

    /**
     * Set the value of originalManufacturerId.
     * @param v  Value to assign to originalManufacturerId.
     */
    public void setOriginalManufacturerId(int v) {
        this.originalManufacturerId = v;
    }

    /**
     * Get the value of bcuTypeId.
     * @return Value of bcuTypeId.
     */
    public int getBcuTypeId() {
        return bcuTypeId;
    }

    /**
     * Set the value of bcuTypeId.
     * @param v  Value to assign to bcuTypeId.
     */
    public void setBcuTypeId(int v) {
        this.bcuTypeId = v;
    }

    /**
     * Get the value of manufacturer.
     * @return Value of manufacturer.
     */
    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    /**
     * Set the value of manufacturer.
     * @param v  Value to assign to manufacturer.
     */
    public void setManufacturer(Manufacturer v) {
        this.manufacturer = v;
    }

    /**
     * Get the value of symbol.
     * @return Value of symbol.
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * Set the value of symbol.
     * @param v  Value to assign to symbol.
     */
    public void setSymbol(Symbol v) {
        this.symbol = v;
    }

    /**
     * Get the value of bcuType.
     * @return Value of bcuType.
     */
    public BcuType getBcuType() {
        return bcuType;
    }

    /**
     * Set the value of bcuType.
     * @param v  Value to assign to bcuType.
     */
    public void setBcuType(BcuType v) {
        this.bcuType = v;
    }

    /**
     * Get the value of productToProgram.
     * @return Value of productToProgram.
     */
    public ProductToProgram[] getProductToProgram() {
        return productToProgram;
    }

    /**
     * Set the value of productToProgram.
     * @param v  Value to assign to productToProgram.
     */
    public void setProductToProgram(ProductToProgram[] v) {
        this.productToProgram = v;
    }

    /**
     * Get the value of originalManufacturer.
     * @return Value of originalManufacturer.
     */
    public Manufacturer getOriginalManufacturer() {
        return originalManufacturer;
    }

    /**
     * Set the value of originalManufacturer.
     * @param v  Value to assign to originalManufacturer.
     */
    public void setOriginalManufacturer(Manufacturer v) {
        this.originalManufacturer = v;
    }

    /**
     * Get the value of productName.
     * @return Value of productName.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Set the value of productName.
     * @param v  Value to assign to productName.
     */
    public void setProductName(String v) {
        this.productName = v;
    }

    /**
     * Get the value of productVersionNumber.
     * @return Value of productVersionNumber.
     */
    public String getProductVersionNumber() {
        return productVersionNumber;
    }

    /**
     * Set the value of productVersionNumber.
     * @param v  Value to assign to productVersionNumber.
     */
    public void setProductVersionNumber(String v) {
        this.productVersionNumber = v;
    }

    /**
     * Get the value of componentType.
     * @return Value of componentType.
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * Set the value of componentType.
     * @param v  Value to assign to componentType.
     */
    public void setComponentType(String v) {
        this.componentType = v;
    }

    /**
     * Get the value of componentAttributes.
     * @return Value of componentAttributes.
     */
    public int getComponentAttributes() {
        return componentAttributes;
    }

    /**
     * Set the value of componentAttributes.
     * @param v  Value to assign to componentAttributes.
     */
    public void setComponentAttributes(int v) {
        this.componentAttributes = v;
    }

    /**
     * Get the value of busCurrent.
     * @return Value of busCurrent.
     */
    public float getBusCurrent() {
        return busCurrent;
    }

    /**
     * Set the value of busCurrent.
     * @param v  Value to assign to busCurrent.
     */
    public void setBusCurrent(float v) {
        this.busCurrent = v;
    }

    /**
     * Get the value of productSerialNumber.
     * @return Value of productSerialNumber.
     */
    public String getProductSerialNumber() {
        return productSerialNumber;
    }

    /**
     * Set the value of productSerialNumber.
     * @param v  Value to assign to productSerialNumber.
     */
    public void setProductSerialNumber(String v) {
        this.productSerialNumber = v;
    }

    /**
     * Get the value of componentTypeNumber.
     * @return Value of componentTypeNumber.
     */
    public int getComponentTypeNumber() {
        return componentTypeNumber;
    }

    /**
     * Set the value of componentTypeNumber.
     * @param v  Value to assign to componentTypeNumber.
     */
    public void setComponentTypeNumber(int v) {
        this.componentTypeNumber = v;
    }

    /**
     * Get the value of productPicture.
     * @return Value of productPicture.
     */
    public String getProductPicture() {
        return productPicture;
    }

    /**
     * Set the value of productPicture.
     * @param v  Value to assign to productPicture.
     */
    public void setProductPicture(String v) {
        this.productPicture = v;
    }

    /**
     * Get the value of productHandling.
     * @return Value of productHandling.
     */
    public int getProductHandling() {
        return productHandling;
    }

    /**
     * Set the value of productHandling.
     * @param v  Value to assign to productHandling.
     */
    public void setProductHandling(int v) {
        this.productHandling = v;
    }

    /**
     * Get the value of productClass.
     * @return Value of productClass.
     */
    public String getProductClass() {
        return productClass;
    }

    /**
     * Set the value of productClass.
     * @param v  Value to assign to productClass.
     */
    public void setProductClass(String v) {
        this.productClass = v;
    }

    /**
     * Checks if this component is a accessory part.
     * @return true, if this component is a accessory part.
     */
    public boolean isAccessoryPart() {
        return (componentAttributes & IS_ACCESSORY_PART) == IS_ACCESSORY_PART;
    }

    /**
     * Checks if this component has a physical address.
     * @return true, if this component has a physical address.
     */
    public boolean hasPhysicalAddr() {
        return (componentAttributes & HAS_PHYSICAL_ADDR) == HAS_PHYSICAL_ADDR;
    }

    /**
     * Checks if download components physical address.
     * @return true, if download components physical address.
     */
    public boolean downloadPhysicalAddr() {
        return (componentAttributes & DOWNLOAD_PHYS_ADDR) == DOWNLOAD_PHYS_ADDR;
    }

    /**
     * Checks if this component has an application program.
     * @return true, if this component has an application program.
     */
    public boolean hasApplicProg() {
        return (componentAttributes & HAS_APPLIC_PROG) == HAS_APPLIC_PROG;
    }

    /**
     * Checks if download components application program
     * @return true, if download components application program.
     */
    public boolean downloadApplicProg() {
        return (componentAttributes & DOWNLOAD_APPLIC_PROG) == DOWNLOAD_APPLIC_PROG;
    }

    /**
     * Checks if this component is a power supply.
     * @return true, if this component is a power supply.
     */
    public boolean isPowerSupply() {
        return (componentAttributes & IS_POWER_SUPPLY) == IS_POWER_SUPPLY;
    }

    /**
     * Checks if this component is a coupler.
     * @return true, if this component is a coupler.
     */
    public boolean isCoupler() {
        return (componentAttributes & IS_COUPLER) == IS_COUPLER;
    }

    /**
     * Checks if this component is a choke.
     * @return true, if this component is a choke.
     */
    public boolean isChoke() {
        return (componentAttributes & IS_CHOKE) == IS_CHOKE;
    }

    /**
     * Checks if this component has a PEI program.
     * @return true, if this component has a PEI program.
     */
    public boolean hasPeiProgram() {
        return (componentAttributes & HAS_PEI_PROGRAM) == HAS_PEI_PROGRAM;
    }

    /**
     * Checks if download components PEI program.
     * @return true, if download components PEI program.
     */
    public boolean downloadPeiProgram() {
        return (componentAttributes & DOWNLOAD_PEI_PROGRAM) == DOWNLOAD_PEI_PROGRAM;
    }

    /**
     * Checks if this component is a repeater.
     * @return true, if this component is a repeater.
     */
    public boolean isRepeater() {
        return (componentAttributes & IS_REPEATER) == IS_REPEATER;
    }

    /**
     * Checks if this component is a signal filter.
     * @return true, if this component is a signal filter.
     */
    public boolean isSignalFilter() {
        return (componentAttributes & IS_SIGNAL_FILTER) == IS_SIGNAL_FILTER;
    }

    /**
     * Compares one HwProduct to another. The HwProduct's names are
     * used to determine their order.
     *
     * @param o the other HwProduct object
     * @return calls <code>productName.compareTo(((HwProduct)o).getProductName() );</code> 
     */
    public int compareTo(Object o) {
        return productName.compareTo(((HwProduct) o).getProductName());
    }

    /**
     * Prints all entrys of HwProduct.
     */
    public String toString() {
        String str = "";
        final String START = "<";
        final String EQUAL = "=";
        final String QUOTE = "\"";
        final String END = " />";
        final String COMMENT_START = "<!-- ";
        final String COMMENT_END = " -->";
        str += COMMENT_START + "HwProduct" + " " + productName + COMMENT_END + System.getProperty("line.separator");
        str += START;
        str += getClass().getName() + System.getProperty("line.separator");
        str += "\t" + PRODUCT_ID + EQUAL + QUOTE + productId + QUOTE + System.getProperty("line.separator");
        str += "\t" + MANUFACTURER_ID + EQUAL + QUOTE + manufacturerId + QUOTE + System.getProperty("line.separator");
        str += "\t" + SYMBOL_ID + EQUAL + QUOTE + symbolId + QUOTE + System.getProperty("line.separator");
        str += "\t" + BCU_TYPE_ID + EQUAL + QUOTE + bcuTypeId + QUOTE + System.getProperty("line.separator");
        str += "\t" + ORIGINAL_MANUFACTURER_ID + EQUAL + QUOTE + originalManufacturerId + QUOTE + System.getProperty("line.separator");
        if (productName == null) str += "\t" + PRODUCT_NAME + EQUAL + QUOTE + QUOTE + System.getProperty("line.separator"); else str += "\t" + PRODUCT_NAME + EQUAL + QUOTE + productName + QUOTE + System.getProperty("line.separator");
        if (productVersionNumber == null) str += "\t" + PRODUCT_VERSION_NUMBER + EQUAL + QUOTE + QUOTE + System.getProperty("line.separator"); else str += "\t" + PRODUCT_VERSION_NUMBER + EQUAL + QUOTE + productVersionNumber + QUOTE + System.getProperty("line.separator");
        if (componentType == null) str += "\t" + COMPONENT_TYPE + EQUAL + QUOTE + QUOTE + System.getProperty("line.separator"); else str += "\t" + COMPONENT_TYPE + EQUAL + QUOTE + componentType + QUOTE + System.getProperty("line.separator");
        str += "\t" + COMPONENT_ATTRIBUTES + EQUAL + QUOTE + componentAttributes + QUOTE + System.getProperty("line.separator");
        str += "\t" + BUS_CURRENT + EQUAL + QUOTE + busCurrent + QUOTE + System.getProperty("line.separator");
        if (productSerialNumber == null) str += "\t" + PRODUCT_SERIAL_NUMBER + EQUAL + QUOTE + QUOTE + System.getProperty("line.separator"); else str += "\t" + PRODUCT_SERIAL_NUMBER + EQUAL + QUOTE + productSerialNumber + QUOTE + System.getProperty("line.separator");
        str += "\t" + COMPONENT_TYPE_NUMBER + EQUAL + QUOTE + componentTypeNumber + QUOTE + System.getProperty("line.separator");
        if (productPicture == null) str += "\t" + PRODUCT_PICTURE + EQUAL + QUOTE + QUOTE + System.getProperty("line.separator"); else str += "\t" + PRODUCT_PICTURE + EQUAL + QUOTE + productPicture + QUOTE + System.getProperty("line.separator");
        str += "\t" + PRODUCT_HANDLING + EQUAL + QUOTE + productHandling + QUOTE + System.getProperty("line.separator");
        if (productClass == null) str += "\t" + PRODUCT_CLASS + EQUAL + QUOTE + QUOTE + END + System.getProperty("line.separator"); else str += "\t" + PRODUCT_CLASS + EQUAL + QUOTE + productClass + QUOTE + END + System.getProperty("line.separator");
        return str;
    }
}

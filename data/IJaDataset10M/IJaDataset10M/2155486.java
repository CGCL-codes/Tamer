package freets.data.xml;

import freets.tools.*;
import freets.data.*;
import freets.data.settings.*;
import com.sun.xml.tree.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.reflect.*;

/**
 * <B>IoUtils</B> get Java object or save a Java object to
 * an XML file
 *
 * @version	 : 1.0
 * @author	 : W. Sauter
 */
public class XmlIoUtils {

    /**
     * Default Constructor
     *
     * @param
     */
    public XmlIoUtils() {
    }

    /**
     * Parse a xml-file and return <code>Document</code>.
     */
    public static Document getDocument(URL url) {
        Document doc = null;
        try {
            InputStream i = url.openStream();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(i);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            LocalizedError.display(e);
        }
        return doc;
    }

    /**
     * Parse a xml-file and return <code>Document</code>.
     */
    public static Document getDocument(File file) {
        Document doc = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(file);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            LocalizedError.display(e);
        }
        return doc;
    }

    /**
     *  Write objects in a hashtable to a xml file. The objects must implement
     * the toString-methode and this methode must return a string that 
     * represents the objects in xml format.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of objects.
     */
    public static void saveObjects(PrintWriter pw, Hashtable data) {
        Enumeration e = data.elements();
        while (e.hasMoreElements()) {
            pw.println(e.nextElement());
        }
        pw.flush();
    }

    /**
     *  Write an object in a hashtable to a xml file. The object must implement
     * the toString-methode and this methode must return a string that 
     * represents the object in xml format.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of objects.
     */
    public static void saveObject(PrintWriter pw, Object data) {
        pw.println(data);
        pw.flush();
    }

    /**
     * Return a <code>Manufacturer</code> object.
     *
     * @param element the node with <code>Manufacturer</code> adat.
     * @return a <code>Manufacturer</code>
     */
    public static Manufacturer parseManufacturer(Element element) {
        Manufacturer manufacturer;
        manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(Integer.parseInt(element.getAttribute(Manufacturer.MANUFACTURER_ID)));
        manufacturer.setManufacturerName(element.getAttribute(Manufacturer.MANUFACTURER_NAME));
        try {
            String url = element.getAttribute(Manufacturer.URL);
            if (!url.equals("")) manufacturer.setUrl(new URL(url));
        } catch (MalformedURLException mex) {
            LocalizedError.display(mex);
        }
        manufacturer.setImported(Boolean.valueOf(element.getAttribute(Manufacturer.IMPORTED)).booleanValue());
        manufacturer.setFilter(Boolean.valueOf(element.getAttribute(Manufacturer.FILTER)).booleanValue());
        manufacturer.setAddressId(Integer.parseInt(element.getAttribute(Manufacturer.ADDRESS_ID)));
        return manufacturer;
    }

    /**
     * Return <code>Manufacturer</code> objects.
     *
     * @param doc parsed xml-file
     * @return a Hashtable with <code>Manufacturer</code>s
     */
    public static Hashtable getManufacturers(Document doc) {
        final String ROOT_ELEMENT_TAG = Manufacturer.class.getName();
        Hashtable manus = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        Manufacturer manufacturer;
        for (int i = 0; i < size; i++) {
            manufacturer = new Manufacturer();
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            manufacturer = parseManufacturer(row);
            manus.put(new Integer(manufacturer.getManufacturerId()), manufacturer);
        }
        return manus;
    }

    /**
     *  Write <code>Manufacturer</code>s in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of Manufacturer objects.
     */
    public static void saveManufacturers(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Return a <code>BcuType</code> object.
     *
     * @param element the node with <code>BcuType</code> data.
     * @return a <code>BcuType</code>
     */
    public static BcuType parseBcuType(Element element) {
        BcuType bcuType = new BcuType();
        bcuType.setBcuTypeId(Integer.parseInt(element.getAttribute(BcuType.BCU_TYPE_ID)));
        bcuType.setBcuTypeName(element.getAttribute(BcuType.BCU_TYPE_NAME));
        bcuType.setBcuTypeCpu(element.getAttribute(BcuType.BCU_TYPE_CPU));
        return bcuType;
    }

    public static Hashtable getBcuTypes(Document doc) {
        final String ROOT_ELEMENT_TAG = BcuType.class.getName();
        Hashtable bcus = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        BcuType bcuType;
        for (int i = 0; i < size; i++) {
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            bcuType = parseBcuType(row);
            bcus.put(new Integer(bcuType.getBcuTypeId()), bcuType);
        }
        return bcus;
    }

    /**
     *  Write <code>BcuType</code>s in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of BcuType objects.
     */
    public static void saveBcuTypes(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Return a <code>PeiType</code> object.
     *
     * @param element the node with <code>PeiType</code> data.
     * @return a <code>PeiType</code>
     */
    public static PeiType parsePeiType(Element element) {
        PeiType peiType = new PeiType();
        peiType.setPeiTypeId(Integer.parseInt(element.getAttribute(PeiType.PEI_TYPE_ID)));
        peiType.setPeiTypeName(element.getAttribute(PeiType.PEI_TYPE_NAME));
        return peiType;
    }

    public static Hashtable getPeiTypes(Document doc) {
        final String ROOT_ELEMENT_TAG = PeiType.class.getName();
        Hashtable peis = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        PeiType peiType;
        for (int i = 0; i < size; i++) {
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            peiType = parsePeiType(row);
            peis.put(new Integer(peiType.getPeiTypeId()), peiType);
        }
        return peis;
    }

    /**
     *  Write <code>PeiType</code>s in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of <code>PeiType</code> objects.
     */
    public static void savePeiTypes(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Return a <code>ParameterAtomicType</code> object.
     *
     * @param element the node with <code>ParameterAtomicType</code> data.
     * @return a <code>ParameterAtomicType</code>
     */
    public static ParameterAtomicType parseParameterAtomicType(Element element) {
        ParameterAtomicType parameterAtomicType = new ParameterAtomicType();
        parameterAtomicType.setAtomicTypeNumber(Integer.parseInt(element.getAttribute(ParameterAtomicType.ATOMIC_TYPE_NUMBER)));
        parameterAtomicType.setAtomicTypeName(element.getAttribute(ParameterAtomicType.ATOMIC_TYPE_NAME));
        parameterAtomicType.setDispattr(element.getAttribute(ParameterAtomicType.DISPATTR));
        return parameterAtomicType;
    }

    public static Hashtable getParameterAtomicTypes(Document doc) {
        final String ROOT_ELEMENT_TAG = ParameterAtomicType.class.getName();
        Hashtable parameterAtomicTypes = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        ParameterAtomicType parameterAtomicType;
        for (int i = 0; i < size; i++) {
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            parameterAtomicType = parseParameterAtomicType(row);
            parameterAtomicTypes.put(new Integer(parameterAtomicType.getAtomicTypeNumber()), parameterAtomicType);
        }
        return parameterAtomicTypes;
    }

    /**
     *  Write <code>ParameterAtomicType</code>s in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of <code>ParameterAtomiType</code> objects.
     */
    public static void saveParameterAtomicTypes(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Return a <code>Symbol</code> object.
     *
     * @param element the node with <code>Symbol</code> dats.
     * @return a <code>Symbol</code>
     */
    public static Symbol parseSymbol(Element element) {
        Symbol symbol;
        symbol = new Symbol();
        symbol.setSymbolId(Integer.parseInt(element.getAttribute(Symbol.SYMBOL_ID)));
        symbol.setSymbolName(element.getAttribute(Symbol.SYMBOL_NAME));
        symbol.setSymbolFilename(element.getAttribute(Symbol.SYMBOL_FILENAME));
        return symbol;
    }

    public static Hashtable getSymbols(Document doc) {
        final String ROOT_ELEMENT_TAG = Symbol.class.getName();
        Hashtable symbols = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        Symbol symbol;
        for (int i = 0; i < size; i++) {
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            symbol = parseSymbol(row);
            symbols.put(new Integer(symbol.getSymbolId()), symbol);
        }
        return symbols;
    }

    /**
     *  Write <code>Symbol</code>s in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of Symbols.
     */
    public static void saveSymbols(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Return a <code>MediumType</code> object.
     *
     * @param element the node with <code>MediumType</code> data.
     * @return a <code>MediumType</code>
     */
    public static MediumType parseMediumType(Element element) {
        MediumType mediumType = new MediumType();
        mediumType.setMediumTypeNumber(Integer.parseInt(element.getAttribute(MediumType.MEDIUM_TYPE_NUMBER)));
        mediumType.setMediumTypeName(element.getAttribute(MediumType.MEDIUM_TYPE_NAME));
        mediumType.setMediumTypeShortName(element.getAttribute(MediumType.MEDIUM_TYPE_SHORT_NAME));
        return mediumType;
    }

    public static Hashtable getMediumTypes(Document doc) {
        final String ROOT_ELEMENT_TAG = MediumType.class.getName();
        Hashtable mediumTypes = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        MediumType mediumType;
        for (int i = 0; i < size; i++) {
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            mediumType = parseMediumType(row);
            mediumTypes.put(new Integer(mediumType.getMediumTypeNumber()), mediumType);
        }
        return mediumTypes;
    }

    /**
     *  Write <code>MediumType</code>s in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of <code>MediumType</code> objects.
     */
    public static void saveMediumTypes(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Return a <code>MaskEntry</code> object.
     *
     * @param element the node with <code>MaskEntry</code> data.
     * @return a <code>MaskEntry</code>
     */
    public static MaskEntry parseMaskEntry(Element element) {
        MaskEntry maskEntry = new MaskEntry();
        maskEntry.setMaskEntryId(Integer.parseInt(element.getAttribute(MaskEntry.MASK_ENTRY_ID)));
        maskEntry.setMaskId(Integer.parseInt(element.getAttribute(MaskEntry.MASK_ID)));
        maskEntry.setMaskEntryName(element.getAttribute(MaskEntry.MASK_ENTRY_NAME));
        maskEntry.setMaskEntryAddress(Integer.parseInt(element.getAttribute(MaskEntry.MASK_ENTRY_ADDRESS)));
        return maskEntry;
    }

    public static Hashtable getMaskEntries(Document doc) {
        final String ROOT_ELEMENT_TAG = MaskEntry.class.getName();
        Hashtable maskEntries = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        MaskEntry maskEntry;
        for (int i = 0; i < size; i++) {
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            maskEntry = parseMaskEntry(row);
            maskEntries.put(new Integer(maskEntry.getMaskEntryId()), maskEntry);
        }
        return maskEntries;
    }

    /**
     *  Write <code>MasksEntries</code> in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of <code>MaskEntry</code> objects.
     */
    public static void saveMaskEntries(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Return a <code>MaskSystemPointer</code> object.
     *
     * @param element the node with <code>MaskSystemPointer</code> data.
     * @return a <code>MaskSystemPointer</code>
     */
    public static MaskSystemPointer parseMaskSystemPointer(Element element) {
        MaskSystemPointer maskSystemPointer = new MaskSystemPointer();
        maskSystemPointer.setMaskSystemPointerId(Integer.parseInt(element.getAttribute(MaskSystemPointer.MASK_SYSTEM_POINTER_ID)));
        maskSystemPointer.setMaskId(Integer.parseInt(element.getAttribute(MaskSystemPointer.MASK_ID)));
        maskSystemPointer.setSystemPointerName(element.getAttribute(MaskSystemPointer.SYSTEM_POINTER_NAME));
        maskSystemPointer.setSystemPointerAddress(Integer.parseInt(element.getAttribute(MaskSystemPointer.SYSTEM_POINTER_ADDRESS)));
        return maskSystemPointer;
    }

    public static Hashtable getMaskSystemPointers(Document doc) {
        final String ROOT_ELEMENT_TAG = MaskSystemPointer.class.getName();
        Hashtable maskSystemPointers = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        MaskSystemPointer maskSystemPointer;
        for (int i = 0; i < size; i++) {
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            maskSystemPointer = parseMaskSystemPointer(row);
            maskSystemPointers.put(new Integer(maskSystemPointer.getMaskSystemPointerId()), maskSystemPointer);
        }
        return maskSystemPointers;
    }

    /**
     *  Write <code>MaskSystemPointer</code>s in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of <code>MaskSystemPointer</code> objects.
     */
    public static void saveMaskSystemPointers(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Return a <code>Mask</code> object.
     *
     * @param element the node with <code>Mask</code> dats.
     * @return a <code>Mask</code>
     */
    public static Mask parseMask(Element element) {
        Mask mask;
        mask = new Mask();
        mask.setMaskId(Integer.parseInt(element.getAttribute(Mask.MASK_ID)));
        mask.setMaskVersion(Integer.parseInt(element.getAttribute(Mask.MASK_VERSION)));
        mask.setUserRamStart(Integer.parseInt(element.getAttribute(Mask.USER_RAM_START)));
        mask.setUserRamEnd(Integer.parseInt(element.getAttribute(Mask.USER_RAM_END)));
        mask.setUserEepromStart(Integer.parseInt(element.getAttribute(Mask.USER_EEPROM_START)));
        mask.setUserEepromEnd(Integer.parseInt(element.getAttribute(Mask.USER_EEPROM_END)));
        mask.setRunErrorAddress(Integer.parseInt(element.getAttribute(Mask.RUN_ERROR_ADDRESS)));
        mask.setAddressTabAddress(Integer.parseInt(element.getAttribute(Mask.ADDRESS_TAB_ADDRESS)));
        mask.setAssocTabPtrAddress(Integer.parseInt(element.getAttribute(Mask.ASSOC_TAB_PTR_ADDRESS)));
        mask.setCommsTabPtrAddress(Integer.parseInt(element.getAttribute(Mask.COMMS_TAB_PTR_ADDRESS)));
        mask.setManufacturerDataAddress(Integer.parseInt(element.getAttribute(Mask.MANUFACTURER_DATA_ADDRESS)));
        mask.setManufacturerDataSize(Integer.parseInt(element.getAttribute(Mask.MANUFACTURER_DATA_SIZE)));
        mask.setManufacturerIdAddress(Integer.parseInt(element.getAttribute(Mask.MANUFACTURER_ID_ADDRESS)));
        mask.setRoutecntAddress(Integer.parseInt(element.getAttribute(Mask.ROUTE_CNT_ADDRESS)));
        mask.setManufacturerIdProtected(Integer.parseInt(element.getAttribute(Mask.MANUFACTURER_ID_PROTECTED)));
        mask.setMaskVersionName(element.getAttribute(mask.MASK_VERSION_NAME));
        String hexEepromData = element.getAttribute(Mask.MASK_EEPROM_DATA);
        byte[] eepromData = new byte[hexEepromData.length() / 2];
        int index = 0;
        for (int j = 0; j < hexEepromData.length() / 2; j++) {
            eepromData[j] = (byte) Integer.parseInt(hexEepromData.substring(index, index + 2), 16);
            index += 2;
        }
        mask.setMaskEepromData(eepromData);
        mask.setMaskDataLength(Integer.parseInt(element.getAttribute(Mask.MASK_DATA_LENGTH)));
        mask.setAddressTabLcs(Integer.parseInt(element.getAttribute(Mask.ADDRESS_TAB_LCS)));
        mask.setAssocTabLcs(Integer.parseInt(element.getAttribute(Mask.ASSOC_TAB_LCS)));
        mask.setApplicationProgramLcs(Integer.parseInt(element.getAttribute(Mask.APPLICATION_PROGRAM_LCS)));
        mask.setPeiProgramLcs(Integer.parseInt(element.getAttribute(Mask.PEI_PROGRAM_LCS)));
        mask.setLoadControlAddress(Integer.parseInt(element.getAttribute(Mask.LOAD_CONTROL_ADDRESS)));
        mask.setRunControlAddress(Integer.parseInt(element.getAttribute(Mask.RUN_CONTROL_ADDRESS)));
        mask.setExternalMemoryStart(Integer.parseInt(element.getAttribute(Mask.EXTERNAL_MEMORY_START)));
        mask.setExternalMemoryEnd(Integer.parseInt(element.getAttribute(Mask.EXTERNAL_MEMORY_END)));
        mask.setApplicationProgramRcs(Integer.parseInt(element.getAttribute(Mask.APPLICATION_PROGRAM_RCS)));
        mask.setPeiProgramRcs(Integer.parseInt(element.getAttribute(Mask.PEI_PROGRAM_RCS)));
        mask.setPortAddr(Integer.parseInt(element.getAttribute(Mask.PORT_ADDR)));
        mask.setPortAddressProtected(Integer.parseInt(element.getAttribute(Mask.PORT_ADDRESS_PROTECTED)));
        return mask;
    }

    /**
     * Help methode to set the references for the parsed masks.
     * 
     * @param mask the <code>Mask</code> refernces set for.
     * @param maskEntries Hashtable with <code>MaskEntries</code>.
     * @param maskSystemPointers Hashtable with 
     *                           <code>MaskSystemPointer</code>s.
     */
    private static void setMaskReferences(Mask mask, Hashtable maskEntries, Hashtable maskSystemPointers) {
        Vector foundMaskEntries = new Vector();
        Enumeration maskEntryElements = maskEntries.elements();
        MaskEntry maskEntry;
        while (maskEntryElements.hasMoreElements()) {
            maskEntry = (MaskEntry) maskEntryElements.nextElement();
            if (maskEntry.getMaskId() == mask.getMaskId()) {
                maskEntry.setMask(mask);
                foundMaskEntries.addElement(maskEntry);
            }
        }
        mask.setMaskEntries(foundMaskEntries);
        Vector foundSystemPointers = new Vector();
        Enumeration systemPointerElements = maskSystemPointers.elements();
        MaskSystemPointer systemPointer;
        while (systemPointerElements.hasMoreElements()) {
            systemPointer = (MaskSystemPointer) systemPointerElements.nextElement();
            if (systemPointer.getMaskId() == mask.getMaskId()) {
                systemPointer.setMask(mask);
                foundSystemPointers.addElement(systemPointer);
            }
        }
        mask.setMaskSystemPointers(foundSystemPointers);
    }

    public static Hashtable getMasks(Document doc) {
        final String ROOT_ELEMENT_TAG = Mask.class.getName();
        Hashtable masks = new Hashtable();
        int size = XmlUtils.getSize(doc, ROOT_ELEMENT_TAG);
        Mask mask;
        for (int i = 0; i < size; i++) {
            Element row = XmlUtils.getElement(doc, ROOT_ELEMENT_TAG, i);
            mask = parseMask(row);
            masks.put(new Integer(mask.getMaskId()), mask);
        }
        Hashtable maskEntries = getMaskEntries(doc);
        Hashtable maskSystemPointers = getMaskSystemPointers(doc);
        Enumeration maskElements = masks.elements();
        while (maskElements.hasMoreElements()) {
            mask = (Mask) maskElements.nextElement();
            setMaskReferences(mask, maskEntries, maskSystemPointers);
        }
        return masks;
    }

    /**
     *  Write <code>MaskSystemPointer</code>s in a hashtable to a xml file.
     *
     * @param pw   <code>PrintWriter</code> for xml file.
     * @param data the collection of <code>MaskSystemPointer</code> objects.
     */
    public static void saveMasks(PrintWriter pw, Hashtable data) {
        saveObjects(pw, data);
    }

    /**
     * Parse an xml file and setting freets default data in 
     * <code>Settings</code>.
     *
     * @param doc xml-file to parse.
     */
    public static void getDefaultData(Document doc) {
        Hashtable manufacturers = new Hashtable();
        Hashtable symbols = new Hashtable();
        Hashtable bcuTypes = new Hashtable();
        Hashtable peiTypes = new Hashtable();
        Hashtable parameterAtomicTypes = new Hashtable();
        Hashtable mediumTypes = new Hashtable();
        Hashtable masks = new Hashtable();
        Hashtable maskEntries = new Hashtable();
        Hashtable maskSystemPointers = new Hashtable();
        Node rootNode = doc.getLastChild();
        NodeList classNodes = rootNode.getChildNodes();
        Node node;
        Class elementClass = null;
        Object elementObject = null;
        for (int i = 0; i < classNodes.getLength(); i++) {
            node = classNodes.item(i);
            if ((node.getNodeType() == Node.ELEMENT_NODE)) {
                String className = node.getNodeName();
                try {
                    elementClass = Class.forName(className, true, freets.tools.FreeTsClassLoader.loader);
                } catch (ClassNotFoundException cnfex) {
                    DebugDialog.print("Class " + className + "not found.", 5);
                    LocalizedError.display(cnfex);
                }
                try {
                    elementObject = elementClass.newInstance();
                } catch (Exception ex) {
                    LocalizedError.display(ex);
                }
                Method method;
                Class[] parameterTypes = { Element.class };
                Object[] args = { node };
                if (elementObject instanceof Manufacturer) {
                    try {
                        method = elementClass.getDeclaredMethod("parseManufacturer", parameterTypes);
                        Object manu = method.invoke(elementObject, args);
                        manufacturers.put(new Integer(((Manufacturer) manu).getManufacturerId()), manu);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                } else if (elementObject instanceof Symbol) {
                    try {
                        method = elementClass.getDeclaredMethod("parseSymbol", parameterTypes);
                        Object symbol = method.invoke(elementObject, args);
                        symbols.put(new Integer(((Symbol) symbol).getSymbolId()), symbol);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                } else if (elementObject instanceof BcuType) {
                    try {
                        method = elementClass.getDeclaredMethod("parseBcuType", parameterTypes);
                        Object bcuType = method.invoke(elementObject, args);
                        bcuTypes.put(new Integer(((BcuType) bcuType).getBcuTypeId()), bcuType);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                } else if (elementObject instanceof PeiType) {
                    try {
                        method = elementClass.getDeclaredMethod("parsePeiType", parameterTypes);
                        Object peiType = method.invoke(elementObject, args);
                        peiTypes.put(new Integer(((PeiType) peiType).getPeiTypeId()), peiType);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                } else if (elementObject instanceof ParameterAtomicType) {
                    try {
                        method = elementClass.getDeclaredMethod("parseParameterAtomicType", parameterTypes);
                        Object parameterAtomicType = method.invoke(elementObject, args);
                        parameterAtomicTypes.put(new Integer(((ParameterAtomicType) parameterAtomicType).getAtomicTypeNumber()), parameterAtomicType);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                } else if (elementObject instanceof MediumType) {
                    try {
                        method = elementClass.getDeclaredMethod("parseMediumType", parameterTypes);
                        Object mediumType = method.invoke(elementObject, args);
                        mediumTypes.put(new Integer(((MediumType) mediumType).getMediumTypeNumber()), mediumType);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                } else if (elementObject instanceof Mask) {
                    try {
                        method = elementClass.getDeclaredMethod("parseMask", parameterTypes);
                        Object mask = method.invoke(elementObject, args);
                        masks.put(new Integer(((Mask) mask).getMaskId()), mask);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                } else if (elementObject instanceof MaskEntry) {
                    try {
                        method = elementClass.getDeclaredMethod("parseMaskEntry", parameterTypes);
                        Object maskEntry = method.invoke(elementObject, args);
                        maskEntries.put(new Integer(((MaskEntry) maskEntry).getMaskEntryId()), maskEntry);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                } else if (elementObject instanceof MaskSystemPointer) {
                    try {
                        method = elementClass.getDeclaredMethod("parseMaskSystemPointer", parameterTypes);
                        Object maskSystemPointer = method.invoke(elementObject, args);
                        maskSystemPointers.put(new Integer(((MaskSystemPointer) maskSystemPointer).getMaskSystemPointerId()), maskSystemPointer);
                    } catch (Exception e) {
                        LocalizedError.display(e);
                    }
                }
            }
        }
        Enumeration maskElements = masks.elements();
        Mask mask;
        while (maskElements.hasMoreElements()) {
            mask = (Mask) maskElements.nextElement();
            setMaskReferences(mask, maskEntries, maskSystemPointers);
        }
        Settings.setManufacturers(manufacturers);
        Settings.setBcuTypes(bcuTypes);
        Settings.setPeiTypes(peiTypes);
        Settings.setParameterAtomicTypes(parameterAtomicTypes);
        Settings.setSymbols(symbols);
        Settings.setMediumTypes(mediumTypes);
        Settings.setMasks(masks);
    }

    /**
     *  Generating an xml file and writing freets default data to it.
     *
     * @param fileName name of file where data write to.
     */
    public static void saveDefaultData(String fileName) {
        final String INITIAL_BUNDLE = Settings.PROPERTIES_PATH + "data/XML";
        final String START = "<DefaultData>";
        final String END = "</DefaultData>";
        ResourceBundle resources = ResourceBundle.getBundle(INITIAL_BUNDLE, Options.defaultOptions.getFreeTsLocale(), freets.tools.FreeTsClassLoader.loader);
        Hashtable data;
        try {
            FileOutputStream fo = new FileOutputStream(fileName);
            PrintWriter pw = new PrintWriter(fo);
            pw.println(resources.getString("xml.default.data.dtd"));
            pw.println(START);
            data = Settings.getManufacturers();
            saveManufacturers(pw, data);
            data = Settings.getBcuTypes();
            saveBcuTypes(pw, data);
            data = Settings.getPeiTypes();
            savePeiTypes(pw, data);
            data = Settings.getParameterAtomicTypes();
            saveParameterAtomicTypes(pw, data);
            data = Settings.getSymbols();
            saveSymbols(pw, data);
            data = Settings.getMediumTypes();
            saveMediumTypes(pw, data);
            data = Settings.getMasks();
            saveMasks(pw, data);
            Mask mask;
            Enumeration e = Settings.getMasks().elements();
            Object[] o;
            while (e.hasMoreElements()) {
                mask = (Mask) e.nextElement();
                o = mask.getMaskEntries().toArray();
                if (o != null) {
                    for (int i = 0; i < o.length; i++) {
                        saveObject(pw, o[i]);
                    }
                }
                o = mask.getMaskSystemPointers().toArray();
                if (o != null) {
                    for (int i = 0; i < o.length; i++) {
                        saveObject(pw, o[i]);
                    }
                }
            }
            pw.println(END);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            LocalizedError.display(e);
        }
    }
}

package jcontrol.conect.data.conversion;

import jcontrol.conect.data.*;
import jcontrol.conect.data.settings.*;
import java.util.*;
import java.io.*;

/**
 * Class to parse ETS2 elemnts out of, e.g. a <code>vd_</code> file.
 */
public class ElementParser {

    /** Name of ResourceBundle for this class. */
    protected static final String INITIAL_BUNDLE = Settings.PROPERTIES_PATH + "data/ConversionThread";

    /** ResourcsBundle or this class. */
    protected static ResourceBundle resources = ResourceBundle.getBundle(INITIAL_BUNDLE, Options.defaultOptions.getConectLocale(), jcontrol.conect.tools.ConectClassLoader.loader);

    /** Empty String. */
    protected static final String EMPTY = resources.getString("conversion.empty.string");

    /** The "\\" that indicates that the line belongs to the previos. */
    protected static final String BACK_SLASH_SLASH = resources.getString("conversion.back.slash.slash");

    /**
     * Parse a single <code>AddressFixup</code>.
     *
     * @param in the <code>BufferedReader</code> that reads the file.
     * @param currentLine current line in vd_ file.
     * @param addressFixup The parsed <code>AddressFixup</code>.
     *
     * @return the current line after parsing of an <code>AddressFixup</code>.
     */
    public static String getAddressFixup(BufferedReader in, String currentLine, AddressFixup addressFixup) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) addressFixup.setFixupId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) addressFixup.setProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) addressFixup.setFixupType(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) addressFixup.setFixupName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            addressFixup.setFixupName(addressFixup.getFixupName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) addressFixup.setFixupAddress(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>ApplicationProgram</code>
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param program the <code>ApplicationProgram</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getApplicationProgram(BufferedReader in, String currentLine, ApplicationProgram program) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setSymbolId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setMaskId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setProgramName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            program.setProgramName(program.getProgramName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) program.setProgramVersion(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            program.setProgramVersion(program.getProgramVersion() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) program.setProgramVersionNumber(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            program.setProgramVersionNumber(program.getProgramVersionNumber() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) {
            int i;
            i = Integer.parseInt(currentLine);
            if (i == 0) program.setLinkable(false); else program.setLinkable(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setDeviceType(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setPeiType(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setAddressTabSize(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setAssoctabAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setAssoctabSize(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setCommstabAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setCommstabSize(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setProgramSerialNumber(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            program.setProgramSerialNumber(program.getProgramSerialNumber() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) program.setManufacturerId(Integer.parseInt(currentLine));
        String hexEepromData = null;
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) hexEepromData = currentLine;
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            hexEepromData += currentLine.substring(BACK_SLASH_SLASH.length());
            currentLine = in.readLine();
        }
        if (hexEepromData != null) {
            byte[] eepromData = new byte[hexEepromData.length() / 2];
            int index = 0;
            for (int j = 0; j < eepromData.length; j++) {
                eepromData[j] = (byte) Integer.parseInt(hexEepromData.substring(index, index + 2), 16);
                index += 2;
            }
            program.setEepromData(eepromData);
        }
        if (!currentLine.equals(EMPTY)) program.setDataLength(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setS19File(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            program.setS19File(program.getS19File() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) program.setMapFile(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            program.setMapFile(program.getMapFile() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) program.setAssemblerId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        currentLine = in.readLine();
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int i;
            i = Integer.parseInt(currentLine);
            if (i == 0) program.setDynamicManagement(false); else program.setDynamicManagement(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setProgramType(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setRamSize(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setOriginalManufacturerId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setApiVersion(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setProgramStyle(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int i;
            i = Integer.parseInt(currentLine);
            if (i == 0) program.setIsPollingMaster(false); else program.setIsPollingMaster(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) program.setNumberOfPollingGroups(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>Assembler</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param assembler the <code>Assembler</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getAssembler(BufferedReader in, String currentLine, Assembler assembler) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) assembler.setAssemblerId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) assembler.setAssemblerName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            assembler.setAssemblerName(assembler.getAssemblerName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) assembler.setAssemblerType(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>BcuType</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param bcuType the <code>BcuType</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getBcuType(BufferedReader in, String currentLine, BcuType bcuType) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) bcuType.setBcuTypeId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) bcuType.setBcuTypeName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            bcuType.setBcuTypeName(bcuType.getBcuTypeName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) bcuType.setBcuTypeCpu(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            bcuType.setBcuTypeCpu(bcuType.getBcuTypeCpu() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>CatalogEntry</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param catalogEntry the <code>CatalogEntry</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getCatalogEntry(BufferedReader in, String currentLine, CatalogEntry catalogEntry) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setCatalogEntryId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if ((!currentLine.equals(EMPTY))) catalogEntry.setProductId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setManufacturerId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setSymbolId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setOrderNumber(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setOrderNumber(catalogEntry.getOrderNumber() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) catalogEntry.setEntryName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setEntryName(catalogEntry.getEntryName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) catalogEntry.setEntryColor(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setEntryName(catalogEntry.getEntryColor() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) catalogEntry.setEntryWidthInModules(Double.parseDouble(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setEntryWidthInMillimeters(Double.parseDouble(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setPrice(Double.parseDouble(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setCurrency(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setCurrency(catalogEntry.getCurrency() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) catalogEntry.setQuantityUnit(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setQuantityUnit(catalogEntry.getQuantityUnit() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) catalogEntry.setMaterialPrice(Double.parseDouble(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setMountingTime(Double.parseDouble(currentLine) * 60);
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setMountingTime(catalogEntry.getMountingTime() + Double.parseDouble(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setMountingTime(catalogEntry.getMountingTime() + (Double.parseDouble(currentLine) / 60));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            if (Integer.parseInt(currentLine) == 1) catalogEntry.setDinFlag(true); else catalogEntry.setDinFlag(false);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setSeries(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setSeries(catalogEntry.getSeries() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) catalogEntry.setCatalogName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setCatalogName(catalogEntry.getCatalogName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            StringTokenizer st = new StringTokenizer(currentLine, BACK_SLASH_SLASH);
            for (int i = 0; i < st.countTokens(); i++) st.nextToken();
            catalogEntry.setEntryPicture(st.nextToken());
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setDesignationType(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setDesignationType(catalogEntry.getDesignationType() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) catalogEntry.setDesignationFunction(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setDesignationFunction(catalogEntry.getDesignationFunction() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        currentLine = in.readLine();
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) catalogEntry.setRamSize(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            currentLine = in.readLine();
        }
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) catalogEntry.setEntryStatusCode(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            catalogEntry.setEntryStatusCode(catalogEntry.getEntryStatusCode() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>CommunicationObject</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param comObject the <code>CommunicationObject</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getCommunicationObject(BufferedReader in, String currentLine, CommunicationObject commObject) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setObjectName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            commObject.setObjectName(commObject.getObjectName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) commObject.setObjectFunction(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            commObject.setObjectFunction(commObject.getObjectFunction() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) commObject.setObjectReadenabled(false); else commObject.setObjectReadenabled(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) commObject.setObjectWriteenabled(false); else commObject.setObjectWriteenabled(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) commObject.setObjectCommenabled(false); else commObject.setObjectCommenabled(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) commObject.setObjectTransenabled(false); else commObject.setObjectTransenabled(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setObjectDisplayOrder(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setParentParameterValue(new Long(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setObjectId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setParameterId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setObjectNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setObjectDescription(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            commObject.setObjectDescription(commObject.getObjectDescription() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) commObject.setObjectTypeId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setObjectPriorityId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) commObject.setObjectUpdateenabled(false); else commObject.setObjectUpdateenabled(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) commObject.setObjectUniqueNumber(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>DeviceObject</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param deviceObject the <code>DeviceObject</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getDeviceObject(BufferedReader in, String currentLine, DeviceObject deviceObject) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setVirtualDeviceId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setObjectPriorityId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) deviceObject.setObjectRead(false); else deviceObject.setObjectRead(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) deviceObject.setObjectWrite(false); else deviceObject.setObjectWrite(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) deviceObject.setObjectComm(false); else deviceObject.setObjectComm(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) deviceObject.setObjectTrans(false); else deviceObject.setObjectTrans(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setObjectSubgroupsText(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            deviceObject.setObjectSubgroupsText(deviceObject.getObjectSubgroupsText() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) deviceObject.setDeviceObjectId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setCommunicationObjectId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setDeviceObjectNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setObjectSubgroupsText2(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            deviceObject.setObjectSubgroupsText2(deviceObject.getObjectSubgroupsText2() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) deviceObject.setDeviceObjectVisible(false); else deviceObject.setDeviceObjectVisible(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setDeviceObjectUniqueName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            deviceObject.setDeviceObjectUniqueName(deviceObject.getDeviceObjectUniqueName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) {
            int bool = Integer.parseInt(currentLine);
            if (bool == 0) deviceObject.setObjectUpdate(false); else deviceObject.setObjectUpdate(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setDeviceObjectUniqueNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) deviceObject.setDeviceObjectType(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>DeviceParameter</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param parameter the <code>DeviceParamete</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getDeviceParameter(BufferedReader in, String currentLine, DeviceParameter parameter) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setDeviceId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setDeviceParameterId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setDeviceParameterNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int i;
            i = Integer.parseInt(currentLine);
            if (i == 0) parameter.setDeviceParameterVisible(false); else parameter.setDeviceParameterVisible(true);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterValueLong(new Long(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterValueString(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            parameter.setParameterValueString(parameter.getParameterValueString() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) parameter.setProgramType(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterValueDouble(new Double(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>FunctionalEntity</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param functionalEntity the <code>FunctionalEntity</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getFunctionalEntity(BufferedReader in, String currentLine, FunctionalEntity functionalEntity) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) functionalEntity.setFunctionalEntityId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) functionalEntity.setManufacturerId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) functionalEntity.setFunctionalEntityName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            functionalEntity.setFunctionalEntityName(functionalEntity.getFunctionalEntityName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) functionalEntity.setFunctionalEntityNumb(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            functionalEntity.setFunctionalEntityNumb(functionalEntity.getFunctionalEntityNumb() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) functionalEntity.setFunctionalEntityDescription(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            functionalEntity.setFunctionalEntityDescription(functionalEntity.getFunctionalEntityDescription() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) functionalEntity.setFunFunctionalEntityId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            functionalEntity.setFunctionalEntityDescription(functionalEntity.getFunctionalEntityDescription() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>HwProduct</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param product the <code>HwProduct</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getHwProduct(BufferedReader in, String currentLine, HwProduct product) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setProductId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setManufacturerId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setSymbolId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setProductName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            product.setProductName(product.getProductName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) product.setProductVersionNumber(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            product.setProductVersionNumber(product.getProductVersionNumber() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) product.setComponentType(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            product.setComponentType(product.getComponentType() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) product.setComponentAttributes(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setBusCurrent(Float.parseFloat(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setProductSerialNumber(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            product.setProductSerialNumber(product.getProductSerialNumber() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) product.setComponentTypeNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setProductPicture(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            product.setProductPicture(product.getProductPicture() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) product.setBcuTypeId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setProductHandling(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) product.setProductClass(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            product.setProductClass(product.getProductClass() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) product.setOriginalManufacturerId(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>Manufacturer</code>.
     *
     * @param in The <code>BufferedRead</code> that reads the file.
     * @param currentLine the current line in the file.
     * @param manufacturer the <code>Manufacturer</code>.
     *
     * @return the new current line int he file where the parsing stoped.
     */
    public static String getManufacturer(BufferedReader in, String currentLine, Manufacturer manufacturer) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) manufacturer.setManufacturerId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) manufacturer.setManufacturerName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            manufacturer.setManufacturerName(manufacturer.getManufacturerName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) manufacturer.setAddressId(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>MaskEntry</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param maskEntry The parsed <code>MaskEntry</code>.
     *
     * @return the current line after parsing of a <code>MaskEntry</code>.
     */
    public static String getMaskEntry(BufferedReader in, String currentLine, MaskEntry maskEntry) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) maskEntry.setMaskEntryId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) maskEntry.setMaskId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) maskEntry.setMaskEntryName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            maskEntry.setMaskEntryName(maskEntry.getMaskEntryName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) maskEntry.setMaskEntryAddress(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>Mask</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param mask The parsed <code>Mask</code>.
     *
     * @return the current line after parsing of a <code>Mask</code>.
     */
    public static String getMask(BufferedReader in, String currentLine, Mask mask) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setMaskId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setMaskVersion(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setUserRamStart(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setUserRamEnd(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setUserEepromStart(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setUserEepromEnd(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setRunErrorAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setAddressTabAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setAssocTabPtrAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setCommsTabPtrAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setManufacturerDataAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setManufacturerDataSize(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setManufacturerIdAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setRoutecntAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setManufacturerIdProtected(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setMaskVersionName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            mask.setMaskVersionName(mask.getMaskVersionName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        String hexEepromData = null;
        if (!currentLine.equals(EMPTY)) hexEepromData = currentLine;
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            hexEepromData += currentLine.substring(BACK_SLASH_SLASH.length());
            currentLine = in.readLine();
        }
        if (hexEepromData != null) {
            byte[] eepromData = new byte[hexEepromData.length() / 2];
            int index = 0;
            for (int j = 0; j < hexEepromData.length() / 2; j++) {
                eepromData[j] = (byte) Integer.parseInt(hexEepromData.substring(index, index + 2), 16);
                index += 2;
            }
            mask.setMaskEepromData(eepromData);
        }
        if (!currentLine.equals(EMPTY)) mask.setMaskDataLength(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setManufacturerIdProtected(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setAssocTabLcs(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setApplicationProgramLcs(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setPeiProgramLcs(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setLoadControlAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setRunControlAddress(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setExternalMemoryStart(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setExternalMemoryEnd(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setApplicationProgramRcs(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setPeiProgramRcs(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setPortAddr(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mask.setPortAddressProtected(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>MaskSystemPointer</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param maskSystemPointer The parsed <code>MaskSystemPointer</code>.
     *
     * @return the current line after parsing of a <code>MaskEntry</code>.
     */
    public static String getMaskSystemPointer(BufferedReader in, String currentLine, MaskSystemPointer maskSystemPointer) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) maskSystemPointer.setMaskSystemPointerId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) maskSystemPointer.setMaskId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) maskSystemPointer.setSystemPointerName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            maskSystemPointer.setSystemPointerName(maskSystemPointer.getSystemPointerName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) maskSystemPointer.setSystemPointerAddress(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>MediumType</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param mediumType The parsed <code>MediumType</code>.
     *
     * @return the current line after parsing of a <code>MediumType</code>.
     */
    public static String getMediumType(BufferedReader in, String currentLine, MediumType mediumType) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mediumType.setMediumTypeNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) mediumType.setMediumTypeName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            mediumType.setMediumTypeName(mediumType.getMediumTypeName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) mediumType.setMediumTypeShortName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            mediumType.setMediumTypeShortName(mediumType.getMediumTypeShortName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>ObjectPriority</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param objectPrio The parsed <code>ObjectPriority</code>.
     *
     * @return the current line after parsing of a <code>ObjectPriority</code>.
     */
    public static String getObjectPriority(BufferedReader in, String currentLine, ObjectPriority objectPrio) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) objectPrio.setObjectPriorityCode(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) objectPrio.setObjectPriorityName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            objectPrio.setObjectPriorityName(objectPrio.getObjectPriorityName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>ObjectType</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param objectType The parsed <code>ObjectType</code>.
     *
     * @return the current line after parsing of an <code>ObjectType</code>.
     */
    public static String getObjectType(BufferedReader in, String currentLine, ObjectType objectType) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) objectType.setObjectTypeCode(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) objectType.setObjectTypeName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            objectType.setObjectTypeName(objectType.getObjectTypeName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>ParameterAtomicType</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param atomicType The parsed <code>ParameterAtomicType</code>.
     *
     * @return the current line after parsing of a 
     *         <codeParameterAtomic>Type</code>.
     */
    public static String getParameterAtomicType(BufferedReader in, String currentLine, ParameterAtomicType atomicType) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) atomicType.setAtomicTypeNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) atomicType.setAtomicTypeName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            atomicType.setAtomicTypeName(atomicType.getAtomicTypeName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) atomicType.setDispattr(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            atomicType.setDispattr(atomicType.getDispattr() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>ParameterListOfValues</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param list The parsed <code>ParameterListOfValues</code>.
     *
     * @return the current line after parsing of a 
     *         <codeParameterListOfValues</code>.
     */
    public static String getParameterListOfValues(BufferedReader in, String currentLine, ParameterListOfValues list) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) list.setParameterTypeId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) list.setRealValue(Long.parseLong(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) list.setDisplayValue(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            list.setDisplayValue(list.getDisplayValue() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) list.setDisplayOrder(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) list.setParameterValueId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        int index = 0;
        byte[] bytes = new byte[currentLine.length() / 2];
        if (!currentLine.equals(EMPTY)) {
            for (int i = 0; i < currentLine.length() / 2; i++) {
                bytes[i] = (byte) Integer.parseInt(currentLine.substring(index, index + 2), 16);
                index += 2;
            }
            list.setBinaryValue(bytes);
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) list.setBinaryValueLength(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) list.setDoubleValue(Double.parseDouble(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>Parameter</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param parameter The parsed <code>Parameter</code>.
     *
     * @return the current line after parsing of a <codeParameter</code>.
     */
    public static String getParameter(BufferedReader in, String currentLine, Parameter parameter) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterTypeId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            parameter.setParameterName(parameter.getParameterName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) parameter.setParameterLowAccess(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterHighAccess(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParentParmValue(new Long(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterSize(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterFunction(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            parameter.setParameterFunction(parameter.getParameterFunction() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) parameter.setParameterDisplayOrder(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterAddress(new Integer(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterBitoffset(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterDescription(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            parameter.setParameterDescription(parameter.getParameterDescription() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) parameter.setParameterId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParParameterId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterLabel(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            parameter.setParameterLabel(parameter.getParameterLabel() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) parameter.setParameterDefaultLong(new Long(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterDefaultString(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            parameter.setParameterDefaultString(parameter.getParameterDefaultString() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameter.setParameterDefaultDouble(new Double(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) {
            int i;
            i = Integer.parseInt(currentLine);
            if (i == 0) parameter.setPatchAlways(false); else parameter.setPatchAlways(true);
        }
        return currentLine;
    }

    /**
     * Parse a single <code>ParameterType</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param parameterType The parsed <code>ParameterType</code>.
     *
     * @return the current line after parsing of a <codeParameterType</code>.
     */
    public static String getParameterType(BufferedReader in, String currentLine, ParameterType parameterType) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setParameterTypeId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setAtomicTypeNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setParameterTypeName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            parameterType.setParameterTypeName(parameterType.getParameterTypeName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) parameterType.setParameterMinimumValue(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setParameterMaximumValue(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setParameterTypeDescription(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            parameterType.setParameterTypeDescription(parameterType.getParameterTypeDescription() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) parameterType.setParameterTypeLowAccess(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setParameterTypeHighAccess(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setParameterTypeSize(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setParameterMinimumDoubleValue(Double.parseDouble(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) parameterType.setParameterMaximumDoubleValue(Double.parseDouble(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>ProductToProgram</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param prod2Prog The parsed <code>ProductToProgram</code>.
     *
     * @return the current line after parsing of a 
     *         <codeProductToProgram</code>.
     */
    public static String getProductToProgram(BufferedReader in, String currentLine, ProductToProgram prod2Prog) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) prod2Prog.setProduct2ProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) prod2Prog.setProductId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) prod2Prog.setProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) prod2Prog.setProd2ProgStatus(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            prod2Prog.setProd2ProgStatus(prod2Prog.getProd2ProgStatus() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) prod2Prog.setPeiProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) prod2Prog.setProd2ProgStatusCode(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        currentLine = in.readLine();
        currentLine = in.readLine();
        currentLine = in.readLine();
        currentLine = in.readLine();
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            currentLine = in.readLine();
        }
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>ProductToProgramToMediumType</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param prod2Prog2Mt The parsed 
     *                     <code>ProductToProgramToMediumType</code>.
     *
     * @return the current line after parsing of a 
     *         <code>ProductToProgramToMediumType</code>.
     */
    public static String getProductToProgramToMediumType(BufferedReader in, String currentLine, ProductToProgramToMediumType prod2Prog2Mt) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) prod2Prog2Mt.setProduct2Program2MediumTypeId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) prod2Prog2Mt.setProductToProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) prod2Prog2Mt.setMediumTypeNumber(Integer.parseInt(currentLine));
        return currentLine;
    }

    /**
     * Parse a single <code>Symbol</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param symbol The parsed <code>Symbol</code>.
     *
     * @return the current line after parsing of a <code>Symbol</code>.
     */
    public static String getSymbol(BufferedReader in, String currentLine, Symbol symbol) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) symbol.setSymbolId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) symbol.setSymbolFilename(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            symbol.setSymbolFilename(symbol.getSymbolFilename() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) symbol.setSymbolName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            symbol.setSymbolName(symbol.getSymbolName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        return currentLine;
    }

    /**
     * Parse a single <code>VirtualDevice</code>.
     *
     * @param in the <code>BufferedReader</code>.
     * @param currentLine current line in vd_ file.
     * @param virtualDevice The parsed <code>VirtualDevice</code>.
     *
     * @return the current line after parsing of a <code>VirtualDevice</code>.
     */
    public static String getVirtualDevice(BufferedReader in, String currentLine, VirtualDevice virtualDevice) throws IOException {
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setVirtualDeviceId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setSymbolId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setCatalogEntryId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setVirtualDeviceName(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            virtualDevice.setVirtualDeviceName(virtualDevice.getVirtualDeviceName() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) virtualDevice.setVirtualDeviceDescription(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            virtualDevice.setVirtualDeviceDescription(virtualDevice.getVirtualDeviceDescription() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        if (!currentLine.equals(EMPTY)) virtualDevice.setFunctionalEntityId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setProductTypeId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            currentLine = in.readLine();
        }
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setPeiProgramId(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setVirtualDeviceNumber(Integer.parseInt(currentLine));
        currentLine = in.readLine();
        if (!currentLine.equals(EMPTY)) virtualDevice.setMediumTypes(currentLine);
        currentLine = in.readLine();
        while ((!currentLine.equals(EMPTY)) && (currentLine.startsWith(BACK_SLASH_SLASH))) {
            virtualDevice.setMediumTypes(virtualDevice.getMediumTypes() + currentLine.substring(BACK_SLASH_SLASH.length()));
            currentLine = in.readLine();
        }
        return currentLine;
    }
}

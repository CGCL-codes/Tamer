package cz.fi.muni.xkremser.editor.client.metadata;

import java.util.ArrayList;
import java.util.List;
import cz.fi.muni.xkremser.editor.client.mods.CopyInformationTypeClient;
import cz.fi.muni.xkremser.editor.client.mods.ExtensionTypeClient;
import cz.fi.muni.xkremser.editor.client.mods.HoldingSimpleTypeClient;
import cz.fi.muni.xkremser.editor.client.mods.LocationTypeClient;
import cz.fi.muni.xkremser.editor.client.mods.PhysicalLocationTypeClient;
import cz.fi.muni.xkremser.editor.client.mods.UrlTypeClient;

/**
 * The Class LocationHolder.
 */
public class LocationHolder extends MetadataHolder {

    /** The shelf locators. */
    private final ListOfSimpleValuesHolder shelfLocators;

    /** The holding externals. */
    private final ListOfSimpleValuesHolder holdingExternals;

    /** The physical locations. */
    private final ListOfListOfSimpleValuesHolder physicalLocations;

    /** The urls. */
    private final ListOfListOfSimpleValuesHolder urls;

    /** The holding simples. */
    private final List<CopyInformationHolder> holdingSimples;

    /**
     * Instantiates a new location holder.
     */
    public LocationHolder() {
        this.shelfLocators = new ListOfSimpleValuesHolder();
        this.holdingExternals = new ListOfSimpleValuesHolder();
        this.physicalLocations = new ListOfListOfSimpleValuesHolder(ModsConstants.PHYSICAL_LOCATION, ModsConstants.DISPLAY_LABEL, ModsConstants.TYPE, ModsConstants.AUTHORITY, ModsConstants.XLINK, ModsConstants.LANG, ModsConstants.XML_LANG, ModsConstants.TRANSLITERATION, ModsConstants.SCRIPT);
        this.urls = new ListOfListOfSimpleValuesHolder(ModsConstants.URL, ModsConstants.DISPLAY_LABEL, ModsConstants.DATE_LAST_ACCESSED, ModsConstants.NOTE, ModsConstants.ACCESS, ModsConstants.USAGE);
        this.holdingSimples = new ArrayList<CopyInformationHolder>();
    }

    /**
     * Gets the location.
     * 
     * @return the location
     */
    public LocationTypeClient getLocation() {
        LocationTypeClient locationTypeClient = new LocationTypeClient();
        locationTypeClient.setShelfLocator(shelfLocators.getValues());
        if (holdingExternals.getValues() != null && holdingExternals.getValues().size() > 0) {
            ExtensionTypeClient ex = new ExtensionTypeClient();
            StringBuilder sb = new StringBuilder();
            for (String val : holdingExternals.getValues()) {
                sb.append(val).append('\n');
            }
            if (!"".equals(sb.toString())) {
                ex.setContent(sb.toString());
                locationTypeClient.setHoldingExternal(ex);
            }
        }
        List<PhysicalLocationTypeClient> list = null;
        List<List<String>> listOfValues = physicalLocations.getListOfList();
        boolean isNull = true;
        if (listOfValues != null && listOfValues.size() != 0) {
            list = new ArrayList<PhysicalLocationTypeClient>();
            for (List<String> values : listOfValues) {
                if (values != null) {
                    PhysicalLocationTypeClient val = new PhysicalLocationTypeClient();
                    val.setValue(values.get(0));
                    val.setDisplayLabel(values.get(1));
                    val.setType(values.get(2));
                    val.setAuthority(values.get(3));
                    val.setXlink(values.get(4));
                    val.setLang(values.get(5));
                    val.setXmlLang(values.get(5));
                    val.setTransliteration(values.get(6));
                    val.setScript(values.get(7));
                    list.add(val);
                    isNull = false;
                }
            }
        }
        locationTypeClient.setPhysicalLocation(isNull ? null : list);
        List<UrlTypeClient> list2 = null;
        List<List<String>> listOfValues2 = urls.getListOfList();
        isNull = true;
        if (listOfValues2 != null && listOfValues2.size() != 0) {
            list2 = new ArrayList<UrlTypeClient>();
            for (List<String> values : listOfValues2) {
                if (values != null) {
                    UrlTypeClient val = new UrlTypeClient();
                    val.setValue(values.get(0));
                    val.setDisplayLabel(values.get(1));
                    val.setDateLastAccessed(values.get(2));
                    val.setNote(values.get(3));
                    val.setAccess(values.get(4));
                    val.setUsage(values.get(5));
                    list2.add(val);
                    isNull = false;
                }
            }
        }
        locationTypeClient.setUrl(isNull ? null : list2);
        HoldingSimpleTypeClient holding = null;
        if (holdingSimples.size() > 0) {
            holding = new HoldingSimpleTypeClient();
            List<CopyInformationTypeClient> info = new ArrayList<CopyInformationTypeClient>(holdingSimples.size());
            for (CopyInformationHolder holder : holdingSimples) {
                info.add(holder.getCopyInfo());
            }
            holding.setCopyInformation(info);
        }
        locationTypeClient.setHoldingSimple(holding);
        return locationTypeClient;
    }

    @Override
    public List<MetadataHolder> getSubelements() {
        throw new UnsupportedOperationException("Mods");
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Mods");
    }

    @Override
    public List<String> getValues() {
        throw new UnsupportedOperationException("Mods");
    }

    @Override
    public List<String> getAttributes() {
        throw new UnsupportedOperationException("Mods");
    }

    /**
     * Gets the shelf locators.
     * 
     * @return the shelf locators
     */
    public ListOfSimpleValuesHolder getShelfLocators() {
        return shelfLocators;
    }

    /**
     * Gets the holding externals.
     * 
     * @return the holding externals
     */
    public ListOfSimpleValuesHolder getHoldingExternals() {
        return holdingExternals;
    }

    /**
     * Gets the physical locations.
     * 
     * @return the physical locations
     */
    public ListOfListOfSimpleValuesHolder getPhysicalLocations() {
        return physicalLocations;
    }

    /**
     * Gets the urls.
     * 
     * @return the urls
     */
    public ListOfListOfSimpleValuesHolder getUrls() {
        return urls;
    }

    /**
     * Gets the holding simples.
     * 
     * @return the holding simples
     */
    public List<CopyInformationHolder> getHoldingSimples() {
        return holdingSimples;
    }
}

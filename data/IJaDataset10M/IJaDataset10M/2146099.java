package saadadb.products;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import saadadb.classmapping.MappingSpectrum;
import saadadb.collection.SpectrumSaada;
import saadadb.command.ArgsParser;
import saadadb.database.Database;
import saadadb.exceptions.AbortException;
import saadadb.exceptions.IgnoreException;
import saadadb.exceptions.SaadaException;
import saadadb.meta.AttributeHandler;
import saadadb.prdconfiguration.ConfigurationDefaultHandler;
import saadadb.prdconfiguration.ConfigurationSpectrum;
import saadadb.util.DefineType;
import saadadb.util.Messenger;
import saadadb.util.RegExp;
import saadadb.util.SaadaConstant;

/**
 * This class redefines method specific in spectra during their collection load.
 * 
 * @author Millan Patrick
 */
public class Spectrum extends Product {

    /**
	 *  * @version $Id: Spectrum.java 118 2012-01-06 14:33:51Z laurent.mistahl $

	 */
    private static final long serialVersionUID = 1L;

    protected AttributeHandler sc_attr = null;

    SpectralCoordinate spectralCoordinate = new SpectralCoordinate();

    protected String native_unit;

    /**
	 * @param fileName
	 * @throws AbortException 
	 * @throws SaadaException 
	 */
    public Spectrum(File file, ConfigurationDefaultHandler conf) throws IgnoreException {
        super(file, conf);
        spectralCoordinate = new SpectralCoordinate();
        if (!spectralCoordinate.isConfigurationValid(1, 1, SpectralCoordinate.getDispersionCode(Database.getSpect_type()), Database.getSpect_unit())) {
            IgnoreException.throwNewException(SaadaException.MAPPING_FAILURE, "Spectral Configuration not valid");
        }
    }

    /**
	 * 
	 */
    public void setSpecCoordinateFields() {
        SpectrumSaada obj = (SpectrumSaada) this.saadainstance;
        obj.setX_min_org_csa(spectralCoordinate.getOrgMin());
        obj.setX_max_org_csa(spectralCoordinate.getOrgMax());
        obj.setX_unit_org_csa(spectralCoordinate.getOrgUnit());
        obj.setX_min_csa(spectralCoordinate.getConvertedMin());
        obj.setX_max_csa(spectralCoordinate.getConvertedMax());
        obj.setX_unit_csa(spectralCoordinate.getConvertedUnit());
        obj.setX_type_csa(spectralCoordinate.getConvertedType());
        obj.setX_naxis_csa(spectralCoordinate.getConvertedNaxis());
    }

    /**
	 * @param ds
	 * @return
	 */
    public void getMinMaxValues(double[] ds) {
        if (ds == null || ds.length != 2) {
            this.spectralCoordinate.setOrgMax(SaadaConstant.DOUBLE);
            this.spectralCoordinate.setOrgMin(SaadaConstant.DOUBLE);
        } else {
            this.spectralCoordinate.setOrgMax(ds[1]);
            this.spectralCoordinate.setOrgMin(ds[0]);
        }
    }

    /**
	 * @throws Exception 
	 * 
	 */
    protected void mapCollectionAttributes() throws Exception {
        super.mapCollectionAttributes();
        this.mapCollectionSpectralCoordinate();
    }

    /**
	 * @throws Exception 
	 * 
	 */
    protected void mapCollectionSpectralCoordinate() throws Exception {
        ConfigurationSpectrum cs = (ConfigurationSpectrum) (this.configuration);
        int priority = cs.getSpectralCoordinatePriority();
        boolean mapping_ok = true;
        if (priority == DefineType.ONLY) {
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Spectral Coordinate mapping priority: ONLY: only mapped keyword will be used");
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Look for Spectral Coordinate keywords defined into the mapping");
            mapping_ok = this.mapCollectionSpectralCoordinateFromMapping();
        } else if (priority == DefineType.FIRST) {
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Spectral Coordinate mapping priority: FIRST: Mapped keyword will be searched first and then spectral KWs will be infered");
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Look for Spectral Coordinate keywords defined into the mapping");
            if (!this.mapCollectionSpectralCoordinateFromMapping()) {
                if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Try to find out Spectral Coordinate keywords");
                mapping_ok = this.mapCollectionSpectralCoordinateAuto();
            }
        } else if (priority == DefineType.LAST) {
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Spectral Coordinate mapping priority: LAST: Spectral KWs will be infered and then apped keyword will be searched");
            if (!this.mapCollectionSpectralCoordinateAuto()) {
                if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Look for Spectral Coordinate keywords defined into the mapping");
                mapping_ok = this.mapCollectionSpectralCoordinateFromMapping();
            }
        } else {
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "No Spectral Coordinate priority:  KWs will be infered");
            mapping_ok = this.mapCollectionSpectralCoordinateAuto();
        }
        if (!mapping_ok) {
            Messenger.printMsg(Messenger.TRACE, "No  Spectral Coordinate found: It won't be set for this product");
        } else {
            Messenger.printMsg(Messenger.TRACE, "Native spectral range: " + this.spectralCoordinate.toString());
        }
    }

    /**
	 * @throws Exception 
	 * 
	 */
    private boolean mapCollectionSpectralCoordinateFromMapping() throws Exception {
        ConfigurationSpectrum cs = (ConfigurationSpectrum) (this.configuration);
        String sc_col = cs.getSpectralCordinateColumn();
        String sc_unit = cs.getSpectralCordinateUnit();
        spectralCoordinate.setOrgUnit(SaadaConstant.STRING);
        if (sc_col.startsWith("'")) {
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Spectral range given as numeric values.");
            sc_col = sc_col.replaceAll("'", "");
            Pattern p = Pattern.compile("(" + RegExp.FITS_INT_VAL + "|" + RegExp.FITS_FLOAT_VAL + ")[,:;\\- ](" + RegExp.FITS_INT_VAL + "|" + RegExp.FITS_FLOAT_VAL + ")");
            Matcher m = p.matcher(sc_col);
            if (m.find() && m.groupCount() == 2) {
                this.spectralCoordinate.setOrgMin(Double.parseDouble(m.group(1)));
                this.spectralCoordinate.setOrgMax(Double.parseDouble(m.group(2)));
                if (sc_unit.equals("AutoDetect")) {
                    spectralCoordinate.setOrgUnit("channel");
                } else {
                    spectralCoordinate.setOrgUnit(sc_unit);
                }
                return spectralCoordinate.convert();
            } else {
                Messenger.printMsg(Messenger.WARNING, "spectral coord. <" + sc_col + "> ca not be interptreted");
                return false;
            }
        }
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Checking if column <" + sc_col + "> exists");
        LinkedHashMap<String, AttributeHandler> tah = new LinkedHashMap<String, AttributeHandler>();
        this.productFile.getKWEntry(tah);
        for (AttributeHandler ah : tah.values()) {
            String key = ah.getNameorg();
            if (key.equals(sc_col)) {
                Messenger.printMsg(Messenger.TRACE, "Spectral abscissa column <" + sc_col + "> found");
                this.sc_attr = ah;
                this.getMinMaxValues(this.productFile.getExtrema(key));
                if (sc_unit.equals("AutoDetect")) {
                    if (ah.getUnit() != null && ah.getUnit().length() > 0) {
                        spectralCoordinate.setOrgUnit(ah.getUnit());
                        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "spectral coord. unit <" + sc_unit + "> taken from column  description");
                    } else {
                        Messenger.printMsg(Messenger.WARNING, "spectral coord. unit found neither in column description nor in mapping");
                    }
                } else {
                    spectralCoordinate.setOrgUnit(sc_unit);
                    if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "spectral coord. unit <" + sc_unit + "> taken from mapping");
                }
                return spectralCoordinate.convert();
            }
        }
        return false;
    }

    /**
	 * @return
	 * @throws Exception 
	 */
    public boolean mapCollectionSpectralCoordinateAuto() throws Exception {
        if (this.findSpectralCoordinateByUCD()) {
            return spectralCoordinate.convert();
        } else if (this.findSpectralCoordinateByWCS()) {
            return true;
        } else if (this.findSpectralCoordinateByKW()) {
            return spectralCoordinate.convert();
        } else if (this.findSpectralCoordinateInPixels()) {
            return spectralCoordinate.convert();
        } else {
            return false;
        }
    }

    /**
	 * @return
	 * @throws Exception 
	 */
    public boolean findSpectralCoordinateInPixels() throws Exception {
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Searching spectral coordinate in pixel <" + this.file.getName() + ">");
        double[] ext = this.productFile.getExtrema(null);
        if (ext != null) {
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Spectral coordinates found in FITS image pixels");
            this.getMinMaxValues(ext);
            String unit = ((ConfigurationSpectrum) (this.configuration)).getSpectralCordinateUnit();
            if (!"AutoDetect".equals(unit)) {
                spectralCoordinate.setOrgUnit(((ConfigurationSpectrum) (this.configuration)).getSpectralCordinateUnit());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
	 * @return
	 * @throws Exception 
	 */
    public boolean findSpectralCoordinateByWCS() throws Exception {
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Searching spectral coordinate in WCS keywords <" + this.file.getName() + ">");
        return spectralCoordinate.convertWCS(this.tableAttributeHandler, ((MappingSpectrum) (this.configuration.getMapping())).getAbcisseUnit());
    }

    /**
	 * @return
	 * @throws IgnoreException 
	 */
    public boolean findSpectralCoordinateByKW() throws Exception {
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Searching spectral coordinate in KWs <" + this.file.getName() + ">");
        spectralCoordinate.setOrgUnit(SaadaConstant.STRING);
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "No Spectral coordinate found in header: explore columns names");
        LinkedHashMap<String, AttributeHandler> tah = new LinkedHashMap<String, AttributeHandler>();
        this.productFile.getKWEntry(tah);
        for (AttributeHandler ah : tah.values()) {
            if (ah.getNameorg().matches(RegExp.SPEC_AXIS)) {
                Messenger.printMsg(Messenger.TRACE, "Column <" + ah.getNameorg() + "> taken as dispersion value");
                this.sc_attr = ah;
                this.getMinMaxValues(this.productFile.getExtrema(ah.getNameorg()));
                if (this.sc_attr.getUnit() != null) {
                    spectralCoordinate.setOrgUnit(this.sc_attr.getUnit());
                    if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, spectralCoordinate.getOrgMin() + " " + spectralCoordinate.getOrgMax() + " " + spectralCoordinate.getOrgUnit());
                }
                return true;
            }
        }
        return false;
    }

    /**
	 * @return
	 * @throws Exception 
	 */
    public boolean findSpectralCoordinateByUCD() throws Exception {
        boolean findMin = false;
        boolean findMax = false;
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Searching spectral coordinate by UCDs or UTypes <" + this.file.getName() + ">");
        Iterator<String> it = this.tableAttributeHandler.keySet().iterator();
        String unit = "";
        while (it.hasNext()) {
            AttributeHandler ah = this.tableAttributeHandler.get(it.next());
            if (!findMin && (ah.getUcd().equals("em.wl;stat.min") || ah.getUtype().equals("sed:SpectralMinimumWavelength"))) {
                String min = ah.getValue();
                spectralCoordinate.setOrgMin(Double.parseDouble(min));
                findMin = true;
                unit = ah.getUnit();
            } else if (!findMax && (ah.getUcd().equals("em.wl;stat.max") || ah.getUtype().equals("sed:SpectralMaximumWavelength"))) {
                String max = ah.getValue();
                spectralCoordinate.setOrgMax(Double.parseDouble(max));
                findMax = true;
                if (unit.length() == 0) {
                    unit = ah.getUnit();
                }
            }
        }
        if (unit == null || unit.length() == 0) {
            spectralCoordinate.setOrgUnit(SaadaConstant.STRING);
        }
        if (findMax && findMin) {
            spectralCoordinate.setOrgUnit(unit);
            Messenger.printMsg(Messenger.TRACE, "Spectral coordinate found:" + spectralCoordinate.getOrgMin() + " to " + spectralCoordinate.getOrgMax() + " " + spectralCoordinate.getOrgUnit());
            return true;
        } else {
            if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "No Spectral coordinate found in header UCDs or UTypes: explore column definitions");
            findMin = false;
            LinkedHashMap<String, AttributeHandler> tah = new LinkedHashMap<String, AttributeHandler>();
            this.productFile.getKWEntry(tah);
            for (AttributeHandler ah : tah.values()) {
                if (ah.getUcd().equals("em.wl")) {
                    this.sc_attr = ah;
                    this.getMinMaxValues(this.productFile.getExtrema(ah.getNameorg()));
                    spectralCoordinate.setOrgUnit(ah.getUnit());
                    findMin = true;
                    break;
                }
                if (unit == null || unit.length() == 0) {
                    spectralCoordinate.setOrgUnit(SaadaConstant.STRING);
                }
                if (findMin) {
                    Messenger.printMsg(Messenger.TRACE, "Spectral coordinate found:" + spectralCoordinate.getOrgMin() + " to " + spectralCoordinate.getOrgMax() + " " + spectralCoordinate.getOrgUnit());
                    return true;
                } else {
                    if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Spectral coordinate not found by using UCDs and UTYPES");
                    return false;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            ArgsParser ap = new ArgsParser(args);
            Database.init("BENCH1_5_1_MSQL");
            Spectrum sp;
            sp = new Spectrum(new File("/home/michel/Desktop/MGC29864B.fit"), new ConfigurationSpectrum("", new ArgsParser(new String[] { "-collection=Collection0", "-spcmapping=first", "-spcunit=nm" })));
            sp.loadProductFile(new ConfigurationSpectrum("", new ArgsParser(new String[] { "-collection=Collection0", "-spcmapping=first", "-spcunit=Angstrom" })));
            Messenger.debug_mode = true;
            sp.findSpectralCoordinateByWCS();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

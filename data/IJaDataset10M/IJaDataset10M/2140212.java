package edu.ucdavis.genomics.metabolomics.binbase.algorythm.matching;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdom.Element;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.validate.ValidateSpectra;
import edu.ucdavis.genomics.metabolomics.util.config.Configable;

/**
 * @deprecated @see {@link StandardAlgorithmHandler} use this one instead and add your own filters!
 *
 * a more complex filter with hirachial analysis
 * 
 * @author wohlgemuth
 * @version Apr 19, 2006
 * 
 */
public class SifterAlgorythmHandler extends BasicAlgorythmHandler {

    private Logger logger = Logger.getLogger(SifterAlgorythmHandler.class);

    /**
	 * DOCUMENT ME!
	 */
    private boolean override = false;

    /**
	 * the minimal value of the current task
	 */
    private double maximal = 0;

    /**
	 * the maximal value of the current task
	 */
    private double minimal = 0;

    /**
	 * DOCUMENT ME!
	 */
    private double similarity = 100;

    /**
	 * DOCUMENT ME!
	 */
    private int distance = 0;

    private double similarityOffset;

    private double minRatio;

    private double cleanning;

    /**
	 * 
	 */
    public SifterAlgorythmHandler() {
        super();
        try {
            cleanning = Configable.CONFIG.getElement("values.filter.clean").getAttribute("cut").getDoubleValue();
            logger.debug("using cleaning offset to: " + cleanning);
        } catch (Exception e) {
            logger.error("error at getting value, using default value. Exception was: " + e.getMessage(), e);
            cleanning = 3;
        }
        try {
            similarityOffset = Configable.CONFIG.getElement("values.filter.similarity").getAttribute("offset").getDoubleValue();
            logger.debug("using similariy offset of: " + similarityOffset);
        } catch (Exception e) {
            logger.error("error at getting value, using default value. Exception was: " + e.getMessage(), e);
            similarityOffset = 100;
        }
        try {
            minRatio = Configable.CONFIG.getElement("values.filter.unique").getAttribute("ratio").getDoubleValue();
            logger.debug("set min ratio to: " + minRatio);
        } catch (Exception e) {
            logger.error("error at getting value, using default value. Exception was: " + e.getMessage(), e);
            minRatio = 0.05;
        }
        try {
            Element ex = Configable.CONFIG.getElement("values.matching");
            override = new Boolean(ex.getAttribute("override").getValue()).booleanValue();
            distance = Integer.parseInt(ex.getAttribute("maxRiDistance").getValue());
            similarity = Double.parseDouble(ex.getAttribute("similarity").getValue());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            override = false;
            distance = 0;
        }
    }

    /**
	 * @see edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.util.transform.abstracthandler.algorythm.binlib.algorythm.AlgorythmHandler#handleValues(double,
	 *      double, double, org.jdom.Element)
	 */
    public boolean compare(Map lib, Map unk, Element configuration) {
        if (lib == null | unk == null) {
            logger.fatal("parameter can't be null!");
            return false;
        }
        double sn = this.getSingnalNoise(unk);
        double ri = getRi(unk);
        double binRi = getRi(lib);
        double min = getBinMin(lib);
        double max = getBinMax(lib);
        double[][] unkSpectra = calculateMassSpec(unk);
        int basePeak = calculateBasePeak(unkSpectra);
        double baseIntensity = unkSpectra[basePeak - 1][ValidateSpectra.FRAGMENT_ABS_POSITION];
        int unkUnique = getUniqueMass(unk);
        int unique = getUniqueMass(lib);
        double uniqueIntensity = unkSpectra[unique - 1][ValidateSpectra.FRAGMENT_ABS_POSITION];
        int binId = Integer.parseInt(lib.get("bin_id").toString());
        int spectraId = Integer.parseInt(unk.get("spectra_id").toString());
        min = binRi - min;
        max = binRi + max;
        logger.debug("check for ri of " + lib.get("name"));
        logger.debug("unknown id " + spectraId);
        logger.debug("bin id " + binId);
        if (filterByRange(ri, min, max) == false) {
            logger.debug("massspec was not in ri range");
            logger.debug("check for ri of " + lib.get("name"));
            logger.debug("unknown id " + spectraId);
            logger.debug("bin id " + binId);
            logger.debug("min ri: " + min);
            logger.debug("max ri: " + max);
            logger.debug("unknown ri " + ri);
            logger.debug("bin ri " + binRi);
            return false;
        }
        try {
            if (this.isLargePeak(sn) == true) {
                logger.debug("found a large peak --> ignore unique ions validation");
            } else {
                if (validateUniqueRatio(uniqueIntensity, baseIntensity, minRatio) == false) {
                    logger.debug("unique ion is not big enough");
                    logger.debug("check for ri of " + lib.get("name"));
                    logger.debug("unknown id " + spectraId);
                    logger.debug("bin id " + binId);
                    logger.debug("bin unique mass " + unique);
                    logger.debug("unique sn " + sn);
                    logger.debug("unique mass " + unkUnique);
                    logger.debug("unique intensity " + uniqueIntensity);
                    logger.debug("base peak " + basePeak);
                    logger.debug("base intensity " + baseIntensity);
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("no annotation possible, " + e.getMessage(), e);
            return false;
        }
        try {
            if (this.isLargePeak(sn) == true) {
                logger.debug("found a large peak --> ignore unique ions");
            } else {
                logger.debug("found a small peak --> validate over apex masses");
                if (filerByUnique(unkUnique, unique, (String) lib.get("apex")) == false) {
                    logger.debug("unknown unique mass is not in the apex masses of the bin");
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("no annotation possible, " + e.getMessage(), e);
            return false;
        }
        double[][] libSpectra = calculateMassSpec(lib);
        double[][] cleanSpectra = calculateCleanMassSpec(unk, cleanning);
        double similarity = ValidateSpectra.similarity(libSpectra, cleanSpectra);
        unk.put("similarity", String.valueOf(similarity));
        if (complexFilter(lib, unk, configuration)) {
            return true;
        }
        return false;
    }

    /**
	 * @author wohlgemuth
	 * @version Apr 19, 2006
	 * @param bin
	 * @param spectra
	 * @return
	 */
    private boolean complexFilter(Map bin, Map spectra, Element configuration) {
        Element settingPurity = null;
        Element settingSignalNoise = null;
        Element settingSimilarity = null;
        Iterator purityIterator;
        Iterator similarityIterator;
        Iterator snIterator;
        List purityList;
        List similarityList;
        List snList;
        double signalNoise = Double.parseDouble((String) spectra.get("apex_sn"));
        double purity = Double.parseDouble((String) spectra.get("purity"));
        double similarity = Double.parseDouble((String) spectra.get("similarity"));
        int spectraUnique = Integer.parseInt(spectra.get("uniquemass").toString());
        int binUnique = Integer.parseInt(bin.get("uniquemass").toString());
        int spectraRi = Integer.parseInt(spectra.get("retention_index").toString());
        int binRi = Integer.parseInt(bin.get("retention_index").toString());
        Element element = configuration;
        if (configuration == null) {
            logger.warn("no configuration provided!");
            return false;
        }
        try {
            String enable = element.getAttributeValue("enable");
            if (enable == null) {
            } else {
                if (new Boolean(enable).booleanValue() == true) {
                } else {
                    return false;
                }
            }
            purityList = element.getChildren("purity");
            purityIterator = purityList.iterator();
            try {
                while (purityIterator.hasNext()) {
                    try {
                        settingPurity = (Element) purityIterator.next();
                        if (checkElement(settingPurity, purity) == true) {
                            snList = settingPurity.getChildren("signalnoise");
                            if (snList == null) {
                            } else {
                                snIterator = snList.iterator();
                                while (snIterator.hasNext()) {
                                    try {
                                        settingSignalNoise = (Element) snIterator.next();
                                        logger.debug(" check signalnoise");
                                        if (checkElement(settingSignalNoise, signalNoise) == true) {
                                            logger.debug(" succes");
                                            similarityList = settingSignalNoise.getChildren("similarity");
                                            if (similarityList == null) {
                                            } else {
                                                similarityIterator = similarityList.iterator();
                                                while (similarityIterator.hasNext()) {
                                                    try {
                                                        settingSimilarity = (Element) similarityIterator.next();
                                                        logger.debug(" check similarity");
                                                        if (checkElement(settingSimilarity, similarity) == true) {
                                                            return true;
                                                        } else {
                                                            if (override) {
                                                                if (spectraUnique == binUnique) {
                                                                    if (Math.abs(spectraRi - binRi) <= distance) {
                                                                        if (similarity > this.similarity) {
                                                                            logger.debug("accepted");
                                                                            return true;
                                                                        }
                                                                    }
                                                                }
                                                                return false;
                                                            } else {
                                                                logger.debug("override not enabled");
                                                                return false;
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        logger.error(e.getMessage(), e);
                                                        return false;
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        logger.error(e.getMessage(), e);
                                        return false;
                                    }
                                }
                            }
                            return false;
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        return false;
                    }
                }
            } finally {
                logger.debug("");
                logger.debug("leave method");
                logger.debug("");
            }
            return false;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
	 * 
	 * @param element
	 *            Element with the parameters minimal and maximal as doubles
	 * @param value
	 *            value to check
	 * @return true if value beetween minimal and maximal, false if not
	 */
    private boolean checkElement(Element element, double value) throws Exception {
        logger.debug("checking: " + element.getName());
        String minValue = element.getAttribute("minimal").getValue();
        String maxValue = element.getAttribute("maximal").getValue();
        boolean minND = false;
        boolean maxND = false;
        if (minValue == null) {
            throw new Exception("needed attribut minimal!");
        }
        if (maxValue == null) {
            throw new Exception("needed attribut maximal!");
        }
        if ("nd".equals(minValue)) {
            minND = true;
        } else {
            minimal = new Double(minValue).doubleValue();
        }
        if ("nd".equals(maxValue)) {
            maxND = true;
        } else {
            maximal = new Double(maxValue).doubleValue();
        }
        if ((minND == true) && (maxND == true)) {
            throw new Exception("sorry on parameter must unequal \"nd\"");
        } else {
            if (minND == true) {
                logger.debug(" checkElement: " + element.getName() + "  maximal = " + maximal + " have = " + value);
                if (value <= maximal) {
                    logger.debug("accept");
                    return true;
                } else {
                    logger.debug("failed");
                    return false;
                }
            }
            if (maxND == true) {
                logger.debug(" checkElement: " + element.getName() + " minimal = " + minimal + " have = " + value);
                if (value >= minimal) {
                    logger.debug("accept");
                    return true;
                } else {
                    logger.debug("failed");
                    return false;
                }
            }
        }
        logger.debug(" checkElement: " + element.getName() + " minimal = " + minimal + " maximal = " + maximal + " have = " + value);
        if ((value >= minimal) && (value <= maximal)) {
            logger.debug("accept");
            return true;
        } else {
            logger.debug("failed");
            return false;
        }
    }
}

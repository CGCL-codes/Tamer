package joelib.desc.types;

import wsi.ra.tool.PropertyHolder;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Category;
import joelib.desc.DescDescription;
import joelib.desc.DescResult;
import joelib.desc.Descriptor;
import joelib.desc.DescriptorException;
import joelib.desc.DescriptorHelper;
import joelib.desc.DescriptorInfo;
import joelib.desc.ResultFactory;
import joelib.desc.result.APropDoubleArrResult;
import joelib.desc.result.DoubleMatrixResult;
import joelib.molecule.JOEMol;
import joelib.molecule.types.AtomProperties;
import joelib.util.JOEHelper;
import joelib.util.JOEProperty;
import joelib.util.JOEPropertyHelper;

/**
 * Radial Basis Function (RDF).
 *
 * @author     wegnerj
 */
public class RadialDistributionFunction implements Descriptor {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.RadialDistributionFunction");

    public static final String ATOM_PROPERTY = "ATOM_PROPERTY";

    public static final String MIN_SPHERICAL_VOLUME = "MIN_SPHERICAL_VOLUME";

    public static final String MAX_SPHERICAL_VOLUME = "MAX_SPHERICAL_VOLUME";

    public static final String SPHERICAL_VOLUME_RESOLUTION = "SPHERICAL_VOLUME_RESOLUTION";

    public static final String SMOOTHING_FACTOR = "SMOOTHING_FACTOR";

    public static final String REMOVE_HYDROGENS = "REMOVE_HYDROGENS";

    public static final JOEProperty ATOM_PROPERTY_PROPERTY = new JOEProperty(ATOM_PROPERTY, "java.lang.String", "Atom property to use.", true, "Gasteiger_Marsili");

    public static final JOEProperty MIN_SPHERICAL_VOLUME_PROPERTY = new JOEProperty(MIN_SPHERICAL_VOLUME, "java.lang.Double", "Minimum spherical volume radius.", true, new Double(0.2));

    public static final JOEProperty MAX_SPHERICAL_VOLUME_PROPERTY = new JOEProperty(MAX_SPHERICAL_VOLUME, "java.lang.Double", "Maximum spherical volume radius.", true, new Double(10.0));

    public static final JOEProperty SPHERICAL_VOLUME_RESOLUTION_PROPERTY = new JOEProperty(SPHERICAL_VOLUME_RESOLUTION, "java.lang.Double", "Resolution to use for the spherical volume radius.", true, new Double(0.2));

    public static final JOEProperty SMOOTHING_FACTOR_PROPERTY = new JOEProperty(SMOOTHING_FACTOR, "java.lang.Double", "Smoothing parameter for the interatomic distances.", true, new Double(0.2));

    public static final JOEProperty REMOVE_HYDROGENS_PROPERTY = new JOEProperty(REMOVE_HYDROGENS, "java.lang.Boolean", "Remove hydrogens before calculating radial basis methodName.", true, Boolean.TRUE);

    private static final JOEProperty[] ACCEPTED_PROPERTIES = new JOEProperty[] { ATOM_PROPERTY_PROPERTY, MIN_SPHERICAL_VOLUME_PROPERTY, MAX_SPHERICAL_VOLUME_PROPERTY, SPHERICAL_VOLUME_RESOLUTION_PROPERTY, SMOOTHING_FACTOR_PROPERTY, REMOVE_HYDROGENS_PROPERTY };

    public static final String DESC_KEY = "RDF";

    private DescriptorInfo descInfo;

    private String propertyName;

    private boolean removeHydrogens = true;

    private double maxSphericalVolume = 10.0;

    private double minSphericalVolume = 0.2;

    private double smoothingFactorB = 25;

    private double sphericalVolumeResolution = 0.2;

    public RadialDistributionFunction() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_NO_COORDINATES, null, "joelib.desc.result.APropDoubleArrResult");
        PropertyHolder pHolder = PropertyHolder.instance();
        Properties prop = pHolder.getProperties();
        String className = this.getClass().getName();
        double valueD;
        valueD = PropertyHolder.getDouble(prop, className + ".minSphericalVolume", 0.1, 50, 0.2);
        if (!Double.isNaN(valueD)) {
            minSphericalVolume = valueD;
            MIN_SPHERICAL_VOLUME_PROPERTY.setDefaultProperty(new Double(minSphericalVolume));
            if (logger.isDebugEnabled()) {
                logger.debug("Set minSphericalVolume=" + minSphericalVolume);
            }
        }
        valueD = PropertyHolder.getDouble(prop, className + ".maxSphericalVolume", minSphericalVolume, 100, 10);
        if (!Double.isNaN(valueD)) {
            maxSphericalVolume = valueD;
            MAX_SPHERICAL_VOLUME_PROPERTY.setDefaultProperty(new Double(maxSphericalVolume));
            if (logger.isDebugEnabled()) {
                logger.debug("Set maxSphericalVolume=" + maxSphericalVolume);
            }
        }
        valueD = PropertyHolder.getDouble(prop, className + ".sphericalVolumeResolution", 0.01, 0.5, 0.2);
        if (!Double.isNaN(valueD)) {
            sphericalVolumeResolution = valueD;
            SPHERICAL_VOLUME_RESOLUTION_PROPERTY.setDefaultProperty(new Double(sphericalVolumeResolution));
            if (logger.isDebugEnabled()) {
                logger.debug("Set sphericalVolumeResolution=" + sphericalVolumeResolution);
            }
        }
        valueD = PropertyHolder.getDouble(prop, className + ".smoothingFactor", 1.0, 100000.0, 25.0);
        if (!Double.isNaN(valueD)) {
            smoothingFactorB = valueD;
            SMOOTHING_FACTOR_PROPERTY.setDefaultProperty(new Double(smoothingFactorB));
            if (logger.isDebugEnabled()) {
                logger.debug("Set smoothingFactor=" + smoothingFactorB);
            }
        }
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public DescriptorInfo getDescInfo() {
        return descInfo;
    }

    /**
     *  Gets the description attribute of the Descriptor object
     *
     * @return    The description value
     */
    public DescDescription getDescription() {
        return new DescDescription(descInfo.getDescriptionFile());
    }

    public JOEProperty[] acceptedProperties() {
        return ACCEPTED_PROPERTIES;
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  DescriptorException  Description of the Exception
     */
    public DescResult calculate(JOEMol mol) throws DescriptorException {
        DescResult result = ResultFactory.instance().getDescResult(descInfo.getName());
        return calculate(mol, result, null);
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @param  initData                 Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  DescriptorException  Description of the Exception
     */
    public DescResult calculate(JOEMol mol, Map properties) throws DescriptorException {
        DescResult result = ResultFactory.instance().getDescResult(descInfo.getName());
        return calculate(mol, result, properties);
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @param  descResult               Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  DescriptorException  Description of the Exception
     */
    public DescResult calculate(JOEMol mol, DescResult descResult) throws DescriptorException {
        return calculate(mol, descResult, null);
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @param  initData                 Description of the Parameter
     * @param  descResult               Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  DescriptorException  Description of the Exception
     */
    public DescResult calculate(JOEMol molOriginal, DescResult descResult, Map properties) throws DescriptorException {
        APropDoubleArrResult result = null;
        if (!(descResult instanceof APropDoubleArrResult)) {
            logger.error(descInfo.getName() + " result should be of type " + APropDoubleArrResult.class.getName() + " but it's of type " + descResult.getClass().toString());
            return null;
        } else {
            result = (APropDoubleArrResult) descResult;
        }
        if (!initialize(properties)) {
            return null;
        }
        if (molOriginal.empty()) {
            result.value = new double[1];
            result.atomProperty = propertyName;
            logger.warn("Empty molecule '" + molOriginal.getTitle() + "'. " + DESC_KEY + " was set to ac[0]=0.");
            return result;
        }
        JOEMol mol = null;
        if (removeHydrogens) {
            mol = (JOEMol) molOriginal.clone(true, new String[] { propertyName });
            mol.deleteHydrogens();
        } else {
            mol = molOriginal;
        }
        DescResult tmpResult;
        String distanceMatrixKey = "Geometrical_distance_matrix";
        tmpResult = DescriptorHelper.instance().descFromMol(mol, distanceMatrixKey);
        if (!(tmpResult instanceof DoubleMatrixResult)) {
            logger.error("Needed descriptor '" + distanceMatrixKey + "' should be of type " + DoubleMatrixResult.class.getName() + ". " + DESC_KEY + " can not be calculated.");
            return null;
        }
        DoubleMatrixResult distResult = (DoubleMatrixResult) tmpResult;
        double[][] distances = distResult.value;
        int intervals = (int) ((maxSphericalVolume - minSphericalVolume) / sphericalVolumeResolution);
        DescResult tmpPropResult;
        tmpPropResult = DescriptorHelper.instance().descFromMol(mol, propertyName);
        AtomProperties atomProperties;
        if (JOEHelper.hasInterface(tmpPropResult, "AtomProperties")) {
            atomProperties = (AtomProperties) tmpPropResult;
        } else {
            logger.error("Property '" + propertyName + "' must be an atom type to calculate the " + DESC_KEY + ".");
            return null;
        }
        int atoms = mol.numAtoms();
        int atoms_1 = mol.numAtoms() - 1;
        double[] rdfValues = new double[intervals + 1];
        double aPropWeights;
        double eTerm;
        int index = 0;
        double tmpRDF = 0.0;
        for (double r = minSphericalVolume; r <= maxSphericalVolume; r += sphericalVolumeResolution) {
            tmpRDF = 0.0;
            for (int i = 0; i < atoms_1; i++) {
                for (int j = i + 1; j < atoms; j++) {
                    aPropWeights = atomProperties.getDoubleValue(i + 1) * atomProperties.getDoubleValue(j + 1);
                    eTerm = (r - distances[i][j]);
                    eTerm *= eTerm;
                    tmpRDF += (aPropWeights * Math.exp(-smoothingFactorB * eTerm));
                }
            }
            rdfValues[index] = tmpRDF;
            index++;
        }
        result.value = rdfValues;
        result.atomProperty = propertyName;
        return result;
    }

    /**
     *  Description of the Method
     */
    public void clear() {
    }

    /**
     *  Description of the Method
     *
     * @param  initData  Description of the Parameter
     * @return           Description of the Return Value
     */
    public boolean initialize(Map properties) {
        if (!JOEPropertyHelper.checkProperties(this, properties)) {
            logger.error("Empty property definition or missing property entry.");
            return false;
        }
        String propertyS = (String) JOEPropertyHelper.getProperty(this, ATOM_PROPERTY, properties);
        if (propertyS == null) {
            propertyName = "Gasteiger_Marsili";
        } else {
            propertyName = propertyS;
        }
        Double propertyD = (Double) JOEPropertyHelper.getProperty(this, MIN_SPHERICAL_VOLUME, properties);
        if (propertyD != null) {
            minSphericalVolume = propertyD.doubleValue();
        }
        propertyD = (Double) JOEPropertyHelper.getProperty(this, MAX_SPHERICAL_VOLUME, properties);
        if (propertyD != null) {
            maxSphericalVolume = propertyD.doubleValue();
        }
        propertyD = (Double) JOEPropertyHelper.getProperty(this, SPHERICAL_VOLUME_RESOLUTION, properties);
        if (propertyD != null) {
            sphericalVolumeResolution = propertyD.doubleValue();
        }
        propertyD = (Double) JOEPropertyHelper.getProperty(this, SMOOTHING_FACTOR, properties);
        if (propertyD != null) {
            smoothingFactorB = propertyD.doubleValue();
        }
        Boolean propertyB = (Boolean) JOEPropertyHelper.getProperty(this, REMOVE_HYDROGENS, properties);
        if (propertyD != null) {
            removeHydrogens = propertyB.booleanValue();
        }
        return true;
    }

    /**
     * Test the implementation of this descriptor.
     *
     * @return <tt>true</tt> if the implementation is correct
     */
    public boolean testDescriptor() {
        return true;
    }
}

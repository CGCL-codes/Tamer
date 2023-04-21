package joelib2.data;

import joelib2.molecule.Molecule;
import joelib2.smarts.BasicSMARTSPatternMatcher;
import joelib2.smarts.SMARTSPatternMatcher;
import joelib2.smarts.types.BasicSMARTSPatternDoubles;
import joelib2.util.HelperMethods;
import wsi.ra.tool.BasicPropertyHolder;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.apache.log4j.Category;

/**
 * Model for the protonation/deprotonation of molecules.
 * The definition file can be defined in the
 * <tt>joelib2.data.JOEPhModel.resourceFile</tt> property in the {@link wsi.ra.tool.BasicPropertyHolder}.
 * The {@link wsi.ra.tool.BasicResourceLoader} loads the <tt>joelib2.properties</tt> file for default.
 *
 * <p>
 * Default:<br>
 * joelib2.data.JOEPhModel.resourceFile=<a href="http://cvs.sourceforge.net/cgi-bin/viewcvs.cgi/joelib/joelib/src/joelib2/data/plain/phmodel.txt?rev=HEAD&content-type=text/vnd.viewcvs-markup">joelib2/data/plain/phmodel.txt</a>
 *
 * @.author     wegnerj
 * @.wikipedia  Protonation
 * @.wikipedia  Deprotonation
 * @.wikipedia  PH
 * @.wikipedia Molecule
 * @.license GPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/02/17 16:48:29 $
 * @see wsi.ra.tool.BasicPropertyHolder
 * @see wsi.ra.tool.BasicResourceLoader
 * @see joelib2.data.BasicTransformationRulesHolder
 */
public class BasicProtonationModel extends AbstractDataHolder implements IdentifierHardDependencies, ProtonationModel {

    /**
     * Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance(BasicProtonationModel.class.getName());

    private static BasicProtonationModel phmodel;

    private static final String DEFAULT_RESOURCE = "joelib2/data/plain/phmodel.txt";

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.7 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:29 $";

    private static final Class[] DEPENDENCIES = new Class[] { BasicImplicitValenceTyper.class, BasicSMARTSPatternMatcher.class };

    /**
     * Seed charges for the Gasteiger-Marsili partial charges.
     */
    private List<BasicSMARTSPatternDoubles> seedChargeGM;

    /**
     * SMARTS based transformation patterns.
     */
    private List<BasicTransformationRulesHolder> transformation;

    /**
     *  Constructor for the JOEPhModel object
     */
    private BasicProtonationModel() {
        initialized = false;
        Properties prop = BasicPropertyHolder.instance().getProperties();
        resourceFile = prop.getProperty(this.getClass().getName() + ".resourceFile", DEFAULT_RESOURCE);
        transformation = new Vector<BasicTransformationRulesHolder>();
        seedChargeGM = new Vector<BasicSMARTSPatternDoubles>();
        IdentifierExpertSystem.instance().addHardCodedKernel(this);
        init();
        IdentifierExpertSystem.instance().addSoftCodedKernel(this);
        logger.info("Using pH value correction model: " + resourceFile);
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getReleaseDate() {
        return VENDOR;
    }

    public static String getReleaseVersion() {
        return IdentifierExpertSystem.transformCVStag(RELEASE_VERSION);
    }

    public static String getVendor() {
        return IdentifierExpertSystem.transformCVStag(RELEASE_DATE);
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public static synchronized BasicProtonationModel instance() {
        if (phmodel == null) {
            phmodel = new BasicProtonationModel();
        }
        return phmodel;
    }

    /**
     *  Description of the Method
     *
     * @param  mol  Description of the Parameter
     */
    public void assignSeedPartialCharge(Molecule mol, double[] pCharges) {
        if (!initialized) {
            init();
        }
        if (!mol.isAssignPartialCharge()) {
            return;
        }
        BasicSMARTSPatternDoubles sfvec;
        List<int[]> matchList;
        for (int i = 0; i < seedChargeGM.size(); i++) {
            sfvec = seedChargeGM.get(i);
            if (sfvec.smartsValue.match(mol)) {
                matchList = sfvec.smartsValue.getMatchesUnique();
                int k;
                int[] iarr;
                for (int j = 0; j < matchList.size(); j++) {
                    iarr = matchList.get(j);
                    for (k = 0; k < iarr.length; k++) {
                        pCharges[mol.getAtom(iarr[k]).getIndex() - 1] = sfvec.doubles[k];
                    }
                }
            }
        }
    }

    /**
     * Corrects the molecule for PH.
     * Changes the state of oxygen and nitrogen atoms, if it
     * is allowed to change the formal charges of the atoms, that means
     * if <tt>Molecule.automaticFormalCharge()</tt> returns <tt>true</tt>
     *
     * @param  mol  Description of the Parameter
     */
    public void correctForPH(Molecule mol) {
        if (!initialized) {
            init();
        }
        if (mol.isCorrectedForPH()) {
            return;
        }
        if (!mol.isAssignFormalCharge()) {
            return;
        }
        mol.setCorrectedForPH();
        BasicTransformationRulesHolder ctsfm;
        for (int i = 0; i < transformation.size(); i++) {
            ctsfm = transformation.get(i);
            ctsfm.apply(mol);
        }
        BasicImplicitValenceTyper.instance().correctAromaticNitrogens(mol);
    }

    /**
     * Release date for this expert system (hard coded).
     *
     * @return Release date for this expert system (hard coded).
     */
    public String getReleaseDateInternal() {
        return BasicProtonationModel.getReleaseDate();
    }

    /**
     * Release version for this expert system (hard coded).
     *
     * @return Release version for this expert system (hard coded).
     */
    public String getReleaseVersionInternal() {
        return BasicProtonationModel.getReleaseVersion();
    }

    /**
     * Vendor for this expert system (hard coded).
     *
     * @return Vendor for this expert system (hard coded).
     */
    public String getVendorInternal() {
        return BasicProtonationModel.getVendor();
    }

    /**
     *  Description of the Method
     *
     * @param  buffer  Description of the Parameter
     */
    protected void parseLine(String buffer) {
        Vector vs = new Vector();
        SMARTSPatternMatcher sp;
        if (buffer.trim().equals("") || (buffer.charAt(0) == '#')) {
            return;
        }
        if (HelperMethods.EQn(buffer, "TRANSFORM", 7)) {
            HelperMethods.tokenize(vs, buffer);
            if ((vs.size() == 0) || (vs.size() < 4)) {
                return;
            }
            BasicTransformationRulesHolder tsfm = new BasicTransformationRulesHolder();
            if (!tsfm.init((String) vs.get(1), (String) vs.get(3))) {
                tsfm = null;
                return;
            }
            transformation.add(tsfm);
        } else if (HelperMethods.EQn(buffer, "SEEDCHARGE", 10)) {
            HelperMethods.tokenize(vs, buffer);
            if ((vs.size() == 0) || (vs.size() < 2)) {
                return;
            }
            sp = new BasicSMARTSPatternMatcher();
            if (!sp.init((String) vs.get(1)) || ((vs.size() - 2) != sp.getQueryAtomsSize())) {
                sp = null;
                return;
            }
            double[] seedCharge = new double[vs.size() - 2];
            int index = 0;
            for (int i = 2; i < vs.size(); i++, index++) {
                seedCharge[index] = Double.parseDouble((String) vs.get(i));
            }
            seedChargeGM.add(new BasicSMARTSPatternDoubles(sp, seedCharge));
        }
    }
}

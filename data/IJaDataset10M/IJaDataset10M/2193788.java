package org.xmlcml.cml.normalise;

import org.xmlcml.cml.CMLAtom;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLCoordinate2;
import org.xmlcml.cml.CMLCoordinate3;
import org.xmlcml.molutil.ChemicalElement;

/** provides methods for normalising an CMLAtom */
public interface NormalAtom extends CMLAtom {

    /** no action on hydrogens */
    public static final int HMODE_NONE = 0;

    /** generate all explicit hydrogens on the atom	and reset HydrogenCount
	* to zero. No default information is provided for generated H's other than
	* elementType (H) and normal default values. There may be other routines
	* for generating this.
	*/
    public static final int HMODE_GENERATE = HMODE_NONE + 1;

    /** use a mixed mode (implicit and explicit Hydrogens. The hydrogenCount
	* will be set to the count for the IMPLICIT atoms (e.g. H-C-Cl (for chloro-methane)
	* would generate an IMPLICT count of 2 on the C atom and leave the explicit H
	* atom in that state.
	*/
    public static final int HMODE_MIXED = HMODE_GENERATE + 1;

    /** subsume all explicit hydrogens into the hydrogen Count of the atom
	* If bonds to these H-atoms carry stereo-information this must be processed and
	* saved, i.e. wedge bonds to H generate atom parity values first. Then H-atoms
	* are deleted. Isotopic information on H throws an Exception; HMODE_MIXED
	* should be used
	*/
    public static final int HMODE_SUBSUME = HMODE_MIXED + 1;

    public static final int HMODE_OPTION_COUNT = HMODE_SUBSUME + 1;

    public static final String[] HMODE_TYPES = { "No action on hydrogens", "Generate explicit hydrogens", "Mixed mode (experimental)", "Subsume hydrogens into implict counts" };

    /** generate parity for a "chiral" atom. Uses either the parity (if given) or tries
	* to analyse Wedge/Hatch symbols. After this, getParity() should be valid
	@param int mode Choose from STEREO_* flags
	@see AbstractBuiltinContainer
	*/
    public void processParity(int mode) throws CMLException;

    /** process hydrogens. by default only do C, N and O atoms. Use
	* multiple bonds, charges, etc. Not completely tested.
	* @param int mode. Choose from HMODE_* flags 
	*/
    public void processHydrogens(int mode) throws CMLException;

    public int getDoubleBondEquivalents();

    /** increase XY2 coordinate; if atom.xy2 is null, sets coordinate to r*/
    public void plusXY2(CMLCoordinate2 r);

    /** transform XY2 coordinate; if atom.xy2 is null, no op*/
    public void transform(jumbo.euclid.Transform2 t);

    /** increase XYZ3 coordinate; if atom.xyz3 is null, sets coordinate to r */
    public void plusXYZ3(CMLCoordinate3 p);

    /** increase XYZFract coordinate; if atom.xyzFract is null, sets coordinate to p */
    public void plusXYZFract(CMLCoordinate3 p);

    /** gets SMILES representation of atom */
    public String getSMILES();

    /** get the chemical element for the atom */
    public ChemicalElement getChemicalElement();

    /** set the chemical element for the atom */
    public void setChemicalElement(ChemicalElement chemicalElement);

    /** get the geometrical hybridisation (experimental) */
    public int getGeometricHybridization();
}

package ra.lrrr.clustering.container;

import org.openscience.cdk.protein.data.PDBAtom;

public class PDBAtomWithOriginID {

    private PDBAtom pdbAtom;

    private String pdbOrigin;

    /**
	 * @param pdbAtom
	 * @param pdbOrigin
	 */
    public PDBAtomWithOriginID(PDBAtom pdbAtom, String pdbOrigin) {
        super();
        this.pdbAtom = pdbAtom;
        this.pdbOrigin = pdbOrigin;
    }

    /**
	 * @return the pdbAtom
	 */
    public PDBAtom getPDBAtom() {
        return pdbAtom;
    }

    /**
	 * @return the pdbOrigin
	 */
    public String getPDBOrigin() {
        return pdbOrigin;
    }
}

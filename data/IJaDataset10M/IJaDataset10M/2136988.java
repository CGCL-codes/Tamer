package ra.persei.clustering;

import java.util.ArrayList;
import javax.vecmath.Point3d;
import org.openscience.cdk.protein.data.PDBAtom;
import ra.persei.container.PDBAtomWithOriginID;
import ra.raMa.RaMa;

public class ClusteringToolsPDBAtomWithOriginID {

    public static ArrayList<PDBAtomWithOriginID> getArrayListWithAllAtomsPartOf(String aminoAcid, ArrayList<PDBAtomWithOriginID> listToFilter) {
        ArrayList<PDBAtomWithOriginID> returnArrayList = new ArrayList<PDBAtomWithOriginID>();
        for (PDBAtomWithOriginID pdbAtom : listToFilter) {
            if (pdbAtom.getPDBAtom().getAtomTypeName().toLowerCase().startsWith((aminoAcid.toLowerCase()))) {
                returnArrayList.add(pdbAtom);
            }
        }
        return returnArrayList;
    }

    /**
	 * 
	 * @return
	 */
    public static Point3d generateMittelvektorOfPunktWolke(ArrayList<PDBAtomWithOriginID> punktWolkeWithOriginalID) {
        ArrayList<PDBAtom> punktWolke = new ArrayList<PDBAtom>();
        for (PDBAtomWithOriginID pdbAtomWithOriginID : punktWolkeWithOriginalID) {
            punktWolke.add(pdbAtomWithOriginID.getPDBAtom());
        }
        return ClusteringTools.generateMittelvektorOfPunktWolke(punktWolke);
    }

    /**
	 * 
	 * entering o2!
	 * @param allAtoms
	 * @return
	 */
    public static double getMaximumDistance(ArrayList<PDBAtomWithOriginID> allAtoms) {
        double overallMaximumDistance = 0d;
        for (PDBAtomWithOriginID pdbAtomOriginID : allAtoms) {
            double thisMaximumDistance = 0d;
            for (PDBAtomWithOriginID otherAtom : allAtoms) {
                double tempDistance = RaMa.getEuclidicDistance(pdbAtomOriginID.getPDBAtom().getPoint3d(), otherAtom.getPDBAtom().getPoint3d());
                thisMaximumDistance = Math.max(thisMaximumDistance, tempDistance);
            }
            overallMaximumDistance = Math.max(overallMaximumDistance, thisMaximumDistance);
        }
        return overallMaximumDistance;
    }

    /**
	 * 
	 * entering o2!
	 * @param allAtoms
	 * @return
	 */
    public static ArrayList<PDBAtomWithOriginID> getUniquePDBIsFrom(ArrayList<PDBAtomWithOriginID> allAtoms) {
        ArrayList<PDBAtomWithOriginID> returnList = new ArrayList<PDBAtomWithOriginID>();
        for (PDBAtomWithOriginID pdbid : allAtoms) {
            boolean add = true;
            for (PDBAtomWithOriginID id : returnList) {
                if (id.getPDBOrigin().equals(pdbid.getPDBOrigin())) {
                    add = false;
                    break;
                }
            }
            if (add) {
                returnList.add(pdbid);
            }
        }
        return returnList;
    }

    public static String getCommaSeperated(ArrayList<PDBAtomWithOriginID> allAtoms) {
        StringBuffer sb = new StringBuffer();
        boolean firstElement = true;
        for (PDBAtomWithOriginID pdbAtomWithOriginID : allAtoms) {
            if (!firstElement) {
                sb.append(", " + pdbAtomWithOriginID.getPDBOrigin());
            } else {
                sb.append(pdbAtomWithOriginID.getPDBOrigin());
                firstElement = false;
            }
        }
        return sb.toString();
    }
}

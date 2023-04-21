package ra.lrrr.superposition;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;
import ra.lrrr.tools.PDBToAtomArrayConverter;

public class LigandParser {

    /** The logger. */
    private static org.apache.log4j.Logger log = Logger.getLogger(LigandParser.class);

    /**
     * 
     * @param pdbFile
     * @return
     */
    public static ArrayList<AtomContainer> getAtomContainertOfAtoms(String pdbFile, String ligandName, boolean onlyReadInFirstLigandOfProtein) {
        ArrayList<AtomContainer> returnList = new ArrayList<AtomContainer>();
        if (pdbFile.contains("/home/ra/bibliothek/lrrr/validationruns_PLP/without_1ELQ.pdb/lrrr/0.1506849315068493")) {
            System.out.println("is: " + pdbFile);
        }
        ArrayList<IAtom> atomArray = PDBToAtomArrayConverter.getArrayListOfAtoms(pdbFile, true, true);
        String residueSequenceNumber = null;
        String chainID = null;
        AtomContainer tempContainer = new AtomContainer();
        for (IAtom atom : atomArray) {
            PDBAtom thisAtom = (PDBAtom) atom;
            if (thisAtom.getResName().equals(ligandName)) {
                if ((residueSequenceNumber == null) || (chainID == null)) {
                    log.debug("setting residue sequence number/ID " + "for the first time");
                    residueSequenceNumber = thisAtom.getResSeq();
                    chainID = thisAtom.getChainID();
                } else if ((!residueSequenceNumber.equals(thisAtom.getResSeq())) || (!chainID.equals(thisAtom.getChainID()))) {
                    log.debug("there is more than one ligand here " + "- saving this one and continuing...");
                    returnList.add(tempContainer);
                    tempContainer = new AtomContainer();
                    residueSequenceNumber = thisAtom.getResSeq();
                    chainID = thisAtom.getChainID();
                }
                if (residueSequenceNumber.equals(thisAtom.getResSeq()) && chainID.equals(thisAtom.getChainID())) {
                    tempContainer.addAtom(thisAtom);
                }
            }
        }
        if (tempContainer.getAtomCount() > 0) {
            returnList.add(tempContainer);
        }
        if ((onlyReadInFirstLigandOfProtein) && (returnList.size() > 0)) {
            ArrayList<AtomContainer> aret = new ArrayList<AtomContainer>();
            aret.add(returnList.get(0));
            return aret;
        } else {
            return returnList;
        }
    }
}

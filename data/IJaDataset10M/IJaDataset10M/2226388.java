package subsearch.index.features.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import subsearch.graph.MoleculeGraph;
import subsearch.index.features.storage.FeatureStorage;

/**
 * A copy of the CDK fingerprinter, modified for comparability.
 */
public class CDKPathExtractor extends FeatureExtractor<String> {

    private int searchDepth;

    public CDKPathExtractor(MoleculeGraph g, FeatureStorage<? super String, ?> featureStorage, int searchDepth) {
        super(g, featureStorage);
        this.searchDepth = searchDepth;
    }

    public void extractFeatures() {
        Map paths = findPathes(((MoleculeGraph) graph).getAtomContainer(), searchDepth);
        for (Iterator e = paths.values().iterator(); e.hasNext(); ) {
            featureStorage.processFeature((String) e.next());
        }
    }

    protected Map findPathes(IAtomContainer ac, int searchDepth) {
        Map paths = new HashMap();
        List currentPath = new ArrayList();
        for (int f = 0; f < ac.getAtomCount(); f++) {
            currentPath.clear();
            currentPath.add(ac.getAtom(f));
            checkAndStore(currentPath, paths);
            depthFirstSearch(ac, ac.getAtom(f), paths, currentPath, 0, searchDepth);
        }
        return paths;
    }

    private void depthFirstSearch(IAtomContainer ac, IAtom root, Map paths, List currentPath, int currentDepth, int searchDepth) {
        List bonds = ac.getConnectedBondsList(root);
        org.openscience.cdk.interfaces.IAtom nextAtom = null;
        List newPath = null;
        String bondSymbol = null;
        currentDepth++;
        for (int f = 0; f < bonds.size(); f++) {
            IBond bond = (IBond) bonds.get(f);
            nextAtom = bond.getConnectedAtom(root);
            if (!currentPath.contains(nextAtom)) {
                newPath = new ArrayList(currentPath);
                bondSymbol = this.getBondSymbol(bond);
                newPath.add(bondSymbol);
                newPath.add(nextAtom);
                checkAndStore(newPath, paths);
                if (currentDepth < searchDepth) {
                    depthFirstSearch(ac, nextAtom, paths, newPath, currentDepth, searchDepth);
                }
            } else {
            }
        }
    }

    private void checkAndStore(List newPath, Map paths) {
        String newPathString = "";
        for (int f = 0; f < newPath.size(); f++) {
            if ((newPath.get(f)) instanceof IAtom) {
                newPathString += convertSymbol(((IAtom) newPath.get(f)).getSymbol());
            } else {
                newPathString += (String) newPath.get(f);
            }
        }
        String storePath = new String(newPathString);
        String reversePath = new StringBuffer(storePath).reverse().toString();
        if (reversePath.compareTo(newPathString) < 0) {
            storePath = reversePath;
        }
        if (!paths.containsKey(storePath)) {
            paths.put(storePath, storePath);
        }
    }

    private String convertSymbol(String symbol) {
        String returnSymbol = symbol;
        if (symbol.equals("Cl")) {
            symbol = "X";
        } else if (symbol.equals("Si")) {
            symbol = "Y";
        } else if (symbol.equals("Br")) {
            symbol = "Z";
        }
        return returnSymbol;
    }

    /**
	 *  Gets the bondSymbol attribute of the Fingerprinter class
	 *
	 *@param  bond  Description of the Parameter
	 *@return       The bondSymbol value
	 */
    protected String getBondSymbol(org.openscience.cdk.interfaces.IBond bond) {
        String bondSymbol = "";
        if (bond.getFlag(CDKConstants.ISAROMATIC)) {
            bondSymbol = ":";
        } else if (bond.getOrder() == IBond.Order.SINGLE) {
            bondSymbol = "-";
        } else if (bond.getOrder() == IBond.Order.DOUBLE) {
            bondSymbol = "=";
        } else if (bond.getOrder() == IBond.Order.TRIPLE) {
            bondSymbol = "#";
        }
        return bondSymbol;
    }
}

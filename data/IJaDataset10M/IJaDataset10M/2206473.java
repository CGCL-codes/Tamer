package joelib2.gui.render3D.util;

import joelib2.data.BasicElementHolder;
import joelib2.feature.types.atomlabel.AtomENPauling;
import joelib2.feature.types.atomlabel.AtomHybridisation;
import joelib2.feature.types.atomlabel.AtomImplicitValence;
import joelib2.feature.types.atomlabel.AtomInAromaticSystem;
import joelib2.feature.types.atomlabel.AtomPartialCharge;
import joelib2.feature.types.bondlabel.BondInAromaticSystem;
import joelib2.molecule.Atom;
import joelib2.molecule.Bond;
import org.apache.log4j.Category;

/**
 * Description of the Class
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.10 $, $Date: 2005/02/17 16:48:34 $
 */
public class MolViewerEventAdapter implements MolViewerEventListener {

    private static Category logger = Category.getInstance("joelib2.gui.render3D.util.MolViewerEventAdapter");

    /**
     *Constructor for the MolViewerEventAdapter object
     */
    public MolViewerEventAdapter() {
    }

    public void atomPicked(MolViewerEvent event) {
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer(100);
            Atom atom = (Atom) event.getParam();
            sb.append("atom idx:" + atom.getIndex());
            sb.append(", atomic number:" + atom.getAtomicNumber());
            sb.append(", element symbol:" + BasicElementHolder.instance().getSymbol(atom.getAtomicNumber()));
            sb.append(", aromatic flag:" + AtomInAromaticSystem.isValue(atom));
            sb.append(", atom vector:" + atom.getCoords3D());
            sb.append(", hybridisation:" + AtomHybridisation.getIntValue(atom));
            sb.append(", implicit valence:" + AtomImplicitValence.getImplicitValence(atom));
            sb.append(", charge:" + atom.getFormalCharge());
            sb.append(", partial charge:" + AtomPartialCharge.getPartialCharge(atom));
            sb.append(", valence:" + atom.getValence());
            sb.append(", ext Electrons:" + BasicElementHolder.instance().getExteriorElectrons(atom.getAtomicNumber()));
            sb.append(", pauling electronegativity:" + AtomENPauling.getDoubleValue(atom));
            sb.append(", free electrons:" + atom.getFreeElectrons());
            logger.debug(sb.toString());
        }
    }

    public void bondPicked(MolViewerEvent event) {
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer(100);
            Bond bond = (Bond) event.getParam();
            sb.append("  atom #");
            sb.append(bond.getBeginIndex());
            sb.append(" is attached to atom #");
            sb.append(bond.getEndIndex());
            sb.append(" with bond of order ");
            sb.append(bond.getBondOrder());
            sb.append(" which is ");
            if (BondInAromaticSystem.isAromatic(bond)) {
                sb.append("an aromatic bond.");
            } else {
                sb.append("a non-aromatic bond.");
            }
            logger.debug(sb.toString());
        }
    }

    /**
     * Description of the Method
     *
     * @param event  Description of the Parameter
     */
    public void centralDisplayChange(MolViewerEvent event) {
    }
}

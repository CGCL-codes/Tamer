package fca.gui.lattice.tool;

import java.util.Stack;
import java.util.Vector;
import fca.core.lattice.NestedConcept;
import fca.core.lattice.NestedLattice;
import fca.core.lattice.operator.search.SearchApproximateExtentNested;
import fca.core.lattice.operator.search.SearchApproximateIntentNested;
import fca.core.util.BasicSet;
import fca.exception.LatticeMinerException;
import fca.gui.lattice.LatticePanel;
import fca.gui.lattice.element.GraphicalConcept;
import fca.gui.lattice.element.GraphicalLattice;
import fca.gui.lattice.element.LatticeStructure;
import fca.gui.util.DialogBox;
import fca.messages.GUIMessages;

/**
 * Panneau de recherche pour les treillis imbriqu�s
 * @author Ludovic Thomas
 * @version 1.0
 */
public class SearchNested extends Search {

    /**
	 * 
	 */
    private static final long serialVersionUID = -884650316657528607L;

    /**
	 * Constructeur
	 * @param l Le {@link GraphicalLattice} pour lequel ce panneau affiche les attributs et objets
	 * @param lp Le {@link LatticePanel} dans lequel est affich� le treillis
	 */
    public SearchNested(GraphicalLattice l, LatticePanel lp) {
        super(l, lp);
        exactMatchOnly.setVisible(false);
        exactMatchOnly.setSelected(true);
        openResultButton.setVisible(false);
    }

    @Override
    public void approximateMatchAction() throws LatticeMinerException {
        if (!isSearchOnObjects() && !isSearchOnAttributes()) {
            DialogBox.showMessageInformation(viewer, NO_SEARCH, GUIMessages.getString("GUI.noSearch"));
        } else {
            GraphicalConcept result = (GraphicalConcept) approximateMatch();
            showAndShakeResult(result);
            activateOpenResultButton(result);
        }
    }

    @Override
    public void exactMatchAction() {
        if (!isSearchOnObjects() && !isSearchOnAttributes()) {
            DialogBox.showMessageInformation(viewer, NO_SEARCH, GUIMessages.getString("GUI.noSearch"));
        } else {
            GraphicalConcept result = exactMatch();
            showAndShakeResult(result);
            activateOpenResultButton(result);
        }
    }

    /**
	 * Active ou non le bouton "OpenResult" pour ouvrir le resultat dans une nouvelle frame
	 * @param result le resultat {@link GraphicalConcept} a afficher
	 */
    private void activateOpenResultButton(GraphicalConcept result) {
        if (result != null && result.getInternalLattice() != null) {
            GraphicalLattice resultLattice = result.getInternalLattice();
            Vector<LatticeStructure> latticesStructures = new Vector<LatticeStructure>();
            latticesStructures.add(resultLattice.getLatticeStructure());
            latticesStructures.addAll(resultLattice.getInternalLatticeStructures());
            NestedLattice nestedLattice = resultLattice.getNestedLattice();
            GraphicalLattice graphLattice = new GraphicalLattice(nestedLattice, null, latticesStructures);
            resultOperation = graphLattice;
            openResultButton.setEnabled(true);
        } else {
            openResultButton.setEnabled(false);
            resultOperation = null;
        }
    }

    @Override
    protected GraphicalConcept searchApproximateNodeWithExtent(BasicSet extent) {
        SearchApproximateExtentNested search = new SearchApproximateExtentNested(lattice.getNestedLattice());
        Stack<NestedConcept> formalResult = search.perform(extent);
        GraphicalConcept result = null;
        if (!formalResult.isEmpty()) {
            GraphicalLattice latticeRec = lattice;
            for (NestedConcept node : formalResult) {
                result = latticeRec.getGraphicalConcept(node);
                latticeRec = result.getInternalLattice();
            }
        }
        return result;
    }

    @Override
    protected GraphicalConcept searchApproximateNodeWithIntent(BasicSet intent) {
        SearchApproximateIntentNested search = new SearchApproximateIntentNested(lattice.getNestedLattice());
        Stack<NestedConcept> formalResult = search.perform(intent);
        GraphicalConcept result = null;
        if (!formalResult.isEmpty()) {
            GraphicalLattice latticeRec = lattice;
            for (NestedConcept node : formalResult) {
                result = latticeRec.getGraphicalConcept(node);
                latticeRec = result.getInternalLattice();
            }
        }
        return result;
    }
}

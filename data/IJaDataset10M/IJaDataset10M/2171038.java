package fca.core.lattice.operator.search;

import fca.core.lattice.ConceptLattice;
import fca.core.lattice.DataCel;
import fca.core.lattice.FormalConcept;
import fca.core.lattice.operator.Operator;
import fca.core.util.BasicSet;
import fca.core.util.Couple;
import fca.core.util.Triple;

/**
 * Op�ration d'approximation simple sur un treillis plat </br> L'id�e d'approximation d'un couple
 * (X,Y), non n�cessairement complet, au sein d'un treillis vient initialement de Professeur Mohamed
 * Quafafou de l'universit� d'Aix-Marseille et a �t� reformul�e par Rokia Missaoui de l'universit�
 * du Qu�bec en Outaouais.
 * @author Ludovic Thomas
 * @version 1.0
 */
public class ApproximationSimple extends Operator<ConceptLattice, DataCel, Triple<ConceptLattice, ConceptLattice, ConceptLattice>> {

    /**
	 * Constructeur d'une op�ration d'approximation simple sur un treillis plat
	 * @param data le treillis dont sur lequel on doit faire l'approximation
	 */
    public ApproximationSimple(ConceptLattice data) {
        super(data, "ApproximationSimple");
    }

    @Override
    public Triple<ConceptLattice, ConceptLattice, ConceptLattice> perform(DataCel entry) {
        ConceptLattice latticeL1 = approximationL1Rec(entry, data.getTopConcept());
        Couple<ConceptLattice, ConceptLattice> latticeL2andUI = approximationL2etUIRec(entry, data.getBottomConcept(), latticeL1);
        return new Triple<ConceptLattice, ConceptLattice, ConceptLattice>(latticeL1, latticeL2andUI.getFirst(), latticeL2andUI.getSecond());
    }

    /**
	 * Realise l'approximation par rapport � l'extension afin de r�cup�rer le treillis L1. C'est une
	 * recherche recursive dont le premier appel est fait pour le supremum
	 * @param c la cellule � approximer
	 * @param fc le concept formel courant dans la recherche recursive
	 * @return le treillis L1
	 */
    private ConceptLattice approximationL1Rec(DataCel c, FormalConcept fc) {
        FormalConcept topL1 = fc.cloneIntExt();
        BasicSet extentC = c.getExtent();
        for (FormalConcept children : fc.getChildren()) {
            BasicSet extentChildren = children.getExtent();
            if ((extentC.size() <= extentChildren.size()) && (extentChildren.isIncluding(extentC))) {
                ConceptLattice lattL1rec = approximationL1Rec(c, children);
                topL1.addChild(lattL1rec.getTopConcept());
            }
        }
        return new ConceptLattice(topL1, "L1");
    }

    /**
	 * Realise l'approximation par rapport � l'intention et l'intersection avec L1 afin de r�cup�rer
	 * le treillis L2 et UI. C'est une recherche recursive dont le premier appel est fait pour
	 * l'infimum
	 * @param c la cellule � approximer
	 * @param fc le concept formel courant dans la recherche recursive
	 * @param latticeL1 le treillis L1 utilis� pour le calcul de l'intersection
	 * @return le treillis L2 et UI
	 */
    private Couple<ConceptLattice, ConceptLattice> approximationL2etUIRec(DataCel c, FormalConcept fc, ConceptLattice latticeL1) {
        FormalConcept topL2 = fc.cloneIntExt();
        FormalConcept topUI = null;
        if (latticeL1.getConceptWithIntent(fc.getIntent()) != null) {
            topUI = fc.cloneIntExt();
        }
        BasicSet intentC = c.getIntent();
        for (FormalConcept parent : fc.getParents()) {
            BasicSet intentParent = parent.getIntent();
            if ((intentC.size() <= intentParent.size()) && (intentParent.isIncluding(intentC))) {
                Couple<ConceptLattice, ConceptLattice> latticeL2UIrec = approximationL2etUIRec(c, parent, latticeL1);
                topL2.addParent(latticeL2UIrec.getFirst().getBottomConcept());
                ConceptLattice latticeUIrec = latticeL2UIrec.getSecond();
                if ((topUI != null) && (latticeUIrec != null) && (latticeL1.getConceptWithIntent(parent.getIntent()) != null)) {
                    if (latticeUIrec.getBottomConcept().getIntent().isIncluding(topUI.getIntent())) {
                        topUI = latticeUIrec.getBottomConcept();
                    } else {
                        topUI.addParent(latticeUIrec.getBottomConcept());
                    }
                } else if ((topUI != null) && (latticeUIrec != null)) {
                    if (latticeUIrec.getBottomConcept().getIntent().isIncluding(topUI.getIntent())) {
                        topUI = latticeUIrec.getBottomConcept();
                    }
                } else if (latticeUIrec != null) {
                    topUI = latticeUIrec.getBottomConcept();
                }
            }
        }
        ConceptLattice latticeL2 = new ConceptLattice(topL2, "L2", true);
        ConceptLattice latticeUI = null;
        if (topUI != null) {
            latticeUI = new ConceptLattice(topUI, "UI", true);
        }
        return new Couple<ConceptLattice, ConceptLattice>(latticeL2, latticeUI);
    }
}

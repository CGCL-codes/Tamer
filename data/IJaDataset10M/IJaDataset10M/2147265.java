package fca.core.lattice;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import fca.core.context.binary.BinaryContext;
import fca.core.util.BasicSet;
import fca.exception.AlreadyExistsException;
import fca.exception.InvalidTypeException;
import fca.exception.LMLogger;
import fca.messages.CoreMessages;

public class ConceptLattice {

    private BinaryContext context;

    private Vector<FormalConcept> concepts;

    private FormalConcept topConcept;

    private FormalConcept bottomConcept;

    private String name;

    private boolean generatorsCalculated;

    public ConceptLattice(BinaryContext bc) {
        context = bc;
        generatorsCalculated = false;
        BordatAlgo algo = new BordatAlgo(context);
        concepts = algo.getConcepts();
        if (concepts.size() == 0) {
            topConcept = null;
            bottomConcept = null;
        } else {
            FormalConcept currentConcept = concepts.elementAt(0);
            while (currentConcept.getParents().size() > 0) {
                Vector<FormalConcept> parents = currentConcept.getParents();
                currentConcept = parents.elementAt(0);
            }
            topConcept = currentConcept;
            currentConcept = concepts.elementAt(0);
            while (currentConcept.getChildren().size() > 0) {
                Vector<FormalConcept> children = currentConcept.getChildren();
                currentConcept = children.elementAt(0);
            }
            bottomConcept = currentConcept;
        }
        name = bc.getName();
    }

    public ConceptLattice(FormalConcept fc, String n) {
        concepts = findConcepts(fc);
        topConcept = fc;
        FormalConcept currentConcept = fc;
        while (currentConcept.getChildren().size() > 0) {
            Vector<FormalConcept> children = currentConcept.getChildren();
            currentConcept = children.elementAt(0);
        }
        bottomConcept = currentConcept;
        name = n;
        context = getContext();
        generatorsCalculated = false;
    }

    public ConceptLattice(FormalConcept fc, String n, boolean bottomFirst) {
        concepts = findConcepts(fc, bottomFirst);
        if (bottomFirst) {
            bottomConcept = fc;
            FormalConcept currentConcept = fc;
            while (currentConcept.getParents().size() > 0) {
                Vector<FormalConcept> parent = currentConcept.getParents();
                currentConcept = parent.elementAt(0);
            }
            topConcept = currentConcept;
        } else {
            topConcept = fc;
            FormalConcept currentConcept = fc;
            while (currentConcept.getChildren().size() > 0) {
                Vector<FormalConcept> children = currentConcept.getChildren();
                currentConcept = children.elementAt(0);
            }
            bottomConcept = currentConcept;
        }
        name = n;
        context = getContext();
        generatorsCalculated = false;
        System.out.println("Calcul des gene3");
    }

    /**
	 * Trouve tous les noeuds rejoignable � partir du noeud sp�cifi�, en incluant celui-ci
	 * @param node Le ConceptNode � partir duquel les noeuds doivent �tre cherch�s
	 * @return Vector La liste des ConceptNode accessibles � partir du noeud sp�cifi�
	 */
    private Vector<FormalConcept> findConcepts(FormalConcept node) {
        return findConcepts(node, false);
    }

    /**
	 * Trouve tous les noeuds rejoignable � partir du noeud sp�cifi�, en incluant celui-ci
	 * @param node Le ConceptNode � partir duquel les noeuds doivent �tre cherch�s
	 * @return Vector La liste des ConceptNode accessibles � partir du noeud sp�cifi�
	 */
    private Vector<FormalConcept> findConcepts(FormalConcept node, boolean bottomFirst) {
        Vector<FormalConcept> visitedNodes = new Vector<FormalConcept>();
        visitedNodes.add(node);
        if (!bottomFirst) {
            List<FormalConcept> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                FormalConcept currentChild = children.get(i);
                if (!visitedNodes.contains(currentChild)) visitedNodes.add(currentChild);
                Vector<FormalConcept> childNodes = findConcepts(currentChild, bottomFirst);
                for (int j = 0; j < childNodes.size(); j++) {
                    FormalConcept currentNode = childNodes.elementAt(j);
                    if (!visitedNodes.contains(currentNode)) visitedNodes.add(currentNode);
                }
            }
        } else {
            List<FormalConcept> parents = node.getParents();
            for (int i = 0; i < parents.size(); i++) {
                FormalConcept currentParent = parents.get(i);
                if (!visitedNodes.contains(currentParent)) visitedNodes.add(currentParent);
                Vector<FormalConcept> parentsNodes = findConcepts(currentParent, bottomFirst);
                for (int j = 0; j < parentsNodes.size(); j++) {
                    FormalConcept currentNode = parentsNodes.elementAt(j);
                    if (!visitedNodes.contains(currentNode)) visitedNodes.add(currentNode);
                }
            }
        }
        return visitedNodes;
    }

    public Vector<FormalConcept> getConcepts() {
        return concepts;
    }

    /**
	 * @return le supremum
	 */
    public FormalConcept getTopConcept() {
        return topConcept;
    }

    /**
	 * @return l'infimum
	 */
    public FormalConcept getBottomConcept() {
        return bottomConcept;
    }

    /**
	 * Permet d'obtenir le concept qui poss�de l'intention sp�cifi�e. Retourne null si aucun concept
	 * n'est trouv�.
	 * @param intent Le BasicSet contenant l'intent recherch�
	 * @return Le FormalConcept qui poss�de l'intention sp�cifi�e
	 */
    public FormalConcept getConceptWithIntent(BasicSet intent) {
        if (topConcept == null) return null;
        BasicSet topIntent = topConcept.getIntent();
        if (intent.size() < topIntent.size()) return null;
        BasicSet intersection = bottomConcept.getIntent().intersection(intent);
        if (intent.size() != intersection.size()) return null;
        if (intent.size() == topIntent.size() && intent.equals(topIntent)) return topConcept; else {
            FormalConcept currentConcept = topConcept;
            BasicSet currentIntent = currentConcept.getIntent();
            BasicSet currentInter = intersection.intersection(currentIntent);
            BasicSet remainder = intersection.difference(currentIntent);
            while (currentConcept != null && currentInter.size() != intersection.size() && currentInter.size() == currentIntent.size()) {
                Vector<FormalConcept> children = currentConcept.getChildren();
                currentConcept = null;
                for (int i = 0; i < children.size() && currentConcept == null; i++) {
                    FormalConcept child = children.elementAt(i);
                    BasicSet childIntent = child.getIntent();
                    BasicSet childInter = intersection.intersection(childIntent);
                    BasicSet childRemainder = childIntent.difference(childInter);
                    if (childInter.size() > 0 && childRemainder.size() == 0) {
                        currentConcept = child;
                        currentIntent = currentConcept.getIntent();
                        currentInter = intersection.intersection(currentIntent);
                        remainder = intersection.difference(currentIntent);
                    }
                }
            }
            if (currentInter.size() != currentIntent.size()) return null;
            if (remainder.size() == 0) return currentConcept;
        }
        return null;
    }

    /**
	 * @return le nom du treillis
	 */
    public String getName() {
        return name;
    }

    public int size() {
        return concepts.size();
    }

    @Override
    public String toString() {
        String str = CoreMessages.getString("Core.name") + ": " + context.getName() + "\n" + CoreMessages.getString("Core.conceptCount") + ": " + concepts.size() + "\n" + CoreMessages.getString("Core.attributes") + ": " + bottomConcept.getIntent().toString() + "\n" + CoreMessages.getString("Core.objects") + ": " + topConcept.getExtent().toString();
        for (int i = 0; i < concepts.size(); i++) {
            FormalConcept c = concepts.elementAt(i);
            str = str + "\n\n" + CoreMessages.getString("Core.concept") + " : " + c.getIntent().toString() + "\n" + CoreMessages.getString("Core.children") + " :";
            Vector<FormalConcept> children = c.getChildren();
            for (int j = 0; j < children.size(); j++) str = str + " " + (children.elementAt(j)).getIntent().toString();
            str = str + "\n" + CoreMessages.getString("Core.parents") + " :";
            Vector<FormalConcept> parents = c.getParents();
            for (int j = 0; j < parents.size(); j++) str = str + " " + (parents.elementAt(j)).getIntent().toString();
        }
        return str;
    }

    public boolean areGeneratorsCalculated() {
        return generatorsCalculated;
    }

    /**
	 * @param calculated
	 */
    public void setGeneratorsCalculated(boolean calculated) {
        generatorsCalculated = calculated;
    }

    public void findGenerators() {
        new JenAlgorithm(this);
    }

    public BinaryContext getContext() {
        Vector<String> attributes = new Vector<String>();
        attributes.addAll(getBottomConcept().getIntent());
        Vector<String> objects = new Vector<String>();
        objects.addAll(getTopConcept().getExtent());
        BinaryContext newContext = new BinaryContext(getName());
        try {
            for (int i = 0; i < objects.size(); i++) newContext.addObject(objects.elementAt(i));
            for (int i = 0; i < attributes.size(); i++) newContext.addAttribute(attributes.elementAt(i));
        } catch (AlreadyExistsException e) {
            LMLogger.logSevere(e, false);
        }
        Vector<FormalConcept> conceptList = getConcepts();
        for (int i = 0; i < conceptList.size(); i++) {
            FormalConcept currentConcept = conceptList.elementAt(i);
            Iterator<String> intent = currentConcept.getIntent().iterator();
            while (intent.hasNext()) {
                String currAtt = intent.next();
                Iterator<String> extent = currentConcept.getExtent().iterator();
                while (extent.hasNext()) {
                    String currObj = extent.next();
                    try {
                        newContext.setValueAt(BinaryContext.TRUE, currObj, currAtt);
                    } catch (InvalidTypeException e) {
                        LMLogger.logSevere(e, false);
                    }
                }
            }
        }
        return newContext;
    }
}

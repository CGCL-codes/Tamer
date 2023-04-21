package cspfj.problem;

import java.util.ArrayList;
import java.util.List;
import cspfj.constraint.Constraint;
import cspfj.constraint.DynamicConstraint;
import cspfj.util.BitVector;

/**
 * @author scand1sk
 * 
 */
public final class Variable implements Cloneable {

    /**
	 * Contraintes impliquant la variable.
	 */
    private Constraint[] constraints;

    private DynamicConstraint[] dynamicConstraints;

    private Domain domain;

    private boolean assigned;

    /**
	 * Pour générer l'ID.
	 */
    private static int nbV = 0;

    /**
	 * ID de la variable.
	 */
    private final int id;

    private final String name;

    private int[] positionInConstraint;

    public Variable(final int[] dom) {
        this(dom, null);
    }

    /**
	 * @param dom
	 *            Liste des éléments du domaine.
	 * @param p
	 *            Le problème lié.
	 */
    public Variable(final int[] dom, String name) {
        this.domain = new BitVectorDomain(dom);
        assigned = false;
        id = nbV++;
        if (name == null) {
            this.name = "X" + id;
        } else {
            this.name = name;
        }
    }

    /**
	 * Réinitialise le générateur d'ID (pour charger un nouveau problème).
	 */
    public static void resetVId() {
        nbV = 0;
    }

    @Override
    public String toString() {
        return name + "[" + getDomainSize() + "]";
    }

    /**
	 * @param constraints2
	 *            Liste des contraintes impliquant la variable
	 */
    public void setInvolvingConstraints(final Constraint[] constraints2) {
        this.constraints = constraints2;
        final List<DynamicConstraint> dynamicConstraints = new ArrayList<DynamicConstraint>();
        for (Constraint c : constraints2) {
            if (c instanceof DynamicConstraint) {
                dynamicConstraints.add((DynamicConstraint) c);
            }
        }
        this.dynamicConstraints = dynamicConstraints.toArray(new DynamicConstraint[dynamicConstraints.size()]);
        positionInConstraint = new int[constraints2.length];
        for (int i = constraints2.length; --i >= 0; ) {
            updatePositionInConstraint(i);
        }
    }

    public void updatePositionInConstraint(final int constraintPosition) {
        positionInConstraint[constraintPosition] = constraints[constraintPosition].getPosition(this);
        constraints[constraintPosition].setPositionInVariable(positionInConstraint[constraintPosition], constraintPosition);
    }

    /**
	 * @return La taille du domaine
	 */
    public int getDomainSize() {
        return domain.size();
    }

    /**
	 * @return L'état (assignée ou non) de la variable
	 */
    public boolean isAssigned() {
        return assigned;
    }

    /**
	 * @param index
	 *            La valeur à assigner
	 */
    public void assign(final int index, final Problem problem) {
        assert !assigned;
        assert domain.present(index);
        assigned = true;
        domain.setSingle(index);
        problem.decreaseFutureVariables();
    }

    /**
	 * @param level
	 *            Le niveau en cours
	 */
    public void unassign(final Problem problem) {
        assert assigned;
        assigned = false;
        problem.increaseFutureVariables();
    }

    /**
	 * @param index
	 *            L'index à supprimer
	 * @param level
	 *            Le niveau en cours
	 */
    public void remove(final int index) {
        assert !assigned : "Trying to remove a value from an assigned variable";
        assert domain.present(index);
        domain.remove(index);
    }

    /**
	 * @return L'ID de la variable
	 */
    public int getId() {
        return id;
    }

    public long getNbSupports(final int index) {
        long nbSupports = 0;
        for (Constraint c : constraints) {
            final long nbSup = c.getNbSupports(this, index);
            if (nbSup <= 0) {
                return -1;
            }
            nbSupports += nbSup;
        }
        return nbSupports;
    }

    public Constraint[] getInvolvingConstraints() {
        return constraints;
    }

    public DynamicConstraint[] getDynamicConstraints() {
        return dynamicConstraints;
    }

    public Domain getDomain() {
        return domain;
    }

    public int getFirst() {
        return domain.first();
    }

    public int getLast() {
        return domain.last();
    }

    public int[] getCurrentDomain() {
        return domain.currentValues();
    }

    public String getName() {
        return name;
    }

    public int getNext(final int index) {
        return domain.next(index);
    }

    public int getPrev(final int index) {
        return domain.prev(index);
    }

    public int getLastAbsent() {
        return domain.lastAbsent();
    }

    public int getPrevAbsent(final int index) {
        return domain.prevAbsent(index);
    }

    public BitVector getBitDomain() {
        return ((BitVectorDomain) domain).getBitVector();
    }

    public Variable clone() throws CloneNotSupportedException {
        final Variable variable = (Variable) super.clone();
        variable.domain = domain.clone();
        return variable;
    }

    public int getPositionInConstraint(final int constraint) {
        return positionInConstraint[constraint];
    }

    public void setLevel(int level) {
        domain.setLevel(level);
    }

    public void restoreLevel(int level) {
        domain.restoreLevel(level);
    }

    public void reset(Problem problem) {
        if (isAssigned()) {
            unassign(problem);
        }
        domain.restoreLevel(0);
    }

    public boolean isPresent(int index) {
        return domain.present(index);
    }

    public int getValue(int index) {
        return domain.value(index);
    }
}

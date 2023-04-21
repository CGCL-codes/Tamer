package uk.ac.ebi.rhea.domain;

import java.io.IOException;
import java.util.Collection;
import org.apache.commons.collections.Bag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.bag.TreeBag;
import uk.ac.ebi.biobabel.util.collections.ChemicalElementSymbolComparator;

/**
 * Exception thrown when the reaction is unbalanced.
 *
 * @author <a href="mailto:rafalcan@ebi.ac.uk">Rafael Alcantara</a>
 * @version 1.0
 */
public class StoichiometryException extends ReactionException {

    private static final long serialVersionUID = -8724373287381737017L;

    private Collection<?> leftAtoms;

    private Collection<?> rightAtoms;

    private Bag leftExcess;

    private Bag rightExcess;

    private int leftCharge, rightCharge;

    /**
	 * Creates a new <code>StoichiometryException</code> instance.
	 *
	 * @param left a <code>Collection</code> of atom counts for the left side.
	 * @param right a <code>Collection</code> of atom counts for the right side.
	 * @param leftCharge total charge for left side
	 * @param rightCharge total charge for right side
	 * @throws IOException 
	 */
    @SuppressWarnings("unchecked")
    public StoichiometryException(Collection<?> left, Collection<?> right, int leftCharge, int rightCharge) {
        this.leftAtoms = left;
        this.rightAtoms = right;
        this.leftCharge = leftCharge;
        this.rightCharge = rightCharge;
        try {
            ChemicalElementSymbolComparator elemComparator = new ChemicalElementSymbolComparator();
            leftExcess = new TreeBag(elemComparator);
            rightExcess = new TreeBag(elemComparator);
        } catch (IOException e) {
            leftExcess = new TreeBag();
            rightExcess = new TreeBag();
        }
        leftExcess.addAll(CollectionUtils.subtract(left, right));
        rightExcess.addAll(CollectionUtils.subtract(right, left));
    }

    public StoichiometryException(String message) {
        super(message);
    }

    /**
	 * @return a <code>org.apache.commons.collections.Bag</code> with the excess of
	 *     atoms on the left side of the reaction (if any).
	 */
    public Bag getLeftExcess() {
        return leftExcess;
    }

    /**
	 * @return The excess of atoms on the left side of the reaction as a string
	 * 		of the form <code>[X:m, Y:n...]</code> where <code>X, Y...</code>
	 * 		are chemical symbols, and <code>m, n...</code> are integers, or
	 * 		<code>null</code> if there is no left excess.
	 */
    public String getLeftExcessAsString() {
        return leftExcess.isEmpty() ? null : leftExcess.toString().replaceAll("(\\d+):(\\w+)", "$2:$1");
    }

    /**
	 * @return a <code>Collection</code> with the count of atoms in the left side
	 *     of the reaction.
	 */
    public Collection<?> getLeft() {
        return leftAtoms;
    }

    /**
	 * @return a <code>Collection</code> with the count of atoms in the right side
	 *     of the reaction.
	 */
    public Collection<?> getRight() {
        return rightAtoms;
    }

    /**
	 * @return a <code>org.apache.commons.collections.Bag</code> with the excess of
	 *     atoms on the right side of the reaction (if any).
	 */
    public Bag getRightExcess() {
        return rightExcess;
    }

    /**
	 * @return The excess of atoms on the right side of the reaction as a string
	 * 		of the form <code>[X:m, Y:n...]</code> where <code>X, Y...</code>
	 * 		are chemical symbols, and <code>m, n...</code> are integers, or
	 * 		<code>null</code> if there is no right excess.
	 */
    public String getRightExcessAsString() {
        return rightExcess.isEmpty() ? null : rightExcess.toString().replaceAll("(\\d+):(\\w+)", "$2:$1");
    }

    public int getLeftCharge() {
        return leftCharge;
    }

    public int getRightCharge() {
        return rightCharge;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder("Unbalanced reaction.");
        if (!CollectionUtils.isEmpty(leftExcess)) {
            sb.append(" Left excess: ").append(getLeftExcessAsString()).append('.');
        }
        if (!CollectionUtils.isEmpty(rightExcess)) {
            sb.append(" Right excess: ").append(getRightExcessAsString()).append('.');
        }
        if (leftCharge != rightCharge) {
            sb.append(" Unbalanced charge: ").append(leftCharge > 0 ? "+" : "").append(leftCharge).append(" (left), ").append(rightCharge > 0 ? "+" : "").append(rightCharge).append(" (right)");
        }
        return sb.toString();
    }
}

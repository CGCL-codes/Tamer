package fr.soleil.bensikin.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import fr.soleil.bensikin.data.context.ContextAttribute;

public class BensikinAttributesComparator implements Comparator {

    private Comparator referenceComparator;

    public BensikinAttributesComparator() {
        referenceComparator = Collator.getInstance(Locale.FRENCH);
    }

    public int compare(Object o1, Object o2) {
        if (o1 != null && o2 != null) {
            if (o1 instanceof ContextAttribute && o2 instanceof ContextAttribute) {
                return referenceComparator.compare(((ContextAttribute) o1).getName(), ((ContextAttribute) o2).getName());
            }
        }
        return 0;
    }
}

package au.edu.ausstage.mapping.types;

import java.util.Comparator;

/**
 * A class to compare venues using their name
 */
public class VenueNameComparator implements Comparator<Venue>, java.io.Serializable {

    /**
	 * Compare two venues sorting by name
	 *
	 * @param first  a venue object for comparison
	 * @param second a venue object for comparison
	 *
	 * @return the result of the comparison
	 */
    public int compare(Venue first, Venue second) {
        String firstValue = first.getName();
        String secondValue = second.getName();
        if (firstValue.equals(secondValue) == true) {
            firstValue += first.getStreet();
            secondValue += second.getStreet();
            firstValue += first.getSuburb();
            secondValue += second.getSuburb();
        }
        return firstValue.compareTo(secondValue);
    }
}

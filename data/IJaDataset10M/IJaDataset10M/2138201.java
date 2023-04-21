package org.conserve.sort;

/**
 * @author Erik Berglund
 *
 */
public class Descending extends Sorter {

    /**
	 * @param sortBy
	 */
    public Descending(Object sortBy) {
        super(sortBy);
    }

    @Override
    public String getKeyWord() {
        return "DESC";
    }
}

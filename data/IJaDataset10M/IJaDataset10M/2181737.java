package net.sourceforge.aiexperiments.symbiotic.examples.tspsymbiot;

public class NeighbourhoodException extends RuntimeException {

    /**
   * CONSTRUCTOR:
  */
    public NeighbourhoodException(String s) {
        super(s);
    }

    /**
   * CONSTRUCTOR:
  */
    public NeighbourhoodException(int index) {
        super("Incorrect index: " + index);
    }
}

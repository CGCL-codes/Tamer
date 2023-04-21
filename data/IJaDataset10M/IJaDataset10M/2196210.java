package nl.ru.rd.facedetection.nnbfd;

/**
 * A set with a Matrix for input and a vector of an expected result of a Network.
 * 
 * @author Wouter Geraedts (s0814857)
 */
public class LearnSet {

    /**
	 * A Matrix functioning as an input for a Neural Network.
	 */
    public Matrix matrix;

    /**
	 * A vector functioning as an output for a Neural Network.
	 */
    public double[] expectedResult;

    /**
	 * Constructs a set to be learned to a Neural network.
	 * 
	 * @param matrix
	 *            A Matrix functioning as an input for a Neural Network.
	 * @param expectedResult
	 *            A vector functioning as an output for a Neural Network.
	 */
    public LearnSet(Matrix matrix, double[] expectedResult) {
        this.matrix = matrix;
        this.expectedResult = expectedResult;
    }
}

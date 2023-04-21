package it.tac.ct.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mario Stefanutti
 *         <p>
 *         It is used to construct the sequence of coordinates (Begin and End), as in the following example: 1b+, 11b+, 11e+, 8b+, 2b-, 9b+, 8e-, 3b-, 9e+, 6b+, 10b+, 10e+, 7b+, 7e+, 4b-, 5b-, 6e+, 5e+, 4e+, 3e+, 2e+, 1e+
 *         </p>
 */
public class SequenceOfCoordinates implements Cloneable {

    public List<FCoordinate> sequence = new ArrayList<FCoordinate>();

    /**
	 * @param fNumber
	 *            The number of F to be inserted
	 * @param beginInsertPoint
	 *            The begin point of the new F
	 * @param endInsertPoint
	 *            The end point of the new F
	 */
    public void insertF(int fNumber, int beginInsertPoint, int endInsertPoint) {
        sequence.add(beginInsertPoint, new FCoordinate(FCoordinate.TYPE.BEGIN, fNumber, true));
        sequence.add(endInsertPoint, new FCoordinate(FCoordinate.TYPE.END, fNumber, true));
        for (int i = beginInsertPoint + 1; i < endInsertPoint; i++) {
            sequence.get(i).isVisible = false;
        }
    }

    /**
	 * Which face is at the insert point?
	 * 
	 * @param index
	 *            The index where to search (from 1 to (size of the sequence - 1))
	 * @param offset
	 *            0 = face at index -1 = face at index as if the last face wasn't inserted
	 * @return The F number (from 1 to ...) at the given coordinate
	 */
    public int fNumberAtIndex(int index, int offset) {
        int fNumberFound = -1;
        boolean isFFound = false;
        boolean skipCurrentCoordinate = false;
        for (int iF = (sequence.size() / 2) + offset; (iF > 0) && (isFFound == false); iF--) {
            for (int iSequence = index - 1; (iSequence >= 0) && (skipCurrentCoordinate == false) && (isFFound == false); iSequence--) {
                if (sequence.get(iSequence).fNumber == iF) {
                    if (sequence.get(iSequence).type == FCoordinate.TYPE.END) {
                        skipCurrentCoordinate = true;
                    } else {
                        fNumberFound = iF;
                        isFFound = true;
                    }
                }
            }
            skipCurrentCoordinate = false;
        }
        return fNumberFound;
    }

    /**
	 * @return The number of visible Edges (that have a direct access to the ocean)
	 */
    public int numberOfVisibleEdgesAtBorders() {
        int numberOfVisibleEdgesAtBorders = 0;
        for (int iCoordinate = 1; iCoordinate < (sequence.size() - 1); iCoordinate++) {
            if (sequence.get(iCoordinate).isVisible == true) {
                numberOfVisibleEdgesAtBorders++;
            }
        }
        return (numberOfVisibleEdgesAtBorders);
    }

    /**
	 * @return The cloned SequenceOfCoordinates
	 */
    @Override
    public SequenceOfCoordinates clone() throws CloneNotSupportedException {
        SequenceOfCoordinates clonedSequenceOfCoordinates = new SequenceOfCoordinates();
        Iterator<FCoordinate> iter = this.sequence.iterator();
        while (iter.hasNext()) {
            FCoordinate clonedFCoordinate = iter.next().clone();
            clonedSequenceOfCoordinates.sequence.add(clonedFCoordinate);
        }
        return clonedSequenceOfCoordinates;
    }
}

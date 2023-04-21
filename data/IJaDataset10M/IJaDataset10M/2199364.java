package emil.poker.entities;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class OppsPreflopStrength implements Serializable {

    OpponentsPreflopStrengthKey opponentsPreflopStrengthKey;

    @Embeddable
    public static class OpponentsPreflopStrengthKey implements Serializable {

        int numOfUnKnown = 0;

        int numOfLow = 0;

        int numOfMedium = 0;

        int numOfHigh = 0;

        int numOfVeryHigh = 0;

        public int getNumOfHigh() {
            return numOfHigh;
        }

        public void setNumOfHigh(int numOfHigh) {
            this.numOfHigh = numOfHigh;
        }

        public int getNumOfLow() {
            return numOfLow;
        }

        public void setNumOfLow(int numOfLow) {
            this.numOfLow = numOfLow;
        }

        public int getNumOfMedium() {
            return numOfMedium;
        }

        public void setNumOfMedium(int numOfMedium) {
            this.numOfMedium = numOfMedium;
        }

        public int getNumOfUnKnown() {
            return numOfUnKnown;
        }

        public void setNumOfUnKnown(int numOfUnKnown) {
            this.numOfUnKnown = numOfUnKnown;
        }

        public int getNumOfVeryHigh() {
            return numOfVeryHigh;
        }

        public void setNumOfVeryHigh(int numOfVeryHigh) {
            this.numOfVeryHigh = numOfVeryHigh;
        }

        @Override
        public boolean equals(Object arg0) {
            if (arg0 instanceof OpponentsPreflopStrengthKey) {
                OpponentsPreflopStrengthKey opponentsPreflopStrengthKey = (OpponentsPreflopStrengthKey) arg0;
                return numOfHigh == opponentsPreflopStrengthKey.getNumOfHigh() && numOfLow == opponentsPreflopStrengthKey.getNumOfLow() && numOfMedium == opponentsPreflopStrengthKey.getNumOfMedium() && numOfUnKnown == opponentsPreflopStrengthKey.getNumOfUnKnown() && numOfVeryHigh == opponentsPreflopStrengthKey.getNumOfVeryHigh();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return numOfHigh + numOfLow * 10 + numOfMedium * 100 + numOfUnKnown * 1000 + numOfVeryHigh * 10000;
        }

        /**
		 * 
		 * @param numOfUnKnown
		 * @param numOfLow
		 * @param numOfMedium
		 * @param numOfHigh
		 * @param numOfVeryHigh
		 */
        public OpponentsPreflopStrengthKey(int numOfUnKnown, int numOfLow, int numOfMedium, int numOfHigh, int numOfVeryHigh) {
            super();
            this.numOfUnKnown = numOfUnKnown;
            this.numOfLow = numOfLow;
            this.numOfMedium = numOfMedium;
            this.numOfHigh = numOfHigh;
            this.numOfVeryHigh = numOfVeryHigh;
        }
    }

    @Id
    public OpponentsPreflopStrengthKey getOpponentsPreflopStrengthKey() {
        return opponentsPreflopStrengthKey;
    }

    public void setOpponentsPreflopStrengthKey(OpponentsPreflopStrengthKey opponentsPreflopStrengthKey) {
        this.opponentsPreflopStrengthKey = opponentsPreflopStrengthKey;
    }

    public OppsPreflopStrength(OpponentsPreflopStrengthKey opponentsPreflopStrengthKey) {
        super();
        this.opponentsPreflopStrengthKey = opponentsPreflopStrengthKey;
    }
}

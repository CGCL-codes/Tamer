package org.jcvi.fluValidator2.flan.errors;

import org.jcvi.fluValidator2.error.AbstractNonLocalizedError;

public class PossibleVectorContaimination extends AbstractNonLocalizedError {

    public static PossibleVectorContaimination downstream(int numberOfBases) {
        return new PossibleDownstreamVectorContaimination(numberOfBases);
    }

    public static PossibleVectorContaimination upstream(int numberOfBases) {
        return new PossibleUpstreamVectorContaimination(numberOfBases);
    }

    private final int numberOfBases;

    private PossibleVectorContaimination(int numberOfBases, String message) {
        super(message);
        this.numberOfBases = numberOfBases;
    }

    /**
     * @return the numberOfBases
     */
    public int getNumberOfBases() {
        return numberOfBases;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + numberOfBases;
        return result;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof PossibleVectorContaimination)) {
            return false;
        }
        PossibleVectorContaimination other = (PossibleVectorContaimination) obj;
        if (numberOfBases != other.numberOfBases) {
            return false;
        }
        return true;
    }

    static class PossibleUpstreamVectorContaimination extends PossibleVectorContaimination {

        private PossibleUpstreamVectorContaimination(int numberOfBases) {
            super(numberOfBases, String.format("extra %d bases upstream of 5'", numberOfBases));
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            return obj instanceof PossibleUpstreamVectorContaimination;
        }
    }

    static class PossibleDownstreamVectorContaimination extends PossibleVectorContaimination {

        private PossibleDownstreamVectorContaimination(int numberOfBases) {
            super(numberOfBases, String.format("extra %d bases downstream of 3'", numberOfBases));
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            return obj instanceof PossibleDownstreamVectorContaimination;
        }
    }
}

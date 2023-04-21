/*
 * TournamentSelection.java
 *
 * Copyright 2010-2012 Lee Christie.
 *
 * This file is part of EZvolve.
 *
 * EZvolve is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * EZvolve is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with EZvolve.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.ezvolve.algorithms.operators.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.ezvolve.CandidatePool;
import org.ezvolve.EvaluatedCandidate;
import org.ezvolve.FitnessComparator;
import org.ezvolve.algorithms.operators.SelectionOperator;

/**
 * <p>A selection operator which selects the best 1 candidate from a
 * randomly-chosen set of N candidates (with replacement).</p>
 *
 * @author Lee Christie
 */
public final class TournamentSelection
        implements SelectionOperator {

    private final int size;

    /**
     * Returns the number of individuals in a tournament.
     *
     * @return the number of individuals in a tournament, {@code >= 2}
     */
    public int size() {
        return size;
    }

    /**
     * Creates a new instance of {@code TournamentSelection}.
     *
     * @param size the number of individuals per tournament, {@code >= 2}
     * @throws IllegalArgumentException if {@code size < 2}
     */
    public TournamentSelection(int size) {
        if (size < 2) {
            throw new IllegalArgumentException(
                    "size = " + size + ", expected: >= 2");
        }
        this.size = size;
    }    
    
    @Override
    public <C> Iterator<EvaluatedCandidate<C>> select(
            final int requiredSampleSize,
            final FitnessComparator comparator, 
            final CandidatePool<C> candidates, 
            final int iterationNumber, 
            final Random random) {

        if (comparator == null) {
            throw new NullPointerException("comparator");
        }
        if (candidates == null) {
            throw new NullPointerException("candidates");
        }
        if (random == null) {
            throw new NullPointerException("random");
        }
        if (requiredSampleSize < 0) {
            throw new IllegalArgumentException("requiredSampleSize = "
                    + requiredSampleSize + ", expected >= 0");
        }
        if (random == null) {
            throw new NullPointerException("random");
        }
        if (iterationNumber < 0) {
            throw new IllegalArgumentException("iterationNumber = "
                    + iterationNumber + ", iterationNumber >= 0");
        }
        
        return new Iterator<EvaluatedCandidate<C>>() {

            private final AtomicInteger cursor = new AtomicInteger(0);
            
            @Override
            public boolean hasNext() {
                return cursor.get() < requiredSampleSize;
            }

            @Override
            public EvaluatedCandidate<C> next() {
                
                // Gets the next index (uses non-blocking concurrency)
                int index;
                do {
                    index = cursor.get();
                    if (index >= requiredSampleSize) {
                        throw new NoSuchElementException();
                    }
                } while (!cursor.compareAndSet(index, index + 1));

                // Chooses tournament participants
                List<EvaluatedCandidate<C>> tournament = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    tournament.add(candidates.get(
                            random.nextInt(candidates.size())));
                }

                // Returns the best
                return comparator.bestOf(tournament);
                
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
            
        };
        
    }
    
}

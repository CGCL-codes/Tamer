package org.flightreservationsclient.main.utils;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Facundo Manuel Quiroga
 * Jan 26, 2009
 * 
 */
public class RandomChooser<T> {

    T[] objects;

    int[] probabilities;

    public RandomChooser(T[] objects, int[] probabilities) {
        if (objects.length != probabilities.length) {
            throw new IllegalArgumentException("Array of objects to be returned and array of probabilities don't have the same lenght, " + objects.length + " - " + probabilities.length);
        }
        if ((objects.length == 0) || (probabilities.length == 0)) {
            throw new IllegalArgumentException("Both arrays need to contain elements");
        }
        int sumOfProbabilities = this.sumOfProbabilities(probabilities);
        if (sumOfProbabilities != 100) {
            throw new IllegalArgumentException("The sum of the individual probabilities must equal 100");
        }
        if (this.hasNegativeNumbers(probabilities)) {
            throw new IllegalArgumentException("Probabilities should be >0 and <=100");
        }
        this.objects = Arrays.copyOf(objects, objects.length);
        this.probabilities = Arrays.copyOf(probabilities, probabilities.length);
    }

    /**
		 * @param probabilities2
		 * @return
		 */
    private boolean hasNegativeNumbers(int[] probabilities) {
        for (int probability : probabilities) {
            if (probability < 0) {
                return true;
            }
        }
        return false;
    }

    protected int sumOfProbabilities(int[] probabilities) {
        int sumOfProbabilities = 0;
        for (int probability : probabilities) {
            sumOfProbabilities += probability;
        }
        return sumOfProbabilities;
    }

    public T getObject() {
        int randomNumber = new Random().nextInt(100) + 1;
        int sumOfProbabilities = 0;
        int objectIndex = 0;
        for (int i = 0; sumOfProbabilities < randomNumber; i++) {
            assert (i < this.probabilities.length);
            sumOfProbabilities += this.probabilities[i];
            objectIndex = i;
        }
        return this.objects[objectIndex];
    }
}

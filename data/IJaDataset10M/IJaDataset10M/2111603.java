package org.mathematux.core.model;

import java.util.Random;

/**
 * Generator is a class that provides functions for generatating specific values.   
 * 
 * @author Markus Kohlhase
 */
public class Generator {

    private Random generator = new Random();

    /**
	 * Returns an random integer value within the range between min and max 
	 * 
	 * @param min	the minimum value
	 * @param max	the maximum value
	 * @return 		a value between min and max
	 */
    public int getRandomInt(int min, int max) {
        if (min >= 0 && max >= min) {
            if ((max - min) <= 0) {
                if (max == min) {
                    return min;
                } else {
                    return 0;
                }
            } else {
                return generator.nextInt(max - min + 1) + min;
            }
        } else {
            return 0;
        }
    }
}

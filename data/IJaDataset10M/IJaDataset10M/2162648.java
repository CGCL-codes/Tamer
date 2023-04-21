package org.jazzteam.AnArrayOfDifferentFigure;

public class AnArrayOfDifferentFigures {

    public int count() {
        int array[] = { 1, 2, 3, 1, 2 };
        int number = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (array[i] == array[j] && i != j) break; else if (j == array.length - 1) number++;
            }
        }
        return number;
    }
}

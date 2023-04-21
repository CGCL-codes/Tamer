package com.algorithmdb.algorithms.sorting;

/**
 * @author Arunan R
 * @date   20 Nov, 2010
 * 
 * Algorithm Definition:
 * Quicksort is a sorting Algorithm which will divide an array of elements into two 
 * by choosing an element called pivot; adds the elements less than pivot in one list 
 * and adds the elements greater than pivot on another list;recursively sort the sublist
 * contains the lesser elements and the sublist contains the greater elements will 
 * finally generates a sorted list. 
 *
 *
 * Performance Review :
 * This implementation has tested with the following datas
 *    Input                                       |    Average Time taken in Milliseconds |  Average Time taken in Nanoseconds
 *    -----------------------------------------------------------------------------------------------------------------------
 *    Sequence numbers from 1 to 10000            |    15 ms                              |  6849474 ns
 *    Reverse sequence numbers from 10000 to 1    |    15 ms                              |  11932801 ns
 *    10000 Random numbers                        |    16 ms                              |  12214122 ns
 *    
 *     
 * Copyright (C) 2010  Arunan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class QuickSort extends Sort {

    @Override
    public int[] sort(int[] input) {
        if (input.length == 0) return input;
        int pivotIndex = input.length / 2;
        int pivotValue = input[pivotIndex];
        int lessThanPivotLength = 0;
        int greaterThanPivotLength = 0;
        for (int i = 0; i < input.length; i++) {
            if (i != pivotIndex) {
                if (input[i] < pivotValue) {
                    lessThanPivotLength++;
                } else if (input[i] >= pivotValue) {
                    greaterThanPivotLength++;
                }
            }
        }
        int[] lessThanPivot = new int[lessThanPivotLength];
        int[] greaterThanPivot = new int[greaterThanPivotLength];
        int j = 0, k = 0;
        for (int i = 0; i < input.length; i++) {
            if (i != pivotIndex) {
                if (input[i] < pivotValue) {
                    lessThanPivot[j++] = input[i];
                } else if (input[i] >= pivotValue) {
                    greaterThanPivot[k++] = input[i];
                }
            }
        }
        return concatenateArrays(sort(lessThanPivot), sort(greaterThanPivot), pivotValue);
    }

    @Override
    public String[] sort(String[] input) {
        if (input.length == 0) return input;
        int pivotIndex = input.length / 2;
        String pivotValue = input[pivotIndex];
        input[pivotIndex] = null;
        String[] lessThanPivot = new String[input.length + 1];
        String[] greaterThanPivot = new String[input.length + 1];
        int lessThanPivotIncrement = 0;
        int greaterThanPivotIncrement = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] != null) {
                int result = input[i].toString().compareToIgnoreCase(pivotValue.toString());
                if (result < 0) {
                    lessThanPivot[lessThanPivotIncrement++] = input[i];
                } else if (result >= 0) {
                    greaterThanPivot[greaterThanPivotIncrement++] = input[i];
                }
            }
        }
        lessThanPivot = removeNullValuesInArray(lessThanPivot);
        greaterThanPivot = removeNullValuesInArray(greaterThanPivot);
        return concatenateArrays(sort(lessThanPivot), sort(greaterThanPivot), pivotValue);
    }

    private int[] concatenateArrays(int[] arrayOne, int[] arrayTwo, int pivotValue) {
        int i = 0;
        int resultArrayLength = arrayOne.length + arrayTwo.length + 1;
        int[] result = new int[resultArrayLength];
        for (int temp : arrayOne) {
            result[i++] = temp;
        }
        result[i++] = pivotValue;
        for (int temp : arrayTwo) {
            result[i++] = temp;
        }
        return result;
    }

    private String[] concatenateArrays(String[] arrayOne, String[] arrayTwo, String pivotValue) {
        int i = 0;
        int resultArrayLength = arrayOne.length + arrayTwo.length + 1;
        String[] result = new String[resultArrayLength];
        for (String temp : arrayOne) {
            if (temp != null && temp != "") result[i++] = temp;
        }
        result[i++] = pivotValue;
        for (String temp : arrayTwo) {
            if (temp != null && temp != "") result[i++] = temp;
        }
        return result;
    }
}

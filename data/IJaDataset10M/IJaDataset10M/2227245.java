package org.reprap.scanning.DataStructures;

import org.reprap.scanning.Geometry.Point2d;
import org.reprap.scanning.Geometry.PointPair2D;

/******************************************************************************
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * The license can be found on the WWW at: http://www.fsf.org/copyleft/gpl.html
 * 
 * Or by writing to: Free Software Foundation, Inc., 59 Temple Place - Suite
 * 330, Boston, MA 02111-1307, USA.
 *  
 * 
 * If you make changes you think others would like, please contact one of the
 * authors or someone at the reprap.org web site.
 * 
 * 				Author list
 * 				===========
 * 
 * Reece Arnott	reece.arnott@gmail.com
 * 
 * Last modified by Reece Arnott 16th October 2009
 *  Used by the LensDistortion class for calculation of the distortion matrix.
 *
 *TODO - Add in provision for non-square pixels??
 *
 ******************************************************************************/
public class SignedRadiiPairs {

    public double[] radiione;

    public double[] radiitwo;

    public boolean[] positive;

    public int[] indexarray;

    public SignedRadiiPairs(PointPair2D[] pointpairs, Point2d centerforone, Point2d centerfortwo) {
        radiione = new double[pointpairs.length];
        radiitwo = new double[pointpairs.length];
        positive = new boolean[pointpairs.length];
        indexarray = new int[pointpairs.length];
        for (int i = 0; i < pointpairs.length; i++) {
            radiione[i] = Math.sqrt(pointpairs[i].pointone.CalculateDistanceSquared(centerforone));
            radiitwo[i] = Math.sqrt(pointpairs[i].pointtwo.CalculateDistanceSquared(centerfortwo));
            positive[i] = (pointpairs[i].pointone.dot(pointpairs[i].pointtwo) > 0);
            indexarray[i] = i;
        }
    }

    public int[] SortIndexes(boolean unsigned, boolean useradiione) {
        if (indexarray.length > 1) QuickSort(0, indexarray.length - 1, unsigned, useradiione);
        return indexarray;
    }

    public double[] SortAndReturn(boolean unsignedsort, boolean useradiionetosort, boolean unsignedreturn, boolean returnradiione) {
        indexarray = SortIndexes(unsignedsort, useradiionetosort);
        double[] returnvalue = new double[indexarray.length];
        for (int i = 0; i < indexarray.length; i++) {
            if (returnradiione) returnvalue[i] = radiione[indexarray[i]]; else returnvalue[i] = radiitwo[indexarray[i]];
            if ((!unsignedreturn) && (!positive[indexarray[i]])) returnvalue[i] = 0 - returnvalue[i];
        }
        return returnvalue;
    }

    private void QuickSort(int left, int right, boolean unsigned, boolean useradiione) {
        int index = partition(left, right, unsigned, useradiione);
        if (left < index - 1) QuickSort(left, index - 1, unsigned, useradiione);
        if (index < right) QuickSort(index, right, unsigned, useradiione);
    }

    private int partition(int left, int right, boolean unsigned, boolean useradiione) {
        int i = left, j = right;
        int tmp;
        double pivot = getValue(unsigned, useradiione, indexarray[(left + right) / 2]);
        while (i <= j) {
            while (getValue(unsigned, useradiione, indexarray[i]) < pivot) i++;
            while (getValue(unsigned, useradiione, indexarray[j]) > pivot) j--;
            if (i <= j) {
                tmp = indexarray[i];
                indexarray[i] = indexarray[j];
                indexarray[j] = tmp;
                i++;
                j--;
            }
        }
        ;
        return i;
    }

    public double getValue(boolean unsigned, boolean useradiione, int index) {
        double value;
        if (useradiione) value = radiione[index]; else value = radiitwo[index];
        if ((!unsigned) && (!positive[index])) value = 0 - value;
        return value;
    }
}

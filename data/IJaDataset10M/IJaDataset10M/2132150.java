package javax.media.ding3d.utils.geometry;

import java.io.*;
import java.util.*;
import javax.media.ding3d.vecmath.*;

class Clean {

    static void initPUnsorted(Triangulator triRef, int number) {
        if (number > triRef.maxNumPUnsorted) {
            triRef.maxNumPUnsorted = number;
            triRef.pUnsorted = new Point2f[triRef.maxNumPUnsorted];
            for (int i = 0; i < triRef.maxNumPUnsorted; i++) triRef.pUnsorted[i] = new Point2f();
        }
    }

    static int cleanPolyhedralFace(Triangulator triRef, int i1, int i2) {
        int removed;
        int i, j, numSorted, index;
        int ind1, ind2;
        initPUnsorted(triRef, triRef.numPoints);
        for (i = 0; i < triRef.numPoints; ++i) triRef.pUnsorted[i].set(triRef.points[i]);
        sort(triRef.points, triRef.numPoints);
        i = 0;
        for (j = 1; j < triRef.numPoints; ++j) {
            if (pComp(triRef.points[i], triRef.points[j]) != 0) {
                ++i;
                triRef.points[i] = triRef.points[j];
            }
        }
        numSorted = i + 1;
        removed = triRef.numPoints - numSorted;
        for (i = i1; i < i2; ++i) {
            ind1 = triRef.loops[i];
            ind2 = triRef.fetchNextData(ind1);
            index = triRef.fetchData(ind2);
            while (ind2 != ind1) {
                j = findPInd(triRef.points, numSorted, triRef.pUnsorted[index]);
                triRef.updateIndex(ind2, j);
                ind2 = triRef.fetchNextData(ind2);
                index = triRef.fetchData(ind2);
            }
            j = findPInd(triRef.points, numSorted, triRef.pUnsorted[index]);
            triRef.updateIndex(ind2, j);
        }
        triRef.numPoints = numSorted;
        return removed;
    }

    static void sort(Point2f points[], int numPts) {
        int i, j;
        Point2f swap = new Point2f();
        for (i = 0; i < numPts; i++) {
            for (j = i + 1; j < numPts; j++) {
                if (pComp(points[i], points[j]) > 0) {
                    swap.set(points[i]);
                    points[i].set(points[j]);
                    points[j].set(swap);
                }
            }
        }
    }

    static int findPInd(Point2f sorted[], int numPts, Point2f pnt) {
        int i;
        for (i = 0; i < numPts; i++) {
            if ((pnt.x == sorted[i].x) && (pnt.y == sorted[i].y)) {
                return i;
            }
        }
        return -1;
    }

    static int pComp(Point2f a, Point2f b) {
        if (a.x < b.x) return -1; else if (a.x > b.x) return 1; else {
            if (a.y < b.y) return -1; else if (a.y > b.y) return 1; else return 0;
        }
    }
}

package org.photovault.imginfo;

import java.util.Comparator;
import java.util.Date;

/**
 Comparator that orders the photos based on their quality setting. Ordering is
 done like this: top > good > OK > fair > poor > undefined > unusable
 */
public class QualityComparator implements Comparator {

    private int getComparisonOrder(int quality) {
        int order = 100;
        switch(quality) {
            case PhotoInfo.QUALITY_TOP:
                order = 1;
                break;
            case PhotoInfo.QUALITY_GOOD:
                order = 2;
                break;
            case PhotoInfo.QUALITY_FAIR:
                order = 3;
                break;
            case PhotoInfo.QUALITY_POOR:
                order = 4;
                break;
            case PhotoInfo.QUALITY_UNDEFINED:
                order = 5;
                break;
            case PhotoInfo.QUALITY_UNUSABLE:
                order = 6;
                break;
            default:
                order = 100;
                break;
        }
        return order;
    }

    public int compare(Object o1, Object o2) {
        PhotoInfo p1 = (PhotoInfo) o1;
        PhotoInfo p2 = (PhotoInfo) o2;
        int q1 = getComparisonOrder(p1.getQuality());
        int q2 = getComparisonOrder(p2.getQuality());
        int res = 0;
        if (q1 < q2) {
            res = -1;
        } else if (q1 > q2) {
            res = 1;
        }
        return res;
    }
}

package com.yerihyo.yeritools.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import com.yerihyo.yeritools.collections.CollectionsToolkit;
import com.yerihyo.yeritools.debug.YeriDebug;

public class SegmentationMetricToolkit {

    public static double getPKMetricByEndIndexArray(int[] tokenEndIndexArray, int[] hypothesisSegmentationEndIndexArray, int[] referenceSegmentationEndIndexArray) {
        int tokenEndIndexArrayLastValue = tokenEndIndexArray[tokenEndIndexArray.length - 1];
        int hypothesisSegmentationEndIndexArrayLastValue = hypothesisSegmentationEndIndexArray[hypothesisSegmentationEndIndexArray.length - 1];
        int referenceSegmentationEndIndexArrayLastValue = referenceSegmentationEndIndexArray[referenceSegmentationEndIndexArray.length - 1];
        YeriDebug.ASSERT_compareInteger(tokenEndIndexArrayLastValue, hypothesisSegmentationEndIndexArrayLastValue);
        YeriDebug.ASSERT_compareInteger(tokenEndIndexArrayLastValue, referenceSegmentationEndIndexArrayLastValue);
        int tokenCount = tokenEndIndexArray.length;
        double referenceSegmentationAverageTokenCount = ((double) tokenCount) / referenceSegmentationEndIndexArray.length;
        int k = (int) Math.round(referenceSegmentationAverageTokenCount / 2);
        int wrongPairCount = 0;
        int checkPointCount = tokenCount - k;
        for (int i = 0; i < checkPointCount; i++) {
            int[] checkPointArray = new int[] { tokenEndIndexArray[i], tokenEndIndexArray[i + k] };
            if (CollectionsToolkit.isInSameSegment(hypothesisSegmentationEndIndexArray, checkPointArray) != CollectionsToolkit.isInSameSegment(referenceSegmentationEndIndexArray, checkPointArray)) {
                wrongPairCount++;
            }
        }
        return ((double) wrongPairCount) / checkPointCount;
    }

    public static void main(String args[]) {
        test02();
    }

    private static void test02() {
    }

    protected static void test01() {
        int n = 100;
        int v = 10000;
        Set<Integer> valueSet = new TreeSet<Integer>();
        Random r = new Random();
        for (int i = 0; valueSet.size() < n; i++) {
            valueSet.add(r.nextInt(v));
        }
        int[] valueArray = CollectionsToolkit.toIntArray(valueSet);
        Arrays.sort(valueArray);
        List<Integer> valueList = new ArrayList<Integer>(valueSet);
        Collections.shuffle(valueList);
        int[] refArray = CollectionsToolkit.toIntArray(CollectionsToolkit.copyOf(valueList, 0, 50));
        Arrays.sort(refArray);
        int[] userArray = CollectionsToolkit.toIntArray(CollectionsToolkit.copyOf(valueList, 30, 50));
        Arrays.sort(userArray);
        System.out.println("valueArray: " + CollectionsToolkit.toString(valueArray));
        System.out.println("refArray: " + CollectionsToolkit.toString(refArray));
        System.out.println("userArray: " + CollectionsToolkit.toString(userArray));
        System.out.println("PK: " + getPKMetricByEndIndexArray(valueArray, refArray, userArray));
    }
}

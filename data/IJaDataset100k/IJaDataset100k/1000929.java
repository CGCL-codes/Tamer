package collections.wildCards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Sergiy Doroshenko
 * Date: Sep 15, 2010
 * Time: 1:52:58 PM
 */
public class RestrictionsOfWildCards {

    public static void main(String[] args) {
        List<Number> nums = new ArrayList<Number>();
        List<? super Number> sink = nums;
        List<? extends Number> source = nums;
        for (int i = 0; i < 10; i++) sink.add(i);
        double sum = 0;
        List<List<? super Number>> numslist = new ArrayList<List<? super Number>>();
        numslist.add(Arrays.asList(2.78, "", new Object()));
        System.out.println(numslist);
    }
}

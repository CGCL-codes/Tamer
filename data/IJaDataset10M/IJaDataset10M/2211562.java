package edu.teacmore.simplex;

import java.util.*;
import edu.webteach.practice.data.*;

/**
 * User: Andrey Kuzmenko
 * Date: 06.05.2007
 * Time: 21:32:22
 */
public class EmptyResult implements ICalculator {

    public int calc(Map in, Map results) {
        System.out.append("EmptyResult.invisible");
        int count = ((Integer) in.get("countIn")).intValue();
        if (count == 1) results.put("result", new Integer(6)); else results.put("result", new Integer(2));
        return 0;
    }
}

package edu.teacmore;

import java.util.*;
import edu.webteach.practice.data.*;

/**
 * @author Igor Shubovych
 *
 * ������������
 *
 * ����� ���������
 * source	�������
 *
 * ������ ���������
 * target	����
 */
public class Assign implements ICalculator {

    /**
     * 
     * @see edu.webteach.practice.data.ICalculator#calc(java.util.Map, java.util.Map, java.util.Map)
     */
    public int calc(Map input, Map results) {
        results.put("target", input.get("source"));
        return 0;
    }
}

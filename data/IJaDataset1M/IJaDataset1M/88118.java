package org.datamining.guha.data.provider;

import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import org.datamining.guha.model.literal.Literal;

/**
 * This class is used to generate rows of specified length
 * having random order of positive and negative Literals.
 * Each Literal occurs just once in the row in
 * ether positive or negative form.
 * 
 * @author Lukas Vlcek
 */
public class RandomDataProvider extends AbstractDataProvider implements DataProvider {

    public RandomDataProvider(int rowLength) {
        super(rowLength);
    }

    public List<Literal> nextData() {
        List<Literal> list = new ArrayList<Literal>(rowLength);
        List<Integer> tmpList = new ArrayList<Integer>(rowLength);
        for (int i = 0; i < rowLength; i++) {
            tmpList.add(i, new Integer(i));
        }
        int size = tmpList.size();
        for (int i = 0; i < size; i++) {
            Float pos = new Float(Math.random() * (tmpList.size() - 1));
            int position = Math.round(pos.floatValue());
            Integer ref = tmpList.get(position);
            if (Math.random() < 0.5) {
                list.add(positiveLiterals.get(ref.intValue()));
            } else {
                list.add(negativeLiterals.get(ref.intValue()));
            }
            tmpList.remove(position);
        }
        return list;
    }
}

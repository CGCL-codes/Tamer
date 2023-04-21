package net.sourceforge.align.model.length;

import java.util.List;

public class LengthModelFactory {

    private static LengthModelFactory instance = new LengthModelFactory();

    /**
	 * @return Zwraca insatncje singletona.
	 */
    public static LengthModelFactory getInstance() {
        return instance;
    }

    private LengthModelFactory() {
    }

    public LengthModel train(List<Integer> segmentLengthList) {
        MutableLengthModel model = new MutableLengthModel();
        for (int segmentLength : segmentLengthList) {
            model.addLengthOccurence(segmentLength);
        }
        model.normalize();
        return model;
    }
}

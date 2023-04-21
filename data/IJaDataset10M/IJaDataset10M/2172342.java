package org.netbeams.dsp.test.randomnumber;

import java.util.ArrayList;
import org.netbeams.dsp.message.MessageContent;

public class RandomNumbers extends MessageContent {

    private ArrayList<RandomNumber> rNumbers;

    public RandomNumbers(ArrayList<RandomNumber> rNumbers) {
        this.rNumbers = rNumbers;
    }

    ;

    public ArrayList<RandomNumber> getRandomNumbers() {
        return rNumbers;
    }

    public String toXml() {
        return null;
    }
}

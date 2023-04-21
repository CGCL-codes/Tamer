package uk.ac.bath.ai.io;

import uk.ac.bath.ai.util.Data;
import uk.ac.bath.ai.util.Util;
import uk.ac.bath.ai.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author pjl
 */
class RandomishInvertingImageSource extends RandomInvertingImageSource {

    private int mykey;

    public RandomishInvertingImageSource(String string, int i) throws FileNotFoundException, IOException {
        super(string);
        mykey = i;
    }

    @Override
    public Data getData(int index) {
        Data data = source.getData(index);
        int key = Util.toLabel(data.label);
        if (key != mykey && rand.nextBoolean()) {
            for (int i = 0; i < data.image.length; i++) {
                data.image[i] = 1.0f - data.image[i];
            }
        }
        return data;
    }
}

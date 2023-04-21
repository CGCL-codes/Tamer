package ch.byteality.copperhead.example;

import java.util.Random;

/**
 * The Class ExampleUtil.
 */
public class ExampleUtil {

    /**
	 * Gets the random text.
	 * 
	 * @return the random text
	 */
    public static String getRandomText() {
        Random r = new Random();
        return Long.toString(Math.abs(r.nextLong()), 36);
    }
}

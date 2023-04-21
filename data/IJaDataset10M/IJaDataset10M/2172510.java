package org.jazzteam.edu.algo.numberConverterHor1zont.parserForNumber;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Parameterized test for class ParserForNumber, check values after running method parseForNumber
 * 
 * @author Hor1zonT
 * @version $Rev: $
 */
@RunWith(value = Parameterized.class)
public class ParserForNumberTest {

    private final Long number;

    private static Long arrayOfNumbers[] = new Long[20];

    private final List<Long> correctNumber;

    private static List<Long> arrayOfCorrectNumbers[] = new ArrayList[20];

    private static ParserForNumber parserForNumber = new ParserForNumber();

    public ParserForNumberTest(Long number, List<Long> correctNumber) {
        this.number = number;
        this.correctNumber = correctNumber;
    }

    @Parameters
    public static Collection<Object[]> data() {
        for (int i = 0; i < arrayOfCorrectNumbers.length; i++) {
            arrayOfCorrectNumbers[i] = new ArrayList<Long>();
        }
        int index;
        index = 0;
        arrayOfCorrectNumbers[index].add(0L);
        arrayOfNumbers[index] = 0L;
        index = 1;
        arrayOfCorrectNumbers[index].add(5L);
        arrayOfNumbers[index] = 5L;
        index = 2;
        arrayOfCorrectNumbers[index].add(10L);
        arrayOfNumbers[index] = 10L;
        index = 3;
        arrayOfCorrectNumbers[index].add(11L);
        arrayOfNumbers[index] = 11L;
        index = 4;
        arrayOfCorrectNumbers[index].add(20L);
        arrayOfNumbers[index] = 20L;
        index = 5;
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfCorrectNumbers[index].add(20L);
        arrayOfNumbers[index] = 21L;
        index = 6;
        arrayOfCorrectNumbers[index].add(4L);
        arrayOfCorrectNumbers[index].add(60L);
        arrayOfNumbers[index] = 64L;
        index = 7;
        arrayOfCorrectNumbers[index].add(80L);
        arrayOfNumbers[index] = 80L;
        index = 8;
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfNumbers[index] = 100L;
        index = 9;
        arrayOfCorrectNumbers[index].add(3L);
        arrayOfCorrectNumbers[index].add(20L);
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(3L);
        arrayOfNumbers[index] = 323L;
        index = 10;
        arrayOfCorrectNumbers[index].add(5L);
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(3L);
        arrayOfNumbers[index] = 305L;
        index = 11;
        arrayOfCorrectNumbers[index].add(15L);
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(6L);
        arrayOfNumbers[index] = 615L;
        index = 12;
        arrayOfCorrectNumbers[index].add(11L);
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfNumbers[index] = 111L;
        index = 13;
        arrayOfCorrectNumbers[index].add(11L);
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfCorrectNumbers[index].add(1000L);
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfNumbers[index] = 1111L;
        index = 14;
        arrayOfCorrectNumbers[index].add(1000L);
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfNumbers[index] = 1000L;
        index = 15;
        arrayOfCorrectNumbers[index].add(1000000L);
        arrayOfCorrectNumbers[index].add(10L);
        arrayOfNumbers[index] = 10000000L;
        index = 16;
        arrayOfCorrectNumbers[index].add(9L);
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(9L);
        arrayOfCorrectNumbers[index].add(1000L);
        arrayOfCorrectNumbers[index].add(2L);
        arrayOfNumbers[index] = 2909L;
        index = 17;
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfNumbers[index] = 1L;
        index = 18;
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(5L);
        arrayOfCorrectNumbers[index].add(1000000L);
        arrayOfCorrectNumbers[index].add(19L);
        arrayOfCorrectNumbers[index].add(100L);
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfCorrectNumbers[index].add(1000000000000L);
        arrayOfCorrectNumbers[index].add(5L);
        arrayOfNumbers[index] = 5000119000500L;
        index = 19;
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfCorrectNumbers[index].add(1000000000000L);
        arrayOfCorrectNumbers[index].add(1L);
        arrayOfNumbers[index] = 1000000000001L;
        Object[][] data = new Object[][] { { arrayOfNumbers[0], arrayOfCorrectNumbers[0] }, { arrayOfNumbers[1], arrayOfCorrectNumbers[1] }, { arrayOfNumbers[2], arrayOfCorrectNumbers[2] }, { arrayOfNumbers[3], arrayOfCorrectNumbers[3] }, { arrayOfNumbers[4], arrayOfCorrectNumbers[4] }, { arrayOfNumbers[5], arrayOfCorrectNumbers[5] }, { arrayOfNumbers[6], arrayOfCorrectNumbers[6] }, { arrayOfNumbers[7], arrayOfCorrectNumbers[7] }, { arrayOfNumbers[8], arrayOfCorrectNumbers[8] }, { arrayOfNumbers[9], arrayOfCorrectNumbers[9] }, { arrayOfNumbers[10], arrayOfCorrectNumbers[10] }, { arrayOfNumbers[11], arrayOfCorrectNumbers[11] }, { arrayOfNumbers[12], arrayOfCorrectNumbers[12] }, { arrayOfNumbers[13], arrayOfCorrectNumbers[13] }, { arrayOfNumbers[14], arrayOfCorrectNumbers[14] }, { arrayOfNumbers[15], arrayOfCorrectNumbers[15] }, { arrayOfNumbers[16], arrayOfCorrectNumbers[16] }, { arrayOfNumbers[17], arrayOfCorrectNumbers[17] }, { arrayOfNumbers[18], arrayOfCorrectNumbers[18] }, { arrayOfNumbers[19], arrayOfCorrectNumbers[19] } };
        return Arrays.asList(data);
    }

    @Test
    public void test() {
        assertTrue(listsAreEquals(correctNumber, parserForNumber.divideNumber(number)));
    }

    /**
	 * Method for equals lists with long value
	 */
    boolean listsAreEquals(List<Long> testListFirst, List<Long> testListSecond) {
        for (int i = 0; i < testListFirst.size(); i++) {
            if (!(testListFirst.get(i).equals(testListSecond.get(i)))) {
                return false;
            }
        }
        return true;
    }
}

package pl.edu.pjwstk.types;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Robert Strzelecki rstrzele@gmail.com
 * package pl.edu.pjwstk.types
 */
public class ExtendedBitSetTest {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExtendedBitSetTest.class);

    private static void configureLogger() {
        Calendar calendar = new GregorianCalendar();
        System.setProperty("log4j.year", "" + calendar.get(Calendar.YEAR));
        System.setProperty("log4j.month", "" + (calendar.get(Calendar.MONTH) + 1));
        System.setProperty("log4j.day", "" + calendar.get(Calendar.DAY_OF_MONTH));
        System.setProperty("log4j.hour", "" + calendar.get(Calendar.HOUR_OF_DAY));
        System.setProperty("log4j.minute", "" + calendar.get(Calendar.MINUTE));
        System.setProperty("log4j.second", "" + calendar.get(Calendar.SECOND));
        PropertyConfigurator.configure("log4j.properties");
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        configureLogger();
        ExtendedBitSet ebs = new ExtendedBitSet(4);
        logger.debug(ebs.length() + " " + ebs.toString());
        logger.debug(ebs.size() + " " + ebs.toStringBytes());
        ebs.set(0);
        logger.debug(ebs.length() + " " + ebs.toString());
        ebs.set(2);
        logger.debug(ebs.length() + " " + ebs.toString());
        ExtendedBitSet ebs1 = new ExtendedBitSet("1010", 8, false, false);
        logger.debug(ebs1.toString());
        if (ebs1.equals(ebs)) {
            logger.debug("equal - OK");
        } else {
            logger.error("not equal - something wrong");
        }
        ExtendedBitSet ebs2 = ExtendedBitSet.add(ebs1, ebs);
        if (ebs2.equals(new ExtendedBitSet("10101010"))) {
            logger.debug("equal - OK");
        } else {
            logger.error("not equal - something wrong ebs2(" + ebs2.toString() + ")");
        }
        ebs1.add(ebs);
        if (ebs2.equals(ebs1)) {
            logger.debug("equal - OK");
        } else {
            logger.error("not equal - something wrong");
            logger.error("ebs1(" + ebs2.toString() + ")");
            logger.error("ebs2(" + ebs2.toString() + ")");
        }
        for (int i = 0; i < 50; i++) {
            ExtendedBitSet ebsint1 = new ExtendedBitSet(Integer.toBinaryString(i));
            if (ebsint1.toInt() == i) {
                logger.debug("for variable i = " + i + " equal - OK");
            } else {
                logger.error("not equal - something wrong");
                logger.error("ebsint1(" + ebsint1.toString() + ") = " + ebsint1.toInt());
                logger.error("int2str(" + Integer.toBinaryString(i) + ") = " + i);
            }
        }
        byte[] liczby = new byte[8];
        liczby[0] = 1;
        for (int i = 1; i < 8; i++) {
            liczby[i] = (byte) (2 * (int) liczby[i - 1]);
        }
        ExtendedBitSet ebslib = new ExtendedBitSet(64);
        ebslib.set(0, liczby);
        logger.debug(ebslib.toStringBytes());
        byte[] liczby2 = new byte[8];
        liczby2[0] = 1;
        for (int i = 1; i < 8; i++) {
            liczby2[i + 1] = (byte) ((2 * (int) liczby[i - 1]) + (int) liczby2[i - 1]);
            logger.debug(liczby2[i]);
        }
        ExtendedBitSet ebslib2 = new ExtendedBitSet(64);
        ebslib2.set(0, liczby2);
        logger.debug(ebslib2.toStringBytes());
    }
}

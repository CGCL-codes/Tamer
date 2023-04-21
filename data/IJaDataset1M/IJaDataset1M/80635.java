package cham.open.pattern.adapter;

import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chaminda Amarasinghe <chaminda.sl@gmail.com>
 */
public class ComputerTest {

    private final Logger logger = Logger.getLogger(ComputerTest.class.getName());

    @Test
    public void testHttp() {
        String s = "Fantastic%20Sams%20Hair%20Salon";
        logger.info("before process " + s);
        String[] codes = s.split("%");
        StringBuilder ret = new StringBuilder("");
        int i = 0;
        for (String s1 : codes) {
            if (i == 0) {
                i++;
                ret.append(s1);
            } else {
                try {
                    logger.info(s1);
                    int n = Integer.parseInt(s1.substring(0, 2), 16);
                    n &= 0xFF;
                    char ch = (char) n;
                    String s2 = "" + ch + s1.substring(2, s1.length());
                    ret.append(s2);
                    logger.info(s2);
                } catch (Exception ex) {
                    ret.append(s1);
                    logger.info("Error-----------");
                }
            }
        }
        logger.info("after process " + ret.toString());
        assertEquals("Fantastic Sams Hair Salon", ret.toString());
    }
}

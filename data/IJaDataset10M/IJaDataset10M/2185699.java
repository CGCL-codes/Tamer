package com.idocbox.common.math;

import java.util.Random;
import com.idocbox.common.log.IdocboxLog;
import com.idocbox.common.log.impl.IdocboxLogFactory;

/**
 * Code util, used to generate code.
 * @author Chunhui Li
 *
 */
public class CodeUtil {

    /**
	 * logger of the class
	 */
    private static final IdocboxLog logger = IdocboxLogFactory.getLog(CodeUtil.class);

    /**
	 * generate a random code whose length is given.
	 * @param len length of code.
	 * @return random code.
	 */
    public static String genericCode(final int len) {
        String radStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuffer generateRandStr = new StringBuffer();
        Random rand = new Random();
        int length = len;
        for (int i = 0; i < length; i++) {
            int randNum = rand.nextInt(36);
            generateRandStr.append(radStr.substring(randNum, randNum + 1));
        }
        return generateRandStr.toString();
    }
}

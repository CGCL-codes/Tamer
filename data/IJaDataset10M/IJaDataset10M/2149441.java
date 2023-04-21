package chaski.utils;

import java.util.Arrays;

/**
 * if a < 92 and a>=0 then represented as a+33 (range 33 ~ 124)  [ convert back: a-33 ]
 * else if a >= 92 and a <= 127 then represented as 125(b1) a-92+33=a-59 (b2), b2 range (33~68) [convert back a+59]
 * else if a <0 and a>=-58 then represent as 125(b1)  a+127(b2) b2 range(69~126) (69==-58),(126=-1) [convert back a-127]
 * else if a<-58 then represent as 126(b1) a+128+33=a+161(b2) b2 range(33 ~ 102) [convert back a-161]
 * @author qing
 *
 */
public class CompactB2SCodec extends BinaryToStringCodec {

    public CompactB2SCodec(boolean compress) {
        super(compress);
    }

    /**
	 * Decode the string, for every byte, if it is < 92, then 
	 * return  
	 * @param rep
	 * @return
	 */
    public byte[] decodeString(String rep) {
        byte[] ret = new byte[rep.length()];
        char last = (char) 0;
        int ttlen = 0;
        for (int i = 0; i < rep.length(); i++) {
            char curr = rep.charAt(i);
            if (last == 0 && (curr == b125 || curr == b126)) {
                last = curr;
                continue;
            } else {
                if (last == b125) {
                    if (curr < 69) {
                        ret[ttlen++] = (byte) (curr + 59);
                    } else {
                        ret[ttlen++] = (byte) (curr - 127);
                    }
                } else if (last == b126) {
                    ret[ttlen++] = (byte) (curr - 161);
                } else {
                    ret[ttlen++] = (byte) (curr - 33);
                }
                last = (char) 0;
            }
        }
        return Arrays.copyOf(ret, ttlen);
    }

    static final char b125 = (char) 125;

    static final char b126 = (char) 126;

    public String encodeString(byte[] r) {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < r.length; i++) {
            byte b = r[i];
            if (b < -58) {
                bf.append(b126);
                bf.append((char) (b + 161));
            } else if (b < 0) {
                bf.append(b125);
                bf.append((char) (b + 127));
            } else if (b < 92) {
                bf.append((char) (b + 33));
            } else {
                bf.append(b125);
                bf.append((char) (b - 59));
            }
        }
        return bf.toString();
    }
}

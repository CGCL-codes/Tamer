package net.cimd.packets.parameters;

import net.cimd.packets.InvalidContentException;

/**
 * @author <a href="mailto:dpoleakovski@gmail.com">Dmitri Poleakovski</a>
 * @version $Revision: 1.1 $ $Date: 2007/03/14 14:17:06 $
 */
public class TimestampParamRestriction extends IntegerParamRestriction {

    public void check(CimdParameter param) throws InvalidContentException {
        String val = param.getValue();
        if (val.length() != 12) {
            throw new InvalidContentException("Invalid timestamp format: " + val);
        }
        super.check(param);
        checkFormat(val, 2, 1, 12);
        checkFormat(val, 4, 1, 31);
        checkFormat(val, 6, 0, 24);
        checkFormat(val, 8, 0, 59);
        checkFormat(val, 10, 0, 59);
    }

    private void checkFormat(String tstmp, int startChar, int min, int max) throws InvalidContentException {
        int val = Character.digit(tstmp.charAt(startChar), 10) * 10 + Character.digit(tstmp.charAt(startChar + 1), 10);
        if (val < min || val > max) {
            throw new InvalidContentException("Invalid timestamp format: " + val);
        }
    }
}

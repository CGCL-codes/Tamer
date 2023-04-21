package se.notima.bg.bgmax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import se.notima.bg.BgParseException;
import se.notima.bg.BgRecord;

/**
 * Presents BgMax record with transaction code 25: information record.
 * 
 * @author Daniel Tamm daniel@notima.se
 * @author Ilya Ismagilov ismagilov@gmail.com
 */
public class BgMaxTk25Record extends BgRecord {

    public static final String TC = "25";

    private static Pattern linePattern = Pattern.compile(TC + "(.{50}) {28}.*");

    private String infoText;

    public BgMaxTk25Record() {
        super(TC);
    }

    @Override
    public BgRecord fromRecordString(String line) throws BgParseException {
        Matcher m = linePattern.matcher(line);
        if (m.matches()) {
            infoText = m.group(1).trim();
            return this;
        } else {
            throw new BgParseException("Information record doesn`t match expected pattern", line);
        }
    }

    @Override
    public String toRecordString() {
        return "";
    }

    public String getInfoText() {
        return infoText;
    }
}

package dsb.bar.flowmeter.monitor.impl;

import java.math.BigDecimal;
import java.util.Scanner;
import javax.inject.Inject;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import dsb.bar.flowmeter.monitor.FlowmeterDataParser;
import dsb.bar.flowmeter.monitor.FlowmeterDataParserException;
import dsb.bar.flowmeter.monitor.FlowmeterMeasurement;

public class FlowmeterDataParserImpl implements FlowmeterDataParser {

    @Inject
    private Logger logger;

    @Override
    public FlowmeterMeasurement parse(String data) {
        final String dataErrorMsg = "data should be a 58-character string";
        Validate.notEmpty(data, dataErrorMsg);
        Validate.isTrue(data.length() == 58, dataErrorMsg);
        int i = 0;
        while (i < data.length()) {
            if (data.charAt(i) == 'A') break;
            i++;
        }
        if (i <= 29) {
            return this.doParsing(data.substring(i, i + 29));
        } else {
            logger.trace("Found 'A' at too high index: " + i);
            return null;
        }
    }

    /**
	 * Do the actual parsing of the interesting 29 characters from the 58
	 * character string.
	 * 
	 * @param data
	 *            The string of 29 characters.
	 * 
	 * @return The {@link FlowmeterMeasurement} instance that contains the
	 *         parsed values.
	 */
    FlowmeterMeasurement doParsing(String data) {
        final String dataErrorMsg = "data should be a 29-character string";
        Validate.notEmpty(data, dataErrorMsg);
        Validate.isTrue(data.length() == 29, dataErrorMsg);
        Scanner sc = new Scanner(data);
        if (!sc.next().equals("A")) {
            logger.trace("Did not find leading 'A' marker.");
            throw new FlowmeterDataParserException(data);
        }
        BigDecimal valA = null;
        if (sc.hasNextBigDecimal()) {
            valA = sc.nextBigDecimal();
        } else {
            logger.trace("Found no value parseable as BigDecimal for value A.");
            throw new FlowmeterDataParserException(data);
        }
        if (!sc.next().equals("B")) {
            logger.trace("Did not find 'B' marker.");
            throw new FlowmeterDataParserException(data);
        }
        BigDecimal valB = null;
        if (sc.hasNextBigDecimal()) {
            valB = sc.nextBigDecimal();
        } else {
            logger.trace("Found no value parseable as BigDecimal for value B.");
            throw new FlowmeterDataParserException(data);
        }
        final BigDecimal thousand = new BigDecimal(1000);
        final long valueA = valA.multiply(thousand).longValue();
        final long valueB = valB.multiply(thousand).longValue();
        FlowmeterMeasurement result = new FlowmeterMeasurement(valueA, valueB);
        return result;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        return builder.toString();
    }
}

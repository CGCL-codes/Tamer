package net.sf.myway.gps.garmin.mapping.converters;

import java.text.ParsePosition;
import net.sf.myway.gps.garmin.unit.GarminData;

/**
 * @author Andreas Beckers
 * @version $Revision: 1.1 $
 */
public class Uint16Converter implements Converter<Integer> {

    /**
	 * @see net.sf.myway.gps.garmin.mapping.converters.Converter#convert(byte[], int, java.lang.Object, int)
	 */
    @Override
    public int convert(final byte[] buffer, final int pos, final Integer data, final int length) {
        return 0;
    }

    /**
	 * @see net.sf.myway.gps.garmin.mapping.converters.Converter#convert(net.sf.myway.gps.garmin.unit.GarminData,
	 *      int, int, java.text.ParsePosition)
	 */
    @Override
    public Integer convert(final GarminData data, final int pos, final int length, final ParsePosition ppos) {
        return data.getWord(pos);
    }
}

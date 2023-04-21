package net.sf.myway.gps.garmin.mapping.converters;

import java.text.ParsePosition;
import net.sf.myway.gps.datatypes.Color;
import net.sf.myway.gps.garmin.datatype.GarminColor;
import net.sf.myway.gps.garmin.unit.GarminData;

/**
 * @author Andreas Beckers
 * @version $Revision: 1.1 $
 */
public class ColorD110Converter implements Converter<Color> {

    /**
	 * @see net.sf.myway.gps.garmin.mapping.converters.Converter#convert(byte[], int, java.lang.Object, int)
	 */
    @Override
    public int convert(final byte[] buffer, final int pos, final Color data, final int length) {
        buffer[pos] |= (byte) GarminColor.getD110Code(data);
        return 1;
    }

    /**
	 * @see net.sf.myway.gps.garmin.mapping.converters.Converter#convert(byte[], int, int,
	 *      java.text.ParsePosition)
	 */
    @Override
    public Color convert(final GarminData data, final int pos, final int length, final ParsePosition ppos) {
        return GarminColor.getD110(data.getByte(pos) & 0x0f);
    }
}

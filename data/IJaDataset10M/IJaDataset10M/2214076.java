package org.shestkoff.nimium.model.converter;

import org.shestkoff.nimium.common.struct.Candle;
import org.shestkoff.nimium.common.struct.Bar;
import java.io.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ShestkoFF
 * Date: 06/09/2009
 * Time: 2:09:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class CSVConverter implements DataConverter<InputStream, Collection<Bar<Candle>>> {

    private static final String COLUMN_SEPARATOR = ";";

    private static final int DATE_COLUMN = 0;

    private static final int TIME_COLUMN = 1;

    private static final int OPEN_COLUMN = 2;

    private static final int HIGH_COLUMN = 3;

    private static final int LOW_COLUMN = 4;

    private static final int CLOSE_COLUMN = 5;

    private static final int VOLUME_COLUMN = 6;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    public Collection<Bar<Candle>> convert(@NotNull final InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final Collection<Bar<Candle>> resultCollection = new ArrayList<Bar<Candle>>(10);
        String line;
        while ((line = reader.readLine()) != null) {
            String[] strings = line.split(COLUMN_SEPARATOR);
            if (strings.length == 7) {
                final Date date = SDF.parse(String.format("%s%s", strings[DATE_COLUMN], strings[TIME_COLUMN]));
                float open = Float.valueOf(strings[OPEN_COLUMN]);
                float high = Float.valueOf(strings[HIGH_COLUMN]);
                float low = Float.valueOf(strings[LOW_COLUMN]);
                float close = Float.valueOf(strings[CLOSE_COLUMN]);
                long volume = Long.valueOf(strings[VOLUME_COLUMN]);
                resultCollection.add(new Bar<Candle>(date.getTime(), new Candle(open, high, low, close), volume));
            }
        }
        return resultCollection;
    }
}

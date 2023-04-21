package shared;

import com.sybase.jdbc3.tds.SybTimestamp;
import java.util.Date;

/**
 *
 * @author Praca
 */
public class Converter {

    public static Date convertSybTimestampIntoDate(SybTimestamp element) {
        if (element == null) {
            return null;
        }
        return new Date(element.getTime());
    }
}

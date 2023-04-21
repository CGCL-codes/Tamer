package fi.internetix.quercus;

import com.caucho.quercus.annotation.Optional;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.lib.date.DateTime;
import com.caucho.quercus.lib.date.DateTimeZone;

public class DateModule extends com.caucho.quercus.lib.date.DateModule {

    public static DateTime date_create(Env env, @Optional("now") String time, @Optional DateTimeZone dateTimeZone) {
        if (time == null) time = "";
        return DateTime.__construct(env, time, dateTimeZone);
    }
}

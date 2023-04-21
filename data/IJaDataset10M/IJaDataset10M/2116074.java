package hotpotato.util;

import java.util.Date;

public interface Clock {

    Date newDate();

    long currentTimeMillis();
}

package br.com.thelastsurvivor.provider.trophies;

import android.net.Uri;
import android.provider.BaseColumns;
import br.com.thelastsurvivor.provider.TheLastSurvivorProvider;
import br.com.thelastsurvivor.provider.util.Constant;

public class TrophiesProvider extends TheLastSurvivorProvider implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + TheLastSurvivorProvider.AUTHORITY + "/trophies");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TheLastSurvivorProvider.AUTHORITY;

    public static final String NAME_TABLE = "trophies";

    public static final String ID = "id";

    public static final String DATE_ACHIEVED = "date_achieved";

    static {
        getMatcher().addURI(TheLastSurvivorProvider.AUTHORITY, TrophiesProvider.NAME_TABLE, Constant.IS_TROPHIES);
    }
}

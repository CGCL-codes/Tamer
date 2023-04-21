package net.sf.myway.gps.garmin.datatype;

import net.sf.myway.gps.datatypes.Waypoint;
import net.sf.myway.gps.garmin.parser.ByteField;
import net.sf.myway.gps.garmin.parser.FloatField;
import net.sf.myway.gps.garmin.parser.IntField;
import net.sf.myway.gps.garmin.parser.SemicircleField;
import net.sf.myway.gps.garmin.parser.StringField;
import net.sf.myway.gps.garmin.unit.GarminData;
import net.sf.myway.gps.garmin.unit.GarminObject;

/**
 * @version	$Revision: 1.1 $
 * @author andreas
 */
public class D152Waypoint extends Waypoint implements GarminObject {

    static final StringField IDENT_FIELD = new StringField(0, 6);

    static final SemicircleField POSITION_FIELD = new SemicircleField(6);

    static final StringField COMMENT_FIELD = new StringField(18, 40);

    static final FloatField DISTANCE_FIELD = new FloatField(58);

    static final StringField NAME_FIELD = new StringField(62, 30);

    static final StringField CITY_FIELD = new StringField(92, 24);

    static final StringField STATE_FIELD = new StringField(116, 2);

    static final IntField ALTITUDE_FIELD = new IntField(118);

    static final StringField COUNTRY_FIELD = new StringField(120, 2);

    static final ByteField WPTCLASS_FIELD = new ByteField(123);

    @Override
    public void parseData(GarminData data) {
        setIdent(IDENT_FIELD.get(data));
        setPosition(POSITION_FIELD.get(data));
        setComment(COMMENT_FIELD.get(data));
        setDistance(new Float(DISTANCE_FIELD.get(data)));
        setName(NAME_FIELD.get(data));
        setCity(CITY_FIELD.get(data));
        setState(STATE_FIELD.get(data));
        setAltitude(new Float(ALTITUDE_FIELD.get(data)));
        setCountry(COUNTRY_FIELD.get(data));
        setWptClass(WPTCLASS_FIELD.get(data));
    }
}

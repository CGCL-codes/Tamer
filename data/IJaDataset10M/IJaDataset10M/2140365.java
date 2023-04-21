package net.fortuna.ical4j.model;

import java.net.URISyntaxException;
import net.fortuna.ical4j.model.parameter.AltRep;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.CuType;
import net.fortuna.ical4j.model.parameter.DelegatedFrom;
import net.fortuna.ical4j.model.parameter.DelegatedTo;
import net.fortuna.ical4j.model.parameter.Dir;
import net.fortuna.ical4j.model.parameter.Encoding;
import net.fortuna.ical4j.model.parameter.FbType;
import net.fortuna.ical4j.model.parameter.FmtType;
import net.fortuna.ical4j.model.parameter.Language;
import net.fortuna.ical4j.model.parameter.Member;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Range;
import net.fortuna.ical4j.model.parameter.RelType;
import net.fortuna.ical4j.model.parameter.Related;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Rsvp;
import net.fortuna.ical4j.model.parameter.SentBy;
import net.fortuna.ical4j.model.parameter.TzId;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.parameter.XParameter;

/**
 * A factory for creating iCalendar parameters.
 * 
 * @author benfortuna
 */
public final class ParameterFactory {

    private static ParameterFactory instance = new ParameterFactory();

    /**
	 * Constructor made private to prevent instantiation.
	 */
    private ParameterFactory() {
    }

    /**
	 * @return Returns the instance.
	 */
    public static ParameterFactory getInstance() {
        return instance;
    }

    /**
	 * Creates a parameter.
	 * 
	 * @param name
	 *            name of the parameter
	 * @param value
	 *            a parameter value
	 * @return a component
	 * @throws URISyntaxException
	 *             thrown when the specified string is not a valid
	 *             representation of a URI for selected parameters
	 */
    public Parameter createParameter(final String name, final String value) throws URISyntaxException {
        if (Parameter.ALTREP.equalsIgnoreCase(name)) {
            return new AltRep(value);
        } else if (Parameter.CN.equalsIgnoreCase(name)) {
            return new Cn(value);
        } else if (Parameter.CUTYPE.equalsIgnoreCase(name)) {
            return new CuType(value);
        } else if (Parameter.DELEGATED_FROM.equalsIgnoreCase(name)) {
            return new DelegatedFrom(value);
        } else if (Parameter.DELEGATED_TO.equalsIgnoreCase(name)) {
            return new DelegatedTo(value);
        } else if (Parameter.DIR.equalsIgnoreCase(name)) {
            return new Dir(value);
        } else if (Parameter.ENCODING.equalsIgnoreCase(name)) {
            return new Encoding(value);
        } else if (Parameter.FMTTYPE.equalsIgnoreCase(name)) {
            return new FmtType(value);
        } else if (Parameter.FBTYPE.equalsIgnoreCase(name)) {
            return new FbType(value);
        } else if (Parameter.LANGUAGE.equalsIgnoreCase(name)) {
            return new Language(value);
        } else if (Parameter.MEMBER.equalsIgnoreCase(name)) {
            return new Member(value);
        } else if (Parameter.PARTSTAT.equalsIgnoreCase(name)) {
            return new PartStat(value);
        } else if (Parameter.RANGE.equalsIgnoreCase(name)) {
            return new Range(value);
        } else if (Parameter.RELATED.equalsIgnoreCase(name)) {
            return new Related(value);
        } else if (Parameter.RELTYPE.equalsIgnoreCase(name)) {
            return new RelType(value);
        } else if (Parameter.ROLE.equalsIgnoreCase(name)) {
            return new Role(value);
        } else if (Parameter.RSVP.equalsIgnoreCase(name)) {
            return new Rsvp(value);
        } else if (Parameter.SENT_BY.equalsIgnoreCase(name)) {
            return new SentBy(value);
        } else if (Parameter.TZID.equalsIgnoreCase(name)) {
            return new TzId(value);
        } else if (Parameter.VALUE.equalsIgnoreCase(name)) {
            return new Value(value);
        } else {
            return new XParameter(name, value);
        }
    }
}

package com.risertech.xdav.carddav.element;

import com.risertech.xdav.caldav.type.Collation;
import com.risertech.xdav.carddav.type.Match;
import com.risertech.xdav.internal.carddav.CardDAVElement;

/**
 * 10.5.4.  CARDDAV:text-match XML Element
 * 
 * Name:  text-match
 * Namespace:  urn:ietf:params:xml:ns:carddav
 * Purpose:  Specifies a substring match on a property or parameter
 * 		value.
 * Description:  The CARDDAV:text-match XML element specifies text used
 * 		for a substring match against the property or parameter value
 * 		specified in an address book REPORT request.
 * 
 * 		The "collation" attribute is used to select the collation that the
 * 		server MUST use for character string matching.  In the absence of
 * 		this attribute the server MUST use the "i;unicode-casemap"
 * 		collation.
 * 
 * 		The "negate-condition" attribute is used to indicate that this
 * 		test returns a match if the text matches, when the attribute value
 * 		is set to "no", or return a match if the text does not match, if
 * 		the attribute value is set to "yes".  For example, this can be
 * 		used to match components with a CATEGORIES property not set to
 * 		PERSON.
 * 
 * 		The "match-type" attribute is used to indicate the type of match
 * 		operation to use.  Possible choices are:
 * 
 * 		"equals" - an exact match to the target string
 * 
 * 		"contains" - a substring match, matching anywhere within the
 * 		target string
 * 
 * 		"starts-with" - a substring match, matching only at the start
 * 		of the target string
 * 
 * 		"ends-with" - a substring match, matching only at the end of
 * 		the target string
 * 
 * Definition:
 * 		<!ELEMENT text-match (#PCDATA)>
 * 		<!-- PCDATA value: string -->
 * 
 * 		<!ATTLIST text-match
 * 			collation        CDATA "i;unicode-casemap"
 * 			negate-condition (yes | no) "no"
 * 			match-type (equals|contains|starts-with|ends-with) "contains">
 * 
 * @author phil
 */
public class TextMatch extends CardDAVElement {

    private final String text;

    private final Collation collation;

    private final boolean negateCondition;

    private final Match matchType;

    public TextMatch(String text, Collation collation, boolean negateCondition, Match matchType) {
        this.text = text;
        this.collation = collation;
        this.negateCondition = negateCondition;
        this.matchType = matchType;
    }

    public String getText() {
        return text;
    }

    public Collation getCollation() {
        return collation;
    }

    public boolean isNegateCondition() {
        return negateCondition;
    }

    public Match getMatchType() {
        return matchType;
    }
}

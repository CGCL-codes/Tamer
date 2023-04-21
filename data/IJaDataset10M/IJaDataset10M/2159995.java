package com.mojavelinux.open18;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;

/**
 * Exposes a collection of state abbreviations to be used to
 * back the options of a select menu form field.
 * 
 * @author Dan Allen <dan.allen@mojavelinux.com>
 */
@Name("states")
@Scope(ScopeType.APPLICATION)
public class StateListBean {

    private String[] abbreviations = new String[] { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY" };

    @Unwrap
    public List<String> getStateAbbreviations() {
        List<String> values = new ArrayList<String>();
        for (String abbr : abbreviations) {
            values.add(abbr);
        }
        return values;
    }
}

package com.starlight.types;

/**
 * Enumeration class containing states.
 *
 * @author reden
 * @version $Revision$ - $DateTime$
 */
public class USState implements Comparable {

    public static final USState ALABAMA = new USState("Alabama", "AL");

    public static final USState ALASKA = new USState("Alaska", "AK");

    public static final USState ARIZONA = new USState("Arizona", "AZ");

    public static final USState ARKANSAS = new USState("Arkansas", "AR");

    public static final USState CALIFORNIA = new USState("California", "CA");

    public static final USState COLORADO = new USState("Colorado", "CO");

    public static final USState CONNECTICUT = new USState("Connecticut", "CT");

    public static final USState DELIWARE = new USState("Delaware", "DE");

    public static final USState FLORIDA = new USState("Florida", "FL");

    public static final USState GEORGIA = new USState("Georgia", "GA");

    public static final USState HAWAII = new USState("Hawaii", "HI");

    public static final USState IDAHO = new USState("Idaho", "ID");

    public static final USState ILLINOIS = new USState("Illinois", "IL");

    public static final USState INDIANA = new USState("Indiana", "IN");

    public static final USState IOWA = new USState("Iowa", "IA");

    public static final USState KANSAS = new USState("Kansas", "KS");

    public static final USState KENTUKY = new USState("Kentucky", "KY");

    public static final USState LOUISIANA = new USState("Lousiana", "LA");

    public static final USState MAINE = new USState("Maine", "ME");

    public static final USState MARYLAND = new USState("Maryland", "MD");

    public static final USState MASSACHUSETTS = new USState("Massachusetts", "MA");

    public static final USState MICHIGAN = new USState("Michigan", "MI");

    public static final USState MINNESOTA = new USState("Minnesota", "MN");

    public static final USState MISSISSIPPI = new USState("Mississippi", "MS");

    public static final USState MISSOURI = new USState("Missouri", "MO");

    public static final USState MONTANA = new USState("Montana", "MT");

    public static final USState NEBRASKA = new USState("Nebraska", "NE");

    public static final USState NEVADA = new USState("Nevada", "NV");

    public static final USState NEW_HAMPSHIRE = new USState("New Hampshire", "NH");

    public static final USState NEW_JERSEY = new USState("New Jersey", "NJ");

    public static final USState NEW_MEXICO = new USState("New Mexico", "NM");

    public static final USState NEW_YORK = new USState("New York", "NY");

    public static final USState NORTH_CAROLINA = new USState("North Carolina", "NC");

    public static final USState NORTH_DAKOTA = new USState("North Dakota", "ND");

    public static final USState OHIO = new USState("Ohio", "OH");

    public static final USState OKLAHOMA = new USState("Oklahoma", "OK");

    public static final USState OREGON = new USState("Oregon", "OR");

    public static final USState PENNSYLVANIA = new USState("Pennsylvania", "PA");

    public static final USState RHODE_ISLAND = new USState("Rhode Island", "RI");

    public static final USState SOUTH_CAROLINA = new USState("South Carolina", "SC");

    public static final USState SOUTH_DAKOTA = new USState("South Dakota", "SD");

    public static final USState TENNESSEE = new USState("Tennessee", "TN");

    public static final USState TEXAS = new USState("Texas", "TX");

    public static final USState UTAH = new USState("Utah", "UT");

    public static final USState VERMONT = new USState("Vermont", "VT");

    public static final USState VIRGINIA = new USState("Virginia", "VA");

    public static final USState WASHINGTON = new USState("Washington", "WA");

    public static final USState WEST_VIRGINIA = new USState("West Virginia", "WV");

    public static final USState WISCONSIN = new USState("Wisconsin", "WI");

    public static final USState WYOMING = new USState("Wyoming", "WY");

    public static final USState AMERICAN_SAMOA = new USState("Amercian Samoa", "AS");

    public static final USState DISTRICT_OF_COLUMBIA = new USState("District of Columbia", "DC");

    public static final USState MICRONESIA = new USState("Federated States of Micronesia", "FM");

    public static final USState GUAM = new USState("Guam", "GU");

    public static final USState MARSHALL_ISLANDS = new USState("Marshall Islands", "MH");

    public static final USState NORTHERN_MARIANA_ISLANDS = new USState("Northern Mariana Islands", "MP");

    public static final USState PALAU = new USState("Palau", "PW");

    public static final USState PUERTO_RICO = new USState("Puerto Rico", "PR");

    public static final USState VIRGIAN_ISLANDS = new USState("Virgin Islands", "VI");

    public static final USState ARMED_FORCES_AFRICA = new USState("Armed Forces - Africa", "AE");

    public static final USState ARMED_FORCES_AMERICAS = new USState("Armed Forces - Africa", "AA");

    public static final USState ARMED_FORCES_CANADA = new USState("Armed Forces - Africa", "AE");

    public static final USState ARMED_FORCES_EUROPE = new USState("Armed Forces - Africa", "AE");

    public static final USState ARMED_FORCES_MIDDLE_EAST = new USState("Armed Forces - Africa", "AE");

    public static final USState ARMED_FORCES_PACIFIC = new USState("Armed Forces - Africa", "AP");

    public static final USState[] ALL_STATES = { ALABAMA, ALASKA, ARIZONA, ARKANSAS, CALIFORNIA, COLORADO, CONNECTICUT, DELIWARE, FLORIDA, GEORGIA, HAWAII, IDAHO, ILLINOIS, INDIANA, IOWA, KANSAS, KENTUKY, LOUISIANA, MAINE, MARYLAND, MASSACHUSETTS, MICHIGAN, MINNESOTA, MISSISSIPPI, MISSOURI, MONTANA, NEBRASKA, NEVADA, NEW_HAMPSHIRE, NEW_JERSEY, NEW_MEXICO, NEW_YORK, NORTH_CAROLINA, NORTH_DAKOTA, OHIO, OKLAHOMA, OREGON, PENNSYLVANIA, RHODE_ISLAND, SOUTH_CAROLINA, SOUTH_DAKOTA, TENNESSEE, TEXAS, UTAH, VERMONT, VIRGINIA, WASHINGTON, WEST_VIRGINIA, WISCONSIN, WYOMING, AMERICAN_SAMOA, DISTRICT_OF_COLUMBIA, MICRONESIA, GUAM, MARSHALL_ISLANDS, NORTHERN_MARIANA_ISLANDS, PALAU, PUERTO_RICO, VIRGIAN_ISLANDS, ARMED_FORCES_AFRICA, ARMED_FORCES_AMERICAS, ARMED_FORCES_CANADA, ARMED_FORCES_EUROPE, ARMED_FORCES_MIDDLE_EAST, ARMED_FORCES_PACIFIC };

    static {
        ALABAMA.bordering_states = new USState[] { FLORIDA, GEORGIA, MISSISSIPPI, TENNESSEE };
        ARIZONA.bordering_states = new USState[] { CALIFORNIA, COLORADO, NEVADA, NEW_MEXICO, UTAH };
        ARKANSAS.bordering_states = new USState[] { LOUISIANA, MISSISSIPPI, MISSOURI, OKLAHOMA, TENNESSEE, TEXAS };
        CALIFORNIA.bordering_states = new USState[] { NEVADA, OREGON };
        COLORADO.bordering_states = new USState[] { KANSAS, NEBRASKA, NEW_MEXICO, OKLAHOMA, UTAH, WYOMING };
        CONNECTICUT.bordering_states = new USState[] { MASSACHUSETTS, NEW_YORK, RHODE_ISLAND };
        DELIWARE.bordering_states = new USState[] { MARYLAND, NEW_JERSEY, PENNSYLVANIA };
        DISTRICT_OF_COLUMBIA.bordering_states = new USState[] { MARYLAND, VIRGINIA };
        FLORIDA.bordering_states = new USState[] { GEORGIA };
        GEORGIA.bordering_states = new USState[] { NORTH_CAROLINA, SOUTH_CAROLINA, TENNESSEE };
        IDAHO.bordering_states = new USState[] { MONTANA, NEVADA, OREGON, UTAH, WASHINGTON, WYOMING };
        ILLINOIS.bordering_states = new USState[] { INDIANA, IOWA, KENTUKY, MISSOURI, WISCONSIN };
        INDIANA.bordering_states = new USState[] { KENTUKY, MICHIGAN, OHIO };
        IOWA.bordering_states = new USState[] { MINNESOTA, MISSOURI, NEBRASKA, SOUTH_DAKOTA, WISCONSIN };
        KANSAS.bordering_states = new USState[] { MISSOURI, NEBRASKA, OKLAHOMA };
        KENTUKY.bordering_states = new USState[] { MISSOURI, OHIO, TENNESSEE, VIRGINIA, WEST_VIRGINIA };
        LOUISIANA.bordering_states = new USState[] { MISSISSIPPI, TEXAS };
        MAINE.bordering_states = new USState[] { NEW_HAMPSHIRE };
        MARYLAND.bordering_states = new USState[] { PENNSYLVANIA, VIRGINIA, WEST_VIRGINIA };
        MASSACHUSETTS.bordering_states = new USState[] { NEW_HAMPSHIRE, NEW_YORK, RHODE_ISLAND, VERMONT };
        MICHIGAN.bordering_states = new USState[] { OHIO, WISCONSIN };
        MINNESOTA.bordering_states = new USState[] { NORTH_DAKOTA, SOUTH_DAKOTA, WISCONSIN };
        MISSISSIPPI.bordering_states = new USState[] { TENNESSEE };
        MISSOURI.bordering_states = new USState[] { NEBRASKA, OKLAHOMA, TENNESSEE };
        MONTANA.bordering_states = new USState[] { NORTH_DAKOTA, SOUTH_DAKOTA, WYOMING };
        NEBRASKA.bordering_states = new USState[] { SOUTH_DAKOTA, WYOMING };
        NEVADA.bordering_states = new USState[] { OREGON, UTAH };
        NEW_HAMPSHIRE.bordering_states = new USState[] { VERMONT };
        NEW_JERSEY.bordering_states = new USState[] { NEW_YORK, PENNSYLVANIA };
        NEW_MEXICO.bordering_states = new USState[] { OKLAHOMA, TEXAS, UTAH };
        NEW_YORK.bordering_states = new USState[] { PENNSYLVANIA, VERMONT };
        NORTH_CAROLINA.bordering_states = new USState[] { SOUTH_CAROLINA, TENNESSEE, VIRGINIA };
        NORTH_DAKOTA.bordering_states = new USState[] { SOUTH_DAKOTA };
        OHIO.bordering_states = new USState[] { PENNSYLVANIA, WEST_VIRGINIA };
        OKLAHOMA.bordering_states = new USState[] { TEXAS };
        OREGON.bordering_states = new USState[] { WASHINGTON };
        PENNSYLVANIA.bordering_states = new USState[] { WEST_VIRGINIA };
        SOUTH_DAKOTA.bordering_states = new USState[] { WYOMING };
        TENNESSEE.bordering_states = new USState[] { VIRGINIA };
        UTAH.bordering_states = new USState[] { WYOMING };
        VIRGINIA.bordering_states = new USState[] { WEST_VIRGINIA };
    }

    private String name;

    private String abbreviation;

    private USState[] bordering_states;

    /**
	 * Find the state object with the given abbreviation.
	 */
    public static USState findByAbbreviation(String abbr) {
        if (abbr == null) return null;
        for (int i = 0; i < ALL_STATES.length; i++) {
            if (ALL_STATES[i].getAbbreviation().equalsIgnoreCase(abbr)) return ALL_STATES[i];
        }
        throw new IllegalArgumentException("Unknown state abbreviation: " + abbr);
    }

    /**
	 * Find the state object with the given name.
	 */
    public static USState findByName(String abbr) {
        if (abbr == null) return null;
        for (int i = 0; i < ALL_STATES.length; i++) {
            if (ALL_STATES[i].getName().equalsIgnoreCase(abbr)) return ALL_STATES[i];
        }
        return null;
    }

    private USState(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
        assert name != null;
        assert abbreviation != null;
    }

    public String getName() {
        return name;
    }

    /**
	 * Return the postal abbreviation for the state.
	 */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
	 * Return an array of the states that this state borders. Null if none.
	 */
    public USState[] getBorderingStates() {
        return bordering_states;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !getClass().equals(obj.getClass())) return false;
        USState other = (USState) obj;
        return name.equals(other.name);
    }

    public int hashCode() {
        int result;
        result = name.hashCode();
        result = 29 * result + abbreviation.hashCode();
        return result;
    }

    public int compareTo(Object o) {
        return name.compareTo(((USState) o).name);
    }
}

package de.mpii.html.table.property;

public class Nationality extends Property {

    public Nationality() {
        super(Type.BY_ENUMERATION);
        enumeration = new String[][] { { "Britain", "British", "English", "England" }, { "Dutch", "Dutch" }, { "France", "French" }, { "Iran", "Iranian" }, { "Italy", "Italian" }, { "Japan", "Japanese" }, { "Germany", "German" }, { "Poland", "Polish" }, { "Russia", "Russian" }, { "USA", "American" } };
    }
}

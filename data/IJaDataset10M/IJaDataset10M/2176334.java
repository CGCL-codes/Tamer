package ppine.io.parsers;

import ppine.io.parsers.rootparser.RootDataParser;
import ppine.io.exceptions.FamiliesTreeFormatException;
import ppine.io.exceptions.SpeciesTreeFormatException;

public abstract class DataParser {

    private static DataParser parser = new RootDataParser();

    public static DataParser getInstance() {
        return parser;
    }

    public abstract void readSpeciesString(String treeString) throws SpeciesTreeFormatException;

    public abstract void readFamiliesTreeString(String treeString) throws FamiliesTreeFormatException;
}

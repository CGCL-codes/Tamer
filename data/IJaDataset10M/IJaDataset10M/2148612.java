package uk.ac.ebi.intact.searchengine.parser.iql2luceneParser;

public interface valtagTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int Whitespace = 4;

    int Letter = 5;

    int Digit = 6;

    int SpecialChar = 7;

    int VALUE = 8;
}

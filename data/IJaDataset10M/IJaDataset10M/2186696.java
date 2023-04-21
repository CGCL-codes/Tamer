package visolate.parser;

public interface ParserConstants {

    int EOF = 0;

    int PARAM_DELIM = 5;

    int CODE_DELIM = 6;

    int SIGN = 7;

    int DIGITS = 8;

    int DIGIT = 9;

    int IN = 10;

    int LN = 11;

    int ADD = 12;

    int AM = 13;

    int COMMENT = 14;

    int TEXT = 15;

    int DIGITS2 = 16;

    int DIGIT2 = 17;

    int ALNUMS = 18;

    int AL = 19;

    int DEFAULT = 0;

    int TEXT_EXPECTED = 1;

    int ALNUMS_EXPECTED = 2;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"%\"", "\"*\"", "<SIGN>", "<DIGITS>", "<DIGIT>", "\"IN\"", "\"LN\"", "\"ADD\"", "\"AM\"", "\"G04\"", "<TEXT>", "<DIGITS2>", "<DIGIT2>", "<ALNUMS>", "<AL>", "\"N\"", "\"G00\"", "\"G01\"", "\"G02\"", "\"G03\"", "\"G36\"", "\"G37\"", "\"G54\"", "\"G70\"", "\"G71\"", "\"G74\"", "\"G75\"", "\"G90\"", "\"G91\"", "\"D01\"", "\"D02\"", "\"D03\"", "\"D\"", "\"X\"", "\"Y\"", "\"I\"", "\"J\"", "\".\"", "\"M00\"", "\"M01\"", "\"M02\"", "\"IC\"", "\"AS\"", "\"EB\"", "\"BC\"", "\"IS\"", "\"EI\"", "\"A\"", "\"B\"", "\"FS\"", "\"L\"", "\"T\"", "\"G\"", "\"Z\"", "\"M\"", "\"IP\"", "\"POS\"", "\"NEG\"", "\"LP\"", "\"C\"", "\"MOIN\"", "\"MOMM\"", "\"OF\"", "\"SF\"", "\"SR\"", "\",\"", "\"x\"", "\"/\"", "\"$\"" };
}

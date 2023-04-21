package nk.records;

public class RecordNameTokenizer {

    private static final RecordNameTokenList tokenList = new RecordNameTokenList();

    public static RecordNameToken getNameToken(String name) {
        return tokenList.getRecordNameToken(name);
    }

    public static String getName(RecordNameToken nameToken) {
        return tokenList.getRecordName(nameToken);
    }
}

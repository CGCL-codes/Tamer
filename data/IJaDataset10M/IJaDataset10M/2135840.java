package nk.records;

public class RecordPackage {

    private RecordPackageNameToken nameToken = null;

    private String modifier = null;

    private RecordList recordList = null;

    public RecordPackage(RecordPackageNameToken nameToken) {
        this.nameToken = nameToken;
    }

    public RecordPackage(RecordPackageNameToken nameToken, String modifier) {
        this.nameToken = nameToken;
        this.modifier = modifier;
    }

    public RecordPackage(RecordPackageNameToken nameToken, String modifier, RecordList recordList) {
        this.nameToken = nameToken;
        this.modifier = modifier;
        this.recordList = recordList;
    }

    public RecordPackage(RecordPackageNameToken nameToken, RecordList recordList) {
        this.nameToken = nameToken;
        this.recordList = recordList;
    }

    public RecordPackageNameToken getNameToken() {
        return nameToken;
    }

    public String getName() {
        return RecordPackageNameTokenizer.getName(nameToken);
    }

    public String getModifier() {
        return modifier;
    }

    public RecordList getRecordList() {
        return recordList;
    }

    public void setRecordList(RecordList recordList) {
        this.recordList = recordList;
    }
}

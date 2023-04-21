package common.protocol;

public class MsgField {

    private String szName;

    private StringBuffer szData;

    public MsgField(String _szName, StringBuffer _szData) {
        this.szName = _szName;
        this.szData = _szData;
    }

    public String getFieldName() {
        return this.szName.toString();
    }

    public String getFieldData() {
        return this.szData.toString();
    }

    public String toString() {
        StringBuffer szRet = new StringBuffer(szName.length() + 1 + szData.length());
        szRet.append(szName);
        szRet.append(':');
        szRet.append(szData);
        return szRet.toString();
    }
}

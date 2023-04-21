package axb.data;

public class ASubmit {

    public String by, key, type, value, tag, num, exeFectchIndex;

    public int fetchGroupNum;

    public ASubmit(String type, String by, String key, String tag, String num, String value, String fetchGroupNum, String exeFectchIndex) {
        this.type = type;
        this.by = by;
        this.key = key;
        this.tag = tag;
        this.num = num;
        this.value = value;
        this.fetchGroupNum = Integer.parseInt(fetchGroupNum);
        this.exeFectchIndex = exeFectchIndex;
    }
}

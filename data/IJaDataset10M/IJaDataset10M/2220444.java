package collabed.basics;

public class StringElement extends Element<String> {

    private static int id = 0;

    String str;

    public StringElement(String str) {
        super(id++);
        this.str = str;
    }

    public int size() {
        return str.length();
    }

    public String data() {
        return str;
    }

    public void extend(String str) {
        this.str = this.str + str;
    }

    public StringElement newInstance(String d) {
        return new StringElement(d);
    }

    public StringElement split(int index) {
        String rightStr = str.substring(index);
        StringElement rightElm = newInstance(rightStr);
        rightElm.prev = this;
        rightElm.next = this.next;
        if (rightElm.next != null) rightElm.next.prev = rightElm;
        this.next = rightElm;
        str = str.substring(0, index);
        return rightElm;
    }
}

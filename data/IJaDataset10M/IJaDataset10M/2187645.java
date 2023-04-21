package sgf.value;

public class ValueSimpleText implements Value {

    private String text;

    public ValueSimpleText() {
        text = "";
    }

    public ValueSimpleText(String str) {
        text = str;
    }

    public void setSgfString(String sgfStr) {
        StringBuilder str = new StringBuilder();
        boolean escaped = false;
        for (char c : sgfStr.toCharArray()) {
            if (escaped) {
                if (c == '\t' || c == '\f' || c == '\n' || c == '\r') {
                    str.append(' ');
                } else {
                    str.append(c);
                }
                escaped = false;
            } else {
                if (c == '\\') {
                    escaped = true;
                } else if (c == '\t' || c == '\f' || c == '\n' || c == '\r') {
                    str.append(' ');
                } else {
                    str.append(c);
                }
            }
        }
        text = str.toString();
    }

    public String getSgfString() {
        StringBuilder str = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c == ']' || c == '\\' || c == ':') {
                str.append('\\');
            }
            str.append(c);
        }
        return str.toString();
    }

    public String getValueString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        str.append(getSgfString());
        str.append("]");
        return str.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }
}

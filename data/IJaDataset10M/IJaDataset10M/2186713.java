package pub.beans;

public class BgColorBean {

    private int counter;

    public BgColorBean(int counter) {
        this.counter = counter;
    }

    public BgColorBean() {
        this(0);
    }

    public BgColorBean(String counter) {
        this(Integer.parseInt(counter));
    }

    public String getSame() {
        String value;
        if ((counter % 2) == 0) {
            value = "bgcolor=\"#D5D9DD\"";
        } else {
            value = "bgcolor=\"white\"";
        }
        return value;
    }

    public String same() {
        return getSame();
    }

    public String getNext() {
        counter++;
        String value = getSame();
        return value;
    }

    public String next() {
        return getNext();
    }

    public String colorPart() {
        String color = next();
        int start = color.indexOf('=');
        return color.substring(start + 2, color.length() - 1);
    }

    public String getColorPart() {
        return colorPart();
    }
}

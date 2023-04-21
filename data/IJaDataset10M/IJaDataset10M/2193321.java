package ognlscript;

/**
 * User: avilches
 * Date: 11-nov-2008
 * Time: 16:04:11
 */
public class Line {

    private String text;

    private String realCode;

    private int number;

    public Line(int number, String text) {
        this.number = number;
        this.text = text;
    }

    public Line(int number, String text, String realCode) {
        this.number = number;
        this.text = text;
        this.realCode = realCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRealCode() {
        return realCode;
    }

    public void setRealCode(String realCode) {
        this.realCode = realCode;
    }

    @Override
    public String toString() {
        return "Line{" + "text='" + text + '\'' + ", number=" + number + '}';
    }
}

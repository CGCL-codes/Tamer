package xml;

public class OtherExample {

    @Attribute(name = "text1")
    public String text1;

    @Element(name = "text2")
    public String text2;

    public String toString() {
        return "<" + text1 + ", " + text2 + ">";
    }
}

package hoverball.serial;

public class Team extends Serial {

    public int t;

    public int score;

    public Object clone() {
        return new Team(t, score);
    }

    public Team(int t, int score) {
        this.t = t;
        this.score = score;
    }

    public void serializeNative(StringBuffer buffer) {
        buffer.append("(team ");
        buffer.append(putInt(t));
        buffer.append(' ');
        buffer.append(putInt(score));
        buffer.append(')');
    }

    public XMLElement serializeXML() {
        XMLElement xml = new XMLElement();
        xml.setName("team");
        xml.setAttribute("t", putInt(t));
        xml.setAttribute("score", putInt(score));
        return xml;
    }

    public static Serial deserializeNative(Object[] objects) {
        String keyword = getString(objects[0]);
        if (!keyword.equals("team")) return null;
        if (objects.length != 3) throw new IllegalNumberOfArguments();
        int t = getInt(objects[1]);
        int score = getInt(objects[2]);
        return new Team(t, score);
    }

    public static Serial deserializeXML(XMLElement xml) {
        String keyword = getString(xml.getName());
        if (!keyword.equals("team")) return null;
        int t = getInt(xml.getAttribute("t"));
        int score = getInt(xml.getAttribute("score"));
        return new Team(t, score);
    }
}

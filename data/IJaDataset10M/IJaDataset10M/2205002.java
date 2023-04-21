package persister.data;

import java.util.Hashtable;
import org.eclipse.swt.graphics.Color;
import persister.AbstractRoot;

public interface Legend extends AbstractRoot {

    public static final Hashtable<String, Color> COLORS = new Hashtable<String, Color>() {

        private static final long serialVersionUID = -5632838312324903365L;

        {
            put("red", new Color(null, 255, 99, 71));
            put("blue", new Color(null, 173, 216, 230));
            put("green", new Color(null, 143, 188, 143));
            put("yellow", new Color(null, 255, 246, 143));
            put("white", new Color(null, 255, 255, 255));
            put("pink", new Color(null, 255, 192, 203));
            put("peach", new Color(null, 255, 218, 185));
            put("khaki", new Color(null, 189, 183, 107));
            put("aqua", new Color(null, 102, 205, 170));
            put("grey", new Color(null, 190, 190, 190));
            put("gray", new Color(null, 190, 190, 190));
            put("transparent", new Color(null, 123, 111, 120));
        }
    };

    public String getBlue();

    public void setBlue(String blue);

    public String getRed();

    public String getColor(String color);

    public void setRed(String red);

    public String getGreen();

    public void setGreen(String green);

    public String getYellow();

    public void setYellow(String yellow);

    public String getWhite();

    public void setWhite(String white);

    public String getPink();

    public void setPink(String pink);

    public String getAqua();

    public void setAqua(String aqua);

    public String getKhaki();

    public void setKhaki(String khaki);

    public String getPeach();

    public void setPeach(String peach);

    public String getGrey();

    public void setGrey(String gray);

    public long getId();

    public String getName();

    public void setId(long id);

    public void setName(String name);
}

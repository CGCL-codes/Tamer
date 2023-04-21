package geometry.objects;

import frames.MainFrame;
import frames.controls.ToolEntityButton;
import frames.controls.ToolMenuItem;
import geometry.base.SelectableEntity;
import geometry.base.ToolType;
import geometry.objects.editors.Resizer;
import geometry.objects.editors.Resizer.ResizerTypen;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.Point;
import java.util.ArrayList;
import property.Property;
import property.Property.PropertyType;
import tools.BaseObjectTool;
import util.Constants;
import util.UtilClass;

/**
 * Diese Klasse ist dazu gedacht, Texte darstellen zu können.
 * Diese Texte können fett, kursiv oder unterstrichen sein.
 * Außerdem können sie eine bestimmte Schriftart und
 * eine bestimmte Schriftgröße haben...
 * 
 * @author Etzlstorfer Andreas
 *
 */
public class Text extends SelectableEntity {

    static {
        SelectableEntity.registerToolEntity(new Text(50, 100, "Textfeld"));
    }

    public static final long serialVersionUID = 1;

    /**
	 * Text der dargestellt werden soll
	 */
    private String text = new String();

    /**
     * Schrift, mit dem der Text dargestellt werden soll
     */
    private String fontname = new String();

    /**
	 * Schriftgröße
	 */
    private Integer size;

    /**
     * Flag, ob Schrift Fett sein soll
     */
    private Boolean bold;

    /**
     * Flag, ob Schrift kursiv sein soll
     */
    private Boolean italic;

    /**
     * Flag, ob Schrift unterstrichen sein soll
     */
    private Boolean underline;

    /**
     * Wird gerade die Font verändert
     */
    private Boolean editmode = new Boolean(false);

    /**
	 * Ist zum internen Rechnen, um rauszufinden, wie
     * Breit die Schrift wird
	 */
    private Integer fontwidth;

    /**
     * Ist zum internen Rechnen, um rauszufinden, wie
     * hoch die Schrift wird
     */
    private int fontheight;

    /**
	 * virtuelle huelle des textfeldes
	 */
    private Rect hull;

    /**
     * Konstruktor der Klasse Text
     * 
     * @param x x-Weite
     * @param y y-Weite
     * @param text Text der geschrieben wird
     */
    public Text(int x, int y, String text) {
        this(x, y, text, Constants.STD_FONTSIZE);
    }

    /**
     * Konstruktor der Klasse Text
     * 
     * @param x x-Weite
     * @param y y-Weite
     * @param text Text der geschrieben wird
     * @param size Schriftgröße
     */
    public Text(int x, int y, String text, int size) {
        this(x, y, text, size, Constants.STD_FONTNAME);
    }

    /**
     * Konstruktor der Klasse Text
     * 
     * @param x x-Weite
     * @param y y-Weite
     * @param text Text der geschrieben wird
     * @param size Schriftgröße
     * @param fontname Schriftart
     */
    public Text(int x, int y, String text, int size, String fontname) {
        this(x, y, text, size, fontname, Constants.STD_FONTCOLOR);
    }

    /**
     * Konstruktor der Klasse Text
     * 
     * @param x x-Weite
     * @param y y-Weite
     * @param text Text der geschrieben wird
     * @param size Schriftgröße
     * @param fontname Schriftart
     * @param textcolor Textfarbe
     */
    public Text(int x, int y, String text, int size, String fontname, Color textcolor) {
        super(x, y, textcolor);
        this.text = text;
        this.size = size;
        setStandardDecoration();
    }

    /**
     * Setzt die Standarddekoration fuer die Schrift
     */
    private void setStandardDecoration() {
        bold = italic = underline = false;
    }

    public Point getExtremPunktNO() {
        return hull.getExtremPunktNO();
    }

    public Point getExtremPunktNW() {
        return hull.getExtremPunktNW();
    }

    public Point getExtremPunktSO() {
        return hull.getExtremPunktSO();
    }

    public Point getExtremPunktSW() {
        return hull.getExtremPunktSW();
    }

    public void setProperties(ArrayList<Property> properties) {
        text = (String) properties.remove(4).getRef();
        fontname = (String) properties.remove(4).getRef();
        size = (Integer) properties.remove(4).getRef();
        bold = (Boolean) properties.remove(4).getRef();
        italic = (Boolean) properties.remove(4).getRef();
        underline = (Boolean) properties.remove(4).getRef();
        super.setProperties(properties);
    }

    public ArrayList<Property> getProperties() {
        ArrayList<Property> temp = super.getProperties();
        Property p[] = { (Property) temp.get(2), (Property) temp.get(4), (Property) temp.get(5) };
        for (int i = 0; i < p.length; i++) p[i].setVisible(false);
        ((Property) temp.get(3)).setLabel("Textfarbe");
        temp.add(4, new Property("Text", PropertyType.STRING, text));
        temp.add(5, new Property("Schriftart", PropertyType.FONT_NAME_BOX, fontname));
        temp.add(6, new Property("Schriftgröße", PropertyType.NUMBER_SPINNER, size));
        temp.add(7, new Property("Fett", PropertyType.CHECKBOX, bold));
        temp.add(8, new Property("Kursiv", PropertyType.CHECKBOX, italic));
        temp.add(9, new Property("Unterstrichen", PropertyType.CHECKBOX, underline));
        return temp;
    }

    public void ResizerAction(Resizer r) {
        switch(r.getType()) {
            case N_RES:
                size += r.getOffY();
                break;
            case S_RES:
                size -= r.getOffY();
                break;
        }
        if (size < 0) size = 0;
    }

    public void drawMoveObjects(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(x - 5, y, x + 5, y);
        g.drawLine(x, y - 5, x, y + 5);
        g.setColor(Color.BLACK);
        g.drawString("x: " + x + " y: " + y, x, y + 20);
    }

    public void drawResizeObjects(Graphics g, Resizer r) {
        super.drawResizeObjects(g, r);
        int x = r.getX() + 5, y = r.getY();
        switch(r.getType()) {
            case N_RES:
                y -= 10;
                break;
            case O_RES:
                y += 15;
                break;
        }
        g.drawString("Schriftgröße" + this.size, x, y);
    }

    public String toString() {
        return "Textfeld (Text:" + text + " Schriftart:" + fontname + " Schriftgröße:" + size + " Fett/Kursiv/Unterstrichen:" + (bold ? "f" : "-") + (italic ? "k" : "-") + (underline ? "u" : "-") + ")";
    }

    public boolean isMouseOver(int x, int y) {
        return hull.isMouseOver(x, y);
    }

    public boolean isObjectInArea(int x1, int y1, int x2, int y2) {
        return hull.isObjectInArea(x1, y1, x2, y2);
    }

    public void Draw(Graphics g) {
        int style = Font.PLAIN;
        Font old = g.getFont();
        if (bold) style |= Font.BOLD;
        if (italic) style |= Font.ITALIC;
        Font temp = new Font(fontname, style, size);
        g.setColor(line);
        g.setFont(temp);
        g.drawString((editmode ? text + "_" : text), x, y);
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fontMetrics = g2d.getFontMetrics();
        fontwidth = (editmode ? fontMetrics.stringWidth(text + "_") : fontMetrics.stringWidth(text));
        fontheight = fontMetrics.getHeight();
        hull = new Rect(x, y - size, fontwidth, size);
        if (underline) {
            g2d.setStroke(new BasicStroke(this.size / 10));
            g.drawLine(x, y + 1 + this.size / 20, x + fontwidth, y + 1 + this.size / 20);
        }
        g.setFont(old);
    }

    public ArrayList<Resizer> getResizers() {
        ArrayList<Resizer> resizers = new ArrayList<Resizer>();
        resizers.add(new Resizer(getExtremPunktN(), this, ResizerTypen.N_RES));
        resizers.add(new Resizer(getExtremPunktS().x, getExtremPunktS().y + 3, this, ResizerTypen.S_RES));
        return resizers;
    }

    @Override
    public ArrayList<SelectableEntity> getLines() {
        return null;
    }

    /**
     * Gibt an ob sich das Objekt hier im Editiermodus befindet
     * 
     * @return ja/nein
     */
    public boolean isEditmode() {
        return editmode;
    }

    /**
     * Setzt den Editmodus
     * 
     * @param editmode ja/nein
     */
    public void setEditmode(boolean editmode) {
        this.editmode = editmode;
    }

    /**
     * Gibt an, ob diese Schrift fett ist
     * 
     * @return fette Schrift?
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * Gibt an, ob diese Schrift kursiv ist
     * 
     * @return fette Schrift?
     */
    public boolean isItalic() {
        return italic;
    }

    /**
     * Gibt die Größe an
     * 
     * @return Schriftgröße
     */
    public int getSize() {
        return size;
    }

    /**
     * Gibt den Text zurueck
     * 
     * @return Schrifttext
     */
    public String getText() {
        return text;
    }

    /**
     * Gibt an, ob diese Schrift unterstrichen ist
     * 
     * @return unterstrichene Schrift?
     */
    public boolean isUnderline() {
        return underline;
    }

    /**
     * Setzt die Schrift unterstrichen
     * 
     * @param bold unterstrichene Schrift?
     */
    public void setBold(boolean bold) {
        this.bold = bold;
    }

    /**
     * Setzt die Schrift kursiv
     * 
     * @param italic kursive Schrift?
     */
    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    /**
     * Setzt die schriftgröße
     * 
     * @param size Schriftgröße
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Setzt den schrifttext
     * 
     * @param text Schrifttext
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Setzt die Schrift unterstrichen
     * 
     * @param underline unterstrichene Schrift
     */
    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.TEXT;
    }

    public ToolEntityButton getToolButton() {
        return new ToolEntityButton("Textfeld", UtilClass.loadImageIconFromJAR("./img.jar", "buttons/text2.gif"), this);
    }

    @Override
    public ArrayList<MenuItem> getExtendedPopup() {
        ArrayList<MenuItem> temp = new ArrayList<MenuItem>();
        BaseObjectTool fett = new BaseObjectTool(this) {

            public void action(MainFrame f) {
                Text text = ((Text) this.entity);
                text.setBold(!text.isBold());
                super.action(f);
            }
        };
        BaseObjectTool kursiv = new BaseObjectTool(this) {

            public void action(MainFrame f) {
                Text text = ((Text) this.entity);
                text.setItalic(!text.isItalic());
                super.action(f);
            }
        };
        BaseObjectTool unterstrichen = new BaseObjectTool(this) {

            public void action(MainFrame f) {
                Text text = ((Text) this.entity);
                text.setUnderline(!text.isUnderline());
                super.action(f);
            }
        };
        temp.add(new ToolMenuItem("fett", fett));
        temp.add(new ToolMenuItem("kursiv", kursiv));
        temp.add(new ToolMenuItem("unterstrichen", unterstrichen));
        return temp;
    }
}

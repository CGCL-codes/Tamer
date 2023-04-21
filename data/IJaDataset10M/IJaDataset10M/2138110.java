package bm.ui.def.view.listBrowser;

import bm.core.io.SerializationException;
import bm.core.io.SerializerInputStream;
import bm.ui.def.view.ViewDef;
import java.util.Vector;

/**
 * List browser definition.
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision$
 */
public class ListBrowserDef extends ViewDef {

    public static final byte ENHANCED = 0;

    public static final byte NATIVE = 1;

    private Byte type;

    private String separator;

    private Integer pageSize;

    private Boolean wrap;

    private Integer fontFace;

    private Integer fontStyle;

    private Integer fontSize;

    private Integer color;

    private Integer selectedColor;

    private Integer background;

    private Integer selectedBackground;

    private Integer maxLines;

    private Vector fields = new Vector(10);

    private Vector speedOptions = new Vector(10);

    public Byte getType() {
        return type;
    }

    public String getSeparator() {
        return separator;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getFontFace() {
        return fontFace;
    }

    public Integer getFontStyle() {
        return fontStyle;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public Integer getColor() {
        return color;
    }

    public Integer getSelectedColor() {
        return selectedColor;
    }

    public Integer getBackground() {
        return background;
    }

    public Integer getSelectedBackground() {
        return selectedBackground;
    }

    public Integer getMaxLines() {
        return maxLines;
    }

    public Boolean getWrap() {
        return wrap;
    }

    public Vector getFields() {
        return fields;
    }

    public Vector getSpeedOptions() {
        return speedOptions;
    }

    protected void read(final SerializerInputStream in) throws SerializationException {
        in.readByte();
        type = in.readNullableByte();
        separator = in.readNullableString();
        pageSize = in.readNullableInt();
        wrap = in.readNullableBoolean();
        fontFace = in.readNullableInt();
        fontStyle = in.readNullableInt();
        fontSize = in.readNullableInt();
        color = in.readNullableInt();
        selectedColor = in.readNullableInt();
        background = in.readNullableInt();
        selectedBackground = in.readNullableInt();
        maxLines = in.readNullableInt();
        fields.removeAllElements();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            final FieldDef field = new FieldDef();
            field.read(in);
            fields.addElement(field);
        }
        speedOptions.removeAllElements();
        count = in.readInt();
        for (int i = 0; i < count; i++) {
            final SpeedOptionDef option = new SpeedOptionDef();
            option.read(in);
            speedOptions.addElement(option);
        }
    }
}

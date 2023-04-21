package ieditor.model.type;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

public class FontType extends XmlAdapter<String, Font> {

    public String marshal(Font font) throws Exception {
        return font.toString();
    }

    public Font unmarshal(String arg0) throws Exception {
        String[] font = arg0.split(" ");
        String name;
        int type = SWT.NORMAL;
        Integer height = 12;
        if (font.length == 2) {
            name = font[0];
        } else if (font.length == 3) {
            if (font[1].equalsIgnoreCase("bold")) type = SWT.BOLD;
            name = font[0];
            height = Integer.parseInt(font[2]);
            switch(height) {
                case 16:
                case 18:
                    height = 13;
                    break;
                case 20:
                    height = 16;
                    break;
                case 24:
                    height = 18;
                    break;
                case 36:
                    height = 26;
                    break;
                default:
                    height = 10;
            }
        }
        name = "Myriad Web";
        return new Font(null, name, height, type);
    }
}

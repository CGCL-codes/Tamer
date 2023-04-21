package options;

import javax.microedition.lcdui.*;
import mts.TransSched;

public class StylePrefs extends Form implements CommandListener {

    public void commandAction(Command cmd, Displayable d) {
        if (cmd == TransSched.cmdOK) {
            SaveSettings();
            TransSched.display.setCurrent(next);
        } else if (cmd == TransSched.cmdCancel) {
            TransSched.display.setCurrent(next);
        }
    }

    ChoiceGroup fontSize = null;

    ChoiceGroup fontFace = null;

    FontExample fe = null;

    TextField tfLineSpacing = null;

    TextField tfTextColorR = null;

    TextField tfTextColorG = null;

    TextField tfTextColorB = null;

    TextField tfFavColorR = null;

    TextField tfFavColorG = null;

    TextField tfFavColorB = null;

    Displayable next;

    public StylePrefs(Displayable next) {
        super("Настройки");
        this.next = next;
        addCommand(TransSched.cmdOK);
        addCommand(TransSched.cmdCancel);
        setCommandListener(this);
        append(new Spacer(0, 5));
        String fontSizes[] = { "Малый", "Средний", "Большой" };
        fontSize = new ChoiceGroup("Размер шрифта", Choice.POPUP, fontSizes, null);
        append(fontSize);
        String fontFaces[] = { "SYSTEM", "MONOSPACE", "PROPORTIONAL" };
        fontFace = new ChoiceGroup("Стиль шрифта", Choice.POPUP, fontFaces, null);
        append(fontFace);
        fe = new FontExample();
        fe.setItemCommandListener(new ItemCommandListener() {

            public void commandAction(Command command, Item item) {
                UpdateFontExample();
            }
        });
        fe.setDefaultCommand(new Command("Обновить", Command.ITEM, 1));
        append(fe);
        lastFontFace = FontExample.fontFace;
        lastFontSize = FontExample.fontSize;
        tfLineSpacing = new TextField("Межстр. интервал", "", 6, TextField.NUMERIC);
        append(tfLineSpacing);
        append(new StringItem(null, "Цвет текста"));
        tfTextColorR = new TextField("R", "", 6, TextField.DECIMAL);
        append(tfTextColorR);
        tfTextColorG = new TextField("G", "", 6, TextField.DECIMAL);
        append(tfTextColorG);
        tfTextColorB = new TextField("B", "", 6, TextField.DECIMAL);
        append(tfTextColorB);
        append(new StringItem(null, "Цвет текста избранного"));
        tfFavColorR = new TextField("R", "", 6, TextField.DECIMAL);
        append(tfFavColorR);
        tfFavColorG = new TextField("G", "", 6, TextField.DECIMAL);
        append(tfFavColorG);
        tfFavColorB = new TextField("B", "", 6, TextField.DECIMAL);
        append(tfFavColorB);
        LoadSettings();
        UpdateFontExample();
        setItemStateListener(new ItemStateListener() {

            public void itemStateChanged(Item item) {
                UpdateFontExample();
            }
        });
    }

    int lastFontFace;

    int lastFontSize;

    void UpdateFontExample() {
        int newFF = GetFontFace();
        int newFS = GetFontSize();
        if (newFF != lastFontFace || lastFontSize != newFS) {
            lastFontFace = newFF;
            lastFontSize = newFS;
            FontExample.fontFace = newFF;
            FontExample.fontSize = newFS;
            fe.update();
        }
    }

    void LoadSettings() {
        switch(Options.fontSize) {
            default:
            case Font.SIZE_SMALL:
                fontSize.setSelectedIndex(0, true);
                break;
            case Font.SIZE_MEDIUM:
                fontSize.setSelectedIndex(1, true);
                break;
            case Font.SIZE_LARGE:
                fontSize.setSelectedIndex(2, true);
                break;
        }
        switch(Options.fontFace) {
            default:
            case Font.FACE_SYSTEM:
                fontFace.setSelectedIndex(0, true);
                break;
            case Font.FACE_MONOSPACE:
                fontFace.setSelectedIndex(1, true);
                break;
            case Font.FACE_PROPORTIONAL:
                fontFace.setSelectedIndex(2, true);
                break;
        }
        tfLineSpacing.setString("" + Options.lineSpacing);
        tfTextColorR.setString("" + ((Options.textColor >> 16) & 0xFF));
        tfTextColorG.setString("" + ((Options.textColor >> 8) & 0xFF));
        tfTextColorB.setString("" + (Options.textColor & 0xFF));
        tfFavColorR.setString("" + ((Options.favoritesColor >> 16) & 0xFF));
        tfFavColorG.setString("" + ((Options.favoritesColor >> 8) & 0xFF));
        tfFavColorB.setString("" + (Options.favoritesColor & 0xFF));
    }

    int GetFontSize() {
        switch(fontSize.getSelectedIndex()) {
            case 1:
                return Font.SIZE_MEDIUM;
            case 2:
                return Font.SIZE_LARGE;
        }
        return Font.SIZE_SMALL;
    }

    int GetFontFace() {
        switch(fontFace.getSelectedIndex()) {
            case 1:
                return Font.FACE_MONOSPACE;
            case 2:
                return Font.FACE_PROPORTIONAL;
        }
        return Font.FACE_SYSTEM;
    }

    void SaveSettings() {
        Options.fontSize = GetFontSize();
        Options.fontFace = GetFontFace();
        Options.lineSpacing = Byte.parseByte(tfLineSpacing.getString());
        Options.textColor = (Integer.parseInt(tfTextColorR.getString()) << 16) | (Integer.parseInt(tfTextColorG.getString()) << 8) | Integer.parseInt(tfTextColorB.getString());
        Options.favoritesColor = (Integer.parseInt(tfFavColorR.getString()) << 16) | (Integer.parseInt(tfFavColorG.getString()) << 8) | Integer.parseInt(tfFavColorB.getString());
        OptionsStoreManager.SaveSettings();
        for (int i = 0; i < TransSched.optionsListeners.length; i++) {
            TransSched.optionsListeners[i].OptionsUpdated();
        }
    }
}

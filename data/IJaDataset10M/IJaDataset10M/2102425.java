package eln.editors;

import java.lang.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.peer.*;
import eln.nob.*;
import javax.swing.UIManager;
import WhiteBoard.WhiteBoard;
import WhiteBoard.WBMenuBar;
import emsl.JavaShare.ImageLoad;
import java.util.Locale;
import java.util.ResourceBundle;

public class WhiteBoardWrapper extends Panel implements INBEditor, IWhiteBoardWrapper {

    private INBClient mClient;

    private NObNode mNOb;

    private WhiteBoard wb;

    private static final String kIconPath = "icons";

    private static final String kEditorIcon = "EDwhitebd.gif";

    protected String langCode;

    protected String encoding;

    private Image getClassIcon() {
        Image classImage = ImageLoad.getImage(this, this, kIconPath, kEditorIcon);
        return classImage;
    }

    public Image getIcon() {
        return getClassIcon();
    }

    public String getLabel() {
        return "Whiteboard (V1.0)";
    }

    public void setReadOnly(boolean readonly) {
    }

    public void Launch(NObNode aNOb) {
        Launch(aNOb, "");
    }

    public void Launch(NObNode aNOb, String lc) {
        langCode = lc;
        Locale currentLocale = Locale.getDefault();
        if ((langCode != null) && (langCode.length() > 0)) currentLocale = new Locale(langCode);
        String editorL, font;
        try {
            ResourceBundle labels = ResourceBundle.getBundle("LabelsBundle", currentLocale);
            editorL = labels.getString("wbEditor");
            encoding = labels.getString("encoding");
            font = labels.getString("font");
        } catch (Exception e) {
            System.err.println("Error retrieving labels from LabelsBundle. Please check LabelsBundle.properties file.");
            editorL = "WhiteBoard Editor";
            encoding = "UTF-8";
            font = null;
        }
        if ((font == null) || (font.length() == 0)) {
            font = "Dialog";
        }
        Font currentFont = new Font(font, java.awt.Font.PLAIN, 12);
        mNOb = aNOb;
        wb = new WhiteBoard();
        wb.setClient((IWhiteBoardWrapper) this);
        wb.setFont(currentFont);
        if (mNOb != null) {
            String theFile = (String) aNOb.get("dataRef");
            if ((theFile != null) && (theFile.length() > 0)) {
                int posn = theFile.indexOf(":");
                if (posn >= 0) {
                    theFile = theFile.substring(posn + 3, theFile.length());
                }
                wb.Launch();
                wb.getWBCanvas().loadFileImage(theFile);
            } else {
                wb.Launch();
                wb.getWBCanvas().loadByte((byte[]) aNOb.get("data"));
            }
        } else {
            wb.Launch();
        }
        Frame f = wb.getFrame();
        MenuBar nwb = new WBMenuBar(wb, langCode);
        f.setMenuBar(nwb);
        f.setTitle(editorL);
        f.setFont(currentFont);
        f.pack();
        f.show();
    }

    public void setClient(INBClient aClient) {
        mClient = aClient;
    }

    public void SaveWhiteBoard(byte[] gifImage) {
        if (mNOb == null) {
            mNOb = new NOb();
        }
        mNOb.put("dataType", "image/gif");
        mNOb.remove("dataRef");
        mNOb.put("data", gifImage);
        mNOb.put("label", "GIF Image");
        mNOb.put("editor", "WhiteBoardWrapper");
        mClient.Save((NObNode) mNOb);
        wb = null;
    }

    public void SaveWhiteBoardJPG(byte[] jpgImage) {
        if (mNOb == null) {
            mNOb = new NOb();
        }
        mNOb.put("dataType", "image/jpg");
        mNOb.remove("dataRef");
        mNOb.put("data", jpgImage);
        mNOb.put("label", "JPG Image");
        mNOb.put("editor", "WhiteBoardWrapper");
        mClient.Save((NObNode) mNOb);
        wb = null;
    }
}

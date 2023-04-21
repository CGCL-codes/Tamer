package apollo.config;

import apollo.gui.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class PeptideStatus {

    private String status = "not analyzed";

    private int precedence = 0;

    private Font font = null;

    private Font default_font = Config.getDefaultFeatureLabelFont();

    private boolean curated = false;

    public PeptideStatus() {
    }

    public String initStatus(String config_str) {
        int breakIndex = config_str.indexOf(":");
        if (breakIndex > 0) {
            String prec_str = config_str.substring(0, breakIndex);
            setPrecedence(prec_str);
            config_str = config_str.substring(breakIndex + 1);
            breakIndex = config_str.indexOf(":");
            if (breakIndex > 0) {
                String font_str = config_str.substring(0, breakIndex);
                font = Style.parseFont(font_str);
                config_str = config_str.substring(breakIndex + 1);
                breakIndex = config_str.indexOf(":");
                if (breakIndex > 0) {
                    String curated_str = config_str.substring(0, breakIndex);
                    curated = (curated_str.equalsIgnoreCase("true"));
                    config_str = config_str.substring(breakIndex + 1);
                }
            }
        }
        if (config_str != null && !config_str.equals("")) status = config_str;
        return status;
    }

    public String getText() {
        return status;
    }

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int prec) {
        precedence = prec;
    }

    public void setPrecedence(String precedence_str) {
        try {
            int prec = Integer.parseInt(precedence_str);
            setPrecedence(prec);
        } catch (Exception e) {
            System.out.println("Failed to parse precedence from " + precedence_str);
            setPrecedence(0);
        }
    }

    public Font getFont() {
        return (font == null ? default_font : font);
    }

    public boolean getCurated() {
        return curated;
    }
}

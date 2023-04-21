package com.memoire.bu;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BuTitledBorder extends TitledBorder {

    private static final int getDefaultJustification() {
        int r = UIManager.getInt("TitledBorder.justification");
        if ((r <= 0) || (r > 5)) r = 4;
        return r;
    }

    private static final int getDefaultPosition() {
        int r = UIManager.getInt("TitledBorder.position");
        if ((r <= 0) || (r > 6)) r = 2;
        return r;
    }

    public BuTitledBorder(String _title) {
        this(null, _title, getDefaultJustification(), getDefaultPosition(), null, null);
    }

    public BuTitledBorder(Border _border) {
        this(_border, "", getDefaultJustification(), getDefaultPosition(), null, null);
    }

    public BuTitledBorder(Border _border, String _title) {
        this(_border, _title, getDefaultJustification(), getDefaultPosition(), null, null);
    }

    public BuTitledBorder(Border _border, String _title, int _titleJustification, int _titlePosition) {
        this(_border, _title, _titleJustification, _titlePosition, null, null);
    }

    public BuTitledBorder(Border _border, String _title, int _titleJustification, int _titlePosition, Font _titleFont) {
        this(_border, _title, _titleJustification, _titlePosition, _titleFont, null);
    }

    public BuTitledBorder(Border _border, String _title, int _titleJustification, int _titlePosition, Font _titleFont, Color _titleColor) {
        super(_border, _title, _titleJustification, _titlePosition, _titleFont, _titleColor);
    }
}

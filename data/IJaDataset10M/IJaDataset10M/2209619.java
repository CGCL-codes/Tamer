package com.wb.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.*;
import android.widget.*;
import android.widget.RelativeLayout.LayoutParams;

public class Cell extends RelativeLayout {

    public int rowNum;

    public int colNum;

    public boolean isInInventory;

    public boolean isInGame;

    private CellButton overlayButton;

    private RelativeLayout rowOneLayout;

    private RelativeLayout rowTwoLayout;

    private TextView charOneView;

    private TextView charTwoView;

    private String charOne;

    private String charTwo;

    private int LAYOUT_SIZE;

    private int TEXT_SIZE;

    private int TEXT_PADDING_VERTICAL_SIZE;

    private int TEXT_PADDING_HORIZON_SIZE;

    public Cell(Context context) {
        super(context);
        initCell(context, null);
    }

    public Cell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCell(context, attrs);
    }

    public Cell(Context context, int s, String f, String l) {
        super(context);
        initCell(context, s, f, l);
    }

    private void initCell(Context c, AttributeSet attrs) {
        initSizes(100);
        generateViews(c);
        generateCharacters("", "");
        generateViewSizes(LAYOUT_SIZE);
    }

    private void initCell(Context c, int s, String f, String l) {
        rowNum = 0;
        colNum = 0;
        isInInventory = false;
        isInGame = false;
        initSizes(s);
        generateViews(c);
        generateCharacters(f, l);
        generateViewSizes(LAYOUT_SIZE);
    }

    private void generateViewSizes(int lS) {
        addView(overlayButton, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        addView(rowOneLayout, generateLayoutParam(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_TOP));
        addView(rowTwoLayout, generateLayoutParam(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_BOTTOM));
        rowOneLayout.addView(charOneView, generateLayoutParam(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT));
        rowTwoLayout.addView(charTwoView, generateLayoutParam(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT));
    }

    private RelativeLayout.LayoutParams generateLayoutParam(int w, int h, int a) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
        lp.setMargins(TEXT_PADDING_HORIZON_SIZE, TEXT_PADDING_VERTICAL_SIZE, TEXT_PADDING_HORIZON_SIZE, TEXT_PADDING_VERTICAL_SIZE);
        lp.addRule(a);
        return lp;
    }

    private void generateCharacters(String f, String l) {
        charOne = f;
        charTwo = l;
        generateCharacterView(charOneView, f);
        generateCharacterView(charTwoView, l);
    }

    private void generateCharacterView(TextView tW, String c) {
        tW.setText(c);
        tW.setTextSize(TEXT_SIZE);
        tW.setTextColor(Color.parseColor("#FF6600"));
        tW.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void generateViews(Context c) {
        overlayButton = new CellButton(c, this);
        rowOneLayout = new RelativeLayout(c);
        rowTwoLayout = new RelativeLayout(c);
        charOneView = new TextView(c);
        charTwoView = new TextView(c);
    }

    private void initSizes(int s) {
        LAYOUT_SIZE = s;
        TEXT_SIZE = LAYOUT_SIZE / 3;
        TEXT_PADDING_HORIZON_SIZE = TEXT_SIZE / 10;
        TEXT_PADDING_VERTICAL_SIZE = TEXT_PADDING_HORIZON_SIZE;
        this.setLayoutParams(new LayoutParams(LAYOUT_SIZE, LAYOUT_SIZE));
    }

    public void setCharacters(char f, char l) {
        generateCharacters(Character.toString(f), Character.toString(l));
    }

    public void setCharacters(String f, String l) {
        generateCharacters(f, l);
    }

    public void setCharacters(String fl) {
        generateCharacters(Character.toString(fl.charAt(0)), Character.toString(fl.charAt(1)));
    }

    public void regenerateViewSizes(int lS) {
        initSizes(lS);
        generateViewSizes(lS);
    }

    public String[] getCharacters() {
        return new String[] { charOne, charTwo };
    }

    public String getCharacterSet() {
        return charOne + charTwo;
    }

    public void setOnClickCell(OnClickListener oCL) {
        overlayButton.setOnClickListener(oCL);
    }

    public CellButton getOverlayButton() {
        return this.overlayButton;
    }

    public boolean isCellFilled() {
        if (charOne.equals("") || charTwo.equals("")) {
            return false;
        }
        return true;
    }
}

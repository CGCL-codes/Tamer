package ch.mastermapframework.swingElements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextArea;

public class MMTextArea {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected JTextArea myTextArea;

    private String mainFormattedText;

    private int nbRrows;

    private int columnLength = 100;

    private Integer minNbRows;

    private Integer minNbCols;

    private int textAreaHeight;

    private float textFont = 10f;

    private static int lineSpacer = 3;

    protected Color color = Color.white;

    public MMTextArea() {
        initTextPanel();
    }

    public MMTextArea(String in) {
        initTextPanel();
        this.setText(in);
    }

    public MMTextArea(String in, float size) {
        this.textFont = size;
        this.initTextPanel();
        this.setText(in);
    }

    public void setEditable(Boolean in) {
        this.myTextArea.setEditable(in);
    }

    public void setTextSize(float in) {
        this.textFont = in;
        this.setTextAreaOutline();
    }

    public void setBackGroundColor(Color in) {
        this.color = in;
    }

    private void initTextPanel() {
        this.myTextArea = new JTextArea();
        this.myTextArea.setEditable(false);
        this.mainFormattedText = new String();
        this.myTextArea.setBackground(this.color);
    }

    private void setTextAreaOutline() {
        this.myTextArea.setEditable(false);
        Font f = this.myTextArea.getFont();
        this.myTextArea.setFont(f.deriveFont(this.textFont));
        this.myTextArea.setBackground(this.color);
        String s = this.myTextArea.getText();
        String lineReturn = "\n";
        int pos = 0;
        this.nbRrows = 0;
        while ((pos < s.length())) {
            this.nbRrows++;
            pos = s.indexOf(lineReturn, pos);
            if (pos < 0) {
                break;
            }
            pos++;
        }
        if (this.minNbRows != null) {
            if (this.minNbRows > this.nbRrows) {
                this.nbRrows = this.minNbRows;
            } else if (this.minNbRows < this.nbRrows) {
                this.nbRrows = this.minNbRows;
            }
        }
        if (this.minNbCols != null) {
            if (this.minNbCols > this.columnLength) {
                this.columnLength = this.minNbCols;
            } else if (this.minNbCols < this.columnLength) {
                this.columnLength = this.minNbCols;
            }
        }
        this.textAreaHeight = this.nbRrows * (MMTextArea.lineSpacer + (int) this.textFont);
        this.myTextArea.setColumns(this.columnLength);
        this.myTextArea.setRows(this.nbRrows);
        this.myTextArea.setSize(new Dimension(this.columnLength, this.textAreaHeight));
        this.myTextArea.repaint();
        this.myTextArea.validate();
    }

    public JTextArea getTextArea() {
        return this.myTextArea;
    }

    public String getText() {
        return this.myTextArea.getText();
    }

    public void addText(String in) {
        this.mainFormattedText += in;
        this.setText(this.mainFormattedText);
    }

    public void setText(String in) {
        this.mainFormattedText = in;
        this.myTextArea.setText(this.mainFormattedText);
        this.setTextAreaOutline();
        this.myTextArea.setToolTipText(this.mainFormattedText);
    }

    public void setTextAreaColumnSize(int d) {
        this.columnLength = d;
        this.setTextAreaOutline();
    }

    public void setFontSize(float in) {
        this.textFont = in;
        this.setTextAreaOutline();
    }

    public void setMinNBRows(int in) {
        this.minNbRows = in;
        this.setTextAreaOutline();
    }

    public void setMinNBCols(int in) {
        this.minNbCols = in;
        this.setTextAreaOutline();
    }

    public void setToolTipText(String in) {
        this.myTextArea.setToolTipText(in);
    }
}

package orproject.frontend.helpers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.border.LineBorder;

/**
 * Steuerelement, welches ein Simplex-Tableau darstellt
 *
 * @author Andreas Hirsch
 */
public class JTableau extends JPanel implements Scrollable {

    /**
     * Default: Anzahl der inneren Spalten
     */
    private static final int DEF_INNER_COLUMN_COUNT = 5;

    /**
     * Default: Anzahl der inneren Zeilen
     */
    private static final int DEF_INNER_ROW_COUNT = 5;

    /**
     * Default: Hintergrundfarbe aller Beschriftungsfelder
     */
    private static final Color DEF_BACKGROUND_LBL = Color.GRAY;

    /**
     * Default: Hintergrundfarbe aller Eingabefelder
     */
    private static final Color DEF_BACKGROUND_TXT = Color.WHITE;

    /**
     * Default: Aktivierungsstatus
     */
    private static final boolean DEF_ENABLED = true;

    /**
     * Default: Spaltenbreite
     */
    private static final int DEF_COLUMN_WIDTH = 30;

    /**
     * Default: Zeilenhöhe
     */
    private static final int DEF_ROW_HEIGHT = 30;

    /**
     * Anzahl der erlaubten Vorkommastellen in den Eingabefeldern
     */
    private static final int STELLEN_VORKOMMA = 6;

    /**
     * Anzahl der erlaubten Nachkommastellen in den Eingabefeldern
     */
    private static final int STELLEN_NACHKOMMA = 2;

    /**
     * Erlaubtes Trennzeichen zwischen Vor- und Nachkommastellen
     */
    private static final char DEZIMAL_TRENNZEICHEN = ',';

    public static final String PROP_INNERROWCOUNT = "innerRowCount";

    public static final String PROP_INNERCOLUMNCOUNT = "innerColumnCount";

    public static final String PROP_COLUMNWIDTH = "columnWidth";

    public static final String PROP_ENABLED = "enabled";

    public static final String PROP_INPUT = "input";

    public static final String PROP_ROWHEIGHT = "rowHeight";

    /**
     * Anzahl der inneren Spalten
     */
    private int innerRowCount;

    /**
     * Anzahl der inneren Zeilen
     */
    private int innerColumnCount;

    /**
     * Hintergrundfarbe aller Beschriftungsfelder
     */
    private Color backgroundLbl;

    /**
     * Hintergrundfarbe aller Eingabefelder
     */
    private Color backgroundTxt;

    /**
     * Aktivierungsstatus
     */
    private boolean enabled;

    /**
     * Spaltenbreite
     */
    private int columnWidth;

    /**
     * Zeilenhöhe
     */
    private int rowHeight;

    /**
     * Anzuzeigende Breite
     */
    private int widthToShow;

    /**
     * Anzuzeigende Höhe
     */
    private int heightToShow;

    /**
     * Anzahl der Spalten, die Eingabefelder beinhalten
     */
    private int inputColumnCount;

    /**
     * Anzahl aller Spalten
     */
    private int totalColumnCount;

    /**
     * Anzahl der Spalten, die Eingabefelder beinhalten
     */
    private int inputRowCount;

    /**
     * Anzahl aller Zeilen
     */
    private int totalRowCount;

    /**
     * Beinhaltet alle Eingabefelder des Tableaus
     */
    private JTextField[][] inputFields;

    /**
     * Beinhaltet alle Beschriftungsfelder der ersten Zeile
     */
    private JLabel[] labelsTop;

    /**
     * Beinhaltet alle Beschriftungsfelder der ersten Spalte
     */
    private JLabel[] labelsLeft;

    /**
     * Reguläre Ausdrücke, die korrekte Eingaben beschreiben
     */
    private List<String> correctPatterns;

    /**
     * Anlage eines neuen JTableaus
     */
    public JTableau() {
        setInnerColumnCount(DEF_INNER_COLUMN_COUNT);
        setInnerRowCount(DEF_INNER_ROW_COUNT);
        setEnabled(DEF_ENABLED);
        setBackgroundLbl(DEF_BACKGROUND_LBL);
        setBackgroundTxt(DEF_BACKGROUND_TXT);
        setColumnWidth(DEF_COLUMN_WIDTH);
        setRowHeight(DEF_ROW_HEIGHT);
        initializeValidationPatterns();
        adjustTableau();
    }

    private void initializeValidationPatterns() {
        correctPatterns = new ArrayList<String>();
        correctPatterns.add("[0-9]");
        correctPatterns.add("[1-9][0-9]{0," + (STELLEN_VORKOMMA - 1) + "}");
        correctPatterns.add("[0-9][1-9]{0," + (STELLEN_VORKOMMA - 1) + "}[" + DEZIMAL_TRENNZEICHEN + "][0-9]{0," + STELLEN_NACHKOMMA + "}");
        correctPatterns.add("[-0-9]");
        correctPatterns.add("[-0-9]{0," + (STELLEN_VORKOMMA) + "}");
        correctPatterns.add("[-0-9]{0," + STELLEN_VORKOMMA + "}[" + DEZIMAL_TRENNZEICHEN + "][0-9]{0," + STELLEN_NACHKOMMA + "}");
    }

    private void adjustSize() {
        setPreferredSize(new Dimension(totalColumnCount * columnWidth, totalRowCount * rowHeight));
    }

    /**
     * Neuaufbau des Tableaus
     */
    private void adjustTableau() {
        removeAll();
        adjustSize();
        inputFields = new JTextField[inputRowCount][inputColumnCount];
        labelsLeft = new JLabel[innerRowCount];
        labelsTop = new JLabel[innerColumnCount];
        setLayout(new GridLayout(0, totalColumnCount));
        for (int tRow = 0; tRow < totalRowCount; tRow++) {
            if (tRow == 0) {
                generateColumnsFirstRow();
            } else {
                generateColumnsOtherRows(tRow);
            }
        }
        revalidate();
    }

    /**
     * Erzeugung der ersten Tableau-Zeile, die ausschließlich
     * Spaltenbeschriftungen enthält
     */
    private void generateColumnsFirstRow() {
        for (int tColumn = 0; tColumn < totalColumnCount; tColumn++) {
            String tText = new String();
            if (tColumn >= 1 && tColumn < innerColumnCount + 1) {
                tText = "x" + tColumn;
            }
            JLabel tLabel = createLabelField(tText);
            if (tColumn >= 1 && tColumn < innerColumnCount + 1) {
                labelsTop[tColumn - 1] = tLabel;
            }
            if (tColumn == innerColumnCount + 1) {
                tLabel.setText("<=");
            }
            add(tLabel);
        }
    }

    /**
     * Erzeugung aller weiteren Tableau-Zeilen, die jeweils in der ersten
     * Spalte ein Beschriftungsfeld und in allen weiteren Spalten ein
     * Eingabefeld erhalten.
     *
     * @param pRow Zu erzeugende Zeile
     */
    private void generateColumnsOtherRows(int pRow) {
        for (int tColumn = 0; tColumn < totalColumnCount; tColumn++) {
            if (tColumn == 0) {
                String tText = new String();
                if (pRow < innerRowCount + 1) {
                    tText = "z" + pRow;
                }
                JLabel tLabel = createLabelField(tText);
                add(tLabel);
                if (pRow < innerRowCount + 1) {
                    labelsLeft[pRow - 1] = tLabel;
                }
            } else {
                JTextField tField = createInputField("0");
                add(tField);
                inputFields[pRow - 1][tColumn - 1] = tField;
                if (pRow == inputRowCount && tColumn == inputColumnCount) {
                    tField.setEnabled(false);
                }
            }
        }
    }

    /**
     * Erzeugung eines neuen Beschriftungsfeldes
     *
     * @param pText Anzuzeigender Text
     * @return Konfiguriertes Beschriftungsfeld
     */
    private JLabel createLabelField(String pText) {
        JLabel tLabel = new JLabel(pText, JLabel.CENTER);
        tLabel.setBorder(new LineBorder(Color.BLACK, 1));
        tLabel.setBackground(backgroundLbl);
        tLabel.setOpaque(true);
        return tLabel;
    }

    /**
     * Erzeugung eines neuen Tableau-Eingabefeldes
     *
     * @param pDefaultText Standardmäßig angezeigter Text
     * @return Konfiguriertes Tableau-Eingabefeld
     */
    private JTextField createInputField(String pDefaultText) {
        JTextField tField = new JTextField();
        tField.setBorder(new LineBorder(Color.BLACK, 1));
        tField.setHorizontalAlignment(JTextField.HORIZONTAL);
        tField.setOpaque(true);
        tField.setBackground(backgroundTxt);
        tField.setEnabled(enabled);
        tField.addKeyListener(new ValidationKeyListener(correctPatterns, "0"));
        tField.addFocusListener(new SelectionFocusListener());
        tField.setText(pDefaultText);
        tField.setForeground(Color.BLACK);
        tField.setDisabledTextColor(Color.BLACK);
        return tField;
    }

    /**
     * Setzt den Fokus auf das angegebene Feld
     *
     * @param pRow Zeilenindex
     * @param pColumn Spaltenindex
     */
    public void setFocousOnField(int pRow, int pColumn) {
        if (pRow < inputFields.length && pColumn < inputFields[0].length) {
            inputFields[pRow][pColumn].requestFocus();
        }
    }

    /**
     * Setzen der Zeilenbeschriftungen in der ersten Spalte für alle inneren
     * Zeilen (d.h. nicht für die erste und letzte Zeile)
     *
     * @param pText Anzuzeigender Text je Beschriftungsfeld
     */
    public void setLabelsLeft(String[] pText) {
        if (labelsLeft.length == pText.length) {
            for (int i = 0; i < labelsLeft.length; i++) {
                labelsLeft[i].setText(pText[i]);
            }
        }
    }

    /**
     * Setzen der Spaltenbeschriftungen in der ersten Zeile für alle inneren
     * Spalten (d.h. nicht für die erste und letzte Spalte)
     *
     * @param pText Anzuzeigender Text je Beschriftungsfeld
     */
    public void setLabelsTop(String[] pText) {
        if (labelsTop.length == pText.length) {
            for (int i = 0; i < labelsTop.length; i++) {
                labelsTop[i].setText(pText[i]);
            }
        }
    }

    /**
     * Liefert die Anzahl der inneren Spalten (d.h. die Anzahl aller Spalten
     * ohne die erste und letzte Spalte)
     *
     * @return Anzahl der inneren Spalten
     */
    public int getInnerColumnCount() {
        return innerColumnCount;
    }

    /**
     * Setzt die Anzahl der inneren Spalten (d.h. die Anzahl aller Spalten ohne
     * die erste und letzte Spalte)
     *
     * @param pInnerColumnCount Anzahl der inneren Spalten
     */
    public void setInnerColumnCount(int pInnerColumnCount) {
        int oldInnerColumnCount = this.innerColumnCount;
        this.innerColumnCount = pInnerColumnCount;
        if (oldInnerColumnCount == innerColumnCount) {
        } else {
            inputColumnCount = innerColumnCount + 1;
            totalColumnCount = inputColumnCount + 1;
            adjustTableau();
        }
        firePropertyChange(PROP_INNERCOLUMNCOUNT, oldInnerColumnCount, pInnerColumnCount);
    }

    /**
     * Liefert die Anzahl der inneren Zeilen (d.h. die Anzahl aller Zeilen
     * ohne die erste und letzte Zeile)
     *
     * @return Anzahl der inneren Zeilen
     */
    public int getInnerRowCount() {
        return innerRowCount;
    }

    /**
     * Setzt die Anzahl der inneren Zeilen (d.h. die Anzahl aller Zeilen ohne
     * die erste und letzte Zeile)
     *
     * @param pInnerRowCount Anzahl der inneren Zeilen
     */
    public void setInnerRowCount(int pInnerRowCount) {
        int oldInnerRowCount = this.innerRowCount;
        this.innerRowCount = pInnerRowCount;
        if (oldInnerRowCount == innerRowCount) {
        } else {
            inputRowCount = innerRowCount + 1;
            totalRowCount = inputRowCount + 1;
            adjustTableau();
        }
        firePropertyChange(PROP_INNERROWCOUNT, oldInnerRowCount, innerRowCount);
    }

    /**
     * Liefert den Aktivierungsstatus aller Eingabefelder
     *
     * @return the value of enabled
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Setzt den Aktivierungsstatus aller Eingabefelder
     *
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        super.setEnabled(pEnabled);
        boolean oldEnabled = enabled;
        enabled = pEnabled;
        setFieldsEnabled();
        firePropertyChange(PROP_ENABLED, oldEnabled, pEnabled);
    }

    /**
     * Ändert den Aktivierungsstatus aller Eingabefelder
     */
    private void setFieldsEnabled() {
        for (int tRow = 0; tRow < inputFields.length; tRow++) {
            for (int tColumn = 0; tColumn < inputFields[0].length; tColumn++) {
                inputFields[tRow][tColumn].setEnabled(enabled);
            }
        }
        inputFields[innerRowCount][innerColumnCount].setEnabled(false);
    }

    /**
     * Liefert alle Eingaben des Tableaus ohne Zeilen- und Spaltenbeschriftungen
     *
     * @return Alle Eingaben des Tableaus ohne Zeilen- und Spaltenbeschriftungen
     */
    public String[][] getInput() {
        String[][] tInput = new String[inputFields.length][inputFields[0].length];
        for (int tRow = 0; tRow < inputFields.length; tRow++) {
            for (int tColumn = 0; tColumn < inputFields[0].length; tColumn++) {
                tInput[tRow][tColumn] = inputFields[tRow][tColumn].getText().replace(',', '.');
            }
        }
        return tInput;
    }

    /**
     * Setzt die Werte aller Eingabefelder des Tableaus (Beschriftungsfelder
     * spielen folglich keine Rolle).
     *
     * @param pInput Anzuzeigende Werte der Eingabefelder
     */
    public void setInput(String[][] pInput) {
        if (inputFields.length == pInput.length && inputFields[0].length == pInput[0].length) {
            for (int tRow = 0; tRow < inputFields.length; tRow++) {
                for (int tColumn = 0; tColumn < inputFields[0].length; tColumn++) {
                    inputFields[tRow][tColumn].setText(pInput[tRow][tColumn]);
                }
            }
        }
    }

    /**
     * Liefert die Hintergrundfarbe der Beschriftungsfelder (erste Zeile und
     * erste Spalte)
     *
     * @return Hintergrundfarbe der Beschriftungsfelder
     */
    public Color getBackgroundLbl() {
        return backgroundLbl;
    }

    /**
     * Setzt die Hintergrundfarbe der Zeilen- und Spaltenbeschriftung (erste
     * Zeile und erste Spalte)
     *
     * @param pBackgroundLbl new value of backgroundLbl
     */
    public void setBackgroundLbl(Color pBackgroundLbl) {
        backgroundLbl = pBackgroundLbl;
        for (int tIndexZeile = 0; tIndexZeile < labelsLeft.length; tIndexZeile++) {
            labelsLeft[tIndexZeile].setBackground(pBackgroundLbl);
        }
        for (int tIndexSpalte = 0; tIndexSpalte < labelsTop.length; tIndexSpalte++) {
            labelsTop[tIndexSpalte].setBackground(pBackgroundLbl);
        }
    }

    /**
     * Liefert die Hintergrundfarbe der Eingabefelder
     *
     * @return Hintergrundfarbe der Eingabefelder
     */
    public Color getBackgroundTxt() {
        return backgroundTxt;
    }

    /**
     * Setzt die Hintergrundfarbe der Eingabefelder
     *
     * @param pBackgroundTxt Hintergrundfarbe der Eingabefelder
     */
    public void setBackgroundTxt(Color pBackgroundTxt) {
        backgroundTxt = pBackgroundTxt;
        for (int tRow = 0; tRow < inputFields.length; tRow++) {
            for (int tColumn = 0; tColumn < inputFields[0].length; tColumn++) {
                inputFields[tRow][tColumn].setBackground(pBackgroundTxt);
            }
        }
    }

    /**
     * Liefert die Breite einer einzelnen Spalte
     *
     * @return Spaltenbreite
     */
    public int getColumnWidth() {
        return columnWidth;
    }

    /**
     * Setzt die Breite einer Spalte. Diese Breite wird beibehalten, egal wie
     * viele Felder angezeigt werden (JTableau wird deshalb größer)
     *
     * @param pColumnWidth Spaltenbreite
     */
    public void setColumnWidth(int pColumnWidth) {
        int oldColumnWidth = this.columnWidth;
        columnWidth = pColumnWidth;
        adjustSize();
        firePropertyChange(PROP_COLUMNWIDTH, oldColumnWidth, pColumnWidth);
    }

    /**
     * Liefert die Höhe einer einzelnen Zeile
     *
     * @return Zeilenhöhe
     */
    public int getRowHeight() {
        return rowHeight;
    }

    /**
     * Setzt die Höhe einer Zeile. Diese Höhe wird beibehalten, egal wie
     * viele Felder angezeigt werden (JTableau wird deshalb größer)
     *
     * @param pRowHeight Zeilenhöhe
     */
    public void setRowHeight(int pRowHeight) {
        int oldRowHeight = this.rowHeight;
        rowHeight = pRowHeight;
        adjustSize();
        firePropertyChange(PROP_ROWHEIGHT, oldRowHeight, pRowHeight);
    }

    /**
     * Liefert die anzuzeigende Breite
     *
     * @return Anzuzeigende Breite
     */
    public int getWidthToShow() {
        return widthToShow;
    }

    /**
     * Setzt die anzuzeigende Breite
     *
     * @param pWidthToShow Anzuzeigende Breite
     */
    public void setWidthToShow(int pWidthToShow) {
        widthToShow = pWidthToShow;
    }

    /**
     * Liefert die anzuzeigende Höhe
     *
     * @return Anzuzeigende Höhe
     */
    public int getHeightToShow() {
        return heightToShow;
    }

    /**
     * Setzt die anzuzeigende Höhe
     *
     * @param pHeightToShow Anzuzeigende Höhe
     */
    public void setHeightToShow(int pHeightToShow) {
        heightToShow = pHeightToShow;
    }

    public Dimension getPreferredScrollableViewportSize() {
        int panelWidth = totalColumnCount * columnWidth;
        int panelHeight = totalRowCount * rowHeight;
        if (panelHeight > heightToShow) {
            panelHeight = heightToShow;
        }
        if (panelWidth > widthToShow) {
            panelWidth = widthToShow;
        }
        return new Dimension(panelWidth, panelHeight);
    }

    public int getScrollableUnitIncrement(Rectangle pVisibleRect, int pOrientation, int pDirection) {
        return 15;
    }

    public int getScrollableBlockIncrement(Rectangle pVisibleRect, int pOrientation, int pDirection) {
        return 30;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    /**
     * KeyListener zur automatischen Eingabevalidierung je gedrückter Taste
     */
    private class ValidationKeyListener implements KeyListener {

        /**
	 * Zwischengespeicherter Text
	 */
        private String oldText;

        /**
	 * Alle regülären Ausdrücke, die gültige Eingaben beschreiben
	 */
        private List<String> correctPatterns;

        /**
	 * Standardwert, der gesetzt wird wenn das Feld nach der Prüfung
	 * leer wäre
	 */
        private String defaultValue;

        /**
	 * Sperre, die bei erfolgtem Tastendruck gesetzt und erst nach erfolgter
	 * Valdidierung wieder aufgehoben wird
	 */
        private boolean locked;

        public ValidationKeyListener(List<String> pCorrectPatterns, String pDefaultValue) {
            correctPatterns = pCorrectPatterns;
            defaultValue = pDefaultValue;
            locked = false;
        }

        public void keyTyped(KeyEvent e) {
            if (!locked) {
                oldText = ((JTextField) e.getSource()).getText();
                locked = true;
            }
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
            JTextField tField = (JTextField) e.getSource();
            if (!ValidationUtils.validate(tField.getText(), correctPatterns)) {
                boolean tToReset = oldText.equals(new String());
                if (tToReset) {
                    oldText = defaultValue;
                }
                tField.setText(oldText);
                if (oldText.equals(defaultValue)) {
                    tField.setSelectionStart(0);
                    tField.setSelectionEnd(tField.getText().length());
                }
            }
            locked = false;
        }
    }

    /**
     * FocusListener, der die Markierung des gesamten Feldinhaltes bei
     * Erlangen des Fokus durchführt
     */
    private class SelectionFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
            JTextField tField = (JTextField) e.getSource();
            tField.setSelectionStart(0);
            tField.setSelectionEnd(tField.getText().length());
        }

        public void focusLost(FocusEvent e) {
        }
    }
}

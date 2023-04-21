package tuner3d.util.swing;

import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JTNumEdit extends JTextField {

    private boolean initialized = true;

    protected int gDecimalLength = -1;

    protected int nMaxStringLen = 14;

    protected String Formatter = "#,##0.00";

    protected boolean zero2100 = false;

    protected boolean noFS = false;

    public JTNumEdit() {
        super();
        init();
    }

    public JTNumEdit(double d) {
        super();
        init();
        setValueToTextField(d);
    }

    public JTNumEdit(int MaxStringLen, String Formatter) {
        super();
        init();
        this.nMaxStringLen = MaxStringLen;
        this.Formatter = Formatter;
    }

    public JTNumEdit(int MaxStringLen, int decimalLength, String Formatter) {
        super();
        init();
        this.nMaxStringLen = MaxStringLen;
        this.Formatter = Formatter;
        this.gDecimalLength = decimalLength;
    }

    public JTNumEdit(int MaxStringLen, int decimalLength, String Formatter, boolean zero2100) {
        super();
        init();
        this.nMaxStringLen = MaxStringLen;
        this.Formatter = Formatter;
        this.gDecimalLength = decimalLength;
        this.zero2100 = zero2100;
    }

    public JTNumEdit(int MaxStringLen, String Formatter, boolean noFS) {
        super();
        init();
        this.nMaxStringLen = MaxStringLen;
        this.Formatter = Formatter;
        this.noFS = noFS;
    }

    protected void init() {
        this.setHorizontalAlignment(SwingConstants.RIGHT);
        this.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(FocusEvent e) {
                NumEdit_FocusLost(e);
            }
        });
        this.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(FocusEvent e) {
                NumEdit_FocusGained(e);
            }
        });
    }

    void NumEdit_FocusLost(FocusEvent e) {
        if (!this.getText().trim().equals("")) setValueToTextField(getValueFromTextField());
    }

    void NumEdit_FocusGained(FocusEvent e) {
        this.selectAll();
    }

    public void setText(String text) {
        setValueToTextField(Convert(text));
    }

    public double getValueFromTextField() {
        return Convert(this.getText());
    }

    public void setValueToTextField(double d) {
        String ss = this.getText();
        String tt = FormattedText(d);
        if (!ss.equals(tt)) super.setText(tt);
    }

    public void setValueToTextField(String s) {
        String ss = this.getText();
        String tt = FormattedText(Convert(s));
        if (!ss.equals(tt)) this.setText(tt);
    }

    public String FormattedText(double Value) {
        String ValueText = null;
        java.text.DecimalFormat dfStandard = new java.text.DecimalFormat(Formatter);
        ValueText = dfStandard.format(Value);
        return ValueText;
    }

    public static double Convert(String TextString) {
        double d = 0;
        TextString = TextString.replaceAll(",", "");
        String TempString = "";
        TempString = TextString.replaceAll("-", "");
        if (!TempString.trim().equals("")) try {
            d = Double.parseDouble(TextString);
        } catch (Exception ex) {
            d = 0;
        }
        return d;
    }

    protected Document createDefaultModel() {
        return new NumCheck();
    }

    public void setDocument(Document d) {
        if (!initialized || d instanceof NumCheck) super.setDocument(d); else throw new UnsupportedOperationException();
    }

    private class NumCheck extends PlainDocument {

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (getLength() > nMaxStringLen - 1) {
                return;
            }
            if (str == null || str.equals("")) {
                super.insertString(offs, str, a);
            } else {
                if (offs == 0) {
                    if (noFS) {
                        if (((int) str.charAt(0) == 45) && ((int) str.charAt(0) != 46) && ((int) str.charAt(0) < 48 || (int) str.charAt(0) > 57)) {
                            return;
                        }
                    } else {
                        if (((int) str.charAt(0) != 45) && ((int) str.charAt(0) != 46) && ((int) str.charAt(0) < 48 || (int) str.charAt(0) > 57)) {
                            return;
                        }
                    }
                    if (str.length() > 1) {
                        for (int i = 1; i < str.length(); i++) {
                            if (((int) str.charAt(i) < 48 || (int) str.charAt(i) > 57) && ((int) str.charAt(i) != 44) && ((int) str.charAt(i) != 46)) {
                                return;
                            }
                        }
                    }
                } else {
                    if (offs == 1) {
                        for (int i = 0; i < str.length(); i++) {
                            if ((((int) getText(0, 1).charAt(0)) < 48 || ((int) getText(0, 1).charAt(0)) > 57) && (((int) str.charAt(i) < 48 || (int) str.charAt(i) > 57) && ((int) str.charAt(i) != 44))) {
                                return;
                            }
                        }
                    }
                    for (int i = 0; i < str.length(); i++) {
                        if (((int) str.charAt(i) < 48 || (int) str.charAt(i) > 57) && ((int) str.charAt(i) != 44) && ((int) str.charAt(i) != 46)) {
                            return;
                        }
                        if (((int) str.charAt(i) == 46)) {
                            String strOld = getText(0, getLength());
                            for (int j = 0; j < strOld.length(); j++) {
                                if ((int) strOld.charAt(j) == 46) {
                                    return;
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < str.length(); i++) {
                    if (((int) str.charAt(i) == 46)) {
                        String strOld = getText(0, getLength());
                        for (int j = 0; j < strOld.length(); j++) {
                            if ((int) strOld.charAt(j) == 46) {
                                return;
                            }
                        }
                    }
                    if (((int) str.charAt(i) == 45)) {
                        String strOld = getText(0, getLength());
                        for (int j = 0; j < strOld.length(); j++) {
                            if ((int) strOld.charAt(j) == 45) {
                                return;
                            }
                        }
                    }
                }
                int iFlag = str.lastIndexOf(".");
                if (iFlag >= 0) {
                    if ((str.length() - iFlag) > 10) {
                        str = str.substring(0, iFlag + 11);
                    }
                }
                String oldString = getText(0, getLength());
                String newString = oldString.substring(0, offs) + str + oldString.substring(offs);
                iFlag = newString.lastIndexOf(".");
                if (iFlag >= 0) {
                    if ((getLength() - iFlag) > 10) {
                        return;
                    }
                }
                if (gDecimalLength > -1) {
                    int decimalPosition = newString.lastIndexOf(".");
                    if (decimalPosition > -1) {
                        if ((newString.length() - (decimalPosition + 1)) > gDecimalLength) return;
                    }
                }
                if (zero2100) {
                    if (str.equals("-")) return;
                    if (newString.equals("")) newString = "0";
                    newString = newString.replaceAll(",", "");
                    double value = 0;
                    if (!newString.equals(".")) value = new Double(Double.parseDouble(newString)).doubleValue();
                    if (value < 0 || value >= 100) return;
                }
                super.insertString(offs, str, a);
            }
        }
    }
}

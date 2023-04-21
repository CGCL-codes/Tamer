package met.flatfile.format;

import met.flatfile.Format;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;
import met.flatfile.config.ConfigurationException;
import met.flatfile.FormatException;

/**
 * @author Ville Vuori
 * 
 * <code>BigDecimalFormat<code> converts text to number and number to text.   	
 * Format rules can be configured with setters or method <code>setParameter</code>
 * which accepts parameter and value pairs:
 * <ul>
 * <li>max-digits	{@link java.text.DecimalFormat#setMaximumFractionDigits(int)}
 * <li>min-digits	{@link java.text.DecimalFormat#setMinimumFractionDigits(int)}
 * <li>number-format		{@link java.text.DecimalFormat#applyPattern(java.lang.String)}
 * <li>decimal-separator 	decimal separator as String
 * </ul> 
 * Default numberformat uses <code>java.text.DecimalFormat</code> with pattern = #, 
 * maximum number of digits = 9 and minimum number of digits = 0
 * <p>
 * 
 * TODO: tämä luokka on tarkistettava / korjattava -> numberformat on hieman kökkö tähän tarkoitukseen!!
 */
public class BigDecimalFormat implements Format<BigDecimal> {

    private NumberFormat numberFormat;

    private DecimalFormatSymbols dfs;

    private String decimalSeparator;

    private int digits;

    private Pattern decimalPattern;

    /**
     * Initializes class format with default numberformat
     */
    public BigDecimalFormat() {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(dfs);
        numberFormat = decimalFormat;
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(9);
        decimalSeparator = ".";
        digits = 0;
        decimalPattern = Pattern.compile(this.decimalSeparator);
    }

    @Override
    public BigDecimal parse(String source) {
        return convertToBigDecimal(source);
    }

    @Override
    public String format(BigDecimal source) throws FormatException {
        return convertToString(source);
    }

    @Override
    public Class getType() {
        return BigDecimal.class;
    }

    /** Return decimal separator of this object.
     * @return String decimal separator
     */
    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    /** Set decimal separator.
     * @param decimalSeparator
     */
    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    /** Return number format of this object.
     * @return numberformat of BigDecimalFormat
     */
    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    /** Set number format.
     * @param numberFormat
     */
    public void setNumberFormat(DecimalFormat decimalFormat) {
        this.numberFormat = decimalFormat;
    }

    /** Get number digit count.
     * @return numberformat of BigDecimalFormat
     */
    public int getDigits() {
        return digits;
    }

    /** Set number digit count.
     * @param digits
     */
    public void setDigits(int digits) {
        this.digits = digits;
    }

    private BigDecimal convertToBigDecimal(String str) {
        String s;
        BigDecimal big = null;
        if (!this.decimalSeparator.equals(".")) {
            if (this.decimalSeparator.length() == 0) {
                int length = str.length();
                int splitPoint = length - digits;
                StringBuilder sb = new StringBuilder();
                if (length >= digits) {
                    sb.append(str.substring(0, splitPoint));
                    sb.append(".");
                    sb.append(str.substring(splitPoint));
                } else {
                    int lenDiff = digits - length;
                    sb.append(".");
                    for (int i = 0; i < lenDiff; i++) {
                        sb.append("0");
                    }
                    sb.append(str);
                }
                s = sb.toString();
            } else {
                s = decimalPattern.matcher(str).replaceAll(".");
            }
        } else {
            s = str;
        }
        try {
            big = new BigDecimal(numberFormat.parse(s).doubleValue());
        } catch (ParseException pf) {
            pf.printStackTrace();
        }
        return big;
    }

    private String convertToString(BigDecimal bd) {
        String str = numberFormat.format(bd.doubleValue());
        if (numberFormat.getMaximumFractionDigits() > 0) {
            String[] tmpStr = str.split("\\.");
            StringBuilder sb = new StringBuilder();
            sb.append(tmpStr[0]);
            if (tmpStr.length > 1) {
                sb.append(decimalSeparator);
                sb.append(tmpStr[1]);
            }
            str = sb.toString();
        }
        return str;
    }
}

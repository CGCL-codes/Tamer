package KFrameWork.Base;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.table.*;
import java.text.*;
import com.toedter.calendar.JDateChooser;
import KFrameWork.FieldValidation.*;
import java.text.SimpleDateFormat;

/**
	General purpose meta class, with some basic general methods.
*/
public class KMetaUtilsClass extends java.lang.Object {

    public static final int DATE_FIELD = 1;

    public static final int TIME_FIELD = 2;

    static int nextInternalFramePosition = 0;

    /** Open a file from server */
    public static InputStream openRemoteFile(URL urlParam) throws KExceptionClass {
        InputStream result = null;
        try {
            result = urlParam.openStream();
        } catch (IOException error) {
            String message = new String();
            message = "Cant open resource [";
            message += urlParam.toString();
            message += "][";
            message += error.toString();
            message += "]";
            throw new KExceptionClass(message, error);
        }
        ;
        return (result);
    }

    public static final String DISPLAY_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DISPLAY_HOUR_FORMAT = "HH:MM";

    public static final String KDEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final long MILLSECS_PER_DAY = 1000 * 60 * 60 * 24;

    public static java.util.Date addToDate(int timeUnit, int value, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(timeUnit, value);
        return (calendar.getTime());
    }

    ;

    public static String changeDateFormat(String sourceFormat, String targetFormat, String value) throws KExceptionClass {
        String resultado = "";
        SimpleDateFormat fmtOrigen = new SimpleDateFormat(sourceFormat);
        SimpleDateFormat fmtDestino = new SimpleDateFormat(targetFormat);
        try {
            resultado = fmtDestino.format(fmtOrigen.parse(value));
        } catch (Exception e) {
            throw new KExceptionClass(" Invalid Date ", e);
        }
        return resultado;
    }

    public static String dateToString(String targetFormat, java.util.Date value) {
        if (value == null) return (new String());
        SimpleDateFormat fmtDestino = new SimpleDateFormat(targetFormat);
        return fmtDestino.format(value);
    }

    public static java.util.Date stringToDate(String sourceFormat, String value) throws KExceptionClass {
        java.util.Date resultado = null;
        if (value != null) {
            try {
                SimpleDateFormat fmtOrigen = new SimpleDateFormat(sourceFormat);
                resultado = fmtOrigen.parse(value);
            } catch (Exception e) {
                throw new KExceptionClass(" Invalid Date ", e);
            }
        }
        return resultado;
    }

    /** Get currrent date */
    public static java.util.Date getCurrentDate() {
        return new java.util.Date();
    }

    /** Get currrent date */
    public static String getCurrentDateString(String formatoFecha) {
        java.util.Date fecha = new java.util.Date();
        SimpleDateFormat formateador = new SimpleDateFormat(formatoFecha);
        String fechaFormateada = formateador.format(fecha);
        return fechaFormateada;
    }

    public static long daysBetween(String fechaInicio, String fechaFin) throws KExceptionClass {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(stringToDate(DISPLAY_DATE_FORMAT, fechaFin));
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(stringToDate(DISPLAY_DATE_FORMAT, fechaInicio));
        return endDate.get(Calendar.DAY_OF_YEAR) - startDate.get(Calendar.DAY_OF_YEAR);
    }

    /**  Get a timestamp. Useful to stamp anything. */
    public static String timeStamp() {
        String result;
        GregorianCalendar calendar = new GregorianCalendar();
        result = String.valueOf(calendar.get(calendar.YEAR));
        result += "/";
        result += String.valueOf(calendar.get(calendar.MONTH) + 1);
        result += "/";
        result += String.valueOf(calendar.get(calendar.DAY_OF_MONTH));
        result += " ";
        result += String.valueOf(calendar.get(calendar.HOUR_OF_DAY));
        result += ":";
        result += String.valueOf(calendar.get(calendar.MINUTE));
        result += ":";
        result += String.valueOf(calendar.get(calendar.SECOND));
        result += ".";
        result += String.valueOf(calendar.get(calendar.MILLISECOND));
        return (result);
    }

    /**  Get a timestamp. Useful to stamp anything. */
    public static String time() {
        String result;
        GregorianCalendar calendar = new GregorianCalendar();
        result = String.valueOf(calendar.get(calendar.YEAR));
        result += "/";
        result += String.valueOf(calendar.get(calendar.MONTH) + 1);
        result += "/";
        result += String.valueOf(calendar.get(calendar.DAY_OF_MONTH));
        result += " ";
        result += String.valueOf(calendar.get(calendar.HOUR_OF_DAY));
        result += ":";
        result += String.valueOf(calendar.get(calendar.MINUTE));
        result += ":";
        result += String.valueOf(calendar.get(calendar.SECOND));
        return (result);
    }

    /**  Get a timestamp. Useful to stamp anything. */
    public static String timeOfDay() {
        String result;
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);
        formatter.setMinimumIntegerDigits(2);
        GregorianCalendar calendar = new GregorianCalendar();
        String hours = formatter.format(calendar.get(calendar.HOUR));
        if (hours.equals("00")) hours = "12";
        result = hours;
        result += ":";
        result += formatter.format(calendar.get(calendar.MINUTE));
        result += " ";
        if (calendar.get(calendar.AM_PM) == calendar.AM) {
            result += "AM";
        } else {
            result += "PM";
        }
        ;
        return (result);
    }

    /** Returns the stack trace from any exception as a String */
    public static String getStackTrace(Exception exceptionParam) {
        String stackTrace = new String();
        StringWriter stringBuffer = new StringWriter();
        PrintWriter traceWriter = new PrintWriter(stringBuffer);
        exceptionParam.printStackTrace(traceWriter);
        stackTrace = stringBuffer.getBuffer().toString();
        return (stackTrace);
    }

    public static java.awt.Window getParentWindow(java.awt.Component componentParam) {
        return (Frame) SwingUtilities.getAncestorOfClass(Window.class, componentParam);
    }

    public static void getComponentsFromContainer(Container container, java.util.List list) {
        Component[] componentArray = container.getComponents();
        for (int i = 0; i < componentArray.length; i++) {
            Component currentComponent = componentArray[i];
            if (currentComponent.getName() != null) {
                list.add(currentComponent);
            }
            ;
            if (currentComponent instanceof Container) getComponentsFromContainer((Container) currentComponent, list);
        }
    }

    /** General message with ICON, modal type and all that.... */
    public static void showMessage(java.awt.Window parent, String message) {
        try {
            JOptionPane.showMessageDialog(parent, message);
        } catch (Exception handlerError) {
            System.out.println(handlerError.toString());
            System.out.println(message);
        }
        ;
    }

    /** General error message with ICON, modal type and all that.... */
    public static void showErrorMessageFromException(java.awt.Window parent, Exception error) {
        String errorMessage = new String();
        if (error instanceof KExceptionClass) {
            errorMessage = ((KExceptionClass) error).message;
        } else {
            errorMessage = error.toString();
            Throwable nextError = error;
            while (true) {
                nextError = error.getCause();
                if (nextError == null) break;
                errorMessage += "\n -->" + nextError.toString();
            }
        }
        try {
            JOptionPane.showMessageDialog(parent, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception handlerError) {
            System.out.println(handlerError.toString());
            System.out.println(errorMessage);
        }
        ;
    }

    /** General error message with ICON, modal type and all that.... */
    public static void showErrorMessageFromText1(java.awt.Window parent, String errorMessage) {
        try {
            JOptionPane.showMessageDialog(parent, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception handlerError) {
            System.out.println(handlerError.toString());
            System.out.println(errorMessage);
        }
        ;
    }

    /** General confirmation message with ICON, modal type and all that.... */
    public static String showConfirmationMessage(java.awt.Window parent, String message) {
        String result = "";
        try {
            Object[] options = { "OK", "CANCEL" };
            int n = JOptionPane.showOptionDialog(parent, message, "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n == 0) result = " OK "; else result = " CANCEL ";
        } catch (Exception handlerError) {
            System.out.println(handlerError.toString());
            System.out.println(message);
        }
        ;
        return (result);
    }

    /** General confirmation message with ICON, modal type and all that.... */
    public static String showGetDataMessage(java.awt.Window parent, String title, String message) throws KExceptionClass {
        String result = "";
        result = JOptionPane.showInputDialog(parent, message, title, JOptionPane.QUESTION_MESSAGE);
        return (result);
    }

    /** Generic UI open window */
    public static boolean bringToFront(JDesktopPane desktop, String internalFrameTitle) {
        JInternalFrame[] openInternalFrames = desktop.getAllFrames();
        int totalInternalFramesOpen = java.lang.reflect.Array.getLength(openInternalFrames);
        boolean success = false;
        for (int index = 0; index < totalInternalFramesOpen; index++) {
            if (openInternalFrames[index].getTitle() == internalFrameTitle) {
                if (openInternalFrames[index].isIcon()) {
                    try {
                        openInternalFrames[index].setIcon(false);
                    } catch (java.beans.PropertyVetoException denied) {
                    }
                    ;
                }
                ;
                desktop.moveToFront(openInternalFrames[index]);
                openInternalFrames[index].show();
                success = true;
            }
            ;
        }
        ;
        return (success);
    }

    public static void centerInScreen(Component component) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = component.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        component.setLocation(x, y);
    }

    public static void stopTableCellEditing(JTable visualTable) {
        int column = visualTable.getEditingColumn();
        if (column >= 0) {
            TableCellEditor cellEditor = visualTable.getColumnModel().getColumn(column).getCellEditor();
            if (cellEditor == null) {
                cellEditor = visualTable.getDefaultEditor(visualTable.getColumnClass(column));
            }
            if (cellEditor != null) {
                cellEditor.stopCellEditing();
            }
        }
    }

    public static String stringDateValidation1(String date, String fieldName) throws KExceptionClass {
        String result = "", buf = date.trim();
        if (date.trim().length() != 0) try {
            int index = buf.indexOf(',');
            if (index != -1 && index + 1 < buf.length() && buf.charAt(index + 1) != ' ') {
                buf = buf.substring(0, index + 1) + ' ' + buf.substring(index + 1);
            }
            if ((index = buf.lastIndexOf(' ')) != -1 || (index = buf.lastIndexOf('/')) != -1) {
                if (buf.length() - (index + 1) != 4 && buf.length() - (index + 1) != 2) throw new KExceptionClass("Invalid date ", null);
            }
            DateFormat formatShort = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
            DateFormat formatMedium = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            try {
                formatShort.setLenient(false);
                result = formatMedium.format(formatShort.parse(buf));
            } catch (Exception error) {
                formatMedium.setLenient(false);
                result = formatMedium.format(formatMedium.parse(buf));
            }
        } catch (Exception error) {
            throw new KExceptionClass("***Error*** \n invalid date ---- [" + date + "]!\n" + "Field : [" + fieldName + "]", null);
        }
        return (result);
    }

    public static String timeValidation(String timeString, String fieldName) throws KExceptionClass {
        if (timeString.length() <= 5) {
            String separator = timeString.substring(2, 3);
            int separatorPosition = timeString.indexOf(":");
            if (!(separatorPosition == 1 || separatorPosition == 2)) throw new KExceptionClass("*** Invalid Entry [" + timeString + "] on " + fieldName + "***\n" + "':' Not found .\n" + "[hh24:mm]", null);
            int hour = Integer.parseInt(timeString.substring(0, separatorPosition));
            if (hour > 24 || hour < 0) throw new KExceptionClass("*** Invalid Entry  [" + timeString + "] on " + fieldName + "***\n" + "Invalid Hour.\n" + "[hh24:mm]", null);
            int minute = Integer.parseInt(timeString.substring(separatorPosition + 1, timeString.length()));
            if (minute > 59 || minute < 0) throw new KExceptionClass("*** Invalid Entry  [" + timeString + "] on " + fieldName + "***\n" + "Los minutos son inválidos.\n" + " [hh24:mm]", null);
        } else {
            throw new KExceptionClass("*** Invalid Entry  [" + timeString + "] on " + fieldName + "***\n" + "Invalid Minute.\n" + " [hh24:mm]", null);
        }
        return (timeString);
    }

    public static void setFieldValidation(JTextComponent textComponent, int validation) throws KExceptionClass {
        switch(validation) {
            case DATE_FIELD:
                textComponent.setDocument(new dateDocumentClass());
                break;
            case TIME_FIELD:
                textComponent.setDocument(new timeDocumentClass());
                break;
            default:
                throw new KExceptionClass("***Error*** Cannot apply validation to this type of field.", null);
        }
    }

    public static void setMaxCharacters(JTextComponent textComponent, int maxCharacters) {
        textComponent.setDocument(new limitedCharactersDocumentClass(maxCharacters));
    }

    public static void setMaxNumbers(JTextComponent textComponent, int maxNumbers) {
        textComponent.setDocument(new limitedNumbersDocumentClass(maxNumbers));
        textComponent.setAlignmentY(SwingConstants.RIGHT);
    }

    public static void showInternalFrame(JDesktopPane desktop, JInternalFrame internalFrame) {
        internalFrame.setSize(800, 450);
        desktop.add(internalFrame);
        internalFrame.setLocation(nextInternalFramePosition, nextInternalFramePosition);
        nextInternalFramePosition += 30;
        if (nextInternalFramePosition > 200) nextInternalFramePosition = 0;
        internalFrame.show();
    }

    public static Vector parseCSV(String sourceText) {
        Vector result = new Vector();
        String currentToken = new String();
        boolean insideQuotes = false;
        for (int index = 0; index < sourceText.length(); index++) {
            char currentChar = sourceText.charAt(index);
            if (!insideQuotes) {
                if (currentChar == ',') {
                    result.add(currentToken);
                    currentToken = "";
                } else if (currentChar == '"') {
                    insideQuotes = true;
                } else {
                    currentToken += currentChar;
                }
            } else {
                if (currentChar == '"') {
                    insideQuotes = false;
                } else {
                    currentToken += currentChar;
                }
            }
        }
        result.add(currentToken);
        return (result);
    }

    public static long getIntegralNumericValueFromString(String numberString) throws KExceptionClass {
        return (getIntegralNumericValueFromString(numberString, ""));
    }

    public static long getIntegralNumericValueFromString(String numberString, String fieldName) throws KExceptionClass {
        long result;
        try {
            if (numberString.length() > 0) {
                if (numberString.substring(0, 1).equals("$")) {
                    numberString = numberString.substring(1);
                }
            } else {
                numberString = "0";
            }
            String cleanedString = new String();
            for (int i = 0; i < numberString.length(); i++) {
                char character = numberString.charAt(i);
                if (Character.isDigit(character) || (character == '.') || (character == '-') || (character == 'E')) cleanedString += character;
            }
            result = Long.parseLong(cleanedString);
        } catch (Exception error) {
            throw new KExceptionClass("\n*** Invalid Number. ***\n" + "[" + fieldName + "]", error);
        }
        if (result < -999999999.99 || result > 999999999.99) throw new KExceptionClass("\n*** Invalid Number. ***\n" + "[" + fieldName + "]", null);
        return (result);
    }

    public static double getDecimalNumericValueFromString(String numberString) throws KExceptionClass {
        return (getDecimalNumericValueFromString(numberString, ""));
    }

    public static double getDecimalNumericValueFromString(String numberString, String fieldName) throws KExceptionClass {
        double result;
        try {
            if (numberString.length() > 0) {
                if (numberString.substring(0, 1).equals("$")) {
                    numberString = numberString.substring(1);
                }
            } else {
                numberString = "0";
            }
            String cleanedString = new String();
            boolean isNegative = false;
            for (int i = 0; i < numberString.length(); i++) {
                char character = numberString.charAt(i);
                if (character == '-' || character == '(') isNegative = true;
                if (Character.isDigit(character) || (character == '.') || (character == 'E')) {
                    cleanedString += character;
                }
            }
            if (!isNegative) {
                result = Double.parseDouble(cleanedString);
            } else {
                result = -(Double.parseDouble(cleanedString));
            }
        } catch (Exception error) {
            throw new KExceptionClass("\n*** Invalid Number. ***\n" + "[" + fieldName + "]", error);
        }
        if (result < -999999999.99 || result > 999999999.99) throw new KExceptionClass("\n*** Invalid Number. ***\n" + "[" + fieldName + "]", null);
        return (result);
    }

    public static String toCurrencyString(String value) {
        return (toCurrencyString(Double.parseDouble(value)));
    }

    public static String toCurrencyString(double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return (formatter.format(value));
    }

    public static String toDecimalString(double value, int fractions) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMinimumFractionDigits(fractions);
        formatter.setMaximumFractionDigits(fractions);
        return (formatter.format(value));
    }

    public static int toJavaStaticMonth(int numberOfMonth) throws KExceptionClass {
        int java_static_month = -1;
        switch(numberOfMonth) {
            case 1:
                java_static_month = Calendar.JANUARY;
                break;
            case 2:
                java_static_month = Calendar.FEBRUARY;
                break;
            case 3:
                java_static_month = Calendar.MARCH;
                break;
            case 4:
                java_static_month = Calendar.APRIL;
                break;
            case 5:
                java_static_month = Calendar.MAY;
                break;
            case 6:
                java_static_month = Calendar.JUNE;
                break;
            case 7:
                java_static_month = Calendar.JULY;
                break;
            case 8:
                java_static_month = Calendar.AUGUST;
                break;
            case 9:
                java_static_month = Calendar.SEPTEMBER;
                break;
            case 10:
                java_static_month = Calendar.OCTOBER;
                break;
            case 11:
                java_static_month = Calendar.NOVEMBER;
                break;
            case 12:
                java_static_month = Calendar.DECEMBER;
                break;
            default:
                throw new KExceptionClass("Invalid Month [" + numberOfMonth + "]!", null);
        }
        return java_static_month;
    }

    public static int toNumberOfMonth(int javaStaticMonth) throws KExceptionClass {
        int number_of_month = -1;
        switch(javaStaticMonth) {
            case Calendar.JANUARY:
                number_of_month = 1;
                break;
            case Calendar.FEBRUARY:
                number_of_month = 2;
                break;
            case Calendar.MARCH:
                number_of_month = 3;
                break;
            case Calendar.APRIL:
                number_of_month = 4;
                break;
            case Calendar.MAY:
                number_of_month = 5;
                break;
            case Calendar.JUNE:
                number_of_month = 6;
                break;
            case Calendar.JULY:
                number_of_month = 7;
                break;
            case Calendar.AUGUST:
                number_of_month = 8;
                break;
            case Calendar.SEPTEMBER:
                number_of_month = 9;
                break;
            case Calendar.OCTOBER:
                number_of_month = 10;
                break;
            case Calendar.NOVEMBER:
                number_of_month = 11;
                break;
            case Calendar.DECEMBER:
                number_of_month = 12;
                break;
            default:
                throw new KExceptionClass("Invalid Month [" + javaStaticMonth + "]!", null);
        }
        return (number_of_month);
    }

    public static void cursorWait(Component component) {
        component.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    public static void cursorNormal(Component component) {
        component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public static boolean isContained(long id, String idList) {
        boolean resultado = false;
        String lista[] = idList.split(",");
        for (int i = 0; i < lista.length; i++) {
            if (lista[i].trim().equals(Long.toString(id))) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }

    public static final String[] countries = { "United States", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica", "Cote D'Ivoire", "Croatia (local name: Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France, Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, the former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City State (Holy See)", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis And Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zaire", "Zambia", "Zimbabwe" };

    public static final String[] states = { "N/A", "AL", "AK", "AZ", "AR", "AA", "AE", "AP", "CA", "CO", "CT", "DE", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MP", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VI", "VT", "VA", "WA", "DC", "WV", "WI", "WY" };

    /**  returns the current date millisecond serial.  */
    public static long getCurrentTimeMilliseconds() {
        GregorianCalendar calendar = new GregorianCalendar();
        long result = calendar.getTimeInMillis();
        return (result);
    }

    /**
         * 
         *  Returns the hex dump from an array of bytes
         * 
         * @param noMensaje
         * @return
         */
    public static String byteToHexadecimal(byte[] noMensaje) {
        StringBuffer strMegId = new StringBuffer();
        String strHexa = "";
        if (noMensaje != null) {
            for (int i = 0; i < noMensaje.length; i++) {
                strHexa = Integer.toHexString(noMensaje[i] & 255);
                if (strHexa.length() == 1) strMegId.append("0");
                strMegId.append(strHexa);
            }
        }
        return strMegId.toString();
    }

    /**
         * 
         *  Handy  func that also checks for the string "NULL"
         * 
         */
    public static boolean isEmptyString(String value) {
        boolean bReturn = true;
        if (value != null) {
            if (!value.trim().equalsIgnoreCase("NULL")) {
                if (((value.trim()).length() >= 1)) {
                    bReturn = false;
                }
            }
        }
        return bReturn;
    }
}

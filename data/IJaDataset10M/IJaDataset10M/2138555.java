package er.modern.components;

import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.appserver.ERXApplication;
import er.extensions.appserver.ERXResponseRewriter;
import er.extensions.components.ERXStatelessComponent;
import er.extensions.formatters.ERXTimestampFormatter;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.localization.ERXLocalizer;

/**
 * Wrapper around http://www.frequency-decoder.com/2009/09/09/unobtrusive-date-picker-widget-v5/
 * 
 * Because many options take a date with the format of YYYYMMDD there is a utility method:
 * ERMDatePicker.optionsStringForTimestamp(NSTimestamp ts) that will return a correctly formatted
 * string for a given NSTimestamp.
 * 
 * @binding dateIn an NSTimestamp supplying the value for the field (required)
 * @binding cssFile name of the css file (defaults to datepicker.css)
 * @binding cssFramework name of the framework containing the css file (defaults to ERModernDirectToWeb)
 * @binding dateformat string containing the date format for the field
 * @binding injectStylesheet choose whether to dynamically inject the datepicker.css at component load. 
 * 			if used in a ajax loaded component, it may be safer to load this manually.
 * 
 * See date-picker documentation for following optional values:
 * 
 * @binding hightlightDays string - identifies days to highlight on the calendar (format: [0,0,0,0,0,1,1])
 * @binding rangeLow string - date in format YYYYMMDD defining the lowest selectable date
 * @binding rangeHigh string - date in format YYYYMMDD defining the highest selectable date
 * @binding disabledDays string - identifies days on calendar that are disabled (format: [1,0,1,0,1,0,1])
 * @binding disabledDates string - date range of disabled dates (format: YYYYMMDD:YYYYMMDD or YYYYMMDD if single date)
 * @binding enabledDates string - date range of enabled dates (format: YYYYMMDD:YYYYMMDD or YYYYMMDD if single date)
 * @binding noFadeEffect boolean - disables fade in/out effect
 * @binding finalOpacity number - sets final opacity (20 - 100)
 * @binding showWeeks boolean - show week numbers
 * @binding noTodayButton boolean - hide the "Today" button
 * @binding cursorDate string - date in format YYYYMMDD that sets the default cursor date
 * @binding dragDisabled boolean - disable dragging of calendar
 * @binding fillGrid boolean - fill all dates, not just those in current month
 * @binding constrainSelection boolean - if fillGrid is yes, constrain selection to current month (defaults to true)
 * @binding hideInput boolean - hide the input field (don't use if hideControl is specified)
 * @binding hideControl boolean - hide the calendar button (don't use if hideInput is specified)
 * 
 * @author davidleber
 *
 */
public class ERMDatePicker extends ERXStatelessComponent {

    static final Logger log = Logger.getLogger(ERMDatePicker.class);

    public static final String FRAMEWORK_NAME = "ERModernDirectToWeb";

    public static final String CSS_FILENAME = "datepicker.css";

    private String _elementID;

    private String _openScript;

    private String _createScript;

    public ERMDatePicker(WOContext context) {
        super(context);
    }

    public void awake() {
        super.awake();
    }

    @Override
    public void reset() {
        super.reset();
        _elementID = null;
        _openScript = null;
        _createScript = null;
    }

    public NSTimestamp value() {
        return (NSTimestamp) objectValueForBinding("value");
    }

    public void setValue(NSTimestamp newDateIn) {
        setValueForBinding(newDateIn, "value");
    }

    /**
     * Adds date-picker.js to the header or includes it in an Ajax friendly manner.
     *
     * @see er.extensions.components.ERXNonSynchronizingComponent#appendToResponse(com.webobjects.appserver.WOResponse, com.webobjects.appserver.WOContext)
     * @see ERXResponseRewriter#addScriptResourceInHead(WOResponse, WOContext, String, String)
     */
    public void appendToResponse(WOResponse response, WOContext context) {
        if (booleanValueForBinding("injectStylesheet")) {
            String framework = stringValueForBinding("cssFramework", FRAMEWORK_NAME);
            String cssFilename = stringValueForBinding("cssFile", CSS_FILENAME);
            ERXResponseRewriter.addStylesheetResourceInHead(response, context, framework, cssFilename);
        }
        String datepickerjsName = ERXApplication.isDevelopmentModeSafe() ? "datepicker_lg.js" : "datepicker.js";
        ERXResponseRewriter.addScriptResourceInHead(response, context, FRAMEWORK_NAME, datepickerjsName);
        String langScript = ERXLocalizer.currentLocalizer().languageCode() + ".js";
        ERXResponseRewriter.addScriptResourceInHead(response, context, FRAMEWORK_NAME, "lang/" + langScript);
        super.appendToResponse(response, context);
    }

    public String dateformat() {
        String format = (String) stringValueForBinding("dateformat");
        if (format == null) {
            format = ERXTimestampFormatter.DEFAULT_PATTERN;
        }
        return format;
    }

    @Override
    public String name() {
        return elementID();
    }

    public void setDateformat(String value) {
        setValueForBinding(value, "dateformat");
    }

    /**
	 * 
	 * @return
	 */
    public String dateFormatString() {
        String result = dateformat();
        result = ERXStringUtilities.replaceStringByStringInString("-", "-ds", result);
        result = ERXStringUtilities.replaceStringByStringInString("%a", "-D", result);
        result = ERXStringUtilities.replaceStringByStringInString("%A", "-l", result);
        result = ERXStringUtilities.replaceStringByStringInString("%b", "-M", result);
        result = ERXStringUtilities.replaceStringByStringInString("%B", "-F", result);
        result = ERXStringUtilities.replaceStringByStringInString("%d", "-d", result);
        result = ERXStringUtilities.replaceStringByStringInString("%e", "-j", result);
        result = ERXStringUtilities.replaceStringByStringInString("%m", "-m", result);
        result = ERXStringUtilities.replaceStringByStringInString("%y", "-y", result);
        result = ERXStringUtilities.replaceStringByStringInString("%Y", "-Y", result);
        result = ERXStringUtilities.replaceStringByStringInString("%w", "-w", result);
        result = ERXStringUtilities.replaceStringByStringInString(" ", "-sp", result);
        result = ERXStringUtilities.replaceStringByStringInString(".", "-dt", result);
        result = ERXStringUtilities.replaceStringByStringInString("/", "-sl", result);
        result = ERXStringUtilities.replaceStringByStringInString(",", "-cc", result);
        if (result.indexOf("-") == 0) {
            result = result.substring(1);
        }
        return result;
    }

    public String datePickerCreateScript() {
        if (_createScript == null) {
            _createScript = "datePickerController.destroyDatePicker('" + elementID() + "'); datePickerController.createDatePicker(" + datePickerOptions() + ")";
        }
        log.debug(_createScript);
        return _createScript;
    }

    private String datePickerOptions() {
        String opts = "{";
        opts += "formElements:{'" + elementID() + "':'" + dateFormatString() + "'}";
        String highlightDays = stringValueForBinding("highlightDays");
        if (ne(highlightDays)) opts += ",highlightDays:" + highlightDays;
        String rangeLow = stringValueForBinding("rangeLow");
        if (ne(rangeLow)) opts += ",rangeLow:'" + rangeLow + "'";
        String rangeHigh = stringValueForBinding("rangeHigh");
        if (ne(rangeHigh)) opts += ",rangeHigh:'" + rangeHigh + "'";
        String disabledDays = stringValueForBinding("disabledDays");
        if (ne(disabledDays)) opts += ",disabledDays:" + disabledDays;
        String disabledDates = stringValueForBinding("disabledDates");
        if (ne(disabledDates)) {
            opts += ",disabledDates:" + parseDateRangeString(disabledDates);
        }
        String enabledDates = stringValueForBinding("enabledDates");
        if (ne(enabledDates)) opts += ",enabledDates:" + parseDateRangeString(enabledDates);
        boolean disableFade = booleanValueForBinding("noFadeEffect", false);
        if (disableFade) opts += ",noFadeEffect:true";
        int finalOpacity = intValueForBinding("finalOpacity", 0);
        if (finalOpacity > 20) opts += ",finalOpacity:" + finalOpacity;
        boolean showWeeks = booleanValueForBinding("showWeeks", false);
        if (showWeeks) opts += ",showWeeks:true";
        boolean noTodayButton = booleanValueForBinding("noTodayButton", false);
        if (noTodayButton) opts += ",noTodayButton:true";
        String cursorDate = stringValueForBinding("cursorDate");
        if (ne(cursorDate)) opts += ",cursorDate:'" + cursorDate + "'";
        boolean noDrag = booleanValueForBinding("dragDisabled", false);
        if (noDrag) opts += ",dragDisabled:true";
        boolean hideInput = booleanValueForBinding("hideInput", false);
        if (hideInput) opts += ",hideInput:true";
        boolean fillGrid = booleanValueForBinding("fillGrid", false);
        if (fillGrid) {
            opts += ",fillGrid:true";
            boolean constrainSelection = booleanValueForBinding("constrainSelection", true);
            opts += ",constrainSelection:" + constrainSelection;
        }
        boolean hideControl = booleanValueForBinding("hideControl", false);
        if (hideControl) opts += ",hideControl:true";
        opts += "}";
        return opts;
    }

    public String datePickerOpenScript() {
        if (_openScript == null) {
            _openScript = "datePickerController.show('" + elementID() + "');";
        }
        return _openScript;
    }

    public String elementID() {
        if (_elementID == null) {
            _elementID = ERXStringUtilities.safeIdentifierName(context().elementID(), "datebox");
        }
        return _elementID;
    }

    private String parseDateRangeString(String dateRange) {
        String result = "";
        if (dateRange.indexOf(":") > 0) {
            NSArray<String> components = NSArray.componentsSeparatedByString(dateRange, ":");
            String firstDate = "'" + components.objectAtIndex(0) + "'";
            String lastDate = components.objectAtIndex(1);
            if (!lastDate.equals("1")) lastDate = "'" + lastDate + "'";
            result = "{" + firstDate + ":" + lastDate + "}";
        } else {
            result = "{'" + dateRange + "':1}";
        }
        return result;
    }

    private boolean ne(String v) {
        return v != null && v.length() > 0;
    }

    public static String optionsStringForTimestamp(NSTimestamp ts) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(ts);
    }
}

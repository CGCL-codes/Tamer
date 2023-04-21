package net.sf.boincecho;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import net.sf.boincecho.boinc.UserComparator.ComparisonField;
import net.sf.metagloss.preference.AnnotatedPreferenceActivity;
import net.sf.metagloss.preference.BindPreference;
import net.sf.metagloss.preference.BooleanString;
import net.sf.metagloss.preference.IntegerConstraint;
import net.sf.metagloss.preference.PreferenceResource;

@PreferenceResource(resource = R.xml.preferences, binds = { @BindPreference(key = "chart_fill_below", format = "Area below chart line is %sfilled."), @BindPreference(key = "chart_fill_transparency", format = "%s%% transparent"), @BindPreference(key = "chart_zoom_data", format = "Y-axis %s"), @BindPreference(key = "thousand_separator", format = "Delimiter: %s"), @BindPreference(key = "update_interval", format = "Updates every %s hours"), @BindPreference(key = "user_order_direction") }, integerConstraints = { @IntegerConstraint(key = "chart_fill_transparency", max = 100), @IntegerConstraint(key = "update_interval", min = 3, max = 100) }, booleanStrings = { @BooleanString(key = "chart_fill_below", checked = "", unchecked = "not "), @BooleanString(key = "chart_zoom_data", checked = "matches data values.", unchecked = "starts at 0."), @BooleanString(key = "user_order_direction", checked = "descending", unchecked = "ascending") })
public class ApplicationSettings extends AnnotatedPreferenceActivity {

    public static final String PREF_SCREEN_SORT = "sort_preference";

    public static final String PREF_SCREEN_CHART = "chart_preference";

    public static final String PREF_THOUSAND_SEPARATOR = "thousand_separator";

    public static final String PREF_DATE_FORMAT = "date_format";

    @BindPreference(format = "Delimiter: %s", fireChange = { PREF_DATE_FORMAT })
    public static final String PREF_DATE_FORMAT_SEPARATOR = "date_format_separator";

    public static final String PREF_PROJECT_UPDATE_INTERVAL = "update_interval";

    @BindPreference
    public static final String PREF_USER_ORDER_BY = "user_order_by";

    public static final String PREF_USER_ORDER_REVERSE = "user_order_direction";

    public static final String PREF_CHART_ZOOM_DATA = "chart_zoom_data";

    public static final String PREF_CHART_FILL_BELOW = "chart_fill_below";

    public static final String PREF_CHART_TRANSPARENCY = "chart_fill_transparency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListPreference orderBy = (ListPreference) findPreference(PREF_USER_ORDER_BY);
        if (orderBy != null) {
            orderBy.setEntries(ComparisonField.getLabels());
            orderBy.setEntryValues(ComparisonField.getIdentifiers());
            orderBy.setDefaultValue(ComparisonField.ProjectName.toString());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);
        Preference preference = findPreference(key);
        if (key.equals(PREF_DATE_FORMAT)) {
            ListPreference dateFormatList = (ListPreference) findPreference(PREF_DATE_FORMAT_SEPARATOR);
            String delim = dateFormatList.getEntry().toString();
            String summary = ((ListPreference) preference).getEntry().toString();
            if (delim.length() == 0) {
                delim = " ";
            }
            preference.setSummary(summary.replace('-', delim.charAt(0)));
        }
    }
}

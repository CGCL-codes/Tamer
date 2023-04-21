package de.wadndadn.playgolfhere;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * TODO Document.
 * 
 * @author <a href="http://schubertch.users.sourceforge.net/">SchubertCh</a>
 * 
 * @since 1.0
 */
public class PlayGolfHerePreferencesActivity extends PreferenceActivity {

    /**
	 * {@inheritDoc}
	 * 
	 * Called when the activity is first created.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.play_golf_here_preferences);
    }
}

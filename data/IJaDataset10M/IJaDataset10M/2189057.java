package com.loribel.commons.abstraction.preference;

import java.io.IOException;

/**
 * Writer and laoder of Preferences.
 * @author Gregory Borelli
 */
public interface GB_PreferenceMgrWriter {

    void loadPreferences(GB_PreferenceMgr a_preferenceMgr) throws IOException;

    void savePreferences(GB_PreferenceMgr a_preferenceMgr) throws IOException;
}

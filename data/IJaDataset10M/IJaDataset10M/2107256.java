package org.rapla.storage.xml;

import java.io.IOException;
import java.util.Iterator;
import org.rapla.entities.RaplaObject;
import org.rapla.entities.RaplaType;
import org.rapla.entities.User;
import org.rapla.entities.configuration.Preferences;
import org.rapla.entities.configuration.internal.PreferencesImpl;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

public class PreferenceWriter extends RaplaXMLWriter {

    public static final String WRITERMAP = PreferenceWriter.class.getPackage().getName() + ".writerMap";

    public PreferenceWriter(RaplaContext sm) throws RaplaException {
        super(sm);
    }

    protected void printPreferences(Preferences preferences) throws IOException, RaplaException {
        if (!preferences.isEmpty()) {
            openTag("rapla:preferences");
            if (isIdOnly()) {
                att("id", getId(preferences));
                User owner = preferences.getOwner();
                if (owner != null) {
                    att("ownerid", getId(owner));
                }
            }
            printVersion(preferences);
            closeTag();
            Iterator it = ((PreferencesImpl) preferences).getPreferenceEntries();
            while (it.hasNext()) {
                String role = (String) it.next();
                Object entry = preferences.getEntry(role);
                if (entry instanceof String) {
                    openTag("rapla:entry");
                    att("key", role);
                    att("value", (String) entry);
                    closeElementTag();
                }
                if (entry instanceof RaplaObject) {
                    openTag("rapla:entry");
                    att("key", role);
                    closeTag();
                    RaplaType raplaType = ((RaplaObject) entry).getRaplaType();
                    RaplaXMLWriter writer = getWriterFor(raplaType);
                    writer.writeObject((RaplaObject) entry);
                    closeElement("rapla:entry");
                }
            }
            closeElement("rapla:preferences");
        }
    }

    public void writeObject(RaplaObject object) throws IOException, RaplaException {
        printPreferences((Preferences) object);
    }
}

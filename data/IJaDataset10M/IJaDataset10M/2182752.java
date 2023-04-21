package com.softwaresmithy.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.softwaresmithy.R;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;

public class LibraryFactory {

    private String className;

    private Node libNode;

    public LibraryFactory(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String libName = prefs.getString(context.getString(R.string.pref_key_libChoice), null);
        String stateName = prefs.getString(context.getString(R.string.pref_key_stateName), null);
        if (libName != null && stateName != null) {
            File libXml = new File(context.getExternalFilesDir(null), "libraries.xml");
            if (libXml.exists()) {
                try {
                    InputSource source = new InputSource(new FileInputStream(libXml));
                    XPath newXPath = XPathFactory.newInstance().newXPath();
                    String libNodeXpath = "/xml/library[state='" + stateName + "' and name='" + libName + "']";
                    libNode = (Node) newXPath.evaluate(libNodeXpath, source, XPathConstants.NODE);
                    className = newXPath.evaluate("class", libNode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else throw new RuntimeException("Library implementation not found in Preferences");
    }

    public Library getLibrary() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Library clazz = (Library) Class.forName(className).newInstance();
        clazz.parseNode(libNode);
        return clazz;
    }
}

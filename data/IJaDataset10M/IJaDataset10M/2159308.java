package org.deft.export.xslt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.osgi.framework.Bundle;

public class XsltExporterHelper {

    private PreferenceStore prefs = null;

    public XsltExporterHelper(String name, Map<String, String> defaults) {
        File prefFile = new Path(getBundlePath()).append(name).toFile();
        this.prefs = new PreferenceStore(prefFile.getAbsolutePath());
        try {
            if (!prefFile.exists()) {
                prefFile.createNewFile();
            }
            prefs.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (defaults != null) {
            for (String k : defaults.keySet()) {
                if (!prefs.contains(k)) {
                    prefs.putValue(k, defaults.get(k));
                }
            }
        }
    }

    public XsltExporterHelper(String name) {
        this(name, null);
    }

    public String get(String key) {
        return prefs.getString(key);
    }

    public void set(String key, String value) {
        prefs.setValue(key, value);
    }

    public void save() {
        try {
            if (prefs.needsSaving()) {
                prefs.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getBundlePath() {
        Bundle bundle = Platform.getBundle("org.deft.export.xslt");
        URL bundleUrl = bundle.getEntry("");
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.resolve(bundleUrl);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return fileUrl.getPath();
    }

    /**
	 * Helper method which creates a group with cols columns
	 * @param groupText the text for the group widget
	 * @param parent the parent composite widget
	 */
    public static Group createGroup(String groupText, int cols, Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = cols;
        GridData groupGridData = new GridData();
        groupGridData.horizontalAlignment = GridData.FILL;
        groupGridData.grabExcessHorizontalSpace = true;
        groupGridData.verticalAlignment = GridData.CENTER;
        group.setText(groupText);
        group.setLayout(gridLayout);
        group.setLayoutData(groupGridData);
        return group;
    }
}

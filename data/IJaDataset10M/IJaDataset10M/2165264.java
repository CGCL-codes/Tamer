package org.xaware.schemanavigator.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * PreferenceStore that uses the CategoryList as its data backing source. This was required to allow deleting and
 * modifying of properties. It uses the Properties class to persist the data to disk.
 * 
 */
public class SchemaNavPreferenceStore implements IPreferenceStore {

    private String propertyFilename;

    private CategoryList categoryList;

    private boolean hasLoaded;

    private static Vector listeners;

    public static String SCHEMA_NAV_PROPERTY_NAME = "SchemaNavPreferenceStore";

    public SchemaNavPreferenceStore(final String schemaNavPreferenceFileName) {
        propertyFilename = schemaNavPreferenceFileName;
        categoryList = new CategoryList();
    }

    public boolean contains(final String name) {
        return false;
    }

    public void firePropertyChangeEvent(final String name, final Object oldValue, final Object newValue) {
    }

    public boolean getBoolean(final String name) {
        return false;
    }

    public boolean getDefaultBoolean(final String name) {
        return false;
    }

    public double getDefaultDouble(final String name) {
        return 0;
    }

    public float getDefaultFloat(final String name) {
        return 0;
    }

    public int getDefaultInt(final String name) {
        return 0;
    }

    public long getDefaultLong(final String name) {
        return 0;
    }

    public String getDefaultString(final String name) {
        return null;
    }

    public double getDouble(final String name) {
        return 0;
    }

    public float getFloat(final String name) {
        return 0;
    }

    public int getInt(final String name) {
        return 0;
    }

    public long getLong(final String name) {
        return 0;
    }

    /**
     * Return the location matching the name description passed in by the name parameter. Otherwise return null.
     */
    public String getString(final String name) {
        final Vector data = categoryList.getLocationData();
        final int size = data.size();
        for (int i = 0; i < size; i++) {
            final CategoryCellData cd = (CategoryCellData) data.get(i);
            if (name.equals(cd.getDescription())) {
                return cd.getLocation();
            }
        }
        return null;
    }

    public boolean isDefault(final String name) {
        return false;
    }

    public boolean needsSaving() {
        return false;
    }

    public void putValue(final String name, final String value) {
    }

    public void removePropertyChangeListener(final IPropertyChangeListener listener) {
        final int size = listeners.size();
        for (int i = 0; i < size; i++) {
            if (listeners.get(i).equals(listener)) {
                listeners.remove(i);
            }
        }
    }

    public void setDefault(final String name, final double value) {
    }

    public void setDefault(final String name, final float value) {
    }

    public void setDefault(final String name, final int value) {
    }

    public void setDefault(final String name, final long value) {
    }

    public void setDefault(final String name, final String defaultObject) {
    }

    public void setDefault(final String name, final boolean value) {
    }

    public void setToDefault(final String name) {
    }

    public void setValue(final String name, final double value) {
    }

    public void setValue(final String name, final float value) {
    }

    public void setValue(final String name, final int value) {
    }

    public void setValue(final String name, final long value) {
    }

    /**
     * add category to categoryList
     */
    public void setValue(final String name, final String value) {
        categoryList.addCategory(name, value);
    }

    public void setValue(final String name, final boolean value) {
    }

    public void removeCategory(final String category) {
        final CategoryCellData data = categoryList.getCategory(category);
        if (data != null) {
            categoryList.removeCategory(data);
        }
    }

    public void renameCategory(final String category, final String newName) {
        categoryList.renameCategory(category, newName);
    }

    /**
     * read a file one line at a time adding to an ArrayList. Return the ArrayList
     * 
     * @param fileName
     * @return
     */
    public static ArrayList readFile(final String fileName) {
        String line = "";
        final ArrayList data = new ArrayList();
        try {
            final FileReader fr = new FileReader(fileName);
            final BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (final FileNotFoundException fN) {
            fN.printStackTrace();
        } catch (final IOException e) {
            System.out.println(e);
        }
        return data;
    }

    /**
     * Read the data in and load the CategoryList. The CategoryList is returned for use in updating the data.
     * 
     * @return
     */
    public CategoryList load() {
        if (hasLoaded) {
            return categoryList;
        }
        hasLoaded = true;
        final ArrayList catagories = readFile(propertyFilename);
        final int size = catagories.size();
        for (int i = 0; i < size; i++) {
            final String line = (String) catagories.get(i);
            if (line.startsWith("#")) {
                continue;
            }
            final StringTokenizer tokenizer = new StringTokenizer(line, "=");
            if (tokenizer.countTokens() == 2) {
                final String name = (String) tokenizer.nextElement();
                final String loc = (String) tokenizer.nextElement();
                ;
                categoryList.addCategory(name, loc);
            } else {
                System.out.println("Unable to parse line: " + line);
            }
        }
        return categoryList;
    }

    /**
     * returns all descriptive names that can be used to retrieve data from the store.
     * 
     * @return
     */
    public String[] preferenceNames() {
        final Vector data = categoryList.getLocationData();
        final int size = data.size();
        final String[] nameArr = new String[size];
        for (int i = 0; i < size; i++) {
            final CategoryCellData cd = (CategoryCellData) data.get(i);
            nameArr[i] = cd.getDescription();
        }
        return nameArr;
    }

    /**
     * Write the data to disk. This builds a Properties class using the data in the CategoryList to write the data to
     * disk in the properties layout key=value.
     * 
     */
    public void save() {
        final File propFile = new File(propertyFilename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(propFile);
            final String header = "#Schema Navigator Preferences\n";
            fos.write(header.getBytes());
            final Vector data = categoryList.getLocationData();
            final int size = data.size();
            for (int i = 0; i < size; i++) {
                final CategoryCellData cd = (CategoryCellData) data.get(i);
                final String locFile = cd.getLocation();
                final String outCat = cd.getDescription() + "=" + locFile + "\n";
                fos.write(outCat.getBytes());
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * loop thru all listeners calling propertyChange
     * 
     */
    public void notifyListeners() {
        if (listeners != null) {
            final int size = listeners.size();
            for (int i = 0; i < size; i++) {
                final IPropertyChangeListener listener = (IPropertyChangeListener) listeners.get(i);
                final PropertyChangeEvent event = new PropertyChangeEvent(categoryList, SCHEMA_NAV_PROPERTY_NAME, "", "");
                listener.propertyChange(event);
            }
        }
    }

    public void addPropertyChangeListener(final IPropertyChangeListener listener) {
        if (listeners == null) {
            listeners = new Vector(10);
        }
        listeners.add(listener);
    }
}

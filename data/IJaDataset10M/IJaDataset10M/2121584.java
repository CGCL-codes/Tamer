package org.androidsoft.app.permission.model;

import android.graphics.drawable.Drawable;

/**
 * Application Info object
 * @author Pierre Levy
 */
public class AppInfo {

    private String _name;

    private String _packageName;

    private String _version;

    private Drawable _icon;

    private int _score;

    private boolean _trusted;

    /**
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return _version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        _version = version;
    }

    /**
     * @return the icon
     */
    public Drawable getIcon() {
        return _icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Drawable icon) {
        _icon = icon;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return _score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        _score = score;
    }

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return _packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName) {
        _packageName = packageName;
    }

    public boolean isTrusted() {
        return _trusted;
    }

    public void setTrusted(boolean trusted) {
        _trusted = trusted;
    }
}

package Deroid.FirstUI;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Represents a launchable application. An application is made of a name (or title), an intent
 * and an icon.
 */
class ApplicationInfo {

    CharSequence title;

    Intent intent;

    Drawable icon;

    boolean filtered;

    final void setActivity(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationInfo)) {
            return false;
        }
        ApplicationInfo that = (ApplicationInfo) o;
        return title.equals(that.title) && intent.getComponent().getClassName().equals(that.intent.getComponent().getClassName());
    }

    @Override
    public int hashCode() {
        int result;
        result = (title != null ? title.hashCode() : 0);
        final String name = intent.getComponent().getClassName();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}

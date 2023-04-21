package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.sdklib.IAndroidTarget;
import org.eclipse.swt.graphics.Image;

/**
 * Resource Qualifier for Touch Screen type.
 */
public final class TouchScreenQualifier extends ResourceQualifier {

    public static final String NAME = "Touch Screen";

    private TouchScreenType mValue;

    /**
     * Screen Orientation enum.
     */
    public static enum TouchScreenType {

        NOTOUCH("notouch", "No Touch"), STYLUS("stylus", "Stylus"), FINGER("finger", "Finger");

        private String mValue;

        private String mDisplayValue;

        private TouchScreenType(String value, String displayValue) {
            mValue = value;
            mDisplayValue = displayValue;
        }

        /**
         * Returns the enum for matching the provided qualifier value.
         * @param value The qualifier value.
         * @return the enum for the qualifier value or null if no matching was found.
         */
        public static TouchScreenType getEnum(String value) {
            for (TouchScreenType orient : values()) {
                if (orient.mValue.equals(value)) {
                    return orient;
                }
            }
            return null;
        }

        public String getValue() {
            return mValue;
        }

        public String getDisplayValue() {
            return mDisplayValue;
        }

        public static int getIndex(TouchScreenType touch) {
            int i = 0;
            for (TouchScreenType t : values()) {
                if (t == touch) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public static TouchScreenType getByIndex(int index) {
            int i = 0;
            for (TouchScreenType value : values()) {
                if (i == index) {
                    return value;
                }
                i++;
            }
            return null;
        }
    }

    public TouchScreenQualifier() {
    }

    public TouchScreenQualifier(TouchScreenType touchValue) {
        mValue = touchValue;
    }

    public TouchScreenType getValue() {
        return mValue;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortName() {
        return NAME;
    }

    @Override
    public Image getIcon() {
        return IconFactory.getInstance().getIcon("touch");
    }

    @Override
    public boolean isValid() {
        return mValue != null;
    }

    @Override
    public boolean checkAndSet(String value, FolderConfiguration config) {
        TouchScreenType type = TouchScreenType.getEnum(value);
        if (type != null) {
            TouchScreenQualifier qualifier = new TouchScreenQualifier();
            qualifier.mValue = type;
            config.setTouchTypeQualifier(qualifier);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object qualifier) {
        if (qualifier instanceof TouchScreenQualifier) {
            return mValue == ((TouchScreenQualifier) qualifier).mValue;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (mValue != null) {
            return mValue.hashCode();
        }
        return 0;
    }

    /**
     * Returns the string used to represent this qualifier in the folder name.
     */
    @Override
    public String getFolderSegment(IAndroidTarget target) {
        if (mValue != null) {
            return mValue.getValue();
        }
        return "";
    }

    @Override
    public String getStringValue() {
        if (mValue != null) {
            return mValue.getDisplayValue();
        }
        return "";
    }
}

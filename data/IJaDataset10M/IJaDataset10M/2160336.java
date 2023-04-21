package net.sf.mpxj;

/**
 * Instances of this class represent enumerated file version values.
 */
public final class FileVersion {

    /**
    * Private constructor.
    *
    * @param value file version value
    */
    private FileVersion(int value) {
        m_value = value;
    }

    /**
    * Retrieves the int representation of the file version.
    *
    * @return file version value
    */
    public int getValue() {
        return (m_value);
    }

    /**
    * Retrieve a FileVersion instance representing the supplied value.
    *
    * @param value file version value
    * @return FileVersion instance
    */
    public static FileVersion getInstance(String value) {
        FileVersion result = VERSION_4_0;
        if (value != null) {
            if (value.startsWith("4") == false) {
                if (value.startsWith("3") == true) {
                    result = VERSION_3_0;
                } else {
                    if (value.startsWith("1") == true) {
                        result = VERSION_1_0;
                    }
                }
            }
        }
        return (result);
    }

    /**
    * Retrieve the string representation of this file type.
    *
    * @return string representation of the file type
    */
    @Override
    public String toString() {
        String result;
        switch(m_value) {
            case 1:
                {
                    result = "1.0";
                    break;
                }
            case 3:
                {
                    result = "3.0";
                    break;
                }
            default:
            case 4:
                {
                    result = "4.0";
                    break;
                }
        }
        return (result);
    }

    private int m_value;

    /**
    * Constant representing file version.
    */
    public static final FileVersion VERSION_1_0 = new FileVersion(1);

    /**
    * Constant representing file version.
    */
    public static final FileVersion VERSION_3_0 = new FileVersion(3);

    /**
    * Constant representing file version.
    */
    public static final FileVersion VERSION_4_0 = new FileVersion(4);
}

package biz.xsoftware.mock;

/**
 * Miscellaneous class that just prints the version of the mock object jar
 * getting it from the manifest file.
 * 
 * @author Dean Hiller
 */
public class MockVersion {

    private Package thePackage;

    /**
	 * The main program for MockVersion that prints the version info from 
	 * the manifest file.
	 * 
	 * @param args Ignores all arguments.
	 */
    public static void main(String[] args) {
        MockVersion version = new MockVersion(MockVersion.class);
        System.out.println("" + version);
    }

    /**
	 * Constructor that takes a class to get the version information
	 * from out of the manifest.  Uses the class's package to retrieve
	 * the manifest version info.
	 * @param c The Class on whose package to use to get version info.
	 */
    public MockVersion(Class c) {
        String name = c.getName();
        int index = name.lastIndexOf(".");
        if (index < 0) throw new RuntimeException("This class is the default package and can't be to use this feature");
        String packageName = name.substring(0, index);
        thePackage = Package.getPackage(packageName);
    }

    /**
	 * Prints the version info the MockVersion represents.
	 * 
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        String version = "\nVersion of MockObject...";
        version += "\ntitle=" + thePackage.getImplementationTitle();
        version += "\nwebsite=" + thePackage.getImplementationVendor();
        version += "\nversion=" + thePackage.getImplementationVersion() + "\n";
        return version;
    }
}

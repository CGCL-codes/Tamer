package grammarscope.common;

/**
 * Java version
 * 
 * @author Bernard Bou
 */
public class JavaVersion {

    /**
	 * The names of properties
	 */
    private static String[] theNames = new String[] { "java.version", "java.vendor", "java.vendor.url", "java.specification.name", "java.specification.version", "java.specification.vendor", "java.vm.name", "java.vm.version", "java.vm.vendor", "java.vm.specification.name", "java.vm.specification.version", "java.vm.specification.vendor" };

    /**
	 * Get property values
	 * 
	 * @return string array of name=value pairs
	 */
    public static String[] getJavaProps() {
        final String[] thisResult = new String[JavaVersion.theNames.length];
        String thisProp;
        for (int i = 0; i < JavaVersion.theNames.length; i++) {
            thisProp = JavaVersion.theNames[i] + " : ";
            try {
                thisProp += System.getProperty(JavaVersion.theNames[i]);
            } catch (final SecurityException e) {
                thisProp += "<protected>";
            } catch (final Exception e) {
                thisProp += "<>";
            }
            thisResult[i] = thisProp;
        }
        return thisResult;
    }

    /**
	 * Get property values as string
	 * 
	 * @return s string
	 */
    public static String getJavaPropsString() {
        final String[] theseStrings = JavaVersion.getJavaProps();
        final StringBuffer thisBuffer = new StringBuffer();
        for (final String thisString : theseStrings) {
            thisBuffer.append(thisString);
            thisBuffer.append("\n");
        }
        return thisBuffer.toString();
    }

    /**
	 * Get property values as string
	 * 
	 * @return s string
	 */
    @SuppressWarnings("boxing")
    public static String getJavaMemory() {
        final long heapSize = Runtime.getRuntime().totalMemory();
        final long heapFreeSize = Runtime.getRuntime().freeMemory();
        final long heapMaxSize = Runtime.getRuntime().maxMemory();
        final StringBuffer thisBuffer = new StringBuffer();
        thisBuffer.append(String.format("heap.size : %d\n", heapSize));
        thisBuffer.append(String.format("heap.free : %d\n", heapFreeSize));
        thisBuffer.append(String.format("heap.max : %d\n", heapMaxSize));
        return thisBuffer.toString();
    }

    /**
	 * Dependencies
	 * 
	 * @param theseArgs
	 *            arguments
	 */
    public static void main(final String[] theseArgs) {
        System.out.println(JavaVersion.getJavaPropsString());
        System.out.println(JavaVersion.getJavaMemory());
    }
}

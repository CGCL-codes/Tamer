package dakside.reports;

/**
 *
 * @author takaji
 */
public class ReportHelper {

    /**
     * Validate arguments
     * @param args
     * @param types
     * @throws ReportException
     */
    public static void validateArguments(Object[] args, Class... types) throws ReportException {
        if (args == null) {
            throw new ReportException("Invalid argument(s): Null array passed in.");
        } else if (args.length != types.length) {
            throw new ReportException("Invalid argument(s) count.");
        } else {
            for (int i = 0; i < types.length; i++) {
                Class type = types[i];
                if (!type.isInstance(args[i])) {
                    throw new ReportException("Invalid argument type at index " + i);
                }
            }
        }
    }
}

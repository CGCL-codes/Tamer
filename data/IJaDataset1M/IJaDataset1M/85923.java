package pcgen.util;

/**
 * A Factory class that generates Input Interfaces
 */
public class InputFactory {

    private static String interfaceClassname = null;

    /**
	 * Deliberately private so it can't be instantiated.
	 */
    private InputFactory() {
    }

    /**
	 * The default implementation returns a SwingChooser
	 * @return InputInterface
	 */
    public static InputInterface getInputInstance() {
        try {
            Class c = Class.forName(interfaceClassname);
            InputInterface ci = (InputInterface) c.newInstance();
            return ci;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param interfaceClassname The interfaceClassname to set.
     */
    public static void setInterfaceClassname(String interfaceClassname) {
        InputFactory.interfaceClassname = interfaceClassname;
    }
}

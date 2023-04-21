package fr.soleil.mambo.datasources.tango.alternate;

public class TangoAlternateSelectionManagerFactory {

    public static final int DUMMY = 1;

    public static final int BASIC = 2;

    public static final int BUFFERED = 3;

    public static final int ORDERED = 4;

    public static final int BUFFERED_AND_ORDERED = 5;

    private static ITangoAlternateSelectionManager currentImpl = null;

    private static boolean canBeBuffered = true;

    /**
     * @param typeOfImpl
     * @return 8 juil. 2005
     */
    public static ITangoAlternateSelectionManager getImpl(int typeOfImpl) {
        switch(typeOfImpl) {
            case DUMMY:
                currentImpl = new DummyTangoAlternateSelectionManagerImpl();
                break;
            case BASIC:
                currentImpl = new BasicTangoAlternateSelectionManager();
                break;
            case ORDERED:
                currentImpl = new OrderedTangoAlternateSelectionManager(new BasicTangoAlternateSelectionManager());
                break;
            case BUFFERED:
                if (canBeBuffered) {
                    currentImpl = new ThreadedBufferedTangoAlternateSelectionManager(new BasicTangoAlternateSelectionManager());
                } else {
                    currentImpl = new BasicTangoAlternateSelectionManager();
                }
                break;
            case BUFFERED_AND_ORDERED:
                if (canBeBuffered) {
                    currentImpl = new OrderedTangoAlternateSelectionManager(new ThreadedBufferedTangoAlternateSelectionManager(new BasicTangoAlternateSelectionManager()));
                } else {
                    currentImpl = new OrderedTangoAlternateSelectionManager(new BasicTangoAlternateSelectionManager());
                }
                break;
            default:
                throw new IllegalStateException("Expected either DUMMY(1) or BASIC(2) or BUFFERED(3) or ORDERED(4) or BUFFERED_AND_ORDERED(5), got " + typeOfImpl + " instead.");
        }
        return currentImpl;
    }

    /**
     * @return 28 juin 2005
     */
    public static ITangoAlternateSelectionManager getCurrentImpl() {
        return currentImpl;
    }

    public static void setBuffered(boolean _canBeBuffered) {
        TangoAlternateSelectionManagerFactory.canBeBuffered = _canBeBuffered;
    }
}

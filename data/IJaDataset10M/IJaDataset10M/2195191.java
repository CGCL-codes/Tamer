package nuts.core.images;

/**
 */
public abstract class ImageFactory {

    private static ImageFactory instance;

    /**
	 * @return the instance
	 */
    public static ImageFactory getInstance() {
        if (instance == null) {
            instance = new JavaImageFactory();
        }
        return instance;
    }

    /**
	 * @param instance the instance to set
	 */
    public static void setInstance(ImageFactory instance) {
        ImageFactory.instance = instance;
    }

    public abstract ImageWrapper makeImage(byte[] data) throws Exception;
}

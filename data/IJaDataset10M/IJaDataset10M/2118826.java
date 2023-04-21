package net.pms.formats;

public class TIF extends JPG {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String[] getId() {
        return new String[] { "tif", "tiff" };
    }

    @Override
    public String mimeType() {
        return "image/tiff";
    }
}

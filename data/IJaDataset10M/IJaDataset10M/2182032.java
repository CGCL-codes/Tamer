package au.gov.naa.digipres.xena.plugin.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import au.gov.naa.digipres.xena.kernel.properties.InvalidPropertyException;
import au.gov.naa.digipres.xena.kernel.properties.PluginProperties;
import au.gov.naa.digipres.xena.kernel.properties.PropertyMessageException;
import au.gov.naa.digipres.xena.kernel.properties.XenaProperty;

/**
 * @author Justin Waddell
 *
 */
public class ImageProperties extends PluginProperties {

    private List<XenaProperty> properties;

    public static final String IMAGE_PLUGIN_NAME = "Image";

    public static final String TESSERACT_LOCATION_PROP_NAME = "Tesseract Executable Location";

    public static final String IMAGEMAGIC_LOCATION_PROP_NAME = "Image Magick Convert Executable Location";

    @Override
    public String getName() {
        return IMAGE_PLUGIN_NAME;
    }

    @Override
    public List<XenaProperty> getProperties() {
        return properties;
    }

    @Override
    public void initialiseProperties() {
        properties = new ArrayList<XenaProperty>();
        XenaProperty tesseractLocationProperty = new XenaProperty(TESSERACT_LOCATION_PROP_NAME, "Location of tesseract executable", XenaProperty.PropertyType.FILE_TYPE, getName()) {

            @Override
            public void validate(String newValue) throws InvalidPropertyException, PropertyMessageException {
                super.validate(newValue);
                File location = new File(newValue);
                if (!location.exists() || !location.isFile()) {
                    throw new InvalidPropertyException("Invalid location for tesseract!");
                }
            }
        };
        XenaProperty imageMagickLocationProperty = new XenaProperty(IMAGEMAGIC_LOCATION_PROP_NAME, "Location of the Image Magick convert executable", XenaProperty.PropertyType.FILE_TYPE, getName()) {

            @Override
            public void validate(String newValue) throws InvalidPropertyException, PropertyMessageException {
                super.validate(newValue);
                if (newValue.length() > 0) {
                    File location = new File(newValue);
                    if (location == null || !location.exists() || !location.isFile() || !location.canExecute()) {
                        throw new InvalidPropertyException("Invalid location for Image Magick!");
                    }
                }
            }
        };
        getManager().loadProperty(tesseractLocationProperty);
        getManager().loadProperty(imageMagickLocationProperty);
        properties.add(tesseractLocationProperty);
        properties.add(imageMagickLocationProperty);
    }
}

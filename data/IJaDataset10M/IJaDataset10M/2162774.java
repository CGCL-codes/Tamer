package org.pagger.data.picture;

import java.util.logging.Logger;
import org.pagger.data.AccessSet;
import org.pagger.data.Property;
import org.pagger.data.picture.xmp.XmpDublinCoreSchema;
import org.pagger.util.Validator;

/**
 * @author Gerd Saurer
 */
public class JpegDescriptionMetadata extends TiffDescriptionMetadata {

    private static final Logger LOGGER = Logger.getLogger(JpegDescriptionMetadata.class.getName());

    private final AccessSet<XmpDublinCoreSchema> _xmpDublinCoreAccessSet;

    public JpegDescriptionMetadata(final XmpDublinCoreSchema xmpDublinCoreSchema) {
        super();
        Validator.notNull(xmpDublinCoreSchema, "XmpDublinCoreSchema");
        _xmpDublinCoreAccessSet = new AccessSet<XmpDublinCoreSchema>(xmpDublinCoreSchema, LOGGER);
        _xmpDublinCoreAccessSet.put(TAGS, XmpDublinCoreSchema.SUBJECT);
    }

    @Override
    public <T> T getValue(Property<T> prop) {
        T retVal = _xmpDublinCoreAccessSet.getValue(prop);
        if (retVal == null) {
            retVal = super.getValue(prop);
        }
        return retVal;
    }

    @Override
    public <T> void setValue(Property<T> prop, T value) {
        _xmpDublinCoreAccessSet.setValue(prop, value);
        super.setValue(prop, value);
    }

    @Override
    public void clearCache() {
        super.clearCache();
        _xmpDublinCoreAccessSet.clearCache();
    }
}

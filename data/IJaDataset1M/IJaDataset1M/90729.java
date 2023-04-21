package org.pagger.data.picture.iptc;

import org.apache.sanselan.formats.jpeg.iptc.IPTCType;
import org.pagger.data.MetadataException;
import org.pagger.data.ValueAccess;
import org.pagger.util.Validator;

/**
 * @author Gerd Saurer
 */
public class IptcDefaultMetadataAccess<T> extends AbstractIptcMetadataAccess<T> {

    private final Class<T> _clazz;

    public IptcDefaultMetadataAccess(Class<T> clazz, IPTCType type) {
        super(type);
        Validator.notNull(clazz, "Clazz");
        _clazz = clazz;
    }

    @Override
    public T getIPTCValue(final IptcRawMetadata metadata) throws MetadataException {
        return _clazz.cast(IptcUtil.getValue(metadata, getType()));
    }

    @Override
    public void setIPTCValue(final IptcRawMetadata metadata, final T value) throws MetadataException {
        IptcUtil.setValue(metadata, getType(), (String) value);
    }

    @Override
    public ValueAccess<IptcRawMetadata, T> copy() {
        return new IptcDefaultMetadataAccess<T>(_clazz, getType());
    }
}

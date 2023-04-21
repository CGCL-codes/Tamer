package org.iptc.ines.factory.meta;

import org.iptc.nar.interfaces.datatype.FlexPropType;
import org.iptc.nar.interfaces.datatype.QualPropType;
import org.iptc.nar.interfaces.model.ItemMetadataComponent;

public interface IItemMetaDataFactory {

    public abstract ItemMetadataComponent createItemMetadata(QualPropType itemClass, FlexPropType provider) throws ItemMetadataComponentCreateException;
}

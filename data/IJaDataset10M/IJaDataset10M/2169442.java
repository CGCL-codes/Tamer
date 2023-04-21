package org.identifylife.character.store.oxm.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.identifylife.character.store.model.Feature;
import org.identifylife.character.store.model.FeatureMapping;
import org.identifylife.character.store.model.MappingRef;
import org.identifylife.character.store.model.MappingTypeEnum;
import org.identifylife.character.store.service.impl.FeatureServiceImpl;
import org.springframework.util.Assert;

/**
 * @author dbarnier
 *
 */
public class FeatureMappingRefAdapter extends XmlAdapter<MappingRef, FeatureMapping> {

    @Override
    public MappingRef marshal(FeatureMapping mapping) throws Exception {
        Assert.notNull(mapping);
        Assert.notNull(mapping.getMappedTo());
        MappingRef ref = new MappingRef();
        ref.setRef(mapping.getMappedTo().getUuid());
        ref.setType(mapping.getType());
        return ref;
    }

    static Logger logger = Logger.getLogger(FeatureMappingRefAdapter.class);

    @Override
    public FeatureMapping unmarshal(MappingRef ref) throws Exception {
        logger.info("TRACE: unmarshalling Mapping ref->" + ref);
        logger.info("TRACE: unmarshalling Mapping getRef->" + ref.getRef());
        logger.info("TRACE: unmarshalling Mapping getType-> " + ref.getType());
        logger.info("TRACE: unmarshalling Mapping RefFeature->" + new RefFeature(ref.getRef()));
        logger.info("TRACE: unmarshalling Mapping FeatureMapping-> " + new FeatureMapping(new RefFeature(ref.getRef()), ref.getType()));
        return new FeatureMapping(new RefFeature(ref.getRef()), ref.getType());
    }

    public static class RefFeature extends Feature {

        public RefFeature() {
        }

        public RefFeature(String ref) {
            setUuid(ref);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("uuid", getUuid()).toString();
        }
    }
}

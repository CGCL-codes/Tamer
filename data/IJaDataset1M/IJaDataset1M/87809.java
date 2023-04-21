package net.stickycode.resource;

import java.util.Set;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.stereotype.component.StickyRepository;

@StickyRepository
public class StickyResourceCodecRegistry implements ResourceCodecRegistry {

    private Logger log = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    @Inject
    private Set<ResourceCodec> codecs;

    @Override
    public <T> ResourceCodec<T> find(CoercionTarget target) {
        log.info("find a codec for {}", target);
        ResourceCodec<T> lookup = lookup(target);
        log.info("found {}", lookup);
        return lookup;
    }

    @SuppressWarnings("unchecked")
    private <T> ResourceCodec<T> lookup(CoercionTarget target) {
        for (ResourceCodec<?> r : codecs) {
            if (r.isApplicableTo(target)) return (ResourceCodec<T>) r;
        }
        throw new ResourceCodecNotFoundException(target, codecs);
    }
}

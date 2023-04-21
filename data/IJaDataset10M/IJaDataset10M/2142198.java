package com.compomics.acromics.mapreduce.fix;

import com.compomics.acromics.mapreduce.io.TaggedInputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.log4j.Logger;
import java.io.IOException;

/**
 * An {@link Mapper} that delegates behaviour of paths to multiple other
 * mappers.
 */
public class DelegatingMapperFIX<K1, V1, K2, V2> extends Mapper<K1, V1, K2, V2> {

    private static Logger logger = Logger.getLogger(DelegatingMapperFIX.class);

    private PublicMapper<K1, V1, K2, V2> iPublicMapper;

    private TaggedInputSplit iSplit;

    /**
     * An {@link Mapper} that delegates behaviour of paths to multiple other
     * mappers.
     */
    public DelegatingMapperFIX() {
        iPublicMapper = null;
        iSplit = null;
    }

    @Override
    public String toString() {
        return "DelegatingMapperFIX{" + "iPublicMapper=" + iPublicMapper + ", iSplit=" + iSplit + '}';
    }

    /**
     * Called once for each key/value pair in the input split. Most applications
     * should override this, but the default is the identity function.
     */
    @Override
    public void map(K1 key, V1 value, Context context) throws IOException, InterruptedException {
        if (iPublicMapper == null) {
            iSplit = (TaggedInputSplit) context.getInputSplit();
            logger.debug("creating " + iSplit.getMapperClass().toString() + " for inputsplit ");
            iPublicMapper = (PublicMapper<K1, V1, K2, V2>) ReflectionUtils.newInstance(iSplit.getMapperClass(), context.getConfiguration());
        }
        iPublicMapper.map(key, value, context);
    }
}

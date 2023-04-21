package cunei.hypothesis;

import java.util.Arrays;
import java.util.Map;
import cunei.config.SystemConfig;
import cunei.translate.SimilarityModel;
import cunei.type.SequenceType;
import cunei.type.TypeSequence;

public class ReferenceSimilarityModel extends SimilarityModel {

    private static final int[] FEATURE_IDS = getFeatureIds(SystemConfig.getInstance("Reference"), Arrays.asList(SequenceType.LEXICAL));

    public ReferenceSimilarityModel(final float scale, final Map<TypeSequence, Integer>[] ngramsA, final Map<TypeSequence, Integer>[] ngramsB, final int lengthA, final int lengthB) {
        super(scale, ngramsA, ngramsB, lengthA, lengthB, null, FEATURE_IDS);
    }
}

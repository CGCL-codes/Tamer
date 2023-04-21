package opennlp.tools.coref.resolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.coref.DiscourseEntity;
import opennlp.tools.coref.Linker;
import opennlp.tools.coref.mention.MentionContext;

/**
 * Resolves coreference between plural pronouns and their referents.
 */
public class PluralPronounResolver extends MaxentResolver {

    int NUM_SENTS_BACK_PRONOUNS = 2;

    public PluralPronounResolver(String projectName, ResolverMode m) throws IOException {
        super(projectName, "tmodel", m, 30);
    }

    public PluralPronounResolver(String projectName, ResolverMode m, NonReferentialResolver nrr) throws IOException {
        super(projectName, "tmodel", m, 30, nrr);
    }

    protected List getFeatures(MentionContext mention, DiscourseEntity entity) {
        List features = new ArrayList();
        features.addAll(super.getFeatures(mention, entity));
        if (entity != null) {
            features.addAll(getPronounMatchFeatures(mention, entity));
            MentionContext cec = entity.getLastExtent();
            features.addAll(getDistanceFeatures(mention, entity));
            features.addAll(getContextFeatures(cec));
            features.add(getMentionCountFeature(entity));
        }
        return (features);
    }

    protected boolean outOfRange(MentionContext mention, DiscourseEntity entity) {
        MentionContext cec = entity.getLastExtent();
        return (mention.getSentenceNumber() - cec.getSentenceNumber() > NUM_SENTS_BACK_PRONOUNS);
    }

    public boolean canResolve(MentionContext mention) {
        String tag = mention.getHeadTokenTag();
        return (tag != null && tag.startsWith("PRP") && Linker.pluralThirdPersonPronounPattern.matcher(mention.getHeadTokenText()).matches());
    }
}

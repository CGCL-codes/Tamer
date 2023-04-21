package org.yawni.wordnet;

import com.google.common.base.Joiner;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.yawni.wordnet.GlossAndExampleUtils.*;

public class GlossAndExampleUtilsTest {

    private WordNetInterface dictionary;

    @Before
    public void init() {
        dictionary = WordNet.getInstance();
    }

    @Test
    public void synsets() {
        System.err.println("synsets");
        for (final Synset synset : dictionary.synsets(POS.ALL)) {
            final String definitions = getDefinitionsChunk(synset);
            assertTrue(definitions.length() > 0);
            final String examples = getExamplesChunk(synset);
        }
        System.err.println("success!");
    }

    @Test
    public void glossChunkParsing() {
        System.err.println("glossChunkParsing");
        final WordSense release = dictionary.lookupWord("release", POS.NOUN).getSense(3);
        final String sentenceGloss = "a process that liberates or discharges something; \"there was a sudden release of oxygen\"; \"the release of iodine from the thyroid gland\"";
        assertThat(sentenceGloss).isEqualTo(release.getSynset().getGloss());
        final String definition1 = "a process that liberates or discharges something";
        final String example1 = "there was a sudden release of oxygen";
        final String example2 = "the release of iodine from the thyroid gland";
        assertThat(getDefinitionsChunk(release.getSynset())).contains(definition1);
        final String examples = getExamplesChunk(release.getSynset());
        assertThat(examples).contains(example1);
        assertThat(examples).contains(example2);
    }

    @Test
    public void glossParsing1() {
        System.err.println("glossParsing");
        final WordSense release = dictionary.lookupWord("release", POS.NOUN).getSense(3);
        final String sentenceGloss = "a process that liberates or discharges something; \"there was a sudden release of oxygen\"; \"the release of iodine from the thyroid gland\"";
        assertThat(sentenceGloss).isEqualTo(release.getSynset().getGloss());
        final String definition1 = "a process that liberates or discharges something";
        final String example1 = "there was a sudden release of oxygen";
        final String example2 = "the release of iodine from the thyroid gland";
        final Pattern glossParts = Pattern.compile("([^;]+)(?:; \"([^\"]+)\")*");
        assertThat(sentenceGloss).matches(glossParts.pattern());
        assertThat(getDefinitions(release.getSynset())).contains(definition1);
        final List<String> examples = getExamples(release.getSynset());
        System.err.println("examples: " + Joiner.on('\n').join(examples));
        assertThat(examples).contains(example1, example2);
    }
}

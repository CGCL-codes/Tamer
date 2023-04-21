package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.pdlib.PVector.pvec;
import static com.xenoage.util.math.Fraction.fr;
import static com.xenoage.zong.core.music.MP.mp0;
import static com.xenoage.zong.io.score.ScoreController.getMeasureBeats;
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.xenoage.pdlib.PVector;
import com.xenoage.util.Delta;
import com.xenoage.util.math.Fraction;
import com.xenoage.util.test.TestIO;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInputTest;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterTest;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Test cases for the {@link BeatOffsetBasedVoiceSpacingStrategy}.
 * 
 * TODO: Task #36: Update test because clefs/keys are not handled any more
 * in voices.
 *
 * @author Andreas Wenger
 */
public class BeatOffsetBasedVoiceSpacingStrategyTest {

    private LayoutSettings layoutSettings;

    @Before
    public void setUp() {
        TestIO.initWithSharedDir();
        layoutSettings = LayoutSettings.loadDefault();
    }

    @Test
    public void testComputeSharedBeats() {
        BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
        PVector<SpacingElement> list1 = pvec(new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(3), 0f), new SpacingElement(null, beat(7), 0f), new SpacingElement(null, beat(8), 0f), new SpacingElement(null, beat(9), 0f));
        PVector<BeatOffset> list2 = pvec(new BeatOffset(beat(0), 0f), new BeatOffset(beat(5), 0f), new BeatOffset(beat(7), 0f), new BeatOffset(beat(9), 0f));
        List<BeatOffset> res = strategy.computeSharedBeats(list1, list2);
        assertEquals(3, res.size());
        assertEquals(beat(0), res.get(0).getBeat());
        assertEquals(beat(7), res.get(1).getBeat());
        assertEquals(beat(9), res.get(2).getBeat());
        list1 = pvec(new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(1), 0f), new SpacingElement(null, beat(3), 0f));
        list2 = pvec(new BeatOffset(beat(2), 0f), new BeatOffset(beat(4), 0f));
        res = strategy.computeSharedBeats(list1, list2);
        assertEquals(0, res.size());
        list1 = pvec(new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(3), 0f), new SpacingElement(null, beat(3), 0f));
        list2 = pvec(new BeatOffset(beat(0), 0f), new BeatOffset(beat(1), 0f), new BeatOffset(beat(2), 0f), new BeatOffset(beat(3), 0f));
        res = strategy.computeSharedBeats(list1, list2);
        assertEquals(2, res.size());
        assertEquals(beat(0), res.get(0).getBeat());
        assertEquals(beat(3), res.get(1).getBeat());
    }

    @Test
    public void computeVoiceSpacingTest1() {
        float is = 2;
        VoiceSpacing voiceSpacing = new VoiceSpacing(Voice.empty, is, pvec(new SpacingElement(null, beat(2), 1f), new SpacingElement(null, beat(4), 2f), new SpacingElement(null, beat(7), 4f), new SpacingElement(null, beat(8), 6f)));
        PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 0f), new BeatOffset(beat(4), 8f), new BeatOffset(beat(8), 20f));
        BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
        PVector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets).getSpacingElements();
        assertEquals(4, finalSpacing.size());
        assertEquals(beat(2), finalSpacing.get(0).beat);
        assertEquals(4f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT_ROUGH);
        assertEquals(beat(4), finalSpacing.get(1).beat);
        assertEquals(8f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT_ROUGH);
        assertEquals(beat(7), finalSpacing.get(2).beat);
        assertEquals(14f / is, finalSpacing.get(2).offset, Delta.DELTA_FLOAT_ROUGH);
        assertEquals(beat(8), finalSpacing.get(3).beat);
        assertEquals(20f / is, finalSpacing.get(3).offset, Delta.DELTA_FLOAT_ROUGH);
    }

    @Test
    public void computeVoiceSpacingTest2() {
        float is = 3;
        VoiceSpacing voiceSpacing = new VoiceSpacing(Voice.empty, is, pvec(new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(2), 2f)));
        PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 0f), new BeatOffset(beat(2), 2f));
        BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
        PVector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets).getSpacingElements();
        assertEquals(2, finalSpacing.size());
        assertEquals(beat(0), finalSpacing.get(0).beat);
        assertEquals(0f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT);
        assertEquals(beat(2), finalSpacing.get(1).beat);
        assertEquals(2f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT);
    }

    @Test
    public void computeVoiceSpacingTest3() {
        float is = 4;
        VoiceSpacing voiceSpacing = new VoiceSpacing(Voice.empty, is, pvec(new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(2), 2f)));
        PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 2f), new BeatOffset(beat(2), 6f));
        BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
        PVector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets).getSpacingElements();
        assertEquals(2, finalSpacing.size());
        assertEquals(beat(0), finalSpacing.get(0).beat);
        assertEquals(2f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT);
        assertEquals(beat(2), finalSpacing.get(1).beat);
        assertEquals(6f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT);
    }

    /**
   * Test file "BeatOffsetBasedVoiceSpacingStrategyTest-1.xml".
   */
    @Test
    public void computeVoiceSpacing_File1() {
        Score score = MusicXMLScoreFileInputTest.loadXMLTestScore("BeatOffsetBasedVoiceSpacingStrategyTest-1.xml");
        float is = 2;
        score = score.withFormat(score.format.withInterlineSpace(is));
        NotationStrategy notationStrategy = ScoreLayouterTest.getNotationStrategy();
        NotationsCache notations = notationStrategy.computeNotations(score, null, layoutSettings);
        VoiceSpacing voiceSpacing = new SeparateVoiceSpacingStrategy().computeVoiceSpacing(score.getVoice(mp0), null, is, false, notations.get(0), getMeasureBeats(score, 0), layoutSettings);
        PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 5f), new BeatOffset(beat(1), 10f), new BeatOffset(beat(2), 15f));
        BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
        PVector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets).getSpacingElements();
        assertEquals(4, finalSpacing.size());
        assertEquals(beat(0), finalSpacing.get(0).beat);
        assertEquals(5f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT);
        assertEquals(beat(1), finalSpacing.get(1).beat);
        assertEquals(10f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT);
        assertEquals(beat(2), finalSpacing.get(2).beat);
        assertEquals(15f / is, finalSpacing.get(2).offset, Delta.DELTA_FLOAT);
    }

    /**
   * Test file "BeatOffsetBasedVoiceSpacingStrategyTest-2.xml".
   */
    @Test
    public void computeVoiceSpacing_File2() {
        Score score = MusicXMLScoreFileInputTest.loadXMLTestScore("BeatOffsetBasedVoiceSpacingStrategyTest-2.xml");
        float is = 3;
        score = score.withFormat(score.format.withInterlineSpace(is));
        NotationStrategy notationStrategy = ScoreLayouterTest.getNotationStrategy();
        NotationsCache notations = notationStrategy.computeNotations(score, null, layoutSettings);
        for (int voice = 0; voice <= 1; voice++) {
            VoiceSpacing voiceSpacing = new SeparateVoiceSpacingStrategy().computeVoiceSpacing(score.getVoice(mp0), null, is, false, notations.get(0), getMeasureBeats(score, 0), layoutSettings);
            PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 5f), new BeatOffset(beat(1), 10f), new BeatOffset(beat(2), 15f));
            BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
            PVector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets).getSpacingElements();
            assertEquals(4, finalSpacing.size());
            assertEquals(beat(0), finalSpacing.get(0).beat);
            assertEquals(5f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT);
            assertEquals(beat(1), finalSpacing.get(1).beat);
            assertEquals(10f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT);
            assertEquals(beat(2), finalSpacing.get(2).beat);
            assertEquals(15f / is, finalSpacing.get(2).offset, Delta.DELTA_FLOAT);
        }
    }

    private Fraction beat(int quarters) {
        return fr(quarters, 4);
    }
}

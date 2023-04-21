package ca.ucalgary.cpsc.agilePlanner.test.cards.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import cards.model.StoryCardModel;
import cards.model.StoryCardModelAqua;
import cards.model.StoryCardModelBlue;
import cards.model.StoryCardModelGreen;
import cards.model.StoryCardModelKhaki;
import cards.model.StoryCardModelPeach;
import cards.model.StoryCardModelPink;
import cards.model.StoryCardModelRed;
import cards.model.StoryCardModelWhite;
import cards.model.StoryCardModelYellow;

public class StoryCardModelTest {

    @Test
    public void shouldReturnCorrespondingColoredSCMClass() {
        assertEquals(StoryCardModelRed.class, StoryCardModel.getChildClassOfColor("red"));
        assertEquals(StoryCardModelBlue.class, StoryCardModel.getChildClassOfColor("blue"));
        assertEquals(StoryCardModelPink.class, StoryCardModel.getChildClassOfColor("pink"));
        assertEquals(StoryCardModelGreen.class, StoryCardModel.getChildClassOfColor("green"));
        assertEquals(StoryCardModelYellow.class, StoryCardModel.getChildClassOfColor("yellow"));
        assertEquals(StoryCardModelPeach.class, StoryCardModel.getChildClassOfColor("peach"));
        assertEquals(StoryCardModelWhite.class, StoryCardModel.getChildClassOfColor("white"));
        assertEquals(StoryCardModelAqua.class, StoryCardModel.getChildClassOfColor("aqua"));
        assertEquals(StoryCardModelKhaki.class, StoryCardModel.getChildClassOfColor("khaki"));
        assertEquals(StoryCardModelRed.class, StoryCardModel.getChildClassOfColor("RED"));
        assertNull(StoryCardModel.getChildClassOfColor("purple"));
    }
}

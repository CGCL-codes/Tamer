package com.twolattes.json.inheritanceerror;

import org.junit.Test;
import com.twolattes.json.TwoLattes;

public class InheritanceErrorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNoDiscriminatorName() throws Exception {
        TwoLattes.createMarshaller(NoDiscriminatorName.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoSubclasses() throws Exception {
        TwoLattes.createMarshaller(NoSubclasses.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubclassesAndInlining() throws Exception {
        TwoLattes.createMarshaller(SubclassesAndInlining.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMentionsNonSubclass() throws Exception {
        TwoLattes.createMarshaller(MentionsNonSubclass.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoDiscriminator1() throws Exception {
        TwoLattes.createMarshaller(NoDiscriminator1.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoDiscriminator2() throws Exception {
        TwoLattes.createMarshaller(NoDiscriminator2.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiscriminatorUsedTwice() throws Exception {
        TwoLattes.createMarshaller(DiscriminatorUsedTwice.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentEntity() throws Exception {
        TwoLattes.createMarshaller(ParentEntity.class);
    }
}

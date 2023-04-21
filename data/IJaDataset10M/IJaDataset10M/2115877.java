package org.hibernate.search.test.similarity;

import org.hibernate.search.test.util.FullTextSessionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class IllegalSimilarityConfigurationTest {

    @Test
    public void testValidConfiguration() {
        boolean configurationIsLegal = true;
        FullTextSessionBuilder builder = null;
        try {
            builder = new FullTextSessionBuilder().addAnnotatedClass(Can.class).addAnnotatedClass(Trash.class).build();
        } catch (Exception e) {
            configurationIsLegal = false;
        } finally {
            if (builder != null) builder.close();
        }
        assertTrue("A valid configuration could not be started.", configurationIsLegal);
    }

    @Test
    public void testInconsistentSimilarityInClassHierarchy() {
        boolean configurationIsLegal = true;
        FullTextSessionBuilder builder = null;
        try {
            builder = new FullTextSessionBuilder().addAnnotatedClass(Trash.class).addAnnotatedClass(LittleTrash.class).build();
        } catch (Exception e) {
            configurationIsLegal = false;
        } finally {
            if (builder != null) builder.close();
        }
        assertFalse("Invalid Similarity declared, should have thrown an exception: same similarity" + " must be used across class hierarchy", configurationIsLegal);
    }

    @Test
    public void testInconsistentSimilarityInClassSharingAnIndex() {
        boolean configurationIsLegal = true;
        FullTextSessionBuilder builder = null;
        try {
            builder = new FullTextSessionBuilder().addAnnotatedClass(Trash.class).addAnnotatedClass(Sink.class).build();
        } catch (Exception e) {
            configurationIsLegal = false;
        } finally {
            if (builder != null) builder.close();
        }
        assertFalse("Invalid Similarity declared, should have thrown an exception: two entities" + "sharing the same index are using a different similarity", configurationIsLegal);
    }

    @Test
    public void testImplicitSimilarityInheritanceIsValid() {
        boolean configurationIsLegal = true;
        FullTextSessionBuilder builder = null;
        try {
            builder = new FullTextSessionBuilder().addAnnotatedClass(Trash.class).addAnnotatedClass(ProperTrashExtension.class).build();
        } catch (Exception e) {
            configurationIsLegal = false;
        } finally {
            if (builder != null) builder.close();
        }
        assertTrue("Valid configuration could not be built", configurationIsLegal);
    }

    @Test
    public void testInvalidToOverrideParentsSimilarity() {
        boolean configurationIsLegal = true;
        FullTextSessionBuilder builder = null;
        try {
            builder = new FullTextSessionBuilder().addAnnotatedClass(Can.class).addAnnotatedClass(SmallerCan.class).build();
        } catch (Exception e) {
            configurationIsLegal = false;
        } finally {
            if (builder != null) builder.close();
        }
        assertFalse("Invalid Similarity declared, should have thrown an exception: child entity" + " is overriding parent's Similarity", configurationIsLegal);
    }
}

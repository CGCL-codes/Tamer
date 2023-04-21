package com.googlecode.greenbridge.annotation;

import com.googlecode.greenbridge.annotation.StoryRef;

/**
 *
 * @author ryan
 */
public class Story2121 implements StoryRef {

    @Override
    public String[] narrative() {
        return new String[] { "As a ", "I want ", "So that" };
    }

    @Override
    public String name() {
        return "Story2121";
    }

    @Override
    public long version() {
        return 1;
    }

    @Override
    public String storyPackage() {
        return "brads-stuff-stories:com.brad.stuff:1.0-SNAPSHOT";
    }

    @Override
    public String linkUrl() {
        return "http://code.googlecode.com/p/greenbridge/Story";
    }

    @Override
    public String linkName() {
        return "Wiki Link";
    }

    @Override
    public Class<? extends StoryModule> getStoryModule() {
        return SampleStoryModule.class;
    }
}

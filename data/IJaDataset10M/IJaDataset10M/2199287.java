package org.fest.reflect.method;

import jacky.lanlan.song.util.Assert;
import jacky.lanlan.song.lang.StringUtils;

/**
 * Understands a template for the name of a method to invoke using Java Reflection.
 * 
 * @author Alex Ruiz
 */
abstract class NameTemplate {

    final String name;

    NameTemplate(String name) {
        Assert.isTrue(StringUtils.hasLength(name), "The name of the method to access should not be null or empty");
        this.name = name;
    }
}

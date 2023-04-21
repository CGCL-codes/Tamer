package org.nakedobjects.noa.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class StringUtilsNaturalize {

    @Test
    public void shouldNaturalizeMultipleCamelCase() {
        assertThat(StringUtils.naturalize("thisIsACamelCasePhrase"), is("This Is A Camel Case Phrase"));
    }

    @Test
    public void shouldNaturalizeMultiplePascalCase() {
        assertThat(StringUtils.naturalize("ThisIsAPascalCasePhrase"), is("This Is A Pascal Case Phrase"));
    }

    @Test
    public void shouldNaturalizeSingleWordStartingWithLowerCase() {
        assertThat(StringUtils.naturalize("foo"), is("Foo"));
    }

    @Test
    public void shouldNaturalizeSingleWordStartingWithUpperCase() {
        assertThat(StringUtils.naturalize("Foo"), is("Foo"));
    }
}

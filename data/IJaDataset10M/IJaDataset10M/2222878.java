package com.ail.core;

import static java.util.Locale.CANADA;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TestLocale {

    /**
     * @throws Exception
     */
    @Test
    public void testLocaleConstruction() throws Exception {
        try {
            new ThreadLocale().getInstance();
            fail("You should not be able to get an instance from a badly initialised locale");
        } catch (IllegalStateException e) {
        } catch (Throwable e) {
            fail("Wrong exception thrown when getting an instance from a badly initialised locale");
        }
        try {
            new ThreadLocale(null, null, null).getInstance();
            fail("You should not be able to get an instance from a badly initialised locale");
        } catch (IllegalStateException e) {
        } catch (Throwable e) {
            fail("Wrong exception thrown when getting an instance from a badly initialised locale");
        }
        try {
            new ThreadLocale(null, null, "p").getInstance();
            fail("You should not be able to get an instance from a badly initialised locale");
        } catch (IllegalStateException e) {
        } catch (Throwable e) {
            fail("Wrong exception thrown when getting an instance from a badly initialised locale");
        }
        try {
            new ThreadLocale(null, java.util.Locale.CANADA.getCountry(), "p").getInstance();
            fail("You should not be able to get an instance from a badly initialised locale");
        } catch (IllegalStateException e) {
        } catch (Throwable e) {
            fail("Wrong exception thrown when getting an instance from a badly initialised locale");
        }
        new ThreadLocale(CANADA.getLanguage(), null, null).getInstance();
        new ThreadLocale(CANADA.getLanguage(), CANADA.getCountry(), null).getInstance();
        new ThreadLocale(CANADA.getLanguage(), CANADA.getCountry(), "p").getInstance();
    }
}

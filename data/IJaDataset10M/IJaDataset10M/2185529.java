package com.xiledsystems.AlternateJavaBridgelib.components.altbridge;

import android.util.Log;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import com.xiledsystems.AlternateJavaBridgelib.components.Component;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.collect.Maps;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.ExternalTextToSpeech;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.ITextToSpeech;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.InternalTextToSpeech;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.SdkLevel;
import com.xiledsystems.AlternateJavaBridgelib.components.events.EventDispatcher;

/**
 * Component for converting text to speech using either the built-in TTS library or the
 * TextToSpeech Extended library (which must be pre-installed).
 *
 */
public class TextToSpeech extends AndroidNonvisibleComponent implements Component, OnStopListener, OnResumeListener {

    private static final Map<String, Locale> iso3LanguageToLocaleMap = Maps.newHashMap();

    private static final Map<String, Locale> iso3CountryToLocaleMap = Maps.newHashMap();

    private static final String LOG_TAG = "TextToSpeech";

    static {
        initLocaleMaps();
    }

    private boolean result;

    private String language;

    private String country;

    private final ITextToSpeech tts;

    private String iso2Language;

    private String iso2Country;

    private static void initLocaleMaps() {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            try {
                String iso3Country = locale.getISO3Country();
                if (iso3Country.length() > 0) {
                    iso3CountryToLocaleMap.put(iso3Country, locale);
                }
            } catch (MissingResourceException e) {
            }
            try {
                String iso3Language = locale.getISO3Language();
                if (iso3Language.length() > 0) {
                    iso3LanguageToLocaleMap.put(iso3Language, locale);
                }
            } catch (MissingResourceException e) {
            }
        }
    }

    /**
   * Creates a TextToSpeech component.
   *
   * @param container container, component will be placed in
   */
    public TextToSpeech(ComponentContainer container) {
        super(container.$form());
        result = false;
        Language("");
        Country("");
        boolean useExternalLibrary = SdkLevel.getLevel() < SdkLevel.LEVEL_DONUT;
        Log.v(LOG_TAG, "Using " + (useExternalLibrary ? "external" : "internal") + " TTS library.");
        ITextToSpeech.TextToSpeechCallback callback = new ITextToSpeech.TextToSpeechCallback() {

            @Override
            public void onSuccess() {
                result = true;
                Log.d(LOG_TAG, "Success");
                AfterSpeaking(true);
            }

            @Override
            public void onFailure() {
                result = false;
                Log.d(LOG_TAG, "Failure");
                AfterSpeaking(false);
            }
        };
        tts = useExternalLibrary ? new ExternalTextToSpeech(container, callback) : new InternalTextToSpeech(container.$context(), callback);
        form.registerForOnStop(this);
        form.registerForOnResume(this);
    }

    /**
   * Result property getter method.
   */
    public boolean Result() {
        return result;
    }

    /**
   * Sets the language for this TextToSpeech component.
   *
   * @param language is the ISO2 (i.e. 2 letter) or ISO3 (i.e. 3 letter) language code to set this
   * TextToSpeech component to.
   */
    public void Language(String language) {
        Locale locale;
        switch(language.length()) {
            case 3:
                locale = iso3LanguageToLocale(language);
                this.language = locale.getISO3Language();
                break;
            case 2:
                locale = new Locale(language);
                this.language = locale.getLanguage();
                break;
            default:
                locale = Locale.getDefault();
                this.language = locale.getLanguage();
                break;
        }
        iso2Language = locale.getLanguage();
    }

    private static Locale iso3LanguageToLocale(String iso3Language) {
        Locale mappedLocale = iso3LanguageToLocaleMap.get(iso3Language);
        if (mappedLocale == null) {
            mappedLocale = iso3LanguageToLocaleMap.get(iso3Language.toLowerCase(Locale.ENGLISH));
        }
        return mappedLocale == null ? Locale.getDefault() : mappedLocale;
    }

    /**
   * Gets the language for this TextToSpeech component.  This will be either an ISO2 (i.e. 2 letter)
   * or ISO3 (i.e. 3 letter) code depending on which kind of code the property was set with.
   *
   * @return the language code for this TextToSpeech component.
   */
    public String Language() {
        return language;
    }

    /**
   * Sets the country for this TextToSpeech component.
   *
   * @param country is the ISO2 (i.e. 2 letter) or ISO3 (i.e. 3 letter) country code to set this
   * TextToSpeech component to.
   */
    public void Country(String country) {
        Locale locale;
        switch(country.length()) {
            case 3:
                locale = iso3CountryToLocale(country);
                this.country = locale.getISO3Country();
                break;
            case 2:
                locale = new Locale(country);
                this.country = locale.getCountry();
                break;
            default:
                locale = Locale.getDefault();
                this.country = locale.getCountry();
                break;
        }
        iso2Country = locale.getCountry();
    }

    private static Locale iso3CountryToLocale(String iso3Country) {
        Locale mappedLocale = iso3CountryToLocaleMap.get(iso3Country);
        if (mappedLocale == null) {
            mappedLocale = iso3CountryToLocaleMap.get(iso3Country.toUpperCase(Locale.ENGLISH));
        }
        return mappedLocale == null ? Locale.getDefault() : mappedLocale;
    }

    /**
   * Gets the country for this TextToSpeech component.  This will be either an ISO2 (i.e. 2 letter)
   * or ISO3 (i.e. 3 letter) code depending on which kind of code the property was set with.
   *
   * @return country code for this TextToSpeech component.
   */
    public String Country() {
        return country;
    }

    public void Speak(final String message) {
        BeforeSpeaking();
        final Locale loc = new Locale(iso2Language, iso2Country);
        tts.speak(message, loc);
    }

    /**
   * Event to raise when Speak is invoked, before the message is spoken.
   */
    public void BeforeSpeaking() {
        EventDispatcher.dispatchEvent(this, "BeforeSpeaking");
    }

    /**
   * Event to raise after the message is spoken.
   *
   * @param result whether the message was successfully spoken
   */
    public void AfterSpeaking(boolean result) {
        Log.d(LOG_TAG, "Calling AfterSpeaking");
        EventDispatcher.dispatchEvent(this, "AfterSpeaking", result);
    }

    @Override
    public void onStop() {
        tts.onStop();
    }

    @Override
    public void onResume() {
        tts.onResume();
    }
}

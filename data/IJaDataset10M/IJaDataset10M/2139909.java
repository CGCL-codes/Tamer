package com.google.code.appengine.awt.im.spi;

import java.util.Locale;
import com.google.code.appengine.awt.AWTException;
import com.google.code.appengine.awt.Image;
import com.google.code.appengine.awt.im.spi.InputMethod;

public interface InputMethodDescriptor {

    public Locale[] getAvailableLocales() throws AWTException;

    public InputMethod createInputMethod() throws Exception;

    public String getInputMethodDisplayName(Locale inputLocale, Locale displayLanguage);

    public Image getInputMethodIcon(Locale inputLocale);

    public boolean hasDynamicLocaleList();
}

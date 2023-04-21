package org.apache.harmony.awt.wtk;

import java.lang.Character.Subset;
import java.util.Locale;
import com.google.code.appengine.awt.AWTEvent;
import com.google.code.appengine.awt.AWTException;
import com.google.code.appengine.awt.Image;
import com.google.code.appengine.awt.Rectangle;
import com.google.code.appengine.awt.im.spi.InputMethod;
import com.google.code.appengine.awt.im.spi.InputMethodContext;
import com.google.code.appengine.awt.im.spi.InputMethodDescriptor;

/**
 * A cross-platform interface for native input
 * method sub-system functionality.
 */
public abstract class NativeIM implements InputMethod, InputMethodDescriptor {

    protected InputMethodContext imc;

    public void activate() {
    }

    public void deactivate(boolean isTemporary) {
    }

    public void dispatchEvent(AWTEvent event) {
    }

    public void dispose() {
    }

    public void endComposition() {
    }

    public Object getControlObject() {
        return null;
    }

    public Locale getLocale() {
        return null;
    }

    public void hideWindows() {
    }

    public boolean isCompositionEnabled() {
        return false;
    }

    public void notifyClientWindowChange(Rectangle bounds) {
    }

    public void reconvert() {
    }

    public void removeNotify() {
    }

    public void setCharacterSubsets(Subset[] subsets) {
    }

    public void setCompositionEnabled(boolean enable) {
    }

    public void setInputMethodContext(InputMethodContext context) {
        imc = context;
    }

    public boolean setLocale(Locale locale) {
        return false;
    }

    public Locale[] getAvailableLocales() throws AWTException {
        return new Locale[] { Locale.getDefault(), Locale.ENGLISH };
    }

    public InputMethod createInputMethod() throws Exception {
        return this;
    }

    public String getInputMethodDisplayName(Locale inputLocale, Locale displayLanguage) {
        return "System input methods";
    }

    public Image getInputMethodIcon(Locale inputLocale) {
        return null;
    }

    public boolean hasDynamicLocaleList() {
        return false;
    }

    public abstract void disableIME();
}

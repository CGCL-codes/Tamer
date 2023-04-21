package ru.dpelevin.gddc.service.facade;

import ru.dpelevin.gddc.command.DetectDefaultInputLanguagesCommand;
import ru.dpelevin.gddc.service.KeyboardLayoutManager;

/**
 * Facade for {@link KeyboardLayoutManager}.
 * 
 * @author Dmitry Pelevin
 */
public final class KeyboardLayoutManagerStaticFacade {

    /** The keyboard layout manager. */
    private static KeyboardLayoutManager keyboardLayoutManager;

    /**
	 * Instantiates a new keyboard layout manager static facade.
	 */
    private KeyboardLayoutManagerStaticFacade() {
    }

    /**
	 * Detect default input languages command.
	 * 
	 * @return the detect default input languages command. result
	 */
    public static DetectDefaultInputLanguagesCommand.Result detectDefaultInputLanguagesCommand() {
        if (keyboardLayoutManager != null) {
            return keyboardLayoutManager.detectDefaultInputLanguagesCommand();
        } else {
            return null;
        }
    }

    /**
	 * Sets the keyboard layout manager.
	 * 
	 * @param keyboardLayoutManager
	 *            the new keyboard layout manager
	 */
    public void setKeyboardLayoutManager(final KeyboardLayoutManager keyboardLayoutManager) {
        KeyboardLayoutManagerStaticFacade.keyboardLayoutManager = keyboardLayoutManager;
    }
}

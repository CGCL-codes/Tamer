package de.fraunhofer.isst.axbench.editors.axlmultipage.axleditor;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * @brief aXLang source code editor word detector.
 * @todo Adjust to aXLang language elements.
 * @author Ekkart Kleinod
 * @version 0.3.0
 * @since 0.1.0
 */
public class AXLWordDetector implements IWordDetector {

    /**
     * @brief Returns, if the character is part of a word.
     * @param cTest character
     * @return part of a word?
     */
    public boolean isWordPart(char cTest) {
        return Character.isJavaIdentifierPart(cTest);
    }

    /**
     * @brief Returns, if the character is start of a word.
     * @param cTest character
     * @return start of a word?
     */
    public boolean isWordStart(char cTest) {
        return Character.isJavaIdentifierStart(cTest);
    }
}

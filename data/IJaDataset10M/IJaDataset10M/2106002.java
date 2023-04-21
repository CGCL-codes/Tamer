package xvrengine.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class XVRWhiteSpaceDetector implements IWhitespaceDetector {

    public boolean isWhitespace(char c) {
        return Character.isWhitespace(c);
    }
}

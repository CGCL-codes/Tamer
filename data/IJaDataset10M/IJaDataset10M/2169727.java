package net.sourceforge.arguscodewatch.builders;

import net.sourceforge.arguscodewatch.CodewatchPlugin;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Creates a marker when the equals() method is overridden but hashCode() is not
 */
public class OverrideEqualsAndHashCodeCheck extends AbstractObjectEqualsHashCodeCheck {

    @Override
    void chooseMarker(String message, ASTNode node) {
        if (message.equals(CodewatchPlugin.Resources.objectEqualsCheck_requireHashCode)) {
            createMarker(message, node);
        }
    }
}

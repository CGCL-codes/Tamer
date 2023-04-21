package org.dyno.visual.swing.types;

import java.util.List;
import javax.swing.Icon;
import org.dyno.visual.swing.base.ResourceIcon;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;

@SuppressWarnings("unchecked")
public class IconValueParser implements IValueParser {

    public Object parseValue(Object oldValue, List args) {
        if (oldValue != null && !(oldValue instanceof ResourceIcon)) {
            Icon icon = (Icon) oldValue;
            Expression arg = (Expression) args.get(0);
            if (arg instanceof ClassInstanceCreation) {
                ClassInstanceCreation instanceCreation = (ClassInstanceCreation) arg;
                args = instanceCreation.arguments();
                arg = (Expression) args.get(0);
                if (arg instanceof MethodInvocation) {
                    MethodInvocation mi = (MethodInvocation) arg;
                    args = mi.arguments();
                    arg = (Expression) args.get(0);
                    if (arg instanceof StringLiteral) {
                        StringLiteral sl = (StringLiteral) arg;
                        String path = sl.getLiteralValue();
                        return new ResourceIcon(icon, path);
                    }
                }
            }
        }
        return oldValue;
    }
}

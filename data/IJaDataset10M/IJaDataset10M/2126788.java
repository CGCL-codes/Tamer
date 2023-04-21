package org.nomadpim.ide.velocite;

import org.apache.velocity.VelocityContext;

public class FieldNameConverter extends AbstractConverter {

    public FieldNameConverter(VelocityContext velocityContext) {
        super(velocityContext);
    }

    @Override
    protected String convert(String name) {
        if (name.length() == 0) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }
}

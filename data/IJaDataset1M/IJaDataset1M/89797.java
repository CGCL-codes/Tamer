package java.beans;

import java.awt.Color;

class AwtColorPersistenceDelegate extends DefaultPersistenceDelegate {

    @Override
    @SuppressWarnings("boxing")
    protected Expression instantiate(Object oldInstance, Encoder enc) {
        Color color = (Color) oldInstance;
        return new Expression(oldInstance, oldInstance.getClass(), Statement.CONSTRUCTOR_NAME, new Object[] { color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() });
    }
}

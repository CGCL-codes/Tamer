package org.jrichclient.richdock;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import org.easymock.IArgumentMatcher;

public class UnitTestUtils {

    public static void propertyChange(PropertyChangeListener listener, PropertyChangeEvent event) {
        reportMatcher(new PropertyChangeEventEquals(event));
        listener.propertyChange(event);
    }

    public static void indexedPropertyChange(PropertyChangeListener listener, IndexedPropertyChangeEvent event) {
        reportMatcher(new IndexedPropertyChangeEventEquals(event));
        listener.propertyChange(event);
    }

    public static void vetoableChange(VetoableChangeListener listener, PropertyChangeEvent event) throws PropertyVetoException {
        reportMatcher(new PropertyChangeEventEquals(event));
        listener.vetoableChange(event);
    }

    private static class PropertyChangeEventEquals implements IArgumentMatcher {

        private final PropertyChangeEvent expectedEvent;

        private PropertyChangeEvent actualEvent;

        public PropertyChangeEventEquals(PropertyChangeEvent expectedEvent) {
            this.expectedEvent = expectedEvent;
        }

        public boolean matches(Object actual) {
            return equalEvents((PropertyChangeEvent) actual, expectedEvent);
        }

        public void appendTo(StringBuffer buffer) {
            buffer.append("Expected PropertyChangeEvent\n");
            buffer.append(" PropertyName=");
            buffer.append(expectedEvent.getPropertyName() + "\n");
            buffer.append("  Source=");
            buffer.append(expectedEvent.getSource() + "\n");
            buffer.append("  OldValue=");
            buffer.append(expectedEvent.getOldValue() + "\n");
            buffer.append("  NewValue=");
            buffer.append(expectedEvent.getNewValue() + "\n");
            if (actualEvent != null) {
                buffer.append("Actual PropertyChangeEvent");
                buffer.append(" PropertyName=");
                buffer.append(actualEvent.getPropertyName() + "\n");
                buffer.append("  Source=");
                buffer.append(actualEvent.getSource() + "\n");
                buffer.append("  OldValue=");
                buffer.append(actualEvent.getOldValue() + "\n");
                buffer.append("  NewValue=");
                buffer.append(actualEvent.getNewValue() + "\n");
            }
        }
    }

    private static class IndexedPropertyChangeEventEquals implements IArgumentMatcher {

        private final IndexedPropertyChangeEvent expectedEvent;

        private IndexedPropertyChangeEvent actualEvent;

        public IndexedPropertyChangeEventEquals(IndexedPropertyChangeEvent expectedEvent) {
            this.expectedEvent = expectedEvent;
        }

        public boolean matches(Object actual) {
            actualEvent = (IndexedPropertyChangeEvent) actual;
            if (!equalEvents(actualEvent, expectedEvent)) return false;
            return actualEvent.getIndex() == expectedEvent.getIndex();
        }

        public void appendTo(StringBuffer buffer) {
            buffer.append("Expected IndexedPropertyChangeEvent\n");
            buffer.append(" PropertyName=");
            buffer.append(expectedEvent.getPropertyName() + "\n");
            buffer.append("  Source=");
            buffer.append(expectedEvent.getSource() + "\n");
            buffer.append("  Index=");
            buffer.append(expectedEvent.getIndex() + "\n");
            buffer.append("  OldValue=");
            buffer.append(expectedEvent.getOldValue() + "\n");
            buffer.append("  NewValue=");
            buffer.append(expectedEvent.getNewValue() + "\n");
            if (actualEvent != null) {
                buffer.append("Actual IndexedPropertyChangeEvent");
                buffer.append(" PropertyName=");
                buffer.append(actualEvent.getPropertyName() + "\n");
                buffer.append("  Source=");
                buffer.append(actualEvent.getSource() + "\n");
                buffer.append("  Index=");
                buffer.append(actualEvent.getIndex() + "\n");
                buffer.append("  OldValue=");
                buffer.append(actualEvent.getOldValue() + "\n");
                buffer.append("  NewValue=");
                buffer.append(actualEvent.getNewValue() + "\n");
            }
        }
    }

    private static boolean equalObjects(Object obj1, Object obj2) {
        if (obj1 != null) return obj1.equals(obj2);
        if (obj2 != null) return obj2.equals(obj1);
        return true;
    }

    private static boolean equalEvents(PropertyChangeEvent actualEvent, PropertyChangeEvent expectedEvent) {
        if (!equalObjects(actualEvent.getPropertyName(), expectedEvent.getPropertyName())) return false;
        if (actualEvent.getSource() != expectedEvent.getSource()) return false;
        if (!equalObjects(actualEvent.getOldValue(), expectedEvent.getOldValue())) return false;
        if (!equalObjects(actualEvent.getNewValue(), expectedEvent.getNewValue())) return false;
        return true;
    }

    public static BufferedImage createWhiteImage(int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        return bi;
    }

    public static void assertIsNotTotallyWhite(BufferedImage bi) {
        int whiteRGB = Color.WHITE.getRGB();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (bi.getRGB(x, y) != whiteRGB) return;
            }
        }
        fail("All pixels in the image are white");
    }
}

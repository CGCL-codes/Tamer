package skycastle.sound.editor.linkedbeaneditor;

import org.apache.commons.beanutils.PropertyUtils;
import skycastle.sound.UnexpectedReflectionFailure;
import skycastle.sound.producers.ConstantSignal;
import skycastle.util.ColorUtils;
import skycastle.util.MathUtils;
import skycastle.util.StringUtilities;
import skycastle.utils.BeanUtilities;
import skycastle.utils.linkedbeans.BeanLink;
import skycastle.utils.linkedbeans.LinkedBeansContainer;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Random;

/**
 * @author Hans H�ggstr�m
 */
public class DefaultBeanRenderer implements BeanRenderer {

    private final Random myRandom = new Random();

    private int myWidth = 120;

    private int myHeight = 150;

    private static final BasicStroke THICK_LINE = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private static final BasicStroke MEDIUM_LINE = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private static final int PROPERTIES_Y_OFFSET = 40;

    private static final int TITLE_Y_OFFSET = 20;

    private static final int PROPERTY_HEIGHT = 20;

    private static final int PROPERTY_PORT_SIZE = PROPERTY_HEIGHT - 4;

    private static final int FIELD_PORT_BORDER_WIDTH = 2;

    public boolean beanContainsDragPoint(final Object bean, final float x, final float y) {
        final int portRadius = FIELD_PORT_BORDER_WIDTH / 2;
        return x >= portRadius && x < myWidth - portRadius && y >= 0 && y <= myHeight;
    }

    public void renderBean(final Graphics2D g2, final Object bean, final int x, final int y) {
        final Color beanTypeColor = calculateTypeColor(bean);
        final Color bgColor = ColorUtils.interpolateColors(beanTypeColor, new Color(255, 255, 255, 150), 0.4f);
        g2.setColor(bgColor);
        g2.fillRect(x, y, myWidth, myHeight);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, myWidth, myHeight);
        g2.drawString(getBeanTypeName(bean), x, y + TITLE_Y_OFFSET);
        int yoffs = PROPERTIES_Y_OFFSET;
        int index = 0;
        for (Map.Entry<String, Object> entry : getFields(bean).entrySet()) {
            renderProperty(g2, entry, x, y + yoffs, index);
            yoffs += PROPERTY_HEIGHT;
            index++;
        }
    }

    public void renderLink(final Graphics2D g2, final BeanLink link, final LinkedBeansContainer linkedBeansContainer) {
        Point2D sourcePortPos = new Point2D.Float();
        Point2D destPortPos = new Point2D.Float();
        calculatePropertyPortPosition(link.getSourceBean(), link.getSourcePropertyName(), sourcePortPos, true, linkedBeansContainer);
        calculatePropertyPortPosition(link.getTargetBean(), link.getDestinationPropertyName(), destPortPos, false, linkedBeansContainer);
        final Color color = calculateTypeColor(link.getSourceBean());
        drawColoredLine(g2, sourcePortPos, destPortPos, color);
    }

    public PortInfo getPortAt(final Object bean, final float x, final float y, final LinkedBeansContainer linkedBeansContainer, float beanX, float beanY) {
        final Point2D.Float questedPosition = new Point2D.Float(x, y);
        for (Map.Entry<String, Object> entry : getFields(bean).entrySet()) {
            PortInfo foundPort;
            foundPort = getPortIfCloseToQuestedPosition(entry, bean, linkedBeansContainer, questedPosition, false);
            if (foundPort == null) {
                foundPort = getPortIfCloseToQuestedPosition(entry, bean, linkedBeansContainer, questedPosition, true);
            }
            if (foundPort != null) {
                return foundPort;
            }
        }
        return null;
    }

    private PortInfo getPortIfCloseToQuestedPosition(final Map.Entry<String, Object> entry, final Object bean, final LinkedBeansContainer linkedBeansContainer, final Point2D.Float questedPosition, final boolean outputPort) {
        final String propertyName = entry.getKey();
        Color propertyColor = calculateTypeColor(entry.getValue());
        final Point2D.Float portPositionOut = new Point2D.Float();
        calculatePropertyPortPosition(bean, propertyName, portPositionOut, outputPort, linkedBeansContainer);
        if (questedPosition.distance(portPositionOut) < PROPERTY_PORT_SIZE) {
            return new PortInfo(bean, propertyName, (float) portPositionOut.getX(), (float) portPositionOut.getY(), propertyColor);
        }
        return null;
    }

    public void renderLine(final Graphics2D g2, final Color color, final float sx, final float sy, final float tx, final float ty) {
        g2.setColor(Color.BLACK);
        g2.setStroke(THICK_LINE);
        g2.drawLine((int) sx, (int) sy, (int) tx, (int) ty);
        g2.setColor(color);
        g2.setStroke(MEDIUM_LINE);
        g2.drawLine((int) sx, (int) sy, (int) tx, (int) ty);
    }

    private void drawColoredLine(final Graphics2D g2, final Point2D sourcePortPos, final Point2D destPortPos, final Color color) {
        renderLine(g2, color, (float) sourcePortPos.getX(), (float) sourcePortPos.getY(), (float) destPortPos.getX(), (float) destPortPos.getY());
    }

    private void calculatePropertyPortPosition(Object bean, final String propertyName, Point2D portPositionOut, boolean outputPort, final LinkedBeansContainer linkedBeansContainer) {
        int x = (int) linkedBeansContainer.getBeanX(bean) - PROPERTY_HEIGHT / 2;
        int y = (int) linkedBeansContainer.getBeanY(bean);
        if (outputPort) {
            x += myWidth;
        }
        if (propertyName == null) {
            portPositionOut.setLocation(x, y);
        } else {
            int i = getPropertyIndex(bean, propertyName);
            y += i * PROPERTY_HEIGHT + PROPERTIES_Y_OFFSET - PROPERTY_HEIGHT / FIELD_PORT_BORDER_WIDTH;
            portPositionOut.setLocation(x, y);
        }
    }

    private int getPropertyIndex(final Object bean, final String propertyName) {
        final Map<String, Object> fields = getFields(bean);
        int i = 0;
        for (String key : fields.keySet()) {
            if (propertyName.equals(key)) {
                break;
            }
            i++;
        }
        return i;
    }

    private Color calculateTypeColor(final Object bean) {
        int typeHashCode = 0;
        if (bean != null) {
            typeHashCode = BeanUtilities.getClassName(bean).hashCode();
        }
        MathUtils.seedRandom(myRandom, typeHashCode);
        final float hue = myRandom.nextFloat();
        final float saturation = myRandom.nextFloat() * 0.3f + 0.3f;
        final float brightness = myRandom.nextFloat() * 0.3f + 0.7f;
        return ColorUtils.createHSBAColor(hue, saturation, brightness, 1);
    }

    private String getBeanTypeName(final Object bean) {
        String name = BeanUtilities.getUserReadableClassname(bean);
        return StringUtilities.removeSuffix(name, " Signal");
    }

    private void renderProperty(final Graphics2D g2, final Map.Entry<String, Object> entry, final int x, final int y, final int index) {
        final String propertyName = StringUtilities.camelCaseToUserReadableString(entry.getKey());
        g2.drawString(propertyName, x, y);
        final Object value = entry.getValue();
        if (value instanceof ConstantSignal) {
            ConstantSignal constantSignal = (ConstantSignal) value;
            final float signalValue = constantSignal.getSignalValue();
            g2.drawString(StringUtilities.numberToUiString(signalValue), x + 100, y);
        }
        final Color fieldTypeColor = calculateTypeColor(value);
        drawOval(g2, x - PROPERTY_HEIGHT / 2, y, fieldTypeColor);
        drawOval(g2, x + myWidth - PROPERTY_HEIGHT / 2, y, fieldTypeColor);
    }

    private void drawOval(final Graphics2D g2, final int x, final int y, final Color fieldTypeColor) {
        g2.setColor(Color.BLACK);
        g2.fillOval(x, y - PROPERTY_HEIGHT, PROPERTY_PORT_SIZE, PROPERTY_PORT_SIZE);
        g2.setColor(fieldTypeColor);
        g2.fillOval(x + FIELD_PORT_BORDER_WIDTH, y - PROPERTY_HEIGHT + FIELD_PORT_BORDER_WIDTH, PROPERTY_PORT_SIZE - 2 * FIELD_PORT_BORDER_WIDTH, PROPERTY_PORT_SIZE - 2 * FIELD_PORT_BORDER_WIDTH);
    }

    private Map<String, Object> getFields(final Object bean) {
        Map<String, Object> fields;
        try {
            fields = PropertyUtils.describe(bean);
        } catch (Exception e) {
            throw new UnexpectedReflectionFailure(e);
        }
        fields.remove("class");
        fields.remove("currentValue");
        fields.remove("currentTime_s");
        return fields;
    }
}

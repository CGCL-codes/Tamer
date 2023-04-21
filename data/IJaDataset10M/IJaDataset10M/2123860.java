package beantable;

import java.awt.*;
import java.awt.event.*;
import java.beans.BeanDescriptor;
import java.beans.Customizer;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.beans.SimpleBeanInfo;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import junit.framework.TestCase;
import propertyeditors.*;
import com.jgoodies.binding.*;
import com.jgoodies.binding.adapter.*;
import com.jgoodies.binding.value.*;

public class AbstractBeanTestCase extends AbstractTestCase {

    protected Bean bean;

    protected Bean bean2;

    public AbstractBeanTestCase(final String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        final String[] path = PropertyEditorManager.getEditorSearchPath();
        assertNotNull(path);
        final ArrayList list = new ArrayList(Arrays.asList(path));
        list.add("propertyeditors");
        PropertyEditorManager.setEditorSearchPath((String[]) list.toArray(new String[list.size()]));
        final Bean bean = new Bean();
        bean.addPropertyChangeListener(new PropertyChangeListener() {

            public final void propertyChange(final PropertyChangeEvent event) {
                assertNotNull(event);
                System.out.println("Property " + event.getPropertyName() + " on " + event.getSource() + " changed from " + event.getOldValue() + " to " + event.getNewValue());
            }
        });
        this.bean = bean;
        bean.setCegg("Hoopy!");
        final Bean2 bean2 = new Bean2();
        bean2.addPropertyChangeListener(new PropertyChangeListener() {

            public final void propertyChange(final PropertyChangeEvent event) {
                assertNotNull(event);
                System.out.println(event.getPropertyName() + " changed from " + event.getOldValue() + " to " + event.getNewValue());
            }
        });
        this.bean2 = bean2;
    }

    public static class Bean {

        private final PropertyChangeSupport support;

        private Bean child;

        private Color color;

        private Date date;

        private String cegg;

        private Long interval;

        private URI uri;

        private URL url;

        private String gnozz;

        public Bean() {
            this(new Long(2L));
        }

        public Bean(final Long interval) {
            super();
            this.support = new PropertyChangeSupport(this);
            this.setBrazzInterval(interval);
        }

        public Bean getChild() {
            return this.child;
        }

        public void setChild(final Bean bean) {
            final Bean old = this.getChild();
            this.child = bean;
            this.firePropertyChange("child", old, bean);
        }

        public String getGnozz() {
            return this.gnozz;
        }

        public void setGnozz(final String gnozz) {
            final String old = this.getGnozz();
            this.gnozz = gnozz;
            this.firePropertyChange("gnozz", old, gnozz);
        }

        public String getFroo() {
            return "Froo";
        }

        public Long getBrazzInterval() {
            return this.interval;
        }

        public void setBrazzInterval(final Long interval) {
            final Long old = this.getBrazzInterval();
            this.interval = interval;
            this.firePropertyChange("brazzInterval", old, interval);
        }

        public URI getFrobURI() {
            return this.uri;
        }

        public void setFrobURI(final URI uri) {
            final URI old = this.getFrobURI();
            this.uri = uri;
            this.firePropertyChange("frobURI", old, uri);
        }

        public URL getBroogURL() {
            return this.url;
        }

        public void setBroogURL(final URL url) {
            final URL old = this.getBroogURL();
            this.url = url;
            this.firePropertyChange("broogURL", old, url);
        }

        public String getCegg() {
            return this.cegg;
        }

        public void setCegg(final String cegg) {
            final String old = this.getCegg();
            this.cegg = cegg;
            this.firePropertyChange("cegg", old, cegg);
        }

        public Date getDate() {
            return this.date;
        }

        public void setDate(final Date date) {
            final Date old = this.getDate();
            this.date = date;
            this.firePropertyChange("date", old, date);
        }

        public Color getColor() {
            return this.color;
        }

        public void setColor(final Color color) {
            final Color old = this.getColor();
            this.color = color;
            this.firePropertyChange("color", old, color);
        }

        public void addPropertyChangeListener(final String name, final PropertyChangeListener listener) {
            this.support.addPropertyChangeListener(name, listener);
        }

        public void addPropertyChangeListener(final PropertyChangeListener listener) {
            this.support.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(final String name, final PropertyChangeListener listener) {
            this.support.removePropertyChangeListener(name, listener);
        }

        public void removePropertyChangeListener(final PropertyChangeListener listener) {
            this.support.removePropertyChangeListener(listener);
        }

        public PropertyChangeListener[] getPropertyChangeListeners() {
            return this.support.getPropertyChangeListeners();
        }

        public PropertyChangeListener[] getPropertyChangeListeners(final String name) {
            return this.support.getPropertyChangeListeners(name);
        }

        protected void firePropertyChange(final String name, final Object old, final Object newValue) {
            this.support.firePropertyChange(name, old, newValue);
        }

        protected void firePropertyChange(final String name, final int old, final int newValue) {
            this.support.firePropertyChange(name, old, newValue);
        }

        protected void firePropertyChange(final String name, final boolean old, final boolean newValue) {
            this.support.firePropertyChange(name, old, newValue);
        }

        public String toString() {
            final StringBuffer buffer = new StringBuffer();
            buffer.append(String.valueOf(super.toString()));
            buffer.append(" (cegg:");
            buffer.append(String.valueOf(this.getCegg()));
            buffer.append(")");
            return buffer.toString();
        }
    }

    public static final class Bean2 extends Bean {

        private int fnegh;

        public Bean2() {
            this(new Long(2L));
        }

        public Bean2(final Long interval) {
            super(interval);
        }

        public int getFnegh() {
            return fnegh;
        }
    }

    public static final class MyCustomizer extends JTextField implements Customizer {

        private static final long serialVersionUID = 1L;

        private final PresentationModel presentationModel;

        public MyCustomizer() {
            super();
            this.setName("Wicked Cool Customizer");
            final ValueHolder beanChannel = new ValueHolder(null, true);
            beanChannel.addValueChangeListener(new PropertyChangeListener() {

                public final void propertyChange(final PropertyChangeEvent event) {
                    assertNotNull(event);
                    assertEquals("value", event.getPropertyName());
                    assertSame(beanChannel, event.getSource());
                    MyCustomizer.this.firePropertyChange("object", event.getOldValue(), event.getNewValue());
                }
            });
            this.presentationModel = new PresentationModel(beanChannel);
            Bindings.bind(MyCustomizer.this, this.presentationModel.getModel("cegg"), false);
        }

        public void setObject(final Object bean) {
            this.presentationModel.setBean(bean);
            System.out.println("Object set on customizer: " + bean);
        }
    }

    public static final class BeanBeanInfo extends SimpleBeanInfo {

        public BeanDescriptor getBeanDescriptor() {
            return new BeanDescriptor(Bean.class, MyCustomizer.class);
        }

        public PropertyDescriptor[] getPropertyDescriptors() {
            try {
                final PropertyDescriptor cegg = new PropertyDescriptor("cegg", Bean.class);
                cegg.setPreferred(true);
                final PropertyDescriptor date = new PropertyDescriptor("date", Bean.class);
                date.setDisplayName("The Date");
                return new PropertyDescriptor[] { cegg, date };
            } catch (final IntrospectionException kaboom) {
                return new PropertyDescriptor[0];
            }
        }
    }

    public static final class BeanEditor extends propertyeditors.PropertyEditorSupport {

        public BeanEditor() {
            super();
        }

        protected Object copy(final Object object) throws Exception {
            return object;
        }

        public void setValue(final Object object) {
            System.out.println("setValue() called with " + object);
            super.setValue(object);
        }

        public void setAsText(String text) {
            if (text == null) {
                text = "";
            } else {
                text = text.trim();
                assert text != null;
            }
            if (text.length() <= 0) {
                this.setValue(null);
            } else {
                final Bean bean = new Bean();
                bean.setCegg(text);
                this.setValue(bean);
            }
        }

        public String getAsText() {
            final Bean bean = (Bean) this.getValue();
            if (bean == null) {
                return "";
            }
            String cegg = bean.getCegg();
            if (cegg == null) {
                return "";
            }
            cegg = cegg.trim();
            assert cegg != null;
            return cegg;
        }
    }
}

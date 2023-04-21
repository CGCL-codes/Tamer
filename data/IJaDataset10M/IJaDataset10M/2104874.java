package beantable;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.net.URI;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JScrollPane;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import junit.framework.TestCase;

public class TestCasePresentationModelBasedBeanTableModel extends TestCase {

    public TestCasePresentationModelBasedBeanTableModel(final String name) {
        super(name);
    }

    public void testAll() throws Exception {
        final Runnable runnable = new Runnable() {

            public final void run() {
                final Bean bean = new Bean();
                final PresentationModelBasedBeanTableModel model = new PresentationModelBasedBeanTableModel(bean);
                assertNull(bean.getURI());
                ValueModel vm = model.getModel("URI");
                assertNotNull(vm);
                assertNull(vm.getValue());
                assertNull(model.getValue("URI"));
                int rowNumber = model.getRow("URI");
                assertTrue(rowNumber >= 0);
                URI uri = URI.create("http://www.boston.com");
                vm.setValue(uri);
                assertSame(uri, vm.getValue());
                rowNumber = model.getRow("URI");
                assertTrue(rowNumber >= 0);
                assertSame(uri, model.getValue("URI"));
                assertSame(uri, model.getValueAt(rowNumber, 1));
                uri = URI.create("http://www.yahoo.com");
                model.setValueAt(uri, rowNumber, 1);
                model.triggerCommit();
                rowNumber = model.getRow("URI");
                assertTrue(rowNumber >= 0);
                assertSame(uri, vm.getValue());
                assertSame(uri, model.getValue("URI"));
                assertSame(uri, model.getValueAt(rowNumber, 1));
            }
        };
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            EventQueue.invokeLater(runnable);
        }
    }

    public final class Bean {

        private Date birthDate;

        private String firstName;

        private int shoeSize;

        private URI uri;

        private PropertyChangeSupport propertyChangeSupport;

        public Bean() {
            super();
        }

        public URI getURI() {
            return this.uri;
        }

        public void setURI(final URI uri) {
            final URI old = this.getURI();
            this.uri = uri;
            this.firePropertyChange("URI", old, uri);
        }

        public String getFirstName() {
            return this.firstName;
        }

        public void setFirstName(final String firstName) {
            final String old = this.getFirstName();
            this.firstName = firstName;
            this.firePropertyChange("firstName", old, firstName);
        }

        public Date getBirthDate() {
            return this.birthDate;
        }

        public void setBirthDate(final Date birthDate) {
            final Date old = this.getBirthDate();
            this.birthDate = birthDate;
            this.firePropertyChange("birthDate", old, birthDate);
        }

        public int getShoeSize() {
            return this.shoeSize;
        }

        public void setShoeSize(final int shoeSize) {
            final int old = this.getShoeSize();
            this.shoeSize = shoeSize;
            this.firePropertyChange("shoeSize", old, shoeSize);
        }

        public void addPropertyChangeListener(final String name, final PropertyChangeListener listener) {
            if (listener != null) {
                if (this.propertyChangeSupport == null) {
                    this.propertyChangeSupport = new PropertyChangeSupport(this);
                }
                this.propertyChangeSupport.addPropertyChangeListener(name, listener);
            }
        }

        public void addPropertyChangeListener(final PropertyChangeListener listener) {
            if (listener != null) {
                if (this.propertyChangeSupport == null) {
                    this.propertyChangeSupport = new PropertyChangeSupport(this);
                }
                this.propertyChangeSupport.addPropertyChangeListener(listener);
            }
        }

        public void removePropertyChangeListener(final String name, final PropertyChangeListener listener) {
            if (listener != null && this.propertyChangeSupport != null) {
                this.propertyChangeSupport.removePropertyChangeListener(name, listener);
            }
        }

        public void removePropertyChangeListener(final PropertyChangeListener listener) {
            if (listener != null && this.propertyChangeSupport != null) {
                this.propertyChangeSupport.removePropertyChangeListener(listener);
            }
        }

        public PropertyChangeListener[] getPropertyChangeListeners(final String name) {
            if (this.propertyChangeSupport != null) {
                return this.propertyChangeSupport.getPropertyChangeListeners(name);
            }
            return new PropertyChangeListener[0];
        }

        public PropertyChangeListener[] getPropertyChangeListeners() {
            if (this.propertyChangeSupport != null) {
                return this.propertyChangeSupport.getPropertyChangeListeners();
            }
            return new PropertyChangeListener[0];
        }

        protected final void firePropertyChange(final String propertyName, final Object old, final Object newValue) {
            if (this.propertyChangeSupport != null) {
                this.propertyChangeSupport.firePropertyChange(propertyName, old, newValue);
            }
        }

        protected final void firePropertyChange(final String propertyName, final int old, final int newValue) {
            if (this.propertyChangeSupport != null) {
                this.propertyChangeSupport.firePropertyChange(propertyName, old, newValue);
            }
        }

        protected void firePropertyChange(final String name, final boolean old, final boolean newValue) {
            if (this.propertyChangeSupport != null) {
                this.propertyChangeSupport.firePropertyChange(name, old, newValue);
            }
        }
    }
}

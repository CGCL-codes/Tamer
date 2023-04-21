package net.sf.jvibes.ui.views;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.View;
import net.infonode.docking.properties.DockingWindowProperties;
import net.sf.jvibes.JVibes;
import net.sf.jvibes.kernel.elements.Element;
import net.sf.jvibes.kernel.elements.Model;
import net.sf.jvibes.kernel.elements.Property;
import net.sf.jvibes.kernel.elements.Model.Event;
import org.apache.log4j.Logger;

public class ModelView extends View {

    private static final Logger __logger = Logger.getLogger(ModelView.class);

    private final class Listener implements Model.Listener {

        public void elementAdded(Event event) {
            Element e = event.getElement();
            _gridView.add(e);
        }

        public void elementRemoved(Event event) {
            Element e = event.getElement();
            _gridView.remove(e);
        }
    }

    private static final long serialVersionUID = 4202209887474795277L;

    private Model _model;

    private GridView _gridView;

    private final Listener _listener = new Listener();

    public ModelView(Model model) {
        super((String) model.getProperty(Element.PROPERTY_NAME).getValue(), null, null);
        setModel(model);
        addListener(new DockingWindowAdapter() {

            @Override
            public void windowShown(DockingWindow window) {
                super.windowShown(window);
                JVibes.getWorkspace().setSelected(_model);
            }

            @Override
            public void windowClosed(DockingWindow window) {
                super.windowClosed(window);
                JVibes.getWorkspace().remove(_model);
            }
        });
        add(new JToolBar(JToolBar.VERTICAL), BorderLayout.WEST);
    }

    public void setModel(Model model) {
        if (_model == model) return;
        if (_model != null) _model.removeEventListener(_listener);
        _gridView = null;
        _model = model;
        if (_model != null) {
            _model.addListener(_listener);
            _gridView = (GridView) GridViewFactory.createGridView(_model);
            Property nameProp = _model.getProperty(Element.PROPERTY_NAME);
            nameProp.addPropertyChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    __logger.trace("Received pce (property: " + evt.getPropertyName() + " , new value: " + evt.getNewValue() + ")");
                    getViewProperties().setTitle((String) evt.getNewValue());
                }
            });
            getViewProperties().setTitle((String) nameProp.getValue());
        }
        JScrollPane js = new JScrollPane(_gridView);
        JToolBar jToolBar = new JToolBar(JToolBar.VERTICAL);
        setComponent(js);
        DockingWindowProperties properties = getWindowProperties();
        properties.setUndockEnabled(false);
        properties.setMinimizeEnabled(false);
    }

    public Model getModel() {
        return _model;
    }
}

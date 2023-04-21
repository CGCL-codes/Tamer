package net.entelijan.cobean.bind.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.entelijan.cobean.bind.IBeanProperty;
import net.entelijan.cobean.bind.IBinding;
import net.entelijan.cobean.bind.IModelChangeListener;
import net.entelijan.cobean.bind.IRemovableFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BoundPropBinding implements IBinding<Object> {

    private static Log log = LogFactory.getLog(BoundPropBinding.class);

    private static final long serialVersionUID = 1L;

    private String modelPath;

    private String compProperty;

    private Collection<Object> notifyingObjects;

    public BoundPropBinding() {
        super();
    }

    public IModelChangeListener createModelChangeListener(IBeanProperty beanProperty, Object comp, IRemovableFactory removableFactory) {
        throw new IllegalStateException("Should never be called");
    }

    public String getModelProperty() {
        return getModelPath();
    }

    public <T> T bind(T model, Object comp) {
        log.debug("[bind]  modelPath=" + modelPath + " viewProperty=" + compProperty + " model=" + model + " comp=" + comp);
        List<String> pathList = Arrays.asList(this.modelPath.split("\\."));
        IBinding<Object> binding = createBinding(pathList, model);
        return binding.bind(model, comp);
    }

    private IBinding<Object> createBinding(List<String> path, Object model) {
        if (path == null || path.size() <= 0) {
            throw new IllegalArgumentException("Illegal path:'" + path + "'. Size must be at least 1");
        }
        if (path.size() == 1) {
            final IBinding<Object> re = createTailBinding(path.get(0));
            log.debug("[createBinding] re='" + re + "' path=" + path + " model='" + model + "'");
            return re;
        } else {
            final NestedBinding<Object> re = createNestedBinding(path, model);
            log.debug("[createBinding] re='" + re + "' path=" + path + " model='" + model + "'");
            return re;
        }
    }

    private IBinding<Object> createTailBinding(String modelProperty) {
        BoundPropertyBinding re = null;
        try {
            re = new BoundPropertyBinding();
            re.setModelProperty(modelProperty);
            re.setComponentProperty(compProperty);
        } catch (Exception e) {
            throw new IllegalStateException("Could not create binding for '" + re + "'. " + e.getMessage(), e);
        }
        return re;
    }

    private NestedBinding<Object> createNestedBinding(List<String> path, Object model) {
        NestedBinding<Object> re = new NestedBinding<Object>();
        String modelProperty = path.get(0);
        re.setModelProperty(modelProperty);
        if (model != null) {
            if (notifyingObjects == null) {
                notifyingObjects = new ArrayList<Object>();
            }
            BeanWrapper bw = new BeanWrapperImpl(model);
            Object nextObj = bw.getPropertyValue(modelProperty);
            notifyingObjects.add(model);
            re.setInnerBinding(createBinding(restList(path), nextObj));
        } else {
            re.setInnerBinding(createBinding(restList(path), null));
        }
        return re;
    }

    private <U> List<U> restList(List<U> path) {
        ArrayList<U> re = new ArrayList<U>();
        for (int i = 1; i < path.size(); i++) {
            re.add(path.get(i));
        }
        return re;
    }

    public void setModelPath(String path) {
        this.modelPath = path;
    }

    public String getModelPath() {
        return this.modelPath;
    }

    public String getCompProperty() {
        return compProperty;
    }

    public void setCompProperty(String viewProperty) {
        this.compProperty = viewProperty;
    }
}

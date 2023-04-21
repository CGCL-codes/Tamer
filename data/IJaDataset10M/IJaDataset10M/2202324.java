package org.jrcaf.mvc.internal.part.mvc.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jrcaf.core.model.ModelEventManager;
import org.jrcaf.mvc.MVCPlugin;
import org.jrcaf.mvc.annotations.Data.Visibility;
import org.jrcaf.mvc.internal.mapper.BasePropertyDataAccessor;
import org.jrcaf.mvc.internal.part.mvc.accessor.data.ControllerDataAccessor;
import org.jrcaf.mvc.mapper.IObservableFactory;
import org.jrcaf.mvc.part.IPart;
import org.jrcaf.mvc.part.mvc.IController;
import org.jrcaf.mvc.registry.IPartRegistry;

/**
 *  Accesses the models in the IController.
 */
public class ControllerAccessor extends BasePropertiesAccessor {

    private static final List<String> STOP_LIST = Arrays.asList(new String[] { ".$assertionsDisabled", ".init" });

    private final List<ControllerDataAccessor> accessors;

    private final IPart part;

    private final Map<String, ModelAccessor> modelAccessors;

    /**
    * Creates a new ControllerAccessor
    * @param aPart The part the IController is part of.
    */
    public ControllerAccessor(IPart aPart) {
        super("");
        part = aPart;
        accessors = new ArrayList<ControllerDataAccessor>();
        modelAccessors = new HashMap<String, ModelAccessor>();
        calcDataAccessors(part.getController().getClass(), null);
    }

    /**
    *  Maps the parameters of the parameter Map to the models of the IController.
    */
    public void map2Controller() {
        for (ControllerDataAccessor accessor : accessors) {
            String name = accessor.getName();
            if (part.getMapper().canGetModelInParameters(name) == true) try {
                Object modelValue = part.getMapper().getModelInParameters(name);
                accessor.setValue(modelValue, part.getController());
            } catch (IllegalArgumentException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping to controller. data: " + name, ex));
            } catch (IllegalAccessException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping to controller. data: " + name, ex));
            } catch (InvocationTargetException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping to controller. data: " + name, ex));
            }
        }
    }

    /**
    *  Maps the models of the IController to the "parameter" Map.
    */
    public void map2Parameter() {
        part.getMapper().setResultValueOf(IPartRegistry.CONTROLLER_RESULT_NAME, getController());
        for (ControllerDataAccessor accessor : accessors) {
            String name = accessor.getName();
            if ((accessor.canGetModelValueOf(name)) && (isNotInStopList(name)) && accessor.isMinVisibility(Visibility.PUBLIC)) try {
                Object model = accessor.getValue(part.getController());
                part.getMapper().setResultValueOf(name, model);
            } catch (IllegalArgumentException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping to parameter. data: " + name, ex));
            } catch (IllegalAccessException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping to parameter. data: " + name, ex));
            } catch (InvocationTargetException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping to parameter. data: " + name, ex));
            }
        }
    }

    /**
    * Checks if an attribute is in the stop list.
    * @param aName The name of the accessed data.
    * @return true if the named attribute is not in the stop list.
    */
    private boolean isNotInStopList(String aName) {
        return STOP_LIST.contains(aName) == false;
    }

    /**
    * @return Returns the controller accessors.
    */
    public List<ControllerDataAccessor> getControllerAccessors() {
        return accessors;
    }

    /**
    * @return Returns the IController.
    */
    public IController getController() {
        return part.getController();
    }

    /**
    * @param aName The name of the model.
    * @return Returns true, if the model can be found. 
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    */
    public boolean canGetModelValueOf(String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        boolean check = false;
        for (ControllerDataAccessor accessor : accessors) {
            if (accessor.canGetModelValueOf(aName) == true) {
                check = true;
                break;
            }
        }
        if (check == false) return false;
        Object model = getModel(aName);
        String parentName = getParentName(aName);
        if (aName.equals(parentName)) return true;
        ModelAccessor modelAccessor = getModelAccessor(parentName);
        return modelAccessor.canGetModelValueOf(model, aName, parentName);
    }

    private ModelAccessor getModelAccessor(String aParentName) {
        ModelAccessor modelAccessor = modelAccessors.get(aParentName);
        if (modelAccessor == null) {
            modelAccessor = new ModelAccessor(aParentName);
            modelAccessors.put(aParentName, modelAccessor);
        }
        return modelAccessor;
    }

    /**
    * @param aName The name of the model.
    * @return Returns true, if the model can be found. 
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    */
    public boolean canSetModelValueOf(String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        boolean check = false;
        for (ControllerDataAccessor accessor : accessors) {
            if (accessor.canSetModelValueOf(aName) == true) {
                check = true;
                break;
            }
        }
        if (check == false) return false;
        Object model = getModel(aName);
        String parentName = getParentName(aName);
        if (aName.equals(parentName)) return true;
        ModelAccessor modelAccessor = getModelAccessor(parentName);
        return modelAccessor.canSetModelValueOf(model, aName);
    }

    /**
    * @param aName The name of the model.
    * @return Returns the model named aName.
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
    public Object getModel(String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (ControllerDataAccessor accessor : accessors) {
            if (accessor.canGetModelValueOf(aName) == true) return accessor.getValue(part.getController());
        }
        return null;
    }

    /**
    * @param aName The Name
    * @return Returns the parent part of the passed name.
    */
    private String getParentName(String aName) {
        for (ControllerDataAccessor accessor : accessors) {
            if (accessor.canGetModelValueOf(aName) == true) return accessor.getName();
        }
        return null;
    }

    /**
    * Sets the model in the IController 
    * @param aValue The model value. 
    * @param aName The name of the model.
    */
    public void setModel(Object aValue, String aName) {
        for (ControllerDataAccessor accessor : accessors) {
            if (accessor.canGetModelValueOf(aName) == true) try {
                accessor.setValue(aValue, part.getController());
            } catch (IllegalArgumentException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error setting model value. data: " + aName, ex));
            } catch (IllegalAccessException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error setting model value. data: " + aName, ex));
            } catch (InvocationTargetException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error setting model value. data: " + aName, ex));
            }
        }
    }

    /**
    * @return Returns the {@link IPart} accessed by this accessor.
    * @category Getter
    */
    public IPart getPart() {
        return part;
    }

    /**
    * @see org.jrcaf.mvc.internal.part.mvc.accessor.BasePropertiesAccessor#addDataAccessorFor(java.lang.reflect.Method, java.lang.reflect.Method, java.lang.reflect.Field, java.util.List)
    */
    @SuppressWarnings("unchecked")
    @Override
    protected void addDataAccessorFor(Method aGetterMethod, Method aSetterMethod, Field aField, List<? extends BasePropertyDataAccessor> aResult) {
        accessors.add(new ControllerDataAccessor(aGetterMethod, aSetterMethod, aField, this));
    }

    /**
    *  TODO JavaDoc
    * @param aName
    * @return
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    */
    public Object getModelValueOf(String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (canGetModelValueOf(aName) == true) {
            Object model = getModel(aName);
            String parentName = getParentName(aName);
            ModelEventManager.getSharedInstance().addObserver(getPart().getMapper(), model);
            if (aName.equals(parentName)) return model;
            ModelAccessor modelAccessor = getModelAccessor(parentName);
            return modelAccessor.getModelValueOf(model, aName, null);
        }
        return null;
    }

    /**
    * Sets the model property with the passed name to the passed value.
    * @param aValue The value to set.
    * @param aName The name of the property.
    * @throws InvocationTargetException On an exception while reflecting.
    * @throws IllegalAccessException On an exception while reflecting.
    * @throws IllegalArgumentException On an exception while reflecting.
    * @category Logic 
    */
    public void setModelValueOf(Object aValue, String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (canSetModelValueOf(aName) == true) {
            Object model = getModel(aName);
            String parentName = getParentName(aName);
            if (aName.equals(parentName)) {
                setModel(aValue, aName);
                return;
            }
            ModelAccessor modelAccessor = getModelAccessor(parentName);
            modelAccessor.setModelValueOf(model, aValue, aName, parentName);
        }
    }

    /**
    *  TODO JavaDoc
    * @param aModel
    * @param aName
    * @param aParentName 
    * @param aEmpty_string
    * @return
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    */
    public Object getModelValueOf(Object aModel, String aName, String aParentName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        ModelAccessor modelAccessor = getModelAccessor(aParentName);
        return modelAccessor.getModelValueOf(aModel, aName, aParentName);
    }

    /**
    *  TODO JavaDoc
    * @param aName
    * @return
    * @category TODO category
    */
    public Class<?> getModelTypeOf(String aName) {
        try {
            if (canGetModelValueOf(aName) == true) {
                Object model = getModel(aName);
                String parentName = getParentName(aName);
                if (aName.equals(parentName)) return model.getClass();
                ModelAccessor modelAccessor = getModelAccessor(parentName);
                return modelAccessor.getModelTypeOf(model, aName, null);
            }
        } catch (IllegalArgumentException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InvocationTargetException ex) {
        }
        return null;
    }

    /**
    *  TODO JavaDoc
    * @param aName
    * @return
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    * @category TODO category
    */
    public IObservableValue getModelObservable(String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (canGetModelValueOf(aName) == true) {
            String parentName = getParentName(aName);
            if (aName.equals(parentName)) return BeansObservables.observeValue(part.getController(), aName);
            Object model = getModel(aName);
            int indexOf = aName.lastIndexOf(IPartRegistry.DATA_SEPERATOR);
            return BeansObservables.observeValue(model, aName.substring(indexOf + 1));
        }
        return null;
    }

    /**
    *  TODO JavaDoc
    * @param aName
    * @return
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    * @category TODO category
    */
    public IObservableList getModelObservableList(String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (canGetModelValueOf(aName) == true) {
            String parentName = getParentName(aName);
            if (aName.equals(parentName)) return BeansObservables.observeList(Realm.getDefault(), part.getController(), aName);
            Object model = getModel(aName);
            return createModelObservableList(aName, model);
        }
        return null;
    }

    private IObservableList createModelObservableList(String aName, Object model) {
        IObservableList observeableList = null;
        List<IObservableFactory> observableFactories = MVCPlugin.getObservableFactoryRegistry().getObservableFactories();
        for (IObservableFactory observableFactory : observableFactories) {
        }
        return observeableList;
    }
}

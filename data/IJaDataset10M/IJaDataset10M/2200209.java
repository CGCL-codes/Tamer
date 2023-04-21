package org.jrcaf.mvc.internal.part.mvc.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.internal.databinding.internal.swt.SWTProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.jrcaf.core.model.ModelEvent;
import org.jrcaf.core.registry.IPluginXMLAndParameterNameStrings;
import org.jrcaf.mvc.MVCPlugin;
import org.jrcaf.mvc.annotations.AccessType;
import org.jrcaf.mvc.annotations.SubView;
import org.jrcaf.mvc.internal.mapper.BasePropertyDataAccessor;
import org.jrcaf.mvc.internal.mapper.ReflectionUtils;
import org.jrcaf.mvc.internal.mapper.SubViewMapper;
import org.jrcaf.mvc.internal.part.mvc.accessor.data.BaseViewDataAccessor;
import org.jrcaf.mvc.internal.part.mvc.accessor.handler.ActionHandlerAccessor;
import org.jrcaf.mvc.internal.part.mvc.accessor.handler.EventSourceAccessor;
import org.jrcaf.mvc.internal.part.mvc.accessor.handler.HandlerMethodAccessor;
import org.jrcaf.mvc.mapper.IMapper;
import org.jrcaf.mvc.part.IPart;
import org.jrcaf.mvc.part.mvc.IViewController;
import org.jrcaf.mvc.part.rule.detector.SelectionDetector;
import org.jrcaf.mvc.registry.IPartRegistry;
import org.jrcaf.mvc.validator.IValidationResultStrategie;

/**
 *  Accesses the fields, getter-methods and setter-methods of the view.
 *  The name of the accessed data has to be declared in @Data or by naming
 *  convention. 
 */
public class ViewAccessor extends BasePropertiesAccessor implements ModifyListener, SelectionListener {

    private final List<BaseViewDataAccessor> viewFieldAccessors;

    private final IPart part;

    private final List<SubViewMapper> subViewMappers;

    private final Map<SubViewMapper, BaseViewDataAccessor> subViewMapperMap;

    /**
    * Creates a ViewAccessor.
    * @param aPart The part to be accessed.
    * @param aName The name of the accessor.
    * @category Init
    */
    public ViewAccessor(IPart aPart, String aName) {
        super(aName);
        part = aPart;
        viewFieldAccessors = new ArrayList<BaseViewDataAccessor>();
        subViewMappers = new ArrayList<SubViewMapper>();
        subViewMapperMap = new HashMap<SubViewMapper, BaseViewDataAccessor>();
    }

    /**
    * Initializes the accessor.
    * @category Init
    */
    public void init() {
        mapHandler();
        mapActions();
        calcViewAccessors(part.getViewComposite());
        listenForChanges(part.getViewComposite());
        listenForValidation();
        for (SubViewMapper subView : subViewMappers) try {
            subView.setParentComposite((Composite) subViewMapperMap.get(subView).readValue(part.getViewComposite()));
        } catch (IllegalArgumentException ex) {
            MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error setting parent composite for: " + subView.getName(), ex));
        } catch (IllegalAccessException ex) {
            MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error setting parent composite for: " + subView.getName(), ex));
        } catch (InvocationTargetException ex) {
            MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error setting parent composite for: " + subView.getName(), ex));
        }
    }

    /** @category Calc */
    private void calcViewAccessors(Composite aWidget) {
        calcDataAccessors(aWidget.getClass(), null);
        if (true) return;
        Class<? extends Composite> clazz = aWidget.getClass();
        Map<String, Method> getterMap = new HashMap<String, Method>();
        Map<String, Method> setterMap = new HashMap<String, Method>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            AccessType accessType = ReflectionUtils.getAccessType(method);
            switch(accessType) {
                case GETTER:
                    {
                        method.setAccessible(true);
                        getterMap.put(ReflectionUtils.getDataName(method), method);
                        break;
                    }
                case SETTER:
                    {
                        method.setAccessible(true);
                        setterMap.put(ReflectionUtils.getDataName(method), method);
                        break;
                    }
                default:
                    {
                        continue;
                    }
            }
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = ReflectionUtils.getDataName(field);
            Method getterMethod = getterMap.remove(name);
            Method setterMethod = setterMap.remove(name);
            bind(name, aWidget, field, getterMethod, setterMethod);
        }
        for (String name : getterMap.keySet()) {
            Method getterMethod = getterMap.get(name);
            Method setterMethod = setterMap.remove(name);
            bind(name, aWidget, null, getterMethod, setterMethod);
        }
        for (Map.Entry<String, Method> setterEntry : setterMap.entrySet()) {
            Method setterMethod = setterEntry.getValue();
            String name = setterEntry.getKey();
            bind(name, aWidget, null, null, setterMethod);
        }
    }

    @SuppressWarnings("restriction")
    private void bind(String aName, Composite aWidget, Field aField, Method aGetterMethod, Method aSetterMethod) {
        String name = getName() + IPartRegistry.DATA_SEPERATOR + aName;
        IMapper mapper = part.getMapper();
        DataBindingContext context = mapper.getContext();
        Object value = null;
        if (aGetterMethod != null) try {
            value = aGetterMethod.invoke(aWidget, new Object[0]);
        } catch (IllegalArgumentException ex) {
            return;
        } catch (IllegalAccessException ex) {
            return;
        } catch (InvocationTargetException ex) {
            return;
        } else if (aField != null) try {
            value = aField.get(aWidget);
        } catch (IllegalArgumentException ex) {
            return;
        } catch (IllegalAccessException ex) {
            return;
        } else return;
        if ((value instanceof Table) || (value instanceof Tree) || (value instanceof org.eclipse.swt.widgets.List) || (value instanceof Combo)) {
            if (mapper.canGetModelObservableList(name) == true) {
                IObservableList modelObservableList = mapper.getModelObservableList(name);
                IObservableList targetObservableList = new WritableList();
                ObservableListContentProvider contentProvider = new ObservableListContentProvider();
                UpdateListStrategy targetToModel = new UpdateListStrategy();
                UpdateListStrategy modelToTarget = new UpdateListStrategy();
                context.bindList(targetObservableList, modelObservableList, targetToModel, modelToTarget);
            }
        } else if (value instanceof Control) {
            if (mapper.canGetModelObservable(name) == true) {
                IObservableValue modelObservableValue = mapper.getModelObservable(name);
                IObservableValue targetObservableValue = null;
                String target = ReflectionUtils.getTargetFor(aField, aGetterMethod, aSetterMethod);
                if (target.length() == 0) target = getDefaultTargetFor(value);
                if (target.length() == 0) {
                    return;
                }
                if (target.equals(SWTProperties.BACKGROUND)) targetObservableValue = SWTObservables.observeBackground((Control) value); else if (target.equals("editable")) targetObservableValue = SWTObservables.observeEditable((Control) value); else if (target.equals(SWTProperties.ENABLED)) targetObservableValue = SWTObservables.observeEnabled((Control) value); else if (target.equals(SWTProperties.FONT)) targetObservableValue = SWTObservables.observeFont((Control) value); else if (target.equals(SWTProperties.FOREGROUND)) targetObservableValue = SWTObservables.observeForeground((Control) value); else if (target.equals(SWTProperties.MAX)) targetObservableValue = SWTObservables.observeMax((Control) value); else if (target.equals(SWTProperties.MIN)) targetObservableValue = SWTObservables.observeMin((Control) value); else if (target.equals(SWTProperties.SELECTION)) targetObservableValue = SWTObservables.observeSelection((Control) value); else if (target.equals(SWTProperties.SELECTION_INDEX)) targetObservableValue = SWTObservables.observeSingleSelectionIndex((Control) value); else if (target.equals(SWTProperties.TEXT)) {
                    if (value instanceof Text) targetObservableValue = SWTObservables.observeText((Control) value, ReflectionUtils.getUpdateEventFor(aField, aGetterMethod, aSetterMethod)); else targetObservableValue = SWTObservables.observeText((Control) value);
                } else if (target.equals(SWTProperties.TOOLTIP_TEXT)) targetObservableValue = SWTObservables.observeTooltipText((Control) value); else if (target.equals(SWTProperties.VISIBLE)) targetObservableValue = SWTObservables.observeVisible((Control) value);
                UpdateValueStrategy targetToModel = new UpdateValueStrategy();
                UpdateValueStrategy modelToTarget = new UpdateValueStrategy();
                context.bindValue(targetObservableValue, modelObservableValue, targetToModel, modelToTarget);
            }
        } else {
            if (mapper.canGetModelObservable(name) == true) {
                IObservableValue modelObservableValue = mapper.getModelObservable(name);
                IObservableValue targetObservableValue = BeansObservables.observeValue(aWidget, name);
                UpdateValueStrategy targetToModel = new UpdateValueStrategy();
                UpdateValueStrategy modelToTarget = new UpdateValueStrategy();
                if ((aGetterMethod != null) && (aSetterMethod != null)) {
                    targetObservableValue = BeansObservables.observeValue(aWidget, name);
                } else if (aField != null) {
                    targetObservableValue = createObservableValueFor(aField, aGetterMethod, aSetterMethod, aWidget);
                } else return;
                context.bindValue(targetObservableValue, modelObservableValue, targetToModel, modelToTarget);
            }
        }
    }

    /**
    *  TODO JavaDoc
    * @param aField
    * @param aGetterMethod
    * @param aSetterMethod
    * @param aWidget
    * @return
    * @category TODO category
    */
    private IObservableValue createObservableValueFor(Field aField, Method aGetterMethod, Method aSetterMethod, Composite aWidget) {
        return new AbstractObservableValue() {

            @Override
            protected Object doGetValue() {
                return null;
            }

            public Object getValueType() {
                return null;
            }
        };
    }

    /**
    *  TODO JavaDoc
    * @param aValue
    * @return
    * @category TODO category
    */
    @SuppressWarnings("restriction")
    private String getDefaultTargetFor(Object aWidget) {
        if (aWidget instanceof Text) return SWTProperties.TEXT;
        if (aWidget instanceof Button) return SWTProperties.SELECTION;
        if (aWidget instanceof Label) return SWTProperties.TEXT;
        return IPluginXMLAndParameterNameStrings.EMPTY_STRING;
    }

    /**
    * Maps the model of the part to the accessed view.
    * @param aEvent The event triggering the maping.
    * @category Logic 
    */
    public void mapModel2View(ModelEvent aEvent) {
        for (BaseViewDataAccessor viewAccessor : viewFieldAccessors) if (viewAccessor.needsUpdate(aEvent) == true) {
            String[] names = viewAccessor.getNames();
            for (String name : names) try {
                if (part.getMapper().canGetModelValueOf(name) == true) {
                    Object modelValue = part.getMapper().getModelValueOf(name);
                    viewAccessor.setValue(modelValue, part.getViewComposite(), name);
                }
            } catch (IllegalArgumentException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping model2View. data: " + name, ex));
            } catch (IllegalAccessException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping model2View. data: " + name, ex));
            } catch (InvocationTargetException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping model2View. data: " + name, ex));
            }
        }
        for (IMapper subViewMapper : subViewMappers) subViewMapper.mapModel2View(aEvent);
    }

    /**
    * Maps the accessed view to the model of the part. 
    * @param aEvent The event triggering the maping.
    * @category Logic 
    */
    public void mapView2Model(ModelEvent aEvent) {
        for (BaseViewDataAccessor viewAccessor : viewFieldAccessors) if (viewAccessor.canGetValue(aEvent) == true) {
            String[] names = viewAccessor.getNames();
            for (String name : names) try {
                if (part.getMapper().canSetModelValueOf(name) == true) viewAccessor.putValue(part.getViewComposite(), part.getMapper(), name);
            } catch (IllegalArgumentException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping View2Model. data: " + name, ex));
            } catch (IllegalAccessException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping View2Model. data: " + name, ex));
            } catch (InvocationTargetException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error mapping View2Model. data: " + name, ex));
            }
        }
        for (IMapper subViewMapper : subViewMappers) subViewMapper.mapView2Model(aEvent);
    }

    /**
    * @param aParent The parent widget. 
    * @param aName The name od the searched widget.
    * @return Returns the widget named aName
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    * @throws InvocationTargetException 
    * @category Logic 
    */
    public Widget getWidget(Widget aParent, String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (BaseViewDataAccessor viewAccessor : viewFieldAccessors) if (viewAccessor.getName().equals(aName) == true) return (Widget) viewAccessor.readValue(aParent);
        return null;
    }

    /**
    * @param aParent The parent widget.
    * @param aName The name of the widget.
    * @return The selected value of the widget named aName
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    * @category Logic 
    */
    public Object getSelectedValueOf(Widget aParent, String aName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (BaseViewDataAccessor viewAccessor : viewFieldAccessors) if (viewAccessor.getName().equals(aName) == true) return viewAccessor.getSelectedValueOf(aParent);
        return null;
    }

    /** @category Init */
    private void mapHandler() {
        List<HandlerMethodAccessor> handlerMethods = calcHandlerMethod(part.getViewController());
        List<EventSourceAccessor> eventSourceAccessors = calcEventSourceAccessors(part.getViewComposite());
        for (HandlerMethodAccessor handlerMethod : handlerMethods) {
            for (EventSourceAccessor accessor : eventSourceAccessors) {
                if (handlerMethod.matches(accessor) == true) try {
                    accessor.addHandler(handlerMethod);
                } catch (IllegalArgumentException ex) {
                    MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error in mapping handler for: " + accessor.getName(), ex));
                } catch (IllegalAccessException ex) {
                    MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error in mapping handler for: " + accessor.getName(), ex));
                }
            }
        }
    }

    /** @category Calc */
    private List<HandlerMethodAccessor> calcHandlerMethod(IViewController aViewController) {
        List<HandlerMethodAccessor> handlerMethods = new ArrayList<HandlerMethodAccessor>();
        for (Method method : aViewController.getClass().getDeclaredMethods()) {
            HandlerMethodAccessor handlerMethod = HandlerMethodAccessor.create(method, aViewController);
            if (handlerMethod != null) handlerMethods.add(handlerMethod);
        }
        return handlerMethods;
    }

    /** @category Calc */
    private List<EventSourceAccessor> calcEventSourceAccessors(Widget aWidget) {
        List<EventSourceAccessor> accessors = new ArrayList<EventSourceAccessor>();
        for (Field field : aWidget.getClass().getDeclaredFields()) {
            EventSourceAccessor accessor = EventSourceAccessor.createEventSource(field, aWidget);
            if (accessor != null) accessors.add(accessor);
        }
        return accessors;
    }

    /** @category Init */
    private void mapActions() {
        List<EventSourceAccessor> actionSourceAccessors = calcActionSourceAccessors(part.getViewComposite());
        for (EventSourceAccessor accessor : actionSourceAccessors) {
            ActionHandlerAccessor handlerMethod = new ActionHandlerAccessor(accessor.getName(), part, accessor.getCallType(), accessor.getDataNames(), accessor.getMapFlag(), part.getActionHandler());
            try {
                accessor.addHandler(handlerMethod);
            } catch (IllegalArgumentException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error in mapping actions for: " + accessor.getName(), ex));
            } catch (IllegalAccessException ex) {
                MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error in mapping actions for: " + accessor.getName(), ex));
            }
        }
    }

    /** @category Calc */
    private List<EventSourceAccessor> calcActionSourceAccessors(Widget aWidget) {
        List<EventSourceAccessor> accessors = new ArrayList<EventSourceAccessor>();
        for (Field field : aWidget.getClass().getDeclaredFields()) {
            EventSourceAccessor accessor = EventSourceAccessor.createActionSource(field, aWidget);
            if (accessor != null) accessors.add(accessor);
        }
        return accessors;
    }

    /** @category Logic */
    private void listenForChanges(Widget aWidget) {
        for (BaseViewDataAccessor accessor : viewFieldAccessors) try {
            accessor.listenForChanges(this, aWidget);
        } catch (IllegalArgumentException ex) {
            MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error in listen for changes in: " + accessor.getName(), ex));
        } catch (IllegalAccessException ex) {
            MVCPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, MVCPlugin.ID_PLUGIN, IStatus.OK, "Error in listen for changes in: " + accessor.getName(), ex));
        }
    }

    /** @category Logic */
    private void listenForValidation() {
        for (BaseViewDataAccessor accessor : viewFieldAccessors) {
            accessor.listenForValidation(part);
        }
    }

    /**
    * Sets the validation result of an accessed widget.
    * @param aValidated The validated object.
    * @param aFieldName The name of the data.
    * @param aResult The validation result.
    * @param aValidator The validator.
    * @param aStrategyParam The paramter for the strategy.
    * @category Logic
    */
    public void setValidationResult(Object aValidated, String aFieldName, IStatus aResult, IValidator aValidator, String aStrategyParam) {
        IValidationResultStrategie validationResultStrategie = part.getValidationResultStrategie(aFieldName);
        if (validationResultStrategie != null) validationResultStrategie.setValidationResult(aResult, aValidator, aValidated, aStrategyParam);
        part.validationResultChanged(validationResultStrategie, aFieldName, aValidated);
    }

    /**
    * Adds a sub view to the master.
    * @param aMasterName The name of the master to add to.
    * @param aSubViewMapper The sub view mapper.
    * @category Init
    */
    public void addSubViewToMaster(String aMasterName, SubViewMapper aSubViewMapper) {
        for (BaseViewDataAccessor accessor : viewFieldAccessors) if (accessor.addSubViewToMaster(aMasterName, aSubViewMapper) == true) aSubViewMapper.addMaster(accessor);
    }

    /**
    * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
    * @category Logic
    */
    public void modifyText(ModifyEvent aEvent) {
        part.getMapper().setDirty();
    }

    /**
    * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
    * @category Logic
    */
    public void widgetSelected(SelectionEvent aEvent) {
        part.getMapper().setDirty();
    }

    /**
    * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
    * @category Logic
    */
    public void widgetDefaultSelected(SelectionEvent aEvent) {
        part.getMapper().setDirty();
    }

    /**
    * Adds the detector to the named widget.
    * @param aDetectionTargetName The widget name.
    * @param aDetector The detector.
    * @category Init
    */
    public void addSelectionDetector(String aDetectionTargetName, SelectionDetector aDetector) {
        for (BaseViewDataAccessor viewAccessor : viewFieldAccessors) if (viewAccessor.getName().equals(aDetectionTargetName) == true) viewAccessor.addSelectionDetector(aDetector);
    }

    /**
    * Removes the detector from the named widget.
    * @param aDetectionTargetName The widget name.
    * @param aDetector The detector.
    * @category Init
    */
    public void removeSelectionDetector(String aDetectionTargetName, SelectionDetector aDetector) {
        for (BaseViewDataAccessor viewAccessor : viewFieldAccessors) if (viewAccessor.getName().equals(aDetectionTargetName) == true) viewAccessor.removeSelectionDetector(aDetector);
    }

    /**
    * @return Returns the mapper.
    * @category Getter
    */
    public IMapper getMapper() {
        return part.getMapper();
    }

    /**
    * @return Returns true if a subview is dirty.
    * @category Getter
    */
    public boolean isASubViewDirty() {
        for (SubViewMapper subViewMapper : subViewMappers) {
            if (subViewMapper.isDirty() == true) return true;
        }
        return false;
    }

    /**
    * @return Returns the view composite of the accessed part.
    * @category Getter
    */
    public Composite getViewComposite() {
        return part.getViewComposite();
    }

    @Override
    protected void addDataAccessorFor(Method aGetterMethod, Method aSetterMethod, Field aField, List<? extends BasePropertyDataAccessor> aResult) {
        if (aField != null) {
            SubView subViewAnnotation = aField.getAnnotation(SubView.class);
            if (subViewAnnotation == null) {
                viewFieldAccessors.add(ReflectionUtils.createViewDataAccessor(aGetterMethod, aSetterMethod, aField, this, getName(), part.getMapper()));
            } else {
                SubViewMapper subViewMapper = new SubViewMapper(aGetterMethod, aSetterMethod, aField, part);
                subViewMappers.add(subViewMapper);
                subViewMapperMap.put(subViewMapper, ReflectionUtils.createViewDataAccessor(aGetterMethod, aSetterMethod, aField, this, getName(), part.getMapper()));
            }
        } else viewFieldAccessors.add(ReflectionUtils.createViewDataAccessor(aGetterMethod, aSetterMethod, null, this, getName(), part.getMapper()));
    }
}

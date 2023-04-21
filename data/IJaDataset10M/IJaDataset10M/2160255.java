package com.koutra.dist.proc.designer.model.template;

import java.io.FileReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import com.koutra.dist.proc.designer.Messages;
import com.koutra.dist.proc.designer.model.ContentType;
import com.koutra.dist.proc.designer.model.ModelElement;
import com.koutra.dist.proc.designer.model.Template;
import com.koutra.dist.proc.designer.model.ValidationError;
import com.koutra.dist.proc.designer.properties.FileChooserPropertyDescriptor;
import com.koutra.dist.proc.model.ISink;

public class LineTransformingTemplate extends Template {

    public static final String TAG_SCRIPT = "script";

    public static final String TAG_ENGINE_NAME = "engineName";

    /** 
	 * A static array of property descriptors.
	 * There is one IPropertyDescriptor entry per editable property.
	 * @see #getPropertyDescriptors()
	 * @see #getPropertyValue(Object)
	 * @see #setPropertyValue(Object, Object)
	 */
    private static IPropertyDescriptor[] descriptors;

    /** Property ID to use for the script. */
    public static final String SCRIPT = "StreamDemux.Script";

    /** Property ID to use for the engine name. */
    public static final String ENGINE_NAME = "StreamDemux.EngineName";

    protected static String[] engineNameValues;

    static {
        List<String> engineNames = new ArrayList<String>();
        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = mgr.getEngineFactories();
        for (ScriptEngineFactory factory : factories) {
            if (factory.getScriptEngine() instanceof Invocable) {
                engineNames.add(factory.getLanguageName());
            }
        }
        engineNameValues = new String[engineNames.size()];
        engineNameValues = engineNames.toArray(engineNameValues);
        descriptors = new IPropertyDescriptor[] { new FileChooserPropertyDescriptor(SCRIPT, Messages.LineTransformingTemplate_ScriptPropertyDescription), new ComboBoxPropertyDescriptor(ENGINE_NAME, Messages.LineTransformingTemplate_ScriptEnginePropertyDescription, engineNameValues) };
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i].getId().equals(SCRIPT)) {
                ((PropertyDescriptor) descriptors[i]).setValidator(new ICellEditorValidator() {

                    public String isValid(Object value) {
                        return null;
                    }
                });
            } else if (descriptors[i].getId().equals(ENGINE_NAME)) {
                ((PropertyDescriptor) descriptors[i]).setValidator(new ICellEditorValidator() {

                    public String isValid(Object value) {
                        return null;
                    }
                });
            }
        }
    }

    protected String scriptEngineName;

    protected String script;

    public LineTransformingTemplate() {
        scriptEngineName = engineNameValues[0];
        script = "";
    }

    @Override
    public ModelElement deepCopy() {
        LineTransformingTemplate retVal = new LineTransformingTemplate();
        retVal.scriptEngineName = scriptEngineName;
        retVal.script = script;
        return retVal;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<IPropertyDescriptor> superList = new ArrayList<IPropertyDescriptor>(Arrays.asList(super.getPropertyDescriptors()));
        superList.addAll(Arrays.asList(descriptors));
        IPropertyDescriptor[] retVal = new IPropertyDescriptor[superList.size()];
        int counter = 0;
        for (IPropertyDescriptor pd : superList) {
            retVal[counter++] = pd;
        }
        return retVal;
    }

    private int getTargetTypeValue(String name) {
        for (int i = 0; i < engineNameValues.length; i++) {
            if (engineNameValues[i].equals(name)) return i;
        }
        throw new RuntimeException("Unknown target type.");
    }

    /**
	 * Return the property value for the given propertyId, or null.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array 
	 * to obtain the value of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
    public Object getPropertyValue(Object propertyId) {
        if (SCRIPT.equals(propertyId)) {
            return script;
        } else if (ENGINE_NAME.equals(propertyId)) {
            return getTargetTypeValue(scriptEngineName);
        } else {
            return super.getPropertyValue(propertyId);
        }
    }

    /**
	 * Set the property value for the given property id.
	 * If no matching id is found, the call is forwarded to the superclass.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array to set the values
	 * of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
    public void setPropertyValue(Object propertyId, Object value) {
        if (SCRIPT.equals(propertyId)) {
            String oldValue = script;
            script = (String) value;
            firePropertyChange(SCRIPT, oldValue, value);
        } else if (ENGINE_NAME.equals(propertyId)) {
            int oldValue = getTargetTypeValue(scriptEngineName);
            scriptEngineName = engineNameValues[(Integer) value];
            firePropertyChange(ENGINE_NAME, oldValue, value);
        } else {
            super.setPropertyValue(propertyId, value);
        }
    }

    @Override
    public boolean supportsInput(ContentType contentType) {
        switch(contentType) {
            case CharStream:
                return true;
        }
        return false;
    }

    @Override
    public boolean supportsOutput(ContentType contentType) {
        switch(contentType) {
            case CharStream:
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "LineXformTransform[" + scriptEngineName + "]";
    }

    protected com.koutra.dist.proc.pipeline.script.LineBasedReaderToWriterPipelineItemTemplate representation;

    @Override
    public void setUpExecution() {
        representation = new com.koutra.dist.proc.pipeline.script.LineBasedReaderToWriterPipelineItemTemplate(UUID.randomUUID().toString(), scriptEngineName, script);
        ModelElement sink = (ModelElement) getSinkConnection().getSink();
        sink.setUpExecution();
        ISink sinkRepresentation = (ISink) sink.getExecutionRepresentation();
        representation.hookupSink(sinkRepresentation);
    }

    @Override
    public Object getExecutionRepresentation() {
        if (representation == null) setUpExecution();
        return representation;
    }

    @Override
    public void dehydrate(IMemento memento) {
        memento.putString(TAG_SCRIPT, script);
        memento.putString(TAG_ENGINE_NAME, scriptEngineName);
        super.dehydrate(memento);
    }

    @Override
    public void hydrate(IMemento memento) {
        script = memento.getString(TAG_SCRIPT);
        scriptEngineName = memento.getString(TAG_ENGINE_NAME);
        super.hydrate(memento);
    }

    @Override
    public List<ValidationError> validate() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName(scriptEngineName);
        final Invocable invocableEngine = (Invocable) engine;
        try {
            engine.eval(new FileReader(script));
        } catch (Exception e) {
            return Collections.singletonList(new ValidationError(this, MessageFormat.format(Messages.LineTransformingTemplate_ScriptEvaluationError, script, e.getMessage())));
        }
        try {
            invocableEngine.invokeFunction("transformLine", "");
        } catch (Throwable t) {
            return Collections.singletonList(new ValidationError(this, MessageFormat.format(Messages.LineTransformingTemplate_ScriptInvocationError, t.getMessage())));
        }
        return Collections.emptyList();
    }
}

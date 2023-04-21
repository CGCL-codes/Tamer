package at.gp.web.jsf.extval.config.java;

import org.apache.myfaces.extensions.validator.core.initializer.component.ComponentInitializer;
import org.apache.myfaces.extensions.validator.core.interceptor.RendererInterceptor;
import org.apache.myfaces.extensions.validator.core.interceptor.MetaDataExtractionInterceptor;
import org.apache.myfaces.extensions.validator.core.interceptor.ValidationExceptionInterceptor;
import org.apache.myfaces.extensions.validator.core.recorder.ProcessedInformationRecorder;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Gerhard Petracek
 */
class DefaultExtValConfig implements ExtValConfig {

    private List<ConfigurationListener> configurationListeners = new ArrayList<ConfigurationListener>();

    private List<ComponentInitializer> componentInitializers = new ArrayList<ComponentInitializer>();

    private List<RendererInterceptor> rendererInterceptors = new ArrayList<RendererInterceptor>();

    private List<MetaDataExtractionInterceptor> metaDataExtractionInterceptors = new ArrayList<MetaDataExtractionInterceptor>();

    private List<ProcessedInformationRecorder> processedInformationRecorders = new ArrayList<ProcessedInformationRecorder>();

    private List<ValidationExceptionInterceptor> validationExceptionInterceptors = new ArrayList<ValidationExceptionInterceptor>();

    private List<Binding> bindingsToInstall = new ArrayList<Binding>();

    DefaultExtValConfig() {
    }

    public Binding createBinding(ConfigEntry entry) {
        Binding newBindingEntry = new Binding(entry);
        this.bindingsToInstall.add(newBindingEntry);
        return newBindingEntry;
    }

    public ConfigurationListener[] getConfigurationListeners() {
        return this.configurationListeners.toArray(new ConfigurationListener[this.configurationListeners.size()]);
    }

    public ComponentInitializer[] getComponentInitializers() {
        return this.componentInitializers.toArray(new ComponentInitializer[this.componentInitializers.size()]);
    }

    public RendererInterceptor[] getRendererInterceptors() {
        return this.rendererInterceptors.toArray(new RendererInterceptor[this.rendererInterceptors.size()]);
    }

    public MetaDataExtractionInterceptor[] getMetaDataExtractionInterceptors() {
        return this.metaDataExtractionInterceptors.toArray(new MetaDataExtractionInterceptor[this.metaDataExtractionInterceptors.size()]);
    }

    public ProcessedInformationRecorder[] getProcessedInformationRecorders() {
        return this.processedInformationRecorders.toArray(new ProcessedInformationRecorder[this.processedInformationRecorders.size()]);
    }

    public ValidationExceptionInterceptor[] getValidationExceptionInterceptors() {
        return this.validationExceptionInterceptors.toArray(new ValidationExceptionInterceptor[this.validationExceptionInterceptors.size()]);
    }

    public Binding[] getBindings() {
        return this.bindingsToInstall.toArray(new Binding[this.bindingsToInstall.size()]);
    }

    public void addConfigurationListener(ConfigurationListener configurationListener) {
        this.configurationListeners.add(configurationListener);
    }

    public void addComponentInitializer(ComponentInitializer componentInitializer) {
        this.componentInitializers.add(componentInitializer);
    }

    public void addRendererInterceptor(RendererInterceptor rendererInterceptor) {
        this.rendererInterceptors.add(rendererInterceptor);
    }

    public void addMetaDataExtractionInterceptor(MetaDataExtractionInterceptor metaDataExtractionInterceptor) {
        this.metaDataExtractionInterceptors.add(metaDataExtractionInterceptor);
    }

    public void addProcessedInformationRecorder(ProcessedInformationRecorder processedInformationRecorder) {
        this.processedInformationRecorders.add(processedInformationRecorder);
    }

    public void addValidationExceptionInterceptor(ValidationExceptionInterceptor validationExceptionInterceptor) {
        this.validationExceptionInterceptors.add(validationExceptionInterceptor);
    }
}

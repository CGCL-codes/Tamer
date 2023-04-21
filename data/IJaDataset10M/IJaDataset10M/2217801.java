package org.lokee.punchcard.config;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.lokee.punchcard.Constants;
import org.lokee.punchcard.Field;
import org.lokee.punchcard.IPunchCardMessages;
import org.lokee.punchcard.PunchCardUtil;
import org.lokee.punchcard.config.converter.Converter;
import org.lokee.punchcard.config.converter.ISinkConverter;
import org.lokee.punchcard.config.converter.ISourceConverter;
import org.lokee.punchcard.config.converter.IValueConverter;
import org.lokee.punchcard.config.dependency.IDependency;
import org.lokee.punchcard.rules.Rule;
import org.lokee.punchcard.util.ILogSeverity;
import org.lokee.punchcard.util.Utilities;

/**
 * @author claguerre
 * 
 * a single field of punchcard.
 */
public class FieldConfig extends PunchCardConfig implements IDependent {

    public static final String FIELD_TYPE_SELECT = "SELECT_FIELD";

    public static final String FIELD_TYPE_TEXT = "TEXT_FIELD";

    public static final String FIELD_TYPE_TEXT_AREA = "TEXT_AREA_FIELD";

    public static final String FIELD_TYPE_CHECKBOX = "CHECKBOX_FIELD";

    public static final String FIELD_TYPE_RADIO = "RADIO_FIELD";

    public static final String FIELD_TYPE_HIDDEN = "HIDDEN_FIELD";

    public static final String FIELD_TYPE_PASSWORD = "PASSWORD_FIELD";

    /**
	 * Holds value of property cardName
	 */
    private String cardName;

    /**
	 * Holds value of property type
	 */
    private String type;

    /**
	 * Holds value of property size
	 */
    private int size;

    /**
	 * Holds value of property cols
	 */
    private int cols;

    /**
	 * Holds value of property rows
	 */
    private int rows;

    /**
	 * Holds value of property alt
	 */
    private String alt;

    /**
	 * Holds value of property tabindex
	 */
    private int tabindex;

    /**
	 * Holds value of property id
	 */
    private String id;

    /**
	 * Holds value of property lang
	 */
    private String lang;

    /**
	 * Holds value of property title
	 */
    private String title;

    /**
	 * Holds value of property style
	 */
    private String style;

    /**
	 * Holds value of property styleClass
	 */
    private String styleClass;

    /**
	 * Holds value of property multiple
	 */
    private boolean multiple;

    /**
	 * Holds value of property disableScript
	 */
    private boolean disableScript;

    /**
	 * Holds value of property sourceName
	 */
    private String sourceName;

    /**
	 * Holds value of property singleSetFields
	 */
    private final Map singleSetFields;

    /**
	 * Holds value of property name
	 */
    private String name;

    /**
   * Holds value of property description
   */
    private String description;

    /**
	 * Holds value of property value
	 */
    private String value;

    /**
	 * Holds value of property maxlength
	 */
    private int maxlength;

    /**
	 * Holds value of property executeClassName
	 */
    private Class executeClass;

    /**
	 * Holds value of property primaryKey
	 */
    private boolean primaryKey;

    /**
	 * Holds value of property autoIncrement
	 */
    private boolean autoIncrement;

    /**
	 * Holds value of property writable
	 */
    private boolean writable;

    /**
	 * Holds value of property nullable
	 */
    private boolean nullable;

    /**
	 * Holds value of property referenced
	 */
    private boolean referenced;

    /**
	 * Holds value of property displayName
	 */
    private String displayName;

    /**
	 * Holds value of property format
	 */
    private String format;

    /**
	 * Holds value of property scripts
	 */
    private String scripts;

    private String queryScript;

    /**
	 * Holds value of property sequenceConfig
	 */
    private SequenceConfig sequenceConfig;

    /**
	 * Holds value of property displayRules
	 */
    private final Map displayRules;

    /**
	 * Holds value of property executionRules
	 */
    private final Map executionRules;

    /**
	 * Holds value of property validationRules
	 */
    private final Map validationRules;

    /**
	 * Holds value of property multiOptionConfig
	 */
    private final List multiOptionConfig;

    /**
	 * Holds value of property autoIncrementConfig
	 */
    private AutoIncrementConfig autoIncrementConfig;

    /**
	 * Holds value of property options
	 */
    private final List options;

    /**
	 * Holds value of property dependencies
	 */
    private final List dependencies;

    /**
	 * Holds value of property converter
	 */
    private Converter converter;

    /**
	 * Holds value of property valueConverter
	 */
    private IValueConverter valueConverter;

    /**
	 * Holds value of property sinkConverter
	 */
    private ISinkConverter sinkConverter;

    /**
	 * Holds value of property sourceConverter
	 */
    private ISourceConverter sourceConverter;

    /**
	 * Holds value of property binderConfig
	 */
    private BinderConfig binderConfig;

    /**
	 * Holds value of property searchValidationRules
	 */
    private final Map searchValidationRules;

    public FieldConfig() {
        displayRules = new LinkedHashMap();
        executionRules = new LinkedHashMap();
        validationRules = new LinkedHashMap();
        singleSetFields = new LinkedHashMap();
        options = new ArrayList();
        dependencies = new ArrayList();
        searchValidationRules = new LinkedHashMap();
        multiOptionConfig = new ArrayList();
    }

    public Field getFieldInstance() {
        return new Field(getName(), getValue(), isPrimaryKey());
    }

    public FieldConfig cloneFieldConfig() throws ConfigException {
        FieldConfig fieldConfig = new FieldConfig();
        populateClonedFieldConfig(fieldConfig);
        return fieldConfig;
    }

    protected void populateClonedFieldConfig(FieldConfig fieldConfig) throws ConfigException {
        Iterator itr = singleSetFields.keySet().iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            Class clazz = (Class) singleSetFields.get(fieldName);
            try {
                String prefix = "get";
                if (clazz.equals(boolean.class)) {
                    prefix = "is";
                }
                Object value = this.getClass().getMethod(prefix + Utilities.capitalize(fieldName), new Class[] {}).invoke(this, new Object[] {});
                fieldConfig.getClass().getMethod("set" + Utilities.capitalize(fieldName), new Class[] { clazz }).invoke(fieldConfig, new Object[] { value });
            } catch (IllegalArgumentException e) {
                throw new ConfigException(IConfigMessages.E_ERROR_CLONING_FIELD + ": trying to clone fieldConfig(" + this + ")", ILogSeverity.FATAL, e);
            } catch (SecurityException e) {
                throw new ConfigException(IConfigMessages.E_ERROR_CLONING_FIELD + ": trying to clone fieldConfig(" + this + ")", ILogSeverity.FATAL, e);
            } catch (IllegalAccessException e) {
                throw new ConfigException(IConfigMessages.E_ERROR_CLONING_FIELD + ": trying to clone fieldConfig(" + this + ")", ILogSeverity.FATAL, e);
            } catch (InvocationTargetException e) {
                throw new ConfigException(IConfigMessages.E_ERROR_CLONING_FIELD + ": trying to clone fieldConfig(" + this + ")", ILogSeverity.FATAL, e);
            } catch (NoSuchMethodException e) {
                throw new ConfigException(IConfigMessages.E_ERROR_CLONING_FIELD + ": trying to clone fieldConfig(" + this + ")", ILogSeverity.FATAL, e);
            }
        }
        fieldConfig.setValue(this.getValue());
        fieldConfig.setQueryScript(this.getQueryScript());
        fieldConfig.setAutoIncrementConfig(this.getAutoIncrementConfig());
        fieldConfig.setBinderConfig(this.getBinderConfig());
        fieldConfig.setCardName(this.getCardName());
        fieldConfig.setConverter(this.getConverter());
        fieldConfig.setSequenceConfig(this.getSequenceConfig());
        fieldConfig.setSinkConverter(this.getSinkConverter());
        fieldConfig.setSourceConverter(this.getSourceConverter());
        fieldConfig.setValueConverter(this.getValueConverter());
        fieldConfig.setFormat(this.getFormat());
        Enumeration enumer = this.getProperties().propertyNames();
        while (enumer.hasMoreElements()) {
            String name = (String) enumer.nextElement();
            fieldConfig.setProperty(name, this.getProperty(name));
        }
        itr = displayRules.values().iterator();
        while (itr.hasNext()) {
            fieldConfig.addDisplayRule((Rule) itr.next());
        }
        itr = executionRules.values().iterator();
        while (itr.hasNext()) {
            fieldConfig.addExecutionRule((Rule) itr.next());
        }
        itr = validationRules.values().iterator();
        while (itr.hasNext()) {
            fieldConfig.addValidationRule((Rule) itr.next());
        }
        itr = options.iterator();
        while (itr.hasNext()) {
            fieldConfig.addOptionConfig((OptionConfig) itr.next());
        }
        itr = dependencies.iterator();
        while (itr.hasNext()) {
            fieldConfig.addDependencyConfig((IDependency) itr.next());
        }
        itr = searchValidationRules.values().iterator();
        while (itr.hasNext()) {
            fieldConfig.addSearchValidationRule((Rule) itr.next());
        }
        itr = multiOptionConfig.iterator();
        while (itr.hasNext()) {
            fieldConfig.addMultiOptionConfig((MultiOptionConfig) itr.next());
        }
    }

    /**
	 * addOptionConfig
	 * @param config
	 */
    public void addOptionConfig(OptionConfig config) {
        options.add(config);
    }

    /**
	 * addAllOptionConfig
	 * @param configs
	 */
    public void addAllOptionConfig(OptionConfig[] configs) {
        options.addAll(Arrays.asList(configs));
    }

    /**
	 * getAllOptionConfigs
	 * @return
	 */
    public OptionConfig[] getAllOptionConfigs() {
        return (OptionConfig[]) options.toArray(new OptionConfig[options.size()]);
    }

    /**
	 * addDependencyConfig
	 * @param config
	 */
    public void addDependencyConfig(IDependency config) {
        dependencies.add(config);
    }

    public IDependency[] getAllDependencyConfigs() {
        return (IDependency[]) dependencies.toArray(new IDependency[dependencies.size()]);
    }

    public void installMetaData(IMetaDataConfig metaData) throws ConfigException {
        setAutoIncrement(metaData.isAutoIncrement());
        setExecuteClassName(metaData.getExecuteClassName());
        setMaxlength(metaData.getMaxlength());
        setNullable(metaData.isNullable());
        setPrimaryKey(metaData.isPrimaryKey());
        setDisplayName(metaData.getLabel());
        setWritable(metaData.isWritable());
        if (!this.isWritable()) {
            ConfigUtil.addDisplayRule(this, "action", Constants.DISPLAY_TYPE_UNEDITABLE);
        }
        if (!this.isNullable() || this.isPrimaryKey()) {
            ConfigUtil.addValidationRequiredRule(this, (this.isAutoIncrement() || this.getSequenceConfig() != null));
        }
        ConfigUtil.addValidationMaxCharRule(this, getMaxlength());
        if (StringUtils.isBlank(type) && this.isReferenced()) {
            ConfigUtil.makeSelectField(this);
        }
    }

    /**
	 * getDisplayName
	 * @return
	 */
    public String getDisplayName() {
        return displayName;
    }

    /**
	 * setDisplayName
	 * @param displayName
	 */
    public void setDisplayName(String displayName) {
        if (singleSetFields.keySet().contains("displayName")) {
            return;
        }
        singleSetFields.put("displayName", String.class);
        this.displayName = displayName;
    }

    /**
	 * isNullable
	 * @return
	 */
    public boolean isNullable() {
        return nullable;
    }

    /**
	 * setNullable
	 * @param nullable
	 */
    public void setNullable(boolean nullable) {
        if (singleSetFields.keySet().contains("nullable")) {
            return;
        }
        singleSetFields.put("nullable", boolean.class);
        this.nullable = nullable;
    }

    /**
	 * isWritable
	 * @return
	 */
    public boolean isWritable() {
        return writable;
    }

    /**
	 * setWritable
	 * @param writable
	 */
    public void setWritable(boolean writable) {
        if (singleSetFields.keySet().contains("writable")) {
            return;
        }
        singleSetFields.put("writable", boolean.class);
        this.writable = writable;
    }

    /**
	 * getExecuteClassName
	 * @return
	 */
    public String getExecuteClassName() {
        if (executeClass == null) {
            return String.class.getName();
        }
        return executeClass.getName();
    }

    /**
	 * setExecuteClassName
	 * @param className
	 */
    public void setExecuteClassName(String className) throws ConfigException {
        if (singleSetFields.keySet().contains("executeClassName")) {
            return;
        }
        singleSetFields.put("executeClassName", className);
        try {
            this.executeClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ConfigException(IPunchCardMessages.E_ERROR_CLASS_NOTFOUND + " : className=(" + className + ")", ILogSeverity.FATAL, e);
        }
    }

    /**
	 * @return Returns the executeClass.
	 */
    public Class<?> getExecuteClass() {
        if (executeClass != null) {
            return executeClass;
        }
        return String.class;
    }

    /**
	 * @param executeClass The executeClass to set.
	 */
    public void setExecuteClass(Class executionClass) {
        this.executeClass = executionClass;
    }

    /**
	 * getMaxlength
	 * @return
	 */
    public int getMaxlength() {
        return maxlength;
    }

    /**
	 * setMaxlength
	 * @param size
	 */
    public void setMaxlength(int size) {
        if (singleSetFields.keySet().contains("maxlength")) {
            return;
        }
        singleSetFields.put("maxlength", int.class);
        this.maxlength = size;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE, false);
    }

    /**
	 * getName
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * setName
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * getValue
	 * @return
	 */
    public String getValue() {
        return value;
    }

    /**
	 * setValue
	 * @param value
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * addDisplayRule
	 * @param rule
	 */
    public void addDisplayRule(Rule rule) {
        String id = rule.getId();
        if (StringUtils.isBlank(id)) {
            id = rule.getName();
        }
        if (!displayRules.keySet().contains(id)) {
            if (rule.getIncludedActions() == null && rule.getExcludedActions() == null) {
                rule.setExcludedActions(PunchCardUtil.findProperty("DEFAULT_DISPLAY_RULE_EXCLUDE", new IPunchCardConfig[] { this }));
                rule.setIncludedActions(PunchCardUtil.findProperty("DEFAULT_DISPLAY_RULE_INCLUDE", new IPunchCardConfig[] { this }));
            }
            displayRules.put(id, rule);
        }
    }

    /**
	 * addExecutionRule
	 * @param rule
	 */
    public void addExecutionRule(Rule rule) {
        String id = rule.getId();
        if (StringUtils.isBlank(id)) {
            id = rule.getName();
        }
        if (!executionRules.keySet().contains(id)) {
            if (rule.getIncludedActions() == null && rule.getExcludedActions() == null) {
                rule.setExcludedActions(PunchCardUtil.findProperty("DEFAULT_EXECUTION_RULE_EXCLUDE", new IPunchCardConfig[] { this }));
                rule.setIncludedActions(PunchCardUtil.findProperty("DEFAULT_EXECUTION_RULE_INCLUDE", new IPunchCardConfig[] { this }));
            }
            executionRules.put(id, rule);
        }
    }

    /**
	 * addValidationRule
	 * @param rule
	 */
    public void addValidationRule(Rule rule) {
        String id = rule.getId();
        if (StringUtils.isBlank(id)) {
            id = rule.getName();
        }
        if (!validationRules.keySet().contains(id)) {
            if (rule.getIncludedActions() == null && rule.getExcludedActions() == null) {
                rule.setExcludedActions(PunchCardUtil.findProperty("DEFAULT_VALIDATION_RULE_EXCLUDE", new IPunchCardConfig[] { this }));
                rule.setIncludedActions(PunchCardUtil.findProperty("DEFAULT_VALIDATION_RULE_INCLUDE", new IPunchCardConfig[] { this }));
            }
            validationRules.put(id, rule);
        }
    }

    /**
	 * getAllDisplayRules
	 * @return
	 */
    public Rule[] getAllDisplayRules() {
        return (Rule[]) displayRules.values().toArray(new Rule[displayRules.size()]);
    }

    /**
	 * getAllExecutionRules
	 * @return
	 */
    public Rule[] getAllExecutionRules() {
        return (Rule[]) executionRules.values().toArray(new Rule[executionRules.size()]);
    }

    /**
	 * getAllValidationRules
	 * @return
	 */
    public Rule[] getAllValidationRules() {
        return (Rule[]) validationRules.values().toArray(new Rule[validationRules.size()]);
    }

    /**
	 * addSearchValidationRule
	 * @param rule
	 */
    public void addSearchValidationRule(Rule rule) {
        searchValidationRules.put(Utilities.fetchUniqueId(), rule);
    }

    /**
	 * getAllSearchValidationRules
	 * @return
	 */
    public Rule[] getAllSearchValidationRules() {
        return (Rule[]) searchValidationRules.values().toArray(new Rule[searchValidationRules.size()]);
    }

    /**
	 * isPrimaryKey
	 * @return
	 */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
	 * setPrimaryKey
	 * @param primaryKey
	 */
    public void setPrimaryKey(boolean primaryKey) {
        if (singleSetFields.keySet().contains("primaryKey")) {
            return;
        }
        singleSetFields.put("primaryKey", boolean.class);
        this.primaryKey = primaryKey;
    }

    /**
	 * getMultiOptionConfig
	 * @return
	 */
    public MultiOptionConfig[] getAllMultiOptionConfig() {
        return (MultiOptionConfig[]) multiOptionConfig.toArray(new MultiOptionConfig[multiOptionConfig.size()]);
    }

    /**
	 * setReferenceConfig
	 * @param referenceConfig
	 */
    public void addReferenceConfig(MultiOptionConfig referenceConfig) {
        referenced = true;
        this.multiOptionConfig.add(referenceConfig);
    }

    /**
	 * setMultiOptionConfig
	 * @param multiOptionConfig
	 */
    public void addMultiOptionConfig(MultiOptionConfig multiOptionConfig) {
        referenced = true;
        this.multiOptionConfig.add(multiOptionConfig);
    }

    /**
	 * isAutoIncrement
	 * @return
	 */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
	 * setAutoIncrement
	 * @param autoIncrement
	 */
    public void setAutoIncrement(boolean autoIncrement) {
        if (singleSetFields.keySet().contains("autoIncrement")) {
            return;
        }
        singleSetFields.put("autoIncrement", boolean.class);
        this.autoIncrement = autoIncrement;
    }

    /**
	 * isReferenced
	 * @return
	 */
    public boolean isReferenced() {
        return referenced;
    }

    /**
	 * getSequenceConfig
	 * @return
	 */
    public SequenceConfig getSequenceConfig() {
        return sequenceConfig;
    }

    /**
	 * setSequenceConfig
	 * @param sequenceConfig
	 */
    public void setSequenceConfig(SequenceConfig sequenceConfig) {
        this.sequenceConfig = sequenceConfig;
    }

    /**
	 * getAutoIncrementConfig
	 * @return
	 */
    public AutoIncrementConfig getAutoIncrementConfig() {
        return autoIncrementConfig;
    }

    /**
	 * setAutoIncrementConfig
	 * @param autoIncrementConfig
	 */
    public void setAutoIncrementConfig(AutoIncrementConfig autoIncrementConfig) {
        this.autoIncrementConfig = autoIncrementConfig;
    }

    /**
	 * getFormat
	 * @return
	 */
    public String getFormat() {
        return format;
    }

    /**
	 * setFormat
	 * @param format
	 */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
	 * getScripts
	 * @return
	 */
    public String getScripts() {
        return scripts;
    }

    /**
	 * setScripts
	 * @param scripts
	 */
    public void setScripts(String scripts) {
        this.scripts = scripts;
    }

    /**
	 * getAlt
	 * @return
	 */
    public String getAlt() {
        return alt;
    }

    /**
	 * setAlt
	 * @param alt
	 */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
	 * isDisableScript
	 * @return
	 */
    public boolean isDisableScript() {
        return disableScript;
    }

    /**
	 * setDisableScript
	 * @param disableScript
	 */
    public void setDisableScript(boolean disableScript) {
        this.disableScript = disableScript;
    }

    /**
	 * getId
	 * @return
	 */
    public String getId() {
        return id;
    }

    /**
	 * setId
	 * @param id
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * getLang
	 * @return
	 */
    public String getLang() {
        return lang;
    }

    /**
	 * setLang
	 * @param lang
	 */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
	 * getSize
	 * @return
	 */
    public int getSize() {
        return size;
    }

    /**
	 * setSize
	 * @param size
	 */
    public void setSize(int size) {
        this.size = size;
    }

    /**
	 * getStyle
	 * @return
	 */
    public String getStyle() {
        return style;
    }

    /**
	 * setStyle
	 * @param style
	 */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
	 * getTabindex
	 * @return
	 */
    public int getTabindex() {
        return tabindex;
    }

    /**
	 * setTabindex
	 * @param tabindex
	 */
    public void setTabindex(int tabindex) {
        this.tabindex = tabindex;
    }

    /**
	 * getTitle
	 * @return
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * setTitle
	 * @param title
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * isMultiple
	 * @return
	 */
    public boolean isMultiple() {
        return multiple;
    }

    /**
	 * setMultiple
	 * @param multiple
	 */
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    /**
	 * getAlias
	 * @return
	 */
    public String getSourceName() {
        return sourceName;
    }

    /**
	 * setAlias
	 * @param sourceName
	 */
    public void setSourceName(String alias) {
        this.sourceName = alias;
    }

    /**
	 * getConverter
	 * @return
	 */
    public Converter getConverter() {
        return converter;
    }

    /**
	 * getBinderConfig
	 * @return
	 */
    public BinderConfig getBinderConfig() {
        return binderConfig;
    }

    /**
	 * setBinderConfig
	 * @param binderConfig
	 */
    public void setBinderConfig(BinderConfig binderConfig) {
        this.binderConfig = binderConfig;
    }

    /**
	 * getStyleClass
	 * @return
	 */
    public String getStyleClass() {
        return styleClass;
    }

    /**
	 * setStyleClass
	 * @param styleClass
	 */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
	 * getSinkConverter
	 * @return
	 */
    public ISinkConverter getSinkConverter() {
        if (sinkConverter != null) {
            return sinkConverter;
        }
        return converter;
    }

    /**
	 * setSinkConverter
	 * @param sinkConverter
	 */
    public void setSinkConverter(ISinkConverter sinkConverter) {
        this.sinkConverter = sinkConverter;
    }

    /**
	 * getSourceConverter
	 * @return
	 */
    public ISourceConverter getSourceConverter() {
        if (sourceConverter != null) {
            return sourceConverter;
        }
        return converter;
    }

    /**
	 * setSourceConverter
	 * @param sourceConverter
	 */
    public void setSourceConverter(ISourceConverter sourceConverter) {
        this.sourceConverter = sourceConverter;
    }

    /**
	 * getValueConverter
	 * @return
	 */
    public IValueConverter getValueConverter() {
        if (valueConverter != null) {
            return valueConverter;
        }
        return converter;
    }

    /**
	 * setValueConverter
	 * @param valueConverter
	 */
    public void setValueConverter(IValueConverter valueConverter) {
        this.valueConverter = valueConverter;
    }

    /**
	 * setConverter
	 * @param converter
	 */
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    /**
	 * getDescription
	 * @return
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * setDescription
	 * @param description
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardName() {
        return this.cardName;
    }

    public String[] getCardNames() {
        if (StringUtils.isNotBlank(cardName)) {
            return cardName.split(",");
        }
        return new String[] { cardName };
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCols() {
        return this.cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
	 * @return the queryScript
	 */
    public String getQueryScript() {
        return queryScript;
    }

    /**
	 * @param queryScript the queryScript to set
	 */
    public void setQueryScript(String queryScript) {
        this.queryScript = queryScript;
    }
}

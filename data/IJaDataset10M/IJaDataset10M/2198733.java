package self.micromagic.eterna.view.impl;

import java.util.Iterator;
import self.micromagic.eterna.digester.ConfigurationException;
import self.micromagic.eterna.share.EternaFactory;
import self.micromagic.eterna.view.BaseManager;
import self.micromagic.eterna.view.Component;
import self.micromagic.eterna.view.ComponentGenerator;
import self.micromagic.eterna.view.TableForm;
import self.micromagic.eterna.view.TableList;
import self.micromagic.eterna.view.ViewAdapterGenerator;
import self.micromagic.eterna.view.ViewAdapter;
import self.micromagic.util.container.MultiIterator;

public class TR extends ComponentImpl implements Component, ComponentGenerator {

    public static final String DEFAULT_TABLELIST_TR_ATTRIBUTE = "default.table-list.tr";

    public static final int TABLE_TAYE_FORM = 2;

    public static final int TABLE_TAYE_LIST = 1;

    protected int tableType = 0;

    protected Component baseComponent;

    protected String baseComponentName;

    private ViewAdapterGenerator.ModifiableViewRes viewRes = null;

    public TR() {
        this.type = "tr";
    }

    public TR(String name) throws ConfigurationException {
        this();
        this.setName(name);
    }

    public void initialize(EternaFactory factory, Component parent) throws ConfigurationException {
        if (this.initialized) {
            return;
        }
        super.initialize(factory, parent);
        String trName = this.baseComponentName != null ? this.baseComponentName : this.tableType != TABLE_TAYE_LIST ? null : (String) factory.getAttribute(DEFAULT_TABLELIST_TR_ATTRIBUTE);
        if (trName != null && trName.length() > 0) {
            this.baseComponent = factory.getTypicalComponent(trName);
            if (this.baseComponent == null) {
                log.warn("The Typical Component [" + trName + "] not found.");
            }
        }
        if (!this.isIgnoreGlobalParam() && this.baseComponent != null && "tr".equalsIgnoreCase(this.baseComponent.getType())) {
            if (this.componentParam == null) {
                this.componentParam = this.baseComponent.getComponentParam();
            }
            if (this.beforeInit == null) {
                this.beforeInit = this.baseComponent.getBeforeInit();
            } else {
                String parentScript = this.baseComponent.getBeforeInit();
                this.beforeInit = ViewTool.addParentScript(this.beforeInit, parentScript);
            }
            if (this.initScript == null) {
                this.initScript = this.baseComponent.getInitScript();
            } else {
                String parentScript = this.baseComponent.getInitScript();
                this.initScript = ViewTool.addParentScript(this.initScript, parentScript);
            }
        } else {
            this.baseComponent = null;
            this.beforeInit = ViewTool.addParentScript(this.beforeInit, null);
            this.initScript = ViewTool.addParentScript(this.initScript, null);
        }
        if (this.tableType == TABLE_TAYE_LIST && this.beforeInit == null) {
            this.beforeInit = "checkResult=false;checkResult=(eg_temp.rowType==\"title\"||eg_temp.rowType==\"row\");";
        }
    }

    public void setName(String name) throws ConfigurationException {
        super.setName(name);
        if (name == null) {
            return;
        }
        int preNameLength = 12;
        if (name.startsWith(TableList.TR_NAME_PERFIX)) {
            this.tableType = TABLE_TAYE_LIST;
        } else if (name.startsWith(TableForm.TR_NAME_PERFIX)) {
            this.tableType = TABLE_TAYE_FORM;
        } else {
            throw new ConfigurationException("The name must start with [tableList_TR or tableForm_TR] for tr component.");
        }
        if (name.length() > preNameLength) {
            if (name.charAt(preNameLength) != '.') {
                throw new ConfigurationException("If you want set plus base name, must start with \".\" for tr component.");
            }
            super.setName(name.substring(0, preNameLength));
            this.baseComponentName = name.substring(preNameLength + 1);
        }
    }

    public void setType(String type) throws ConfigurationException {
        if (!"tr".equalsIgnoreCase(type)) {
            throw new ConfigurationException("The type must be [tr] for tr component.");
        }
    }

    public Iterator getSubComponents() throws ConfigurationException {
        if (this.baseComponent != null && this.componentList.size() == 0) {
            return this.baseComponent.getSubComponents();
        }
        return super.getSubComponents();
    }

    public Iterator getEvents() throws ConfigurationException {
        if (this.baseComponent == null) {
            return super.getEvents();
        }
        return new MultiIterator(this.baseComponent.getEvents(), super.getEvents());
    }

    protected ViewAdapterGenerator.ModifiableViewRes getModifiableViewRes() throws ConfigurationException {
        if (this.viewRes == null) {
            this.viewRes = super.getModifiableViewRes();
            if (this.baseComponent != null) {
                this.viewRes.addAll(this.baseComponent.getViewRes());
            }
        }
        return this.viewRes;
    }
}

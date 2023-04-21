package shellkk.qiq.gui.build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.datamining.JDMException;
import javax.datamining.LogicalAttributeUsage;
import javax.datamining.base.BuildSettings;
import javax.datamining.data.AttributeDataType;
import javax.datamining.supervised.regression.RegressionSettings;
import javax.datamining.supervised.regression.RegressionSettingsFactory;
import shellkk.qiq.gui.Login;
import shellkk.qiq.gui.Wizard;
import shellkk.qiq.jdm.data.PhysicalAttributeImpl;
import shellkk.qiq.jdm.resource.ConnectionImpl;

public class RegressionSettingsStep implements BuildSettingsStep {

    protected String targetAttribute;

    protected List<String> targetAttrs = new ArrayList();

    protected String weightAttribute;

    protected List<String> weightAttrs = new ArrayList();

    protected List<LogicAttributeConfig> attrConfigs = new ArrayList();

    protected Login login;

    public BuildSettings getBuildSettings() throws JDMException {
        ConnectionImpl connection = null;
        try {
            connection = login.getConnection();
            RegressionSettingsFactory bsFactory = (RegressionSettingsFactory) connection.getFactory(RegressionSettings.class.getName());
            RegressionSettings bs = bsFactory.create();
            bs.setTargetAttributeName(targetAttribute);
            bs.setWeightAttribute(weightAttribute);
            for (LogicAttributeConfig attrConfig : attrConfigs) {
                if (attrConfig.getWeight() != null) {
                    bs.setWeight(attrConfig.getName(), attrConfig.getWeight());
                }
                if (!attrConfig.isActive()) {
                    bs.setUsage(attrConfig.getName(), LogicalAttributeUsage.inactive);
                }
            }
            return bs;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public String getName() {
        return "regStep";
    }

    public String getTitle() {
        return "Regression Settings";
    }

    public void onClose() {
        targetAttribute = null;
        targetAttrs.clear();
        weightAttribute = null;
        weightAttrs.clear();
        attrConfigs.clear();
    }

    public boolean isBackable() {
        return true;
    }

    public boolean isNextable() {
        return targetAttribute != null;
    }

    protected void mergeAttrConfigs(Map<String, PhysicalAttributeImpl> attrs) {
        for (PhysicalAttributeImpl attr : attrs.values()) {
            boolean exist = false;
            for (LogicAttributeConfig config : attrConfigs) {
                if (config.getName().equals(attr.getName())) {
                    config.setDataType(attr.getDataType().name());
                    config.setDescription(attr.getDescription());
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                LogicAttributeConfig attrConfig = new LogicAttributeConfig();
                attrConfig.setName(attr.getName());
                attrConfig.setDataType(attr.getDataType().name());
                attrConfig.setDescription(attr.getDescription());
                attrConfig.setActive(true);
                attrConfigs.add(attrConfig);
            }
        }
        ArrayList<LogicAttributeConfig> removes = new ArrayList();
        Set<String> names = attrs.keySet();
        for (LogicAttributeConfig config : attrConfigs) {
            if (!names.contains(config.getName())) {
                removes.add(config);
            }
        }
        attrConfigs.removeAll(removes);
        Comparator<LogicAttributeConfig> comp = new Comparator<LogicAttributeConfig>() {

            public int compare(LogicAttributeConfig o1, LogicAttributeConfig o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(attrConfigs, comp);
    }

    public void onEnter(Wizard wizard) throws Exception {
        PreprocessStep transStep = (PreprocessStep) wizard.getStep(PreprocessStep.class);
        Map<String, PhysicalAttributeImpl> attrs = transStep.getOutputAttributes();
        targetAttrs.clear();
        weightAttrs.clear();
        for (PhysicalAttributeImpl attr : attrs.values()) {
            if (attr.getDataType().equals(AttributeDataType.integerType)) {
                targetAttrs.add(attr.getName());
                weightAttrs.add(attr.getName());
            } else if (attr.getDataType().equals(AttributeDataType.doubleType)) {
                targetAttrs.add(attr.getName());
                weightAttrs.add(attr.getName());
            }
        }
        Collections.sort(targetAttrs);
        Collections.sort(weightAttrs);
        weightAttrs.add(0, null);
        if (!targetAttrs.contains(targetAttribute)) {
            targetAttribute = null;
        }
        if (!weightAttrs.contains(weightAttribute)) {
            weightAttribute = null;
        }
        mergeAttrConfigs(attrs);
    }

    public String getTargetAttribute() {
        return targetAttribute;
    }

    public void setTargetAttribute(String targetAttribute) {
        this.targetAttribute = targetAttribute;
    }

    public List<String> getTargetAttrs() {
        return targetAttrs;
    }

    public void setTargetAttrs(List<String> targetAttrs) {
        this.targetAttrs = targetAttrs;
    }

    public String getWeightAttribute() {
        return weightAttribute;
    }

    public void setWeightAttribute(String weightAttribute) {
        this.weightAttribute = weightAttribute;
    }

    public List<String> getWeightAttrs() {
        return weightAttrs;
    }

    public void setWeightAttrs(List<String> weightAttrs) {
        this.weightAttrs = weightAttrs;
    }

    public List<LogicAttributeConfig> getAttrConfigs() {
        return attrConfigs;
    }

    public void setAttrConfigs(List<LogicAttributeConfig> attrConfigs) {
        this.attrConfigs = attrConfigs;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}

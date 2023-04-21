package org.tolven.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;
import org.tolven.app.bean.MenuPath;
import org.tolven.app.el.ELFunctions;
import org.tolven.app.el.TrimExpressionEvaluator;
import org.tolven.app.entity.MenuData;
import org.tolven.app.entity.TrimHeader;
import org.tolven.logging.TolvenLogger;
import org.tolven.trim.Act;
import org.tolven.trim.ActRelationship;
import org.tolven.trim.ActRelationshipDirection;
import org.tolven.trim.ActRelationshipType;
import org.tolven.trim.Compute.Property;
import org.tolven.trim.ValueSet;
import org.tolven.trim.ex.TrimEx;
import org.tolven.trim.ex.TrimFactory;
import org.tolven.trim.ex.ValueSetEx;

public class InsertAct extends ComputeBase {

    private static final TrimFactory trimFactory = new TrimFactory();

    private String template;

    private boolean enabled;

    private String arTypeCode;

    private String arName;

    private String arDirection;

    private String position;

    private String action;

    private boolean enableAct;

    private String templateBody;

    public void compute() throws Exception {
        TolvenLogger.info("Compute enabled=" + isEnabled(), InsertAct.class);
        super.checkProperties();
        if (isEnabled() && StringUtils.isNotBlank(getTemplate())) {
            Integer position = Integer.valueOf(getPosition());
            Act act = this.getAct();
            if (getAction().equals("add")) {
                ActRelationship newRelationship = parseTemplate();
                newRelationship.setEnabled(isEnableAct());
                act.getRelationships().add(newRelationship);
            } else {
                List<ActRelationship> lActToRemove = new ArrayList<ActRelationship>();
                for (ActRelationship lRel : act.getRelationships()) {
                    if (!lRel.getName().equals(arName)) continue;
                    if (lRel.getSequenceNumber() != null && lRel.getSequenceNumber().intValue() == position) {
                        lActToRemove.add(lRel);
                    }
                }
                act.getRelationships().removeAll(lActToRemove);
            }
            int index = 1;
            for (ActRelationship lRel : act.getRelationships()) {
                if (!lRel.getName().equals(arName)) continue;
                lRel.setSequenceNumber(new Integer(index));
                index++;
            }
            for (Property property : getComputeElement().getProperties()) {
                if (property.getName().equals("enabled")) {
                    property.setValue(Boolean.FALSE);
                    break;
                }
            }
        }
    }

    public ActRelationship parseTemplate() throws JAXBException {
        TrimExpressionEvaluator ee = new TrimExpressionEvaluator();
        Map<String, Object> variables = new HashMap<String, Object>(10);
        variables.put("account", getAccountUser().getAccount());
        variables.put("accountUser", getAccountUser());
        variables.put("user", getAccountUser().getUser());
        variables.put("knownType", getAccountUser().getAccount().getAccountType().getKnownType());
        variables.put("now", getNow());
        for (MenuPath contextPath : getContextList()) {
            long keys[] = contextPath.getNodeKeys();
            for (int n = 0; n < keys.length; n++) {
                if (keys[n] != 0) {
                    MenuData md = getMenuBean().findMenuDataItem(keys[n]);
                    variables.put(contextPath.getNode(n), md);
                }
            }
        }
        ee.addVariables(variables);
        TrimEx templateTrim = getTrimBean().parseTrim(getTemplateBody(), ee);
        ActRelationship ar = trimFactory.createActRelationship();
        ar.setTypeCode(ActRelationshipType.valueOf(arTypeCode));
        ar.setDirection(ActRelationshipDirection.valueOf(arDirection));
        ar.setName(arName);
        ar.setAct(templateTrim.getAct());
        boolean valueSetFound = false;
        for (ValueSet v_c : templateTrim.getValueSets()) {
            ValueSetEx v_child = (ValueSetEx) v_c;
            valueSetFound = false;
            for (ValueSet v_p : getTrim().getValueSets()) {
                ValueSetEx v_parent = (ValueSetEx) v_p;
                if (v_parent.getName().equalsIgnoreCase(v_child.getName())) {
                    valueSetFound = true;
                    v_parent.getValues().clear();
                    v_parent.getValues().addAll(v_child.getValues());
                    break;
                }
            }
            if (!valueSetFound) getTrim().getValueSets().add(v_child);
        }
        return ar;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
        templateBody = null;
    }

    public String getTemplateBody() {
        if (templateBody == null) {
            TrimHeader trimHeader = getTrimBean().findTrimHeader(template);
            templateBody = new String(trimHeader.getTrim());
        }
        return templateBody;
    }

    public String getArTypeCode() {
        return arTypeCode;
    }

    public void setArTypeCode(String arTypeCode) {
        this.arTypeCode = arTypeCode;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public String getArDirection() {
        return arDirection;
    }

    public void setArDirection(String arDirection) {
        this.arDirection = arDirection;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isEnableAct() {
        return enableAct;
    }

    public void setEnableAct(boolean enableAct) {
        this.enableAct = enableAct;
    }
}

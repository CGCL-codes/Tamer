package org.kwantu.appbrowser;

import org.kwantu.persistence.PersistentObject;
import org.kwantu.m2.model.KwantuAttribute;
import org.kwantu.m2.model.KwantuClass;
import org.kwantu.m2.model.StringUtil;
import org.kwantu.m2.model.KwantuRelationship;
import org.kwantu.m2.model.KwantuMethod;
import org.kwantu.m2.KwantuItemNotFoundException;
import org.kwantu.m2.KwantuFaultException;
import org.kwantu.m2.KwantuValidateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.List;
import org.kwantu.m2.model.KwantuSortedArrayList;
import org.kwantu.commons.ui.PortletUserUtil;

/**
 * Provides a basic initial Mapping between the user interface and an instance of a component
 * of a Kwantu Application.
 */
public class KwantuForm {

    private static final Log LOG = LogFactory.getLog(KwantuForm.class);

    private KwantuApplicationController appController;

    private PersistentObject appItem;

    String appClassName;

    private KwantuClass kwantuClass;

    ArrayList<KwantuFormField> fields;

    ArrayList<KwantuFormButton> subForms;

    ArrayList<KwantuFormButton> methods;

    HashMap<String, Method> getters;

    HashMap<String, Method> setters;

    HashMap<String, Method> kwantuMethods;

    public KwantuForm(PersistentObject item, KwantuApplicationController controller) {
        appController = controller;
        appItem = item;
        Class appClass = appItem.getClass();
        String st = appClass.getName();
        appClassName = st.replaceAll("^.*\\.", "");
        LOG.info("Creating form for class " + appClassName);
        kwantuClass = null;
        try {
            kwantuClass = controller.getKwantuClass(appClassName);
        } catch (KwantuItemNotFoundException e) {
            LOG.error("Unable to find KwantuClass " + appClassName, e);
            return;
        }
        getters = new HashMap<String, Method>();
        setters = new HashMap<String, Method>();
        kwantuMethods = new HashMap<String, Method>();
        String methodName;
        for (Method m : appClass.getMethods()) {
            methodName = m.getName();
            if (methodName.startsWith("get")) {
                String sg = StringUtil.downshiftLeadingLetter(methodName.substring(3));
                LOG.info("Found getter  " + sg);
                if (getters.put(sg, m) != null) {
                    LOG.error("Error: Methods with the same name are not currently supported - " + appClassName + "." + methodName);
                }
            } else if (methodName.startsWith("set")) {
                String ss = StringUtil.downshiftLeadingLetter(methodName.substring(3));
                LOG.info("Found setter  " + ss);
                if (setters.put(ss, m) != null) {
                    LOG.error("Error: Methods with the same name are not currently supported - " + appClassName + "." + methodName);
                }
            } else {
                LOG.info("Found KwantuMethod  " + methodName);
                if (kwantuMethods.put(methodName, m) != null) {
                    LOG.error("Error: Methods with the same name are not currently supported - " + appClassName + "." + methodName);
                }
            }
        }
        refreshFieldsInForm(appItem);
        addSubFormButtons();
        addMethodButtons();
    }

    /**
     * For each relationship create a button which takes you
     * to a new form showing the object of the relationship.
     * (if it exists)
     */
    private void addSubFormButtons() {
        subForms = new ArrayList<KwantuFormButton>();
        List<KwantuRelationship> filteredAndSortedKwantuRelationships = new KwantuSortedArrayList<KwantuRelationship>(kwantuClass.getKwantuRelationships(), PortletUserUtil.isExpertUser()) {

            @Override
            public int compare(final KwantuRelationship a, final KwantuRelationship b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
        };
        for (KwantuRelationship kr : filteredAndSortedKwantuRelationships) {
            String sr = kr.getName();
            if (kr.getCardinality() == KwantuRelationship.Cardinality.ONE) {
                PersistentObject item = getRelatedItem(sr);
                if (item == null) {
                    subForms.add(new KwantuFormButton(sr, null));
                } else {
                    subForms.add(new KwantuFormButton(sr + " " + item.toString(), item));
                }
            }
        }
    }

    private void addMethodButtons() {
        methods = new ArrayList<KwantuFormButton>();
        List<KwantuMethod> filteredAndSortedKwantuMethods = new KwantuSortedArrayList<KwantuMethod>(kwantuClass.getKwantuMethods(), PortletUserUtil.isExpertUser()) {

            @Override
            public int compare(final KwantuMethod a, final KwantuMethod b) {
                return a.getSignature().compareToIgnoreCase(b.getSignature());
            }
        };
        for (KwantuMethod km : filteredAndSortedKwantuMethods) {
            String smn = km.getMethodName();
            LOG.info("Adding method button " + smn);
            Method m = kwantuMethods.get(smn);
            if (m == null) {
                LOG.error("Error: method not found " + smn);
            }
            methods.add(new KwantuFormButton(smn, null));
        }
    }

    private void refreshFieldsInForm(PersistentObject item) {
        fields = new ArrayList<KwantuFormField>();
        for (KwantuAttribute ka : kwantuClass.getKwantuAttributes()) {
            String sa = StringUtil.downshiftLeadingLetter(ka.getName());
            LOG.info("Finding getter for attribute " + sa);
            Method m = getters.get(sa);
            if (m == null) {
                LOG.error("Error: getter not found");
            }
            Object val = null;
            try {
                val = m.invoke(item);
            } catch (InvocationTargetException e) {
                LOG.error("failed to get attribute " + sa, e);
            } catch (IllegalAccessException e) {
                LOG.error("failed to get attribute " + sa, e);
            }
            String sv = "";
            if (val != null) {
                sv = val.toString();
                if (sv == null) {
                    sv = "";
                }
            }
            LOG.info("Adding form field " + sa);
            fields.add(new KwantuFormField(sa, sv, ka.getType()));
        }
    }

    private Object convertFromString(String val, KwantuAttribute.Type type) throws KwantuValidateException {
        switch(type) {
            case BOOLEAN:
                return Boolean.valueOf(val);
            case DATE:
                if (val == null || val.length() == 0) {
                    return null;
                }
                try {
                    return Date.valueOf(val);
                } catch (IllegalArgumentException e) {
                    throw new KwantuValidateException("Error: date " + val + " should be in the format " + "YYYY-MM-DD");
                }
            case INTEGER:
                return Integer.valueOf(val);
            case MONEY:
                if (val == null || val.length() == 0) {
                    return null;
                }
                try {
                    return new BigDecimal(val);
                } catch (NumberFormatException ex) {
                    throw new KwantuValidateException("Error: number " + val + " is not valid.");
                }
            case STRING:
                return val;
            default:
                throw new KwantuValidateException("Unkown data type " + type.name());
        }
    }

    public void updateAppInstanceFromForm() throws KwantuValidateException {
        updateAppInstanceFromForm(appItem);
    }

    private void updateAppInstanceFromForm(PersistentObject item) throws KwantuValidateException {
        String attributeName;
        for (KwantuFormField f : fields) {
            attributeName = f.getName();
            LOG.info("Finding setter for field " + attributeName);
            Method m = setters.get(attributeName);
            Object val = convertFromString(f.getValue(), f.getType());
            try {
                val = m.invoke(item, val);
            } catch (IllegalArgumentException e) {
                throw new KwantuValidateException("Illegal argument - " + e.getMessage());
            } catch (InvocationTargetException e) {
                throw new KwantuValidateException("Unable to call method on target - " + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new KwantuValidateException("Unable to call method on target (illegal access) - " + e.getMessage());
            }
        }
    }

    /**
     * Reset the form values from the Kwantu application instance.
     */
    public void revert() {
        refreshFieldsInForm(appItem);
    }

    public void save() throws KwantuValidateException {
        appController.save(appItem);
    }

    private PersistentObject getRelatedItem(String relationshipName) {
        Method m = getters.get(relationshipName);
        PersistentObject val = null;
        try {
            val = (PersistentObject) m.invoke(appItem);
        } catch (IllegalArgumentException e) {
            throw new KwantuFaultException("Illegal argument - " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new KwantuFaultException("Unable to call method on target - " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new KwantuFaultException("Unable to call method on target (illegal access) - " + e.getMessage());
        }
        LOG.info("Found relationship " + relationshipName);
        return val;
    }

    private Set<PersistentObject> getRelatedItems(String relationshipName) {
        Method m = getters.get(relationshipName);
        Set val = null;
        try {
            val = (Set<PersistentObject>) m.invoke(appItem);
        } catch (IllegalArgumentException e) {
            throw new KwantuFaultException("Illegal argument - " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new KwantuFaultException("Unable to call method on target - " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new KwantuFaultException("Unable to call method on target (illegal access) - " + e.getMessage());
        }
        LOG.info("Found relationship " + relationshipName);
        return val;
    }

    public KwantuForm getSubForm(KwantuFormButton button) throws KwantuItemNotFoundException {
        String relationshipName = button.getName();
        LOG.info("Fetching relationship " + relationshipName);
        if (button.getItem() == null) {
            throw new KwantuItemNotFoundException(relationshipName + " doesn't currently exist.");
        }
        return new KwantuForm(button.getItem(), appController);
    }

    public void callMethod(KwantuFormButton button) throws KwantuItemNotFoundException {
        String methodName = button.getName();
        LOG.info("Fetching method " + methodName);
        Method m = kwantuMethods.get(methodName);
        LOG.info("Found method " + methodName);
        try {
            m.invoke(appItem);
        } catch (IllegalArgumentException e) {
            throw new KwantuFaultException("Illegal argument - " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new KwantuFaultException("unable to call method on target: " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new KwantuFaultException("Unable to call method on target (illegal access) - " + e.getMessage(), e);
        }
    }

    public String getName() {
        return appClassName;
    }

    public ArrayList<KwantuFormField> getFields() {
        return fields;
    }

    public ArrayList<KwantuFormButton> getSubForms() {
        addSubFormButtons();
        return subForms;
    }

    public ArrayList<KwantuFormToManyRelationship> getToManyRelationShips() {
        List<KwantuRelationship> filteredAndSortedKwantuRelationships = new KwantuSortedArrayList<KwantuRelationship>(getKwantuClass().getKwantuRelationships(), PortletUserUtil.isExpertUser()) {

            @Override
            public int compare(final KwantuRelationship a, final KwantuRelationship b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
        };
        ArrayList<KwantuFormToManyRelationship> kwantuFormRelationships = new ArrayList<KwantuFormToManyRelationship>();
        for (KwantuRelationship relation : filteredAndSortedKwantuRelationships) {
            if (relation.getCardinality().equals(KwantuRelationship.Cardinality.MANY)) {
                kwantuFormRelationships.add(new KwantuFormToManyRelationship(this, getAppItem(), relation));
            }
        }
        return kwantuFormRelationships;
    }

    public ArrayList<KwantuFormButton> getMethods() {
        return methods;
    }

    public KwantuClass getKwantuClass() {
        return kwantuClass;
    }

    public PersistentObject getAppItem() {
        return appItem;
    }

    public void initializeClassLoader() {
        appController.initializeClassLoader();
    }
}

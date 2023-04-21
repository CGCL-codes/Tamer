package org.gbif.ipt.validation;

import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.text.ArchiveField.DataType;
import org.gbif.ipt.model.Extension;
import org.gbif.ipt.model.ExtensionMapping;
import org.gbif.ipt.model.ExtensionProperty;
import org.gbif.ipt.model.PropertyMapping;
import org.gbif.ipt.model.Resource;
import org.gbif.metadata.DateUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.xwork.StringUtils;

public class ExtensionMappingValidator {

    public static class ValidationStatus {

        private List<ConceptTerm> missingRequiredFields = new ArrayList<ConceptTerm>();

        private List<ConceptTerm> wrongDataTypeFields = new ArrayList<ConceptTerm>();

        private String idProblem;

        private String[] idProblemParams;

        public void addMissingRequiredField(ConceptTerm missingRequiredField) {
            this.missingRequiredFields.add(missingRequiredField);
        }

        public void addWrongDataTypeField(ConceptTerm wrongDataTypeField) {
            this.wrongDataTypeFields.add(wrongDataTypeField);
        }

        /**
     * @return the i18n key to the message for the problem or null if none
     */
        public String getIdProblem() {
            return idProblem;
        }

        /**
     * @return list of parameters to use for formatting the idProblem i18n message
     */
        public String[] getIdProblemParams() {
            return idProblemParams;
        }

        public List<ConceptTerm> getMissingRequiredFields() {
            return missingRequiredFields;
        }

        public List<ConceptTerm> getWrongDataTypeFields() {
            return wrongDataTypeFields;
        }

        public boolean isValid() {
            return idProblem == null && missingRequiredFields.isEmpty() && wrongDataTypeFields.isEmpty();
        }

        public void setIdProblem(String idProblem) {
            this.idProblem = idProblem;
        }

        public void setIdProblemParams(String[] idProblemParams) {
            this.idProblemParams = idProblemParams;
        }
    }

    private boolean isValidDataType(DataType dt, PropertyMapping pm, List<String[]> peek) {
        if (dt == null || dt == DataType.string) {
            return true;
        }
        Set<String> testData = new HashSet<String>();
        testData.add(pm.getDefaultValue());
        if (pm.getIndex() != null) {
            for (String[] row : peek) {
                if (row.length >= pm.getIndex() && pm.getIndex() >= 0) {
                    testData.add(row[pm.getIndex()]);
                }
            }
        }
        for (String val : testData) {
            val = StringUtils.trimToNull(val);
            if (val == null) {
                continue;
            }
            try {
                if (DataType.bool == dt) {
                    if (val.equals("1")) {
                        continue;
                    }
                } else if (DataType.date == dt) {
                    Date d = DateUtils.parse(val, DateUtils.isoDateFormat);
                    if (d == null) {
                        return false;
                    }
                } else if (DataType.decimal == dt) {
                    Float.parseFloat(val);
                } else if (DataType.integer == dt) {
                    Integer.parseInt(val);
                } else if (DataType.uri == dt) {
                    new URI(val);
                }
            } catch (NumberFormatException e) {
                return false;
            } catch (URISyntaxException e) {
                return false;
            }
        }
        return true;
    }

    public ValidationStatus validate(ExtensionMapping mapping, Resource resource, List<String[]> peek) {
        ValidationStatus v = new ValidationStatus();
        Extension ext = mapping.getExtension();
        if (ext != null) {
            for (ExtensionProperty p : ext.getProperties()) {
                if (p.isRequired() && !mapping.isMapped(p)) {
                    v.addMissingRequiredField(p);
                }
            }
            for (PropertyMapping pm : mapping.getFields()) {
                ExtensionProperty extProperty = mapping.getExtension().getProperty(pm.getTerm());
                DataType type = extProperty != null ? extProperty.getType() : null;
                if (type != null && DataType.string != type) {
                    if (!isValidDataType(type, pm, peek)) {
                        v.addWrongDataTypeField(pm.getTerm());
                    }
                }
            }
            if (mapping.getIdColumn() == null) {
                if (!ext.isCore()) {
                    v.setIdProblem("validation.mapping.coreid.missing");
                }
            } else if (mapping.getIdColumn().equals(ExtensionMapping.IDGEN_LINE_NUMBER)) {
                if (StringUtils.isNumericSpace(StringUtils.trimToNull(mapping.getIdSuffix()))) {
                    v.setIdProblem("validation.mapping.coreid.linenumber.integer");
                }
                if (ext.isCore()) {
                    Set<ExtensionMapping> maps = new HashSet<ExtensionMapping>(resource.getMappings(ext.getRowType()));
                    maps.remove(mapping);
                    if (!maps.isEmpty()) {
                        for (ExtensionMapping m : maps) {
                            if (m.getIdColumn() != null && m.getIdColumn().equals(ExtensionMapping.IDGEN_LINE_NUMBER)) {
                                if (StringUtils.trimToEmpty(mapping.getIdSuffix()).equals(m.getIdSuffix())) {
                                    v.setIdProblem("validation.mapping.coreid.linenumber.samesufix");
                                }
                            }
                        }
                    }
                } else {
                    boolean found = false;
                    for (ExtensionMapping m : resource.getCoreMappings()) {
                        if (m.getIdColumn() != null && m.getIdColumn().equals(ExtensionMapping.IDGEN_LINE_NUMBER)) {
                            if (StringUtils.trimToEmpty(mapping.getIdSuffix()).equals(StringUtils.trimToEmpty(m.getIdSuffix()))) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        v.setIdProblem("validation.mapping.coreid.linenumber.nocoresuffix");
                    }
                }
            } else if (mapping.getIdColumn().equals(ExtensionMapping.IDGEN_UUID)) {
                if (ext.isCore()) {
                    for (ExtensionMapping m : resource.getMappings()) {
                        if (!m.isCore()) {
                            v.setIdProblem("validation.mapping.coreid.uuid.extensions.exist");
                        }
                    }
                } else {
                    v.setIdProblem("validation.mapping.coreid.uuid.extension");
                }
            }
        }
        return v;
    }
}

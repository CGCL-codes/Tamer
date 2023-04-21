package serene.dtdcompatibility;

import java.util.ArrayList;
import org.xml.sax.SAXException;
import serene.restrictor.OverlapController;
import serene.restrictor.ControllerPool;
import serene.validation.schema.simplified.components.SElement;
import serene.validation.schema.simplified.components.SAttribute;
import serene.validation.schema.simplified.components.SNameClass;
import serene.validation.handlers.error.ErrorDispatcher;
import serene.util.IntList;

class CompetitionSimetryController {

    ArrayList<ElementRecord> records;

    OverlapController overlapController;

    ErrorDispatcher errorDispatcher;

    boolean restrictToFileName;

    CompetitionSimetryController(ControllerPool controllerPool, ErrorDispatcher errorDispatcher) {
        this.errorDispatcher = errorDispatcher;
        records = new ArrayList<ElementRecord>();
        overlapController = new OverlapController(controllerPool);
    }

    public void setRestrictToFileName(boolean value) {
        restrictToFileName = value;
    }

    void clear() {
        records.clear();
    }

    void control(SElement element, ArrayList<SAttribute> attributes) throws SAXException {
        SNameClass nameClass = element.getNameClass();
        for (ElementRecord record : records) {
            SNameClass recordNameClass = record.element.getNameClass();
            if (overlapController.overlap(nameClass, recordNameClass)) {
                for (SAttribute attribute : attributes) {
                    String defaultValue = attribute.getDefaultValue();
                    boolean foundCorrespondent = false;
                    SNameClass attributeNC = attribute.getNameClass();
                    for (SAttribute recordAttribute : record.attributes) {
                        SNameClass recordAttributeNC = recordAttribute.getNameClass();
                        if (attributeNC.equals(recordAttributeNC)) {
                            String recordDefaultValue = recordAttribute.getDefaultValue();
                            foundCorrespondent = true;
                            if (defaultValue == null) {
                                if (recordDefaultValue == null) {
                                    break;
                                } else {
                                    String message = "DTD compatibility error. Competing element definitions contain attribute definitions with the same name and different default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " with attribute definition <" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + " without default value;" + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " with attribute definition <" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + " and default value \"" + recordDefaultValue + "\".";
                                    errorDispatcher.error(new AttributeDefaultValueException(message, null));
                                    break;
                                }
                            } else {
                                if (recordDefaultValue != null && recordDefaultValue.equals(defaultValue)) {
                                    break;
                                } else if (recordDefaultValue == null) {
                                    String message = "DTD compatibility error. Competing element definitions contain attribute definitions with the same name and different default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " with attribute definition <" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + " and default value \"" + defaultValue + "\";" + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " with attribute definition <" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + " without default value .";
                                    errorDispatcher.error(new AttributeDefaultValueException(message, null));
                                    break;
                                } else {
                                    String message = "DTD compatibility error. Competing element definitions contain attribute definitions with the same name and different default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " with attribute definition <" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + " and default value \"" + defaultValue + "\";" + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " with attribute definition <" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + " and default value \"" + recordDefaultValue + "\".";
                                    errorDispatcher.error(new AttributeDefaultValueException(message, null));
                                    break;
                                }
                            }
                        }
                    }
                    if (!foundCorrespondent && defaultValue != null) {
                        String message = "DTD compatibility error. Competing element definitions without corresponding attribute definitions with default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " with attribute definition <" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " without corresponding attribute definition.";
                        errorDispatcher.error(new AttributeDefaultValueException(message, null));
                    }
                }
                for (SAttribute recordAttribute : record.attributes) {
                    String defaultValue = recordAttribute.getDefaultValue();
                    if (defaultValue != null) {
                        boolean foundCorrespondent = false;
                        SNameClass recordAttributeNC = recordAttribute.getNameClass();
                        for (SAttribute attribute : attributes) {
                            SNameClass attributeNC = attribute.getNameClass();
                            if (attributeNC.equals(recordAttributeNC)) {
                                foundCorrespondent = true;
                            }
                        }
                        if (!foundCorrespondent && defaultValue != null) {
                            String message = "DTD compatibility error. Competing element definitions without corresponding attribute definitions with default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " without corresponding attribute definition." + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " with attribute definition <" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName);
                            errorDispatcher.error(new AttributeDefaultValueException(message, null));
                        }
                    }
                }
            }
        }
        ElementRecord er = new ElementRecord(element, attributes, null);
        records.add(er);
    }

    void control(SElement element, ArrayList<SAttribute> attributes, IntList attributeIdTypes) throws SAXException {
        SNameClass nameClass = element.getNameClass();
        for (ElementRecord record : records) {
            SNameClass recordNameClass = record.element.getNameClass();
            if (overlapController.overlap(nameClass, recordNameClass)) {
                for (int i = 0; i < attributes.size(); i++) {
                    SAttribute attribute = attributes.get(i);
                    String defaultValue = attribute.getDefaultValue();
                    boolean foundCorrespondent = false;
                    SNameClass attributeNC = attribute.getNameClass();
                    for (int j = 0; j < record.attributes.size(); j++) {
                        SAttribute recordAttribute = record.attributes.get(j);
                        SNameClass recordAttributeNC = recordAttribute.getNameClass();
                        if (attributeNC.equals(recordAttributeNC)) {
                            String recordDefaultValue = recordAttribute.getDefaultValue();
                            foundCorrespondent = true;
                            if (defaultValue == null) {
                                if (recordDefaultValue == null) {
                                } else {
                                    String message = "DTD compatibility error. Competing element definitions contain attribute definitions with the same name and different default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " with attribute definition <" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + " without default value;" + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " with attribute definition <" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + " and default value \"" + recordDefaultValue + "\".";
                                    errorDispatcher.error(new AttributeDefaultValueException(message, null));
                                }
                            } else {
                                if (recordDefaultValue != null && recordDefaultValue.equals(defaultValue)) {
                                } else if (recordDefaultValue == null) {
                                    String message = "DTD compatibility error. Competing element definitions contain attribute definitions with the same name and different default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " with attribute definition <" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + " and default value \"" + defaultValue + "\";" + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " with attribute definition <" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + " without default value .";
                                    errorDispatcher.error(new AttributeDefaultValueException(message, null));
                                } else {
                                    String message = "DTD compatibility error. Competing element definitions contain attribute definitions with the same name and different default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " with attribute definition <" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + " and default value \"" + defaultValue + "\";" + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " with attribute definition <" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + " and default value \"" + recordDefaultValue + "\".";
                                    errorDispatcher.error(new AttributeDefaultValueException(message, null));
                                }
                            }
                            int attributeIdType = attributeIdTypes.get(i);
                            int recordAttributeIdType = record.attributeIdTypes.get(j);
                            if (attributeIdType != recordAttributeIdType) {
                                String message = "DTD compatibility error. Competing attribute definitions specify attribute values with different ID-types:" + "\n<" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + ";" + "\n<" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + ".";
                                errorDispatcher.error(new AttributeIdTypeException(message, null));
                                break;
                            }
                        } else if (overlapController.overlap(attributeNC, recordAttributeNC)) {
                            int attributeIdType = attributeIdTypes.get(i);
                            int recordAttributeIdType = record.attributeIdTypes.get(j);
                            if (attributeIdType != recordAttributeIdType) {
                                String message = "DTD compatibility error. Competing attribute definitions specify attribute values with different ID-types:" + "\n<" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + ";" + "\n<" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + ".";
                                errorDispatcher.error(new AttributeIdTypeException(message, null));
                            }
                        }
                    }
                    if (!foundCorrespondent && defaultValue != null) {
                        String message = "DTD compatibility error. Competing element definitions without corresponding attribute definitions with default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " with attribute definition <" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " without corresponding attribute definition.";
                        errorDispatcher.error(new AttributeDefaultValueException(message, null));
                    }
                }
                for (SAttribute recordAttribute : record.attributes) {
                    String defaultValue = recordAttribute.getDefaultValue();
                    if (defaultValue != null) {
                        boolean foundCorrespondent = false;
                        SNameClass recordAttributeNC = recordAttribute.getNameClass();
                        for (SAttribute attribute : attributes) {
                            SNameClass attributeNC = attribute.getNameClass();
                            if (attributeNC.equals(recordAttributeNC)) {
                                foundCorrespondent = true;
                            }
                        }
                        if (!foundCorrespondent && defaultValue != null) {
                            String message = "DTD compatibility error. Competing element definitions without corresponding attribute definitions with default values:" + "\n<" + element.getQName() + "> at " + element.getLocation(restrictToFileName) + " without corresponding attribute definition." + "\n<" + record.element.getQName() + "> at " + record.element.getLocation(restrictToFileName) + " with attribute definition <" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName);
                            errorDispatcher.error(new AttributeDefaultValueException(message, null));
                        }
                    }
                }
            } else {
                for (int i = 0; i < attributes.size(); i++) {
                    SAttribute attribute = attributes.get(i);
                    SNameClass attributeNC = attribute.getNameClass();
                    for (int j = 0; j < record.attributes.size(); j++) {
                        SAttribute recordAttribute = record.attributes.get(j);
                        SNameClass recordAttributeNC = recordAttribute.getNameClass();
                        if (overlapController.overlap(attributeNC, recordAttributeNC)) {
                            int attributeIdType = attributeIdTypes.get(i);
                            int recordAttributeIdType = record.attributeIdTypes.get(j);
                            if (attributeIdType != recordAttributeIdType) {
                                String message = "DTD compatibility error. Competing attribute definitions specify attribute values with different ID-types:" + "\n<" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + ";" + "\n<" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + ".";
                                errorDispatcher.error(new AttributeIdTypeException(message, null));
                                break;
                            }
                        }
                    }
                }
            }
        }
        ElementRecord er = new ElementRecord(element, attributes, attributeIdTypes);
        records.add(er);
    }

    void control(ArrayList<SAttribute> attributes, IntList attributeIdTypes) throws SAXException {
        ElementRecord record = records.get(0);
        for (int i = 0; i < attributes.size(); i++) {
            SAttribute attribute = attributes.get(i);
            SNameClass attributeNC = attribute.getNameClass();
            for (int j = 0; j < record.attributes.size(); j++) {
                SAttribute recordAttribute = record.attributes.get(j);
                SNameClass recordAttributeNC = recordAttribute.getNameClass();
                if (overlapController.overlap(attributeNC, recordAttributeNC)) {
                    int attributeIdType = attributeIdTypes.get(i);
                    int recordAttributeIdType = record.attributeIdTypes.get(j);
                    if (attributeIdType != recordAttributeIdType) {
                        String message = "DTD compatibility error. Competing attribute definitions specify attribute values with different ID-types:" + "\n<" + attribute.getQName() + "> at " + attribute.getLocation(restrictToFileName) + ";" + "\n<" + recordAttribute.getQName() + "> at " + recordAttribute.getLocation(restrictToFileName) + ".";
                        errorDispatcher.error(new AttributeIdTypeException(message, null));
                        break;
                    }
                }
            }
        }
        record.add(attributes, attributeIdTypes);
    }

    class ElementRecord {

        SElement element;

        ArrayList<SAttribute> attributes;

        IntList attributeIdTypes;

        ElementRecord(SElement element, ArrayList<SAttribute> attributes, IntList attributeIdTypes) {
            this.element = element;
            this.attributes = attributes;
            this.attributeIdTypes = attributeIdTypes;
        }

        void add(ArrayList<SAttribute> attributes, IntList attributeIdTypes) {
            this.attributes.addAll(attributes);
            for (int i = 0; i < attributeIdTypes.size(); i++) {
                this.attributeIdTypes.add(attributeIdTypes.get(i));
            }
        }

        public String toString() {
            return element.toString() + " " + attributes.toString();
        }
    }
}

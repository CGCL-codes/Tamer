package wsdl2ksoap.businesslogic;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import wsdl2ksoap.datatypes.Function;
import wsdl2ksoap.datatypes.PropertyContainer;
import wsdl2ksoap.datatypes.SoapClass;
import wsdl2ksoap.datatypes.SoapClassProperty;

/**
 *
 * @author newky
 */
public class ClassProcessor {

    public static void CreateServiceClass(String packagename) throws Exception {
        InputStream file = ClassProcessor.class.getResourceAsStream("resources/ClassTemplate.txt");
        if (file != null) {
            System.out.println("Woohoo");
            String classText = FileHelper.getContents(file);
            file.close();
            classText = classText.replaceAll("%%PACKAGENAME%%", packagename);
            classText = classText.replaceAll("%%CLASSNAME%%", PropertyContainer.ServiceName + "Soap");
            InputStream impFile = ClassProcessor.class.getResourceAsStream("resources/ServiceImportsTemplate.txt");
            if (impFile != null) {
                classText = classText.replace("%%IMPORTS%%", FileHelper.getContents(impFile));
                impFile.close();
            } else {
                throw new Exception("unable loads methods template");
            }
            InputStream methFile = ClassProcessor.class.getResourceAsStream("resources/MethodTemplate.txt");
            String blankfuncText = FileHelper.getContents(methFile);
            String methText = "";
            if (methFile != null) {
                methFile.close();
                for (int methLoop = 0; methLoop < PropertyContainer.Functions.length; methLoop++) {
                    Function curFunc = PropertyContainer.Functions[methLoop];
                    String funcText = blankfuncText;
                    funcText = funcText.replaceAll("%%METHODNAME%%", curFunc.Name);
                    funcText = funcText.replaceAll("%%INPUT%%", curFunc.InputType);
                    funcText = funcText.replaceAll("%%OUTPUT%%", curFunc.OutputType);
                    methText = methText + funcText + "\n";
                }
                classText = classText.replace("%%METHODS%%", methText);
            } else {
                throw new Exception("unable loads methods template");
            }
            String filePath = FileHelper.GetOutputFolderPath() + "/" + PropertyContainer.ServiceName + "Soap" + ".java";
            if (!FileHelper.WriteClassTextToFile(filePath, classText)) {
                throw new Exception("unable to create service file");
            }
        } else {
            throw new Exception("service template file not found");
        }
    }

    public static void CreateBaseObjectFile(String packagename) throws Exception {
        InputStream file = ClassProcessor.class.getResourceAsStream("resources/BaseObject.txt");
        if (file != null) {
            String classText = FileHelper.getContents(file);
            file.close();
            classText = classText.replaceAll("%%PACKAGENAME%%", packagename);
            classText = classText.replaceAll("%%NAMESPACE%%", PropertyContainer.Namespace);
            String filePath = FileHelper.GetOutputFolderPath() + "/BaseObject.java";
            FileHelper.WriteClassTextToFile(filePath, classText);
        } else {
            throw new Exception("could not locatie BaseObject template");
        }
    }

    public static void CreateLiteralVectorArrayFile(String packagename) throws Exception {
        InputStream file = ClassProcessor.class.getResourceAsStream("resources/LiteralArrayVector.txt");
        if (file != null) {
            String classText = FileHelper.getContents(file);
            file.close();
            classText = classText.replaceAll("%%PACKAGENAME%%", packagename);
            String filePath = FileHelper.GetOutputFolderPath() + "/LiteralArrayVector.java";
            FileHelper.WriteClassTextToFile(filePath, classText);
        } else {
            throw new Exception("could not locatie LiteralArrayVector template");
        }
    }

    public static void CreateClasess(String packagename) throws Exception {
        InputStream file = ClassProcessor.class.getResourceAsStream("resources/SoapComplexTypeClassTemplate.txt");
        if (file != null) {
            String blankText = FileHelper.getContents(file);
            file.close();
            for (SoapClass spClass : PropertyContainer.ComplexTypes) {
                if (spClass.isArray == true) {
                    InputStream litFile = ClassProcessor.class.getResourceAsStream("resources/ArrayObjectTemplate.txt");
                    if (litFile != null) {
                        String classText = FileHelper.getContents(litFile);
                        litFile.close();
                        classText = classText.replaceAll("%%PACKAGENAME%%", packagename);
                        classText = classText.replaceAll("%%CLASSNAME%%", spClass.Name);
                        classText = classText.replaceAll("%%ELEMENTTYPE%%", spClass.ElementType);
                        String filePath = FileHelper.GetOutputFolderPath() + "/" + spClass.Name + ".java";
                        FileHelper.WriteClassTextToFile(filePath, classText);
                    } else {
                        throw new Exception("could not locate Array Object Template");
                    }
                } else {
                    String classText = blankText;
                    classText = classText.replaceAll("%%PACKAGENAME%%", packagename);
                    classText = classText.replaceAll("%%CLASSNAME%%", spClass.Name);
                    String propText = "";
                    String getPropText = "";
                    String getPropInfoText = "";
                    String setPropText = "";
                    String regTypes = "";
                    int caseCount = 0;
                    List<SoapClassProperty> propertyArray = new ArrayList<SoapClassProperty>();
                    if (!spClass.SuperClassType.equals("BaseObject")) {
                        SoapClass superClass = PropertyContainer.GetClassWithName(spClass.SuperClassType);
                        if (superClass != null) {
                            propertyArray.addAll(superClass.Properties);
                        }
                    }
                    propertyArray.addAll(spClass.Properties);
                    classText = classText.replaceAll("%%PROPCOUNT%%", String.format("%d", propertyArray.size()));
                    for (SoapClassProperty prop : propertyArray) {
                        if (!prop.getIsArray()) {
                            propText += String.format("     public %s %s;", prop.getPropertyClassType(), prop.getPropertyName()) + "\n";
                        } else {
                            propText += String.format("     //array \n");
                            propText += String.format("     public %s %s;", prop.getPropertyClassType(), prop.getPropertyName()) + "\n";
                        }
                        getPropText += String.format("           case %d: \n", caseCount);
                        getPropText += String.format("                return %s; \n", prop.getPropertyName());
                        getPropInfoText += String.format("           case %d: \n", caseCount);
                        getPropInfoText += String.format("                info.name = \"%s\"; \n", prop.getPropertyName());
                        getPropInfoText += String.format("                info.type = %s; \n", getClassTypeRetrievalString(prop.getPropertyName(), prop.getPropertyClassType()));
                        getPropInfoText += String.format("                             break; \n", "");
                        setPropText += String.format("           case %d: \n", caseCount);
                        setPropText += String.format("                %s = %s; \n", prop.getPropertyName(), getConvertorForType(prop.getPropertyClassType()));
                        setPropText += String.format("                  break; \n", "");
                        if (prop.getIsComplexType()) {
                            regTypes += String.format("           new %s().register(envelope); \n", prop.getPropertyClassType());
                        }
                        caseCount++;
                    }
                    classText = classText.replaceAll("%%PROPERTIES%%", propText);
                    classText = classText.replaceAll("%%GETPROPERTY%%", getPropText);
                    classText = classText.replaceAll("%%GETPROPINFO%%", getPropInfoText);
                    classText = classText.replaceAll("%%SETPROP%%", setPropText);
                    classText = classText.replaceAll("%%REGISTERTYPES%%", regTypes);
                    String filePath = FileHelper.GetOutputFolderPath() + "/" + spClass.Name + ".java";
                    FileHelper.WriteClassTextToFile(filePath, classText);
                }
            }
        }
    }

    private static String getConvertorForType(String propType) {
        if (propType.equals("boolean")) {
            return String.format("Boolean.getBoolean(value.toString())", propType);
        } else if (propType.equals("int")) {
            return String.format("Integer.parseInt(value.toString())", propType);
        } else {
            return String.format("(%s)value", propType);
        }
    }

    private static String getClassTypeRetrievalString(String propName, String propType) {
        if (propType.equals("boolean")) {
            return String.format("PropertyInfo.BOOLEAN_CLASS", propType);
        } else if (propType.equals("int")) {
            return String.format("PropertyInfo.INTEGER_CLASS", propType);
        } else {
            return String.format("new %s().getClass()", propType);
        }
    }

    public static void CreateFunctionClasses(String packageName) throws Exception {
        for (Function fn : PropertyContainer.Functions) {
            SoapClass paramClass = PropertyContainer.GetClassWithName(fn.InputType);
            SoapClass returnClass = PropertyContainer.GetClassWithName(fn.OutputType);
            if (paramClass != null) {
                InputStream file = ClassProcessor.class.getResourceAsStream("resources/ParameterClassTemplate.txt");
                if (file != null) {
                    String classText = FileHelper.getContents(file);
                    file.close();
                    classText = classText.replaceAll("%%PACKAGENAME%%", packageName);
                    classText = classText.replaceAll("%%CLASSNAME%%", paramClass.Name);
                    String soapAction = fn.SoapAction.replace(PropertyContainer.Namespace, "");
                    classText = classText.replaceAll("%%SOAPMETHODNAME%%", soapAction);
                    classText = classText.replaceAll("%%NAMESPACE%%", PropertyContainer.Namespace);
                    String propText = "";
                    String soapPropText = "";
                    for (SoapClassProperty prop : paramClass.Properties) {
                        propText += String.format("     public %s %s;", prop.getPropertyClassType(), prop.getPropertyName()) + "\n";
                        soapPropText += String.format("     request.addProperty(\"%s\", %s);", prop.getPropertyName(), prop.getPropertyName()) + "\n";
                    }
                    classText = classText.replaceAll("%%PROPERTIES%%", propText);
                    classText = classText.replaceAll("%%SOAPPROPERTIES%%", soapPropText);
                    String filePath = FileHelper.GetOutputFolderPath() + "/" + paramClass.Name + ".java";
                    if (!FileHelper.WriteClassTextToFile(filePath, classText)) {
                        throw new Exception("unable to create parameter class file");
                    }
                } else {
                    throw new Exception("could not locatie Parameter Class template");
                }
            } else {
                throw new Exception("parameter class not found");
            }
            if (returnClass != null) {
                InputStream file = ClassProcessor.class.getResourceAsStream("resources/ResponseTemplate.txt");
                if (file != null) {
                    String classText = FileHelper.getContents(file);
                    file.close();
                    classText = classText.replaceAll("%%PACKAGENAME%%", packageName);
                    classText = classText.replaceAll("%%CLASSNAME%%", returnClass.Name);
                    SoapClassProperty prop = returnClass.Properties.get(0);
                    if (prop.getIsComplexType()) {
                        classText = classText.replaceAll("%%REGISTERTYPES%%", "new %%RESULTPROPTYPE%%().register(envelope);");
                    } else {
                        classText = classText.replaceAll("%%REGISTERTYPES%%", "");
                    }
                    classText = classText.replaceAll("%%GETPROPINFO%%", getClassTypeRetrievalString(prop.getPropertyName(), prop.getPropertyClassType()));
                    classText = classText.replaceAll("%%SETPROP%%", getConvertorForType(prop.getPropertyClassType()));
                    classText = classText.replaceAll("%%RESULTPROPNAME%%", prop.getPropertyName());
                    classText = classText.replaceAll("%%RESULTPROPTYPE%%", prop.getPropertyClassType());
                    String filePath = FileHelper.GetOutputFolderPath() + "/" + returnClass.Name + ".java";
                    if (!FileHelper.WriteClassTextToFile(filePath, classText)) {
                        throw new Exception("unable to create return class file");
                    }
                } else {
                    throw new Exception("could not locatie Return Class template");
                }
            } else {
                throw new Exception("return class not found");
            }
        }
    }
}

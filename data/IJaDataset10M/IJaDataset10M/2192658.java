package net.simplemodel.core.generator.internal;

import net.simplemodel.core.generator.*;

public class PropertyTemplate implements ITemplate {

    protected static String nl;

    public static synchronized PropertyTemplate create(String lineSeparator) {
        nl = lineSeparator;
        PropertyTemplate result = new PropertyTemplate();
        nl = null;
        return result;
    }

    public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "class Property implements Comparable<Property> {" + NL + "\t";

    protected final String TEXT_2 = "private static ";

    protected final String TEXT_3 = " getGetter(Class<?> propertyOf, String name)" + NL + "\t\t\tthrows SecurityException, NoSuchMethodException {" + NL + "\t\ttry {" + NL + "\t\t\treturn propertyOf.getMethod(\"is\" + upperCaseFirst(name));" + NL + "\t\t} catch (NoSuchMethodException e) {" + NL + "\t\t\treturn propertyOf.getMethod(\"get\" + upperCaseFirst(name));" + NL + "\t\t}" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_4 = "private static ";

    protected final String TEXT_5 = " getSettter(Class<?> propertyOf, String name," + NL + "\t\t\tClass<?> propertyType) throws SecurityException," + NL + "\t\t\tNoSuchMethodException {" + NL + "\t\treturn propertyOf.getMethod(\"set\" + upperCaseFirst(name), propertyType);" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_6 = "private static String upperCaseFirst(String s) {" + NL + "\t\tassertTrue(s.length() > 0);" + NL + "\t\treturn Character.toUpperCase(s.charAt(0)) + s.substring(1);" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_7 = "private final String name;" + NL + "\t" + NL + "\t";

    protected final String TEXT_8 = "private final Class<?> propertyOf;" + NL + "" + NL + "\t";

    protected final String TEXT_9 = "private final Class<?> propertyType;" + NL + "" + NL + "\t";

    protected final String TEXT_10 = "private final ";

    protected final String TEXT_11 = " getterMethod;" + NL + "" + NL + "\t";

    protected final String TEXT_12 = "private final ";

    protected final String TEXT_13 = " setterMethod;" + NL + "" + NL + "\t";

    protected final String TEXT_14 = "public Property(Class<?> propertyOf, String name) throws SecurityException," + NL + "\t\t\tNoSuchMethodException {" + NL + "\t\tthis.name = name;" + NL + "\t\tthis.propertyOf = propertyOf;" + NL + "\t\tgetterMethod = getGetter(propertyOf, name);" + NL + "\t\tpropertyType = getterMethod.getReturnType();" + NL + "\t\tsetterMethod = getSettter(propertyOf, name, propertyType);" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_15 = "@Override" + NL + "\tpublic int compareTo(Property o) {" + NL + "\t\tif (o == null)" + NL + "\t\t\treturn -1;" + NL + "\t\treturn name.compareTo(o.name);" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_16 = "@Override" + NL + "\tpublic boolean equals(Object obj) {" + NL + "\t\tif (this == obj)" + NL + "\t\t\treturn true;" + NL + "\t\tif (obj == null)" + NL + "\t\t\treturn false;" + NL + "\t\tif (getClass() != obj.getClass())" + NL + "\t\t\treturn false;" + NL + "\t\tProperty other = (Property) obj;" + NL + "\t\treturn name.equals(other.name);" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_17 = "public Object get(Object owner) {" + NL + "\t\tpropertyOf.cast(owner);" + NL + "\t\ttry {" + NL + "\t\t\treturn getterMethod.invoke(owner);" + NL + "\t\t} catch (IllegalAccessException e) {" + NL + "\t\t\tthrow new RuntimeException(e);" + NL + "\t\t} catch (";

    protected final String TEXT_18 = " e) {" + NL + "\t\t\tthrow new RuntimeException(e);" + NL + "\t\t}" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_19 = "public String getName() {" + NL + "\t\treturn name;" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_20 = "public Class<?> getPropertyOf() {" + NL + "\t\treturn propertyOf;" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_21 = "public Class<?> getPropertyType() {" + NL + "\t\treturn propertyType;" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_22 = "@Override" + NL + "\tpublic int hashCode() {" + NL + "\t\treturn name.hashCode();" + NL + "\t}" + NL + "" + NL + "\t";

    protected final String TEXT_23 = "public void set(Object owner, Object value) {" + NL + "\t\ttry {" + NL + "\t\t\tsetterMethod.invoke(propertyOf.cast(owner), propertyType.cast(value));" + NL + "\t\t} catch (IllegalAccessException e) {" + NL + "\t\t\tthrow new RuntimeException(e);" + NL + "\t\t} catch (";

    protected final String TEXT_24 = " e) {" + NL + "\t\t\tthrow new RuntimeException(e);" + NL + "\t\t}" + NL + "\t}" + NL + "}";

    void willNotGenerate() {
        throw new WillNotGenerateException();
    }

    @Override
    public String generate(ITemplateContext argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        IImportBlock importBlock = argument.getImportBlock();
        stringBuffer.append(TEXT_1);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_2);
        stringBuffer.append(importBlock.imprt(CommonTypes.METHOD));
        stringBuffer.append(TEXT_3);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_4);
        stringBuffer.append(importBlock.imprt(CommonTypes.METHOD));
        stringBuffer.append(TEXT_5);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_6);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_7);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_8);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_9);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_10);
        stringBuffer.append(importBlock.imprt(CommonTypes.METHOD));
        stringBuffer.append(TEXT_11);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_12);
        stringBuffer.append(importBlock.imprt(CommonTypes.METHOD));
        stringBuffer.append(TEXT_13);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_14);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_15);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_16);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_17);
        stringBuffer.append(importBlock.imprt(CommonTypes.INVOCATION_TARGET_EXCEPTION));
        stringBuffer.append(TEXT_18);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_19);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_20);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_21);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_22);
        stringBuffer.append(argument.getGeneratedJavadoc(NL));
        stringBuffer.append(TEXT_23);
        stringBuffer.append(importBlock.imprt(CommonTypes.INVOCATION_TARGET_EXCEPTION));
        stringBuffer.append(TEXT_24);
        return stringBuffer.toString();
    }
}

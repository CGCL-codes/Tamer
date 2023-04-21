package net.sf.joafip.store.service.conversion.def;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.classinfo.FieldInfo;
import net.sf.joafip.store.entity.conversion.input.ConversionDefEntry;
import net.sf.joafip.store.service.classinfo.ClassInfoException;
import net.sf.joafip.store.service.classinfo.ClassInfoFactory;
import net.sf.joafip.store.service.conversion.ConversionException;

/**
 * conversion definition reader<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public abstract class AbstractConversionDefinitionReader {

    private final BufferedReader bufferedReader;

    private final ClassInfoFactory classInfoFactory;

    private final FieldDefParser fieldDefParser = new FieldDefParser();

    private transient int currentLineNumber;

    private transient int beginLineNumber;

    private transient int endLineNumber;

    private transient String currentLine;

    private final URI source;

    /**
	 * 
	 * @param inputStream
	 *            input stream of conversion definition
	 * @param classInfoFactory
	 * @throws ConversionException
	 */
    public AbstractConversionDefinitionReader(final InputStreamAndSource inputStream, final ClassInfoFactory classInfoFactory) throws ConversionException {
        super();
        this.classInfoFactory = classInfoFactory;
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            throw new ConversionException(exception);
        }
        bufferedReader = new BufferedReader(inputStreamReader);
        currentLineNumber = 0;
        beginLineNumber = 0;
        endLineNumber = 0;
        source = inputStream.getSource();
    }

    /**
	 * 
	 * @return a conversion definition, null if no more conversion definition
	 * @throws ConversionException
	 */
    public ConversionDefEntry read() throws ConversionException {
        final List<String> list = extractList();
        final ConversionDefEntry conversionDefEntry;
        if (list.isEmpty()) {
            conversionDefEntry = null;
        } else {
            try {
                conversionDefEntry = conversionDefFromList(list);
            } catch (Exception exception) {
                throw new ConversionException("lines #" + beginLineNumber + " to #" + endLineNumber + " in " + source.toString() + "\n" + currentLine, exception);
            }
        }
        return conversionDefEntry;
    }

    private ConversionDefEntry conversionDefFromList(final List<String> list) throws ConversionException {
        final int listSize = list.size();
        if (listSize < 3) {
            throw new ConversionException("too less elements " + listSize);
        }
        final String[] elts = new String[listSize];
        list.toArray(elts);
        final int oldIdentifier = computeIdentifier(elts[0]);
        try {
            fieldDefParser.parse(elts[1]);
        } catch (ConversionException exception) {
            throw new ConversionException("bad original class/field definition \"" + elts[1], exception);
        }
        final String originalClassName = fieldDefParser.getClassName();
        if (originalClassName == null) {
            throw new ConversionException("bad original class/field definition \"" + elts[1]);
        }
        final ClassInfo originalClass = classForName(originalClassName);
        final String originalFieldName = fieldDefParser.getFieldName();
        final int originalFieldIndex = fieldDefParser.getFieldIndex();
        final String originalFieldClassName = normalizeClassName(fieldDefParser.getFieldClassName());
        final String originalFieldDeclaringClassName = fieldDefParser.getFieldDeclaringClassName();
        final Boolean originalStaticField = fieldDefParser.getStaticField();
        final Boolean originalTransientField = fieldDefParser.getTransientField();
        final ConversionDefEntry conversionDefEntry;
        if ("*".equals(originalFieldName)) {
            final int toIdentifier = computeIdentifier(elts[2]);
            conversionDefEntry = new ConversionDefEntry(originalClass, oldIdentifier, toIdentifier);
            for (int fieldDefIndex = 3; fieldDefIndex < elts.length; fieldDefIndex++) {
                try {
                    fieldDefParser.parse(elts[fieldDefIndex]);
                } catch (ConversionException exception) {
                    throw new ConversionException("bad field definition \"" + elts[fieldDefIndex] + "\"", exception);
                }
                if (fieldDefParser.getClassName() != null) {
                    throw new ConversionException("bad field definition \"" + elts[fieldDefIndex] + "\"");
                }
                final String fieldClassName = normalizeClassName(fieldDefParser.getFieldClassName());
                final Boolean staticField = fieldDefParser.getStaticField();
                final Boolean transientField = fieldDefParser.getTransientField();
                final String declaringClassName = fieldDefParser.getFieldDeclaringClassName();
                final String fieldName = fieldDefParser.getFieldName();
                final int fieldIndex = fieldDefParser.getFieldIndex();
                if (fieldIndex != -1) {
                    throw new ConversionException("bad field definition, must not have field index declaration \"" + elts[fieldDefIndex]);
                }
                final ClassInfo fieldClass = classForName(fieldClassName);
                final ClassInfo declaringClass = classForName(declaringClassName);
                FieldInfo fieldInfo;
                try {
                    fieldInfo = new FieldInfo(declaringClass, fieldName, fieldClass, staticField, transientField);
                } catch (ClassInfoException exception) {
                    throw new ConversionException("bad field definition \"" + elts[fieldDefIndex], exception);
                }
                conversionDefEntry.addToFieldList(fieldInfo);
            }
        } else {
            if (listSize < 3 || listSize > 5) {
                throw new ConversionException("3 thru 5 elements expected for " + listSize + " get");
            } else {
                final int toIdentifier = computeIdentifier(elts[2]);
                if (listSize == 3) {
                    conversionDefEntry = new ConversionDefEntry(originalClass, oldIdentifier, ClassInfo.NULL, toIdentifier, ClassInfo.NULL, ClassInfo.NULL);
                } else {
                    final ClassInfo converterClass;
                    final ClassInfo staticConverterClass;
                    if (listSize == 5) {
                        final String[] converterClassDef = elts[4].split(":");
                        if (converterClassDef.length == 1) {
                            converterClass = classForName(converterClassDef[0]);
                            staticConverterClass = ClassInfo.NULL;
                        } else if (converterClassDef.length == 2 && "static".equals(converterClassDef[0])) {
                            staticConverterClass = classForName(converterClassDef[1]);
                            converterClass = ClassInfo.NULL;
                        } else {
                            throw new ConversionException("bad conversion class declaration \"" + elts[4] + "\"");
                        }
                    } else {
                        converterClass = ClassInfo.NULL;
                        staticConverterClass = ClassInfo.NULL;
                    }
                    try {
                        fieldDefParser.parse(elts[3]);
                    } catch (ConversionException exception) {
                        throw new ConversionException("bad replacemet class/field definition \"" + elts[3] + "\"", exception);
                    }
                    final String replacementClassName = fieldDefParser.getClassName();
                    if (replacementClassName == null) {
                        throw new ConversionException("bad replacement class/field definition \"" + elts[3] + "\"");
                    }
                    final String replacementFieldName = fieldDefParser.getFieldName();
                    final int replacementFieldIndex = fieldDefParser.getFieldIndex();
                    final String replacementFieldClassName = normalizeClassName(fieldDefParser.getFieldClassName());
                    final String replacementFieldDeclaringClassName = fieldDefParser.getFieldDeclaringClassName();
                    final Boolean replacementStaticField = fieldDefParser.getStaticField();
                    final Boolean replacementTransientField = fieldDefParser.getTransientField();
                    final ClassInfo replacementFieldDeclaringClass;
                    final ClassInfo replacementClass = classForName(replacementClassName);
                    if (replacementFieldDeclaringClassName == null) {
                        replacementFieldDeclaringClass = replacementClass;
                    } else {
                        replacementFieldDeclaringClass = classForName(replacementFieldDeclaringClassName);
                    }
                    if (originalFieldName == null) {
                        conversionDefEntry = new ConversionDefEntry(originalClass, oldIdentifier, replacementClass, toIdentifier, converterClass, staticConverterClass);
                    } else {
                        final ClassInfo originalFieldDeclaringClass;
                        if (originalFieldDeclaringClassName == null) {
                            originalFieldDeclaringClass = originalClass;
                        } else {
                            originalFieldDeclaringClass = classForName(originalFieldDeclaringClassName);
                        }
                        final ClassInfo originalFieldType;
                        if (originalFieldClassName == null) {
                            originalFieldType = ClassInfo.NULL;
                        } else {
                            originalFieldType = classForName(originalFieldClassName);
                        }
                        final FieldInfo originalFieldInfo;
                        try {
                            originalFieldInfo = new FieldInfo(originalFieldDeclaringClass, originalFieldName, originalFieldType, originalStaticField, originalTransientField);
                        } catch (ClassInfoException exception) {
                            throw new ConversionException("bad replacement class \"" + elts[1] + "\"", exception);
                        }
                        try {
                            final FieldInfo replacementFieldInfo;
                            if (replacementFieldName == null) {
                                replacementFieldInfo = null;
                            } else {
                                final ClassInfo replacementFieldType;
                                if (replacementFieldClassName == null) {
                                    throw new ConversionException("bad replacement class/field type must be defined \"" + elts[3] + "\"");
                                } else {
                                    replacementFieldType = classForName(replacementFieldClassName);
                                }
                                replacementFieldInfo = new FieldInfo(replacementFieldDeclaringClass, replacementFieldName, replacementFieldType, replacementStaticField, replacementTransientField);
                            }
                            conversionDefEntry = new ConversionDefEntry(originalClass, oldIdentifier, replacementClass, toIdentifier, originalFieldInfo, originalFieldIndex, replacementFieldInfo, replacementFieldIndex);
                        } catch (ClassInfoException exception) {
                            throw new ConversionException("bad replacement class \"" + elts[3] + "\"", exception);
                        }
                    }
                }
            }
        }
        return conversionDefEntry;
    }

    protected abstract int computeIdentifier(final String stringIdentifier) throws ConversionException;

    private String normalizeClassName(final String classNameFromDef) {
        final String normalizedClassName;
        if (classNameFromDef == null) {
            normalizedClassName = null;
        } else {
            final int index = classNameFromDef.lastIndexOf('[');
            if (index == -1) {
                normalizedClassName = classNameFromDef;
            } else {
                normalizedClassName = classNameFromDef.substring(0, index + 1) + "L" + classNameFromDef.substring(index + 1) + ";";
            }
        }
        return normalizedClassName;
    }

    /**
	 * 
	 * @param className
	 * @return class for a class name
	 * @throws ConversionException
	 */
    private ClassInfo classForName(final String className) throws ConversionException {
        try {
            final ClassInfo classForName;
            if (className == null) {
                throw new ConversionException("class name must be defined");
            } else {
                if ("boolean".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(boolean.class);
                } else if ("Boolean".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(Boolean.class);
                } else if ("byte".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(byte.class);
                } else if ("Byte".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(Byte.class);
                } else if ("short".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(short.class);
                } else if ("Short".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(Short.class);
                } else if ("int".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(int.class);
                } else if ("Integer".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(Integer.class);
                } else if ("long".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(long.class);
                } else if ("Long".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(Long.class);
                } else if ("float".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(float.class);
                } else if ("Float".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(Float.class);
                } else if ("double".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(double.class);
                } else if ("Double".equals(className)) {
                    classForName = classInfoFactory.getNoProxyClassInfo(Double.class);
                } else {
                    classForName = classInfoFactory.getClassInfo(className);
                }
            }
            return classForName;
        } catch (ClassNotFoundException exception) {
            throw new ConversionException(exception);
        } catch (ClassInfoException exception) {
            throw new ConversionException(exception);
        }
    }

    private List<String> extractList() throws ConversionException {
        String line;
        boolean done = false;
        final List<String> list = new LinkedList<String>();
        do {
            line = readDefinition();
            if (line == null) {
                done = true;
            } else {
                int fromIndex = 0;
                int index;
                do {
                    index = line.indexOf(';', fromIndex);
                    if (index == -1) {
                        index = line.indexOf(',', fromIndex);
                    }
                    final String substring;
                    if (index == -1) {
                        substring = line.substring(fromIndex).trim();
                        done = true;
                    } else {
                        substring = line.substring(fromIndex, index).trim();
                    }
                    if (substring.length() == 0) {
                        if (index != -1) {
                            list.add(substring);
                            fromIndex = index + 1;
                        }
                        done = false;
                    } else {
                        list.add(substring);
                        fromIndex = index + 1;
                    }
                } while (!done && index != -1);
            }
        } while (!done);
        return list;
    }

    private String readDefinition() throws ConversionException {
        final StringBuilder stringBuilder = new StringBuilder();
        String line;
        boolean addLine;
        beginLineNumber = -1;
        do {
            line = readLine();
            if (beginLineNumber == -1) {
                beginLineNumber = currentLineNumber;
            }
            endLineNumber = currentLineNumber;
            if (line == null) {
                addLine = false;
            } else {
                final String timmedLine = line.trim();
                stringBuilder.append(timmedLine);
                final int lineLength = timmedLine.length();
                if (lineLength == 0) {
                    addLine = false;
                } else {
                    final char lastChar = timmedLine.charAt(lineLength - 1);
                    addLine = lastChar == ';' || lastChar == ',' || lastChar == '#' || lastChar == ':';
                }
            }
        } while (addLine);
        currentLine = stringBuilder.toString();
        return (line == null && currentLine.length() == 0) ? null : currentLine;
    }

    private String readLine() throws ConversionException {
        String line;
        try {
            do {
                currentLineNumber++;
                line = bufferedReader.readLine();
            } while (line != null && line.length() > 0 && line.charAt(0) == '#');
        } catch (IOException exception) {
            throw new ConversionException(exception);
        }
        return line;
    }

    public void close() throws ConversionException {
        try {
            bufferedReader.close();
        } catch (IOException exception) {
            throw new ConversionException(exception);
        }
    }
}

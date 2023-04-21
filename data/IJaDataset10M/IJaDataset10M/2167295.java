package org.cleartk.test.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Assert;
import org.uimafit.factory.ConfigurationParameterFactory;

/**
 * <br>
 * Copyright (c) 2009, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Philip Ogren
 */
public class ParametersTestUtil {

    public static void testParameterDefinitions(String outputDirectory, String... excludeFiles) throws ClassNotFoundException {
        IOFileFilter includeFilter = new SuffixFileFilter(".java");
        if (excludeFiles != null) {
            IOFileFilter excludeFilter = FileFilterUtils.notFileFilter(new SuffixFileFilter(excludeFiles));
            includeFilter = FileFilterUtils.and(excludeFilter, includeFilter);
        }
        Iterator<File> files = org.apache.commons.io.FileUtils.iterateFiles(new File(outputDirectory), includeFilter, TrueFileFilter.INSTANCE);
        testParameterDefinitions(files);
    }

    public static void testParameterDefinitions(Iterator<File> files) throws ClassNotFoundException {
        List<String> badParameters = new ArrayList<String>();
        List<String> missingParameterNameFields = new ArrayList<String>();
        while (files.hasNext()) {
            File file = files.next();
            String className = file.getPath();
            className = className.substring(14);
            className = className.substring(0, className.length() - 5);
            className = className.replace(File.separatorChar, '.');
            Class<?> cls = Class.forName(className);
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (ConfigurationParameterFactory.isConfigurationParameterField(field)) {
                    org.uimafit.descriptor.ConfigurationParameter annotation = field.getAnnotation(org.uimafit.descriptor.ConfigurationParameter.class);
                    String parameterName = annotation.name();
                    String expectedName = className + "." + field.getName();
                    if (parameterName.equals(org.uimafit.descriptor.ConfigurationParameter.USE_FIELD_NAME)) expectedName = org.uimafit.descriptor.ConfigurationParameter.USE_FIELD_NAME;
                    if (!expectedName.equals(parameterName)) {
                        badParameters.add("'" + parameterName + "' should be '" + expectedName + "'");
                    }
                    expectedName = className + "." + field.getName();
                    String fieldName = getParameterNameField(expectedName);
                    try {
                        Field fld = cls.getDeclaredField(fieldName);
                        if ((fld.getModifiers() & Modifier.PUBLIC) == 0 || (fld.getModifiers() & Modifier.FINAL) == 0 || (fld.getModifiers() & Modifier.PUBLIC) == 0) {
                            missingParameterNameFields.add(expectedName);
                        } else if (!fld.get(null).equals(expectedName)) {
                            missingParameterNameFields.add(expectedName);
                        }
                    } catch (Exception e) {
                        missingParameterNameFields.add(expectedName);
                    }
                }
            }
        }
        if (badParameters.size() > 0 || missingParameterNameFields.size() > 0) {
            String message = String.format("%d descriptor parameters with bad names and %d descriptor parameters with no name field. ", badParameters.size(), missingParameterNameFields.size());
            System.err.println(message);
            System.err.println("descriptor parameters with bad names: ");
            for (String badParameter : badParameters) {
                System.err.println(badParameter);
            }
            System.err.println("each configuration parameter should have a public static final String that specifies its name.  The missing fields are: ");
            for (String missingParameterNameField : missingParameterNameFields) {
                System.err.println(missingParameterNameField + " should be named by " + missingParameterNameField.substring(0, missingParameterNameField.lastIndexOf('.')) + "." + getParameterNameField(missingParameterNameField));
            }
            Assert.fail(message);
        }
    }

    private static String getParameterNameField(String parameterName) {
        String parameterNameField = "PARAM";
        String fieldName = parameterName.substring(parameterName.lastIndexOf('.') + 1);
        String[] fieldNameParts = fieldName.split("(?=[A-Z]++)");
        for (String fieldNamePart : fieldNameParts) {
            parameterNameField += "_" + fieldNamePart.toUpperCase();
        }
        return parameterNameField;
    }
}

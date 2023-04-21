package com.google.gwt.search.jsio.rebind;

import com.google.gwt.search.jsio.client.impl.JSONWrapperUtil;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Encapsulates accessors for String properties.
 */
class StringFragmentGenerator extends FragmentGenerator {

    @Override
    boolean accepts(TypeOracle oracle, JType type) {
        JClassType asClass = type.isClassOrInterface();
        if (asClass == null) {
            return false;
        } else {
            return isAssignable(oracle, asClass, String.class);
        }
    }

    @Override
    void fromJS(FragmentGeneratorContext context) throws UnableToCompleteException {
        context.parentLogger.branch(TreeLogger.DEBUG, "Building string value getter statement", null);
        SourceWriter sw = context.sw;
        sw.print(context.parameterName);
    }

    @Override
    boolean isIdentity() {
        return true;
    }

    @Override
    void toJS(FragmentGeneratorContext context) throws UnableToCompleteException {
        context.parentLogger.branch(TreeLogger.DEBUG, "Building string value setter statement", null);
        SourceWriter sw = context.sw;
        sw.print(context.parameterName);
    }

    @Override
    void writeExtractorJSNIReference(FragmentGeneratorContext context) {
        SourceWriter sw = context.sw;
        sw.print("@" + JSONWrapperUtil.class.getName() + "::STRING_EXTRACTOR");
    }
}

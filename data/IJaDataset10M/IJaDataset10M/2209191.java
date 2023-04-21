package tm.cpp.ast;

import tm.clc.ast.TypeNode;

/**
 * Identifying (tag) interface for fundamental types in C++
 */
public interface TyFundamental {

    public static final String INVALID_CV_QUALIFICATION = "The cv-qualification value provided is invalid";

    public TypeNode getQualified(int cv_qual);
}

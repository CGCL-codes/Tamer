package com.example.mini;

import com.example.mini.cmodel.CBinary;
import com.example.mini.cmodel.CFunctionCall;
import com.example.mini.cmodel.CInteger;
import com.example.mini.cmodel.CString;
import com.example.mini.cmodel.CVariable;

public interface IExprVisitor {

    void visitFunctionCall(CFunctionCall cFunctionCall);

    void visitVariable(CVariable cVariable);

    void visitBinary(CBinary cBinary);

    void visitInteger(CInteger cInteger);

    void visitString(CString cString);
}

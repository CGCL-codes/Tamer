package com.dustedpixels.asm;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.SourceInterpreter;
import com.dustedpixels.asm.analysis.Analyzers;
import com.dustedpixels.asm.analysis.FieldInterpreter;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class FieldInliner {

    public static MethodNode inlineField(String className, MethodNode method, FieldNode field) {
        try {
            method = MethodNodes.copy(method);
            Interpreter fieldInterpreter = new FieldInterpreter(field.name);
            Analyzer fieldAnalyzer = Analyzers.newUsing(fieldInterpreter);
            Frame[] fieldFrames = fieldAnalyzer.analyze(className, method);
            Interpreter sourceInterpreter = new SourceInterpreter();
            Analyzer sourceAnalyzer = Analyzers.newUsing(sourceInterpreter);
            Frame[] sourceFrames = sourceAnalyzer.analyze(className, method);
            return method;
        } catch (AnalyzerException e) {
            throw new RuntimeException(e);
        }
    }
}

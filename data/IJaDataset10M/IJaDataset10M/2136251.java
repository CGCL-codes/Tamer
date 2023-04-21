package org.sodbeans.compiler.hop.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.text.Document;
import org.netbeans.modules.parsing.spi.Parser.Result;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;
import org.sonify.vm.CompilerError;
import org.sonify.vm.hop.HopVirtualMachine;

/**
 * Many thanks to the NetBeans wiki:
 * http://wiki.netbeans.org/New_Language_Support_Tutorial_Antlr
 *
 * @author Andreas Stefik
 */
public class SyntaxErrorsHighlightingTask extends ParserResultTask {

    @Override
    public void run(Result result, SchedulerEvent event) {
        try {
            HopParserResult res = (HopParserResult) result;
            File file = org.openide.filesystems.FileUtil.toFile(res.getSnapshot().getSource().getFileObject());
            HopVirtualMachine vm = res.getVirtualMachine();
            Iterator<CompilerError> errors = vm.getCompilerErrors().iterator(file.getAbsolutePath());
            Document document = result.getSnapshot().getSource().getDocument(false);
            List<ErrorDescription> nbErrors = new ArrayList<ErrorDescription>();
            while (errors.hasNext()) {
                ErrorDescription errorDescription;
                CompilerError error = errors.next();
                errorDescription = ErrorDescriptionFactory.createErrorDescription(Severity.ERROR, error.getError(), document, error.getLineNumber());
                nbErrors.add(errorDescription);
            }
            HintsController.setErrors(document, "hop", nbErrors);
        } catch (Exception ex) {
        } catch (AssertionError error) {
        }
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
    }
}

package bank.domain.validation;

import java.util.*;
import org.drools.runtime.*;
import org.drools.runtime.rule.*;
import org.joda.time.*;

public class ValidationHelper {

    public static void error(RuleContext kcontext, Object... context) {
        event(MessageType.error, kcontext, context);
    }

    public static void warning(RuleContext kcontext, Object... context) {
        event(MessageType.warning, kcontext, context);
    }

    private static void event(MessageType type, RuleContext kcontext, Object... context) {
        KnowledgeRuntime knowledgeRuntime = kcontext.getKnowledgeRuntime();
        ValidationReport validationReport = (ValidationReport) knowledgeRuntime.getGlobal("validationReport");
        ReportFactory reportFactory = (ReportFactory) knowledgeRuntime.getGlobal("reportFactory");
        validationReport.addMessage(reportFactory.createMessage(type, kcontext.getRule().getName(), context));
    }

    public static int age(Date arg) {
        return Years.yearsBetween(new DateMidnight(arg.getTime()), new DateMidnight()).getYears();
    }
}

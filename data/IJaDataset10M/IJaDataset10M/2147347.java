package org.emftext.language.OCL.resource.OCL.mopp;

public class OCLProblem implements org.emftext.language.OCL.resource.OCL.IOCLProblem {

    private String message;

    private org.emftext.language.OCL.resource.OCL.OCLEProblemType type;

    private org.emftext.language.OCL.resource.OCL.OCLEProblemSeverity severity;

    private java.util.Collection<org.emftext.language.OCL.resource.OCL.IOCLQuickFix> quickFixes;

    public OCLProblem(String message, org.emftext.language.OCL.resource.OCL.OCLEProblemType type, org.emftext.language.OCL.resource.OCL.OCLEProblemSeverity severity) {
        this(message, type, severity, java.util.Collections.<org.emftext.language.OCL.resource.OCL.IOCLQuickFix>emptySet());
    }

    public OCLProblem(String message, org.emftext.language.OCL.resource.OCL.OCLEProblemType type, org.emftext.language.OCL.resource.OCL.OCLEProblemSeverity severity, org.emftext.language.OCL.resource.OCL.IOCLQuickFix quickFix) {
        this(message, type, severity, java.util.Collections.singleton(quickFix));
    }

    public OCLProblem(String message, org.emftext.language.OCL.resource.OCL.OCLEProblemType type, org.emftext.language.OCL.resource.OCL.OCLEProblemSeverity severity, java.util.Collection<org.emftext.language.OCL.resource.OCL.IOCLQuickFix> quickFixes) {
        super();
        this.message = message;
        this.type = type;
        this.severity = severity;
        this.quickFixes = new java.util.LinkedHashSet<org.emftext.language.OCL.resource.OCL.IOCLQuickFix>();
        this.quickFixes.addAll(quickFixes);
    }

    public org.emftext.language.OCL.resource.OCL.OCLEProblemType getType() {
        return type;
    }

    public org.emftext.language.OCL.resource.OCL.OCLEProblemSeverity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    public java.util.Collection<org.emftext.language.OCL.resource.OCL.IOCLQuickFix> getQuickFixes() {
        return quickFixes;
    }
}

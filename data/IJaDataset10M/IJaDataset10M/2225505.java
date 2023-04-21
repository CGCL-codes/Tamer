package fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.ui;

public class Ocl4tstQuickAssistAssistant extends org.eclipse.jface.text.quickassist.QuickAssistAssistant implements org.eclipse.jface.text.quickassist.IQuickAssistAssistant {

    public Ocl4tstQuickAssistAssistant(fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstResourceProvider resourceProvider, fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.ui.IOcl4tstAnnotationModelProvider annotationModelProvider) {
        setQuickAssistProcessor(new fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.ui.Ocl4tstQuickAssistProcessor(resourceProvider, annotationModelProvider));
        setInformationControlCreator(new org.eclipse.jface.text.AbstractReusableInformationControlCreator() {

            public org.eclipse.jface.text.IInformationControl doCreateInformationControl(org.eclipse.swt.widgets.Shell parent) {
                return new org.eclipse.jface.text.DefaultInformationControl(parent, (org.eclipse.jface.text.DefaultInformationControl.IInformationPresenter) null);
            }
        });
    }

    public boolean canAssist(org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext invocationContext) {
        return false;
    }

    public boolean canFix(org.eclipse.jface.text.source.Annotation annotation) {
        return true;
    }
}

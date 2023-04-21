package fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui;

public class AlfUIMetaInformation extends fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfMetaInformation {

    public fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfHoverTextProvider getHoverTextProvider() {
        return new fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfHoverTextProvider();
    }

    public fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfImageProvider getImageProvider() {
        return fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfImageProvider.INSTANCE;
    }

    public fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfColorManager createColorManager() {
        return new fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfColorManager();
    }

    /**
	 * @deprecated this method is only provided to preserve API compatibility. Use
	 * createTokenScanner(fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfTextRes
	 * ource, fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfColorManager)
	 * instead.
	 */
    public fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfTokenScanner createTokenScanner(fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfColorManager colorManager) {
        return createTokenScanner(null, colorManager);
    }

    public fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfTokenScanner createTokenScanner(fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfTextResource resource, fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfColorManager colorManager) {
        return new fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfTokenScanner(resource, colorManager);
    }

    public fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfCodeCompletionHelper createCodeCompletionHelper() {
        return new fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.ui.AlfCodeCompletionHelper();
    }
}

package org.emftext.language.office.resource.office.ui;

public class OfficeHyperlinkDetector implements org.eclipse.jface.text.hyperlink.IHyperlinkDetector {

    private org.emftext.language.office.resource.office.IOfficeTextResource textResource;

    public OfficeHyperlinkDetector(org.eclipse.emf.ecore.resource.Resource resource) {
        textResource = (org.emftext.language.office.resource.office.IOfficeTextResource) resource;
    }

    public org.eclipse.jface.text.hyperlink.IHyperlink[] detectHyperlinks(org.eclipse.jface.text.ITextViewer textViewer, org.eclipse.jface.text.IRegion region, boolean canShowMultipleHyperlinks) {
        org.emftext.language.office.resource.office.IOfficeLocationMap locationMap = textResource.getLocationMap();
        String resourceFileExtension = textResource.getURI().fileExtension();
        java.util.List<org.eclipse.emf.ecore.EObject> elementsAtOffset = locationMap.getElementsAt(region.getOffset());
        org.eclipse.emf.ecore.EObject resolvedEObject = null;
        for (org.eclipse.emf.ecore.EObject eObject : elementsAtOffset) {
            if (eObject.eIsProxy()) {
                resolvedEObject = org.eclipse.emf.ecore.util.EcoreUtil.resolve(eObject, textResource);
                if (resolvedEObject == eObject || (resolvedEObject.eResource() != null && !resourceFileExtension.equals(resolvedEObject.eResource().getURI().fileExtension()))) {
                    continue;
                }
                int offset = locationMap.getCharStart(eObject);
                int length = locationMap.getCharEnd(eObject) - offset + 1;
                String text = null;
                try {
                    text = textViewer.getDocument().get(offset, length);
                } catch (org.eclipse.jface.text.BadLocationException e) {
                }
                org.eclipse.jface.text.hyperlink.IHyperlink hyperlink = new org.emftext.language.office.resource.office.ui.OfficeHyperlink(new org.eclipse.jface.text.Region(offset, length), resolvedEObject, text);
                return new org.eclipse.jface.text.hyperlink.IHyperlink[] { hyperlink };
            }
        }
        return null;
    }
}

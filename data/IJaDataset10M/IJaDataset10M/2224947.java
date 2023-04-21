package org.emftext.language.office.resource.office.util;

public class OfficeResourceUtil {

    public static java.util.List<org.eclipse.emf.ecore.EObject> findUnresolvedProxies(org.eclipse.emf.ecore.resource.Resource resource) {
        java.util.List<org.eclipse.emf.ecore.EObject> unresolveProxies = new java.util.ArrayList<org.eclipse.emf.ecore.EObject>();
        for (java.util.Iterator<org.eclipse.emf.ecore.EObject> elementIt = org.eclipse.emf.ecore.util.EcoreUtil.getAllContents(resource, true); elementIt.hasNext(); ) {
            org.eclipse.emf.ecore.InternalEObject nextElement = (org.eclipse.emf.ecore.InternalEObject) elementIt.next();
            if (nextElement.eIsProxy()) {
                unresolveProxies.add(nextElement);
            }
            for (org.eclipse.emf.ecore.EObject crElement : nextElement.eCrossReferences()) {
                crElement = org.eclipse.emf.ecore.util.EcoreUtil.resolve(crElement, resource);
                if (crElement.eIsProxy()) {
                    unresolveProxies.add(nextElement);
                }
            }
        }
        return unresolveProxies;
    }

    public static boolean resolveAll(org.eclipse.emf.ecore.resource.Resource resource) {
        org.eclipse.emf.ecore.util.EcoreUtil.resolveAll(resource);
        if (findUnresolvedProxies(resource).size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void saveResource(java.io.File file, org.eclipse.emf.ecore.resource.Resource resource) throws java.io.IOException {
        java.util.Map<?, ?> options = java.util.Collections.EMPTY_MAP;
        java.io.OutputStream outputStream = new java.io.FileOutputStream(file);
        resource.save(outputStream, options);
        outputStream.close();
    }

    public static boolean containsErrors(org.eclipse.emf.ecore.resource.Resource resource) {
        return !resource.getErrors().isEmpty();
    }

    public static boolean containsWarnings(org.eclipse.emf.ecore.resource.Resource resource) {
        return !resource.getWarnings().isEmpty();
    }

    public static boolean containsProblems(org.eclipse.emf.ecore.resource.Resource resource) {
        return containsErrors(resource) || containsWarnings(resource);
    }
}

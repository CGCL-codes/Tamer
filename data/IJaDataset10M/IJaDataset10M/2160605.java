package org.emftext.language.models.resource.model.util;

public class ModelMinimalModelHelper {

    private static final org.emftext.language.models.resource.model.util.ModelEClassUtil eClassUtil = new org.emftext.language.models.resource.model.util.ModelEClassUtil();

    public org.eclipse.emf.ecore.EObject getMinimalModel(org.eclipse.emf.ecore.EClass eClass, java.util.Collection<org.eclipse.emf.ecore.EClass> allAvailableClasses) {
        return getMinimalModel(eClass, allAvailableClasses.toArray(new org.eclipse.emf.ecore.EClass[allAvailableClasses.size()]), null);
    }

    public org.eclipse.emf.ecore.EObject getMinimalModel(org.eclipse.emf.ecore.EClass eClass, org.eclipse.emf.ecore.EClass[] allAvailableClasses) {
        return getMinimalModel(eClass, allAvailableClasses, null);
    }

    public org.eclipse.emf.ecore.EObject getMinimalModel(org.eclipse.emf.ecore.EClass eClass, org.eclipse.emf.ecore.EClass[] allAvailableClasses, String name) {
        org.eclipse.emf.ecore.EPackage ePackage = eClass.getEPackage();
        if (ePackage == null) {
            return null;
        }
        org.eclipse.emf.ecore.EObject root = ePackage.getEFactoryInstance().create(eClass);
        java.util.List<org.eclipse.emf.ecore.EStructuralFeature> features = eClass.getEAllStructuralFeatures();
        for (org.eclipse.emf.ecore.EStructuralFeature feature : features) {
            if (feature instanceof org.eclipse.emf.ecore.EReference) {
                org.eclipse.emf.ecore.EReference reference = (org.eclipse.emf.ecore.EReference) feature;
                if (reference.isUnsettable()) {
                    continue;
                }
                if (!reference.isChangeable()) {
                    continue;
                }
                org.eclipse.emf.ecore.EClassifier type = reference.getEType();
                if (type instanceof org.eclipse.emf.ecore.EClass) {
                    org.eclipse.emf.ecore.EClass typeClass = (org.eclipse.emf.ecore.EClass) type;
                    if (eClassUtil.isNotConcrete(typeClass)) {
                        java.util.List<org.eclipse.emf.ecore.EClass> subClasses = eClassUtil.getSubClasses(typeClass, allAvailableClasses);
                        if (subClasses.size() == 0) {
                            continue;
                        } else {
                            typeClass = subClasses.get(0);
                        }
                    }
                    int lowerBound = reference.getLowerBound();
                    for (int i = 0; i < lowerBound; i++) {
                        org.eclipse.emf.ecore.EObject subModel = null;
                        if (reference.isContainment()) {
                            subModel = getMinimalModel(typeClass, allAvailableClasses);
                        } else {
                            subModel = typeClass.getEPackage().getEFactoryInstance().create(typeClass);
                            String initialValue = "#some" + org.emftext.language.models.resource.model.util.ModelStringUtil.capitalize(typeClass.getName());
                            org.eclipse.emf.common.util.URI proxyURI = org.eclipse.emf.common.util.URI.createURI(initialValue);
                            ((org.eclipse.emf.ecore.InternalEObject) subModel).eSetProxyURI(proxyURI);
                        }
                        if (subModel == null) {
                            continue;
                        }
                        java.lang.Object value = root.eGet(reference);
                        if (value instanceof java.util.List<?>) {
                            java.util.List<org.eclipse.emf.ecore.EObject> list = org.emftext.language.models.resource.model.util.ModelListUtil.castListUnchecked(value);
                            list.add(subModel);
                        } else {
                            root.eSet(reference, subModel);
                        }
                    }
                }
            } else if (feature instanceof org.eclipse.emf.ecore.EAttribute) {
                org.eclipse.emf.ecore.EAttribute attribute = (org.eclipse.emf.ecore.EAttribute) feature;
                if ("EString".equals(attribute.getEType().getName())) {
                    String initialValue;
                    if (attribute.getName().equals("name") && name != null) {
                        initialValue = name;
                    } else {
                        initialValue = "some" + org.emftext.language.models.resource.model.util.ModelStringUtil.capitalize(attribute.getName());
                    }
                    java.lang.Object value = root.eGet(attribute);
                    if (value instanceof java.util.List<?>) {
                        java.util.List<String> list = org.emftext.language.models.resource.model.util.ModelListUtil.castListUnchecked(value);
                        list.add(initialValue);
                    } else {
                        root.eSet(attribute, initialValue);
                    }
                }
            }
        }
        return root;
    }
}

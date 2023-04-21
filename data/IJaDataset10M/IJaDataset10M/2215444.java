package org.emftext.language.models.resource.model.mopp;

public class ModelCodeCompletionHelper {

    private static final org.emftext.language.models.resource.model.util.ModelEClassUtil eClassUtil = new org.emftext.language.models.resource.model.util.ModelEClassUtil();

    public java.util.Collection<String> computeCompletionProposals(org.emftext.language.models.resource.model.mopp.ModelMetaInformation metaInformation, String content, int cursorOffset) {
        java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(content.getBytes());
        org.emftext.language.models.resource.model.IModelTextParser parser = metaInformation.createParser(inputStream, null);
        final java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElements = parseToExpectedElements(parser);
        if (expectedElements == null) {
            return java.util.Collections.emptyList();
        }
        if (expectedElements.size() == 0) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElementsAt = getExpectedElements(expectedElements, cursorOffset);
        setPrefix(expectedElementsAt, content, cursorOffset);
        System.out.println(" PARSER returned expectation: " + expectedElementsAt + " for offset " + cursorOffset);
        java.util.Collection<String> proposals = deriveProposals(expectedElementsAt, content, metaInformation, cursorOffset);
        final java.util.List<String> sortedProposals = new java.util.ArrayList<String>(proposals);
        java.util.Collections.sort(sortedProposals);
        return sortedProposals;
    }

    public java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> parseToExpectedElements(org.emftext.language.models.resource.model.IModelTextParser parser) {
        final java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElements = parser.parseToExpectedElements(null);
        if (expectedElements == null) {
            return java.util.Collections.emptyList();
        }
        removeDuplicateEntries(expectedElements);
        removeInvalidEntriesAtEnd(expectedElements);
        for (org.emftext.language.models.resource.model.IModelExpectedElement expectedElement : expectedElements) {
            System.out.println("PARSER EXPECTS:   " + expectedElement);
        }
        return expectedElements;
    }

    private void removeDuplicateEntries(java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElements) {
        for (int i = 0; i < expectedElements.size() - 1; i++) {
            org.emftext.language.models.resource.model.IModelExpectedElement elementAtIndex = expectedElements.get(i);
            for (int j = i + 1; j < expectedElements.size(); ) {
                org.emftext.language.models.resource.model.IModelExpectedElement elementAtNext = expectedElements.get(j);
                if (elementAtIndex.equals(elementAtNext) && elementAtIndex.getStartExcludingHiddenTokens() == elementAtNext.getStartExcludingHiddenTokens()) {
                    expectedElements.remove(j);
                } else {
                    j++;
                }
            }
        }
    }

    private void removeInvalidEntriesAtEnd(java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElements) {
        for (int i = 0; i < expectedElements.size() - 1; ) {
            org.emftext.language.models.resource.model.IModelExpectedElement elementAtIndex = expectedElements.get(i);
            org.emftext.language.models.resource.model.IModelExpectedElement elementAtNext = expectedElements.get(i + 1);
            if (elementAtIndex.getStartExcludingHiddenTokens() == elementAtNext.getStartExcludingHiddenTokens() && shouldRemove(elementAtIndex.getScopeID(), elementAtNext.getScopeID())) {
                expectedElements.remove(i + 1);
            } else {
                i++;
            }
        }
    }

    private boolean shouldRemove(String scopeID1, String scopeID2) {
        String[] parts1 = scopeID1.split("\\.");
        String[] parts2 = scopeID2.split("\\.");
        for (int p1 = 0; p1 < parts1.length; p1++) {
            String segment1 = parts1[p1];
            if (p1 >= parts2.length) {
                return true;
            }
            String segment2 = parts2[p1];
            int compareTo = segment1.compareTo(segment2);
            if (compareTo == 0) {
                continue;
            }
        }
        return false;
    }

    private String findPrefix(java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElements, org.emftext.language.models.resource.model.IModelExpectedElement expectedAtCursor, String content, int cursorOffset) {
        if (cursorOffset < 0) {
            return "";
        }
        int end = 0;
        for (org.emftext.language.models.resource.model.IModelExpectedElement expectedElement : expectedElements) {
            if (expectedElement == expectedAtCursor) {
                final int start = expectedElement.getStartExcludingHiddenTokens();
                if (start >= 0 && start < Integer.MAX_VALUE) {
                    end = start;
                }
                break;
            }
        }
        end = Math.min(end, cursorOffset);
        final String prefix = content.substring(end, Math.min(content.length(), cursorOffset + 1));
        System.out.println("Found prefix '" + prefix + "'");
        return prefix;
    }

    private java.util.Collection<String> deriveProposals(java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElements, String content, org.emftext.language.models.resource.model.mopp.ModelMetaInformation metaInformation, int cursorOffset) {
        java.util.Collection<String> resultSet = new java.util.HashSet<String>();
        for (org.emftext.language.models.resource.model.IModelExpectedElement expectedElement : expectedElements) {
            resultSet.addAll(deriveProposals(expectedElement, content, metaInformation, cursorOffset));
        }
        return resultSet;
    }

    private java.util.Collection<String> deriveProposals(org.emftext.language.models.resource.model.IModelExpectedElement expectedElement, String content, org.emftext.language.models.resource.model.mopp.ModelMetaInformation metaInformation, int cursorOffset) {
        if (expectedElement instanceof org.emftext.language.models.resource.model.mopp.ModelExpectedCsString) {
            org.emftext.language.models.resource.model.mopp.ModelExpectedCsString csString = (org.emftext.language.models.resource.model.mopp.ModelExpectedCsString) expectedElement;
            return deriveProposal(csString, content, cursorOffset);
        } else if (expectedElement instanceof org.emftext.language.models.resource.model.mopp.ModelExpectedStructuralFeature) {
            org.emftext.language.models.resource.model.mopp.ModelExpectedStructuralFeature expectedFeature = (org.emftext.language.models.resource.model.mopp.ModelExpectedStructuralFeature) expectedElement;
            org.eclipse.emf.ecore.EStructuralFeature feature = expectedFeature.getFeature();
            org.eclipse.emf.ecore.EClassifier featureType = feature.getEType();
            org.eclipse.emf.ecore.EObject container = expectedFeature.getContainer();
            if (feature instanceof org.eclipse.emf.ecore.EReference) {
                org.eclipse.emf.ecore.EReference reference = (org.eclipse.emf.ecore.EReference) feature;
                if (featureType instanceof org.eclipse.emf.ecore.EClass) {
                    if (reference.isContainment()) {
                        org.eclipse.emf.ecore.EClass classType = (org.eclipse.emf.ecore.EClass) featureType;
                        return deriveProposals(classType, metaInformation, content, cursorOffset);
                    } else {
                        return handleNCReference(content, metaInformation, cursorOffset, container);
                    }
                }
            } else if (feature instanceof org.eclipse.emf.ecore.EAttribute) {
                org.eclipse.emf.ecore.EAttribute attribute = (org.eclipse.emf.ecore.EAttribute) feature;
                if (featureType instanceof org.eclipse.emf.ecore.EEnum) {
                    org.eclipse.emf.ecore.EEnum enumType = (org.eclipse.emf.ecore.EEnum) featureType;
                    return deriveProposals(expectedElement, enumType, content, cursorOffset);
                } else {
                    return handleAttribute(metaInformation, expectedFeature, container, attribute);
                }
            } else {
                assert false;
            }
        } else {
            assert false;
        }
        return java.util.Collections.emptyList();
    }

    private java.util.Collection<String> handleNCReference(String content, org.emftext.language.models.resource.model.mopp.ModelMetaInformation metaInformation, int cursorOffset, org.eclipse.emf.ecore.EObject container) {
        org.emftext.language.models.resource.model.IModelReferenceResolverSwitch resolverSwitch = metaInformation.getReferenceResolverSwitch();
        org.emftext.language.models.resource.model.IModelReferenceResolveResult<org.eclipse.emf.ecore.EObject> result = new org.emftext.language.models.resource.model.mopp.ModelReferenceResolveResult<org.eclipse.emf.ecore.EObject>(true);
        resolverSwitch.resolveFuzzy("", container, 0, result);
        java.util.Collection<org.emftext.language.models.resource.model.IModelReferenceMapping<org.eclipse.emf.ecore.EObject>> mappings = result.getMappings();
        if (mappings != null) {
            java.util.Collection<String> resultSet = new java.util.HashSet<String>();
            for (org.emftext.language.models.resource.model.IModelReferenceMapping<org.eclipse.emf.ecore.EObject> mapping : mappings) {
                final String identifier = mapping.getIdentifier();
                System.out.println("deriveProposals() " + identifier);
                resultSet.add(identifier);
            }
            return resultSet;
        }
        return java.util.Collections.emptyList();
    }

    private java.util.Collection<String> handleAttribute(org.emftext.language.models.resource.model.mopp.ModelMetaInformation metaInformation, org.emftext.language.models.resource.model.mopp.ModelExpectedStructuralFeature expectedFeature, org.eclipse.emf.ecore.EObject container, org.eclipse.emf.ecore.EAttribute attribute) {
        java.lang.Object defaultValue = getDefaultValue(attribute);
        if (defaultValue != null) {
            org.emftext.language.models.resource.model.IModelTokenResolverFactory tokenResolverFactory = metaInformation.getTokenResolverFactory();
            String tokenName = expectedFeature.getTokenName();
            if (tokenName != null) {
                org.emftext.language.models.resource.model.IModelTokenResolver tokenResolver = tokenResolverFactory.createTokenResolver(tokenName);
                if (tokenResolver != null) {
                    String defaultValueAsString = tokenResolver.deResolve(defaultValue, attribute, container);
                    java.util.Collection<String> resultSet = new java.util.HashSet<String>();
                    resultSet.add(defaultValueAsString);
                    return resultSet;
                }
            }
        }
        return java.util.Collections.emptyList();
    }

    private java.lang.Object getDefaultValue(org.eclipse.emf.ecore.EAttribute attribute) {
        String typeName = attribute.getEType().getName();
        if ("EString".equals(typeName)) {
            return "some" + org.emftext.language.models.resource.model.util.ModelStringUtil.capitalize(attribute.getName());
        }
        System.out.println("CodeCompletionHelper.getDefaultValue() unknown type " + typeName);
        return attribute.getDefaultValue();
    }

    private java.util.Collection<String> deriveProposals(org.eclipse.emf.ecore.EClass type, org.emftext.language.models.resource.model.mopp.ModelMetaInformation metaInformation, String content, int cursorOffset) {
        java.util.Collection<String> allProposals = new java.util.HashSet<String>();
        org.eclipse.emf.ecore.EClass[] availableClasses = metaInformation.getClassesWithSyntax();
        java.util.Collection<org.eclipse.emf.ecore.EClass> allSubClasses = eClassUtil.getSubClasses(type, availableClasses);
        for (org.eclipse.emf.ecore.EClass subClass : allSubClasses) {
            org.emftext.language.models.resource.model.IModelTextParser parser = metaInformation.createParser(new java.io.ByteArrayInputStream(new byte[0]), null);
            final java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElements = parser.parseToExpectedElements(subClass);
            if (expectedElements == null) {
                continue;
            }
            if (expectedElements.size() == 0) {
                continue;
            }
            java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedElementsAt = getExpectedElements(expectedElements, 0);
            setPrefix(expectedElementsAt, content, 0);
            System.out.println("computeCompletionProposals() " + expectedElementsAt + " for offset " + cursorOffset);
            java.util.Collection<String> proposals = deriveProposals(expectedElementsAt, content, metaInformation, cursorOffset);
            allProposals.addAll(proposals);
        }
        return allProposals;
    }

    private java.util.Collection<String> deriveProposals(org.emftext.language.models.resource.model.IModelExpectedElement expectedElement, org.eclipse.emf.ecore.EEnum enumType, String content, int cursorOffset) {
        java.util.Collection<org.eclipse.emf.ecore.EEnumLiteral> enumLiterals = enumType.getELiterals();
        java.util.Collection<String> result = new java.util.HashSet<String>();
        for (org.eclipse.emf.ecore.EEnumLiteral literal : enumLiterals) {
            String proposal = literal.getLiteral();
            if (proposal.startsWith(expectedElement.getPrefix())) {
                result.add(proposal);
            }
        }
        return result;
    }

    private java.util.Collection<String> deriveProposal(org.emftext.language.models.resource.model.mopp.ModelExpectedCsString csString, String content, int cursorOffset) {
        String proposal = csString.getValue();
        java.util.Collection<String> result = new java.util.HashSet<String>(1);
        result.add(proposal);
        return result;
    }

    public java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> getExpectedElements(final java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> allExpectedElements, int cursorOffset) {
        java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedAfterCursor = getElementsExpectedAt(allExpectedElements, cursorOffset);
        java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedBeforeCursor = getElementsExpectedAt(allExpectedElements, cursorOffset - 1);
        System.out.println("parseToCursor(" + cursorOffset + ") BEFORE CURSOR " + expectedBeforeCursor);
        System.out.println("parseToCursor(" + cursorOffset + ") AFTER CURSOR  " + expectedAfterCursor);
        java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> allExpectedAtCursor = new java.util.ArrayList<org.emftext.language.models.resource.model.IModelExpectedElement>();
        allExpectedAtCursor.addAll(expectedAfterCursor);
        if (expectedBeforeCursor != null) {
            for (org.emftext.language.models.resource.model.IModelExpectedElement expectedBefore : expectedBeforeCursor) {
                if (expectedBefore instanceof org.emftext.language.models.resource.model.mopp.ModelExpectedStructuralFeature) {
                    allExpectedAtCursor.add(expectedBefore);
                }
            }
        }
        return allExpectedAtCursor;
    }

    private void setPrefix(java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> allExpectedElements, String content, int cursorOffset) {
        if (cursorOffset < 0) {
            return;
        }
        for (org.emftext.language.models.resource.model.IModelExpectedElement expectedElementAtCursor : allExpectedElements) {
            expectedElementAtCursor.setPrefix(findPrefix(allExpectedElements, expectedElementAtCursor, content, cursorOffset));
        }
    }

    public java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> getElementsExpectedAt(java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> allExpectedElements, int cursorOffset) {
        java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> expectedAtCursor = new java.util.ArrayList<org.emftext.language.models.resource.model.IModelExpectedElement>();
        for (int i = 0; i < allExpectedElements.size(); i++) {
            org.emftext.language.models.resource.model.IModelExpectedElement expectedElement = allExpectedElements.get(i);
            int startIncludingHidden = expectedElement.getStartIncludingHiddenTokens();
            int end = getEnd(allExpectedElements, i);
            if (cursorOffset >= startIncludingHidden && cursorOffset <= end) {
                expectedAtCursor.add(expectedElement);
            }
        }
        return expectedAtCursor;
    }

    private int getEnd(java.util.List<org.emftext.language.models.resource.model.IModelExpectedElement> allExpectedElements, int indexInList) {
        org.emftext.language.models.resource.model.IModelExpectedElement elementAtIndex = allExpectedElements.get(indexInList);
        int startIncludingHidden = elementAtIndex.getStartIncludingHiddenTokens();
        int startExcludingHidden = elementAtIndex.getStartExcludingHiddenTokens();
        for (int i = indexInList + 1; i < allExpectedElements.size(); i++) {
            org.emftext.language.models.resource.model.IModelExpectedElement elementAtI = allExpectedElements.get(i);
            int startIncludingHiddenForI = elementAtI.getStartIncludingHiddenTokens();
            int startExcludingHiddenForI = elementAtI.getStartExcludingHiddenTokens();
            if (startIncludingHidden != startIncludingHiddenForI || startExcludingHidden != startExcludingHiddenForI) {
                return startIncludingHiddenForI - 1;
            }
        }
        return Integer.MAX_VALUE;
    }
}

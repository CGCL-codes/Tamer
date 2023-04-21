package org.emftext.language.OCL.resource.OCL.mopp;

public class OCLMetaInformation implements org.emftext.language.OCL.resource.OCL.IOCLMetaInformation {

    public String getSyntaxName() {
        return "OCL";
    }

    public String getURI() {
        return "http://www.emftext.org/language/OCL";
    }

    public org.emftext.language.OCL.resource.OCL.IOCLTextScanner createLexer() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLAntlrScanner(new org.emftext.language.OCL.resource.OCL.mopp.OCLLexer());
    }

    public org.emftext.language.OCL.resource.OCL.IOCLTextParser createParser(java.io.InputStream inputStream, String encoding) {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLParser().createInstance(inputStream, encoding);
    }

    public org.emftext.language.OCL.resource.OCL.IOCLTextPrinter createPrinter(java.io.OutputStream outputStream, org.emftext.language.OCL.resource.OCL.IOCLTextResource resource) {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLPrinter2(outputStream, resource);
    }

    public org.eclipse.emf.ecore.EClass[] getClassesWithSyntax() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLSyntaxCoverageInformationProvider().getClassesWithSyntax();
    }

    public org.eclipse.emf.ecore.EClass[] getStartSymbols() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLSyntaxCoverageInformationProvider().getStartSymbols();
    }

    public org.emftext.language.OCL.resource.OCL.IOCLReferenceResolverSwitch getReferenceResolverSwitch() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLReferenceResolverSwitch();
    }

    public org.emftext.language.OCL.resource.OCL.IOCLTokenResolverFactory getTokenResolverFactory() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLTokenResolverFactory();
    }

    public String getPathToCSDefinition() {
        return "org.emftext.language.OCL/metamodel/OCL.cs";
    }

    public String[] getTokenNames() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLParser(null).getTokenNames();
    }

    public org.emftext.language.OCL.resource.OCL.IOCLTokenStyle getDefaultTokenStyle(String tokenName) {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLTokenStyleInformationProvider().getDefaultTokenStyle(tokenName);
    }

    public java.util.Collection<org.emftext.language.OCL.resource.OCL.IOCLBracketPair> getBracketPairs() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLBracketInformationProvider().getBracketPairs();
    }

    public org.eclipse.emf.ecore.EClass[] getFoldableClasses() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLFoldingInformationProvider().getFoldableClasses();
    }

    public org.eclipse.emf.ecore.resource.Resource.Factory createResourceFactory() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLResourceFactory();
    }

    public org.emftext.language.OCL.resource.OCL.mopp.OCLNewFileContentProvider getNewFileContentProvider() {
        return new org.emftext.language.OCL.resource.OCL.mopp.OCLNewFileContentProvider();
    }

    public void registerResourceFactory() {
        org.eclipse.emf.ecore.resource.Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(getSyntaxName(), new org.emftext.language.OCL.resource.OCL.mopp.OCLResourceFactory());
    }

    /**
	 * Returns the key of the option that can be used to register a preprocessor that
	 * is used as a pipe when loading resources. This key is language-specific. To
	 * register one preprocessor for multiple resource types, it must be registered
	 * individually using all keys.
	 */
    public String getInputStreamPreprocessorProviderOptionKey() {
        return getSyntaxName() + "_" + "INPUT_STREAM_PREPROCESSOR_PROVIDER";
    }

    /**
	 * Returns the key of the option that can be used to register a post-processors
	 * that are invoked after loading resources. This key is language-specific. To
	 * register one post-processor for multiple resource types, it must be registered
	 * individually using all keys.
	 */
    public String getResourcePostProcessorProviderOptionKey() {
        return getSyntaxName() + "_" + "RESOURCE_POSTPROCESSOR_PROVIDER";
    }
}

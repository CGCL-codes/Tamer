package org.emftext.language.parametercheck.resource.pcheck.mopp;

public class PcheckTokenResolverFactory implements org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolverFactory {

    private java.util.Map<java.lang.String, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver> tokenName2TokenResolver;

    private java.util.Map<java.lang.String, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver> featureName2CollectInTokenResolver;

    private static org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver defaultResolver = new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckDefaultTokenResolver();

    public PcheckTokenResolverFactory() {
        tokenName2TokenResolver = new java.util.HashMap<java.lang.String, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver>();
        featureName2CollectInTokenResolver = new java.util.HashMap<java.lang.String, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver>();
        registerTokenResolver("EXCLAM", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckEXCLAMTokenResolver());
        registerTokenResolver("NAME_TYPE", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckNAME_TYPETokenResolver());
        registerTokenResolver("BOOLEAN_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckBOOLEAN_LITERALTokenResolver());
        registerCollectInTokenResolver("literals", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckCOLLECT_literalsTokenResolver());
        registerTokenResolver("STRING_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckSTRING_LITERALTokenResolver());
        registerTokenResolver("HEX_FLOAT_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckHEX_FLOAT_LITERALTokenResolver());
        registerTokenResolver("HEX_DOUBLE_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckHEX_DOUBLE_LITERALTokenResolver());
        registerTokenResolver("HEX_LONG_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckHEX_LONG_LITERALTokenResolver());
        registerTokenResolver("HEX_INTEGER_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckHEX_INTEGER_LITERALTokenResolver());
        registerTokenResolver("DECIMAL_FLOAT_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckDECIMAL_FLOAT_LITERALTokenResolver());
        registerTokenResolver("DECIMAL_DOUBLE_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckDECIMAL_DOUBLE_LITERALTokenResolver());
        registerTokenResolver("DECIMAL_LONG_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckDECIMAL_LONG_LITERALTokenResolver());
        registerTokenResolver("DECIMAL_INTEGER_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckDECIMAL_INTEGER_LITERALTokenResolver());
        registerTokenResolver("OCTAL_LONG_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckOCTAL_LONG_LITERALTokenResolver());
        registerTokenResolver("OCTAL_INTEGER_LITERAL", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckOCTAL_INTEGER_LITERALTokenResolver());
        registerTokenResolver("IDENTIFIER", new org.emftext.language.parametercheck.resource.pcheck.analysis.PcheckIDENTIFIERTokenResolver());
    }

    public org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver createTokenResolver(java.lang.String tokenName) {
        return internalCreateResolver(tokenName2TokenResolver, tokenName);
    }

    public org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver createCollectInTokenResolver(java.lang.String featureName) {
        return internalCreateResolver(featureName2CollectInTokenResolver, featureName);
    }

    protected boolean registerTokenResolver(java.lang.String tokenName, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver resolver) {
        return internalRegisterTokenResolver(tokenName2TokenResolver, tokenName, resolver);
    }

    protected boolean registerCollectInTokenResolver(java.lang.String featureName, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver resolver) {
        return internalRegisterTokenResolver(featureName2CollectInTokenResolver, featureName, resolver);
    }

    protected org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver deRegisterTokenResolver(java.lang.String tokenName) {
        return tokenName2TokenResolver.remove(tokenName);
    }

    private org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver internalCreateResolver(java.util.Map<java.lang.String, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver> resolverMap, String key) {
        if (resolverMap.containsKey(key)) {
            return resolverMap.get(key);
        } else {
            return defaultResolver;
        }
    }

    private boolean internalRegisterTokenResolver(java.util.Map<java.lang.String, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver> resolverMap, java.lang.String key, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolver resolver) {
        if (!resolverMap.containsKey(key)) {
            resolverMap.put(key, resolver);
            return true;
        }
        return false;
    }
}

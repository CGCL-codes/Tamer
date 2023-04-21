package org.makumba.guice;

import com.google.inject.Inject;

/**
 * Example client code that needs a MetaDataProvider. We demonstrate usage of
 * the {@link OldFashionedMetaDataProvider} which uses static calls (or could be
 * a singleton, using getInstance()). We then demonstrate an intermediate
 * approach using static injection provided by Google Guice
 * 
 * @author manu
 * 
 */
public class SomeQueryAnalysis {

    @Inject
    public static NonStaticMetaDataProvider metaDataProvider;

    public void doHeavyAnalysisWorkWithStaticCall(String type) {
        System.out.println("SomeQueryAnalysis.doHeavyAnalysisWorkWithStaticCall(): Doing heavy analysis work on type '" + type + "'");
        String someVitalInformation = OldFashionedMetaDataProvider.getVitalMetaData(type);
        System.out.println("=== Computed analysis information: " + someVitalInformation);
    }

    public void doHeavyAnalysisWorkWithoutStaticCall(String type) {
        System.out.println("SomeQueryAnalysis.doHeavyAnalysisWorkWithoutStaticCall(): Doing heavy analysis work on type '" + type + "'");
        System.out.println("=== Computed analysis information: " + metaDataProvider.getVitalMetaData(type));
    }
}

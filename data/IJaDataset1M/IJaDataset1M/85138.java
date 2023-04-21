package br.ufal.npd.fox.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import br.ufal.npd.fox.commons.FoxObjectCreator;
import br.ufal.npd.fox.commons.lang.FoxAnalyzerException;
import br.ufal.npd.fox.commons.lang.FoxObjectCreaterException;

/**
 * @author �dle M�rcio (edlemarcio@gmail.com) 31/07/2006
 * @version 1.0.0
 * 
 * F�brica de Analyzer. A Fa�brica recebe uma classe que implementa FoxAnalyzerIF
 * e retorna uma classe org.apache.lucene.analysis.Analyzer.
 * 
 */
public final class FoxLuceneAnalyzerFactory {

    /**
         * @param className -
         *                Nome da classe filha do Analyzer a ser usada na busca
         *                e na indexa��o.
         * @return Retorna o Analizador textuao escolhido.
     * @throws FoxAnalyzerException 
         */
    public static Analyzer getAnalyzer(String className) throws FoxAnalyzerException {
        try {
            return (Analyzer) (new FoxObjectCreator(className)).createObject();
        } catch (FoxObjectCreaterException e) {
            throw new FoxAnalyzerException(FoxAnalyzerException.NO_CLASS_ANALYZER_FOUND, e);
        }
    }
}

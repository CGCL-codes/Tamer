package org.componentbasedtesting.accessibilityView.objectRecognition;

/**
 * Valutatore dei criteri di riconoscimento di un insieme. Rappresenta una funzione di valutazione
 * di un insieme di criteri di riconoscimento utilizzati per il riconoscimento di uno specifico oggetto.
 * Fornisce una misura che indica quanto i criteri dell'insieme sono soddisfatti.
 * @author Giacomo Perreca 
 */
public abstract class RecognitionEvaluator {

    protected RecognitionCriteriaSet rcs;

    /**
	 * Costruisce un valutatore di uno specifico insieme di criteri di riconoscimento.
	 * @param rcs Insieme dei criteri di riconoscimento da valutare.
	 */
    public RecognitionEvaluator(RecognitionCriteriaSet rcs) {
        super();
        if (rcs == null) throw new IllegalArgumentException("null recognition criteria set (rcs)");
        this.rcs = rcs;
        return;
    }

    /**
	 * Fornisce la valutazione dell'insieme dei criteri di valutazione.
	 * Un vaalutatore concreto deve implementare questo metodo.
	 * @return valore della valutazione.
	 */
    public abstract float evaluationScore();
}

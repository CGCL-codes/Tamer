package se.kth.cid.conzilla.edit;

/** When you want to be informed what happens with a certain draft.
 *
 *  @see NeuronDraft
 */
public interface NeuronDraftListener {

    void madeNeuron(NeuronDraft nd);

    void neuronCanceled(NeuronDraft nd);
}

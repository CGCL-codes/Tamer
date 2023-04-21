package ca.petersens.gwt.databinding.client;

public interface BoundField<B, P> extends BoundFormula<B, P> {

    Editor<P> getEditor();

    void refreshBean();

    void setValue(P value);
}

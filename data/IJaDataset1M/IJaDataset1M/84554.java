package edu.asu.vogon.editor.selection;

import java.util.List;
import org.eclipse.jface.viewers.StructuredSelection;
import edu.asu.vogon.digitalHPS.IText;

public class WordsStructuredSelection extends StructuredSelection {

    public WordsStructuredSelection(IText text) {
        super(text);
    }

    private List<SelectedTerm> selectedTerms;

    private String selectedTermText;

    private int position;

    private IText text;

    public void setSelectedTerms(List<SelectedTerm> terms) {
        this.selectedTerms = terms;
    }

    public List<SelectedTerm> getSelectedTerms() {
        return selectedTerms;
    }

    public String getSelectedTermText() {
        return selectedTermText;
    }

    public void setSelectedTermText(String selectedTermText) {
        this.selectedTermText = selectedTermText;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public IText getText() {
        return text;
    }

    public void setText(IText text) {
        this.text = text;
    }
}

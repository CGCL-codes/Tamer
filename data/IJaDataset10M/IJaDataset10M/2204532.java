package org.pocui.swing.example.composites.stammdaten.anzeigen.vertragsdaten;

import java.util.List;
import org.pocui.core.composites.ISelection;

/**
 * Selection for {@link VisualisiereVertragsdatenComposite}
 * 
 * @author Kai Benjamin Joneleit
 * @author Jan Christian Krause
 * 
 */
public class VisualisiereVertragsdatenSelectionInOut implements ISelection {

    private Long vertragsbeginn;

    private Long vertragsende;

    private String selektiertesProdukt;

    private List<String> produkte;

    public Long getVertragsbeginn() {
        return vertragsbeginn;
    }

    public void setVertragsbeginn(Long vertragsbeginn) {
        this.vertragsbeginn = vertragsbeginn;
    }

    public Long getVertragsende() {
        return vertragsende;
    }

    public void setVertragsende(Long vertragsende) {
        this.vertragsende = vertragsende;
    }

    public String getSelektiertesProdukt() {
        return selektiertesProdukt;
    }

    public void setSelektiertesProdukt(String selektiertesProdukt) {
        this.selektiertesProdukt = selektiertesProdukt;
    }

    public List<String> getProdukte() {
        return produkte;
    }

    public void setProdukte(List<String> produkte) {
        this.produkte = produkte;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((produkte == null) ? 0 : produkte.hashCode());
        result = prime * result + ((selektiertesProdukt == null) ? 0 : selektiertesProdukt.hashCode());
        result = prime * result + ((vertragsbeginn == null) ? 0 : vertragsbeginn.hashCode());
        result = prime * result + ((vertragsende == null) ? 0 : vertragsende.hashCode());
        return result;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VisualisiereVertragsdatenSelectionInOut other = (VisualisiereVertragsdatenSelectionInOut) obj;
        if (produkte == null) {
            if (other.produkte != null) return false;
        } else if (!produkte.equals(other.produkte)) return false;
        if (selektiertesProdukt == null) {
            if (other.selektiertesProdukt != null) return false;
        } else if (!selektiertesProdukt.equals(other.selektiertesProdukt)) return false;
        if (vertragsbeginn == null) {
            if (other.vertragsbeginn != null) return false;
        } else if (!vertragsbeginn.equals(other.vertragsbeginn)) return false;
        if (vertragsende == null) {
            if (other.vertragsende != null) return false;
        } else if (!vertragsende.equals(other.vertragsende)) return false;
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName());
        buffer.append(" [Vertragsbeginn: ");
        buffer.append(vertragsbeginn);
        buffer.append(", Vertragsende: ");
        buffer.append(vertragsende);
        buffer.append(", selektiertes Produkt: ");
        buffer.append(selektiertesProdukt);
        buffer.append(", Produkte: ");
        buffer.append(produkte);
        buffer.append("]");
        return buffer.toString();
    }
}

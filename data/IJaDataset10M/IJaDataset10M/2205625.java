package it.cilea.osd.jdyna.widget;

import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.value.TextValue;
import java.beans.PropertyEditor;
import java.util.LinkedList;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

/**
 * WidgetFormula e' una tipologia di rendering che mantiene al suo interno
 * un'espressione e una regola di ricalcolo per decidere se l'espressione
 * deve essere valutata in base a dei parametri.
 * @see FormulaManager, ProprietaDaRicalcolare
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Table(name = "dyna_widget_formula_testo")
@NamedQueries({ @NamedQuery(name = "WidgetFormulaTesto.findAll", query = "from WidgetFormulaTesto order by id"), @NamedQuery(name = "WidgetFormulaTesto.findVariabiliByWidgetFormula", query = "select variabili from WidgetFormulaTesto formula join formula.variabili variabili where formula.id = ?") })
public class WidgetFormulaTesto extends WidgetFormula {

    /** grandezza dell'area di testo, se textbox e' il numero max di lunghezza, se textarea 
	 *  e' il numero di righe.
	 */
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "row", column = @Column(name = "box_testo_row")), @AttributeOverride(name = "col", column = @Column(name = "box_testo_col")) })
    private Size dimensione;

    public WidgetFormulaTesto() {
        variabili = new LinkedList<String>();
        this.dimensione = new Size();
    }

    @Override
    public String getResultTriview() {
        return "testo";
    }

    @Override
    public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {
        return new StringTrimmerEditor(true);
    }

    @Override
    public AValue getInstanceValore() {
        return new TextValue();
    }

    @Override
    public Class<? extends AValue> getValoreClass() {
        return TextValue.class;
    }

    public Size getDimensione() {
        return dimensione;
    }

    public void setDimensione(Size dimensione) {
        this.dimensione = dimensione;
    }
}

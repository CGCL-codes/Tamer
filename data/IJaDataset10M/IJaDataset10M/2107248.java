package br.com.jnfe.core.filter.classic;

import org.helianto.core.criteria.OrmCriteriaBuilder;
import org.helianto.document.filter.classic.SerializerFilter;
import br.com.jnfe.core.Emitente;

/**
 * Filtro de s�ries.
 * 
 * @author Mauricio Fernandes de Castro
 * @deprecated see JNFeSerieFilterAdapter
 */
public class SerieFilter extends SerializerFilter {

    private static final long serialVersionUID = 1L;

    private Emitente emitente;

    private String serieModelo;

    private int serieNumero;

    /**
	 * Default constructor.
	 */
    public SerieFilter() {
        super();
        setSerieModelo("");
        setSerieNumero(0);
    }

    /**
	 * Secondary constructor.
	 */
    public SerieFilter(Emitente emitente, String serieModelo, int serieNumero) {
        super();
        setEntity(emitente.getEntity());
        setEmitente(emitente);
        setSerieModelo(serieModelo);
        setSerieNumero(serieNumero);
    }

    /**
	 * Filtro de emitente.
	 */
    public Emitente getEmitente() {
        return emitente;
    }

    public void setEmitente(Emitente emitente) {
        this.emitente = emitente;
    }

    /**
	 * Filtro de modelo.
	 */
    public String getSerieModelo() {
        return serieModelo;
    }

    public void setSerieModelo(String serieModelo) {
        this.serieModelo = serieModelo;
    }

    /**
	 * Filtro de n�mero.
	 */
    public int getSerieNumero() {
        return serieNumero;
    }

    public void setSerieNumero(int serieNumero) {
        this.serieNumero = serieNumero;
    }

    @Override
    public void doFilter(OrmCriteriaBuilder mainCriteriaBuilder) {
        super.doFilter(mainCriteriaBuilder);
        if (getEmitente() != null) {
            appendEqualFilter("emitente", getEmitente().getId(), mainCriteriaBuilder);
        }
        appendEqualFilter("serieModelo", getSerieModelo(), mainCriteriaBuilder);
        appendEqualFilter("serieNumero", getSerieNumero(), mainCriteriaBuilder);
    }

    @Override
    public void reset() {
        super.reset();
        setSerieModelo("");
        setSerieNumero(0);
    }
}

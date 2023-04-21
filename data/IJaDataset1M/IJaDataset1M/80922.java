package com.mci.consolidacion.business;

import java.util.HashSet;
import com.mci.consolidacion.dtos.DisipuloTO;
import com.mci.consolidacion.dtos.EstudioTO;
import com.mci.consolidacion.dtos.LiderTO;

/**
 * @hibernate.subclass table="CON_PERSONA" discriminator-value="L"
 * 
 * @modelguid {B2C3DCBF-A303-41B0-9CED-A0BECA8818A7}
 */
public class Lider extends Disipulo {

    private Estudio estudio = new Estudio();

    private Ministerio ministerio = new Ministerio();

    public Lider() {
        setTipo(getLiderType());
    }

    /** @modelguid {88182D4E-DA97-424D-8A47-870B9507AEAE} */
    private java.util.Set disipulos = new HashSet();

    /**
	 * @hibernate.set
	 * @hibernate.collection-key column="LIDER_ID"
	 * @hibernate.collection-one-to-many class="com.mci.consolidacion.business.Disipulo"
	 */
    public java.util.Set getDisipulos() {
        return disipulos;
    }

    /** @modelguid {F6B9A2AB-FE01-49AF-BD0B-70A9A84BDD64} */
    public void setDisipulos(java.util.Set aDisipulos) {
        disipulos = aDisipulos;
    }

    /**
	 * 
	 * @hibernate.many-to-one column="ESTUDIO_ID"
	 *                        class="com.mci.consolidacion.business.Estudio"
	 *                        cascade="all"
	 */
    public Estudio getEstudio() {
        return estudio;
    }

    public void setEstudio(Estudio estudio) {
        this.estudio = estudio;
    }

    /**
	 * 
	 * @hibernate.many-to-one column="MINISTERIO_ID"
	 *                        class="com.mci.consolidacion.business.Ministerio"
	 *                        cascade="all"
	 * 
	 */
    public Ministerio getMinisterio() {
        return ministerio;
    }

    public void setMinisterio(Ministerio ministerio) {
        this.ministerio = ministerio;
    }

    public LiderTO getLiderTO() {
        LiderTO liderTO = new LiderTO(getDisipuloTO(), null, null);
        if (getEstudio() != null) {
            liderTO.setEstudio(getEstudio().getEstudioTO());
        }
        if (getMinisterio() != null) {
            liderTO.setMinisterio(getMinisterio().getMinisterioTO());
        }
        return liderTO;
    }

    public void inicializarHojaVida() {
        estudio = new Estudio();
        ministerio = new Ministerio();
    }

    public Lider(LiderTO liderTO) {
        super(liderTO);
        setTipo(getLiderType());
        estudio = new Estudio(liderTO.getEstudio());
        ministerio = new Ministerio(liderTO.getMinisterio());
    }
}

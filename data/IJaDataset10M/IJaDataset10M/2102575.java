package com.amidasoft.lincat.session;

import com.amidasoft.lincat.entity.Agenda;
import com.amidasoft.lincat.entity.Llocs;

/**
 *
 * @author ricard
 */
public interface AgendaAfegeixEsdeveniment {

    public void destrueix();

    public void inicialitza();

    public void afegeixEsdeveniment();

    public void setDadesAgenda(Agenda dadesAgenda);

    public Agenda getDadesAgenda();

    public Llocs getLloc();

    public String torna();

    public Llocs getLlocNou();

    public void setLlocNou(Llocs llocNou);

    public void afegeixLlocALlista();
}

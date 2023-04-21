package ar.com.larreta.grilla.client;

import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

public abstract class BotonModificarPresionado extends SelectionListener<MenuEvent> {

    protected SectorConGrilla sector;

    public BotonModificarPresionado(SectorConGrilla sector) {
        this.sector = sector;
    }
}

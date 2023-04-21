package com.proyecto.tropero.gui.interfaces;

import java.util.List;
import org.hibernate.criterion.Criterion;
import com.proyecto.tropero.core.domain.Animal;
import com.proyecto.tropero.core.domain.Dieta;
import com.proyecto.tropero.core.domain.GrupoAnimal;
import com.proyecto.tropero.core.domain.Ubicacion;

public interface IVentanaBusquedaAnimal {

    public void setListAnimales(List<Animal> listAnimales);

    public String getCadenaABuscar();

    public void setListUbicaciones(List<Ubicacion> listUbicaciones);

    public void setListGruposAnimales(List<GrupoAnimal> listGruposAnimales);

    public Dieta getUnaDietaSeleccionada();

    public void setListDietas(List<Dieta> listDietas);

    public Ubicacion getUnaUbicacionSeleccionada();

    public Criterion getFiltro();

    public Criterion getFiltroIn();

    public void setFiltro(Criterion criterion);

    public void setListAnimalesDentro(List<Animal> listGanados);
}

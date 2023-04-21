package com.proyecto.tropero.gui.interfaces;

import java.util.List;
import com.proyecto.tropero.core.domain.Animal;

public interface IVentanaModificarGanado {

    public void setListAnimales(List<Animal> listAnimales);

    public List<Animal> getListAnimales();

    public String getCadenaABuscar();
}

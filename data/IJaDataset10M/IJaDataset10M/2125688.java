package com.mdiss.bolsatrabajo.persistencia;

import java.util.List;
import com.mdiss.bolsatrabajo.model.Caracteristica;

public interface CaracteristicaDAO {

    public List<Caracteristica> buscarTodo();

    public void insertar(Caracteristica caracteristica);

    public void eliminar(Integer id);

    public Caracteristica buscar(Integer id);

    public List buscarPorOferta(Integer id);
}

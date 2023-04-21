package org.hmaciel.descop.ejb.controladores;

import java.util.List;
import javax.ejb.Local;
import org.hmaciel.descop.otros.Sala;

@Local
public interface IAltaSalaCU {

    public boolean existeSala(String nombre);

    public List<Sala> listarSalas();

    public Sala findSala(String nombre);

    public void guardarSala(Sala sal);

    public void eliminarSala(String nombre);

    public void modificarSala(Sala sal);
}

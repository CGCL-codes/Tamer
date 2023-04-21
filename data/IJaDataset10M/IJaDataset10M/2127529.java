package Persistencia.Entidades;

import java.util.Date;

/**
 *
 * @author diego
 */
public interface TrabajadorRol extends SuperDruperInterfaz {

    public Date getfechafinrol();

    public Date getfechainiciorol();

    public void setfechafinrol(Date newVal);

    public void setfechainiciorol(Date newVal);

    public Rol getRol();

    public void setRol(Rol rol);

    public Trabajador getTrabajador();

    public void setTrabajador(Trabajador trabajador);
}

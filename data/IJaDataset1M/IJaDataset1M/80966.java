package modelo;

import java.util.ArrayList;
import modelo.THorario.TRangoHorario;
import datos.TElemento;
import enumerados.ECategoriaEmpleado;
import enumerados.EDia;
import enumerados.EEstadoEmpleado;
import enumerados.ETipoElemento;

/**
 * Representa a un empleado de la empresa de servicios de cadetar�a. Continene
 * toda la informaci�n requerida por la empresa para determinar su
 * disponibilidad y rangos de horarios de trabajo. El atributo legajo es el que
 * identifica a un empleado particular de otro, no su DNI. Debido a las
 * limitaciones del modelo, pueden existir dos empleados con el mismo DNI, pero
 * no con el mismo legajo. Son manejados por el usuario administrativo.
 */
public class TEmpleado extends TElemento {

    private static final long serialVersionUID = 1L;

    private long legajo;

    private long dni;

    private String nombre;

    private String direccion;

    private String telefono;

    private THorario horario;

    private ECategoriaEmpleado categoria;

    private EEstadoEmpleado estado;

    private int id_empleado;

    public TEmpleado(long leg, long dnie, String nom, String dir, String tel, ECategoriaEmpleado cat, EEstadoEmpleado est, int id) {
        super(ETipoElemento.EMPLEADO);
        legajo = leg;
        dni = dnie;
        nombre = nom;
        direccion = dir;
        telefono = tel;
        horario = new THorario();
        categoria = cat;
        estado = est;
        id_empleado = id;
    }

    public void asignar_horario(EDia Dia, int HI, int HF) {
        horario.agregar_rango_horario(Dia, HI, HF);
    }

    public void eliminar_horario(EDia Dia, int HI, int HF) {
        horario.quitar_rango_horario(Dia, HI, HF);
    }

    public boolean estado_disp() {
        if ((estado == EEstadoEmpleado.DISPONIBLE) || (estado == EEstadoEmpleado.TRABAJANDO)) {
            return true;
        } else {
            return false;
        }
    }

    public ECategoriaEmpleado getCategoria() {
        return categoria;
    }

    public String getDireccion() {
        return direccion;
    }

    public long getDni() {
        return dni;
    }

    @Override
    public Object getEID() {
        return legajo;
    }

    public EEstadoEmpleado getEstado() {
        return estado;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public long getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public THorario getRangosHorarios() {
        return horario.clonar();
    }

    public ArrayList<TRangoHorario> getRangosHorarios(EDia Dia) {
        return horario.get_rangos_dia(Dia);
    }

    public String getTelefono() {
        return telefono;
    }

    public void setCategoria(ECategoriaEmpleado categoria) {
        this.categoria = categoria;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setDni(long dnie) {
        this.dni = dnie;
    }

    public void setEstado(EEstadoEmpleado estado) {
        this.estado = estado;
    }

    public void setHorario(THorario H) {
        assert H != null;
        horario = H;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean trabaja_en_rango(EDia Dia, int HI, int HF) {
        return horario.trabaja_en_rango(Dia, HI, HF);
    }
}

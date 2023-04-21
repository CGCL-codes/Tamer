package modelo;

import java.util.ArrayList;
import java.util.Date;
import datos.TElemento;
import enumerados.ECategoriaPedido;
import enumerados.EEstadoPedido;
import enumerados.ETipoElemento;

/**
 * Representa los registros de los servicios brindados por la empresa de
 * cadeter�a a los clientes de �sta. Los pedidos cuentan con un id �nico,
 * lugares de origen y destino, fechas y horas de inicio y fin, costo del
 * servicio prestado, categor�a de servicio prestado y el estado actual del
 * pedido. Adem�s se indican el cliente que realiz� el pedido, y las listas de
 * empleados y veh�culos asignados.
 * 
 * Los pedidos no se eliminan del sistema, simplemente se les modifica el estado
 * a completado o cancelado.
 */
public class TPedido extends TElemento {

    private static final long serialVersionUID = 1L;

    private int id_pedido;

    private String origen;

    private String destino;

    private EEstadoPedido estado;

    private ECategoriaPedido categoria;

    private Date ini;

    private Date fin;

    private float costo;

    private ArrayList<TEmpleado> empleados;

    private TCliente cliente;

    private ArrayList<TVehiculo> vehiculos;

    public TPedido(int id, String O, String D, EEstadoPedido E, ECategoriaPedido cat, Date I, Date F, TCliente C, float cos) {
        super(ETipoElemento.PEDIDO);
        assert (I.before(F) || I.equals(F));
        id_pedido = id;
        origen = O;
        destino = D;
        estado = E;
        categoria = cat;
        ini = I;
        fin = F;
        cliente = C;
        costo = cos;
        empleados = new ArrayList<TEmpleado>();
        vehiculos = new ArrayList<TVehiculo>();
    }

    public Date get_fin() {
        return fin;
    }

    public Date get_ini() {
        return ini;
    }

    public ECategoriaPedido getCategoria() {
        return categoria;
    }

    public TCliente getCliente() {
        return cliente;
    }

    public float getCosto() {
        return costo;
    }

    public String getDestino() {
        return destino;
    }

    @Override
    public Object getEID() {
        return id_pedido;
    }

    public ArrayList<TEmpleado> getEmpleados() {
        return empleados;
    }

    public EEstadoPedido getEstado() {
        return estado;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    @SuppressWarnings("unchecked")
    public ArrayList getLista_Tipo(ETipoElemento T) {
        switch(T) {
            case EMPLEADO:
                return getEmpleados();
            case VEHICULO:
                return getVehiculos();
        }
        return null;
    }

    public String getOrigen() {
        return origen;
    }

    public ArrayList<TVehiculo> getVehiculos() {
        return vehiculos;
    }

    public void set_fin(Date Fin) {
        assert (ini.before(Fin) || ini.equals(Fin));
        this.fin = Fin;
    }

    public void set_ini(Date Ini) {
        assert (Ini.before(fin) || Ini.equals(fin));
        this.ini = Ini;
    }

    public void setCategoria(ECategoriaPedido categoria) {
        this.categoria = categoria;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setEstado(EEstadoPedido estado) {
        this.estado = estado;
    }

    public void setId_pedido(int i) {
        id_pedido = i;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public boolean superpone_IF(Date Ini, Date Fin) {
        Date iniP, finP;
        iniP = get_ini();
        finP = get_fin();
        if (Ini.after(iniP) && Ini.before(finP)) {
            return false;
        }
        if (Fin.after(iniP) && Fin.before(finP)) {
            return false;
        }
        return true;
    }
}

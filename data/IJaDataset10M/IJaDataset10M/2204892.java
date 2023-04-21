package mx.ipn.negocios.nomina;

import java.sql.Date;
import java.util.Collection;
import mx.ipn.Constantes;
import mx.ipn.negocios.ConexionConPersistencia;
import mx.ipn.to.*;

public class PuestoSal {

    public static PuestoSalarioTO[] selectPuestoS() {
        return ((PuestoSalarioTO[]) ConexionConPersistencia.invocaServicio("selectPuestoS", PuestoSalarioTO[].class));
    }

    public static PuestoSalarioTO PuestoSId(short idps) {
        return ((PuestoSalarioTO) ConexionConPersistencia.invocaServicio("PuestoSId", idps, PuestoSalarioTO.class));
    }

    public static boolean addPuestoSalario(PuestoSalarioTO puestosalarioTO) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("addPuestoSalario", puestosalarioTO, Boolean.class)).booleanValue();
    }

    public static short modPuestoSalario(PuestoSalarioTO puestosalarioTO) {
        return ((Short) ConexionConPersistencia.invocaServicio("modPuestoSalario", puestosalarioTO, Short.class)).shortValue();
    }

    public static short delPuestoSalario(short idPuesto) {
        return ((Short) ConexionConPersistencia.invocaServicio("delPuestoSalario", idPuesto, Short.class)).shortValue();
    }

    public static boolean addDescuento(DescuentoTO desc) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("addDescuento", desc, Boolean.class)).booleanValue();
    }

    public static DescuentoTO[] selectDescuento() {
        return ((DescuentoTO[]) ConexionConPersistencia.invocaServicio("selectDescuento", DescuentoTO[].class));
    }

    public static short updateDescuento(DescuentoTO desc) {
        return ((Short) ConexionConPersistencia.invocaServicio("updateDescuento", desc, Short.class)).shortValue();
    }

    public static boolean insertPercepcion(PercepcionTO per) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("insertPercepcion", per, Boolean.class)).booleanValue();
    }

    public static PercepcionTO[] selectPercepcion() {
        return ((PercepcionTO[]) ConexionConPersistencia.invocaServicio("selectPercepcion", PercepcionTO[].class));
    }

    public static short updatePercepcion(PercepcionTO per) {
        return ((Short) ConexionConPersistencia.invocaServicio("updatePercepcion", per, Short.class)).shortValue();
    }

    public static boolean insertComprobantePercepcionDescuento(ComprobantePercepcionDescuentoTO comp) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("insertComprobantePercepcionDescuento", comp, Boolean.class)).booleanValue();
    }

    public static ComprobantePercepcionDescuentoTO[] seleccionarCompPercepcionDescuento() {
        return ((ComprobantePercepcionDescuentoTO[]) ConexionConPersistencia.invocaServicio("seleccionarCompPercepcionDescuento", ComprobantePercepcionDescuentoTO[].class));
    }

    public static short updateCompPD(ComprobantePercepcionDescuentoTO comp) {
        return ((Short) ConexionConPersistencia.invocaServicio("updateCompPD", comp, Short.class)).shortValue();
    }

    public static boolean insertDescuentoPuesto(DescuentoPuestoTO dp) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("insertDescuentoPuesto", dp, Boolean.class)).booleanValue();
    }

    public static boolean registraDHistorial(DescuentoHistorialTO dh) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("registraDHistorial", dh, Boolean.class)).booleanValue();
    }

    public static boolean insertarPercepcionP(PercepcionPuestoTO dp) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("insertarPercepcionP", dp, Boolean.class)).booleanValue();
    }

    public static boolean registraPerHist(PercepcionHistorialTO dh) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("registraPerHist", dh, Boolean.class)).booleanValue();
    }

    public static boolean insertarPerEmp(PercepcionEmpleadoTO dp) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("insertarPerEmp", dp, Boolean.class)).booleanValue();
    }

    public static boolean registraDEmpleado(DescuentoEmpleadoTO dp) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("registraDEmpleado", dp, Boolean.class)).booleanValue();
    }

    public static boolean addRango(RangoTO rango) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("addRango", rango, Boolean.class)).booleanValue();
    }

    public static boolean insertarDEmpleado(DescuentoEmpleadoTO dp) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("insertarDEmpleado", dp, Boolean.class)).booleanValue();
    }

    public static RangoTO[] selectRango() {
        return ((RangoTO[]) ConexionConPersistencia.invocaServicio("selectRango", RangoTO[].class));
    }

    public static boolean insertTipoPercepcionDescuento(TipoPercepcionDescuentoTO tipopd) {
        return ((Boolean) ConexionConPersistencia.invocaServicio("insertTipoPercepcionDescuento", tipopd, Boolean.class)).booleanValue();
    }

    public static TipoPercepcionDescuentoTO[] selectTipoPD() {
        return ((TipoPercepcionDescuentoTO[]) ConexionConPersistencia.invocaServicio("selectTipoPD", TipoPercepcionDescuentoTO[].class));
    }

    public static DescuentoPuestoTO[] selectDescuentoPuesto() {
        return ((DescuentoPuestoTO[]) ConexionConPersistencia.invocaServicio("selectDescuentoPuesto", DescuentoPuestoTO[].class));
    }

    public static PercepcionPuestoTO[] selectperpuesto() {
        return ((PercepcionPuestoTO[]) ConexionConPersistencia.invocaServicio("selectperpuesto", PercepcionPuestoTO[].class));
    }
}

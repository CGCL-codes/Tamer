package edu.upc.condominio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JOptionPane;

public class Pagina {

    private List<Residentes> Residentes = new ArrayList<Residentes>();

    private List<Viviendas> Viviendas = new ArrayList<Viviendas>();

    private List<Cuotas> Cuotas = new ArrayList<Cuotas>();

    private List<AreaComun> AreaComun = new ArrayList<AreaComun>();

    public List<Residentes> getResidentes() {
        return Residentes;
    }

    public void setResidentes(List<Residentes> residentes) {
        Residentes = residentes;
    }

    public void registrarResidentes(String dni, String nombres, String edad, String correo, String clave) {
        if (buscarResidentes(dni) != null) throw new RuntimeException("El residente ya se encuentra registrado !!");
        Residentes residente = new Residentes(dni, nombres, edad, correo, clave);
        Residentes.add(residente);
    }

    public Residentes buscarResidentes(String dni) throws RuntimeException {
        for (Residentes residente : Residentes) {
            if (residente.getDni().equals(dni)) {
                return residente;
            }
        }
        return null;
    }

    public void eliminarResidentes(String dni) {
        Residentes.remove(buscarResidentes(dni));
    }

    public Collection<Residentes> mostrarResidentes() {
        Collection<Residentes> listaResidentes = new ArrayList<Residentes>();
        try {
            listaResidentes = getResidentes();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return listaResidentes;
    }

    public List<Viviendas> getViviendas() {
        return Viviendas;
    }

    public void setViviendas(List<Viviendas> viviendas) {
        Viviendas = viviendas;
    }

    public void registrarViviendas(String svivienda, String propietarioDni, String ubicacion, Integer numero, Integer metraje, String tipo) {
        Residentes residente = buscarResidentes(propietarioDni);
        if (residente != null) {
            if (buscarViviendas(svivienda) != null) throw new RuntimeException("La vivienda ya se encuentra registrada !!");
            Viviendas vivienda = new Viviendas(svivienda, propietarioDni, ubicacion, numero, metraje, tipo);
            Viviendas.add(vivienda);
        }
    }

    public Viviendas buscarViviendas(String vivienda) throws RuntimeException {
        for (Viviendas svivienda : Viviendas) {
            if (svivienda.getVivienda().equals(vivienda)) {
                return svivienda;
            }
        }
        return null;
    }

    public void eliminarViviendas(String vivienda) {
        Viviendas.remove(buscarViviendas(vivienda));
    }

    public Collection<Viviendas> mostrarViviendas() {
        Collection<Viviendas> listaViviendas = new ArrayList<Viviendas>();
        try {
            listaViviendas = getViviendas();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return listaViviendas;
    }

    public List<AreaComun> getAreaComun() {
        return AreaComun;
    }

    public void setAreaComun(List<AreaComun> areaComun) {
        AreaComun = areaComun;
    }

    public void registrarAreaComun(String codigo, String tipo, String ubicacion, Integer capacidad, String estado) {
        if (buscarAreaComun(tipo) != null) throw new RuntimeException("El Area Comun ya se encuentra registrada !!");
        AreaComun area = new AreaComun(codigo, tipo, ubicacion, capacidad, estado);
        AreaComun.add(area);
    }

    public AreaComun buscarAreaComun(String tipo) throws RuntimeException {
        for (AreaComun area : AreaComun) {
            if (area.getTipo().equals(tipo)) {
                return area;
            }
        }
        return null;
    }

    public void eliminarAreaComun(String tipo) {
        AreaComun.remove(buscarAreaComun(tipo));
    }

    public Collection<AreaComun> mostrarAreasComunes() {
        Collection<AreaComun> listaAreas = new ArrayList<AreaComun>();
        try {
            listaAreas = getAreaComun();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return listaAreas;
    }

    public List<Cuotas> getCuotas() {
        return Cuotas;
    }

    public void setCuotas(List<Cuotas> cuotas) {
        Cuotas = cuotas;
    }

    public void registarCuotas(String vivienda, String periodo, int importe, String fvencimiento) {
        if (buscarCuotaViviendaPeriodo(vivienda, periodo) != null) throw new RuntimeException("La cuota ya se encuentra registrada para la vivienda en el periodo !!");
        Cuotas cuota = new Cuotas(vivienda, periodo, importe, fvencimiento);
        Cuotas.add(cuota);
    }

    public Cuotas buscarCuotaViviendaPeriodo(String vivienda, String periodo) throws RuntimeException {
        for (Cuotas cuota : Cuotas) {
            if (cuota.getVivienda().equals(vivienda) && cuota.getPeriodo().equals(periodo)) {
                return cuota;
            }
        }
        return null;
    }

    public void eliminarCuotaViviendaPeriodo(String vivienda, String periodo) {
        Cuotas.remove(buscarCuotaViviendaPeriodo(vivienda, periodo));
    }

    public Collection<Cuotas> mostrarCuotas() {
        Collection<Cuotas> listaCuotas = new ArrayList<Cuotas>();
        try {
            listaCuotas = getCuotas();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return listaCuotas;
    }

    public Cuotas buscarCuotaViviendaPeriodo(String vivienda, String periodo, double cuota) throws RuntimeException {
        for (Cuotas cuotas : Cuotas) {
            if (cuotas.getImporte() == cuota && cuotas.getVivienda().equals(vivienda) && cuotas.getPeriodo().equals(periodo)) {
                JOptionPane.showMessageDialog(null, "El pago de su cuota se realiz� exitosamente");
                return cuotas;
            } else {
                JOptionPane.showMessageDialog(null, "No se ha pagado en su totalidad");
                double importeactual = cuotas.getImporte();
                importeactual = importeactual - cuota;
                cuotas.setImporte(importeactual);
            }
        }
        return null;
    }

    public void pagarCuotas(String vivienda, String periodo, double cuota) {
        Cuotas.remove(buscarCuotaViviendaPeriodo(vivienda, periodo, cuota));
    }

    public boolean validarPagoCuota(String cuota) {
        @SuppressWarnings("unused") double pago;
        try {
            pago = Double.parseDouble(cuota);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Ud. Debe ingresar solamente n�meros");
        }
        return true;
    }

    public boolean validarPagoCuota(double cuota) {
        if (cuota <= 0) {
            JOptionPane.showMessageDialog(null, "Ud. Debe ingresar solo cuotas mayores a [0]");
            return false;
        } else {
            JOptionPane.showMessageDialog(null, "La cuota ha sido cancelada exitosamente 25-07-2011");
            return true;
        }
    }
}

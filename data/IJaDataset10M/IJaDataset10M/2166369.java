package com.jsfcompref.model;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceRef;
import java.util.List;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import webservices.*;

@ManagedBean
@SessionScoped
public class UBean {

    @WebServiceRef(wsdlLocation = "http://190.17.0.161:8080/AgenciaWS/AgenciaWS?wsdl")
    public webservices.AgenciaWS_Service service;

    @Resource
    protected WebServiceContext context;

    private int idd;

    private Cliente cliente;

    private String usr;

    private String pas;

    private boolean login = false;

    private List<Servicio> listaServicios;

    private Servicio servic;

    private Proveedor prov;

    private InstanciaServicio inst;

    private InstanciaHotelServicio instHS;

    private InstanciaTourServicio instTR;

    private InstanciaAutoServicio instAS;

    private InstanciaTransporteServicio instTRS;

    private ModeloHabitacion modHabitacion;

    private ModeloAuto modAuto;

    public InstanciaTransporteServicio getInstTRS() {
        return instTRS;
    }

    public void setInstTRS(InstanciaTransporteServicio instTRS) {
        this.instTRS = instTRS;
    }

    public ModeloAuto getModAuto() {
        return modAuto;
    }

    public void setModAuto(ModeloAuto modAuto) {
        this.modAuto = modAuto;
    }

    public InstanciaAutoServicio getInstAS() {
        return instAS;
    }

    public void setInstAS(InstanciaAutoServicio instAS) {
        this.instAS = instAS;
    }

    public InstanciaTourServicio getInstTR() {
        return instTR;
    }

    public void setInstTR(InstanciaTourServicio instTR) {
        this.instTR = instTR;
    }

    public ModeloHabitacion getModHabitacion() {
        return modHabitacion;
    }

    public void setModHabitacion(ModeloHabitacion modHabitacion) {
        this.modHabitacion = modHabitacion;
    }

    public InstanciaServicio getInst() {
        return inst;
    }

    public void setInst(InstanciaServicio inst) {
        this.inst = inst;
    }

    public InstanciaHotelServicio getInstHS() {
        return instHS;
    }

    public void setInstHS(InstanciaHotelServicio instHS) {
        this.instHS = instHS;
    }

    public Proveedor getProv() {
        return prov;
    }

    public void setProv(Proveedor prov) {
        this.prov = prov;
    }

    public Servicio getServic() {
        return servic;
    }

    public void setServic(Servicio servic) {
        this.servic = servic;
    }

    public List<Servicio> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<Servicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public String getPas() {
        return pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public UBean() throws DatatypeConfigurationException {
        this.cliente = new Cliente();
        GregorianCalendar gcal = new GregorianCalendar();
        XMLGregorianCalendar fc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        fc.setDay(1);
        fc.setMonth(1);
        fc.setYear(1981);
        this.cliente.setFechaNacimiento(fc);
        this.instHS = new InstanciaHotelServicio();
        this.instAS = new InstanciaAutoServicio();
        this.instTRS = new InstanciaTransporteServicio();
        GregorianCalendar gcal2 = new GregorianCalendar();
        XMLGregorianCalendar fe = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal2);
        fe.setDay(28);
        fe.setMonth(5);
        fe.setYear(2011);
        this.instHS.setFechaEntrada(fe);
        GregorianCalendar gcal3 = new GregorianCalendar();
        XMLGregorianCalendar fs = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal3);
        fs.setDay(30);
        fs.setMonth(5);
        fs.setYear(2011);
        this.instHS.setFechaSalida(fs);
        GregorianCalendar gcal4 = new GregorianCalendar();
        XMLGregorianCalendar fe2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal4);
        fe2.setDay(28);
        fe2.setMonth(5);
        fe2.setYear(2011);
        this.instAS.setFechaInicio(fe2);
        GregorianCalendar gcal5 = new GregorianCalendar();
        XMLGregorianCalendar fs2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal5);
        fs2.setDay(30);
        fs2.setMonth(5);
        fs2.setYear(2011);
        this.instAS.setFechaFin(fs2);
        this.instTR = new InstanciaTourServicio();
        GregorianCalendar gcal6 = new GregorianCalendar();
        XMLGregorianCalendar ft = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal6);
        ft.setDay(13);
        ft.setMonth(7);
        ft.setYear(2011);
        this.instTR.setFecha(ft);
        GregorianCalendar gcal7 = new GregorianCalendar();
        XMLGregorianCalendar fttr = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal7);
        fttr.setDay(6);
        fttr.setMonth(8);
        fttr.setYear(2011);
        this.instTRS.setPartida(fttr);
    }

    public int getIdd() {
        return idd;
    }

    public void setIdd(int idd) {
        this.idd = idd;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String addConfirmedUser() {
        return "resp_consulta_usuario";
    }

    public String guardarCliente() {
        AgenciaWS port = service.getAgenciaWSPort();
        port.createCliente(cliente);
        this.setLogin(true);
        return "inicio";
    }

    public String actualizarCliente() {
        AgenciaWS port = service.getAgenciaWSPort();
        port.editCliente(cliente);
        return "inicio";
    }

    public String logIn() {
        AgenciaWS port = service.getAgenciaWSPort();
        if (usr.isEmpty()) {
            return "faillogin";
        }
        if (pas.isEmpty()) {
            return "faillogin";
        }
        Cliente clint = port.logInCliente(usr, pas);
        if (clint != null) {
            this.setLogin(true);
            this.setCliente(clint);
            if (this.cliente.getFechaNacimiento() == null) {
                XMLGregorianCalendar fech = new XMLGregorianCalendarImpl();
                fech.setDay(1);
                fech.setMonth(1);
                fech.setYear(2000);
                this.cliente.setFechaNacimiento(fech);
            }
            return "inicio";
        } else {
            this.setLogin(false);
            return "faillogin";
        }
    }

    public String logOut() {
        this.setLogin(false);
        this.setCliente(null);
        return "index";
    }

    public String getUserWelcome() {
        if (this.isLogin()) {
            return ", " + this.getCliente().getNombre() + " " + this.getCliente().getApellido();
        } else {
            return "";
        }
    }

    public String confirmInsths() {
        this.instHS.setHotelServicio((HotelServicio) servic);
        List<Habitacion> listHab = this.instHS.getHotelServicio().getHabitaciones();
        List<ModeloHabitacion> listMod = this.instHS.getHotelServicio().getModelosHabitaciones();
        this.setModHabitacion(listMod.get(0));
        Iterator<Habitacion> it = listHab.iterator();
        Habitacion hab = it.next();
        while (!hab.getTipo().equals(this.modHabitacion)) {
            hab = it.next();
        }
        this.instHS.setHabitacion(hab);
        this.instHS.setPrecio(this.modHabitacion.getPrecio());
        InstanciaHotelServicio inst2 = new InstanciaHotelServicio();
        inst2.setFechaEntrada(instHS.getFechaEntrada());
        inst2.setFechaSalida(instHS.getFechaSalida());
        inst2.setHabitacion(hab);
        inst2.setPrecio(instHS.getPrecio());
        inst2.setHotelServicio(instHS.getHotelServicio());
        this.addInst(inst2);
        return "carrito";
    }

    public String confirmInstas() {
        this.instAS.setAutoServicio((AutoServicio) servic);
        List<Auto> listAut = this.instAS.getAutoServicio().getAutos();
        List<ModeloAuto> listMod = this.instAS.getAutoServicio().getModelosAutos();
        this.setModAuto(listMod.get(0));
        Iterator<Auto> it = listAut.iterator();
        Auto aut = it.next();
        while (!aut.getModeloDelAuto().equals(this.modAuto)) {
            aut = it.next();
        }
        this.instAS.setAuto(aut);
        this.instAS.setPrecio(this.modAuto.getCosto());
        InstanciaAutoServicio inst3 = new InstanciaAutoServicio();
        inst3.setFechaInicio(instAS.getFechaInicio());
        inst3.setFechaFin(instAS.getFechaFin());
        inst3.setAuto(instAS.getAuto());
        inst3.setPrecio(instAS.getPrecio());
        inst3.setAutoServicio(instAS.getAutoServicio());
        this.addInst(inst3);
        return "carrito";
    }

    public String confirmInsttr() {
        InstanciaTourServicio insttr = new InstanciaTourServicio();
        insttr.setTourServicio((TourServicio) servic);
        insttr.setFecha(this.instTR.getFecha());
        float sumprecio = (float) 0;
        for (Iterator<Tour> it = insttr.getTourServicio().getListaToures().iterator(); it.hasNext(); ) {
            Tour trr = it.next();
            sumprecio = sumprecio + trr.getPrecio();
        }
        insttr.setPrecio(sumprecio);
        this.addInst(insttr);
        return "carrito";
    }

    public String confirmInsttrs() {
        return "carrito";
    }

    public void addInst(InstanciaServicio inst) {
        List<Factura> facts = this.cliente.getFacturas();
        if (facts.isEmpty()) {
            Factura fac = new Factura();
            facts.add(fac);
        }
        facts.get(0).getLineas().add(inst);
    }

    public String totalFactum() {
        List<InstanciaServicio> listinst = this.getCliente().getFacturas().get(0).getLineas();
        float totalMonto = (float) 0;
        for (Iterator<InstanciaServicio> it = listinst.iterator(); it.hasNext(); ) {
            totalMonto = totalMonto + it.next().getPrecio();
        }
        return Float.toString(totalMonto);
    }

    public String register() throws DatatypeConfigurationException {
        this.cliente = new Cliente();
        this.setLogin(false);
        if (this.cliente.getFechaNacimiento() == null) {
            GregorianCalendar gcal = new GregorianCalendar();
            XMLGregorianCalendar fech = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            fech.setDay(1);
            fech.setMonth(1);
            fech.setYear(1999);
            this.cliente.setFechaNacimiento(fech);
        }
        return "register";
    }

    public String guardarCambiosCliente() {
        AgenciaWS port = service.getAgenciaWSPort();
        port.editCliente(this.cliente);
        return "inicio";
    }

    public List<String> lineasDescrip(Servicio serv) {
        List<String> lis = new ArrayList<String>();
        if ("webservices.HotelServicio".equals(serv.getClass().getName())) {
            HotelServicio hs = (HotelServicio) serv;
            List<ModeloHabitacion> armod = hs.getModelosHabitaciones();
            for (Iterator<ModeloHabitacion> it = armod.iterator(); it.hasNext(); ) {
                ModeloHabitacion modh = it.next();
                lis.add("Habitacion " + modh.getNombre());
                lis.add("...  " + Integer.toString(modh.getNumeroCamas()) + " camas");
                lis.add("...  Precio: $" + Float.toString(modh.getPrecio()));
            }
        }
        if ("webservices.TourServicio".equals(serv.getClass().getName())) {
            TourServicio ts = (TourServicio) serv;
            lis.add(ts.getDescripcion());
            for (Iterator<Tour> it = ts.getListaToures().iterator(); it.hasNext(); ) {
                Tour tr = it.next();
                lis.add("Tour: " + tr.getNombre());
                lis.add("Cupo: " + Integer.toString(tr.getCupo()));
                lis.add("Precio: $" + Float.toString(tr.getPrecio()));
                lis.add("");
            }
        }
        if ("webservices.AutoServicio".equals(serv.getClass().getName())) {
            AutoServicio as = (AutoServicio) serv;
            List<ModeloAuto> aumod = as.getModelosAutos();
            for (Iterator<ModeloAuto> it = aumod.iterator(); it.hasNext(); ) {
                ModeloAuto moda = it.next();
                lis.add("Modelo " + moda.getNombre());
                lis.add("...  " + Integer.toString(moda.getPlazas()) + " plazas");
                lis.add("... Precio $" + Float.toString(moda.getCosto()));
            }
        }
        if ("webservices.TransporteServicio".equals(serv.getClass().getName())) {
            TransporteServicio tsss = (TransporteServicio) serv;
            lis.add("Transporte: " + tsss.getUnidadTransporte().getModelo().getNombre());
            lis.add("Precio por KM: $" + Float.toString(tsss.getUnidadTransporte().getModelo().getPrecioXKm()));
            lis.add("Cantidad de asientos: " + tsss.getUnidadTransporte().getModelo().getAsientos());
            lis.add("");
            lis.add("Ciudad de Origen: " + tsss.getItinerario().getCiudadOrigen().getNombre());
            lis.add("Paradas: ");
            for (Iterator<Parada> prr = tsss.getItinerario().getCiudad().iterator(); prr.hasNext(); ) {
                Parada pr1 = prr.next();
                lis.add("a " + Float.toString(pr1.getKmDelOrigen()) + " KM: " + pr1.getCiudad().getNombre());
            }
        }
        return lis;
    }

    public List<String> lineasDescripInst(InstanciaServicio inst) {
        List<String> lis = new ArrayList<String>();
        if ("webservices.InstanciaHotelServicio".equals(inst.getClass().getName())) {
            InstanciaHotelServicio insttHS = (InstanciaHotelServicio) inst;
            lis.add("Servicio: " + insttHS.getHotelServicio().getDescripcion());
            lis.add("Habitacion: " + insttHS.getHabitacion().getNumero());
            lis.add("Tipo: " + insttHS.getHabitacion().getTipo().getNombre());
            lis.add("Fecha de entrada: " + insttHS.getFechaEntrada().getDay() + "/" + insttHS.getFechaEntrada().getMonth() + "/" + insttHS.getFechaEntrada().getYear());
            lis.add("Fecha de salida: " + insttHS.getFechaSalida().getDay() + "/" + insttHS.getFechaSalida().getMonth() + "/" + insttHS.getFechaSalida().getYear());
            lis.add("Precio: $" + Float.toString(insttHS.getPrecio()));
        }
        if ("webservices.InstanciaTourServicio".equals(inst.getClass().getName())) {
            InstanciaTourServicio insttTS = (InstanciaTourServicio) inst;
            List<Tour> listt = insttTS.getTourServicio().getListaToures();
            lis.add(insttTS.getTourServicio().getDescripcion());
            for (Iterator<Tour> it = listt.iterator(); it.hasNext(); ) {
                Tour trr = it.next();
                lis.add("Tour: " + trr.getNombre());
                lis.add("Cupo: " + Integer.toString(trr.getCupo()) + " Personas");
                lis.add("Precio: $" + Float.toString(trr.getPrecio()));
            }
            lis.add("Precio total del tour: $" + Float.toString(insttTS.getPrecio()));
        }
        if ("webservices.InstanciaAutoServicio".equals(inst.getClass().getName())) {
            InstanciaAutoServicio insttAS = (InstanciaAutoServicio) inst;
            lis.add("Servicio: " + insttAS.getAutoServicio().getDescripcion());
            lis.add("Patente del auto: " + insttAS.getAuto().getPatente());
            lis.add("Tipo: " + insttAS.getAuto().getModeloDelAuto().getNombre());
            lis.add("Fecha de entrada: " + insttAS.getFechaInicio().getDay() + "/" + insttAS.getFechaInicio().getMonth() + "/" + insttAS.getFechaInicio().getYear());
            lis.add("Fecha de salida: " + insttAS.getFechaFin().getDay() + "/" + insttAS.getFechaFin().getMonth() + "/" + insttAS.getFechaFin().getYear());
            lis.add("Precio: $" + Float.toString(insttAS.getPrecio()));
        }
        return lis;
    }
}

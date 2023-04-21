package org.opensih.gdq.Actions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import org.jboss.annotation.ejb.cache.Cache;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.opensih.gdq.ControladoresCU.IMantenimiento;
import org.opensih.gdq.Modelo.Cie10;
import org.opensih.gdq.Modelo.Coordinacion;
import org.opensih.gdq.Modelo.Datos;
import org.opensih.gdq.Modelo.Intervencion;
import org.opensih.gdq.Modelo.Paciente;
import org.opensih.gdq.Modelo.Sala;
import org.opensih.gdq.Modelo.SaqAsse;
import org.opensih.gdq.Modelo.Servicio;
import org.opensih.gdq.Modelo.TecnicoMedico;
import org.opensih.gdq.Modelo.UnidadEjecutora;
import org.opensih.gdq.Seguridad.IUtils;
import org.opensih.gdq.Utils.Constantes.EstadoVoc;
import org.opensih.gdq.Utils.Converters.Encoder;
import org.opensih.gdq.Utils.Converters.MedicosConverter;
import org.opensih.gdq.Utils.Converters.SalasConverter;
import org.opensih.gdq.Utils.Converters.ServicioConverter;

@Stateful
@Name("ingresarGDQ")
@Cache(org.jboss.ejb3.cache.NoPassivationCache.class)
public class IngresoAction implements IIngreso {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @EJB
    IUtils utils;

    @EJB
    IMantenimiento logica;

    @In(required = false)
    @Out(required = false)
    Intervencion intervencion;

    @In(required = false)
    Paciente paciente;

    UnidadEjecutora ue;

    List<Servicio> orgs;

    Map<String, Servicio> orgsMap;

    List<TecnicoMedico> meds;

    String internado;

    List<Sala> salasInt;

    Sala salaInt;

    String camaInt;

    String anestesia, modalidad, tipo, indicaciones;

    boolean mostrarTipo;

    @In(required = false)
    @Out(required = false)
    String dpo;

    String dpocod;

    String dpotodo;

    List<String> listDpo = new LinkedList<String>();

    String ampliacion_dpo;

    boolean bool;

    @In(required = false)
    @Out(required = false)
    String pp;

    String ppcod;

    String pptodo;

    List<String> listPp = new LinkedList<String>();

    String ampliacion_pp;

    boolean bool2;

    @In(required = false)
    @Out(required = false)
    String ee;

    String eeTexto;

    List<String> listEe = new LinkedList<String>();

    String txEquip;

    String txEspEquip;

    boolean verEquipEsp;

    boolean bool3;

    @In(required = false)
    @Out(required = false)
    String inst;

    List<String> listInst = new LinkedList<String>();

    String txInstr;

    String txEspInstr;

    boolean verInstrDetalles;

    boolean bool4;

    @In(required = false)
    @Out(required = false)
    String insum;

    List<String> listInsum = new LinkedList<String>();

    String txInsum;

    String txEspInsum;

    boolean verInsumEsp;

    boolean bool5;

    @In(required = false)
    @Out(required = false)
    String otrReq;

    List<String> listOtrosReq = new LinkedList<String>();

    String txOtrosReq;

    String txEspOtrosReq;

    boolean verOtrosReq;

    boolean bool6;

    @Create
    @Begin(join = true)
    public String inicial() {
        ue = utils.devolverUE();
        if (ue == null) ue = logica.listarUnidades().get(0);
        orgs = logica.listarServicios(ue.getCodigo());
        Map<String, Servicio> results = new TreeMap<String, Servicio>();
        for (Servicio p : orgs) {
            String nom = p.toString();
            results.put(nom, p);
        }
        orgsMap = results;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date fecha = new Date();
        intervencion = new Intervencion();
        intervencion.setID(sdf.format(fecha));
        intervencion.setFechaIngreso(fecha);
        intervencion.setDatos(new Datos());
        intervencion.setEstado(EstadoVoc.Pronto);
        mostrarTipo = true;
        bool = bool2 = bool3 = bool4 = bool5 = bool6 = true;
        verEquipEsp = false;
        txEspEquip = txEspInstr = txEspInsum = txEspOtrosReq = "";
        internado = "Seleccionar";
        camaInt = "";
        return "ok";
    }

    public String guardar() {
        if (paciente == null) {
            FacesMessages.instance().add("Debe ingresar un Paciente.");
            return null;
        }
        if (paciente.getInfoContacto().compareTo("") == 0) {
            FacesMessages.instance().add("Debe completar la informaci�n de contacto del Paciente.");
            return null;
        }
        if (internado.equals("Seleccionar")) {
            FacesMessages.instance().add("Debe completar si el Paciente se encuentra internado o no.");
            return null;
        }
        if (internado.equals("Si") && salaInt == null) {
            FacesMessages.instance().add("Debe completar la sala de internacion del Paciente.");
            return null;
        }
        if (internado.equals("Si") && camaInt.equals("")) {
            FacesMessages.instance().add("Debe completar la cama de internacion del Paciente.");
            return null;
        }
        if (intervencion.getServicio() == null) {
            FacesMessages.instance().add("Debe agregar un Servicio.");
            return null;
        }
        if (intervencion.getResponsable() == null) {
            FacesMessages.instance().add("Debe agregar un M�dico Responsable.");
            return null;
        }
        if (intervencion.getFechaSolicitud() == null) {
            FacesMessages.instance().add("Debe seleccionar la fecha de solicitud de la cirug�a.");
            return null;
        }
        if ((new Date()).getTime() <= intervencion.getFechaSolicitud().getTime()) {
            FacesMessages.instance().add("La fecha de solicitud no puede ser mayor a la fecha actual.");
            return null;
        }
        String diag = listarDpo();
        if (diag == null || diag.equals("<root></root>")) {
            FacesMessages.instance().add("Debe ingresar un Diagn�stico Pre Operatorio.");
            return null;
        }
        String procs = listarPp();
        if (procs == null || procs.equals("<root></root>")) {
            FacesMessages.instance().add("Debe ingresar un Procedimiento Propuesto.");
            return null;
        }
        if (anestesia == null) {
            FacesMessages.instance().add("Debe seleccionar el tipo de Anestesia.");
            return null;
        }
        if (modalidad == null) {
            FacesMessages.instance().add("Debe seleccionar la Modalidad.");
            return null;
        }
        if (tipo == null && modalidad.equals("Cirug�a con Internaci�n")) {
            FacesMessages.instance().add("Debe seleccionar el Tipo.");
            return null;
        }
        Paciente pac = em.find(Paciente.class, paciente.getRoot_extension());
        if (pac != null) {
            pac.setSexo(paciente.getSexo());
            pac.setFnac(paciente.getFnac());
            pac.setNombre(paciente.getNombre());
            pac.setApellido(paciente.getApellido());
            intervencion.setPaciente(pac);
            em.merge(pac);
        } else {
            intervencion.setPaciente(paciente);
        }
        intervencion.setCoordinaciones(new LinkedList<Coordinacion>());
        intervencion.getDatos().setAnestesia(anestesia);
        intervencion.getDatos().setModalidad(modalidad);
        intervencion.getDatos().setTipo(tipo);
        intervencion.getDatos().setIndicaciones(indicaciones);
        intervencion.getDatos().setDiagnostico(diag);
        intervencion.getDatos().setProcedimiento(procs);
        intervencion.getDatos().setEquipamiento(listarEe());
        intervencion.getDatos().setInstrumental(listarInstr());
        intervencion.getDatos().setInsumos(listarInsum());
        intervencion.getDatos().setOtrosreq(listarOtrosreq());
        intervencion.getDatos().guardarInternacion(salaInt, camaInt);
        em.persist(intervencion);
        return "nueva";
    }

    public Map<String, Servicio> getOrgs() {
        return orgsMap;
    }

    public Converter getConverterServ() {
        return new ServicioConverter(orgs);
    }

    public Converter getConverterMeds() {
        return new MedicosConverter(meds);
    }

    public Converter getConverterSalaInt() {
        return new SalasConverter(salasInt);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List autoCompletarBusquedaMedicos(Object event) {
        String pref = event.toString();
        Query q = em.createQuery("SELECT med FROM UnidadEjecutora ue join ue.tecnicosMedicos med WHERE ue.codigo=:cod AND (med.nombre like :filtro or med.apellido like :filtro)order by med.nombre, med.apellido").setParameter("cod", ue.getCodigo()).setParameter("filtro", pref.concat("%")).setMaxResults(50);
        meds = q.getResultList();
        return meds;
    }

    @SuppressWarnings("unchecked")
    public List<Sala> autoCompletarSalasInt(Object event) {
        String pref = "%" + event.toString();
        Query q = em.createQuery("select s from Sala s where s.ue.codigo=:cod AND s.nombre like :filtro order by s.nombre").setParameter("cod", ue.getCodigo()).setParameter("filtro", pref.concat("%"));
        salasInt = q.getResultList();
        return salasInt;
    }

    @End
    public String cancel() {
        return "ok";
    }

    public void seteo() {
    }

    public void seteo2() {
        if (txEquip.compareTo("Otros") == 0) verEquipEsp = true; else verEquipEsp = false;
        if (txInstr.compareTo("Otros") == 0) verInstrDetalles = true; else verInstrDetalles = false;
        if (txInsum.compareTo("Otros") == 0 || txInsum.compareTo("Maquina de Sutura") == 0 || txInsum.compareTo("Sondas") == 0 || txInsum.compareTo("Productos hemost�ticos") == 0) verInsumEsp = true; else {
            verInsumEsp = false;
            txEspInsum = "";
        }
        if (txOtrosReq.compareTo("Otros") == 0) verOtrosReq = true; else verOtrosReq = false;
    }

    public void seteoMod() {
        if (modalidad != null && modalidad.equals("Cirug�a con Internaci�n")) {
            mostrarTipo = false;
        } else {
            mostrarTipo = true;
            tipo = null;
        }
    }

    @Destroy
    @Remove
    public void destroy() {
    }

    @SuppressWarnings("unchecked")
    public void seteoDiagPreOP() {
        if (dpocod.compareTo("") != 0) {
            String s = dpocod.toUpperCase();
            List<Cie10> b = em.createQuery("from Cie10 c where c.cod = :cod").setParameter("cod", s).getResultList();
            dpotodo = !b.isEmpty() ? b.get(0).getCod() + " | " + b.get(0).getDescrip() : "C�digo invalido.";
        } else {
            dpotodo = "";
        }
    }

    @SuppressWarnings("unchecked")
    public void seteoOpProp() {
        if (ppcod.compareTo("") != 0) {
            String s = ppcod.toUpperCase();
            List<SaqAsse> c = em.createQuery("from SaqAsse c where c.cod = :cod").setParameter("cod", s).getResultList();
            pptodo = !c.isEmpty() ? c.get(0).getCod() + " | " + c.get(0).getDescrip() : "C�digo invalido.";
        } else {
            pptodo = "";
        }
    }

    public String agregardpo() {
        seteoDiagPreOP();
        if (ampliacion_dpo.contains("|")) {
            FacesMessages.instance().add("No se puede utilizar el caracter | en la ampliaci�n.");
            return null;
        }
        if (dpotodo.compareTo("") != 0) {
            if (noPertenece(listDpo, dpocod) && dpotodo.compareTo("C�digo invalido.") != 0) {
                listDpo.add(dpotodo + ((ampliacion_dpo != "") ? " | " + ampliacion_dpo : ""));
            } else if (!noPertenece(listDpo, dpocod)) return "No se puede ingresar dos veces el mismo codigo";
        } else if (ampliacion_dpo != "" && !listDpo.contains(ampliacion_dpo)) {
            listDpo.add(ampliacion_dpo);
        } else {
            FacesMessages.instance().add("Debe ingresar un c�digo o un texto en ampliaci�n");
            return null;
        }
        dpotodo = "";
        dpocod = "";
        ampliacion_dpo = "";
        return "ok";
    }

    public String agregarpp() {
        seteoOpProp();
        if (ampliacion_pp.contains("|")) {
            FacesMessages.instance().add("No se puede utilizar el caracter | en la ampliaci�n.");
            return null;
        }
        if (pptodo.compareTo("") != 0) {
            if (noPertenece(listPp, ppcod) && pptodo.compareTo("C�digo invalido.") != 0) {
                listPp.add(pptodo + ((ampliacion_pp != "") ? " | " + ampliacion_pp : ""));
            } else if (!noPertenece(listPp, ppcod)) {
                FacesMessages.instance().add("No se puede ingresar dos veces el mismo codigo");
                return null;
            }
        } else if (ampliacion_pp != "" && !listPp.contains(ampliacion_pp)) {
            listPp.add(ampliacion_pp);
        } else {
            FacesMessages.instance().add("Debe ingresar un c�digo.");
            return null;
        }
        pptodo = "";
        ppcod = "";
        ampliacion_pp = "";
        return "ok";
    }

    public String agregaree() {
        if (txEquip.equals("_")) {
            FacesMessages.instance().add("Debe completar el equipo.");
            return null;
        } else {
            if (txEquip.compareTo("Otros") == 0) {
                listEe.add(txEquip + " | " + txEspEquip);
            } else {
                listEe.add(txEquip);
            }
        }
        txEquip = "_";
        txEspEquip = "";
        verEquipEsp = false;
        return "ok";
    }

    public String agregarinst() {
        if (txInstr.equals("_")) {
            FacesMessages.instance().add("Debe completar un instrumental.");
            return null;
        } else {
            if (txInstr.compareTo("Otros") == 0) {
                listInst.add(txInstr + " | " + txEspInstr);
            } else {
                listInst.add(txInstr);
            }
        }
        txInstr = "_";
        txEspInstr = "";
        verInstrDetalles = false;
        return "ok";
    }

    public String agregarinsum() {
        if (txInsum.equals("_")) {
            FacesMessages.instance().add("Debe completar un insumo.");
            return null;
        } else {
            String texto = txInsum;
            texto += (txEspInsum.equals("") ? "" : " | " + txEspInsum);
            listInsum.add(texto);
        }
        txInsum = "_";
        txEspInsum = "";
        verInsumEsp = false;
        return "ok";
    }

    public String agregarotroReq() {
        if (txOtrosReq.equals("_")) {
            FacesMessages.instance().add("Debe completar Otros Requerimentos.");
            return null;
        } else {
            if (txOtrosReq.compareTo("Otros") == 0) {
                listOtrosReq.add(txOtrosReq + " | " + txEspOtrosReq);
            } else {
                listOtrosReq.add(txOtrosReq);
            }
        }
        txOtrosReq = "_";
        txEspOtrosReq = "";
        verOtrosReq = false;
        return "ok";
    }

    public String sacardpo() {
        listDpo.remove(dpo);
        bool = true;
        return "ok";
    }

    public String sacarpp() {
        listPp.remove(pp);
        bool2 = true;
        return "ok";
    }

    public String sacaree() {
        listEe.remove(ee);
        bool3 = true;
        return "ok";
    }

    public String sacarintr() {
        listInst.remove(inst);
        bool4 = true;
        return "ok";
    }

    public String sacarinsum() {
        listInsum.remove(insum);
        bool5 = true;
        return "ok";
    }

    public String sacarotrosReq() {
        listOtrosReq.remove(otrReq);
        bool6 = true;
        return "ok";
    }

    private boolean noPertenece(List<String> list, String cod) {
        cod = cod.toUpperCase();
        for (String s : list) {
            if (s.split("\\|").length > 1 && cod.equals(s.split("\\|")[0].trim())) return false;
        }
        return true;
    }

    private String listarDpo() {
        String s = "<root>";
        for (String aux : listDpo) s += "<pre" + codeToXML(aux) + " />";
        s += "</root>";
        return s;
    }

    private String listarPp() {
        String s = "<root>";
        for (String aux : listPp) s += "<pre" + codeToXML(aux) + " />";
        s += "</root>";
        return s;
    }

    private String listarEe() {
        String s = "<root>";
        for (String aux : listEe) s += "<equip>" + aux + "</equip>";
        s += "</root>";
        return s;
    }

    private String listarInstr() {
        String s = "<root>";
        for (String aux : listInst) s += "<instrum>" + aux + "</instrum>";
        s += "</root>";
        return s;
    }

    private String listarInsum() {
        String s = "<root>";
        for (String aux : listInsum) s += "<insum>" + aux + "</insum>";
        s += "</root>";
        return s;
    }

    private String listarOtrosreq() {
        String s = "<root>";
        for (String aux : listOtrosReq) s += "<otrosreq>" + aux + "</otrosreq>";
        s += "</root>";
        return s;
    }

    /*****INSTRUMENTAL
	<root>
		<instrum>gg</instrum>
		<instrum>gdfgd</instrum>
	</root>
	
	
	INSUMOS
	<root>
		<insum>gg</insum>
		<insum>gdfgd</insum>
	</root>
	
	OTROSREQ
	<root>
		<otrosreq>gg</otrosreq>
		<otrosreq>gdfgd</otrosreq>
	</root>
	 ***********/
    private String codeToXML(String cod) {
        String a[] = cod.split("\\|");
        if (a.length == 1) return " ampliacion=\"" + Encoder.parseXML(a[0].trim()) + "\""; else if (a.length == 2) return " code=\"" + Encoder.parseXML(a[0].trim()) + "\"" + " descripcion=\"" + Encoder.parseXML(a[1].trim()) + "\""; else return " code=\"" + Encoder.parseXML(a[0].trim()) + "\"" + " descripcion=\"" + Encoder.parseXML(a[1].trim()) + "\"" + " ampliacion=\"" + Encoder.parseXML(a[2].trim()) + "\"";
    }

    public UnidadEjecutora getUe() {
        return ue;
    }

    public void setUe(UnidadEjecutora ue) {
        this.ue = ue;
    }

    public String getDpocod() {
        return dpocod;
    }

    public void setDpocod(String dpocod) {
        this.dpocod = dpocod;
    }

    public String getDpotodo() {
        return dpotodo;
    }

    public void setDpotodo(String dpotodo) {
        this.dpotodo = dpotodo;
    }

    public List<String> getListDpo() {
        return listDpo;
    }

    public void setListDpo(List<String> listDpo) {
        this.listDpo = listDpo;
    }

    public String getAmpliacion_dpo() {
        return ampliacion_dpo;
    }

    public void setAmpliacion_dpo(String ampliacion_dpo) {
        this.ampliacion_dpo = ampliacion_dpo;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String getPpcod() {
        return ppcod;
    }

    public void setPpcod(String ppcod) {
        this.ppcod = ppcod;
    }

    public String getPptodo() {
        return pptodo;
    }

    public void setPptodo(String pptodo) {
        this.pptodo = pptodo;
    }

    public List<String> getListPp() {
        return listPp;
    }

    public void setListPp(List<String> listPp) {
        this.listPp = listPp;
    }

    public String getAmpliacion_pp() {
        return ampliacion_pp;
    }

    public void setAmpliacion_pp(String ampliacion_pp) {
        this.ampliacion_pp = ampliacion_pp;
    }

    public boolean isBool2() {
        return bool2;
    }

    public void setBool2(boolean bool2) {
        this.bool2 = bool2;
    }

    public String getEeTexto() {
        return eeTexto;
    }

    public void setEeTexto(String eeTexto) {
        this.eeTexto = eeTexto;
    }

    public List<String> getListEe() {
        return listEe;
    }

    public void setListEe(List<String> listEe) {
        this.listEe = listEe;
    }

    public boolean isBool3() {
        return bool3;
    }

    public void setBool3(boolean bool3) {
        this.bool3 = bool3;
    }

    public boolean isBool4() {
        return bool4;
    }

    public void setBool4(boolean bool4) {
        this.bool4 = bool4;
    }

    public boolean isBool5() {
        return bool5;
    }

    public void setBool5(boolean bool5) {
        this.bool5 = bool5;
    }

    public boolean isBool6() {
        return bool6;
    }

    public void setBool6(boolean bool6) {
        this.bool6 = bool6;
    }

    public boolean isMostrarTipo() {
        return mostrarTipo;
    }

    public void setMostrarTipo(boolean mostrarTipo) {
        this.mostrarTipo = mostrarTipo;
    }

    public String getAnestesia() {
        return anestesia;
    }

    public void setAnestesia(String anestesia) {
        this.anestesia = anestesia;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public boolean isVerEquipEsp() {
        return verEquipEsp;
    }

    public void setVerEquipEsp(boolean verEquipEsp) {
        this.verEquipEsp = verEquipEsp;
    }

    public boolean isVerInstrDetalles() {
        return verInstrDetalles;
    }

    public void setVerInstrDetalles(boolean verInstrDetalles) {
        this.verInstrDetalles = verInstrDetalles;
    }

    public boolean isVerInsumEsp() {
        return verInsumEsp;
    }

    public void setVerInsumEsp(boolean verInsumEsp) {
        this.verInsumEsp = verInsumEsp;
    }

    public boolean isVerOtrosReq() {
        return verOtrosReq;
    }

    public void setVerOtrosReq(boolean verOtrosReq) {
        this.verOtrosReq = verOtrosReq;
    }

    public String getTxEquip() {
        return txEquip;
    }

    public void setTxEquip(String txEquip) {
        this.txEquip = txEquip;
    }

    public String getTxInstr() {
        return txInstr;
    }

    public void setTxInstr(String txInstr) {
        this.txInstr = txInstr;
    }

    public String getTxInsum() {
        return txInsum;
    }

    public void setTxInsum(String txInsum) {
        this.txInsum = txInsum;
    }

    public String getTxOtrosReq() {
        return txOtrosReq;
    }

    public void setTxOtrosReq(String txOtrosReq) {
        this.txOtrosReq = txOtrosReq;
    }

    public String getTxEspEquip() {
        return txEspEquip;
    }

    public void setTxEspEquip(String txEspEquip) {
        this.txEspEquip = txEspEquip;
    }

    public String getTxEspInstr() {
        return txEspInstr;
    }

    public void setTxEspInstr(String txEspInstr) {
        this.txEspInstr = txEspInstr;
    }

    public String getInternado() {
        return internado;
    }

    public void setInternado(String internado) {
        this.internado = internado;
    }

    public Sala getSalaInt() {
        return salaInt;
    }

    public void setSalaInt(Sala salaInt) {
        this.salaInt = salaInt;
    }

    public String getCamaInt() {
        return camaInt;
    }

    public void setCamaInt(String camaInt) {
        this.camaInt = camaInt;
    }

    public boolean getCoordEdit() {
        if (internado.equals("Si")) return false; else return true;
    }

    public List<String> getListInst() {
        return listInst;
    }

    public void setListInst(List<String> listInst) {
        this.listInst = listInst;
    }

    public List<String> getListInsum() {
        return listInsum;
    }

    public void setListInsum(List<String> listInsum) {
        this.listInsum = listInsum;
    }

    public List<String> getListOtrosReq() {
        return listOtrosReq;
    }

    public void setListOtrosReq(List<String> listOtrosReq) {
        this.listOtrosReq = listOtrosReq;
    }

    public String getTxEspInsum() {
        return txEspInsum;
    }

    public void setTxEspInsum(String txEspInsum) {
        this.txEspInsum = txEspInsum;
    }

    public String getTxEspOtrosReq() {
        return txEspOtrosReq;
    }

    public void setTxEspOtrosReq(String txEspOtrosReq) {
        this.txEspOtrosReq = txEspOtrosReq;
    }
}

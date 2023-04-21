package Controller;

import GUI.OrdenCompraDl;
import Model.Objects.*;
import Model.Util.DataBase;
import Model.Util.PersonalSimple;
import java.sql.Date;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JDialog;

public class OrdenProduccion_bckp {

    private boolean isOffset;

    private boolean isColoresPapel;

    private boolean isPapel;

    private boolean isAcabados;

    private boolean isSerigrafia;

    private boolean isDigital;

    private boolean isPrePrensa;

    private boolean preprensa;

    private boolean isCtp;

    private boolean isEspeciales;

    private boolean dif_lamina;

    private boolean cmky;

    private boolean pantone;

    private double sobrante, porcentaje_sobrante;

    private int folio;

    private int pliegos;

    private int finales;

    private int laminas;

    private int tiros;

    private int tinta_a;

    private int tinta_b;

    private int suma_tintas;

    private double papel_a;

    private double papel_b;

    private double extendido_a;

    private double extendido_b;

    private double final_a;

    private double final_b;

    private double costoTrabajo;

    private double costoLamna;

    private double costoTiro;

    private double importeLamina;

    private double importeTiro;

    private double subTotal;

    private double total;

    private ArrayList lista_acabados;

    private ArrayList lista_impresion;

    private ArrayList lista_prensas;

    private ArrayList lista_papel;

    private ArrayList lista_personal;

    private ArrayList lista_tecnicos;

    private ArrayList prov_maquila;

    private ArrayList prov_acabado;

    private ArrayList prov_prensa;

    private ArrayList papeles;

    private ArrayList pantones;

    private static ArrayList acabados;

    private Cliente cliente;

    private Date fecha_actual;

    private Date fecha_inicio;

    private Date fecha_entrega;

    private Date fecha_modificacion;

    private DetalleImpresion otro;

    private Elemento elemento;

    private ImpresionDigital diseño_impresion;

    private String nombre;

    private String status;

    private String observaciones;

    private String tintasEspeciales;

    private ListaAcabados acabadosActivos;

    private Offset offset;

    private OrdenPapel orden_papel;

    private OrdenEntrega orden_entrega;

    private OrdenCompraDl orden_compra;

    private Papel papel;

    private Prensa prensa;

    private PrensaDigital prensa_digital;

    private PersonalSimple tecnico;

    private PersonalSimple responsable;

    private Propietario propietario;

    private Serigrafia serigrafia;

    private Servicios servicio;

    public OrdenProduccion_bckp() {
        papeles = new ArrayList();
        pantones = new ArrayList();
        acabados = new ArrayList();
        papel = new Papel();
        this.obtenerValoresFijos();
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public double getCostoTrabajo() {
        return costoTrabajo;
    }

    public void setCostoTrabajo(double costoTrabajo) {
        this.costoTrabajo = costoTrabajo;
    }

    public void calcularCostoTrabajo() {
    }

    public Servicios getServicio() {
        return servicio;
    }

    public void setServicio(Servicios servicio) {
        this.servicio = servicio;
    }

    public ArrayList getLista_tecnicos() {
        return lista_tecnicos;
    }

    public void setLista_tecnicos(ArrayList lista_tecnicos) {
        this.lista_tecnicos = lista_tecnicos;
    }

    public ArrayList getLista_personal() {
        return lista_personal;
    }

    public void setLista_personal(ArrayList lista_personal) {
        this.lista_personal = lista_personal;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public void obtenerValoresFijos() {
        this.porcentaje_sobrante = DataBase.consultaSobrante();
    }

    public ArrayList obtenerListaPapel() {
        papel.crearListaPapel();
        this.lista_papel = papel.getLista_papel();
        return this.lista_papel;
    }

    public void obtenerPersonal() {
        this.lista_personal = DataBase.consultaPersonalProduccion();
    }

    public void obtenerTecnicos() {
        this.lista_tecnicos = DataBase.consultaPersonalXtipo("Tecnico");
    }

    public void obtenerProveedores(int tipo) {
        switch(tipo) {
            case 0:
                {
                    prov_maquila = new ArrayList();
                    prov_maquila = DataBase.consultaProveedores(2);
                    break;
                }
            case 1:
                {
                    prov_acabado = new ArrayList();
                    prov_acabado = DataBase.consultaProveedores(1);
                    break;
                }
            case 2:
                {
                    prov_prensa = new ArrayList();
                    prov_prensa = DataBase.consultaProveedores(3);
                    break;
                }
        }
    }

    public void obtenerServicios(int clasificacion, int tipo) {
        switch(clasificacion) {
            case 1:
                {
                    this.lista_acabados = DataBase.consultaServicios(1, tipo);
                    break;
                }
            case 2:
                {
                    this.lista_impresion = DataBase.consultaServicios(2, 0);
                    System.out.println("Dentro de obtener servicios, tamaño: " + lista_impresion);
                    break;
                }
            case 3:
                {
                    this.lista_prensas = DataBase.consultaServicios(3, tipo);
                    break;
                }
        }
    }

    public void calculaSobrante() {
        sobrante = 0;
        sobrante = this.porcentaje_sobrante * this.pliegos;
    }

    public void procesarTintas(String tintas) {
        StringTokenizer token = new StringTokenizer(tintas, " X ");
        this.tinta_a = Integer.valueOf(token.nextToken());
        this.tinta_b = Integer.valueOf(token.nextToken());
    }

    public void calculaLaminas() {
        if (tinta_a > 0 && tinta_b == 0) {
            this.laminas = tinta_a;
        } else if (tinta_a > 0 && tinta_b > 0 && tinta_a == tinta_b) {
            if (dif_lamina) {
                this.laminas = tinta_a + tinta_b;
            } else {
                this.laminas = tinta_a;
            }
        } else if (tinta_a > 0 && tinta_b > 0 && tinta_b < tinta_a) {
            this.laminas = tinta_a + tinta_b;
        }
    }

    public void calculaTiros() {
        tiros = 0;
        suma_tintas = 0;
        suma_tintas = tinta_a + tinta_b;
        tiros = pliegos / 1000;
        if ((this.pliegos % 1000) > 0) {
            tiros++;
        }
        tiros = tiros * suma_tintas;
    }

    public double getCostoLamna() {
        return costoLamna;
    }

    public void setCostoLamna(double costoLamna) {
        this.costoLamna = costoLamna;
    }

    public double getCostoTiro() {
        return costoTiro;
    }

    public void setCostoTiro(double costoTiro) {
        this.costoTiro = costoTiro;
    }

    public double getImporteLamina() {
        return importeLamina;
    }

    public void setImporteLamina(double importeLamina) {
        this.importeLamina = importeLamina;
    }

    public double getImporteTiro() {
        return importeTiro;
    }

    public void setImporteTiro(double importeTiro) {
        this.importeTiro = importeTiro;
    }

    public int getFinales() {
        return finales;
    }

    public void setFinales(int finales) {
        this.finales = finales;
    }

    public int getPliegos() {
        return pliegos;
    }

    public void setPliegos(int pliegos) {
        this.pliegos = pliegos;
    }

    public double getPorcentaje_sobrante() {
        return porcentaje_sobrante;
    }

    public void setPorcentaje_sobrante(double porcentaje_sobrante) {
        this.porcentaje_sobrante = porcentaje_sobrante;
    }

    public double getSobrante() {
        return sobrante;
    }

    public void setSobrante(double sobrante) {
        this.sobrante = sobrante;
    }

    public ArrayList getProv_acabado() {
        return prov_acabado;
    }

    public void setProv_acabado(ArrayList prov_acabado) {
        this.prov_acabado = prov_acabado;
    }

    public ArrayList getProv_maquila() {
        return prov_maquila;
    }

    public void setProv_maquila(ArrayList prov_maquila) {
        this.prov_maquila = prov_maquila;
    }

    public ArrayList getProv_prensa() {
        return prov_prensa;
    }

    public void setProv_prensa(ArrayList prov_prensa) {
        this.prov_prensa = prov_prensa;
    }

    public ArrayList getLista_acabados() {
        return lista_acabados;
    }

    public void setLista_acabados(ArrayList lista_acabados) {
        this.lista_acabados = lista_acabados;
    }

    public ArrayList getLista_impresion() {
        return this.lista_impresion;
    }

    public void setLista_impresion(ArrayList lista_impresion) {
        this.lista_impresion = lista_impresion;
    }

    public ArrayList getLista_prensas() {
        return lista_prensas;
    }

    public void setLista_prensas(ArrayList lista_prensas) {
        this.lista_prensas = lista_prensas;
    }

    public int getLaminas() {
        return laminas;
    }

    public void setLaminas(int laminas) {
        this.laminas = laminas;
    }

    public int getTiros() {
        return tiros;
    }

    public void setTiros(int tiros) {
        this.tiros = tiros;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isDif_lamina() {
        return dif_lamina;
    }

    public void setDif_lamina(boolean dif_lamina) {
        this.dif_lamina = dif_lamina;
    }

    public Date getFecha_actual() {
        return fecha_actual;
    }

    public void setFecha_actual(Date fecha_actual) {
        this.fecha_actual = fecha_actual;
    }

    public Date getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(Date fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(Date fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }

    public double getFinal_a() {
        return final_a;
    }

    public void setFinal_a(double final_a) {
        this.final_a = final_a;
    }

    public double getFinal_b() {
        return final_b;
    }

    public void setFinal_b(double final_b) {
        this.final_b = final_b;
    }

    public double getPapel_a() {
        return papel_a;
    }

    public void setPapel_a(double papel_a) {
        this.papel_a = papel_a;
    }

    public double getPapel_b() {
        return papel_b;
    }

    public void setPapel_b(double papel_b) {
        this.papel_b = papel_b;
    }

    public PersonalSimple getResponsable() {
        return responsable;
    }

    public void setResponsable(PersonalSimple responsable) {
        this.responsable = responsable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTinta_a() {
        return tinta_a;
    }

    public void setTinta_a(int tinta_a) {
        this.tinta_a = tinta_a;
    }

    public int getTinta_b() {
        return tinta_b;
    }

    public void setTinta_b(int tinta_b) {
        this.tinta_b = tinta_b;
    }

    public ArrayList getPapeles() {
        return papeles;
    }

    public void setPapeles(ArrayList papeles) {
        this.papeles = papeles;
    }

    public void addPapel(Papel papel) {
        if (papeles.size() > 0) {
            papeles.remove(0);
        }
        this.papeles.add(papel);
    }

    public ArrayList getLista_papel() {
        return lista_papel;
    }

    public void setLista_papel(ArrayList lista_papel) {
        this.lista_papel = lista_papel;
    }

    public boolean isCmky() {
        return cmky;
    }

    public void setCmky(boolean cmky) {
        this.cmky = cmky;
    }

    public ArrayList getPantones() {
        return pantones;
    }

    public void setPantones(ArrayList pantones) {
        this.pantones = pantones;
    }

    public Prensa getPrensa() {
        return prensa;
    }

    public void setPrensa(Prensa prensa) {
        this.prensa = prensa;
    }

    public boolean isPantone() {
        return pantone;
    }

    public void setPantone(boolean pantone) {
        this.pantone = pantone;
    }

    public void addPantone(String pantone) {
        this.pantones.add(pantone);
    }

    public static ArrayList getAcabados() {
        return acabados;
    }

    public void setAcabados(ArrayList a) {
        acabados = a;
    }

    public static void addAcabado(Acabado acabado) {
        acabados.add(acabado);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PersonalSimple getTecnico() {
        return tecnico;
    }

    public void setTecnico(PersonalSimple tecnico) {
        this.tecnico = tecnico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ListaAcabados getAcabadosActivos() {
        return acabadosActivos;
    }

    public void setAcabadosActivos(ListaAcabados acabadosActivos) {
        this.acabadosActivos = acabadosActivos;
    }

    public String getTintasEspeciales() {
        return tintasEspeciales;
    }

    public void setTintasEspeciales(String tintasEspeciales) {
        this.tintasEspeciales = tintasEspeciales;
    }

    public OrdenCompraDl getOrden_compra() {
        return orden_compra;
    }

    public void setOrden_compra(OrdenCompraDl orden_compra) {
        this.orden_compra = orden_compra;
    }

    public void inicializaOrdenCompra(JDialog padre) {
        this.orden_compra = new OrdenCompraDl(padre, true);
    }

    public ImpresionDigital getDiseño_impresion() {
        return diseño_impresion;
    }

    public void setDiseño_impresion(ImpresionDigital diseño_impresion) {
        this.diseño_impresion = diseño_impresion;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    public double getExtendido_a() {
        return extendido_a;
    }

    public void setExtendido_a(double extendido_a) {
        this.extendido_a = extendido_a;
    }

    public double getExtendido_b() {
        return extendido_b;
    }

    public void setExtendido_b(double extendido_b) {
        this.extendido_b = extendido_b;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

    public OrdenEntrega getOrden_entrega() {
        return orden_entrega;
    }

    public void setOrden_entrega(OrdenEntrega orden_entrega) {
        this.orden_entrega = orden_entrega;
    }

    public OrdenPapel getOrden_papel() {
        return orden_papel;
    }

    public void setOrden_papel(OrdenPapel orden_papel) {
        this.orden_papel = orden_papel;
    }

    public DetalleImpresion getOtro() {
        return otro;
    }

    public void setOtro(DetalleImpresion otro) {
        this.otro = otro;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public PrensaDigital getPrensa_digital() {
        return prensa_digital;
    }

    public void setPrensa_digital(PrensaDigital prensa_digital) {
        this.prensa_digital = prensa_digital;
    }

    public Serigrafia getSerigrafia() {
        return serigrafia;
    }

    public void setSerigrafia(Serigrafia serigrafia) {
        this.serigrafia = serigrafia;
    }

    public int getSuma_tintas() {
        return suma_tintas;
    }

    public void setSuma_tintas(int suma_tintas) {
        this.suma_tintas = suma_tintas;
    }

    public boolean isIsAcabados() {
        return isAcabados;
    }

    public void setIsAcabados(boolean isAcabados) {
        this.isAcabados = isAcabados;
    }

    public boolean isIsDigital() {
        return isDigital;
    }

    public void setIsDigital(boolean isDigital) {
        this.isDigital = isDigital;
    }

    public boolean isIsOffset() {
        return isOffset;
    }

    public void setIsOffset(boolean isOffset) {
        this.isOffset = isOffset;
    }

    public boolean isIsPapel() {
        return isPapel;
    }

    public void setIsPapel(boolean isPapel) {
        this.isPapel = isPapel;
    }

    public boolean isIsSerigrafia() {
        return isSerigrafia;
    }

    public void setIsSerigrafia(boolean isSerigrafia) {
        this.isSerigrafia = isSerigrafia;
    }

    public boolean isIsColoresPapel() {
        return isColoresPapel;
    }

    public void setIsColoresPapel(boolean isColoresPapel) {
        this.isColoresPapel = isColoresPapel;
    }

    public boolean isIsPrePrensa() {
        return isPrePrensa;
    }

    public void setIsPrePrensa(boolean isPrePrensa) {
        this.isPrePrensa = isPrePrensa;
    }

    public boolean isPreprensa() {
        return preprensa;
    }

    public void setPreprensa(boolean preprensa) {
        this.preprensa = preprensa;
    }

    public boolean isIsCtp() {
        return isCtp;
    }

    public void setIsCtp(boolean isCtp) {
        this.isCtp = isCtp;
    }

    public boolean isIsEspeciales() {
        return isEspeciales;
    }

    public void setIsEspeciales(boolean isEspeciales) {
        this.isEspeciales = isEspeciales;
    }

    public void calcularCostos() {
        this.costoLamna = DataBase.consultaCostoPrensa(1, this.prensa.getId());
        this.costoTiro = DataBase.consultaCostoPrensa(2, this.prensa.getId());
        this.importeLamina = this.costoLamna * this.laminas;
        this.importeTiro = this.costoTiro * this.tiros;
        this.subTotal = this.importeLamina + this.importeTiro;
        this.total = this.subTotal + (subTotal * 0.16);
    }

    public void calcularFinales() {
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        a = (int) (this.papel_a / this.extendido_a);
        b = (int) (this.papel_b / this.extendido_b);
        c = (int) (this.papel_a / this.extendido_b);
        d = (int) (this.papel_b / this.extendido_a);
        e = a * b;
        f = c * d;
        if (e > f) {
            this.finales = this.pliegos * e;
        } else {
            this.finales = this.pliegos * f;
        }
    }
}

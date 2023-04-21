package domain;

/**
 *
 * @author Sonia
 */
public class Periferico {

    /** Creates a new instance of Periferico */
    public Periferico() {
    }

    private int cod_centro;

    private int cod_periferico;

    private String direccion_centro;

    private String nombre;

    private int responsable;

    private String programa1;

    private String programa2;

    private String programa3;

    private String ciclo1;

    private String ciclo2;

    private String ciclo3;

    private String grado1;

    private String grado2;

    private String grado3;

    private String grado4;

    private String grado5;

    private String grado6;

    private String grado7;

    private String grado8;

    private String forma_atencion1;

    private String forma_atencion2;

    private String forma_atencion3;

    private String cod_centropoblado;

    private String cod_distrito;

    private String cod_provincia;

    private String cod_departamento;

    private String TM_Desde;

    private String TM_Hasta;

    private String TT_Desde;

    private String TT_Hasta;

    private String TN_Desde;

    private String TN_Hasta;

    private String departamento;

    private String provincia;

    private String distrito;

    private String lugar;

    private String nombre_persona;

    public int getCodCentro() {
        return cod_centro;
    }

    public void setCodCentro(int cod_centro) {
        this.cod_centro = cod_centro;
    }

    public int getCodPeriferico() {
        return cod_periferico;
    }

    public void setCodPeriferico(int cod_periferico) {
        this.cod_periferico = cod_periferico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccionCentro() {
        return direccion_centro;
    }

    public void setDireccionCentro(String direccion_centro) {
        this.direccion_centro = direccion_centro;
    }

    public int getResponsable() {
        return responsable;
    }

    public void setResponsable(int responsable) {
        this.responsable = responsable;
    }

    public String getPrograma1() {
        return programa1;
    }

    public void setPrograma1(String programa1) {
        this.programa1 = programa1;
    }

    public String getPrograma2() {
        return programa2;
    }

    public void setPrograma2(String programa2) {
        this.programa2 = programa2;
    }

    public String getPrograma3() {
        return programa3;
    }

    public void setPrograma3(String programa3) {
        this.programa3 = programa3;
    }

    public String getCiclo1() {
        return ciclo1;
    }

    public void setCiclo1(String ciclo1) {
        this.ciclo1 = ciclo1;
    }

    public String getCiclo2() {
        return ciclo2;
    }

    public void setCiclo2(String ciclo2) {
        this.ciclo2 = ciclo2;
    }

    public String getCiclo3() {
        return ciclo3;
    }

    public void setCiclo3(String ciclo3) {
        this.ciclo3 = ciclo3;
    }

    public String getGrado1() {
        return grado1;
    }

    public void setGrado1(String grado1) {
        this.grado1 = grado1;
    }

    public String getGrado2() {
        return grado2;
    }

    public void setGrado2(String grado2) {
        this.grado2 = grado2;
    }

    public String getGrado3() {
        return grado3;
    }

    public void setGrado3(String grado3) {
        this.grado3 = grado3;
    }

    public String getGrado4() {
        return grado4;
    }

    public void setGrado4(String grado4) {
        this.grado4 = grado4;
    }

    public String getGrado5() {
        return grado5;
    }

    public void setGrado5(String grado5) {
        this.grado5 = grado5;
    }

    public String getGrado6() {
        return grado6;
    }

    public void setGrado6(String grado6) {
        this.grado6 = grado6;
    }

    public String getGrado7() {
        return grado7;
    }

    public void setGrado7(String grado7) {
        this.grado7 = grado7;
    }

    public String getGrado8() {
        return grado8;
    }

    public void setGrado8(String grado8) {
        this.grado8 = grado8;
    }

    public String getForma1() {
        return forma_atencion1;
    }

    public void setForma1(String forma_atencion1) {
        this.forma_atencion1 = forma_atencion1;
    }

    public String getForma2() {
        return forma_atencion2;
    }

    public void setForma2(String forma_atencion2) {
        this.forma_atencion2 = forma_atencion2;
    }

    public String getForma3() {
        return forma_atencion3;
    }

    public void setForma3(String forma_atencion3) {
        this.forma_atencion3 = forma_atencion3;
    }

    public String getCodDep() {
        return cod_departamento;
    }

    public void setCodDep(String cod_departamento) {
        this.cod_departamento = cod_departamento;
    }

    public String getCodProv() {
        return cod_provincia;
    }

    public void setCodProv(String cod_provincia) {
        this.cod_provincia = cod_provincia;
    }

    public String getCodDist() {
        return cod_distrito;
    }

    public void setCodDist(String cod_distrito) {
        this.cod_distrito = cod_distrito;
    }

    public String getCodCPoblado() {
        return cod_centropoblado;
    }

    public void setCodCPoblado(String cod_centropoblado) {
        this.cod_centropoblado = cod_centropoblado;
    }

    public String getMananaDesde() {
        return TM_Desde;
    }

    public void setMananaDesde(String TM_Desde) {
        this.TM_Desde = TM_Desde;
    }

    public String getMananaHasta() {
        return TM_Hasta;
    }

    public void setMananaHasta(String TM_Hasta) {
        this.TM_Hasta = TM_Hasta;
    }

    public String getTardeDesde() {
        return TT_Desde;
    }

    public void setTardeDesde(String TT_Desde) {
        this.TT_Desde = TT_Desde;
    }

    public String getTardeHasta() {
        return TT_Hasta;
    }

    public void setTardeHasta(String TT_Hasta) {
        this.TT_Hasta = TT_Hasta;
    }

    public String getNocheDesde() {
        return TN_Desde;
    }

    public void setNocheDesde(String TN_Desde) {
        this.TN_Desde = TN_Desde;
    }

    public String getNocheHasta() {
        return TN_Hasta;
    }

    public void setNocheHasta(String TN_Hasta) {
        this.TN_Hasta = TN_Hasta;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNombrePersona() {
        return nombre_persona;
    }

    public void setNombrePersona(String nombre_persona) {
        this.nombre_persona = nombre_persona;
    }
}

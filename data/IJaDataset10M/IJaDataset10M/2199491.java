package marca;

import foto.FotoUtil;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import utilidad.Util;
import utilidad.clasesBase.*;

/**
 *
 * @author Sergio
 */
public class MarcaVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private String entidad;

    private int idEntidad;

    private String nombre;

    private String logo;

    private String web;

    public MarcaVO() {
        inicializarComponetes();
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public int getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(int idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ImageIcon getIconoLogo() {
        Image img = Toolkit.getDefaultToolkit().getImage(FotoUtil.RutaFotos + logo);
        Image imgR = Util.redimensionarImagen(img, MarcaUtil.ladoMayorImagenListado, MarcaUtil.ladoMayorImagenListado);
        ImageIcon icono = new ImageIcon(imgR);
        return icono;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public void inicializarComponetes() {
        super.inicializarComponetes();
        this.entidad = "";
        this.idEntidad = -1;
        this.nombre = "";
        this.logo = "";
        this.web = "";
    }
}

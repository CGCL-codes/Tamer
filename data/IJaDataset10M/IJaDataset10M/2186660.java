package openfield.persistence.database;

import java.beans.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Properties;
import org.openide.util.Exceptions;

/**
 *
 * @author shader
 */
public class Propiedades implements Serializable {

    private File dbfp;

    private Properties props;

    private String titNifcif;

    private String titNombre;

    private String titDireccion;

    private String titCp;

    private String titProvincia;

    private String titMunicipio;

    private String titLocalidad;

    private String titTelefono;

    private String titFax;

    private String titMovil;

    private String titEmail;

    private String expCodigo;

    private String expNombre;

    private String expDireccion;

    private String expCp;

    private String expMunicipio;

    private String expLocalidad;

    private String expProvincia;

    private Double expCoordUtmX;

    private Double expCoordUtmY;

    private String expDescripcion;

    private Integer conCuentaIvaCompra;

    private Integer conSCuentaIvaCompra;

    private Integer conCuentaIvaVenta;

    private Integer conSCuentaIvaVenta;

    private Integer dbVersion;

    private PropertyChangeSupport propertySupport;

    public Propiedades() {
        propertySupport = new PropertyChangeSupport(this);
        propertySupport.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                save();
            }
        });
    }

    public void setProps(File dbf) {
        this.dbfp = dbf;
        this.expCodigo = dbf.getName();
        load();
    }

    public void save() {
        props.setProperty("titNifcif", titNifcif);
        props.setProperty("titNombre", titNombre);
        props.setProperty("titDireccion", titDireccion);
        props.setProperty("titCp", titCp);
        props.setProperty("titProvincia", titProvincia);
        props.setProperty("titMunicipio", titMunicipio);
        props.setProperty("titLocalidad", titLocalidad);
        props.setProperty("titTelefono", titTelefono);
        props.setProperty("titFax", titFax);
        props.setProperty("titMovil", titMovil);
        props.setProperty("titEmail", titEmail);
        props.setProperty("expNombre", expNombre);
        props.setProperty("expDireccion", expDireccion);
        props.setProperty("expCp", expCp);
        props.setProperty("expMunicipio", expMunicipio);
        props.setProperty("expLocalidad", expLocalidad);
        props.setProperty("expProvincia", expProvincia);
        props.setProperty("expCoordUtmX", expCoordUtmX.toString());
        props.setProperty("expCoordUtmY", expCoordUtmY.toString());
        props.setProperty("expDescripcion", expDescripcion);
        props.setProperty("conCuentaIvaCompra", conCuentaIvaCompra.toString());
        props.setProperty("conSCuentaIvaCompra", conSCuentaIvaCompra.toString());
        props.setProperty("conCuentaIvaVenta", conCuentaIvaVenta.toString());
        props.setProperty("conSCuentaIvaVenta", conSCuentaIvaVenta.toString());
        props.setProperty("dbVersion", dbVersion.toString());
        try {
            OutputStream os = new FileOutputStream(dbfp);
            props.store(os, "propiedades de la bd");
            os.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void load() {
        props = new Properties();
        try {
            InputStream is = new FileInputStream(dbfp);
            props.load(is);
            is.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        titNifcif = getNotNullS(props.getProperty("titNifcif"));
        titNombre = getNotNullS(props.getProperty("titNombre"));
        titDireccion = getNotNullS(props.getProperty("titDireccion"));
        titCp = getNotNullS(props.getProperty("titCp"));
        titProvincia = getNotNullS(props.getProperty("titProvincia"));
        titMunicipio = getNotNullS(props.getProperty("titMunicipio"));
        titLocalidad = getNotNullS(props.getProperty("titLocalidad"));
        titTelefono = getNotNullS(props.getProperty("titTelefono"));
        titFax = getNotNullS(props.getProperty("titFax"));
        titMovil = getNotNullS(props.getProperty("titMovil"));
        titEmail = getNotNullS(props.getProperty("titEmail"));
        expNombre = getNotNullS(props.getProperty("expNombre"));
        expDireccion = getNotNullS(props.getProperty("expDireccion"));
        expCp = getNotNullS(props.getProperty("expCp"));
        expMunicipio = getNotNullS(props.getProperty("expMunicipio"));
        expLocalidad = getNotNullS(props.getProperty("expLocalidad"));
        expProvincia = getNotNullS(props.getProperty("expProvincia"));
        expCoordUtmX = getNotNullD(props.getProperty("expCoordUtmX"));
        expCoordUtmY = getNotNullD(props.getProperty("expCoordUtmY"));
        expDescripcion = getNotNullS(props.getProperty("expDescripcion"));
        conCuentaIvaCompra = getNotNullI(props.getProperty("conCuentaIvaCompra"));
        conSCuentaIvaCompra = getNotNullI(props.getProperty("conSCuentaIvaCompra"));
        conCuentaIvaVenta = getNotNullI(props.getProperty("conCuentaIvaVenta"));
        conSCuentaIvaVenta = getNotNullI(props.getProperty("conSCuentaIvaVenta"));
        dbVersion = getNotNullI(props.getProperty("dbVersion"));
    }

    private static String getNotNullS(String txt) {
        return (txt == null ? new String() : txt);
    }

    private static Double getNotNullD(String txt) {
        if (txt == null || txt.length() == 0) {
            return new Double(0);
        }
        try {
            return new Double(txt);
        } catch (Exception e) {
            return new Double(0);
        }
    }

    private static Integer getNotNullI(String txt) {
        if (txt == null || txt.length() == 0) {
            return new Integer(0);
        }
        try {
            return new Integer(txt);
        } catch (Exception e) {
            return new Integer(0);
        }
    }

    public String getExpCodigo() {
        return expCodigo;
    }

    public void setExpCodigo(String expCodigo) {
        String old = this.expCodigo;
        this.expCodigo = expCodigo;
        propertySupport.firePropertyChange("expCodigo", old, expCodigo);
    }

    public Double getExpCoordUtmX() {
        return expCoordUtmX;
    }

    public void setExpCoordUtmX(Double expCoordUtmX) {
        Double old = this.expCoordUtmX;
        this.expCoordUtmX = expCoordUtmX;
        propertySupport.firePropertyChange("expCoordUtmX", old, expCoordUtmX);
    }

    public Double getExpCoordUtmY() {
        return expCoordUtmY;
    }

    public void setExpCoordUtmY(Double expCoordUtmY) {
        Double old = this.expCoordUtmY;
        this.expCoordUtmY = expCoordUtmY;
        propertySupport.firePropertyChange("expCoordUtmY", old, expCoordUtmY);
    }

    public String getExpCp() {
        return expCp;
    }

    public void setExpCp(String expCp) {
        String old = this.expCp;
        this.expCp = expCp;
        propertySupport.firePropertyChange("expCp", old, expCp);
    }

    public String getExpDescripcion() {
        return expDescripcion;
    }

    public void setExpDescripcion(String expDescripcion) {
        String old = this.expDescripcion;
        this.expDescripcion = expDescripcion;
        propertySupport.firePropertyChange("expDescripcion", old, expDescripcion);
    }

    public String getExpDireccion() {
        return expDireccion;
    }

    public void setExpDireccion(String expDireccion) {
        String old = this.expDireccion;
        this.expDireccion = expDireccion;
        propertySupport.firePropertyChange("expDireccion", old, expDireccion);
    }

    public String getExpLocalidad() {
        return expLocalidad;
    }

    public void setExpLocalidad(String expLocalidad) {
        String old = this.expLocalidad;
        this.expLocalidad = expLocalidad;
        propertySupport.firePropertyChange("expLocalidad", old, expLocalidad);
    }

    public String getExpMunicipio() {
        return expMunicipio;
    }

    public void setExpMunicipio(String expMunicipio) {
        String old = this.expMunicipio;
        this.expMunicipio = expMunicipio;
        propertySupport.firePropertyChange("expMunicipio", old, expMunicipio);
    }

    public String getExpNombre() {
        return expNombre;
    }

    public void setExpNombre(String expNombre) {
        String old = this.expNombre;
        this.expNombre = expNombre;
        propertySupport.firePropertyChange("expNombre", old, expNombre);
    }

    public String getExpProvincia() {
        return expProvincia;
    }

    public void setExpProvincia(String expProvincia) {
        String old = this.expProvincia;
        this.expProvincia = expProvincia;
        propertySupport.firePropertyChange("expProvincia", old, expProvincia);
    }

    public String getTitCp() {
        return titCp;
    }

    public void setTitCp(String titCp) {
        String old = this.titCp;
        this.titCp = titCp;
        propertySupport.firePropertyChange("titCp", old, titCp);
    }

    public String getTitDireccion() {
        return titDireccion;
    }

    public void setTitDireccion(String titDireccion) {
        String old = this.titDireccion;
        this.titDireccion = titDireccion;
        propertySupport.firePropertyChange("titDireccion", old, titDireccion);
    }

    public String getTitEmail() {
        return titEmail;
    }

    public void setTitEmail(String titEmail) {
        String old = this.titEmail;
        this.titEmail = titEmail;
        propertySupport.firePropertyChange("titEmail", old, titEmail);
    }

    public String getTitFax() {
        return titFax;
    }

    public void setTitFax(String titFax) {
        String old = this.titFax;
        this.titFax = titFax;
        propertySupport.firePropertyChange("titFax", old, titFax);
    }

    public String getTitLocalidad() {
        return titLocalidad;
    }

    public void setTitLocalidad(String titLocalidad) {
        String old = this.titLocalidad;
        this.titLocalidad = titLocalidad;
        propertySupport.firePropertyChange("titLocalidad", old, titLocalidad);
    }

    public String getTitMovil() {
        return titMovil;
    }

    public void setTitMovil(String titMovil) {
        String old = this.titMovil;
        this.titMovil = titMovil;
        propertySupport.firePropertyChange("titMovil", old, titMovil);
    }

    public String getTitMunicipio() {
        return titMunicipio;
    }

    public void setTitMunicipio(String titMunicipio) {
        String old = this.titMunicipio;
        this.titMunicipio = titMunicipio;
        propertySupport.firePropertyChange("titMunicipio", old, titMunicipio);
    }

    public String getTitNifcif() {
        return titNifcif;
    }

    public void setTitNifcif(String titNifcif) {
        String old = this.titNifcif;
        this.titNifcif = titNifcif;
        propertySupport.firePropertyChange("titNifcif", old, titNifcif);
    }

    public String getTitNombre() {
        return titNombre;
    }

    public void setTitNombre(String titNombre) {
        String old = this.titNombre;
        this.titNombre = titNombre;
        propertySupport.firePropertyChange("titNombre", old, titNombre);
    }

    public String getTitProvincia() {
        return titProvincia;
    }

    public void setTitProvincia(String titProvincia) {
        String old = this.titProvincia;
        this.titProvincia = titProvincia;
        propertySupport.firePropertyChange("titProvincia", old, titProvincia);
    }

    public String getTitTelefono() {
        return titTelefono;
    }

    public void setTitTelefono(String titTelefono) {
        String old = this.titTelefono;
        this.titTelefono = titTelefono;
        propertySupport.firePropertyChange("titTelefono", old, titTelefono);
    }

    public Integer getConSCuentaIvaCompra() {
        return conSCuentaIvaCompra;
    }

    public void setConSCuentaIvaCompra(Integer conSCuentaIvaCompra) {
        Integer oldConSCuentaIvaCompra = this.conSCuentaIvaCompra;
        this.conSCuentaIvaCompra = conSCuentaIvaCompra;
        propertySupport.firePropertyChange("conSCuentaIvaCompra", oldConSCuentaIvaCompra, conSCuentaIvaCompra);
    }

    public Integer getConCuentaIvaCompra() {
        return conCuentaIvaCompra;
    }

    public void setConCuentaIvaCompra(Integer conCuentaIvaCompra) {
        Integer oldConCuentaIvaCompra = this.conCuentaIvaCompra;
        this.conCuentaIvaCompra = conCuentaIvaCompra;
        propertySupport.firePropertyChange("conCuentaIvaCompra", oldConCuentaIvaCompra, conCuentaIvaCompra);
    }

    public Integer getConSCuentaIvaVenta() {
        return conSCuentaIvaVenta;
    }

    public void setConSCuentaIvaVenta(Integer conSCuentaIvaVenta) {
        Integer oldConSCuentaIvaVenta = this.conSCuentaIvaVenta;
        this.conSCuentaIvaVenta = conSCuentaIvaVenta;
        propertySupport.firePropertyChange("conSCuentaIvaVenta", oldConSCuentaIvaVenta, conSCuentaIvaVenta);
    }

    public Integer getConCuentaIvaVenta() {
        return conCuentaIvaVenta;
    }

    public void setConCuentaIvaVenta(Integer conCuentaIvaVenta) {
        Integer oldConCuentaIvaVenta = this.conCuentaIvaVenta;
        this.conCuentaIvaVenta = conCuentaIvaVenta;
        propertySupport.firePropertyChange("conCuentaIvaVenta", oldConCuentaIvaVenta, conCuentaIvaVenta);
    }

    public Integer getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(Integer dbVersion) {
        Integer oldDbVersion = this.dbVersion;
        this.dbVersion = dbVersion;
        propertySupport.firePropertyChange("dbVersion", oldDbVersion, dbVersion);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}

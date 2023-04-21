package basededatostg;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "ComponentesRed.findAll", query = "select o from ComponentesRed o") })
@Table(name = "COMPONENTES_RED")
public class ComponentesRed implements Serializable {

    private String bluetooth;

    @Column(name = "CONX_ALAMBRICA")
    private String conxAlambrica;

    @Id
    @Column(name = "ID_COMPRED", nullable = false)
    private Long idCompred;

    @Column(name = "RED_2_5G")
    private String red25g;

    @Column(name = "RED_3G")
    private String red3g;

    private String wap;

    private String wifi;

    @ManyToOne
    @JoinColumn(name = "ID_DISPOSITIVO")
    private Dispositivo dispositivo;

    public ComponentesRed() {
    }

    public ComponentesRed(String bluetooth, String conxAlambrica, Long idCompred, Dispositivo dispositivo, String red25g, String red3g, String wap, String wifi) {
        this.bluetooth = bluetooth;
        this.conxAlambrica = conxAlambrica;
        this.idCompred = idCompred;
        this.dispositivo = dispositivo;
        this.red25g = red25g;
        this.red3g = red3g;
        this.wap = wap;
        this.wifi = wifi;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getConxAlambrica() {
        return conxAlambrica;
    }

    public void setConxAlambrica(String conxAlambrica) {
        this.conxAlambrica = conxAlambrica;
    }

    public Long getIdCompred() {
        return idCompred;
    }

    public void setIdCompred(Long idCompred) {
        this.idCompred = idCompred;
    }

    public String getRed25g() {
        return red25g;
    }

    public void setRed25g(String red25g) {
        this.red25g = red25g;
    }

    public String getRed3g() {
        return red3g;
    }

    public void setRed3g(String red3g) {
        this.red3g = red3g;
    }

    public String getWap() {
        return wap;
    }

    public void setWap(String wap) {
        this.wap = wap;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }
}

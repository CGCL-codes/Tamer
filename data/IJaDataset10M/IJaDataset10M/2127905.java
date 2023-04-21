package org.opensih.vaq.Actions;

import java.util.Date;
import java.util.List;
import org.opensih.vaq.Modelo.HistorialPuntos;

public interface IHistorialValorPuntos {

    public void nuevo();

    public void destroy();

    public void guardar();

    public List<HistorialPuntos> getValores();

    public void setValores(List<HistorialPuntos> valores);

    public double getValor();

    public void setValor(double valor);

    public Date getFecha();

    public void setFecha(Date fecha);
}

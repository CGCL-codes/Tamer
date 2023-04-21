package ar.com.tifad.domainmodel.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import ar.com.tifad.domainmodel.entities.contracts.IContrato;
import ar.com.tifad.domainmodel.entities.contracts.IDespachoGasOede;
import ar.com.tifad.domainmodel.entities.contracts.ITransporte;
import ar.com.tifad.domainmodel.entities.contracts.ITransportista;
import ar.com.tifad.domainmodel.entities.contracts.IVolumenTipificado;

/**
 * Representa el despacho realizado desde la OEDE para un d�a de gas. No se
 * persisten todas las versiones que puedan hacerse, dado que s�lo tienen un
 * valor de auditor�a, que es persistido en los aspectos correspondientes.
 * @author Ricardo
 *
 */
public class DespachoGasOede implements Serializable, IDespachoGasOede {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8649166081343528970L;

    private int id;

    private Date fechaGas;

    private IContrato contrato;

    private ITransportista transportista;

    private Map<ITransporte, IVolumenTipificado> transportes;

    public DespachoGasOede() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Date getFechaGas() {
        return fechaGas;
    }

    @Override
    public void setFechaGas(Date fechaGas) {
        this.fechaGas = fechaGas;
    }

    @Override
    public IContrato getContrato() {
        return contrato;
    }

    @Override
    public void setContrato(IContrato contrato) {
        this.contrato = contrato;
    }

    @Override
    public ITransportista getTransportista() {
        return transportista;
    }

    @Override
    public void setTransportista(ITransportista transportista) {
        this.transportista = transportista;
    }

    @Override
    public Map<ITransporte, IVolumenTipificado> getTransportes() {
        return transportes;
    }

    @Override
    public void setTransportes(Map<ITransporte, IVolumenTipificado> transportes) {
        this.transportes = transportes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contrato == null) ? 0 : contrato.hashCode());
        result = prime * result + ((fechaGas == null) ? 0 : fechaGas.hashCode());
        result = prime * result + id;
        result = prime * result + ((transportes == null) ? 0 : transportes.hashCode());
        result = prime * result + ((transportista == null) ? 0 : transportista.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DespachoGasOede other = (DespachoGasOede) obj;
        if (contrato == null) {
            if (other.contrato != null) return false;
        } else if (!contrato.equals(other.contrato)) return false;
        if (fechaGas == null) {
            if (other.fechaGas != null) return false;
        } else if (!fechaGas.equals(other.fechaGas)) return false;
        if (id != other.id) return false;
        if (transportes == null) {
            if (other.transportes != null) return false;
        } else if (!transportes.equals(other.transportes)) return false;
        if (transportista == null) {
            if (other.transportista != null) return false;
        } else if (!transportista.equals(other.transportista)) return false;
        return true;
    }
}

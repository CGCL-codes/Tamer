package com.inout.ejb;

import com.inout.dto.cierreDTO;
import com.inout.entities.Cierre;
import java.util.Date;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author pablo
 */
@Remote
public interface cierreLocal {

    public Cierre convertirDTOCierre(cierreDTO CierreDTO);

    public cierreDTO convertirCierreDTO(Cierre cierre);

    public Boolean cerrarMes(Date desde, Date hasta);
}

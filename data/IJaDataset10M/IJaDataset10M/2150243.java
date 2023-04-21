package edu.ftn.ais.service;

import edu.ftn.ais.service.GenericManager;
import edu.ftn.ais.model.Faktura;
import java.util.List;
import javax.jws.WebService;

@WebService
public interface FakturaManager extends GenericManager<Faktura, Long> {
}

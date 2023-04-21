package it.blueocean.acanto.service;

import java.util.List;
import it.blueocean.acanto.model.Gruppo;

public interface GruppoService {

    public void store(Gruppo gruppo);

    public List<Gruppo> extractAll();

    public Gruppo extractById(String id);

    public List<Gruppo> paginatedList(int start, int limit);
}

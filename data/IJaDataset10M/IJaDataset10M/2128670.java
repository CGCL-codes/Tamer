package fr.aston.gestionrh.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletContext;
import fr.aston.gestionrh.dto.ListCongesByEmployeDTO;
import fr.aston.gestionrh.integration.FactoryIntegrationGestionConges;
import fr.aston.gestionrh.integration.IGestionConges;

public class ServiceGestionRh {

    public List<ListCongesByEmployeDTO> getCongesEmployesXML() {
        System.out.println("CommandeListerDemandeConges");
        IGestionConges gestion = FactoryIntegrationGestionConges.getGestionConges();
        File file = new File("c:/RepertoireEchange/Conges_transform.xml");
        List<ListCongesByEmployeDTO> listListCongesByEmployeDTO = gestion.obtenirListeConges(file);
        return listListCongesByEmployeDTO;
    }
}

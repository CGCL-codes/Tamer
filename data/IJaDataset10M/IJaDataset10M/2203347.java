package edu.unibi.agbi.webservice.client.service.dawismd.misc;

import java.util.ArrayList;
import edu.unibi.agbi.webservice.client.generated.HibernateWebServiceStub.HQuery;
import edu.unibi.agbi.webservice.client.service.AbstractQuery;
import edu.unibi.agbi.webservice.client.service.dawismd.objects.Data;
import edu.unibi.agbi.webservice.client.service.dawismd.objects.DataSource;
import edu.unibi.agbi.webservice.client.service.dawismd.objects.Domains;
import edu.unibi.agbi.webservice.client.service.dawismd.objects.Functions;
import edu.unibi.agbi.webservice.client.service.xml.ResourceLibrary;

/**
 * @author Benjamin Kormeier
 * @version 1.0 26.10.2011
 */
public class KeggGenome extends AbstractQuery implements Functions {

    public ArrayList<Data> disease(String id) {
        HQuery id_query = ResourceLibrary.getDAWISMD_Resource("kegg.genome.disease");
        id_query = setQueryParameter(id_query, 1, id);
        return search(id_query, DataSource.KEGG, Domains.Disease);
    }

    @Override
    public ArrayList<Data> all(String id) {
        return disease(id);
    }

    @Override
    public String name(String id) {
        return name(id, "name.kegg.genome");
    }
}

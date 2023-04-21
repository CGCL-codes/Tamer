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
public class KeggReactantPair extends AbstractQuery implements Functions {

    public ArrayList<Data> compound(String id) {
        HQuery id_query = ResourceLibrary.getDAWISMD_Resource("kegg.rpair.compound");
        id_query = setQueryParameter(id_query, 1, id);
        return search(id_query, DataSource.KEGG, Domains.Compound);
    }

    public ArrayList<Data> reaction(String id) {
        HQuery id_query = ResourceLibrary.getDAWISMD_Resource("kegg.rpair.reaction");
        id_query = setQueryParameter(id_query, 1, id);
        return search(id_query, DataSource.KEGG, Domains.Reaction);
    }

    public ArrayList<Data> reactantPair(String id) {
        HQuery id_query = ResourceLibrary.getDAWISMD_Resource("kegg.rpair.rpair");
        id_query = setQueryParameter(id_query, 1, id);
        return search(id_query, DataSource.KEGG, Domains.Reactant_Pair);
    }

    @Override
    public ArrayList<Data> all(String id) {
        ArrayList<Data> data = new ArrayList<Data>();
        data.addAll(reaction(id));
        data.addAll(compound(id));
        data.addAll(reactantPair(id));
        return data;
    }

    @Override
    public String name(String id) {
        return name(id, "name.kegg.rpair");
    }
}

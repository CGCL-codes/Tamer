package edu.unibi.agbi.webservice.client.service.dawismd.gene;

import java.util.ArrayList;
import java.util.HashSet;
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
public class TransfacGene extends AbstractQuery implements Functions {

    private HashSet<Data> set;

    public ArrayList<Data> enzyme(String id) {
        HQuery hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.dblinks");
        hquery = setQueryParameter(hquery, 1, id);
        hquery = setQueryParameter(hquery, 2, "BRENDA");
        return search(hquery, DataSource.KEGG, Domains.Enzyme);
    }

    public ArrayList<Data> disease(String id) {
        HQuery hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.dblinks");
        hquery = setQueryParameter(hquery, 1, id);
        hquery = setQueryParameter(hquery, 2, "OMIM");
        return search(hquery, DataSource.OMIM, Domains.Disease);
    }

    public ArrayList<Data> gene(String id) {
        set = new HashSet<Data>();
        HQuery hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.dblinks");
        hquery = setQueryParameter(hquery, 1, id);
        hquery = setQueryParameter(hquery, 2, "TRANSPATH");
        set.addAll(search(hquery, DataSource.Transpath, Domains.Gene));
        hquery = setQueryParameter(hquery, 2, "EMBL");
        set.addAll(search(hquery, DataSource.EMBL_Bank, Domains.Gene));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.host");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Gene));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.intronic");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Gene));
        return new ArrayList<Data>(set);
    }

    public ArrayList<Data> transcriptionFactor(String id) {
        set = new HashSet<Data>();
        HQuery hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.factor.binding");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.factor.encoded");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.factor.bs");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.gene.factor.bs.site");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        return new ArrayList<Data>(set);
    }

    @Override
    public ArrayList<Data> all(String id) {
        ArrayList<Data> data = new ArrayList<Data>();
        data.addAll(transcriptionFactor(id));
        data.addAll(enzyme(id));
        data.addAll(gene(id));
        data.addAll(disease(id));
        data.addAll(enzyme(id));
        return data;
    }

    @Override
    public String name(String id) {
        return name(id, "name.tf.gene");
    }
}

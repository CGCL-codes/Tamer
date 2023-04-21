package edu.unibi.agbi.webservice.client.service.dawismd.tf;

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
 * @version 1.0 28.10.2011
 */
public class TransfacTranscriptionFactor extends AbstractQuery implements Functions {

    private HashSet<Data> set;

    public ArrayList<Data> compound(String id) {
        HQuery hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.dblinks");
        hquery = setQueryParameter(hquery, 1, id);
        hquery = setQueryParameter(hquery, 2, "TRANSPATH");
        return search(hquery, DataSource.Transpath, Domains.Compound);
    }

    public ArrayList<Data> gene(String id) {
        set = new HashSet<Data>();
        HQuery hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.dblinks");
        hquery = setQueryParameter(hquery, 1, id);
        hquery = setQueryParameter(hquery, 2, "EMBL");
        set.addAll(search(hquery, DataSource.EMBL_Bank, Domains.Gene));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.gene");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Gene));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.gene.bs");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Gene));
        return new ArrayList<Data>(set);
    }

    public ArrayList<Data> protein(String id) {
        HQuery hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.protein");
        hquery = setQueryParameter(hquery, 1, id);
        return search(hquery, DataSource.UniProt, Domains.Protein);
    }

    public ArrayList<Data> transcriptionFactor(String id) {
        set = new HashSet<Data>();
        HQuery hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.subfamaly");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.superfamaly");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.region");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.bs");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.matrix");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.subunit");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.factor");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.Transfac, Domains.Transcription_Factor));
        hquery = ResourceLibrary.getDAWISMD_Resource("tf.tf.jaspar");
        hquery = setQueryParameter(hquery, 1, id);
        set.addAll(search(hquery, DataSource.JASPAR, Domains.Transcription_Factor));
        return new ArrayList<Data>(set);
    }

    @Override
    public ArrayList<Data> all(String id) {
        ArrayList<Data> data = new ArrayList<Data>();
        data.addAll(compound(id));
        data.addAll(protein(id));
        data.addAll(gene(id));
        data.addAll(transcriptionFactor(id));
        return data;
    }

    @Override
    public String name(String id) {
        return name(id, "name.tf.tf");
    }
}

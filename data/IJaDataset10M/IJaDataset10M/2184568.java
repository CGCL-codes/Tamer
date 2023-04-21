package org.tripcom.ws.discover;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.wsmo.common.TopEntity;
import org.wsmo.factory.Factory;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.Serializer;
import com.ontotext.ordi.wsmo4rdf.WSMLTripleParser;

public class RDF2WSML {

    public void process(URI rdfURI, URI goal) {
        File file = new File(rdfURI.toString());
        if (file.exists() == false) {
            System.out.println("The specified file name %s does not exist!");
            System.exit(1);
        }
        Map<String, Object> createParams = new HashMap<String, Object>();
        createParams.put(org.wsmo.factory.Factory.PROVIDER_CLASS, WSMLTripleParser.class.getName());
        Parser wsmlrdfParser = Factory.createParser(createParams);
        TopEntity[] topEntities = null;
        try {
            topEntities = wsmlrdfParser.parse(new FileReader(file));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not processes the input file %s!", file.getAbsolutePath()), e);
        }
        File outputFile = null;
        FileWriter writer = null;
        try {
            outputFile = new File(goal.toString());
            writer = new FileWriter(outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the output file!", e);
        }
        createParams = new HashMap<String, Object>();
        createParams.put(org.wsmo.factory.Factory.PROVIDER_CLASS, "org.deri.wsmo4j.io.serializer.wsml.SerializerImpl");
        Serializer serializer = Factory.createSerializer(createParams);
        try {
            serializer.serialize(topEntities, writer);
        } catch (IOException e) {
            throw new RuntimeException("Could not serialize to WSML!", e);
        }
        System.out.println(String.format("Output has beed saved in %s!", outputFile.getAbsolutePath()));
    }
}

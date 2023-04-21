package it.polito.semreview.enrichment.keyphrasesextraction.opencalais;

public interface Calais extends javax.xml.rpc.Service {

    public java.lang.String getcalaisSoapAddress();

    public CalaisSoap getcalaisSoap() throws javax.xml.rpc.ServiceException;

    public CalaisSoap getcalaisSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

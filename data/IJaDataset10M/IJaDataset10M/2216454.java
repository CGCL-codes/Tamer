package de.offis.example_applications.transformation4u_client;

public interface Transformation4UService extends javax.xml.rpc.Service {

    public java.lang.String getTransformation4UAddress();

    public de.offis.example_applications.transformation4u_client.Transformation4U getTransformation4U() throws javax.xml.rpc.ServiceException;

    public de.offis.example_applications.transformation4u_client.Transformation4U getTransformation4U(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

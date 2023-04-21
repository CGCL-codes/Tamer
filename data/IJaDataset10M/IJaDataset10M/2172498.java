package org.opensih.Test.webservices.ejb;

import java.util.List;

public interface InterfaceInvocadorMigr {

    public List<String> docClins(int i, int j);

    public String doc(String doc);

    public List<String> plantillas();

    public List<String> jefes();

    public List<String> usuarios();

    public List<String> tecnicos();

    public List<String> servicios();

    public List<String> salas();

    public String controlUE();
}

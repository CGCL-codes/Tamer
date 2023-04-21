package org.opensih.servicioJMX.services;

public class FieldConstants2 implements ServiceTwoManagement {

    String IP_RPHM = "localhost";

    String Ldap_Host = "localhost";

    String Ldap_Password = "opensih";

    String MsjAviso_str = "";

    Boolean MsjAviso_bool = false;

    String Dir_Lucene = "";

    public String getIP_RPHM() {
        return IP_RPHM;
    }

    public void setIP_RPHM(String ip_servidor) {
        IP_RPHM = ip_servidor;
    }

    public String getLdap_Host() {
        return Ldap_Host;
    }

    public void setLdap_Host(String ldapHost) {
        this.Ldap_Host = ldapHost;
    }

    public String getLdap_Password() {
        return Ldap_Password;
    }

    public void setLdap_Password(String password) {
        this.Ldap_Password = password;
    }

    public String getMsjAviso_str() {
        return MsjAviso_str;
    }

    public void setMsjAviso_str(String msjAviso_str) {
        this.MsjAviso_str = msjAviso_str;
    }

    public Boolean isMsjAviso_bool() {
        return MsjAviso_bool;
    }

    public void setMsjAviso_bool(Boolean msjAviso_bool) {
        this.MsjAviso_bool = msjAviso_bool;
    }

    public String getDir_Lucene() {
        return Dir_Lucene;
    }

    public void setDir_Lucene(String dirLucene) {
        Dir_Lucene = dirLucene;
    }

    public void create() throws Exception {
        System.out.println("ServiceTwo - Creating");
    }

    public void start() throws Exception {
        System.out.println("ServiceTwo - Starting");
    }

    public void stop() {
        System.out.println("ServiceTwo - Stopping");
    }

    public void destroy() {
        System.out.println("ServiceTwo - Destroying");
    }
}

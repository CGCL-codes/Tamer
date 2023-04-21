package com.oeaide.persistence;

public class HQLUtil {

    public static String getAlumnosByName(String nombre) {
        String query = "select per from com.oeaide.persistence.S012Persona as per " + "join per.s014Tipopersona as tipo " + "where tipo.s014TipoPersona = 'ALUMNO' " + "and per.s012NombresPersona like '%" + nombre + "%'";
        return query;
    }

    public static String getAlumnosByLastName(String apaterno) {
        String query = "select per from com.oeaide.persistence.S012Persona as per " + "join per.s014Tipopersona as tipo " + "where tipo.s014TipoPersona = 'ALUMNO' " + "and per.s012ApellidoPaternoPersona like '%" + apaterno + "%'";
        return query;
    }

    public static String getAlumnosByMothersLastName(String amaterno) {
        String query = "select per from com.oeaide.persistence.S012Persona as per " + "join per.s014Tipopersona as tipo " + "where tipo.s014TipoPersona = 'ALUMNO' " + "and per.s012ApellidoMaternoPersona like '%" + amaterno + "%'";
        return query;
    }
}

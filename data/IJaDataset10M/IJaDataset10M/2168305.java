package org.digitall.apps.gaia.relevamientos.comercial_2009.clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.digitall.lib.sql.LibSQL;

public class ClassCensoComercial2009Seccion1 {

    private int idEncuestaComercial = -1;

    private String f1a = "";

    private String f1b = "";

    private String f1c = "";

    private String f1d = "";

    private String f1e = "";

    private String f1f = "";

    private String f1g = "";

    private String f1h = "";

    private String f1i = "";

    private String f1j = "";

    private String f1k = "";

    private String f1l = "";

    private String estado = "";

    private double x = 0;

    private double y = 0;

    public ClassCensoComercial2009Seccion1() {
    }

    public ClassCensoComercial2009Seccion1(int idEncuestaComercial) {
        this.idEncuestaComercial = idEncuestaComercial;
    }

    public void setIdEncuestaComercial(int idEncuestaComercial) {
        this.idEncuestaComercial = idEncuestaComercial;
    }

    public int getIdEncuestaComercial() {
        return idEncuestaComercial;
    }

    public void setF1a(String f1a) {
        this.f1a = f1a;
    }

    public String getF1a() {
        return f1a;
    }

    public void setF1b(String f1b) {
        this.f1b = f1b;
    }

    public String getF1b() {
        return f1b;
    }

    public void setF1c(String f1c) {
        this.f1c = f1c;
    }

    public String getF1c() {
        return f1c;
    }

    public void setF1d(String f1d) {
        this.f1d = f1d;
    }

    public String getF1d() {
        return f1d;
    }

    public void setF1e(String f1e) {
        this.f1e = f1e;
    }

    public String getF1e() {
        return f1e;
    }

    public void setF1f(String f1f) {
        this.f1f = f1f;
    }

    public String getF1f() {
        return f1f;
    }

    public void setF1g(String f1g) {
        this.f1g = f1g;
    }

    public String getF1g() {
        return f1g;
    }

    public void setF1h(String f1h) {
        this.f1h = f1h;
    }

    public String getF1h() {
        return f1h;
    }

    public void setF1i(String f1i) {
        this.f1i = f1i;
    }

    public String getF1i() {
        return f1i;
    }

    public void setF1j(String f1j) {
        this.f1j = f1j;
    }

    public String getF1j() {
        return f1j;
    }

    public void setF1k(String f1k) {
        this.f1k = f1k;
    }

    public String getF1k() {
        return f1k;
    }

    public void setF1l(String f1l) {
        this.f1l = f1l;
    }

    public String getF1l() {
        return f1l;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public int saveData() {
        int result = -1;
        String params = "";
        if (idEncuestaComercial == -1) {
            params = "";
            result = LibSQL.getInt("gea_salta.addencuestacomercial2009seccion1", params);
        } else if (idEncuestaComercial > 0) {
            params = "";
            result = LibSQL.getInt("gea_salta.addencuestacomercial2009seccion1", params);
        }
        return result;
    }

    public void retrieveData() {
        ResultSet result = LibSQL.exFunction("gea_salta.getseccion1censocomercial", idEncuestaComercial);
        try {
            if (result.next()) {
                setIdEncuestaComercial(result.getInt("idencuestacomercial"));
                setF1a(result.getString("f1a"));
                setF1b(result.getString("f1b"));
                setF1c(result.getString("f1c"));
                setF1d(result.getString("f1d"));
                setF1e(result.getString("f1e"));
                setF1f(result.getString("f1f"));
                setF1g(result.getString("f1g"));
                setF1h(result.getString("f1h"));
                setF1i(result.getString("f1i"));
                setF1j(result.getString("f1j"));
                setF1k(result.getString("f1k"));
                setF1l(result.getString("f1l"));
                setEstado(result.getString("estado"));
                setX(result.getDouble("x"));
                setY(result.getDouble("y"));
            }
        } catch (SQLException e) {
            System.out.println("error");
        } catch (NullPointerException e) {
        }
    }
}

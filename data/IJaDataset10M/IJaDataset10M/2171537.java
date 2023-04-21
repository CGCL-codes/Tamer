package verisoft.BL;

import java.util.ArrayList;
import java.util.List;
import verisoft.BE.Provincia;
import verisoft.BE.Region;
import verisoft.DA.DistritoDA;
import verisoft.DA.ProvinciaDA;

/**
 *
 * @author tany
 */
public class ProvinciaBL {

    public ArrayList<Provincia> listAll() {
        return (ArrayList<Provincia>) new ProvinciaDA().findAll();
    }

    public Provincia findByName(String name) {
        List<String> ident = new ArrayList<String>();
        List<String> criterios = new ArrayList<String>();
        List<Object> valores = new ArrayList<Object>();
        criterios.add("nombre = ");
        valores.add(name);
        char c = 'a';
        for (int i = 0; i < criterios.size(); i++) {
            criterios.set(i, criterios.get(i) + (":" + c));
            ident.add(String.valueOf(c));
            c++;
        }
        return new ProvinciaDA().findByProperties(ident, criterios, valores).get(0);
    }

    public Provincia nuevaProvincia(String nombre, Region region) {
        try {
            Provincia tipo = new Provincia();
            tipo.setNombre(nombre);
            tipo.setRegion(region);
            return tipo;
        } catch (Exception ex) {
            return null;
        }
    }

    public void guardarProvincia(Provincia tipo) {
        System.out.println(tipo.getNombre());
        new ProvinciaDA().save(tipo);
    }

    public List<Provincia> buscarProvincias() {
        return new ProvinciaDA().findAll();
    }
}

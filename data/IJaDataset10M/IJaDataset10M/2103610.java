package SkeletonDispatcher;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import Objetos.Objeto;

public class Skeleton {

    private String alias;

    private Object objeto;

    private Class clase;

    private Method metodos[];

    private Vector parametros;

    private String Retorno;

    private List<String> tipoParametros;

    public Skeleton(Object objeto, String alias) {
        this.objeto = objeto;
        this.alias = alias;
        clase = this.objeto.getClass();
        tipoParametros = new ArrayList<String>();
        parametros = new Vector();
    }

    public Object instanciarMetodo(Objeto obj, String alias) {
        boolean flag = false;
        Object respuesta = null;
        metodos = clase.getDeclaredMethods();
        tipoParametros = new ArrayList<String>();
        parametros = new Vector();
        Retorno = new String();
        try {
            for (int i = 0; i < metodos.length; i++) {
                if (metodos[i].getName().equals(obj.getMetodo()) && this.getParametros(metodos[i]).size() == obj.getParametros().size()) {
                    flag = true;
                    Retorno = metodos[i].getReturnType().getSimpleName();
                    tipoParametros = this.getParametros(metodos[i]);
                    for (int j = 0; j < tipoParametros.size(); j++) {
                        if (tipoParametros.get(j).equals("int")) parametros.add(Integer.parseInt(obj.getParametros().get(j).toString()));
                        if (tipoParametros.get(j).equals("float")) parametros.add(Double.valueOf(obj.getParametros().get(j).toString()));
                        if (tipoParametros.get(j).equals("String")) parametros.add(obj.getParametros().get(j).toString());
                    }
                    if (!Retorno.equals("void")) respuesta = metodos[i].invoke(objeto, parametros.toArray()); else {
                        metodos[i].invoke(objeto, parametros.toArray());
                        respuesta = (Object) "Metodo ejecutado correctamente";
                    }
                }
            }
            if (!flag) respuesta = (Object) "El metodo invocado no se encuentra implementado";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    public List<String> getParametros(Method m) {
        List<String> res = new ArrayList<String>();
        Class<?>[] parametros = m.getParameterTypes();
        for (int i = 0; i < parametros.length; i++) res.add(parametros[i].getSimpleName());
        return res;
    }

    public String getAlias() {
        return alias;
    }
}

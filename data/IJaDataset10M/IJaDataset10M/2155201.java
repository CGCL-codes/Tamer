package org.ibit.rol.form.persistence.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.ibit.rol.form.model.Campo;
import org.ibit.rol.form.model.CheckBox;
import org.ibit.rol.form.model.ComboBox;
import org.ibit.rol.form.model.Formulario;
import org.ibit.rol.form.model.ListBox;
import org.ibit.rol.form.model.ListaElementos;
import org.ibit.rol.form.model.Pantalla;
import org.ibit.rol.form.model.RadioButton;
import org.ibit.rol.form.model.TextBox;
import org.ibit.rol.form.model.TraValorPosible;
import org.ibit.rol.form.model.TreeBox;
import org.ibit.rol.form.model.ValorPosible;
import org.ibit.rol.form.persistence.plugins.DatosListaElementos;

/**
 * Utilidades para generar un Map de valores por defecto a partir de una pantalla.
 */
public final class PantallaUtils {

    protected static Log log = LogFactory.getLog(PantallaUtils.class);

    private PantallaUtils() {
    }

    /**
     * 
     * FUNCION NO SE EMPLEA PARA FORMULARIOS TELEMATICOS
     * 
     * 
     * Calcula els valors inicials dels camps d'una pantalla.
     * @param pantalla
     * @param variables Map amb els valors dels camps emplenats (les claus s�n f_xxx per
     * els camps de la mateixa pantalla i f_yyy_xxx per els camps de la pantalla yyy.)
     * @return Valors inicials.
     * TODO Tenir en compte filebox
     */
    public static Map valoresDefecto(Pantalla pantalla, Map variables) {
        List campos = pantalla.getCampos();
        Map valores = new HashMap(campos.size());
        for (int i = 0; i < campos.size(); i++) {
            try {
                Campo campo = (Campo) campos.get(i);
                String key = campo.getNombreLogico();
                String tipoValor = campo.getTipoValor();
                String initial = null;
                if (campo.getExpresionValoresPosibles() != null && campo.getExpresionValoresPosibles().trim().length() > 0) {
                    CampoUtils.calcularValoresPosibles(campo, variables);
                }
                List valoresPosibles = campo.getAllValoresPosibles();
                if (campo instanceof CheckBox) {
                    boolean defecto = ((CheckBox) campo).isValorDefecto();
                    initial = String.valueOf(defecto);
                } else if (campo instanceof ListBox || campo instanceof TreeBox) {
                    tipoValor = "java.lang.String[]";
                    for (int j = 0; j < valoresPosibles.size(); j++) {
                        ValorPosible vp = (ValorPosible) valoresPosibles.get(j);
                        if (vp.isDefecto()) {
                            if (initial == null) {
                                initial = "" + vp.getValor();
                            } else {
                                initial += ", " + vp.getValor();
                            }
                        }
                    }
                } else if (campo instanceof TextBox) {
                    if (campo.getExpresionValoresPosibles() != null && campo.getExpresionValoresPosibles().trim().length() > 0) {
                        Object calculat = CampoUtils.calcularValorDefecto(campo, variables);
                        if (calculat != null) initial = calculat.toString();
                    } else {
                        TraValorPosible traVp = ((TraValorPosible) ((TextBox) campo).getValorPosible().getTraduccion());
                        if (traVp != null) initial = traVp.getEtiqueta();
                    }
                } else {
                    tipoValor = "java.lang.String";
                    for (int j = 0; j < valoresPosibles.size(); j++) {
                        ValorPosible vp = (ValorPosible) valoresPosibles.get(j);
                        if (vp.isDefecto()) {
                            initial = vp.getValor();
                            break;
                        }
                    }
                }
                Class clazz = getClass(tipoValor);
                Object value = toValue(initial, clazz);
                valores.put(key, value);
                variables.put("f_" + key, value);
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
        }
        return valores;
    }

    /**
     * Carga los valores por defecto de cada campo de una determianda pantalla del formulario telem�tico.
     * (Excepto para listas de elementos)
     * @param pantalla pantalla en curso del formulario.
     * @param doc documento xml con los valores por defecto de los campos de la pantalla.
     * @param variables Map con los valores de los campos rellenados (las llaves s�n f_xxx
     * para los campos de la misma pantalla y f_yyy_xxx para los campo de la pantalla yyy.)
     * @param nombreAtributo es el nombre del atributo con el indice a seleccionar en un campo de tipo indexado.
     * @return valores iniciales de los campos de la pantalla.
     */
    public static Map valoresDefecto(Pantalla pantalla, Document doc, Map variables, String nombreAtributo) {
        List campos = pantalla.getCampos();
        Map valores = new HashMap(campos.size());
        Object valorEtiqueta = null;
        boolean hasText;
        for (Iterator iterator = campos.iterator(); iterator.hasNext(); ) {
            try {
                hasText = false;
                Campo campo = (Campo) iterator.next();
                String key = campo.getNombreLogico();
                if (campo instanceof ListaElementos) continue;
                if (campo.getExpresionValoresPosibles() != null && campo.getExpresionValoresPosibles().trim().length() > 0) {
                    CampoUtils.calcularValoresPosibles(campo, variables);
                }
                boolean autorrellenable = false;
                List autorelleno = null;
                if (campo.getExpresionAutorellenable() != null && campo.getExpresionAutorellenable().trim().length() > 0) {
                    autorrellenable = true;
                    autorelleno = CampoUtils.calcularAutorrellenable(campo, variables);
                }
                String xpath = campo.getEtiquetaPDF();
                boolean valorDesdeXML = (xpath != null) && (xpath.trim().length() > 0) && (doc != null) && (doc.selectSingleNode(xpath) != null);
                List valoresPosibles = campo.getAllValoresPosibles();
                String valIni = null;
                String tipoValor = campo.getTipoValor();
                if (campo instanceof CheckBox) {
                    if (valorDesdeXML) {
                        valIni = doc.selectSingleNode(xpath).getText().trim();
                        if (!"true".equals(valIni)) valIni = "false";
                    } else if (autorrellenable) {
                        valIni = (autorelleno.size() <= 0 ? "false" : (!((ValorPosible) autorelleno.get(0)).getValor().equals("true") ? "false" : "true"));
                    } else {
                        boolean defecto = ((CheckBox) campo).isValorDefecto();
                        valIni = String.valueOf(defecto);
                    }
                } else if (campo instanceof ListBox || campo instanceof TreeBox) {
                    tipoValor = "java.lang.String[]";
                    ArrayList valoresTextIndex = new ArrayList();
                    if (valorDesdeXML) {
                        List valoresInit = doc.selectNodes(xpath);
                        for (Iterator i = valoresInit.iterator(); i.hasNext(); ) {
                            Element element = (Element) i.next();
                            String vInicial = element.attributeValue(nombreAtributo);
                            for (Iterator j = valoresPosibles.iterator(); j.hasNext(); ) {
                                ValorPosible vp = (ValorPosible) j.next();
                                String vPosible = vp.getValor();
                                if (vPosible.equals(vInicial)) {
                                    if (valIni == null) {
                                        valIni = "";
                                    } else {
                                        valIni += ", ";
                                    }
                                    valIni += vPosible;
                                    valoresTextIndex.add(((TraValorPosible) vp.getTraduccion()).getEtiqueta());
                                    hasText = true;
                                }
                            }
                        }
                        valorEtiqueta = valoresTextIndex.toArray();
                    } else if (autorrellenable) {
                        for (Iterator i = autorelleno.iterator(); i.hasNext(); ) {
                            ValorPosible element = (ValorPosible) i.next();
                            String vInicial = element.getValor();
                            for (Iterator j = valoresPosibles.iterator(); j.hasNext(); ) {
                                ValorPosible vp = (ValorPosible) j.next();
                                String vPosible = vp.getValor();
                                if (vPosible.equals(vInicial)) {
                                    if (valIni == null) {
                                        valIni = "";
                                    } else {
                                        valIni += ", ";
                                    }
                                    valIni += vPosible;
                                    valoresTextIndex.add(((TraValorPosible) vp.getTraduccion()).getEtiqueta());
                                    hasText = true;
                                }
                            }
                        }
                        valorEtiqueta = valoresTextIndex.toArray();
                    } else {
                        for (int j = 0; j < valoresPosibles.size(); j++) {
                            ValorPosible vp = (ValorPosible) valoresPosibles.get(j);
                            if (vp.isDefecto()) {
                                if (valIni == null) {
                                    valIni = "";
                                } else {
                                    valIni += ", ";
                                }
                                valIni += vp.getValor();
                                valoresTextIndex.add(((TraValorPosible) vp.getTraduccion()).getEtiqueta());
                            }
                        }
                        valorEtiqueta = valoresTextIndex.toArray();
                    }
                } else if (campo instanceof TextBox) {
                    if (valorDesdeXML) {
                        valIni = doc.selectSingleNode(xpath).getText().trim();
                    } else if (autorrellenable) {
                        valIni = (autorelleno.size() <= 0 ? "" : ((ValorPosible) autorelleno.get(0)).getValor());
                    } else {
                        String exprValPos = campo.getExpresionValoresPosibles();
                        if (exprValPos != null && exprValPos.trim().length() > 0) {
                            Object calculat = CampoUtils.calcularValorDefecto(campo, variables);
                            if (calculat != null) valIni = calculat.toString();
                        } else {
                            TraValorPosible traVp = ((TraValorPosible) ((TextBox) campo).getValorPosible().getTraduccion());
                            if (traVp != null) valIni = traVp.getEtiqueta();
                        }
                    }
                } else if ((campo instanceof ComboBox) || (campo instanceof RadioButton)) {
                    tipoValor = "java.lang.String";
                    if (valorDesdeXML) {
                        valIni = ((Element) doc.selectSingleNode(xpath)).attributeValue(nombreAtributo);
                        for (Iterator i = valoresPosibles.iterator(); i.hasNext(); ) {
                            ValorPosible vp = (ValorPosible) i.next();
                            if (vp.getValor().equals(valIni)) {
                                vp.setDefecto(true);
                                valorEtiqueta = ((TraValorPosible) vp.getTraduccion()).getEtiqueta();
                                hasText = true;
                                break;
                            }
                        }
                    } else if (autorrellenable) {
                        valIni = (autorelleno.size() <= 0 ? "" : ((ValorPosible) autorelleno.get(0)).getValor());
                        for (Iterator i = valoresPosibles.iterator(); i.hasNext(); ) {
                            ValorPosible vp = (ValorPosible) i.next();
                            if (vp.getValor().equals(valIni)) {
                                vp.setDefecto(true);
                                valorEtiqueta = ((TraValorPosible) vp.getTraduccion()).getEtiqueta();
                                hasText = true;
                                break;
                            }
                        }
                    } else {
                        for (int j = 0; j < valoresPosibles.size(); j++) {
                            ValorPosible vp = (ValorPosible) valoresPosibles.get(j);
                            if (vp.isDefecto()) {
                                valIni = vp.getValor();
                                valorEtiqueta = ((TraValorPosible) vp.getTraduccion()).getEtiqueta();
                                hasText = true;
                                break;
                            }
                        }
                    }
                }
                Class clazz = getClass(tipoValor);
                Object value = toValue(valIni, clazz);
                valores.put(key, value);
                if (hasText) {
                    valores.put(key + "_text", valorEtiqueta);
                }
                variables.put("f_" + key, value);
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
        }
        return valores;
    }

    private static Class getClass(String name) throws ClassNotFoundException {
        boolean indexed = false;
        if (name.endsWith("[]")) {
            name = name.substring(0, name.length() - 2);
            indexed = true;
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = PantallaUtils.class.getClassLoader();
        }
        Class baseClass = cl.loadClass(name);
        if (indexed) {
            return Array.newInstance(baseClass, 0).getClass();
        } else {
            return baseClass;
        }
    }

    private static Object toValue(String initial, Class clazz) {
        Object value;
        try {
            if (initial != null) {
                value = ConvertUtils.convert(initial, clazz);
            } else {
                if (clazz.isArray()) {
                    value = Array.newInstance(clazz.getComponentType(), 0);
                } else {
                    value = clazz.newInstance();
                }
            }
        } catch (Throwable t) {
            log.warn("No es pot convertir '" + initial + "' a " + clazz.getName());
            value = null;
        }
        return (value);
    }

    /**
     * Si la pantalla tiene un campo de tipo Lista Elementos carga los valores por defecto de ese campo
     * @return Si tiene un campo lista de elementos devuelve un List con los elementos, si no tiene un campo lista de elementos devuelve nulo.
     */
    public static List valoresDefectoListaElementos(ListaElementos campo, Formulario formulario, Pantalla pantalla, Document doc, Map variables, String nombreAtributo) {
        String referencia = CampoUtils.getReferenciaListaElementos(pantalla.getNombre(), campo.getNombreLogico());
        Pantalla p = buscarPantallaListaElementos(formulario, referencia);
        if (p == null) {
            log.error("No se encuenta pantalla detalle asociada al campo lista de elementos: " + campo.getNombreLogico());
            return new ArrayList();
        }
        List valoresXML = valoresListaElementosFromXML(campo, p, doc, variables, nombreAtributo);
        if (valoresXML.size() > 0) {
            return valoresXML;
        }
        if (campo.getExpresionAutorellenable() != null && campo.getExpresionAutorellenable().trim().length() > 0) {
            List resultScript = CampoUtils.calcularAutorrellenable(campo, variables);
            if (resultScript == null) {
                return new ArrayList();
            }
            if (!(resultScript.get(0) instanceof DatosListaElementos)) {
                log.error("El script autorrellenable no ha devuelto una variable DatosListaElementos para el campo lista de elementos: " + campo.getNombreLogico());
                return new ArrayList();
            }
            DatosListaElementos datosListaElementos = (DatosListaElementos) resultScript.get(0);
            List valoresAutorrelleno = new ArrayList();
            for (int i = 1; i <= datosListaElementos.geNumeroElementos(); i++) {
                Map dataElemento = valoresElementoFromDatosListaElementos(p, variables, datosListaElementos, i);
                valoresAutorrelleno.add(dataElemento);
            }
            return valoresAutorrelleno;
        }
        return new ArrayList();
    }

    /**
     * Obtiene los datos de la lista de elementos desde el XML
     */
    private static List valoresListaElementosFromXML(Campo campoLista, Pantalla pantallaDetalle, Document doc, Map variables, String nombreAtributo) {
        List listaElementos = new ArrayList();
        String xpath = campoLista.getEtiquetaPDF();
        if (StringUtils.isEmpty(xpath)) return listaElementos;
        List elementos = doc.selectNodes(xpath + "/*");
        String xpathElemento = xpath + "/ID";
        for (int i = 1; i <= elementos.size(); i++) {
            Map variablesScript = new HashMap();
            variablesScript.putAll(variables);
            Map datosElemento = valoresElementoFromXML(pantallaDetalle, doc, variablesScript, nombreAtributo, xpathElemento + i);
            listaElementos.add(datosElemento);
        }
        return listaElementos;
    }

    /**
	 * Realiza la carga desde el xml de un elemento de una lista de elementos
	 */
    private static Map valoresElementoFromXML(Pantalla pantalla, Document doc, Map variables, String nombreAtributo, String xpathElemento) {
        List campos = pantalla.getCampos();
        Map valores = new HashMap(campos.size());
        Object valorEtiqueta = null;
        boolean hasText;
        for (Iterator iterator = campos.iterator(); iterator.hasNext(); ) {
            try {
                hasText = false;
                Campo campo = (Campo) iterator.next();
                String key = campo.getNombreLogico();
                if (campo.getExpresionValoresPosibles() != null && campo.getExpresionValoresPosibles().trim().length() > 0) {
                    CampoUtils.calcularValoresPosibles(campo, variables);
                }
                String xpath = (StringUtils.isEmpty(campo.getEtiquetaPDF()) ? campo.getEtiquetaPDF() : xpathElemento + "/" + campo.getEtiquetaPDF());
                boolean valorDesdeXML = (xpath != null) && (xpath.trim().length() > 0) && (doc != null) && (doc.selectSingleNode(xpath) != null);
                List valoresPosibles = campo.getAllValoresPosibles();
                String valIni = null;
                String tipoValor = campo.getTipoValor();
                if (campo instanceof CheckBox) {
                    if (valorDesdeXML) {
                        valIni = doc.selectSingleNode(xpath).getText().trim();
                        if (!"true".equals(valIni)) valIni = "false";
                    } else {
                        boolean defecto = ((CheckBox) campo).isValorDefecto();
                        valIni = String.valueOf(defecto);
                    }
                } else if (campo instanceof ListBox || campo instanceof TreeBox) {
                    tipoValor = "java.lang.String[]";
                    ArrayList valoresTextIndex = new ArrayList();
                    if (valorDesdeXML) {
                        List valoresInit = doc.selectNodes(xpath);
                        for (Iterator i = valoresInit.iterator(); i.hasNext(); ) {
                            Element element = (Element) i.next();
                            String vInicial = element.attributeValue(nombreAtributo);
                            for (Iterator j = valoresPosibles.iterator(); j.hasNext(); ) {
                                ValorPosible vp = (ValorPosible) j.next();
                                String vPosible = vp.getValor();
                                if (vPosible.equals(vInicial)) {
                                    if (valIni == null) {
                                        valIni = "";
                                    } else {
                                        valIni += ", ";
                                    }
                                    valIni += vPosible;
                                    valoresTextIndex.add(((TraValorPosible) vp.getTraduccion()).getEtiqueta());
                                    hasText = true;
                                }
                            }
                        }
                        valorEtiqueta = valoresTextIndex.toArray();
                    } else {
                        for (int j = 0; j < valoresPosibles.size(); j++) {
                            ValorPosible vp = (ValorPosible) valoresPosibles.get(j);
                            if (vp.isDefecto()) {
                                if (valIni == null) {
                                    valIni = "";
                                } else {
                                    valIni += ", ";
                                }
                                valIni += vp.getValor();
                                valoresTextIndex.add(((TraValorPosible) vp.getTraduccion()).getEtiqueta());
                            }
                        }
                        valorEtiqueta = valoresTextIndex.toArray();
                    }
                } else if (campo instanceof TextBox) {
                    if (valorDesdeXML) {
                        valIni = doc.selectSingleNode(xpath).getText().trim();
                    } else {
                        String exprValPos = campo.getExpresionValoresPosibles();
                        if (exprValPos != null && exprValPos.trim().length() > 0) {
                            Object calculat = CampoUtils.calcularValorDefecto(campo, variables);
                            if (calculat != null) valIni = calculat.toString();
                        } else {
                            TraValorPosible traVp = ((TraValorPosible) ((TextBox) campo).getValorPosible().getTraduccion());
                            if (traVp != null) valIni = traVp.getEtiqueta();
                        }
                    }
                } else if ((campo instanceof ComboBox) || (campo instanceof RadioButton)) {
                    tipoValor = "java.lang.String";
                    if (valorDesdeXML) {
                        valIni = ((Element) doc.selectSingleNode(xpath)).attributeValue(nombreAtributo);
                        for (Iterator i = valoresPosibles.iterator(); i.hasNext(); ) {
                            ValorPosible vp = (ValorPosible) i.next();
                            if (vp.getValor().equals(valIni)) {
                                vp.setDefecto(true);
                                valorEtiqueta = ((TraValorPosible) vp.getTraduccion()).getEtiqueta();
                                hasText = true;
                                break;
                            }
                        }
                    } else {
                        for (int j = 0; j < valoresPosibles.size(); j++) {
                            ValorPosible vp = (ValorPosible) valoresPosibles.get(j);
                            if (vp.isDefecto()) {
                                valIni = vp.getValor();
                                valorEtiqueta = ((TraValorPosible) vp.getTraduccion()).getEtiqueta();
                                hasText = true;
                                break;
                            }
                        }
                    }
                }
                Class clazz = getClass(tipoValor);
                Object value = toValue(valIni, clazz);
                valores.put(key, value);
                if (hasText) {
                    valores.put(key + "_text", valorEtiqueta);
                }
                variables.put("f_" + key, value);
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
        }
        return valores;
    }

    /**
     * Busca si una pantalla tiene un campo Lista Elementos
     * @param pantalla
     * @return Devuelve campo ListaElementos si existe y null si no existe
     */
    public static ListaElementos buscarCampoListaElementos(Pantalla pantalla) {
        Campo campo = null;
        boolean enc = false;
        List campos = pantalla.getCampos();
        for (Iterator iterator = campos.iterator(); iterator.hasNext(); ) {
            campo = (Campo) iterator.next();
            if ((campo instanceof ListaElementos)) {
                return (ListaElementos) campo;
            }
        }
        return null;
    }

    /**
     * Busca en el formulario la pantalla asociada a la lista de elementos
     * @param formulario
     * @param referenciaPantallaListaElementos
     * @return
     */
    public static Pantalla buscarPantallaListaElementos(Formulario formulario, String referenciaPantallaListaElementos) {
        Pantalla p = null;
        for (Iterator it = formulario.getPantallas().iterator(); it.hasNext(); ) {
            p = (Pantalla) it.next();
            if (referenciaPantallaListaElementos.equals(p.getComponenteListaElementos())) {
                return p;
            }
        }
        return null;
    }

    private static Map valoresElementoFromDatosListaElementos(Pantalla pantalla, Map variables, DatosListaElementos datosLEL, int indiceElemento) {
        List campos = pantalla.getCampos();
        Map valores = new HashMap(campos.size());
        Object valorEtiqueta = null;
        boolean hasText;
        for (Iterator iterator = campos.iterator(); iterator.hasNext(); ) {
            try {
                hasText = false;
                Campo campo = (Campo) iterator.next();
                String key = campo.getNombreLogico();
                if (campo.getExpresionValoresPosibles() != null && campo.getExpresionValoresPosibles().trim().length() > 0) {
                    CampoUtils.calcularValoresPosibles(campo, variables);
                }
                List valoresPosibles = campo.getAllValoresPosibles();
                String tipoValor = campo.getTipoValor();
                String valIni = null;
                Object valElemento = datosLEL.getDatoElemento(indiceElemento, campo.getNombreLogico());
                boolean valorFromDatosListaElementos = (valElemento != null);
                if (campo instanceof CheckBox) {
                    if (valorFromDatosListaElementos) {
                        if (!"true".equals((String) valElemento)) valIni = "false";
                    } else {
                        boolean defecto = ((CheckBox) campo).isValorDefecto();
                        valIni = String.valueOf(defecto);
                    }
                } else if (campo instanceof ListBox || campo instanceof TreeBox) {
                    tipoValor = "java.lang.String[]";
                    ArrayList valoresTextIndex = new ArrayList();
                    if (valorFromDatosListaElementos) {
                        List valoresInit = (List) valElemento;
                        for (Iterator i = valoresInit.iterator(); i.hasNext(); ) {
                            String vInicial = (String) i.next();
                            for (Iterator j = valoresPosibles.iterator(); j.hasNext(); ) {
                                ValorPosible vp = (ValorPosible) j.next();
                                String vPosible = vp.getValor();
                                if (vPosible.equals(vInicial)) {
                                    if (valIni == null) {
                                        valIni = "";
                                    } else {
                                        valIni += ", ";
                                    }
                                    valIni += vPosible;
                                    valoresTextIndex.add(((TraValorPosible) vp.getTraduccion()).getEtiqueta());
                                    hasText = true;
                                }
                            }
                        }
                        valorEtiqueta = valoresTextIndex.toArray();
                    } else {
                        for (int j = 0; j < valoresPosibles.size(); j++) {
                            ValorPosible vp = (ValorPosible) valoresPosibles.get(j);
                            if (vp.isDefecto()) {
                                if (valIni == null) {
                                    valIni = "";
                                } else {
                                    valIni += ", ";
                                }
                                valIni += vp.getValor();
                                valoresTextIndex.add(((TraValorPosible) vp.getTraduccion()).getEtiqueta());
                            }
                        }
                        valorEtiqueta = valoresTextIndex.toArray();
                    }
                } else if (campo instanceof TextBox) {
                    if (valorFromDatosListaElementos) {
                        valIni = (String) valElemento;
                    } else {
                        String exprValPos = campo.getExpresionValoresPosibles();
                        if (exprValPos != null && exprValPos.trim().length() > 0) {
                            Object calculat = CampoUtils.calcularValorDefecto(campo, variables);
                            if (calculat != null) valIni = calculat.toString();
                        } else {
                            TraValorPosible traVp = ((TraValorPosible) ((TextBox) campo).getValorPosible().getTraduccion());
                            if (traVp != null) valIni = traVp.getEtiqueta();
                        }
                    }
                } else if ((campo instanceof ComboBox) || (campo instanceof RadioButton)) {
                    tipoValor = "java.lang.String";
                    if (valorFromDatosListaElementos) {
                        valIni = (String) valElemento;
                        for (Iterator i = valoresPosibles.iterator(); i.hasNext(); ) {
                            ValorPosible vp = (ValorPosible) i.next();
                            if (vp.getValor().equals(valIni)) {
                                vp.setDefecto(true);
                                valorEtiqueta = ((TraValorPosible) vp.getTraduccion()).getEtiqueta();
                                hasText = true;
                                break;
                            }
                        }
                    } else {
                        for (int j = 0; j < valoresPosibles.size(); j++) {
                            ValorPosible vp = (ValorPosible) valoresPosibles.get(j);
                            if (vp.isDefecto()) {
                                valIni = vp.getValor();
                                valorEtiqueta = ((TraValorPosible) vp.getTraduccion()).getEtiqueta();
                                hasText = true;
                                break;
                            }
                        }
                    }
                }
                Class clazz = getClass(tipoValor);
                Object value = toValue(valIni, clazz);
                valores.put(key, value);
                if (hasText) {
                    valores.put(key + "_text", valorEtiqueta);
                }
                variables.put("f_" + key, value);
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
        }
        return valores;
    }
}

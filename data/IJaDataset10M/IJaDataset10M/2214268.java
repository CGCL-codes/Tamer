package org.opensih.Utils.Secciones;

import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.opensih.Utils.Converters.Encoder;

public class DiagProc {

    String dpocod;

    String dpotodo;

    List<String> listDpo = new LinkedList<String>();

    boolean bool;

    String ppcod;

    String pptodo;

    List<String> listPp = new LinkedList<String>();

    boolean bool2;

    String dopcod;

    String doptodo;

    List<String> listDop = new LinkedList<String>();

    boolean bool3;

    String ircod;

    String irtodo;

    List<String> listIr = new LinkedList<String>();

    boolean bool4;

    String conversion;

    boolean editConv;

    String espConv;

    String texConv;

    String tecnica;

    boolean editTecn;

    String espTecnMinAsist;

    String ampliacion_dpo;

    String ampliacion_pp;

    String ampliacion_dop;

    String ampliacion_ir;

    String Categoria;

    public DiagProc() {
        init();
    }

    public void init() {
        ampliacion_dpo = ampliacion_pp = ampliacion_dop = ampliacion_ir = "";
        espConv = texConv = "";
        dpocod = dopcod = ppcod = ircod = "";
        dpotodo = doptodo = pptodo = irtodo = "";
        bool = bool2 = bool3 = bool4 = true;
        editConv = true;
        conversion = "NO";
        tecnica = "_";
        editTecn = true;
        espTecnMinAsist = "_";
    }

    @SuppressWarnings("unchecked")
    public DiagProc(String xml) {
        init();
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document dP = saxBuilder.build(new StringReader(xml));
            Element raizDiagProc = dP.getRootElement();
            Element diags = raizDiagProc.getChild("Diagnosticos");
            Element procs = raizDiagProc.getChild("Procedimientos");
            for (Element e : (List<Element>) diags.getChildren("pre")) listDpo.add(XMLToCode(e));
            for (Element e : (List<Element>) diags.getChildren("post")) listDop.add(XMLToCode(e));
            for (Element e : (List<Element>) procs.getChildren("pre")) listPp.add(XMLToCode(e));
            for (Element e : (List<Element>) procs.getChildren("post")) listIr.add(XMLToCode(e));
            Element conv = raizDiagProc.getChild("Conversion");
            Element espc = conv.getChild("Especificacion");
            conversion = conv.getText();
            editConv = true;
            espConv = espc.getAttributeValue("dato");
            texConv = espc.getText();
            Element tecn = raizDiagProc.getChild("Tecnica");
            if (tecn != null) {
                Element espt = tecn.getChild("Especificacion");
                tecnica = tecn.getText();
                if (tecnica.equals("Minimamente Invasiva")) {
                    editTecn = false;
                    espTecnMinAsist = espt.getAttributeValue("dato");
                } else {
                    editTecn = true;
                }
            }
            Categoria = raizDiagProc.getChild("Categoria").getText();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public String toXML(boolean isSuspended) {
        if (isSuspended) return "<root>" + "<Diagnosticos>" + ((!listDpo.isEmpty()) ? listarDpo() : "") + "</Diagnosticos>" + "<Procedimientos>" + ((!listPp.isEmpty()) ? listarPp() : "") + "</Procedimientos>" + "<Conversion>" + "<Especificacion dato=\"\">" + "</Especificacion>" + "</Conversion>" + "<Categoria/>" + "</root>"; else Categoria = derivarCategoria(codsIR());
        return "<root>" + "<Diagnosticos>" + ((!listDpo.isEmpty()) ? listarDpo() : "") + ((!listDop.isEmpty()) ? listarDop() : "") + "</Diagnosticos>" + "<Procedimientos>" + ((!listPp.isEmpty()) ? listarPp() : "") + ((!listIr.isEmpty()) ? listarIr() : "") + "</Procedimientos>" + "<Conversion>" + conversion + "<Especificacion dato=\"" + espConv + "\">" + Encoder.parseXML(texConv) + "</Especificacion>" + "</Conversion>" + "<Tecnica>" + tecnica + "<Especificacion dato=\"" + espTecnMinAsist + "\">" + "</Especificacion>" + "</Tecnica>" + "<Categoria>" + Categoria + "</Categoria>" + "</root>";
    }

    public void seteo() {
        if (espConv.equals("Otros")) {
            editConv = false;
            conversion = "SI";
        } else if (espConv.equals("NO")) {
            editConv = true;
            conversion = "NO";
            texConv = "";
        } else {
            editConv = true;
            conversion = "SI";
            texConv = "";
        }
    }

    public void seteo2() {
        if (tecnica.equals("Minimamente Invasiva")) {
            editTecn = false;
        } else {
            editTecn = true;
            espTecnMinAsist = "_";
        }
    }

    public String XMLToCode(Element e) {
        String c1 = e.getAttributeValue("code");
        String c2 = e.getAttributeValue("descripcion");
        String c3 = e.getAttributeValue("ampliacion");
        return ((c1 != null) ? c1 + " | " + c2 : "") + ((c3 != null && c1 != null) ? " | " + c3 : "") + ((c3 != null && c1 == null) ? c3 : "");
    }

    /********* ADD **************/
    public String agregardpo() {
        if (ampliacion_dpo.contains("|")) return "No se puede utilizar el caracter | en la ampliaci�n.";
        if (dpotodo.compareTo("") != 0) {
            if (noPertenece(listDpo, dpocod) && dpotodo.compareTo("C�digo invalido.") != 0) listDpo.add(dpotodo + ((ampliacion_dpo != "") ? " | " + ampliacion_dpo : "")); else if (!noPertenece(listDpo, dpocod)) return "No se puede ingresar dos veces el mismo codigo";
        } else if (ampliacion_dpo != "" && !listDpo.contains(ampliacion_dpo)) {
            listDpo.add(ampliacion_dpo);
        } else {
            return "Debe ingresar un c�digo o un texto en ampliaci�n";
        }
        dpotodo = "";
        dpocod = "";
        return "ok";
    }

    public String agregarpp() {
        if (ampliacion_pp.contains("|")) return "No se puede utilizar el caracter | en la ampliaci�n.";
        if (pptodo.compareTo("") != 0) {
            if (noPertenece(listPp, ppcod) && pptodo.compareTo("C�digo invalido.") != 0) listPp.add(pptodo + ((ampliacion_pp != "") ? " | " + ampliacion_pp : "")); else if (!noPertenece(listPp, ppcod)) return "No se puede ingresar dos veces el mismo codigo";
        } else if (ampliacion_pp != "" && !listPp.contains(ampliacion_pp)) {
            listPp.add(ampliacion_pp);
        } else {
            return "Debe ingresar un c�digo.";
        }
        pptodo = "";
        ppcod = "";
        return "ok";
    }

    public String agregardop() {
        if (ampliacion_dop.contains("|")) return "No se puede utilizar el caracter | en la ampliaci�n.";
        if (doptodo.compareTo("") != 0) {
            if (noPertenece(listDop, dopcod) && doptodo.compareTo("C�digo invalido.") != 0) listDop.add(doptodo + ((ampliacion_dop != "") ? " | " + ampliacion_dop : "")); else if (!noPertenece(listDop, dopcod)) return "No se puede ingresar dos veces el mismo codigo";
        } else if (ampliacion_dop != "" && !listDop.contains(ampliacion_dop)) {
            listDop.add(ampliacion_dop);
        } else {
            return "Debe ingresar un c�digo o un texto en ampliaci�n";
        }
        doptodo = "";
        dopcod = "";
        return "ok";
    }

    public String agregarir() {
        if (ampliacion_ir.contains("|")) return "No se puede utilizar el caracter | en la ampliaci�n.";
        if (irtodo.compareTo("") != 0) {
            if (noPertenece(listIr, ircod) && irtodo.compareTo("C�digo invalido.") != 0) listIr.add(irtodo + ((ampliacion_ir != "") ? " | " + ampliacion_ir : "")); else if (!noPertenece(listIr, ircod)) return "No se puede ingresar dos veces el mismo codigo";
        } else if (ampliacion_ir != "" && !listIr.contains(ampliacion_ir)) {
            listIr.add(ampliacion_ir);
        } else {
            return "Debe ingresar un c�digo.";
        }
        irtodo = "";
        ircod = "";
        return "ok";
    }

    /*******Remove************/
    public void sacarir(String ir) {
        listIr.remove(ir);
        bool4 = true;
    }

    public void sacardop(String dop) {
        listDop.remove(dop);
        bool3 = true;
    }

    public void sacarpp(String pp) {
        listPp.remove(pp);
        bool2 = true;
    }

    public void sacardpo(String dpo) {
        listDpo.remove(dpo);
        bool = true;
    }

    public boolean noPertenece(List<String> list, String cod) {
        cod = cod.toUpperCase();
        for (String s : list) {
            if (s.split("\\|").length > 1 && cod.equals(s.split("\\|")[0].trim())) return false;
        }
        return true;
    }

    /***************Convertidores de Codigos********************/
    public String listarDpo() {
        String s = "";
        for (String aux : listDpo) s += "<pre" + codeToXML(aux) + " />";
        return s;
    }

    public String listarDop() {
        String s = "";
        for (String aux : listDop) s += "<post" + codeToXML(aux) + " />";
        return s;
    }

    public String listarPp() {
        String s = "";
        for (String aux : listPp) s += "<pre" + codeToXML(aux) + " />";
        return s;
    }

    public String listarIr() {
        String s = "";
        for (String aux : listIr) s += "<post" + codeToXML(aux) + " />";
        return s;
    }

    public String codeToXML(String cod) {
        String a[] = cod.split("\\|");
        if (a.length == 1) return " ampliacion=\"" + Encoder.parseXML(a[0].trim()) + "\""; else if (a.length == 2) return " code=\"" + Encoder.parseXML(a[0].trim()) + "\"" + " descripcion=\"" + Encoder.parseXML(a[1].trim()) + "\""; else return " code=\"" + Encoder.parseXML(a[0].trim()) + "\"" + " descripcion=\"" + Encoder.parseXML(a[1].trim()) + "\"" + " ampliacion=\"" + Encoder.parseXML(a[2].trim()) + "\"";
    }

    /********* parser ****************/
    public static String derivarCategoria(List<String> cods) {
        if (cods.isEmpty() || cods.get(0).startsWith("TRD")) return "No Especificada";
        if (cods.size() == 1) if (cods.get(0).charAt(3) == '1') return "corriente"; else if (cods.get(0).charAt(3) == '2') return "mayor"; else if (cods.get(0).charAt(3) == '3') return "alta"; else return "menor"; else {
            List<String> sinCatMenor = sacoCero(cods);
            if (sinCatMenor.size() > 0) {
                if (sinCatMenor.size() == 1) {
                    int a = Integer.parseInt(String.valueOf(sinCatMenor.get(0).charAt(3)));
                    String salida = "";
                    switch(a) {
                        case 1:
                            salida = "corriente";
                            break;
                        case 2:
                            salida = "mayor";
                            break;
                        case 3:
                            salida = "alta";
                            break;
                    }
                    return salida;
                }
                int max = 0;
                Iterator<String> iter = sinCatMenor.iterator();
                while (iter.hasNext() && max < 2) {
                    max = Integer.parseInt(String.valueOf(iter.next().charAt(3)));
                }
                if (max < 2) return "mayor"; else return "alta";
            } else {
                return "menor";
            }
        }
    }

    private static List<String> sacoCero(List<String> cods) {
        List<String> aux = new LinkedList<String>();
        for (String s : cods) {
            if (s.charAt(3) != '0') {
                aux.add(s);
            }
            ;
        }
        return aux;
    }

    public List<String> codsIR() {
        List<String> codsIR = new LinkedList<String>();
        for (String s : listIr) {
            if (s.split("\\|").length > 1) codsIR.add(s.split("\\|")[0].trim());
        }
        return codsIR;
    }

    public String codsIRInf() {
        String cods = "";
        for (String s : listIr) cods += s + " - ";
        if (cods.length() > 0) return cods.substring(0, cods.length() - 3); else return cods;
    }

    public String codsIRInf_Min() {
        if (!listIr.isEmpty()) {
            String cods = "";
            for (String s : listIr) cods += s + " - ";
            String[] s = cods.split("\\|");
            if (s.length < 2) {
                if (s[0].length() > 20) {
                    String min = s[0].substring(0, 20);
                    min = min.concat(" ...");
                    return min;
                } else {
                    return (s[0].substring(0, (s[0].length() - 2)));
                }
            }
            if (s[1].length() > 20) {
                String min = s[1].substring(0, 20);
                min = min.concat(" ...");
                return min;
            }
            return s[1];
        }
        return "--";
    }

    public String codsIPInf() {
        String cods = "";
        for (String s : listPp) cods += s + " - ";
        if (cods.length() > 0) return cods.substring(0, cods.length() - 3); else return cods;
    }

    public String codsIPInf_Min() {
        if (!listPp.isEmpty()) {
            String cods = "";
            for (String s : listPp) cods += s + " - ";
            String[] s = cods.split("\\|");
            if (s.length < 2) {
                if (s[0].length() > 20) {
                    String min = s[0].substring(0, 20);
                    min = min.concat(" ...");
                    return min;
                } else {
                    return (s[0].substring(0, (s[0].length() - 2)));
                }
            }
            if (s[1].length() > 20) {
                String min = s[1].substring(0, 20);
                min = min.concat(" ...");
                return min;
            }
            return s[1];
        }
        return "--";
    }

    public String codsDopInf() {
        String cods = "";
        for (String s : listDop) cods += s + " - ";
        if (cods.length() > 0) return cods.substring(0, cods.length() - 3); else return cods;
    }

    public String codsDopInf_Min() {
        if (!listDop.isEmpty()) {
            String cods = "";
            for (String s : listDop) cods += s + " - ";
            String[] s = cods.split("\\|");
            if (s.length < 2) {
                if (s[0].length() > 20) {
                    String min = s[0].substring(0, 20);
                    min = min.concat(" ...");
                    return min;
                } else {
                    return (s[0].substring(0, (s[0].length() - 2)));
                }
            }
            if (s[1].length() > 20) {
                String min = s[1].substring(0, 20);
                min = min.concat(" ...");
                return min;
            }
            return s[1];
        }
        return "--";
    }

    public String codsIR2() {
        String codsIR = "";
        for (String s : listIr) {
            if (s.split("\\|").length > 1) codsIR += s.split("\\|")[0].trim() + " - ";
        }
        return codsIR;
    }

    public String getDopcod() {
        return dopcod;
    }

    public void setDopcod(String dopcod) {
        this.dopcod = dopcod;
    }

    public String getDoptodo() {
        return doptodo;
    }

    public void setDoptodo(String doptodo) {
        this.doptodo = doptodo;
    }

    public List<String> getListDpo() {
        return listDpo;
    }

    public void setListDpo(List<String> listDpo) {
        this.listDpo = listDpo;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String getDpocod() {
        return dpocod;
    }

    public void setDpocod(String dpocod) {
        this.dpocod = dpocod;
    }

    public String getDpotodo() {
        return dpotodo;
    }

    public void setDpotodo(String dpotodo) {
        this.dpotodo = dpotodo;
    }

    public List<String> getListPp() {
        return listPp;
    }

    public void setListPp(List<String> listPp) {
        this.listPp = listPp;
    }

    public boolean isBool2() {
        return bool2;
    }

    public void setBool2(boolean bool2) {
        this.bool2 = bool2;
    }

    public String getPpcod() {
        return ppcod;
    }

    public void setPpcod(String ppcod) {
        this.ppcod = ppcod;
    }

    public String getPptodo() {
        return pptodo;
    }

    public void setPptodo(String pptodo) {
        this.pptodo = pptodo;
    }

    public List<String> getListDop() {
        return listDop;
    }

    public void setListDop(List<String> listDop) {
        this.listDop = listDop;
    }

    public boolean isBool3() {
        return bool3;
    }

    public void setBool3(boolean bool3) {
        this.bool3 = bool3;
    }

    public String getIrcod() {
        return ircod;
    }

    public void setIrcod(String ircod) {
        this.ircod = ircod;
    }

    public String getIrtodo() {
        return irtodo;
    }

    public void setIrtodo(String irtodo) {
        this.irtodo = irtodo;
    }

    public List<String> getListIr() {
        return listIr;
    }

    public void setListIr(List<String> listIr) {
        this.listIr = listIr;
    }

    public boolean isBool4() {
        return bool4;
    }

    public void setBool4(boolean bool4) {
        this.bool4 = bool4;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public boolean isEditConv() {
        return editConv;
    }

    public void setEditConv(boolean editConv) {
        this.editConv = editConv;
    }

    public String getEspConv() {
        return espConv;
    }

    public void setEspConv(String espConv) {
        this.espConv = espConv;
    }

    public String getTexConv() {
        return texConv;
    }

    public void setTexConv(String texConv) {
        this.texConv = texConv;
    }

    public String getAmpliacion_dpo() {
        return ampliacion_dpo;
    }

    public void setAmpliacion_dpo(String ampliacion_dpo) {
        this.ampliacion_dpo = ampliacion_dpo;
    }

    public String getAmpliacion_pp() {
        return ampliacion_pp;
    }

    public void setAmpliacion_pp(String ampliacion_pp) {
        this.ampliacion_pp = ampliacion_pp;
    }

    public String getAmpliacion_dop() {
        return ampliacion_dop;
    }

    public void setAmpliacion_dop(String ampliacion_dop) {
        this.ampliacion_dop = ampliacion_dop;
    }

    public String getAmpliacion_ir() {
        return ampliacion_ir;
    }

    public void setAmpliacion_ir(String ampliacion_ir) {
        this.ampliacion_ir = ampliacion_ir;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getCodsDpo() {
        String s = "";
        for (String aux : listDpo) s += Encoder.parseXML(aux) + "<br>";
        return s;
    }

    public String getCodsPp() {
        String s = "";
        for (String aux : listPp) s += Encoder.parseXML(aux) + "<br>";
        return s;
    }

    public String getCodsDop() {
        String s = "";
        for (String aux : listDop) s += Encoder.parseXML(aux) + "<br>";
        return s;
    }

    public String getCodsIr() {
        String s = "";
        for (String aux : listIr) s += Encoder.parseXML(aux) + "<br>";
        return s;
    }

    public String getTecnica() {
        return tecnica;
    }

    public void setTecnica(String tecnica) {
        this.tecnica = tecnica;
    }

    public boolean isEditTecn() {
        return editTecn;
    }

    public void setEditTecn(boolean editTecn) {
        this.editTecn = editTecn;
    }

    public String getEspTecnMinAsist() {
        return espTecnMinAsist;
    }

    public void setEspTecnMinAsist(String espTecnMinAsist) {
        this.espTecnMinAsist = espTecnMinAsist;
    }

    public boolean esLaparoscopicaEndoscopica() {
        if (this.espTecnMinAsist.equals("V�deo Asistida") || this.espTecnMinAsist.equals("Endosc�pica")) {
            return true;
        }
        return false;
    }

    public String getLaparoscopicaEndoscopica() {
        if (this.tecnica.equals("Minimamente Invasiva")) {
            return this.tecnica + " " + this.espTecnMinAsist;
        } else if (this.tecnica.equals("_")) {
            return "";
        }
        return this.tecnica;
    }

    public boolean esEndovascular() {
        if (this.espTecnMinAsist.equals("Endovascular")) return true;
        return false;
    }

    public String darIntervencionesRealizadas() {
        String salida = "";
        for (int i = 0; i < listIr.size(); i++) {
            if (listIr.get(i).contains("|")) {
                salida = salida + listIr.get(i) + "-";
            }
        }
        return salida;
    }
}

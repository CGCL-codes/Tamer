package carga;

import java.util.Vector;

public class LectorSiiau extends LectorHtml implements LectorHtmlListener {

    private MateriaSiiau f;

    private String rutaNrc;

    private String rutaClave;

    private String rutaMateria;

    private String rutaSec;

    private String rutaCr;

    private String rutaCup;

    private String rutaDis;

    private String rutaHor;

    private String rutaProf;

    private Vector registros;

    private Vector eventos;

    int invReg;

    public MateriaSiiau getMateriaSiiau() {
        return f;
    }

    public LectorSiiau(MateriaSiiau f) {
        super("http://iasv2.siiau.udg.mx/wco/sspseca.consulta_oferta?ciclop=" + f.getCal() + "&cup=" + f.getCentro() + "&crsep=" + f.getClave());
        super.setLectorHtmlListener(this);
        registros = new Vector(10, 10);
        eventos = new Vector(10, 10);
        this.f = f;
    }

    public void setLectorHtmlListener(LectorHtmlListener e) {
    }

    public void addLectorSiiauListener(LectorSiiauListener e) {
        eventos.add(e);
    }

    public void error(String descripcion) {
        for (int x = 0; x < eventos.size(); x++) {
            ((LectorSiiauListener) eventos.get(x)).error(descripcion);
        }
    }

    public void progreso(String estado, int porcentaje) {
        for (int x = 0; x < eventos.size(); x++) {
            ((LectorSiiauListener) eventos.get(x)).progreso("Descargando " + f.getClave(), porcentaje);
        }
    }

    private void procesar(HtmlTag tabla) {
        for (int x = 2; x < tabla.getSubCont(); x++) {
            HtmlTag reg = tabla.getSubTag(x);
            Registro r = new Registro();
            procesar1(reg, r);
        }
    }

    private void procesar1(HtmlTag nodo, Registro r) {
        try {
            r.nrc = nodo.seguirRuta(rutaNrc, nodo).contenido;
            r.clave = nodo.seguirRuta(rutaClave, nodo).contenido;
            r.materia = nodo.seguirRuta(rutaMateria, nodo).contenido;
            r.seccion = nodo.seguirRuta(rutaSec, nodo).contenido;
            r.creditos = nodo.seguirRuta(rutaCr, nodo).contenido;
            r.cupo = nodo.seguirRuta(rutaCup, nodo).contenido;
            r.disponible = nodo.seguirRuta(rutaDis, nodo).contenido;
            procesar2(nodo.seguirRuta(rutaHor, nodo), r);
            procesar3(nodo.seguirRuta(rutaProf, nodo), r);
        } catch (Exception e) {
            invReg++;
        }
    }

    private void procesar2(HtmlTag horario, Registro r) {
        HtmlTag tmp = horario.getSubTag(0);
        for (int x = 0; x < tmp.getSubCont(); x++) {
            procesar4(tmp.getSubTag(x), r);
        }
    }

    private void procesar4(HtmlTag horario, Registro r) {
        String horas = horario.getSubTag(1).contenido;
        String dias = horario.getSubTag(2).contenido;
        String modulo = horario.getSubTag(3).contenido;
        String aula = horario.getSubTag(4).contenido;
        String periodo = horario.getSubTag(5).contenido;
        r.horario.setPeriodo(periodo);
        r.horario.setDatos(dias, modulo, aula, horas);
    }

    private void procesar3(HtmlTag nodo, Registro r) {
        HtmlTag tmp = nodo.getSubTag(0);
        tmp = tmp.getSubTag(0);
        tmp = tmp.getSubTag(1);
        r.profesor = tmp.contenido;
        registros.add(r);
    }

    public void terminado(HtmlTag arbol) {
        HtmlTag tabla = arbol.buscar("nrc", 2);
        HtmlTag reg;
        if (tabla != null) {
            reg = tabla.buscar("nrc", 1);
            rutaNrc = reg.buscarRuta("nrc", 0);
            rutaClave = reg.buscarRuta("clave", 0);
            rutaCr = reg.buscarRuta("cr", 0);
            rutaCup = reg.buscarRuta("cup", 0);
            rutaSec = reg.buscarRuta("sec", 0);
            rutaDis = reg.buscarRuta("dis", 0);
            rutaMateria = reg.buscarRuta("materia", 0);
            rutaProf = reg.buscarRuta("Ses/Hora/D&iacute;as/Edif/Aula/Periodo", 0);
            rutaHor = reg.buscarRuta("est", 0);
            if (rutaNrc == null || rutaClave == null || rutaCr == null || rutaCup == null || rutaSec == null || rutaDis == null || rutaHor == null || rutaMateria == null || rutaProf == null) throw new RuntimeException("El formato ha sido actualizado por favor contactanos");
            invReg = 0;
            procesar(tabla);
            if (invReg > 0) for (int x = 0; x < eventos.size(); x++) {
                ((LectorSiiauListener) eventos.get(x)).advertencia("Registros inv�lidos: " + Integer.toString(invReg));
            }
        } else throw new RuntimeException("No se encontro la tabla de informaci�n");
        for (int x = 0; x < eventos.size(); x++) {
            ((LectorSiiauListener) eventos.get(x)).terminado(registros, f);
        }
    }
}

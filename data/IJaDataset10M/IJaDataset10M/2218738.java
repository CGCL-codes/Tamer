package org.opensih.Informes;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.opensih.Modelo.DocClin;
import org.opensih.Modelo.UnidadEjecutora;
import org.opensih.Utils.Secciones.Generalidades;

@Stateless
public class GenHojaPacientes implements IGenHojaPacientes {

    private class Operacion {

        public Date fini, ffin;

        public String oportunidad;
    }

    private class Datos {

        public int cantCoord, cantUrg;

        public String nombreUE;

        public int zona;

        public Map<String, List<Operacion>> mapa;
    }

    public Workbook crearInforme(List<DocClin> allDocs, String periodo, List<UnidadEjecutora> ues) {
        Map<String, Datos> tabla = new HashMap<String, Datos>();
        for (UnidadEjecutora ue : ues) {
            Datos d = new Datos();
            d.nombreUE = ue.getNombre();
            d.zona = ue.getZona();
            d.mapa = new HashMap<String, List<Operacion>>();
            tabla.put(ue.getCodigo(), d);
        }
        List<DocClin> noIdentificados = new LinkedList<DocClin>();
        for (DocClin d : allDocs) {
            if (d.getPaciente().getExtension().length() > 10 && d.getPaciente().getId_unificado() == null) {
                noIdentificados.add(d);
            }
            Datos datos = tabla.get(d.getUe().getCodigo());
            Operacion o = new Operacion();
            o.fini = d.getFini();
            o.ffin = d.getFfin();
            Generalidades gen = new Generalidades(d.getGeneralidades().getTexto());
            o.oportunidad = gen.getOportunidad();
            if (datos.mapa.containsKey(d.getPaciente().getRoot_extension())) {
                datos.mapa.get(d.getPaciente().getRoot_extension()).add(o);
            } else {
                List<Operacion> lista = new LinkedList<Operacion>();
                lista.add(o);
                datos.mapa.put(d.getPaciente().getRoot_extension(), lista);
            }
        }
        for (Datos d : tabla.values()) {
            for (List<Operacion> lista : d.mapa.values()) {
                List<Operacion> res = recorridaUnificacion(lista);
                for (Operacion o : res) {
                    if (o.oportunidad.equals("Coordinacion")) d.cantCoord++; else d.cantUrg++;
                }
            }
        }
        List<Datos> mont = new LinkedList<Datos>();
        List<Datos> metro = new LinkedList<Datos>();
        List<Datos> polos = new LinkedList<Datos>();
        List<Datos> inter = new LinkedList<Datos>();
        for (Datos d : tabla.values()) {
            switch(d.zona) {
                case 1:
                    mont.add(d);
                    break;
                case 2:
                    metro.add(d);
                    break;
                case 3:
                    polos.add(d);
                    break;
                case 4:
                    inter.add(d);
                    break;
            }
        }
        Workbook wb = CrearInforme("/resources/InformePacientes.xls");
        Row rowC = wb.getSheetAt(0).getRow(2);
        rowC.createCell(2).setCellValue(periodo);
        int rownum = 6;
        Row rowAux = wb.getSheetAt(0).getRow(rownum);
        if (!mont.isEmpty()) {
            for (Datos d : mont) {
                wb = agregarFila(d, wb, rownum++, rowAux);
            }
            if (mont.size() > 1) agregarArea(mont, "Total Montevideo", wb, rownum++, rowAux);
        }
        if (!metro.isEmpty()) {
            for (Datos d : metro) {
                wb = agregarFila(d, wb, rownum++, rowAux);
            }
            if (metro.size() > 1) agregarArea(metro, "Total Metropolitana", wb, rownum++, rowAux);
        }
        if (!polos.isEmpty()) {
            for (Datos d : polos) {
                wb = agregarFila(d, wb, rownum++, rowAux);
            }
            if (polos.size() > 1) agregarArea(polos, "Total Polos Regionales", wb, rownum++, rowAux);
        }
        if (!inter.isEmpty()) {
            for (Datos d : inter) {
                wb = agregarFila(d, wb, rownum++, rowAux);
            }
            if (inter.size() > 1) agregarArea(inter, "Total Interior", wb, rownum++, rowAux);
        }
        wb.getSheetAt(1).getRow(3).createCell(2).setCellValue(periodo);
        wb.getSheetAt(1).getRow(5).getCell(2).setCellValue(noIdentificados.size());
        rownum = 10;
        rowAux = wb.getSheetAt(1).getRow(10);
        for (DocClin d : noIdentificados) {
            Row row = wb.getSheetAt(1).createRow(rownum++);
            int celda = 0;
            row.createCell(celda).setCellValue(d.getPaciente().getExtension());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            row.createCell(celda).setCellValue(d.getPaciente().getNombre());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            row.createCell(celda).setCellValue(d.getPaciente().getApellido());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            row.createCell(celda).setCellValue(d.getPaciente().getNacimiento());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            row.createCell(celda).setCellValue(d.getPaciente().getSexo());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            row.createCell(celda).setCellValue(d.getUe().getNombre());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            row.createCell(celda).setCellValue(d.getServicio().getNombre());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            row.createCell(celda).setCellValue(d.getAutor().toString());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            row.createCell(celda).setCellValue(d.getSetID());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
            celda++;
            Generalidades gen = new Generalidades(d.getGeneralidades().getTexto());
            row.createCell(celda).setCellValue(gen.getOportunidad());
            row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
        }
        return wb;
    }

    private Workbook agregarFila(Datos d, Workbook wb, int rownum, Row rowAux) {
        Row row = wb.getSheetAt(0).createRow(rownum);
        int celda = 1;
        row.createCell(celda).setCellValue(d.nombreUE);
        row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
        celda++;
        row.createCell(celda).setCellValue(d.cantCoord);
        row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
        celda++;
        row.createCell(celda).setCellValue(d.cantUrg);
        row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
        celda++;
        row.createCell(celda).setCellValue(d.cantCoord + d.cantUrg);
        row.getCell(celda).setCellStyle(rowAux.getCell(celda).getCellStyle());
        return wb;
    }

    private Workbook agregarArea(List<Datos> lista, String area, Workbook wb, int rownum, Row rowAux) {
        Row row = wb.getSheetAt(0).createRow(rownum);
        Datos d = new Datos();
        d.nombreUE = area;
        for (Datos aux : lista) {
            d.cantCoord += aux.cantCoord;
            d.cantUrg += aux.cantUrg;
        }
        int celda = 1;
        row.createCell(celda).setCellValue(d.nombreUE);
        row.getCell(celda).setCellStyle(rowAux.getCell(4).getCellStyle());
        celda++;
        row.createCell(celda).setCellValue(d.cantCoord);
        row.getCell(celda).setCellStyle(rowAux.getCell(4).getCellStyle());
        celda++;
        row.createCell(celda).setCellValue(d.cantUrg);
        row.getCell(celda).setCellStyle(rowAux.getCell(4).getCellStyle());
        celda++;
        row.createCell(celda).setCellValue(d.cantCoord + d.cantUrg);
        row.getCell(celda).setCellStyle(rowAux.getCell(4).getCellStyle());
        return wb;
    }

    private List<Operacion> recorridaUnificacion(List<Operacion> lista) {
        if (lista.size() > 1) {
            boolean unifico = true;
            while (unifico) {
                Iterator<Operacion> it1 = lista.iterator();
                unifico = false;
                Operacion o1 = null, o2 = null;
                while (it1.hasNext() && !unifico) {
                    o1 = it1.next();
                    Iterator<Operacion> it2 = lista.iterator();
                    while (it2.hasNext() && !unifico) {
                        o2 = it2.next();
                        if (o1 != o2 && ((o1.fini.getTime() >= o2.fini.getTime() && o1.fini.getTime() <= o2.ffin.getTime()) || (o2.fini.getTime() >= o1.fini.getTime() && o2.fini.getTime() <= o1.ffin.getTime()))) {
                            unifico = true;
                        }
                    }
                }
                if (unifico) {
                    if (o1.fini.getTime() > o2.fini.getTime()) {
                        o1.fini = o2.fini;
                    }
                    if (o1.ffin.getTime() < o2.ffin.getTime()) {
                        o1.ffin = o2.ffin;
                    }
                    lista.remove(o2);
                }
            }
        }
        return lista;
    }

    private Workbook CrearInforme(String nombre) {
        InputStream inp;
        Workbook wb = new HSSFWorkbook();
        try {
            inp = this.getClass().getResourceAsStream(nombre);
            wb = WorkbookFactory.create(inp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }
}

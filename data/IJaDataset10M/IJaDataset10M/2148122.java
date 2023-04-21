package org.hmaciel.descop.otros.InformeSinadi;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hmaciel.descop.ejb.act.DocClinBean;
import org.hmaciel.descop.ejb.controladores.IBuscarPacientes;
import org.hmaciel.descop.ejb.entity.PersonBean;
import org.hmaciel.descop.ejb.role.PatientBean;
import org.opensih.servicioJMX.invocador.InvocadorService2;
import org.opensih.servicioJMX.invocador.InvocadorServiceBean2;

@Stateless
public class GenHojaSinadi implements IGenHojaSinadi {

    @EJB
    IBuscarPacientes busqPac;

    public Workbook crearInforme(List<DocClinBean> allDocs, Date fecha) {
        SimpleDateFormat sdfmes = new SimpleDateFormat("MM");
        SimpleDateFormat sdfanio = new SimpleDateFormat("yyyy");
        Workbook wb = CrearInforme();
        InvocadorService2 inv = InvocadorServiceBean2.getInstance();
        Row rowDate = wb.getSheetAt(0).getRow(1);
        rowDate.getCell(1).setCellValue(inv.getUnidadEjecutora());
        rowDate.getCell(7).setCellValue(sdfmes.format(fecha));
        rowDate.getCell(12).setCellValue(sdfanio.format(fecha));
        Set<String> idents = new HashSet<String>();
        for (DocClinBean doc : allDocs) {
            idents.add(doc.getRecordTarget().getRol().getii().getExtension());
        }
        List<String> ids = new LinkedList<String>(idents);
        Map<String, PatientBean> pacientes = busqPac.buscarListaPacientes(ids);
        int[][] resF1 = new int[3][9];
        int[][] resM1 = new int[3][9];
        int[][] resF2 = new int[3][9];
        int[][] resM2 = new int[3][9];
        int[][] resF3 = new int[2][5];
        for (DocClinBean doc : allDocs) {
            String id = doc.getRecordTarget().getRol().getii().getExtension();
            if (pacientes.get(id) != null) {
                PersonBean perBean = (PersonBean) pacientes.get(id).getPlayer();
                int edad = calcularEdad(doc.getServEvent().getEffectiveTimeFin(), perBean);
                String sexo = perBean.getAdministrativeGenderCode().getCode();
                if (sexo.equals("F")) {
                    if (doc.getNonXmlBodyBean().getOportunidad2().equals("Coordinacion")) {
                        if (doc.getNonXmlBodyBean().getEsInternado()) resF1[0] = sumarPtoEdad(resF1[0], edad); else resF1[1] = sumarPtoEdad(resF1[1], edad);
                    } else resF1[2] = sumarPtoEdad(resF1[2], edad);
                    if (doc.getNonXmlBodyBean().getCategoria().equals("corriente")) resF2[0] = sumarPtoEdad(resF2[0], edad); else if (doc.getNonXmlBodyBean().getCategoria().equals("mayor")) resF2[1] = sumarPtoEdad(resF2[1], edad); else if (doc.getNonXmlBodyBean().getCategoria().equals("alta")) resF2[2] = sumarPtoEdad(resF2[2], edad);
                } else if (sexo.equals("M")) {
                    if (doc.getNonXmlBodyBean().getOportunidad2().equals("Coordinacion")) {
                        if (doc.getNonXmlBodyBean().getEsInternado()) resM1[0] = sumarPtoEdad(resM1[0], edad); else resM1[1] = sumarPtoEdad(resM1[1], edad);
                    } else resM1[2] = sumarPtoEdad(resM1[2], edad);
                    if (doc.getNonXmlBodyBean().getCategoria().equals("corriente")) resM2[0] = sumarPtoEdad(resM2[0], edad); else if (doc.getNonXmlBodyBean().getCategoria().equals("mayor")) resM2[1] = sumarPtoEdad(resM2[1], edad); else if (doc.getNonXmlBodyBean().getCategoria().equals("alta")) resM2[2] = sumarPtoEdad(resM2[2], edad);
                }
                if (doc.getNonXmlBodyBean().getCodigosProc().contains("GIN2018")) resF3[0] = sumarPtoEdad2(resF3[0], edad); else if (doc.getNonXmlBodyBean().getCodigosProc().contains("GIN2019")) resF3[1] = sumarPtoEdad2(resF3[1], edad);
            }
        }
        for (int j = 2; j < 11; j++) {
            wb.getSheetAt(0).getRow(8).getCell(j).setCellValue(resM1[0][j - 2]);
            wb.getSheetAt(0).getRow(9).getCell(j).setCellValue(resM1[1][j - 2]);
            wb.getSheetAt(0).getRow(11).getCell(j).setCellValue(resM1[2][j - 2]);
            wb.getSheetAt(0).getRow(10).getCell(j).setCellValue(resM1[0][j - 2] + resM1[1][j - 2]);
            wb.getSheetAt(0).getRow(12).getCell(j).setCellValue(resM1[0][j - 2] + resM1[1][j - 2] + resM1[2][j - 2]);
            wb.getSheetAt(0).getRow(8).getCell(j + 10).setCellValue(resF1[0][j - 2]);
            wb.getSheetAt(0).getRow(9).getCell(j + 10).setCellValue(resF1[1][j - 2]);
            wb.getSheetAt(0).getRow(11).getCell(j + 10).setCellValue(resF1[2][j - 2]);
            wb.getSheetAt(0).getRow(10).getCell(j + 10).setCellValue(resF1[0][j - 2] + resF1[1][j - 2]);
            wb.getSheetAt(0).getRow(12).getCell(j + 10).setCellValue(resF1[0][j - 2] + resF1[1][j - 2] + resF1[2][j - 2]);
        }
        int sumM0 = sumatoria(resM1[0]);
        int sumM1 = sumatoria(resM1[1]);
        int sumM2 = sumatoria(resM1[2]);
        wb.getSheetAt(0).getRow(8).getCell(11).setCellValue(sumM0);
        wb.getSheetAt(0).getRow(9).getCell(11).setCellValue(sumM1);
        wb.getSheetAt(0).getRow(10).getCell(11).setCellValue(sumM0 + sumM1);
        wb.getSheetAt(0).getRow(11).getCell(11).setCellValue(sumM2);
        wb.getSheetAt(0).getRow(12).getCell(11).setCellValue(sumM0 + sumM1 + sumM2);
        int sumF0 = sumatoria(resF1[0]);
        int sumF1 = sumatoria(resF1[1]);
        int sumF2 = sumatoria(resF1[2]);
        wb.getSheetAt(0).getRow(8).getCell(21).setCellValue(sumF0);
        wb.getSheetAt(0).getRow(9).getCell(21).setCellValue(sumF1);
        wb.getSheetAt(0).getRow(10).getCell(21).setCellValue(sumF0 + sumF1);
        wb.getSheetAt(0).getRow(11).getCell(21).setCellValue(sumF2);
        wb.getSheetAt(0).getRow(12).getCell(21).setCellValue(sumF0 + sumF1 + sumF2);
        wb.getSheetAt(0).getRow(8).getCell(22).setCellValue(sumF0 + sumM0);
        wb.getSheetAt(0).getRow(9).getCell(22).setCellValue(sumF1 + sumM1);
        wb.getSheetAt(0).getRow(10).getCell(22).setCellValue(sumF0 + sumF1 + sumM0 + sumM1);
        wb.getSheetAt(0).getRow(11).getCell(22).setCellValue(sumF2 + sumM2);
        wb.getSheetAt(0).getRow(12).getCell(22).setCellValue(sumF0 + sumF1 + sumF2 + sumM0 + sumM1 + sumM2);
        for (int j = 2; j < 11; j++) {
            wb.getSheetAt(0).getRow(20).getCell(j).setCellValue(resM2[0][j - 2]);
            wb.getSheetAt(0).getRow(21).getCell(j).setCellValue(resM2[1][j - 2]);
            wb.getSheetAt(0).getRow(22).getCell(j).setCellValue(resM2[2][j - 2]);
            wb.getSheetAt(0).getRow(24).getCell(j).setCellValue(resM2[0][j - 2] + resM2[1][j - 2] + resM2[2][j - 2]);
            wb.getSheetAt(0).getRow(20).getCell(j + 10).setCellValue(resF2[0][j - 2]);
            wb.getSheetAt(0).getRow(21).getCell(j + 10).setCellValue(resF2[1][j - 2]);
            wb.getSheetAt(0).getRow(22).getCell(j + 10).setCellValue(resF2[2][j - 2]);
            wb.getSheetAt(0).getRow(24).getCell(j + 10).setCellValue(resF2[0][j - 2] + resF2[1][j - 2] + resF2[2][j - 2]);
        }
        sumM0 = sumatoria(resM2[0]);
        sumM1 = sumatoria(resM2[1]);
        sumM2 = sumatoria(resM2[2]);
        wb.getSheetAt(0).getRow(20).getCell(11).setCellValue(sumM0);
        wb.getSheetAt(0).getRow(21).getCell(11).setCellValue(sumM1);
        wb.getSheetAt(0).getRow(22).getCell(11).setCellValue(sumM2);
        wb.getSheetAt(0).getRow(24).getCell(11).setCellValue(sumM0 + sumM1 + sumM2);
        sumF0 = sumatoria(resF2[0]);
        sumF1 = sumatoria(resF2[1]);
        sumF2 = sumatoria(resF2[2]);
        wb.getSheetAt(0).getRow(20).getCell(21).setCellValue(sumF0);
        wb.getSheetAt(0).getRow(21).getCell(21).setCellValue(sumF1);
        wb.getSheetAt(0).getRow(22).getCell(21).setCellValue(sumF2);
        wb.getSheetAt(0).getRow(24).getCell(21).setCellValue(sumF0 + sumF1 + sumF2);
        wb.getSheetAt(0).getRow(20).getCell(22).setCellValue(sumF0 + sumM0);
        wb.getSheetAt(0).getRow(21).getCell(22).setCellValue(sumF1 + sumM1);
        wb.getSheetAt(0).getRow(22).getCell(22).setCellValue(sumF2 + sumM2);
        wb.getSheetAt(0).getRow(24).getCell(22).setCellValue(sumF0 + sumF1 + sumF2 + sumM0 + sumM1 + sumM2);
        for (int j = 2; j < 7; j++) {
            wb.getSheetAt(0).getRow(30).getCell(j).setCellValue(resF3[0][j - 2]);
            wb.getSheetAt(0).getRow(31).getCell(j).setCellValue(resF3[1][j - 2]);
            wb.getSheetAt(0).getRow(32).getCell(j).setCellValue(resF3[0][j - 2] + resF3[1][j - 2]);
        }
        sumF0 = sumatoria(resF3[0]);
        sumF1 = sumatoria(resF3[1]);
        wb.getSheetAt(0).getRow(30).getCell(7).setCellValue(sumF0);
        wb.getSheetAt(0).getRow(31).getCell(7).setCellValue(sumF1);
        wb.getSheetAt(0).getRow(32).getCell(7).setCellValue(sumF0 + sumF1);
        return wb;
    }

    public int[] sumarPtoEdad(int[] totales, int edad) {
        if (edad < 0) totales[8]++; else if (edad < 1) totales[0]++; else if (edad <= 4) totales[1]++; else if (edad <= 14) totales[2]++; else if (edad <= 19) totales[3]++; else if (edad <= 44) totales[4]++; else if (edad <= 64) totales[5]++; else if (edad <= 74) totales[6]++; else totales[7]++;
        return totales;
    }

    public int[] sumarPtoEdad2(int[] totales, int edad) {
        if (edad < 0) totales[4]++; else if (edad < 15) totales[0]++; else if (edad <= 19) totales[1]++; else if (edad <= 44) totales[2]++; else totales[3]++;
        return totales;
    }

    public int calcularEdad(Date cirugia, PersonBean person) {
        if (person.getBirthTime() != null) {
            int factor = 0;
            Calendar birth = new GregorianCalendar();
            Calendar today = new GregorianCalendar();
            birth.setTime(person.getBirthTime().getDate());
            today.setTime(cirugia);
            if (today.get(Calendar.MONTH) <= birth.get(Calendar.MONTH)) {
                if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)) {
                    if (today.get(Calendar.DATE) < birth.get(Calendar.DATE)) {
                        factor = -1;
                    }
                } else {
                    factor = -1;
                }
            }
            return (today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)) + factor;
        } else return -1;
    }

    public int sumatoria(int[] fila) {
        int sum = 0;
        for (int i = 0; i < fila.length; i++) {
            sum += fila[i];
        }
        return sum;
    }

    public Workbook CrearInforme() {
        InputStream inp;
        Workbook wb = new HSSFWorkbook();
        try {
            inp = this.getClass().getResourceAsStream("/resources/sinadi.xls");
            wb = WorkbookFactory.create(inp);
        } catch (Exception e) {
            System.out.println("Problema al crear el Workbook del Excel");
        }
        return wb;
    }
}

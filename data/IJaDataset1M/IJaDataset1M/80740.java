package org.guestshome.businessobjects.statistics;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.*;
import java.math.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.hssf.record.formula.functions.*;
import java.io.*;
import org.extutils.web.*;
import org.guestshome.businessobjects.BusinessObjectsFacade;
import org.guestshome.commons.*;
import org.guestshome.entities.Facility;
import org.guestshome.entities.*;
import org.guestshome.entities.GuestReception;
import org.guestshome.entities.Person;
import org.guestshome.entities.ReceptionType;
import org.guestshome.entities.WaitingList;
import org.sqlutils.ListCommand;
import org.sqlutils.ListResponse;
import org.sqlutils.jpa.JPAMethods;
import org.sqlutils.jpa.JPASelectStatement;
import org.sqlutils.logger.Logger;

/**
 * <p>Title: GuestsHome application</p>
 * <p>Description: Business object used to generate statistics about
 * guests pathologies; statistics are expressed in XLS format.</p>
 * <p>Copyright: Copyright (C) 2009 Informatici senza frontiere</p>
 *
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * @author Mauro Carniel
 * @version 1.0
 *
 */
public class GuestsPathologiesStatsBO {

    /**
	 * @return statistics about guests leaving, expressed in XLS format
	 * @throws an exception in case of errors
	 */
    public byte[] getGuestsPathologiesStats(String username, EntityManager em, int year, UserInfo userInfo, PropertiesResourcesFactory factory) throws Throwable {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFRow row = null;
            HSSFCell cell = null;
            HSSFSheet s = wb.createSheet();
            HSSFSheet newsheet = null;
            ListResponse<Pathology> p = BusinessObjectsFacade.getInstance().getPathologiesBO().getPathologies(username, em, new ListCommand(username));
            int[] pathologyIds = new int[p.getValueObjectList().size()];
            for (int i = 0; i < p.getValueObjectList().size(); i++) pathologyIds[i] = p.getValueObjectList().get(i).getId();
            ListResponse<Facility> res = BusinessObjectsFacade.getInstance().getEnabledFacilitiesBO().getUserFacilities(username, em, new ListCommand(username));
            String[] months = new String[] { factory.getResources().getResource("January"), factory.getResources().getResource("February"), factory.getResources().getResource("March"), factory.getResources().getResource("April"), factory.getResources().getResource("May"), factory.getResources().getResource("June"), factory.getResources().getResource("July"), factory.getResources().getResource("August"), factory.getResources().getResource("September"), factory.getResources().getResource("October"), factory.getResources().getResource("November"), factory.getResources().getResource("December") };
            HSSFCellStyle csTitle = wb.createCellStyle();
            csTitle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
            HSSFFont f = wb.createFont();
            f.setFontHeightInPoints((short) 14);
            f.setBoldweight(f.BOLDWEIGHT_BOLD);
            csTitle.setFont(f);
            HSSFCellStyle csBold = wb.createCellStyle();
            csBold.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
            csBold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            csBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            csBold.setBorderRight(HSSFCellStyle.BORDER_THIN);
            csBold.setBorderTop(HSSFCellStyle.BORDER_THIN);
            csBold.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            csBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            f = wb.createFont();
            f.setBoldweight(f.BOLDWEIGHT_BOLD);
            csBold.setFont(f);
            HSSFCellStyle csBold2 = wb.createCellStyle();
            csBold2.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
            csBold2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            csBold2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            csBold2.setBorderRight(HSSFCellStyle.BORDER_THIN);
            csBold2.setBorderTop(HSSFCellStyle.BORDER_THIN);
            csBold2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            csBold2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            f = wb.createFont();
            f.setBoldweight(f.BOLDWEIGHT_BOLD);
            csBold2.setFont(f);
            csBold2.setWrapText(true);
            HSSFCellStyle csBold3 = wb.createCellStyle();
            csBold3.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
            csBold3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            csBold3.setBorderRight(HSSFCellStyle.BORDER_THIN);
            csBold3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            csBold3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            f = wb.createFont();
            f.setBoldweight(f.BOLDWEIGHT_BOLD);
            csBold3.setFont(f);
            csBold3.setWrapText(true);
            HSSFCellStyle csIntNum = wb.createCellStyle();
            csIntNum.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
            csIntNum.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            csIntNum.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            csIntNum.setBorderRight(HSSFCellStyle.BORDER_THIN);
            csIntNum.setBorderTop(HSSFCellStyle.BORDER_THIN);
            csIntNum.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFCellStyle csIntNum2 = wb.createCellStyle();
            csIntNum2.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
            csIntNum2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            csIntNum2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            csIntNum2.setBorderRight(HSSFCellStyle.BORDER_THIN);
            csIntNum2.setBorderTop(HSSFCellStyle.BORDER_THIN);
            csIntNum2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            csIntNum2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            csIntNum2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            s.setColumnWidth(0, 3000);
            s.setColumnWidth(1, 4000);
            s.setColumnWidth(p.getValueObjectList().size() + 2, 5000);
            int r = 0;
            List<Facility> facilities = new ArrayList<Facility>();
            facilities.add(null);
            for (Facility vo : res.getValueObjectList()) facilities.add(vo);
            for (Facility vo : facilities) {
                row = s.createRow(r);
                cell = row.createCell(0);
                cell.setCellStyle(csTitle);
                cell.setCellValue(factory.getResources().getResource("Guests pathologies") + " " + (vo == null ? "" : vo.getDescription()));
                r++;
                r++;
                row = s.createRow(r);
                cell = row.createCell(1);
                cell.setCellStyle(csBold);
                cell.setCellValue(factory.getResources().getResource("Total guests"));
                for (int j = 0; j < p.getValueObjectList().size(); j++) {
                    s.setColumnWidth(j + 2, 5000);
                    cell = row.createCell(j + 2);
                    cell.setCellStyle(csBold);
                    cell.setCellValue(p.getValueObjectList().get(j).getDescription());
                }
                cell = row.createCell(p.getValueObjectList().size() + 2);
                cell.setCellStyle(csBold);
                cell.setCellValue(factory.getResources().getResource("More pathologies"));
                Object[][] data = getGuestsPathologiesStats(username, em, year, vo == null ? null : vo.getId(), pathologyIds);
                r++;
                int[] totals = new int[pathologyIds.length + 2];
                for (int i = 0; i < 12; i++) {
                    row = s.createRow(r);
                    cell = row.createCell(0);
                    cell.setCellValue(months[i]);
                    cell.setCellStyle(csBold);
                    for (int j = 0; j < pathologyIds.length + 2; j++) {
                        cell = row.createCell(j + 1);
                        if (data[i][j] != null) {
                            cell.setCellValue((Integer) data[i][j]);
                            cell.setCellStyle(csIntNum);
                            totals[j] += (Integer) data[i][j];
                        }
                    }
                    r++;
                }
                row = s.createRow(r);
                cell = row.createCell(0);
                cell.setCellValue(factory.getResources().getResource("Totals"));
                cell.setCellStyle(csBold);
                for (int j = 0; j < pathologyIds.length + 2; j++) {
                    cell = row.createCell(j + 1);
                    cell.setCellValue(totals[j]);
                    cell.setCellStyle(csIntNum2);
                }
                r = r + 3;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            out.close();
            return out.toByteArray();
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
	 * @return number of guests hosted in the specified year, for each month of the year
	 * If "facilityId" is not null, then statistics are filtered per specified facility.
	 * @throws an exception in case of errors
	 */
    private Object[][] getGuestsPathologiesStats(String username, EntityManager em, int year, Integer facilityId, int[] pathologyIds) throws Throwable {
        try {
            Calendar cal = Calendar.getInstance();
            Date startDate = null;
            Date endDate = null;
            int day;
            Object[][] res = new Object[12][1 + pathologyIds.length + 1];
            Query q = null;
            List<Object> params = new ArrayList<Object>();
            List list = null;
            for (int i = 0; i < 12; i++) {
                cal.set(cal.YEAR, year);
                cal.set(cal.MONTH, i);
                cal.set(cal.DAY_OF_MONTH, 1);
                cal.set(cal.HOUR_OF_DAY, 0);
                cal.set(cal.MINUTE, 0);
                cal.set(cal.SECOND, 0);
                cal.set(cal.MILLISECOND, 0);
                startDate = new java.sql.Date(cal.getTimeInMillis());
                switch(i) {
                    case 1:
                        day = 28;
                        break;
                    case 10:
                        day = 30;
                        break;
                    case 3:
                        day = 30;
                        break;
                    case 5:
                        day = 30;
                        break;
                    case 8:
                        day = 30;
                        break;
                    default:
                        day = 31;
                        break;
                }
                cal.set(cal.MONTH, i);
                cal.set(cal.DAY_OF_MONTH, day);
                cal.set(cal.HOUR_OF_DAY, 23);
                cal.set(cal.MINUTE, 59);
                cal.set(cal.SECOND, 59);
                cal.set(cal.MILLISECOND, 999);
                endDate = new java.sql.Date(cal.getTimeInMillis());
                String sql = "select count(gr) from GuestReception gr " + "where gr.deleted = ?1 and " + " gr.entryDate<= ?3 and " + "(gr.leavingDate>= ?2 or gr.leavingDate is null)";
                if (facilityId != null) sql += " and gr.facilityId=" + facilityId;
                q = em.createQuery(sql);
                params.clear();
                params.add(Consts.FLAG_N);
                params.add(startDate);
                params.add(endDate);
                list = JPAMethods.getResultList(username, q, params);
                res[i][0] = new Integer(list.get(0).toString());
                for (int j = 0; j < pathologyIds.length; j++) {
                    sql = "select count(gr) from GuestReception gr,GuestPathology gp " + "where gr.deleted = ?1 and " + " gr.entryDate<= ?3 and " + "(gr.leavingDate>= ?2 or gr.leavingDate is null) and " + " gr.personId=gp.personId and gp.id=" + pathologyIds[j];
                    if (facilityId != null) sql += " and gr.facilityId=" + facilityId;
                    q = em.createQuery(sql);
                    params.clear();
                    params.add(Consts.FLAG_N);
                    params.add(startDate);
                    params.add(endDate);
                    list = JPAMethods.getResultList(username, q, params);
                    res[i][j + 1] = new Integer(list.get(0).toString());
                }
                sql = "select count(gr) from GuestReception gr " + "where gr.deleted = ?1 and " + " gr.entryDate<= ?3 and " + "(gr.leavingDate>= ?2 or gr.leavingDate is null) and " + " gr.personId in (select gp.personId from GuestPathology gp " + " where gp.deleted = ?1 group by gp.personId having count(gp.personId) > 1 )";
                if (facilityId != null) sql += " and gr.facilityId=" + facilityId;
                q = em.createQuery(sql);
                params.clear();
                params.add(Consts.FLAG_N);
                params.add(startDate);
                params.add(endDate);
                list = JPAMethods.getResultList(username, q, params);
                res[i][pathologyIds.length + 1] = new Integer(list.get(0).toString());
            }
            return res;
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        }
    }
}

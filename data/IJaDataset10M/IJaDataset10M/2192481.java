package com.kongur.network.erp.report.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Value;
import com.kongur.network.erp.query.SearchReportQuery;
import com.kongur.network.erp.report.ExcelStructure;
import com.kongur.network.erp.report.exception.ReportDownloadException;
import com.kongur.network.erp.report.service.ReportDownloadService;

/**
 * �������ط��������
 */
public abstract class AbstractReportDownloadService implements ReportDownloadService {

    private static final Log log = LogFactory.getLog(AbstractReportDownloadService.class);

    @Value("${upload.reportFilesPath}")
    private String reportFilesPath;

    /**
     * ��ɱ����ļ�
     * @return �����ļ���ַ
     * @throws ReportDownloadException
     * @see com.hundsun.bible.report.service.ReportDownloadService#createReportFile()
     */
    public abstract String createReportFile(SearchReportQuery query);

    /**
     * ���excel�ļ�
     * @param excelStructure
     * @return �����ļ���
     * @throws ReportDownloadException
     */
    public String createExcel(ExcelStructure excelStructure) throws ReportDownloadException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet st = wb.createSheet("report");
        HSSFRow row;
        HSSFCell cell;
        Object h = excelStructure.getHeader();
        String[] title = excelStructure.getTitle();
        List<String[]> data = excelStructure.getData();
        int currentrow = 0;
        int currentcell = 0;
        if (title != null && title.length > 0) {
            currentcell = 0;
            for (int i = 0; i < title.length; i++) {
                row = st.createRow(currentrow);
                cell = row.createCell((short) currentcell);
                cell.setCellValue(new HSSFRichTextString(title[i]));
                currentrow++;
            }
        }
        if (h instanceof String[][]) {
            String[][] header = (String[][]) h;
            int hIndex = 0;
            for (int i = 0; i < header[0].length; ) {
                int cells = Integer.parseInt(header[0][i + 1]);
                row = st.createRow(currentrow);
                cell = row.createCell((short) hIndex);
                if (cells > 1) {
                    st.addMergedRegion(new Region(currentrow, (short) hIndex, currentrow, (short) (hIndex + cells - 1)));
                    hIndex = hIndex + cells - 1;
                }
                cell.setCellValue(new HSSFRichTextString(header[0][i]));
                i = i + 2;
                hIndex++;
            }
            currentrow++;
            for (int j = 0; j < header[1].length; j++) {
                row = st.createRow(currentrow);
                cell = row.createCell((short) j);
                cell.setCellValue(new HSSFRichTextString(header[1][j]));
            }
            currentrow++;
        } else if (h instanceof String[]) {
            String[] header = (String[]) h;
            for (int i = 0; i < header.length; i++) {
                row = st.createRow(currentrow);
                cell = row.createCell((short) i);
                cell.setCellValue(new HSSFRichTextString(header[i]));
            }
            currentrow++;
        }
        for (Iterator<String[]> iter = data.iterator(); iter.hasNext(); ) {
            String[] datas = iter.next();
            for (int i = 0; i < datas.length; i++) {
                row = st.createRow(currentrow);
                cell = row.createCell((short) (currentcell + i));
                cell.setCellValue(new HSSFRichTextString(datas[i]));
            }
            currentrow++;
        }
        return writeFile(wb);
    }

    /**
     * д�ļ�
     * @param wb
     */
    private String writeFile(HSSFWorkbook wb) {
        String transDate = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String realPath = reportFilesPath + getReportEnum().getCode().toLowerCase() + "_report_" + transDate + ".xls";
        FileOutputStream fileOut = null;
        try {
            File dirPath = new File(reportFilesPath);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            fileOut = new FileOutputStream(realPath);
            wb.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            log.error("�ļ�" + realPath + "δ�ҵ�!");
            throw new ReportDownloadException("�ļ�" + realPath + "δ�ҵ�!");
        } catch (IOException e) {
            log.error("error then createExcel!");
            throw new ReportDownloadException("error then writeFile!");
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    log.error("error then createExcel!");
                    throw new ReportDownloadException("error then writeFile!");
                }
            }
        }
        return realPath;
    }
}

package net.excel.report.base;

import java.util.Map;
import net.excel.report.Logger;
import net.excel.report.base.element.Templet;
import net.excel.report.config.ReportConfig.SheetConfig;
import jxl.Range;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;

public abstract class BaseReportWriter implements IReportWriter {

    private static Logger log = Logger.getLogger(BaseReportWriter.class);

    public abstract void analysisTemplet(WritableSheet sheet) throws Exception;

    public abstract void writeData(Parameter param) throws Exception;

    public void setReportConfig(SheetConfig sheetConfig) {
        this.sheetConfig = sheetConfig;
    }

    protected SheetConfig sheetConfig = null;

    private ITempletContainer container = null;

    public void setDataSources(Map dataSources) {
        this.dataSources = dataSources;
    }

    public void setParameters(Map params) {
        this.params = params;
    }

    /**
     * ��ȡģ�����ڵ�������Ϣ,�����ڰ��ͼƬ��Ϣ,��������Ϣ,
     * �ϲ���Ԫ����Ϣ��,��Ϊ��Щ��Ϣ����ͨ�����Ԫ��Ŀ�������
     * ������,ֻ�ܱ�ʶ����һ��һ���ֶ�����.
     * @param sheet
     */
    protected void getMergedCells(WritableSheet sheet) {
        Range[] mergedCells = sheet.getMergedCells();
        if (mergedCells.length <= 0) return;
        for (int i = 0; i < mergedCells.length; i++) {
            container.addMergedCell(mergedCells[i]);
        }
    }

    /**
     * ��ȡģ������ͼƬ��Ϣ������ͼƬ��Ϣװ�뵽��Ӧ��������ȥ��
     * @param sheet ģ��sheet
     */
    protected void getImages(WritableSheet sheet) {
        int images = sheet.getNumberOfImages();
        for (int i = 0; i < images; i++) {
            WritableImage img = sheet.getImage(i);
            container.addImage(img);
        }
    }

    /**
     * ȡ�ñ���Ķ���ģ����������
     * @return ITempletContainer
     */
    protected ITempletContainer getContainer() {
        if (null == container) {
            container = new Templet(null);
        }
        return container;
    }

    /**
     * ɾ��ģ�����
     * @param param
     */
    protected void deleteTemplet(Parameter param) {
        int beginRow = this.container.getBeginRow() + 1;
        int endRow = this.container.getEndRow();
        for (int i = beginRow; i < endRow; i++) {
            param.sheet.removeRow(beginRow);
        }
    }

    /**
     * ɾ����а��ģ��ͼƬ����
     * @param param
     */
    protected void deleteAllTempletImages(Parameter param) {
        this.container.removeAllTempletImages(param.sheet);
    }

    /**
     * ȡ��ָ�������Ƿ���ںϲ��ĵ�Ԫ���������򷵻غϲ���Ԫ�������
     * @param sheet		ģ���ļ�
     * @param col		ָ����
     * @param row		ָ����
     * @return			null ��ָ������λ��û�кϲ���Ԫ��
     * 					��null ����Range����
     */
    protected Range getCellRange(WritableSheet sheet, int col, int row) {
        Range[] mergedCells = sheet.getMergedCells();
        if (mergedCells.length <= 0) return null;
        for (int i = 0; i < mergedCells.length; i++) {
            if (mergedCells[i].getTopLeft().getColumn() == col && mergedCells[i].getTopLeft().getRow() == row) {
                return mergedCells[i];
            }
        }
        return null;
    }

    protected Map params = null;

    protected Map dataSources = null;
}

package com.pn.action;

import java.util.List;
import com.kn.core.struts.base.BaseAction;
import com.pn.be.BaoGiaSanPhamTrongNuocBe;
import com.pn.be.BaoGiaSanPhamXuatKhauBe;
import com.pn.services.GiaBanThanhPhamServices;
import com.pn.services.ThanhPhamServices;
import com.pn.utils.UIMessages;

public class BaoCaoGiaBanCacKhuVuc extends BaseAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private ThanhPhamServices thanhPhamServices;

    private GiaBanThanhPhamServices giaBanThanhPhamServices;

    List<BaoGiaSanPhamTrongNuocBe> listBangBaoGia;

    List<BaoGiaSanPhamXuatKhauBe> listBangBaoGiaXK;

    private Integer numOfRow;

    private String isCoThue;

    private String searchText;

    private Long option;

    private String isOneToSeven;

    private String isSevenToEnd;

    public String xemTuOneToSeven() {
        searchText = "";
        isSevenToEnd = "false";
        isOneToSeven = "true";
        return SUCCESS;
    }

    public String xemTuSevenToEnd() {
        searchText = "";
        isOneToSeven = "false";
        isSevenToEnd = "true";
        return SUCCESS;
    }

    public String processNhomXuatKhau() {
        numOfRow = giaBanThanhPhamServices.getCountBangBaoGiaSPXK();
        String pageNum = this.getRequest().getParameter("d-7075089-p");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        isCoThue = "false";
        listBangBaoGiaXK = giaBanThanhPhamServices.getBangBaoGiaSPXuatKhau(new Integer(pageNum), isCoThue);
        searchText = "";
        return INPUT;
    }

    public String processSearchThanhPhamXK() {
        if (searchText != null && searchText.trim().length() > 0) {
            try {
                if (searchText.contains(",")) {
                    String[] arr = searchText.split(",");
                    searchText = arr[0].trim();
                }
                isCoThue = "false";
                listBangBaoGiaXK = giaBanThanhPhamServices.getSearchBangBaoGiaSPXuatKhau(isCoThue, searchText, option);
                numOfRow = listBangBaoGiaXK.size();
            } catch (Exception e) {
                addActionError(UIMessages.VIEW_ERROR);
                return INPUT;
            }
        }
        searchText = "";
        return INPUT;
    }

    public String process() {
        searchText = "";
        numOfRow = giaBanThanhPhamServices.getCountBangBaoGiaSPTrongNuoc();
        String pageNum = this.getRequest().getParameter("d-7075089-p");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        isCoThue = "false";
        listBangBaoGia = giaBanThanhPhamServices.getBangBaoGiaSPTrongNuoc(new Integer(pageNum), isCoThue);
        return SUCCESS;
    }

    public String processCoThue() {
        searchText = "";
        numOfRow = giaBanThanhPhamServices.getCountBangBaoGiaSPTrongNuoc();
        String pageNum = this.getRequest().getParameter("d-7075089-p");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        isCoThue = "true";
        listBangBaoGia = giaBanThanhPhamServices.getBangBaoGiaSPTrongNuoc(new Integer(pageNum), isCoThue);
        return SUCCESS;
    }

    public String processSearchThanhPham() {
        if (searchText != null && searchText.trim().length() > 0) {
            try {
                if (searchText.contains(",")) {
                    String[] arr = searchText.split(",");
                    searchText = arr[0].trim();
                }
                listBangBaoGia = giaBanThanhPhamServices.getSearchBangBaoGiaSPTrongNuoc(isCoThue, searchText, option);
                numOfRow = listBangBaoGia.size();
            } catch (Exception e) {
                addActionError(UIMessages.VIEW_ERROR);
                return SUCCESS;
            }
        }
        return SUCCESS;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String viewRutGon() {
        listBangBaoGia = giaBanThanhPhamServices.getBangBaoGiaSPTrongNuoc(isCoThue);
        return "viewExport";
    }

    public ThanhPhamServices getThanhPhamServices() {
        return thanhPhamServices;
    }

    public void setThanhPhamServices(ThanhPhamServices thanhPhamServices) {
        this.thanhPhamServices = thanhPhamServices;
    }

    public GiaBanThanhPhamServices getGiaBanThanhPhamServices() {
        return giaBanThanhPhamServices;
    }

    public void setGiaBanThanhPhamServices(GiaBanThanhPhamServices giaBanThanhPhamServices) {
        this.giaBanThanhPhamServices = giaBanThanhPhamServices;
    }

    public List<BaoGiaSanPhamTrongNuocBe> getListBangBaoGia() {
        return listBangBaoGia;
    }

    public void setListBangBaoGia(List<BaoGiaSanPhamTrongNuocBe> listBangBaoGia) {
        this.listBangBaoGia = listBangBaoGia;
    }

    public Integer getNumOfRow() {
        return numOfRow;
    }

    public void setNumOfRow(Integer numOfRow) {
        this.numOfRow = numOfRow;
    }

    public String getIsCoThue() {
        return isCoThue;
    }

    public void setIsCoThue(String isCoThue) {
        this.isCoThue = isCoThue;
    }

    public Long getOption() {
        return option;
    }

    public void setOption(Long option) {
        this.option = option;
    }

    public List<BaoGiaSanPhamXuatKhauBe> getListBangBaoGiaXK() {
        return listBangBaoGiaXK;
    }

    public void setListBangBaoGiaXK(List<BaoGiaSanPhamXuatKhauBe> listBangBaoGiaXK) {
        this.listBangBaoGiaXK = listBangBaoGiaXK;
    }

    public String getIsOneToSeven() {
        return isOneToSeven;
    }

    public void setIsOneToSeven(String isOneToSeven) {
        this.isOneToSeven = isOneToSeven;
    }

    public String getIsSevenToEnd() {
        return isSevenToEnd;
    }

    public void setIsSevenToEnd(String isSevenToEnd) {
        this.isSevenToEnd = isSevenToEnd;
    }
}

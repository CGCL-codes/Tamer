package gui.bindings;

import gui.dataModels.dataTablePaged;
import hibernates.Cennik;
import hibernates.LpotrawyProdukty;
import hibernates.LpotrawyProduktyId;
import hibernates.Produkty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;

public class ProduktyBean extends DaoBean implements dataTablePaged {

    private List<Produkty> produktyTable;

    private Produkty produkt = new Produkty();

    private List<LpotrawyProdukty> lpotProdTable;

    private HtmlDataTable dataTable;

    private Cennik cennik = new Cennik();

    private LpotrawyProdukty lpotrawyProdukty = new LpotrawyProdukty();

    private boolean subsite = false;

    private PotrawyBean potrawyBean;

    public List<Produkty> getProduktyTable() {
        produktyTable = daoServices.searchPordukty();
        return produktyTable;
    }

    public void setProduktyTable(List<Produkty> produktyTable) {
        this.produktyTable = produktyTable;
    }

    public Produkty getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkty produkt) {
        this.produkt = produkt;
    }

    public List<LpotrawyProdukty> getLpotProdTable() {
        lpotProdTable = daoServices.searchLprodPotr(potrawyBean.getPotrawy().getIdPotrawa());
        return lpotProdTable;
    }

    public void setLpotProdTable(List<LpotrawyProdukty> lpotProdTable) {
        this.lpotProdTable = lpotProdTable;
    }

    public HtmlDataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }

    public String listDataTable() {
        produkt = (Produkty) dataTable.getRowData();
        dataTable.setFirst(0);
        return "list";
    }

    public Cennik getCennik() {
        return cennik;
    }

    public void setCennik(Cennik cennik) {
        this.cennik = cennik;
    }

    public LpotrawyProdukty getLpotrawyProdukty() {
        return lpotrawyProdukty;
    }

    public void setLpotrawyProdukty(LpotrawyProdukty lpotrawyProdukty) {
        this.lpotrawyProdukty = lpotrawyProdukty;
    }

    public boolean isSubsite() {
        return subsite;
    }

    public void setSubsite(boolean subsite) {
        this.subsite = subsite;
    }

    public PotrawyBean getPotrawyBean() {
        return potrawyBean;
    }

    public void setPotrawyBean(PotrawyBean potrawyBean) {
        this.potrawyBean = potrawyBean;
    }

    public String setProduktySubsite() {
        this.setSubsite(true);
        return "new_prod_cat";
    }

    public String clearProduktySubsite() {
        this.setSubsite(false);
        return "lista_prod";
    }

    public String editTableItem() {
        produkt = (Produkty) ((HtmlDataTable) produktyTable).getRowData();
        return "edit";
    }

    public String editProdukty() {
        produkt = (Produkty) dataTable.getRowData();
        cennik = produkt.getCennik();
        return "edit";
    }

    public String editLprodPotrawy() {
        lpotrawyProdukty = (LpotrawyProdukty) dataTable.getRowData();
        produkt = lpotrawyProdukty.getId().getProdukty();
        return "edit";
    }

    public String clearProdukty() {
        produkt = new Produkty();
        cennik = new Cennik();
        return "edit";
    }

    public String dodajProdukty() {
        Collection<LpotrawyProdukty> l = new ArrayList<LpotrawyProdukty>();
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            dataTable.setRowIndex(i);
            HtmlSelectBooleanCheckbox chbx = (HtmlSelectBooleanCheckbox) dataTable.findComponent("id_mark");
            if (chbx.isSelected()) {
                Produkty produkty = (Produkty) dataTable.getRowData();
                LpotrawyProduktyId id = new LpotrawyProduktyId();
                LpotrawyProdukty lpotrawyProdukty = new LpotrawyProdukty();
                id.setPotrawy(potrawyBean.getPotrawy());
                id.setProdukty(produkty);
                lpotrawyProdukty.setId(id);
                l.add(lpotrawyProdukty);
            }
        }
        daoServices.saveLpotrawyProdukty(l);
        potrawyBean.getDataTable().setFirst(0);
        return "zapisz";
    }

    public String delProdukt() {
        daoServices.deleteProdukt((LpotrawyProdukty) dataTable.getRowData());
        return "usun";
    }

    public String saveProdukt() {
        cennik.setData(new Date());
        produkt.setAktywny(true);
        produkt.setCennik(cennik);
        daoServices.saveProdukt(produkt);
        cennik = new Cennik();
        produkt = new Produkty();
        return "zapisz";
    }

    public String saveEditedProdukt() {
        lpotrawyProdukty.getId().setProdukty(produkt);
        daoServices.saveProdukt(produkt);
        daoServices.saveLpotrawyProdukty(lpotrawyProdukty);
        return "zapisz";
    }

    public String changeProduktStatus() {
        produkt = (Produkty) dataTable.getRowData();
        if (produkt.isAktywny()) produkt.setAktywny(false); else produkt.setAktywny(true);
        daoServices.saveProdukt(produkt);
        return "zmieniony";
    }

    public String pageFirst() {
        dataTable.setFirst(0);
        return "first";
    }

    public String pagePrevious() {
        dataTable.setFirst(dataTable.getFirst() - dataTable.getRows());
        return "previous";
    }

    public String pageNext() {
        dataTable.setFirst(dataTable.getFirst() + dataTable.getRows());
        return "next";
    }

    public String pageLast() {
        int count = dataTable.getRowCount();
        int rows = dataTable.getRows();
        dataTable.setFirst(count - ((count % rows != 0) ? count % rows : rows));
        return "last";
    }
}

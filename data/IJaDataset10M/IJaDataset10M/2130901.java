package delegats;

import java.util.List;
import cryterias.Cryteria;
import gui.bindings.DaoBean;
import gui.bindings.ProduktyBean2;
import gui.bindings.WlasneZamowieniaBean;
import hibernates.Produkty;
import hibernates.ZamowieniaUHurtownika;

/**
 * @author Rafal
 * 
 */
public class WybierzProduktDelegat extends DaoBean implements AkcjoDelegat {

    private ProduktyBean2 produktyBean2;

    private WlasneZamowieniaBean wlasneZamowieniaBean;

    public ProduktyBean2 getProduktyBean2() {
        return produktyBean2;
    }

    public void setProduktyBean2(ProduktyBean2 produktyBean2) {
        this.produktyBean2 = produktyBean2;
    }

    public WlasneZamowieniaBean getWlasneZamowieniaBean() {
        return wlasneZamowieniaBean;
    }

    public void setWlasneZamowieniaBean(WlasneZamowieniaBean wlasneZamowieniaBean) {
        this.wlasneZamowieniaBean = wlasneZamowieniaBean;
    }

    public void process(Object params) {
        Produkty produkt = produktyBean2.getProdukt();
        ZamowieniaUHurtownika zamowienie = this.wlasneZamowieniaBean.getZamowienie();
        if (produkt != null && zamowienie != null) daoServices.AddPozycjaZamUHUrt(produkt, zamowienie); else System.out.println("Null pointer");
    }

    public ProduktyBean2 getProduktyBean() {
        return produktyBean2;
    }

    public void setProduktyBean(ProduktyBean2 produktyBean) {
        this.produktyBean2 = produktyBean;
    }

    public List<?> search(Cryteria cryteria) {
        return null;
    }
}

package org.sample.product;

import org.sadhar.sia.framework.ClassApplicationModule;
import org.sample.category.CategoryDAO;
import org.sample.category.CategoryDAOImpl;
import org.sample.discount.Discount;
import org.sample.discount.DiscountDAO;
import org.sample.discount.DiscountDAOImpl;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author hendro
 */
public class ProductWnd extends ClassApplicationModule {

    private CategoryDAO categoryDAO;

    private DiscountDAO discDAO;

    private ProductDAO productDAO;

    public ProductWnd() {
        this.categoryDAO = new CategoryDAOImpl();
        this.discDAO = new DiscountDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }

    private Textbox txtboxKeySearch;

    private Button btnCari;

    private Textbox txtboxIdCategory;

    private Textbox txtboxProductName;

    private Textbox txtboxProductId;

    private Doublebox dblboxProductPrice;

    private Combobox cmbboxProductDisc;

    private Textbox txtboxIdDisc;

    private Textbox txtboxDesc;

    private Grid gridProduct;

    public void onCreate() throws Exception {
        txtboxKeySearch = (Textbox) getFellow("txtboxKeySearch");
        btnCari = (Button) getFellow("btnCari");
        txtboxIdCategory = (Textbox) getFellow("txtboxIdCategory");
        txtboxProductName = (Textbox) getFellow("txtboxProductName");
        txtboxProductId = (Textbox) getFellow("txtboxProductId");
        dblboxProductPrice = (Doublebox) getFellow("dblboxProductPrice");
        cmbboxProductDisc = (Combobox) getFellow("cmbboxProductDisc");
        txtboxIdDisc = (Textbox) getFellow("txtboxIdDisc");
        txtboxDesc = (Textbox) getFellow("txtboxDesc");
        gridProduct = (Grid) getFellow("gridProduct");
        clear();
        load();
    }

    public void clear() {
        txtboxKeySearch.setValue("");
        txtboxIdCategory.setValue("");
        txtboxProductId.setValue("");
        txtboxProductName.setValue("");
        txtboxProductName.setValue("");
        txtboxProductId.setValue("");
        txtboxDesc.setValue("");
        txtboxIdDisc.setValue("");
        cmbboxProductDisc.setValue("");
        dblboxProductPrice.setValue(0);
    }

    public void btnCariOnClick() throws Exception {
        final Window win = (Window) Executions.createComponents("popup.zul", this, null);
        Textbox txtboxSearch = (Textbox) win.getFellow("txtboxSearch");
        txtboxSearch.setValue(txtboxKeySearch.getValue());
        win.doModal();
    }

    public void cmbboxProductDiscOnSelect() throws Exception {
        Discount disc = (Discount) cmbboxProductDisc.getSelectedItem().getValue();
        txtboxIdDisc.setValue(disc.getId() + "");
    }

    public void btnBatalOnClick() throws Exception {
        clear();
        loadProduct();
    }

    public void btnSimpanOnClick() throws Exception {
        if (txtboxKeySearch.getValue().isEmpty()) {
            Messagebox.show("Silahkan memilih Category");
            return;
        }
        if (txtboxProductName.getValue().isEmpty()) {
            Messagebox.show("Silahkan masukan Nama Product");
            return;
        }
        if (dblboxProductPrice.getValue().isNaN()) {
            Messagebox.show("Masukan Harga Product");
            return;
        }
        if (cmbboxProductDisc.getValue().isEmpty()) {
            Messagebox.show("Silahkan Pilih Discount");
            return;
        }
        if (txtboxDesc.getValue().isEmpty()) {
            Messagebox.show("Silahakn masukan Description");
            return;
        }
        Product product = new Product();
        product.setName(txtboxProductName.getValue().toString());
        product.setCategoryId(Integer.valueOf(txtboxIdCategory.getValue()));
        product.setPrice(Double.valueOf(dblboxProductPrice.getValue().toString()));
        product.setDiscountId(Integer.valueOf(txtboxIdDisc.getValue()));
        product.setDescription(txtboxDesc.getValue().toString());
        try {
            if (txtboxProductId.getValue().isEmpty()) {
                if (productDAO.select(product.getName()).size() <= 0) {
                    productDAO.insert(product);
                    clear();
                    loadProduct();
                } else {
                    Messagebox.show("Data Product sudah ada, silahkan masukan data lain");
                }
            } else {
                product.setId(Integer.valueOf(txtboxProductId.getValue()));
                if (productDAO.select(product.getId(), product.getName()).size() <= 0) {
                    productDAO.update(product);
                    loadProduct();
                } else {
                    Messagebox.show("Data Product sudah ada, silahkan masukan data lain");
                }
            }
        } catch (Exception ex) {
            Messagebox.show("Data gagal disimpan");
            return;
        }
    }

    public void load() throws Exception {
        loadDisc();
        loadProduct();
    }

    public void loadProduct() throws Exception {
        ListModel model = new SimpleListModel(productDAO.list().toArray());
        RowRenderer renderer = new ProductGridRowRenderer();
        gridProduct.setModel(model);
        gridProduct.setRowRenderer(renderer);
    }

    public void loadDisc() throws Exception {
        cmbboxProductDisc.getItems().clear();
        for (Discount disc : discDAO.list()) {
            Comboitem item = new Comboitem();
            item.setValue(disc);
            item.setLabel(disc.getName());
            cmbboxProductDisc.appendChild(item);
        }
    }
}

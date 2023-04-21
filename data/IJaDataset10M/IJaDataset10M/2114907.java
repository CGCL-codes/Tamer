package basic.sell;

import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.faces.context.effects.JavascriptContext;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.StringHolder;

/**
 *
 * @author Admin
 */
public class TableController {

    private org.tempuri.IExhibitSrvbindingStub service;

    private TreeController treeController;

    private ArrayList<Offer> offers;

    private DataPaginator paginator;

    private Integer pages = 0;

    private int currentID;

    private int currentCount;

    private int currentPage = 1;

    private Boolean visible = false;

    private Boolean visiblePaginator = false;

    private TablePaginator tablePaginator;

    private static final int rows = 10;

    public TableController() {
        offers = new ArrayList<Offer>();
    }

    public TableController(org.tempuri.IExhibitSrvbindingStub service, TreeController treeController) {
        this.service = service;
        this.treeController = treeController;
        offers = new ArrayList<Offer>();
        tablePaginator = new TablePaginator();
    }

    public void createTable(int id, String filter, int page) {
        currentID = id;
        currentPage = page;
        IntHolder totalPages = new IntHolder();
        StringHolder searchTree = new StringHolder();
        StringHolder pathTree = new StringHolder();
        StringHolder filterList = new StringHolder();
        StringHolder sOffers = new StringHolder();
        try {
            service.navigateByOffers(id, filter, new IntHolder(page), totalPages, searchTree, pathTree, filterList, sOffers, new BooleanHolder());
        } catch (RemoteException ex) {
            System.out.println(ex);
        }
        Scanner scanner = new Scanner(pathTree.value);
        String offer = null;
        while (scanner.hasNextLine()) {
            offer = scanner.nextLine();
        }
        String[] temp = offer.split("\t");
        currentCount = Integer.parseInt(temp[3]);
        treeController.createTopTree(pathTree.value);
        offers.clear();
        Scanner sc = new Scanner(sOffers.value);
        int i = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] t = line.split("\t");
            byte[] bytes;
            if (t.length == 14) {
                String imageBase64 = t[13];
                bytes = base64.Base64Coder.decode(imageBase64);
            } else {
                bytes = new byte[0];
            }
            t[2] = t[2].replace(',', '.');
            t[4] = t[4].replace(',', '.');
            Offer item = new Offer(Integer.parseInt(t[0]), t[1], Double.valueOf(t[2]), t[3], Double.valueOf(t[4]), t[5], Integer.parseInt(t[6]), t[7], t[8], Integer.parseInt(t[9]), t[10], Integer.parseInt(t[11]), bytes);
            offers.add(item);
            i++;
        }
        pages = totalPages.value;
        tablePaginator.createPaginator(page, currentCount);
        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "date();");
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public TablePaginator getTablePaginator() {
        return tablePaginator;
    }

    public void setTablePaginator(TablePaginator tablePaginator) {
        this.tablePaginator = tablePaginator;
    }
}

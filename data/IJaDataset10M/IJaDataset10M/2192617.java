package harvestmars.Inventory;

/**
 *
 * @author toshiba
 */
public class Item {

    private int ID = -1;

    private String NamaItem = "\0";

    private int Jumlah = 0;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNamaItem() {
        return NamaItem;
    }

    public void setNamaItem(String NamaItem) {
        this.NamaItem = NamaItem;
    }

    public int getJumlah() {
        return Jumlah;
    }

    public void setJumlah(int Jumlah) {
        this.Jumlah = Jumlah;
    }
}

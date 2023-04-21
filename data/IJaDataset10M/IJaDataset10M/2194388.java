package root.plumbum2;

public class Interesse {

    private String loginInteressado;

    private Item item;

    /**
	 * Cria um interesse com login do interessado e o item interessado
	 * 
	 * @param loginInteressado
	 * @param item
	 */
    public Interesse(String loginInteressado, Item item) {
        this.loginInteressado = loginInteressado;
        this.item = item;
    }

    public String getInteressado() {
        return loginInteressado;
    }

    public void setInteressado(String loginInteressado) {
        this.loginInteressado = loginInteressado;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Interesse)) {
            return false;
        }
        Item item2 = (Item) obj;
        if (this.item.getIDItem().equals(item2.getIDItem())) {
            return true;
        }
        return false;
    }
}

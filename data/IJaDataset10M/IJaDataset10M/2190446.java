package models;

public class EstServicoLinkList {

    private EstServicoLink first;

    private Object[][] result;

    private static int quantidade = 1;

    private static int posicao = 0;

    public EstServicoLinkList() {
        first = null;
    }

    public void insert(EstServicoLink key_node) {
        key_node.setNext(first);
        first = key_node;
    }

    public EstServicoLink find(int key_cliente_id) {
        EstServicoLink current = first;
        while (current.getEstServicoId() != key_cliente_id) {
            if (current.getNext() == null) {
                return null;
            } else {
                current = current.getNext();
            }
        }
        return current;
    }

    public Object[][] list() {
        result = new Object[1][1];
        quantidade = 1;
        posicao = 0;
        return this.findlist(first);
    }

    public Object[][] findlist(EstServicoLink current) {
        if (current != null) {
            findlist(current.getNext());
            generateArray(current);
        }
        return result;
    }

    public void generateArray(EstServicoLink key_current) {
        Object temp[][] = new Object[quantidade][1];
        for (int i = 0; i < result.length; i++) temp[i] = result[i];
        temp[posicao][0] = key_current;
        result = temp;
        quantidade++;
        posicao++;
    }

    public boolean isEmpty() {
        return (first == null);
    }
}

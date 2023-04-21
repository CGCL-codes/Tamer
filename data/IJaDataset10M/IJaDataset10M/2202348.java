package bliste;

/**
 *
 * @author BMB_L2_
 */
public class BListe {

    public Node head;

    void ekle(Node node) {
        if (head == null) {
            head = node;
            return;
        }
        node.sonraki = head;
        head = node;
    }

    Node find(int sayi) {
        Node temp = head;
        while (temp != null) {
            if (temp.getSayi() == sayi) return temp;
            temp = temp.sonraki;
        }
        return temp;
    }

    void sil(int sayi) {
        Node temp = head;
        Node onceki = head;
        while (temp != null) {
            if (temp.getSayi() == sayi) {
                onceki.sonraki = temp.sonraki;
                if (head == temp) {
                    head = head.sonraki;
                }
                return;
            }
            onceki = temp;
            temp = temp.sonraki;
        }
    }

    void listele() {
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.getSayi());
            temp = temp.sonraki;
        }
    }
}

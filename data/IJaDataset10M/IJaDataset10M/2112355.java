package kgarten.proto;

/** Csokiautomat�t reprezent�l� oszt�ly.
 * @inst. 0..*
 */
public class Automat extends KObject {

    /** Az automat�ban l�v� csokik maxim�lis (egyben kezdeti) sz�ma. */
    public static final int MAX_CHOCOLATE = 20;

    /** Az automat�ban l�v� csokik sz�ma. */
    private int chocolate = MAX_CHOCOLATE;

    /** L�trehoz egy �j csokiautomat�t. */
    public Automat() {
    }

    /** T�puslek�rdez� f�ggv�ny. A visszaadott �rt�k <tt>TYPE_AUTOMAT</tt>. */
    public int getType() {
        return TYPE_AUTOMAT;
    }

    /** Visszaadja az automat�ban l�v� csokik sz�m�t. */
    public int getChocolate() {
        return chocolate;
    }

    /** K�r�s <tt>num</tt> darab csoki kiv�tel�re az
	 * automat�b�l. A visszaadott �rt�k a t�nylegesen kivett
	 * csokik sz�m�t adja. Ez lehet kevesebb is a k�rt
	 * darabsz�mn�l, ha a k�rt mennyis�g m�r nem �ll rendelkez�sre
	 * az automat�ban. */
    public int getChocolate(int num) {
        if (num >= chocolate) {
            int tmp = chocolate;
            chocolate = 0;
            return tmp;
        } else {
            chocolate -= num;
            return num;
        }
    }
}

package giis_lab1_rc1.util;

/**
 * @author Нечипуренко Дмитрий 
 * @author Русецкий Кирилл 
 */
public class Pair<A, B> {

    private A first;

    private B second;

    /**
     * Конструктор пары
     * @param first первый элемент
     * @param second второй элемент
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}

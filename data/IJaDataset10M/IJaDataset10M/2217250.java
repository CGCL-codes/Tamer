package framework;

/**
 * Interface to business logic implementation.
 *
 * @author Denis DIR Rozhnev
 */
public interface Business {

    static final int DEFECT = Integer.MIN_VALUE;

    static final int COOPERATE = Integer.MAX_VALUE;

    static final int NEUTRAL = 0;

    /**
     * ���������� ������� �� �� �������� � � ������������ � ���� ��������� ������� ����.
     *
     * @param p1 first person
     * @param p2 second person
     * @return persons scores deltas array [2]
     */
    int[] doBusiness(Person p1, Person p2);

    int[] getDealsByScore(int[] deals);
}

package impl.game.grid;

import framework.*;
import org.apache.log4j.Logger;

/**
 * ��������� ����, ����� ������ ����� ��������� � ������ ��������� �����
 * todo replace arrays with objects (where it posible)
 *
 * @author Denis DIR Rozhnev
 */
public class GridGame extends AbstractGame {

    private static final Logger LOG = Logger.getLogger(GridGame.class);

    private static final boolean VERBOSE = false;

    /** �������������� � 4 ��������. ��� ��������� ������ � ��������, � ������ � ������ ����� ����������������� � ���� */
    public static final int[][] Q4 = { { -1, 0 }, { 0, -1 } };

    /** �������������� � 8 ��������. ������ � ������� 4 �������� - ��. {@link #Q4} */
    public static final int[][] Q8 = { { -1, -1 }, { 0, -1 }, { -1, 0 }, { -1, 1 } };

    /** Array of nearest persons coordinates offsets. Person will deal with them. */
    private final int[][] Q;

    private final int MX, MY;

    private int turnNumber;

    /**
     * Array of Person's deals. Size: n*Q*2. First index is the person, second - is the index in {@link #Q},
     * third - is the index in deal array.
     */
    private int[][][] turn;

    /**
     * @param mx - ������ �������� ���� �� ������
     * @param my - ������ �������� ���� �� ������
     * @param q  - ������ �������� �� ������� � ��� �����������������. ����� ������������ {@link #Q4} � {@link #Q4}
     */
    public GridGame(int mx, int my, int[][] q) {
        this.MX = mx;
        this.MY = my;
        Q = q;
        turn = new int[mx * my][Q.length][2];
    }

    /**
     * @param persons  - ������ ���������-�������. ������ ��������������� �������� ���� ����������� � ������������.
     * @param business - �������� ���������� �����.
     */
    @Override
    public void init(Person[] persons, Business business) {
        super.init(persons, business);
        if (persons.length != MX * MY) throw new RuntimeException("Persons count must be " + MX * MY);
        turnNumber = 0;
        for (Person person : persons) {
            person.init(new Context(person));
        }
    }

    /**
     * ��������� �������� ���������� �����, � ����������� �����.
     *
     * @param count - ���������� �����
     * @throws DeepException ���� �������������� �� ��� ������ ����� {@link #init(framework.Person[], framework.Business)}
     */
    @Override
    public void turn(int count) throws DeepException {
        if (peers.length == 0) throw new DeepException("Every2every game is not initialized");
        StringBuffer buff = null;
        if (LOG.isDebugEnabled()) buff = new StringBuffer(128);
        for (int n = turnNumber + count; turnNumber < n; turnNumber++) {
            if (LOG.isDebugEnabled()) buff.append("i").append(turnNumber).append(':');
            for (int i = 0, x0 = 0, y0 = 0; i < peers.length; i++, x0++) {
                Person p0 = peers[i];
                if (x0 == MX) {
                    x0 = 0;
                    y0++;
                }
                for (int q = 0; q < Q.length; q++) {
                    int[] m = Q[q];
                    int x1 = x0 + m[0];
                    if (x1 < 0) x1 += MX; else if (x1 >= MX) x1 -= MX;
                    int y1 = y0 + m[1];
                    if (y1 < 0) y1 += MY; else if (y1 >= MY) y1 -= MY;
                    final int j = x1 + y1 * MX;
                    Person p1 = peers[j];
                    int[] res = biz.doBusiness(p0, p1);
                    addScore(i, res[0]);
                    addScore(j, res[1]);
                    turn[i][q] = res;
                }
            }
            if (LOG.isDebugEnabled()) {
                for (int res : getScores()) {
                    buff.append(" ").append(res);
                }
                LOG.debug(buff.toString());
                buff.setLength(0);
            }
        }
        notifyListeners(count);
    }

    public int[][] getQ() {
        return Q;
    }

    public int[][][] getTurnDeals() {
        return turn;
    }

    public int getMY() {
        return MY;
    }

    public int getMX() {
        return MX;
    }

    public void setPerson(int i, Person person) {
        LOG.info("i = " + i + " old: " + peers[i].getClass().getSimpleName() + " new: " + person.getClass().getSimpleName());
        peers[i] = person;
    }

    /** ��������� �������� ��������� ����� ��������. */
    private class Context implements PersonContext {

        protected Object personId;

        public Context(Object personId) {
            this.personId = personId;
        }

        @Override
        public Object sendMessage(Object id, Object message) {
            return ((Person) id).messageReceived(personId, message);
        }
    }
}

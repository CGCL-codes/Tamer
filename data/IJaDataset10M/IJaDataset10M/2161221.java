package com.tonbeller.jpivot.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.CellSetAxisMetaData;
import org.olap4j.Position;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Member;
import com.tonbeller.jpivot.olap.model.Visitable;
import com.tonbeller.jpivot.olap.model.Visitor;
import orcajo.azada.core.model.QueryManager;

/**
 * Decorates an Axis by adding the parents of all members.
 * Every Position will contain an equal number of members, 
 * where some of them will be the same. For example, an
 * axis in the result contains 2 members in 1 hierarchy
 * <p>
 * <table>
 *   <tr><th>   </th><th>Revenue</th></tr>
 *   <tr><td>USA</td><td>1000</td></tr>
 *   <tr><td>CA </td><td>100</td></tr>
 * </table>
 * <p>
 * will become
 * <p>
 * <table>
 *   <tr><th>   </th><th>    </th><th>Revenue</th></tr>
 *   <tr><td>USA</td><td>USA</td><td>1000</td></tr>
 *   <tr><td>USA</td><td>CA  </td><td>100</td></tr>
 * </table>
 * 
 * <p>
 * If the all member is not visible on the axis, its not added as a parent, because
 * this would not add information. Otherwise its added like other parent members too. 
 * @author av
 * @version fsaz
 */
public class LevelAxisDecorator implements CellSetAxis, Visitable {

    CellSetAxis axis;

    QueryManager manager;

    int[] levelCount;

    boolean[] skipAllMember;

    int totalLevelCount;

    List<Position> positions;

    /**
   * Constructor for LevelAxisDecorator.
   */
    public LevelAxisDecorator(CellSetAxis axis, QueryManager manager) {
        this.axis = axis;
        this.manager = manager;
        computeLevelCount();
        makePositions();
    }

    /**
   * for each hierarchy of the underlying axis compute the
   * number of levels (maxRootDistance - minRootDistance).
   */
    void computeLevelCount() {
        List<Hierarchy> hiers = manager.findVisiblesHierarchy(axis);
        int hierarchyCount = hiers.size();
        levelCount = new int[hierarchyCount];
        skipAllMember = new boolean[hierarchyCount];
        for (int i = 0; i < hiers.size(); i++) {
            levelCount[i] = Integer.MIN_VALUE;
            skipAllMember[i] = hiers.get(i).hasAll();
        }
        Iterator<Position> it = axis.getPositions().iterator();
        while (it.hasNext()) {
            Position p = (Position) it.next();
            List<Member> members = p.getMembers();
            for (int i = 0; i < members.size(); i++) {
                int count = getDepth(members.get(i)) + 1;
                levelCount[i] = Math.max(levelCount[i], count);
                if (members.get(i).isAll()) skipAllMember[i] = false;
            }
        }
        for (int i = 0; i < hierarchyCount; i++) {
            if (skipAllMember[i]) levelCount[i] -= 1;
        }
        totalLevelCount = 0;
        for (int i = 0; i < hierarchyCount; i++) totalLevelCount += levelCount[i];
    }

    private int getDepth(Member m) {
        int depth = 0;
        while (m.getParentMember() != null) {
            depth++;
            m = m.getParentMember();
        }
        return depth;
    }

    void makePositions() {
        positions = new ArrayList<Position>();
        Iterator<Position> it = axis.getPositions().iterator();
        while (it.hasNext()) {
            Position p = (Position) it.next();
            positions.add(makePosition(p));
        }
    }

    private Position makePosition(Position source) {
        List<Member> members = source.getMembers();
        Member[] result = new Member[totalLevelCount];
        int offset = 0;
        for (int i = 0; i < members.size(); i++) {
            int totalCount = levelCount[i];
            int memberCount = getDepth(members.get(i)) + 1;
            if (skipAllMember[i]) memberCount -= 1;
            addParents(result, offset, totalCount, memberCount, members.get(i));
            offset += totalCount;
        }
        return new PositionDecorator(source, result);
    }

    /**
   * adds members to result array from right to left, starting at offset
   * @param result the array to add the members to
   * @param offset the offset in the array
   * @param totalCount number of positions to fill in the array
   * @param memberCount the number of different members to add, rest will be padded
   * @param member start member
   */
    private void addParents(Member[] result, int offset, int totalCount, int memberCount, Member member) {
        int fillCount = totalCount - memberCount;
        offset = offset + totalCount - 1;
        for (int i = 0; i < fillCount; i++) result[offset--] = member;
        for (int i = 0; i < memberCount; i++) {
            result[offset--] = member;
            member = member.getParentMember();
        }
    }

    /**
   * @see com.tonbeller.jpivot.olap.model.Axis#getPositions()
   */
    public List<Position> getPositions() {
        return positions;
    }

    public CellSetAxisMetaData getAxisMetaData() {
        return axis.getAxisMetaData();
    }

    public Axis getAxisOrdinal() {
        return axis.getAxisOrdinal();
    }

    public CellSet getCellSet() {
        return axis.getCellSet();
    }

    public int getPositionCount() {
        return positions.size();
    }

    public ListIterator<Position> iterator() {
        return positions.listIterator();
    }

    public static final class PositionDecorator implements Position, Visitable {

        Position position;

        List<Member> members;

        PositionDecorator(Position position, Member[] members) {
            this.position = position;
            this.members = Arrays.asList(members);
        }

        public Position getRootDecoree() {
            return position;
        }

        public List<Member> getMembers() {
            return members;
        }

        public int getOrdinal() {
            return position.getOrdinal();
        }

        /**
     * @see com.tonbeller.jpivot.olap.model.Visitable#accept(Visitor)
     */
        public void accept(Visitor visitor) {
            visitor.visitPosition(this);
        }
    }

    /**
   * @see com.tonbeller.jpivot.olap.model.Visitable#accept(Visitor)
   */
    public void accept(Visitor visitor) {
        visitor.visitAxis(this);
    }
}

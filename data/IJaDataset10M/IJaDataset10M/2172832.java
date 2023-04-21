package ca.sqlpower.wabit.rs.olap;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.olap4j.metadata.Member;

public class MemberHierarchyComparator implements Comparator<Member> {

    private static final Logger logger = Logger.getLogger(MemberHierarchyComparator.class);

    public int compare(Member m1, Member m2) {
        if (m1.equals(m2)) return 0;
        List<Member> m1path = path(m1);
        List<Member> m2path = path(m2);
        int i = 0;
        while (i < m1path.size() && i < m2path.size()) {
            if (!m1path.get(i).equals((m2path).get(i))) break;
            i++;
        }
        if (m1path.size() == i) return -1;
        if (m2path.size() == i) return 1;
        logger.debug("m1path[i] ordinal=" + m1path.get(i).getOrdinal() + " name=" + m1path.get(i).getName());
        logger.debug("m2path[i] ordinal=" + m2path.get(i).getOrdinal() + " name=" + m2path.get(i).getName());
        if (m1path.get(i).getName().matches("[0-9]+") && m2path.get(i).getName().matches("[0-9]+")) {
            Integer member1Int = Integer.parseInt(m1path.get(i).getName());
            Integer member2Int = Integer.parseInt(m2path.get(i).getName());
            return member1Int.compareTo(member2Int);
        } else {
            return m1path.get(i).getName().compareToIgnoreCase(m2path.get(i).getName());
        }
    }

    private List<Member> path(Member m) {
        List<Member> path = new LinkedList<Member>();
        Member temp = m;
        while (temp != null) {
            path.add(0, temp);
            temp = temp.getParentMember();
        }
        return path;
    }
}

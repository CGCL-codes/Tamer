package astgen;

import java.util.Set;
import com.google.common.collect.Sets;

/**
 * Represents a member from the abstract grammar.
 * 
 * @author rgrig
 */
public class AgMember {

    public String type = null;

    public String name = null;

    public boolean primitive = true;

    public boolean isenum = false;

    public Set<String> tags = Sets.newHashSet();
}

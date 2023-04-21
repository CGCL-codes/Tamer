package abc.om.ast;

/**
 * @author Neil Ongkingco
 *
 */
public interface ModMemberModule extends ModMember {

    public String name();

    public boolean isConstrained();

    public void setIsConstrained(boolean isConstrained);
}

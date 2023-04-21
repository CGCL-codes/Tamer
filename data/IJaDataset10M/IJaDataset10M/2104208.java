package framework;

public interface AgencyDealer {

    public abstract int insert(Agency agency);

    public abstract int update(String name, Agency agency);

    public abstract java.util.Vector<Agency> select(String name);

    public abstract int delete(String name);
}

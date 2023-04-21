package common;

public interface IParse {

    public void setParameter(String name, String params[]);

    public void commit();

    public String getName();

    public void setName(String name);
}

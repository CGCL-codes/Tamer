package unbfuzzy.membershipfunctions;

public interface IMembershipFunction {

    public double functionOutput();

    public void setName(String name);

    public void setInput(double input);

    public String getName();

    public double getMinDomainValue();

    public double getMaxDomainValue();
}

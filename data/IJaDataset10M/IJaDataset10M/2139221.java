package software;

public interface Software {

    public int power = 0;

    public String version = "1.0";

    public int size = 1;

    public String description = "No Description available";

    public int price = 0;

    public int getSize();

    public void setSize(int size);

    public void run();

    public void getVersion();

    public void setVersion(String version);

    public String getDescription();

    public void setPower(int power);

    public void getPower();

    public void setDescription(String description);

    public void setPrice(int price);

    public void getPrice();
}

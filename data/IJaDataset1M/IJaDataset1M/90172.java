package mindbright.security;

public interface Key extends java.io.Serializable {

    public String getAlgorithm();

    public String getFormat();

    public byte[] getEncoded();
}

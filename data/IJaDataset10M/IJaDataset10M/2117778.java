package ru.itbrains.jicard.xmlsignature;

public class X509Data {

    private X509Certificate x509Certificate;

    public X509Data() {
    }

    public X509Data(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public String toString() {
        return "X509Data{" + "x509Certificate=" + x509Certificate + '}';
    }
}

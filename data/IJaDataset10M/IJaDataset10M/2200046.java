package de.fzj.pkikits.ra;

import org.bouncycastle.asn1.x509.X509Extensions;

public class CertTyp {

    private String name;

    private String title;

    private X509Extensions extensions;

    public CertTyp(String name, String title, X509Extensions extensions) {
        this.name = name;
        this.title = title;
        this.extensions = extensions;
    }

    public X509Extensions getExtensions() {
        return extensions;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}

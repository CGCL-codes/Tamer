package net.llando;

@LlandoClass(tag = "net.llando.ProxyReference", primitive = true)
public class ProxyReference implements ObjectIdentifier {

    @LlandoProperty(name = "llando_id")
    public String llandoId;

    public ProxyReference() {
    }

    public ProxyReference(String llandoId) {
        this.llandoId = llandoId;
    }
}

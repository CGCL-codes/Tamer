package prjfbtypes;

import org.jdom.Element;

public class FBCompiler {

    private String Language;

    private String Vendor;

    private String Product;

    private String Version;

    public FBCompiler(String Language, String Vendor, String Product, String Version) {
        this.Language = Language;
        this.Vendor = Vendor;
        this.Product = Product;
        this.Version = Version;
    }

    public String toString() {
        return "FBCompiler";
    }

    public Element toXML() {
        Element fbCompilerElement = new Element("Compiler");
        fbCompilerElement.setAttribute("Language", Language);
        fbCompilerElement.setAttribute("Vendor", Vendor);
        fbCompilerElement.setAttribute("Product", Product);
        fbCompilerElement.setAttribute("Version", Version);
        return fbCompilerElement;
    }

    public void print() {
        System.out.println("Language: " + Language);
        System.out.println("Vendor: " + Vendor);
        System.out.println("Product: " + Product);
        System.out.println("Version: " + Version);
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }
}

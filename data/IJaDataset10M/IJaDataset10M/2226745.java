package ac.hiu.j314.vesma;

import ac.hiu.j314.elmve.*;
import java.io.*;
import org.w3c.dom.*;

public class Link extends VesmaObject {

    private static final long serialVersionUID = 1L;

    protected String source = "none";

    protected void init() {
        super.init();
        a2UI = "x-res:///ac/hiu/j314/vesma/resources/link.a2";
        a3UI = "x-res:///ac/hiu/j314/vesma/resources/link.a3";
    }

    protected String customizerClass() {
        return "ac.hiu.j314.vesma.SourceCustomizer";
    }

    protected int customizerPolicy() {
        return CustomizerStack.POLICY5;
    }

    public void touched(Order o) {
        ElmStub c = o.getElm(0);
        if (source.equals("none")) {
            send(makeOrder(c, "hear", "This location of link is not set.\n"));
        } else {
            send(makeRequest(c, "go", source));
        }
    }

    public void setSource(Order o) {
        source = o.getString(0);
    }

    protected void saveExtension(Document d, Element e) {
        super.saveExtension(d, e);
        W.addDataDOM(d, e, "source", source);
    }

    protected void loadExtension(Element e, LoadedElmSet elmSet) {
        super.loadExtension(e, elmSet);
        source = W.getDataDOM(e, "source");
    }

    public String toSaveString() {
        return super.toSaveString() + "'" + source + "'\n";
    }

    public void loadFromText(ElmStreamTokenizer f_in) throws IOException {
        super.loadFromText(f_in);
        source = f_in.nextString();
    }

    public String toString() {
        return super.toString() + source;
    }

    public void getCustomizerData(MyRequest r) {
        send(makeReply(r, source));
    }
}

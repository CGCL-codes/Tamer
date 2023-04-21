package strudle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Test {

    private String istruzioni;

    private String nome;

    private Trial[] elementi;

    private ControlModality controlloElementi;

    public Test(String nome, String descr, ControlModality mod) {
        this.nome = nome;
        this.istruzioni = descr;
        this.elementi = null;
        this.controlloElementi = mod;
    }

    public Test(String nome, String descr, Trial[] test) {
        this.nome = nome;
        this.istruzioni = descr;
        this.elementi = test;
    }

    public Test(Node Test) {
        NamedNodeMap attributi = Test.getAttributes();
        this.istruzioni = attributi.getNamedItem("istruzioni").getNodeValue();
        this.nome = attributi.getNamedItem("nome").getNodeValue();
        if (attributi.getNamedItem("controlloEl") != null) {
            String tmp = attributi.getNamedItem("controlloEl").getNodeValue();
            this.controlloElementi = new ControlModality(tmp.split(";"));
        } else {
            this.controlloElementi = new ControlModality();
        }
        if (Test.getChildNodes().getLength() != 0) {
            NodeList elementi = Test.getChildNodes();
            this.elementi = new Trial[0];
            for (int i = 0; i < elementi.getLength(); i++) {
                if (elementi.item(i).getNodeName().equals("Trial")) {
                    Trial[] tmp = new Trial[this.elementi.length + 1];
                    for (int j = 0; j < this.elementi.length; j++) {
                        tmp[j] = this.elementi[j];
                    }
                    tmp[this.elementi.length] = new Trial(elementi.item(i));
                    this.elementi = tmp;
                }
            }
        } else {
            this.elementi = new Trial[0];
        }
    }

    public void addProva(Trial el) {
        Trial[] tmp;
        if (elementi != null) {
            tmp = new Trial[elementi.length + 1];
            for (int i = 0; i < elementi.length; i++) {
                tmp[i] = elementi[i];
            }
        } else {
            tmp = new Trial[1];
        }
        tmp[tmp.length - 1] = el;
        elementi = tmp;
    }

    public Node toNode(Document document) {
        Element Test = (Element) document.createElement("Test");
        Test.setAttribute("istruzioni", this.istruzioni);
        Test.setAttribute("nome", this.nome);
        if (!controlloElementi.isTextControlled()) {
            String[] tmpControl = controlloElementi.getControlElements();
            String tmp = tmpControl[0];
            for (int j = 1; j < tmpControl.length; j++) {
                tmp = tmp.concat(";" + tmpControl[j]);
            }
            Test.setAttribute("controlloEl", tmp);
        }
        if (elementi != null) {
            for (int j = 0; j < elementi.length; j++) {
                Test.insertBefore(elementi[j].toNode(document), null);
            }
        }
        return Test;
    }

    public void removeProva(int prova) {
        int rem = 0;
        Trial[] tmp = new Trial[elementi.length - 1];
        for (int i = 0; i < elementi.length; i++) {
            if (i != prova) {
                tmp[rem++] = elementi[i];
            }
        }
        System.out.println(elementi.length);
        elementi = tmp;
        System.out.println(elementi.length);
    }

    public String getIstruzioni() {
        return istruzioni;
    }

    public ControlModality getControllo() {
        return controlloElementi;
    }

    public String getNome() {
        return nome;
    }

    public Trial[] getElementi() {
        return elementi;
    }

    public int getNumElTest() {
        if (elementi != null) {
            return elementi.length;
        } else {
            return 0;
        }
    }
}

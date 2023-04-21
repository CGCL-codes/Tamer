package it.unina.seclab.jafimon.ui;

public class CfgReplaceNew extends CfgRecord {

    public CfgReplaceNew(CfgEditor owner) {
        super(owner);
        opCode = new String("replaceNew");
        attrib1 = new String("calledClass");
        attrib1val = new String();
        attrib2 = new String("calledMethod");
        attrib2val = new String();
        showOnUI();
    }

    public void showOnUI() {
        theOwner.enable1(attrib1, attrib1val);
        theOwner.enable2(attrib2, attrib2val);
        theOwner.disable3();
        theOwner.disableBody();
        theOwner.disableModifiers();
        updateUI();
    }

    public void updateXML() {
        StringBuffer sb = new StringBuffer();
        attrib1val = theOwner.getJTextFieldA1().getText();
        attrib2val = theOwner.getJTextFieldA2().getText();
        sb.append("<");
        sb.append(opCode + " ");
        sb.append(attrib1 + "=\"");
        sb.append(attrib1val + "\" ");
        sb.append(attrib2 + "=\"");
        sb.append(attrib2val + "\"/>");
        xmlCode = sb.toString();
    }
}

package javad.jconst;

import java.io.*;

public class constName_and_Type_info extends constClass_or_String {

    int descriptor_index;

    constUtf8 descriptor_Utf8;

    public void read(DataInputStream dStream) {
        super.read(dStream);
        descriptor_index = readU2(dStream);
    }

    public void set_ref(constBase objAry[]) {
        super.set_ref(objAry);
        constBase tmp = objAry[descriptor_index];
        if (tmp instanceof constUtf8) {
            descriptor_Utf8 = (constUtf8) tmp;
        } else {
            System.out.println("Descriptor object at index is not a constUtf8");
        }
    }

    public String getString() {
        StringBuffer str = new StringBuffer();
        str.append(super.getString());
        str.append(", ");
        if (descriptor_Utf8 != null) {
            str.append(descriptor_Utf8.getString());
        } else {
            str.append("null ref");
        }
        return str.toString();
    }

    public void pr() {
        System.out.print(getString());
    }
}

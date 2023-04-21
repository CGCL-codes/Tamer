package j2se.typestate.accesspath;

import j2se.typestate.fileComponent.FileComponent;

public class APDoubleClose2 {

    public static void main(String[] args) {
        FileComponent f1 = new FileComponent();
        FileComponent f2 = new FileComponent();
        FileComponent x;
        x = f2;
        x.close();
        x.close();
    }
}

package util.basedatatype.file.Templet;

import java.util.ArrayList;
import java.util.List;
import util.basedatatype.ObjectBase;
import util.basedatatype.file.msword.MSWordManager;
import framework.zze2p.mod.Pojo_0I;
import framework.zze2p.mod.Pojo_2HO;

public class testTempletBaseTab {

    public static void main(String[] args) {
        TabChMod tabChMod = new TabChMod();
        String s = "s1, 1 ,1 ;  0:$h,2:a1,3:a3";
        tabChMod.fromString(s);
        System.out.println(ObjectBase.ToString.toString(tabChMod));
        Pojo_0I pojo = new Pojo_2HO();
        List lPojos = new ArrayList();
        pojo.set("s1", lPojos);
        Pojo_0I pSon = new Pojo_2HO();
        pSon.set("a1", "z3");
        lPojos.add(pSon);
        pSon = new Pojo_2HO();
        pSon.set("a1", "w5");
        lPojos.add(pSon);
        MSWordManager mWord = new MSWordManager(true);
        mWord.openDocument("D:\\working\\temp.doc");
        TempletBase.toAppWordForTable(mWord, tabChMod, pojo);
    }
}

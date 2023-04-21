package com.wendal.java.dex.decomplier.dexfile.model;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.wendal.java.dex.decomplier.com.CommandLineConfig;
import com.wendal.java.dex.decomplier.com.DexD;
import com.wendal.java.dex.decomplier.toolkit.IO_Tool;

public class Test_DexTaken {

    @Test
    public void testGetDexClassList() throws IOException {
        List<String> file_list = new ArrayList<String>();
        file_list.add("dex/com/wendal/dex/simple/multi/xrace_v2.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/multi/rec.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/empty/EmptyClass.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/empty/EmptyInterface.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/empty/EmptyEnum.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/easy/SimpleClass_Abstract.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/easy/SimpleClass_fields.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/easy/SimpleClass_final.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/easy/SimpleClass_SomeInterfaces.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/easy/SimpleClass_Static_fields.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/easy/SimpleClass_with_SuperClass.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/easy/methods/Void_String_Method.dump.txt");
        file_list.add("dex/com/wendal/dex/simple/easy/methods/Static_Methods.dump.txt");
        file_list.add("dex/com/wendal/dex/jmmmm_a/Jmmm_a.dump.txt");
        for (String string : file_list) {
            List<String> list = IO_Tool.getFile(string);
            assertTrue(list.size() > 0);
            DexD dexD = new DexD(CommandLineConfig.parse(new String[] { "-dest", "X:" }));
            dexD.setDex_dump_str(list);
            dexD.convert2DexModel();
            dexD.convert2JavaModel();
            dexD.VM_parse();
            dexD.reshaping();
            dexD.outputSource();
        }
    }
}

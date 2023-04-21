package org.javason.jsonrpc;

import java.util.List;
import java.util.Map;
import org.javason.jsonrpc.description.Param;

public class AnnotatedJsonInterfaceWithMap {

    public Boolean theMinimal(@Param(name = "second") Map input, @Param(name = "first") List secondInput) {
        System.out.println("theMinimal(" + input + "," + secondInput + ")");
        System.out.println("second = " + input + ", first = " + secondInput);
        if (input.equals("ERROR")) throw new RuntimeException("ERROR TEST");
        return true;
    }

    public Boolean theMinimal2(@Param(name = "second") Map<String, Boolean> input, @Param(name = "first") List secondInput) {
        System.out.println("theMinimal2(" + input + "," + secondInput + ")");
        System.out.println("second = " + input + ", first = " + secondInput);
        if (input.equals("ERROR")) throw new RuntimeException("ERROR TEST");
        return true;
    }

    public Boolean theMinimal3(@Param(name = "second") Map<String, JsonBean> input, @Param(name = "first") List secondInput) {
        System.out.println("theMinimal3(" + input + "," + secondInput + ")");
        System.out.println("second = " + input + ", first = " + secondInput);
        if (input.equals("ERROR")) throw new RuntimeException("ERROR TEST");
        return true;
    }
}

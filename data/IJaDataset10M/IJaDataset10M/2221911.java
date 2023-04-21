package org.cyberaide.gridshell.commands.security.test;

import java.util.List;
import org.cyberaide.gridshell.commands.util.ArgumentParser;
import org.cyberaide.gridshell.commands.security.LoadKeys;

;

public class LoadKeysTest {

    public static void main(String[] a) {
        String argsString = "";
        LoadKeys loadKeys = new LoadKeys(argsString);
        loadKeys.execute();
    }
}

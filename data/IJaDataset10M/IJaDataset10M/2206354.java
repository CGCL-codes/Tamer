package org.keplerproject.luajava;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Simple LuaJava console.
 * This is also an example on how to use the Java side of LuaJava and how to startup
 * a LuaJava application.
 * 
 * @author Thiago Ponte
 */
public class Console {

    /**
    * Creates a console for user interaction.
    * 
    * @param args	names of the lua files to be executed
    */
    public static void main(String[] args) {
        try {
            LuaState L = LuaStateFactory.newLuaState();
            L.openLibs();
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    int res = L.LloadFile(args[i]);
                    if (res == 0) {
                        res = L.pcall(0, 0, 0);
                    }
                    if (res != 0) {
                        throw new LuaException("Error on file: " + args[i] + ". " + L.toString(-1));
                    }
                }
                return;
            }
            System.out.println("API Lua Java - console mode.");
            BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
            String line;
            System.out.print("> ");
            while ((line = inp.readLine()) != null && !line.equals("exit")) {
                int ret = L.LloadBuffer(line.getBytes(), "from console");
                if (ret == 0) {
                    ret = L.pcall(0, 0, 0);
                }
                if (ret != 0) {
                    System.err.println("Error on line: " + line);
                    System.err.println(L.toString(-1));
                }
                System.out.print("> ");
            }
            L.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

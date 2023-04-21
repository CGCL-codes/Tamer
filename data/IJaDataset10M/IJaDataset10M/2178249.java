package com.yxl.project.java3d.test.life3d_6.scripting;

import java.io.*;
import javax.script.*;

public class ScriptingEx4 {

    public static void main(String[] args) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("js");
        CompiledScript cs = loadCompile(engine, "sums.js");
        int age = 40;
        int[] nums = { 1, 2, 3, 4, 5, 6, 7 };
        engine.put("age", age);
        engine.put("nums", nums);
        evalCSummer(cs, engine);
    }

    private static CompiledScript loadCompile(ScriptEngine engine, String fnm) {
        Compilable compEngine = (Compilable) engine;
        CompiledScript cs = null;
        try {
            FileReader fr = new FileReader(fnm);
            cs = compEngine.compile(fr);
            fr.close();
        } catch (FileNotFoundException e) {
            System.out.println(fnm + " not found");
        } catch (IOException e) {
            System.out.println("Could not read " + fnm);
        } catch (ScriptException e) {
            System.out.println("Problem compiling script in " + fnm);
        } catch (NullPointerException e) {
            System.out.println("Problem reading script in " + fnm);
        }
        return cs;
    }

    private static void evalCSummer(CompiledScript cs, ScriptEngine engine) {
        boolean isBigger = false;
        try {
            isBigger = (Boolean) cs.eval();
        } catch (ScriptException e) {
            System.out.println("Problem evaluating script");
        }
        double sum = (Double) engine.get("sum");
        System.out.println("(java) sum = " + sum);
        System.out.println("age is bigger = " + isBigger);
        System.out.println();
    }
}

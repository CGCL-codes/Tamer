package com.citizenhawk.antmakerunscript.internal;

/**
 * <u><b><font color="red">FOR INTERNAL USE ONLY.</font></b></u>
 * <p/>
 * Copyright (c)2007, Daniel Kaplan
 *
 * @author Daniel Kaplan
 * @since 7.10.5
 */
public class SayArgument {

    public static void main(String[] args) {
        System.out.println("I found " + args.length + " arguments");
        for (int i = 0; i < args.length; ++i) {
            System.out.println(args[i]);
        }
    }
}

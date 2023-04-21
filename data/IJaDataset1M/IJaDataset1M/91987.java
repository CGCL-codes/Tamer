package net.sourceforge.eclipsejetty.jetty6;

import net.sourceforge.eclipsejetty.jetty5.Jetty5LauncherMain;

/**
 * Main for Jetty 6
 * 
 * @author Christian K&ouml;berl
 * @author Manfred Hantschel
 */
public class Jetty6LauncherMain extends Jetty5LauncherMain {

    public static void main(String[] args) throws Exception {
        new Jetty6LauncherMain().launch(args);
    }

    protected void printLogo() {
        System.out.println("   ____    ___                   __    __  __         ____");
        System.out.println("  / __/___/ (_)__  ___ ___   __ / /__ / /_/ /___ __  / __/");
        System.out.println(" / _// __/ / / _ \\(_-</ -_) / // / -_) __/ __/ // / / _ \\");
        System.out.println("/___/\\__/_/_/ .__/___/\\__/  \\___/\\__/\\__/\\__/\\_, /  \\___/");
        System.out.println("           /_/                              /___/");
    }
}

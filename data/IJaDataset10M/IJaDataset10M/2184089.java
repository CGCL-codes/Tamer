package com.atolsystems.atolutilities;

import com.atolsystems.atolutilities.CommandLine.Arg;
import com.atolsystems.atolutilities.CommandLine.ArgDef;
import com.atolsystems.atolutilities.CommandLine.ArgHandler;
import com.atolsystems.atolutilities.CommandLine.ArgHandlerProvider;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebastien riou
 */
public class AddJarArgHandler implements ArgHandler, ArgHandlerProvider, SelfDocumented {

    static final String mark = "addJarFile:";

    public LinkedList<ArgDef> getArgDefs() {
        LinkedList<ArgDef> out = new LinkedList<ArgDef>();
        out.add(new ArgDef(mark, this));
        return out;
    }

    public String help() {
        return mark + "<file>\n" + "   Add a jar file to the class path";
    }

    public boolean inspectArg(Arg arg, CommandLine cl) {
        handleAddJarFile(arg);
        return true;
    }

    public void endOfCommandLineInspection(CommandLine cl) {
    }

    public boolean processArg(Arg arg, CommandLine cl) {
        return true;
    }

    public void endOfCommandLineProcessing(CommandLine cl) {
    }

    PlugInLoader plugInLoader = new PlugInLoader(this.getClass(), "packageLocator");

    private void handleAddJarFile(Arg arg) {
        String path = arg.value.substring(mark.length());
        File jarFile = arg.getFile(path);
        try {
            plugInLoader.addJarFile(jarFile);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            Logger.getLogger(AddJarArgHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getPlugInClass(String className) throws MalformedURLException, ClassNotFoundException {
        return plugInLoader.getPlugInClass(className);
    }

    public PlugInLoader getPlugInLoader() {
        return plugInLoader;
    }
}

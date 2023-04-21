package JRuby;

import ij.IJ;
import ij.plugin.PlugIn;
import org.jruby.*;
import java.io.PrintStream;
import common.AbstractInterpreter;
import ij.Menus;
import java.io.File;

public class JRuby_Interpreter extends AbstractInterpreter {

    Ruby rubyRuntime;

    protected Object eval(String text) throws Throwable {
        return rubyRuntime.evalScriptlet(text);
    }

    public void run(String ignored) {
        if (null != Ruby.getCurrentInstance()) {
            IJ.error("There is already an instance of " + "the JRuby interpreter");
            return;
        }
        super.run(ignored);
        setTitle("JRuby Interpreter");
        print("Starting JRuby ...");
        prompt.setEnabled(false);
        PrintStream stream = new PrintStream(out);
        rubyRuntime = Ruby.newInstance(System.in, stream, stream);
        println(" done.");
        prompt.setEnabled(true);
        rubyRuntime.evalScriptlet(getStartupScript());
    }

    public static String getImageJRubyPath() {
        String pluginsPath = Menus.getPlugInsPath();
        return pluginsPath + "JRuby" + File.separator + "imagej.rb";
    }

    public static String getStartupScript() {
        String s = "require 'java'\n" + "module Kernel\n" + "  def ij\n" + "    JavaUtilities.get_package_module_dot_format('ij')\n" + "  end\n" + "end\n" + "imagej_functions_path = '" + getImageJRubyPath() + "'\n" + "require imagej_functions_path\n";
        return s;
    }

    protected String getLineCommentMark() {
        return "#";
    }
}

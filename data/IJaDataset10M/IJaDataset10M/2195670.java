package com.tapina.robe.runtime.app;

import com.tapina.robe.runtime.Monitor;
import com.tapina.robe.swi.Wimp;
import com.tapina.robe.swi.OS;
import com.tapina.robe.swi.OSExt;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * This Monitor subclass runs Draw Plus.
 */
public class DrawPlus extends Monitor {

    /**
     * LOAD ANY REQUIRED RESOURCES HERE. NOTE THAT SWI CALLS EXPECT RISC OS-FORMAT PATHS. e.g.
     * Wimp.getInstance().spriteOp(11, "$.DrawPlus.!DrawPlus.!Sprites22");
     */
    protected void loadResources() {
        Wimp.getInstance().spriteOp(11, "$.Volumes.Personal.Projects.ROBE.tools.examples.DrawPlus.!DrawPlus.!Sprites22");
    }

    /**
     * Return the native path of the program image.
     * @return native path of program image
     */
    protected String getImagePath() {
        return "/Volumes/Personal/Projects/ROBE/tools/examples/DrawPlus/!DrawPlus/!RunImage";
    }

    /**
     * WRITE THE PROGRAM'S ENVIRONMENT HERE. THIS ALLOWS THE PROGRAM ACCESS TO ITS COMMAND LINE AND START TIME. e.g.
     * OSExt.getInstance().writeEnv("!RunImage -depth 4,4,8", new Date()); YOU MAY ALSO WISH TO SET VARIABLE VALUES.
     * e.g. OS.getInstance().setVarVal("My$Dir", "$.Foo", 0);
     */
    protected void setEnvironment() {
        OS.getInstance().setVarVal("DrawPlus$Dir", "$.Volumes.Personal.Projects.ROBE.tools.examples.DrawPlus.!DrawPlus", 0);
        OSExt.getInstance().writeEnv("!RunImage -mask 4,8,0", new Date());
    }

    public static void main(String[] args) {
        new DrawPlus();
    }
}

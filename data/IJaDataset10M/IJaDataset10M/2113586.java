package com.manydesigns.portofino.util;

/**
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public interface Command {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    public void run() throws Exception;
}

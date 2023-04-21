package com.htwg.routingengine.samples.runtime;

import com.htwg.routingengine.framework.RoutingSession;
import com.htwg.routingengine.modules.generic.FrameworkModule;

public class FmCall {

    /**
	 * Method <code>main</code>.
	 *
	 * @param args
	 */
    public static void main(String[] args) {
        String xmlIn = new String();
        xmlIn = "<Module name=\"com.htwg.routingengine.modules.testmodules.Testmodule1\" order=\"10\" active=\"yes\" version=\"1\">Testmodule 1 zum Test der dynamischen Objecterzeugung <param name=\"tolerance\" mandatory=\"yes\" description=\"gibt die Groe� des Pixelhaufens an\"/> <param name=\"marking\" mandatory=\"no\" default=\"no\" description=\"Wird der gefundene Pixelhaufen dauerhaft entfernt oder nur markiert\"/> <Module name=\"com.htwg.routingengine.modules.testmodules.Testmodule1\" order=\"10\" active=\"yes\" version=\"1\">	</Module> </Module>";
        FrameworkModule fm = new FrameworkModule(null, null, xmlIn);
        fm.runModule(xmlIn);
    }
}

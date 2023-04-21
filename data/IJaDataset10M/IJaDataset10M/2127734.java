package com.htwg.routingengine.modules.generic;

import com.htwg.routingengine.basis.Basis;

/**
 * Das BasicFramworkModule ist eine abstrakte Klasse, die vom FramworkModule
 * verwendet wird. Die Schnittstelle vom Genericmodule wird hier wieder
 * aufgenommen. Es konnte an dieser stelle nicht einfach das Interface verwendet
 * werden, da ansonsten das Framework module die f�nf Methoden als public
 * anbieten m�sste, was aber nicht erw�nscht ist.
 * 
 * 
 * <code>BasicModule</code>.
 * 
 * 
 * @author mwh
 * @version 1.0
 */
public abstract class BasicFrameworkModule extends Basis {

    protected abstract String init(String xmlIn) throws Exception;

    protected abstract String preAction(String xmlIn) throws Exception;

    protected abstract String action(String xmlIn) throws Exception;

    protected abstract void exit() throws Exception;

    protected abstract void output() throws Exception;
}

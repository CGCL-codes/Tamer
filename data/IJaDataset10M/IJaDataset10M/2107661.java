package net.sf.tacos.seam.beans;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("foo")
@Scope(ScopeType.SESSION)
public class Foo {
}

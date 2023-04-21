#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.tests;

import org.junit.Test;

import ${package}.xbasic.engines.xBasicEngineB.XBasicEngineB;

import static org.junit.Assert.*;
import static ${package}.xbasic.XBasicContainer.xBasic;

public class BasicSimplexTest
{
	@Test public void somethingToTest()
	{
		assertTrue("XBasicEngine::method(A);".equals(xBasic.method()));
		XBasicEngineB eb = new XBasicEngineB();
		xBasic.setEngine(eb);
		assertTrue("XBasicEngine::method(B);".equals(xBasic.method()));
	}
}

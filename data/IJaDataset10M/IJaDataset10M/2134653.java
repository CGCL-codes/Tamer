#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test.server;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ${package}.server.command.ActionOrderServiceImpl;
import ${package}.test.AbstractBaseTest;

public class ActionOrderServiceTest extends AbstractBaseTest {

	private final Logger logger = LoggerFactory.getLogger(ActionOrderServiceTest.class);

	@Autowired
	private ActionOrderServiceImpl actionOrderService;

	@Test
	public void notNull() {
		Assert.assertNotNull(actionOrderService);
	}
	
}

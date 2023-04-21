#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test.dao;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ${package}.dao.OrderDAO;
import ${package}.test.AbstractBaseTest;

public class OrderDAOTest extends AbstractBaseTest {

	private final Logger logger = LoggerFactory.getLogger(OrderDAOTest.class);
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Test
	public void nullTest() {
		Assert.assertNotNull(orderDAO);
	}
	
}

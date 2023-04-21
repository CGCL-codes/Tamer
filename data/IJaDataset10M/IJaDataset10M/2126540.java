package net.sf.autocrud;

import net.sf.autocrud.IndexJspGenerator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO: Class level Javadocs
 * @author Sujit Pal (spal@users.sourceforge.net)
 * @version $Revision: 1.2 $
 */
public class IndexJspGeneratorTest {

    private ApplicationContext context;

    private IndexJspGenerator generator;

    @Before
    public void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext("classpath:applicationContext-autocrud.xml");
        generator = (IndexJspGenerator) context.getBean("indexJspGenerator");
    }

    @Test
    public void testGenerate() throws Exception {
        generator.generate();
    }
}

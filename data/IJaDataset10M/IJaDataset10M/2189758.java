package org.edemocrazy.democracy.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.edemocrazy.democracy.core.domain.Agent;
import org.edemocrazy.democracy.core.test.AbstractSpringContextTest;

public class AgentDAOTest extends AbstractSpringContextTest {

    private AgentDAO agentDAO;

    private static Map<String, Long> map = new HashMap<String, Long>();

    /**
	 * 
	 * @throws java.lang.Exception
	 */
    public void onSetUp() throws Exception {
        super.onSetUp();
        agentDAO = (AgentDAO) applicationContext.getBean("agentDAO");
    }

    /**
	 * 
	 * @throws java.lang.Exception
	 */
    public void testSave() throws Exception {
        Agent agent = new Agent();
        this.agentDAO.save(agent);
        assertFalse(agent.isNew());
        map.put("FirstAgent", agent.getId());
    }

    /**
	 * 
	 * @throws java.lang.Exception
	 */
    public void testLoad() throws Exception {
        Agent agent = agentDAO.load(map.get("FirstAgent"));
        assertNotNull(agent);
    }

    /**
	 * 
	 * @throws java.lang.Exception
	 */
    public void testDelete() throws Exception {
        Agent agent = this.agentDAO.load(map.get("FirstAgent"));
        this.agentDAO.delete(agent);
    }

    /**
	 * 
	 * @throws java.lang.Exception
	 */
    public void testGetAll() throws Exception {
        List<Agent> agents = agentDAO.getAll();
        assertEquals("Added object size", 0, agents.size());
    }
}

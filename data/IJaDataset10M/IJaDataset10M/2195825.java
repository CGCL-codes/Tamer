package net.sf.nodeInsecure.dao.computer;

import net.sf.nodeInsecure.computer.Machine;
import net.sf.nodeInsecure.computer.MachineConfiguration;
import java.util.List;

/**
 * @author: janmejay.singh
 * Date: Aug 28, 2007
 * Time: 6:08:55 PM
 */
public interface MachineDAO {

    List<MachineConfiguration> getAllConfigurations();

    Machine byId(int id);

    Machine createNewMachine(MachineConfiguration conf);

    Machine createNewMachine();
}

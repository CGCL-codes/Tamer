package remote.entities.nnodes;

import java.util.List;
import javax.persistence.OneToMany;
import remote.entities.nnodes.utils.IP6AddrEntryEnt;

/**
 * @author Kasza Miklós
 */
public interface L3Router extends L3Node, InterfaceHolder {

    public void setRouterID(String id);

    public String getRouterID();

    @OneToMany
    public List<IP6AddrEntryEnt> getIPv6AddressEntries();

    public void setIPv6AddressEntries(List<IP6AddrEntryEnt> entries);

    public void addIPv6AddressEntry(IP6AddrEntryEnt entry);

    public void removeIPv6AddressEntry(IP6AddrEntryEnt entry);
}

package net.sf.istcontract.analyserfrontend.server.serverservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import net.sf.istcontract.analyserfrontend.client.clientservice.dataBundles.ContractOverview;

/**
 *
 * @author hodik
 */
public class ContractOverviews extends HashMap<String, ContractOverview> {

    private List<String> listOfContractIDs;

    List<ContractOverview> listOfContractOverviews;

    public ContractOverviews() {
        super();
        listOfContractIDs = new ArrayList<String>();
        listOfContractOverviews = new ArrayList<ContractOverview>();
    }

    @Override
    public void clear() {
        super.clear();
        listOfContractIDs.clear();
        listOfContractOverviews.clear();
    }

    @Override
    public synchronized ContractOverview put(String key, ContractOverview value) {
        ContractOverview previousValue = super.put(key, value);
        listOfContractIDs.add(key);
        if (previousValue != null) {
            listOfContractOverviews.remove(previousValue);
        }
        listOfContractOverviews.add(value);
        return previousValue;
    }

    @Override
    public ContractOverview remove(Object key) {
        ContractOverview removedOverview = super.remove(key);
        listOfContractIDs.remove(key);
        listOfContractOverviews.remove(removedOverview);
        return removedOverview;
    }

    public synchronized List<ContractOverview> getContractOverviews() {
        return listOfContractOverviews;
    }

    public synchronized List<String> getContractIDsList() {
        return listOfContractIDs;
    }

    public synchronized ContractOverview getContractOverview(String contractID) {
        return super.get(contractID);
    }

    @Override
    public synchronized Set<Entry<String, ContractOverview>> entrySet() {
        return super.entrySet();
    }
}

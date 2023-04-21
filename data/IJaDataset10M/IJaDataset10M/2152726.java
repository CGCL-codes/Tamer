package fr.cantor.commore.tests.generator.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cgentestns.AbstractStockApiImpl;
import cgentestns.E;
import cgentestns.Item;
import fr.cantor.commore.Commore;
import fr.cantor.commore.CommoreExecutionException;
import fr.cantor.commore.CommoreInternalException;
import fr.cantor.commore.tuple.Tuple;
import fr.cantor.commore.util.Out;

public class StockApiImpl extends AbstractStockApiImpl {

    Map<String, Item> items = new HashMap<String, Item>();

    @Override
    public int Add(Item arg0) throws CommoreInternalException, CommoreExecutionException {
        System.out.println("StockApiImpl.Add()");
        Item item = items.get(arg0.getId());
        if (item != null) {
            item.setNb(item.getNb() + arg0.getNb());
        } else {
            items.put(arg0.getId(), arg0);
        }
        return Commore.CMR_OK;
    }

    @Override
    public int GetStock(List<String> arg0, List<Tuple> arg1) throws CommoreInternalException, CommoreExecutionException {
        System.out.println("StockApiImpl.GetStock()");
        System.out.println("Get Stock: " + arg0);
        for (String id : arg0) {
            Item item = items.get(id);
            if (item == null) {
                return Commore.ERR_BAD_ARGUMENT;
            } else {
                arg1.add(item.tuple());
            }
        }
        System.out.println("Return: " + arg1);
        return 0;
    }

    @Override
    public int Remove(String arg0) throws CommoreInternalException, CommoreExecutionException {
        return items.remove(arg0) == null ? Commore.ERR_BAD_ARGUMENT : Commore.CMR_OK;
    }

    @Override
    public int Sell(String arg0, Integer arg1) throws CommoreInternalException, CommoreExecutionException {
        System.out.println("StockApiImpl.Sell()");
        Item item = items.get(arg0);
        if (item == null) {
            System.err.println("Error");
            return Commore.ERR_BAD_ARGUMENT;
        }
        int nbProduct = item.getNb();
        if (nbProduct >= arg1) {
            item.setNb(nbProduct - arg1);
            System.out.println(item);
            return Commore.CMR_OK;
        }
        return Commore.ERR_BAD_ARGUMENT;
    }

    @Override
    public int WithEnum(E arg0, Out<E> arg1) throws CommoreInternalException, CommoreExecutionException {
        return 0;
    }
}
